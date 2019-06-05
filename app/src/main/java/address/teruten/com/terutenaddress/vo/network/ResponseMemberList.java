package address.teruten.com.terutenaddress.vo.network;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.vo.Member;

/**
 * Created by teruten on 2017-07-18.
 */

public class ResponseMemberList {
    private String result;
    private ArrayList<Member> member;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Member> getMember() {
        return member;
    }

    public void setMember(ArrayList<Member> member) {
        this.member = member;
    }



}
