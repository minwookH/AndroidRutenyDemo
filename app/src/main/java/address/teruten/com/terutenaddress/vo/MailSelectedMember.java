package address.teruten.com.terutenaddress.vo;

/**
 * Created by teruten on 2017-07-28.
 */

public class MailSelectedMember {
    private Member member;
    private boolean isSelected;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
