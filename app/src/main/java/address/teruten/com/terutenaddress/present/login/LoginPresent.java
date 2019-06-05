package address.teruten.com.terutenaddress.present.login;

import address.teruten.com.terutenaddress.vo.Member;

/**
 * Created by teruten on 2017-06-22.
 */

public interface LoginPresent {

    void onLogin(String id, String password);
    boolean isEmailVaildation(String id);
    boolean isPasswordVaildation(String pw);
    void setAutoLogin(boolean flag);
    void setSaveLoginId(boolean flag);
    boolean isAutoLogin();
    boolean isSaveLoginId();

    interface View{
        void showToast(String text);
        void moveMainPage(Member member);
        void setLoginId(String id);
        void showIndicator();
        void hideIndicator();
        void downKeyboard();
    }
}
