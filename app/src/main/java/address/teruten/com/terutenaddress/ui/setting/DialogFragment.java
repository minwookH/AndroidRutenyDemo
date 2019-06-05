package address.teruten.com.terutenaddress.ui.setting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.data.Define;
import address.teruten.com.terutenaddress.database.DbSQLiteOpenHelper;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.vo.MailSelectedMember;
import address.teruten.com.terutenaddress.vo.Member;

/**
 * Created by teruten on 2017-07-17.
 */

public class DialogFragment extends android.support.v4.app.DialogFragment {

    private DbSQLiteOpenHelper dbSQLiteOpenHelper;
    private LinearLayoutManager mLayoutManager;
    private MemberListAdapter memberListAdapter;

    private OnCallBackListener onCallBackListener;
    private ArrayList<MailSelectedMember> selectedMembers;

    private TerutenSharedpreferences terutenSharedpreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_member_list);

        terutenSharedpreferences = new TerutenSharedpreferences(getContext());
        dbSQLiteOpenHelper = new DbSQLiteOpenHelper(getContext(), Define.DB_NAME, null, Define.DB_VERSION);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_member_list, null);

        RecyclerView memberList = (RecyclerView) view.findViewById(R.id.dialog_member_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        memberList.setLayoutManager(mLayoutManager);
        memberListAdapter = new MemberListAdapter();
        memberList.setAdapter(memberListAdapter);

        //memberListAdapter.addMemberList(dbSQLiteOpenHelper.getMemberList());

        if(selectedMembers == null || selectedMembers.size() <= 0){
            ArrayList<Member> members = dbSQLiteOpenHelper.getMemberList();
            selectedMembers = new ArrayList<>();
            String userId = terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_ID);
            for (Member member : members){

                MailSelectedMember mailSelectedMember = new MailSelectedMember();
                mailSelectedMember.setMember(member);

                if(TextUtils.equals(userId, member.getId())){
                    mailSelectedMember.setSelected(true);
                }else {
                    mailSelectedMember.setSelected(false);
                }


                selectedMembers.add(mailSelectedMember);
            }
        }

        memberListAdapter.addMemberList(selectedMembers);

        Log.d("spinnerItem", "list count : "+ memberListAdapter.getItemCount());
        memberListAdapter.notifyDataSetChanged();
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(getContext(), new LinearLayoutManager(getContext()).getOrientation());
        memberList.addItemDecoration(dividerItemDecoration);
        memberList.addItemDecoration(new VerticalSpaceItemDecoration(1));

        builder.setView(view)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        onCallBackListener.onResultList(memberListAdapter.getCheckedList());
                    }
                })
                .setNegativeButton("취소", null);
        return builder.create();
    }

    public void setOnCallBackListener(OnCallBackListener onCallBackListener){
        this.onCallBackListener = onCallBackListener;
    }

    public interface OnCallBackListener {
        void onResultList(ArrayList<MailSelectedMember> list);
    }

    public void setSelectedMemberList(ArrayList<MailSelectedMember> list){
        selectedMembers = list;
    }

}
