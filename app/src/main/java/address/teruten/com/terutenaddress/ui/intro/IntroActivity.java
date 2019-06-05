package address.teruten.com.terutenaddress.ui.intro;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.data.Define;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.present.intro.IntroPresent;
import address.teruten.com.terutenaddress.present.intro.IntroPresentImpl;
import address.teruten.com.terutenaddress.ui.login.LoginActivity;
import address.teruten.com.terutenaddress.ui.main.MainActivity;
import address.teruten.com.terutenaddress.vo.Member;

public class IntroActivity extends AppCompatActivity implements IntroPresent.View {

    private Handler handler;
    private IntroRunnable introRunnable;
    private IntroPresentImpl introPresentImpl;

    private ImageView introImageView;

    private ProgressDialog mDlg;
    private TerutenSharedpreferences terutenSharedpreferences;
    private String fcmToken;
    private String stringExtra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        introImageView = (ImageView) findViewById(R.id.intro_image_view);

        introPresentImpl = new IntroPresentImpl(getApplicationContext(), this, this);
        introPresentImpl.onRandomIntroImage();

        mDlg = new ProgressDialog(this);
        handler = new Handler();
        introRunnable = new IntroRunnable();
        stringExtra = getIntent().getStringExtra("push");

        initFCM();
        permisstionCheck();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void initFCM(){

        FirebaseApp.initializeApp(this);

        fcmToken = FirebaseInstanceId.getInstance().getToken();
        //fcmToken = "fL9G-iMpeGM:APA91bF4PsWDL4xEStS9JOqQo1qsIFGmSpKmsbkNTgU6R44BuWbcQhdsXmA_Njc8n4jwPkFbQckSvhSOQiyzOKOn47l6HY64e3QV4-1yid6o_JNvAI6Ki-e6XQpHin7Mbf0QbN22TAr0";
        FirebaseMessaging.getInstance().subscribeToTopic("all");

        terutenSharedpreferences = new TerutenSharedpreferences(this);
        String saveFcmToken = terutenSharedpreferences.getStringPreferences("FCM_TOKEN");
        if(TextUtils.isEmpty(fcmToken) || !TextUtils.equals(fcmToken, saveFcmToken)){
            terutenSharedpreferences.setStringPreferences("FCM_TOKEN", this.fcmToken);
        }
    }

    public void permisstionCheck(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            introPresentImpl.versionCheck();
        } else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED
                    || grantResults[1] == PackageManager.PERMISSION_GRANTED){
                introPresentImpl.versionCheck();
            } else{
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void moveMainPage(Member member) {
        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
        intent.putExtra("name", member.getName());
        intent.putExtra("department", member.getDepartment());
        intent.putExtra("position", member.getPosition());
        if(TextUtils.equals("mail", stringExtra)){
            intent.putExtra("push",true);
        }

        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void moveLoginPage() {
        Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void setIntroImage(HashMap<String, Integer> imageRandom) {
        Glide.with(getApplicationContext())
                .load(imageRandom.get("id"))
                .apply(new RequestOptions().transform(new CenterCrop()))
                .into(introImageView);
    }

    @Override
    public void showDialog(final int flag, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (flag){
            case Define.NETWORK_ERROR_TIMEOUT:
                builder.setTitle(getString(R.string.error_intranet_server_timeout));
                break;
            case Define.DIALOG_VERSION_UPDATE:
                builder.setTitle(getString(R.string.version_check_dialog_title));
                break;
            case Define.NETWORK_ERROR_ETC:
                builder.setTitle(getString(R.string.error_intranet_server_timeout));
                break;
            default:
                break;
        }

        builder.setMessage(text);
        builder.setCancelable(false);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (flag){
                            case Define.NETWORK_ERROR_TIMEOUT:
                                IntroActivity.this.finish();
                                break;
                            case Define.DIALOG_VERSION_UPDATE:
                                introPresentImpl.apkDownload();
                                break;
                            default:
                                break;
                        }

                    }
                });
        if(flag != Define.NETWORK_ERROR_TIMEOUT){
            builder.setNegativeButton("취소"
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startInterval();
                        }
                    });
        }

        builder.show();
    }

    public class IntroRunnable implements Runnable {

        @Override
        public void run() {
            introPresentImpl.onInfoCheck();
        }
    }

    @Override
    public void startProgress() {
        //스타일 설정
        mDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //프로그래스 다이얼로그 나올 때 메시지 설정.
        mDlg.setMessage("파일 다운로드 중...");
        //세팅된 다이얼로그를 보여줌.
        mDlg.show();
    }

    @Override
    public void stopProgress() {
        mDlg.dismiss();
    }

    @Override
    public void startInterval() {

        handler.postDelayed(introRunnable, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("LoginGet", "requestCode : "+requestCode+" , resultCode : "+resultCode);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 110:
                    startInterval();
                    break;
                default:
                    break;
            }
        }
    }
}
