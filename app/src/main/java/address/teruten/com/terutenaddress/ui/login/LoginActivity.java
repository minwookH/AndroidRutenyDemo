package address.teruten.com.terutenaddress.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.wang.avi.AVLoadingIndicatorView;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.present.login.LoginPresent;
import address.teruten.com.terutenaddress.present.login.LoginPresentImpl;
import address.teruten.com.terutenaddress.ui.join.JoinActivity;
import address.teruten.com.terutenaddress.ui.main.MainActivity;
import address.teruten.com.terutenaddress.ui.password.FindPasswordActivity;
import address.teruten.com.terutenaddress.vo.Member;

public class LoginActivity extends AppCompatActivity implements LoginPresent.View, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText etLoginId;
    private EditText etLoginPw;
    private Button btnInfoSave;
    private LoginPresentImpl loginPresent;

    private StyleableToast styleableToast;

    private TextView requestId;
    private TextView findPassword;
    private CheckBox autoLogin;
    private CheckBox savaLoginId;
    private AVLoadingIndicatorView avi;
    private RelativeLayout indicatorLayout;

    private String fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("로그인");
        setSupportActionBar(toolbar);

        etLoginId = (EditText) findViewById(R.id.editText_login_id);
        etLoginPw = (EditText) findViewById(R.id.editText_login_pw);
        btnInfoSave = (Button) findViewById(R.id.button_login_save);
        btnInfoSave.setOnClickListener(this);

        loginPresent = new LoginPresentImpl(getApplicationContext(), this);

        requestId = (TextView) findViewById(R.id.request_id);
        findPassword = (TextView) findViewById(R.id.find_password);
        requestId.setOnClickListener(this);
        findPassword.setOnClickListener(this);

        autoLogin = (CheckBox) findViewById(R.id.checkBox_auto_login);
        savaLoginId = (CheckBox) findViewById(R.id.checkBox_save_id);
        autoLogin.setOnCheckedChangeListener(this);
        savaLoginId.setOnCheckedChangeListener(this);

        autoLogin.setChecked(loginPresent.isAutoLogin());
        savaLoginId.setChecked(loginPresent.isSaveLoginId());

        avi= (AVLoadingIndicatorView) findViewById(R.id.login_indicator);
        indicatorLayout = (RelativeLayout) findViewById(R.id.login_indicator_layout);

    }



    @Override
    public void showToast(String text) {
        styleableToast = new StyleableToast
                .Builder(this)
                .duration(Toast.LENGTH_SHORT)
                .text(text)
                .textColor(Color.WHITE)
                .backgroundColor(Color.parseColor("#6799FF"))
                .build();

        styleableToast.show();
        styleableToast = null;
    }

    @Override
    public void moveMainPage(Member member) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", member.getName());
        intent.putExtra("department", member.getDepartment());
        intent.putExtra("position", member.getPosition());
        startActivity(intent);
        finish();
    }

    @Override
    public void setLoginId(String id) {
        etLoginId.setText(id);
    }

    @Override
    public void showIndicator() {
        indicatorLayout.setVisibility(View.VISIBLE);
        avi.show();
    }

    @Override
    public void hideIndicator() {
        avi.hide();
        indicatorLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.button_login_save:
                if(!TextUtils.isEmpty(etLoginId.getText().toString())
                        && !TextUtils.isEmpty(etLoginPw.getText().toString())){
                    loginPresent.onLogin(etLoginId.getText().toString(), etLoginPw.getText().toString());
                }else{
                    showToast("아이디와 비밀번호를 올바르게 입력해주세요");
                }

                break;
            case R.id.request_id:
                intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);

                break;
            case R.id.find_password:
                intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);

                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
        switch (compoundButton.getId()){
            case R.id.checkBox_auto_login:
                loginPresent.setAutoLogin(flag);
                break;
            case R.id.checkBox_save_id:
                loginPresent.setSaveLoginId(flag);
                break;
        }
    }

    @Override
    public void downKeyboard() {
        InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(etLoginId.getWindowToken(), 0);
        mInputMethodManager.hideSoftInputFromWindow(etLoginPw.getWindowToken(), 0);
    }


}
