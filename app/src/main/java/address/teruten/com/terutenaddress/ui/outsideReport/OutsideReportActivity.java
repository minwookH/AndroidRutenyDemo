package address.teruten.com.terutenaddress.ui.outsideReport;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.mailLib.TerutenMailSender;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.present.outsideReport.OutsideReport;
import address.teruten.com.terutenaddress.present.outsideReport.OutsideReportImpl;
import address.teruten.com.terutenaddress.ui.setting.DialogFragment;
import address.teruten.com.terutenaddress.vo.MailSelectedMember;
import address.teruten.com.terutenaddress.vo.Member;

public class OutsideReportActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, View.OnClickListener
                , DatePicker.OnDateChangedListener, DialogFragment.OnCallBackListener, OutsideReport.View {

    private Spinner mailSpinner;
    private Spinner categorySpinner;
    private EditText etCompanyName, etLocation, etEtc, etPurpose;
    private TextView tvPerson, tvTime;
    private DatePicker datePicker;
    private TerutenSharedpreferences terutenSharedpreferences;
    private Button mailSendButton, mailPreviewButton;
    private OutsideReportImpl outsideReportImpl;

    private String recipientList;
    private String date;

    private ArrayList<MailSelectedMember> mailSelectedMembers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_report);

        terutenSharedpreferences = new TerutenSharedpreferences(getApplicationContext());

        mailSpinner = (Spinner) findViewById(R.id.receive_person_spinner);
        categorySpinner = (Spinner) findViewById(R.id.outside_category_spinner);
        tvPerson = (TextView) findViewById(R.id.select_name_text);
        tvTime = (TextView) findViewById(R.id.time_text_view);
        tvTime.setOnClickListener(this);
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        etEtc = (EditText) findViewById(R.id.etc_edit_text);
        etCompanyName = (EditText) findViewById(R.id.company_name_edit_text);
        etLocation = (EditText) findViewById(R.id.location_edit_text);
        etPurpose = (EditText) findViewById(R.id.purpose_edit_text);

        final ArrayList<String> list = new ArrayList<>();
        list.add(terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_ID)+"@teruten.com");
        list.add("tall@teruten.com");
        list.add("tm@teruten.com");

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list);
        mailSpinner.setAdapter(spinnerAdapter);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource
                (this, R.array.category_list, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        mailSpinner.setOnItemSelectedListener(this);
        categorySpinner.setOnItemSelectedListener(this);
        tvPerson.setOnClickListener(this);

        datePicker.init(datePicker.getYear(),datePicker.getMonth(), datePicker.getDayOfMonth(), this);
        date = datePicker.getYear()+"년 "+(datePicker.getMonth()+1)+"월 "+datePicker.getDayOfMonth()+"일";

        mailSendButton = (Button) findViewById(R.id.mail_send_button);
        mailSendButton.setOnClickListener(this);
        mailPreviewButton = (Button) findViewById(R.id.mail_preview_button);
        mailPreviewButton.setOnClickListener(this);

        outsideReportImpl = new OutsideReportImpl(getApplicationContext(), this);
    }

    private String recipient;
    private String outsideCategory;
    private Member senderMember;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d("spinnerItem", "onItemSelected start");
        Log.d("spinnerItem", "onItemSelected id : "+id);
        Log.d("spinnerItem", "onItemSelected position : "+position);
        Log.d("spinnerItem", "onItemSelected person : "+R.id.receive_person_spinner);
        Log.d("spinnerItem", "onItemSelected category : "+R.id.outside_category_spinner);
        switch (adapterView.getId()){
            case R.id.receive_person_spinner:
                Log.d("spinnerItem", "receive_person_spinner start");
                recipient = (String) adapterView.getItemAtPosition(position);
                Log.d("spinnerItem", "receive_person_spinner recipient : "+recipient);
                break;
            case R.id.outside_category_spinner:
                Log.d("spinnerItem", "outside_category_spinner start");
                outsideCategory = (String) adapterView.getItemAtPosition(position);
                Log.d("spinnerItem", "receive_person_spinner outsideCategory : "+outsideCategory);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.select_name_text:
                Log.d("spinnerItem", "receive_person_text start");
                DialogFragment dialogFragment = new DialogFragment();
                dialogFragment.setOnCallBackListener(this);
                dialogFragment.setSelectedMemberList(mailSelectedMembers);
                dialogFragment.show(getSupportFragmentManager(), "dialog");
                break;
            case R.id.mail_send_button:

                Log.d("spinnerItem", "send mail start");
                String userId = terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_ID);

                senderMember = outsideReportImpl.getSendMemberInfo(userId);

                if(outsideReportImpl.validationEmptyText(etCompanyName.getText().toString())){
                    Toast.makeText(this, "업체명이 없습니다", Toast.LENGTH_SHORT).show();
                }else if(outsideReportImpl.validationEmptyText(etLocation.getText().toString())){
                    Toast.makeText(this, "장소가 없습니다", Toast.LENGTH_SHORT).show();
                }else if(outsideReportImpl.validationEmptyText(recipientList)){
                    Toast.makeText(this, "이름이 없습니다", Toast.LENGTH_SHORT).show();
                }else if(outsideReportImpl.validationEmptyText(etPurpose.getText().toString())){
                    Toast.makeText(this, "목적이 없습니다", Toast.LENGTH_SHORT).show();
                }else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("메일을 보내시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            outsideReportImpl.onSendMail(date ,recipientList, outsideCategory, etCompanyName.getText().toString()
                                    ,etLocation.getText().toString(), etEtc.getText().toString()
                                    , senderMember, recipient, etPurpose.getText().toString(), tvTime.getText().toString());

                            finish();
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create().show();
                }


                break;
            case R.id.mail_preview_button:
                String userId1 = terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_ID);

                Member senderMember1 = outsideReportImpl.getSendMemberInfo(userId1);
                outsideReportImpl.onPreviewMail(date ,recipientList, outsideCategory, etCompanyName.getText().toString()
                        ,etLocation.getText().toString(), etEtc.getText().toString(), senderMember1, recipient, etPurpose.getText().toString(), tvTime.getText().toString());
                break;
            case R.id.time_text_view:
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if(hourOfDay > 12){
                                    tvTime.setText("오후 "+ (hourOfDay-12) + "시 " + minute+"분");
                                }else if(hourOfDay == 12){
                                    tvTime.setText("오후 "+hourOfDay + "시 " + minute+"분");
                                }else{
                                    tvTime.setText("오전 "+hourOfDay + "시 " + minute+"분");
                                }


                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

                break;
        }
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        Log.i("spinnerItem", "onDateChanged : "+year+"/"+(month+1)+"/"+day);
        date = year+"년 "+(month+1)+"월 "+day+"일";
    }

    @Override
    public void onResultList(ArrayList<MailSelectedMember> list) {
        Log.i("spinnerItem", "actvity onResultList start");
        outsideReportImpl.getMemberListString(list);
        mailSelectedMembers = list;
    }

    @Override
    public void setRecipients(String memberList) {
        recipientList = memberList;
        tvPerson.setText(recipientList);
    }

    @Override
    public void showPreviewMail(String text) {
        Log.i("spinnerItem", "showPreviewMail start : "+text);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
