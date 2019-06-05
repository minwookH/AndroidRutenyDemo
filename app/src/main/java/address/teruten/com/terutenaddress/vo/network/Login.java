package address.teruten.com.terutenaddress.vo.network;

import address.teruten.com.terutenaddress.vo.Member;

/**
 * Created by teruten on 2017-07-14.
 */

public class Login {
    private String result;
    private Member member;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
