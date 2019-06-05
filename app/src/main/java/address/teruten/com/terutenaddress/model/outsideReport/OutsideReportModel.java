package address.teruten.com.terutenaddress.model.outsideReport;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.data.Define;
import address.teruten.com.terutenaddress.database.DbSQLiteOpenHelper;
import address.teruten.com.terutenaddress.mailLib.TerutenMailSender;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.vo.Member;

/**
 * Created by teruten on 2017-07-21.
 */

public class OutsideReportModel {

    private TerutenSharedpreferences terutenSharedpreferences;
    private Context context;
    private DbSQLiteOpenHelper dbSQLiteOpenHelper;

    public OutsideReportModel(Context context){
        this.context = context;

        terutenSharedpreferences = new TerutenSharedpreferences(context);
        dbSQLiteOpenHelper = new DbSQLiteOpenHelper(context, Define.DB_NAME, null, Define.DB_VERSION);
    }

    public String makeMailSubject(String memberList, String outsideCategory){

        String subject = "["+outsideCategory+"보고] "+memberList;
        return subject;
    }

    public String makeMailContents(String date, String outsideCategory
            , String company, String location, String etc, Member sender, String purpose, String time){

        String senderInfo = "작성자 : "+sender.getName()+" "+sender.getPosition()+"("+sender.getInnerTel()+")"
                + " / "+sender.getTel1()+"-"+sender.getTel2()+"-"+sender.getTel3()+"\n\n";

        String contents = null;
        if(TextUtils.isEmpty(etc)){
            contents = date+" "+time+" "+purpose+"(으)로"+" \'"+location+"\'의 "+"\'"+company+"\' (으)로 "+outsideCategory+"입니다.";
        }else{
            contents = date+" "+time+" "+purpose+"(으)로"+" \'"+location+"\'의 "+"\'"+company+"\' (으)로 "+outsideCategory+"입니다.\n\n"
                        +etc;
        }

        contents = contents + "\n\n급하신 용무는 휴대전화로 부탁 드립니다.\n" +
                "감사합니다.\n\n" +senderInfo +
                "(RUTENY앱에서 작성)";



        /*-제목
                [직출보고] 심재능선임, 허민욱전임

                -내용
        2018-01-05 '이유'으로 '장소'의 '회사명'로 직출입니다.

        (추가 내용 있을시)

        급하신 용무는 휴대전화로 부탁 드립니다.

                감사합니다.

        (RUTENY앱에서 작성)

        작성자 :  허민욱전임(315) / 010-6413-9370*/

        return contents;
    }

    public boolean sendMail(String subject, String contents , Member sendMember, String recipient){
        boolean isSuccess = true;
        Log.d("spinnerItem", "send mail start");
        String userId = terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_ID);
        String userPw = terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_PW);

        TerutenMailSender sender = new TerutenMailSender(userId+"@teruten.com", userPw);

        Log.d("sendMail", "subject : "+subject);
        Log.d("sendMail", "contents : "+contents);
        Log.d("sendMail", "보내는 사람 : "+sendMember.getId()+"@teruten.com");
        Log.d("sendMail", "보내는 사람 이름 : "+sendMember.getName());
        Log.d("sendMail", "받는 사람 : "+recipient);
        try {
            //sender.sendMail("제목111", "내용111" , userId+"@teruten.com", recipient);
            sender.sendMail(subject, contents , sendMember.getId()+"@teruten.com", sendMember.getName(), recipient);
            isSuccess = true;
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
            isSuccess = false;
        }

        return isSuccess;
    }

    public Member selectMember(String id){
        Member selectMember = dbSQLiteOpenHelper.selectItem(id);
        return selectMember;
    }

    public boolean isEmptyText(String text){
        return TextUtils.isEmpty(text);
    }
}
