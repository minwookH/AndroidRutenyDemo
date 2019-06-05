package address.teruten.com.terutenaddress.present.login;

import android.content.Context;

import address.teruten.com.terutenaddress.model.login.LoginCallBack;
import address.teruten.com.terutenaddress.model.login.LoginModel;
import address.teruten.com.terutenaddress.vo.Member;

/**
 * Created by teruten on 2017-06-22.
 */

public class LoginPresentImpl implements LoginPresent, LoginCallBack{

    private LoginModel loginModel;
    private LoginPresent.View view;

    public LoginPresentImpl(Context context, LoginPresent.View view){
        loginModel = new LoginModel(context, this);
        this.view = view;
    }

    @Override
    public void onLogin(String id, String password) {
        view.downKeyboard();
        view.showIndicator();
        loginModel.requestMailPlugLogin(id, password);
    }

    @Override
    public boolean isEmailVaildation(String id) {
        boolean result = false;
        if(loginModel.isEmailVaildation(id)){
            result = true;
        }else {
            view.showToast("이메일 형식이 맞지 않습니다.");
        }

        return result;
    }

    @Override
    public boolean isPasswordVaildation(String pw) {
        boolean result = false;
        if(loginModel.isPasswordVaildation(pw)){
            result = true;
        }else {
            view.showToast("비밀번호 형식이 맞지 않습니다.");
        }

        return result;
    }

    @Override
    public void setAutoLogin(boolean flag) {
        loginModel.setAutoLoginPreferences(flag);
    }

    @Override
    public void setSaveLoginId(boolean flag) {
        loginModel.setSaveLoginIdPreferences(flag);
    }

    @Override
    public boolean isAutoLogin() {
        return loginModel.isAutoLoginPreferences();
    }

    @Override
    public boolean isSaveLoginId() {
        if(loginModel.isSaveLoginIdPreferences()){
            view.setLoginId(loginModel.getLoginId());
        }
        return loginModel.isSaveLoginIdPreferences();
    }

    @Override
    public void getNetworkResponse(Member member, int status) {
        if(status == 410){
            view.showToast("아이디와 비밀번호가 일치하지 않습니다");
            view.hideIndicator();
        }else if(status == 420){
            //view.showToast("로그인 성공");
            view.showToast("인트라넷 서버 연결이 실패하였습니다");
            view.hideIndicator();
            view.moveMainPage(loginModel.getLoginUserInfo());
        }else if(status == 200){
            //view.showToast("로그인 성공");
            view.hideIndicator();
            view.moveMainPage(member);
        }else if(status == 450){
            view.showToast("인트라넷 서버의 연결 시간이 초과하였습니다");
            view.hideIndicator();
        }
    }
}
