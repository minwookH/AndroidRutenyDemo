package address.teruten.com.terutenaddress.model.login;

import address.teruten.com.terutenaddress.vo.Member;


public interface LoginCallBack {
    void getNetworkResponse(Member member, int status);
}
