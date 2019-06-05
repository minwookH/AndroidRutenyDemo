package address.teruten.com.terutenaddress.present.main;

import android.app.Activity;
import android.util.Log;

import address.teruten.com.terutenaddress.model.main.MainModel;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.util.TerutenUtil;

/**
 * Created by teruten on 2017-07-28.
 */

public class MainImpl implements Main{

    private Main.View view;
    private Activity activity;
    private MainModel mainModel;
    private TerutenSharedpreferences terutenSharedpreferences;

    public MainImpl(Activity activity, Main.View view){
        this.activity = activity;
        this.view = view;
        mainModel = new MainModel();
        terutenSharedpreferences = new TerutenSharedpreferences(activity);
    }

    @Override
    public void onBackbutton() {
        if (mainModel.onBackPressed()){
            TerutenUtil.webViewCookieClear();
            view.actvityFinish();
        }else{
            view.showToast("\'뒤로\'버튼 한번 더 누르시면 종료됩니다");
        }
    }

    @Override
    public void uploadFCMToken() {
    }
}
