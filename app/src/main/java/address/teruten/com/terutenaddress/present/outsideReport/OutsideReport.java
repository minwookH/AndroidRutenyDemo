package address.teruten.com.terutenaddress.present.outsideReport;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.vo.MailSelectedMember;
import address.teruten.com.terutenaddress.vo.Member;

/**
 * Created by teruten on 2017-07-21.
 */

public interface OutsideReport {

    void onSendMail(String date, String memberList, String outsideCategory
            , String company, String location, String etc, Member sender, String recipient, String purpose, String time);

    void onPreviewMail(String date, String memberList, String outsideCategory
            , String company, String location, String etc, Member sender, String recipient, String purpose, String time);

    void getMemberListString(ArrayList<MailSelectedMember> list);

    Member getSendMemberInfo(String id);

    boolean validationEmptyText(String text);

    interface View{
        void setRecipients(String memberList);
        void showPreviewMail(String text);
        void showToast(String text);
    }
}
