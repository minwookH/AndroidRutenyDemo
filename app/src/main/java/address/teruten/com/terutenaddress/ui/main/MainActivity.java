package address.teruten.com.terutenaddress.ui.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.Calendar;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.present.main.Main;
import address.teruten.com.terutenaddress.present.main.MainImpl;
import address.teruten.com.terutenaddress.receiver.CafeterialAlarmReceiver;
import address.teruten.com.terutenaddress.ui.address.AddressFragment;
import address.teruten.com.terutenaddress.ui.board.BoardFragment;
import address.teruten.com.terutenaddress.ui.mail.MailFragment;
import address.teruten.com.terutenaddress.ui.setting.SettingFragment;

public class MainActivity extends AppCompatActivity
        implements AddressFragment.OnFragmentInteractionListener, MailFragment.OnFragmentInteractionListener
                , SettingFragment.OnFragmentInteractionListener, BoardFragment.OnFragmentInteractionListener
                , TextView.OnEditorActionListener, Main.View{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView tvUrlView;
    private MainImpl mainImpl;

    private StyleableToast styleableToast;
    private MainViewPagerAdapter mainViewPagerAdapter;

    private static final int TAB_ORGANIZATION_INDEX = 0;
    private static final int TAB_MAIL_INDEX = 1;
    private static final int TAB_BOARD_INDEX = 2;
    private static final int TAB_ETC_INDEX = 3;

    private TerutenSharedpreferences terutenSharedpreferences;
    private String fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String name = getIntent().getStringExtra("name");
        String department = getIntent().getStringExtra("department");
        String position = getIntent().getStringExtra("position");
        boolean isPushStart = getIntent().getBooleanExtra("push", false);
        Log.i("FCM", "main isPushStart :  "+isPushStart);
        tabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        viewPager = (ViewPager) findViewById(R.id.main_view_pager);

        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());

        mainViewPagerAdapter.addFragment(R.drawable.tab_organization_selector, "조직도", AddressFragment.newInstance(null, null));
        mainViewPagerAdapter.addFragment(R.drawable.tab_mail_selector, "메일", MailFragment.newInstance(null, null));
        mainViewPagerAdapter.addFragment(R.drawable.tab_board_selector, "게시판", BoardFragment.newInstance(null, null));
        mainViewPagerAdapter.addFragment(R.drawable.tab_setting_selector, "설정", SettingFragment.newInstance(null, null));

        viewPager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < viewPager.getAdapter().getCount(); i++) {
            tabLayout.getTabAt(i).setIcon(mainViewPagerAdapter.getFragmentInfo(i).getIconResId());
        }

        if(isPushStart){
            viewPager.setCurrentItem(1);
        }

        tvUrlView = (TextView) findViewById(R.id.tv_url_input);
        tvUrlView.setOnEditorActionListener(this);
        tvUrlView.setText(department+" "+name+position);

        mainImpl = new MainImpl(this, this);

        initFCM();
    }



    public void initFCM(){
        FirebaseApp.initializeApp(this);

        fcmToken = FirebaseInstanceId.getInstance().getToken();
        //fcmToken = "fL9G-iMpeGM:APA91bF4PsWDL4xEStS9JOqQo1qsIFGmSpKmsbkNTgU6R44BuWbcQhdsXmA_Njc8n4jwPkFbQckSvhSOQiyzOKOn47l6HY64e3QV4-1yid6o_JNvAI6Ki-e6XQpHin7Mbf0QbN22TAr0";
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        terutenSharedpreferences = new TerutenSharedpreferences(this);
        String saveFcmToken = terutenSharedpreferences.getStringPreferences("FCM_TOKEN");
        if(TextUtils.isEmpty(fcmToken) || !TextUtils.equals(fcmToken, saveFcmToken)){
            terutenSharedpreferences.setStringPreferences("FCM_TOKEN", this.fcmToken);
        }
    }

    public void setCafeterialNotificationAlarm(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 45);

        Intent intent = new Intent(this, CafeterialAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 11, intent, 0);

        AlarmManager alarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), System.currentTimeMillis(), pendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void goMailWrite(String recipient) {
        //메일 탭 이동
        runOnUiThread(new Runnable() { public void run() { // 메시지 큐에 저장될 메시지의 내용
                goMailTab();
            }
        });

        MailFragment fragment = (MailFragment)mainViewPagerAdapter.getFragmentInfo(TAB_MAIL_INDEX).getFragment();
        fragment.setFromMail(recipient);
        fragment.onMailSendPage();
    }

    @Override
    public void goBoardTab() {
        viewPager.setCurrentItem(TAB_BOARD_INDEX);
    }

    @Override
    public void goMailTab() {
        viewPager.setCurrentItem(TAB_MAIL_INDEX);
    }

    @Override
    public void onBackPressed() {
        mainImpl.onBackbutton();
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void showToast(String text) {
        styleableToast = new StyleableToast
                .Builder(this)
                .duration(Toast.LENGTH_SHORT)
                .text(text)
                .textColor(Color.WHITE)
                .backgroundColor(Color.parseColor("#747474"))
                .build();

        styleableToast.show();
        styleableToast = null;
    }

    @Override
    public void actvityFinish() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MailFragment mailFragment = (MailFragment)mainViewPagerAdapter.getFragmentInfo(TAB_MAIL_INDEX).getFragment();
        BoardFragment boardFragment = (BoardFragment)mainViewPagerAdapter.getFragmentInfo(TAB_BOARD_INDEX).getFragment();

        if (requestCode == MailFragment.FILECHOOSER_RESULTCODE) {
            mailFragment.onWebMailFileUploadKitkat(resultCode, data);
        } else if (requestCode == MailFragment.FILECHOOSER_LOLLIPOP_REQ_CODE) {
            mailFragment.onWebMailFileUpload(resultCode, data);
        } else if (requestCode == BoardFragment.BOARD_FILECHOOSER_RESULTCODE) {
            boardFragment.onWebBoardFileUploadKitkat(resultCode, data);
        } else if (requestCode == BoardFragment.BOARD_FILECHOOSER_LOLLIPOP_REQ_CODE) {
            boardFragment.onWebBoardFileUpload(resultCode, data);
        }
    }
}
