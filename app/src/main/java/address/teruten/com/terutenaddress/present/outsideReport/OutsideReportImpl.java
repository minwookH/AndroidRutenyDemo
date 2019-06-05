package address.teruten.com.terutenaddress.present.outsideReport;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.model.outsideReport.OutsideReportModel;
import address.teruten.com.terutenaddress.vo.MailSelectedMember;
import address.teruten.com.terutenaddress.vo.Member;

/**
 * Created by teruten on 2017-07-21.
 */

public class OutsideReportImpl implements OutsideReport {

    private OutsideReportModel outsideReportModel;
    private OutsideReport.View view;

    public OutsideReportImpl(Context context, OutsideReport.View view){
        outsideReportModel = new OutsideReportModel(context);
        this.view = view;
    }

    @Override
    public void onSendMail(String date, String memberList, String outsideCategory, String company
            , String location, String etc, Member sender, String recipient, String purpose, String time) {
        String subject = outsideReportModel.makeMailSubject(memberList, outsideCategory);
        String contents = outsideReportModel.makeMailContents(date, outsideCategory, company, location, etc, sender, purpose, time);
        boolean isSuccess = outsideReportModel.sendMail(subject, contents, sender, recipient);

        if(isSuccess){
            view.showToast("메일 보내기 성공했습니다");
        }else{
            view.showToast("메일 보내기 실패했습니다\n잠시후 다시 시도해주세요");
        }
    }

    @Override
    public void onPreviewMail(String date, String memberList, String outsideCategory, String company
            , String location, String etc, Member sender, String recipient, String purpose, String time) {
        String subject = outsideReportModel.makeMailSubject(memberList, outsideCategory);
        String contents = outsideReportModel.makeMailContents(date, outsideCategory, company, location, etc, sender, purpose, time);

        String senderText = "보내는 사람 : "+sender.getId()+"@teruten.com";
        String recipientText = "받는 사람 : "+recipient;

        String subjectText = "제목 : "+ subject;
        String contentsText = "내용 : "+ contents;

        view.showPreviewMail(senderText+"\n"+recipientText+"\n"+subjectText+"\n"+contentsText);
    }

    @Override
    public void getMemberListString(ArrayList<MailSelectedMember> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i =0; i < list.size(); i++){
            Log.i("spinnerItem", "list : "+list.get(i).getMember().getName());

            if(!list.get(i).isSelected()){
                continue;
            }

            stringBuilder.append(list.get(i).getMember().getName()+" ");

        }

        view.setRecipients(stringBuilder.toString());
    }

    @Override
    public Member getSendMemberInfo(String id) {
        return outsideReportModel.selectMember(id);
    }

    @Override
    public boolean validationEmptyText(String text) {
        return outsideReportModel.isEmptyText(text);
    }
}
