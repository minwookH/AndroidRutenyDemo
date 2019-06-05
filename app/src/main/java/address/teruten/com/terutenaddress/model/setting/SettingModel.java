package address.teruten.com.terutenaddress.model.setting;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;

import com.google.firebase.iid.FirebaseInstanceId;

import address.teruten.com.terutenaddress.data.Define;
import address.teruten.com.terutenaddress.database.DbSQLiteOpenHelper;
import address.teruten.com.terutenaddress.network.IntranetService;
import address.teruten.com.terutenaddress.network.NetworkManager;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.vo.Member;
import address.teruten.com.terutenaddress.vo.response.ResponseFCMKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by teruten on 2017-06-23.
 */

public class SettingModel {

    private Context context;
    private TerutenSharedpreferences terutenSharedpreferences;
    private DbSQLiteOpenHelper dbSQLiteOpenHelper;

    public SettingModel(Context context){
        this.context = context;
        terutenSharedpreferences = new TerutenSharedpreferences(context);
        dbSQLiteOpenHelper = new DbSQLiteOpenHelper(context, Define.DB_NAME, null, Define.DB_VERSION);
    }

    public void deleteUserInfo(){
        //terutenSharedpreferences.setStringPreferences(TerutenSharedpreferences.USER_ID, "");
        terutenSharedpreferences.setStringPreferences(TerutenSharedpreferences.USER_PW, "");

        terutenSharedpreferences.setBooleanPreferences(TerutenSharedpreferences.AUTO_LOGIN, false);
    }

    public String getPreferencesUserId(){
        return terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_ID);
    }

    public Member selectMember(String id){
        Member selectMember = dbSQLiteOpenHelper.selectItem(id);
        return selectMember;
    }

    public void webViewCookieClear(){
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                    Log.d("sdfsdf", "removeSessionCookies aBoolean : " + aBoolean);
                }
            });
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                    Log.d("sdfsdf", "removeAllCookies aBoolean : " + aBoolean);
                }
            });
        }else{
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
        }
    }

    public void requestDeleteFcmToken(){
        String id = getPreferencesUserId();
        Log.i("FCM", ",requestDeleteFcmToken id : "+id);

        IntranetService intranetService = NetworkManager.getIntance().getRetrofit(IntranetService.class, Define.INTRANET_DOMAIN_BASE_URL);
        Call<ResponseFCMKey> responseFCMKeyCall = intranetService.requestDeleteFcmToken(id);
        responseFCMKeyCall.enqueue(new Callback<ResponseFCMKey>() {
            @Override
            public void onResponse(Call<ResponseFCMKey> call, Response<ResponseFCMKey> response) {
                Log.d("FCM", "isSuccessful : "+response.isSuccessful());
                if(response.isSuccessful()){
                    ResponseFCMKey responseFCMKey = response.body();
                    Log.d("FCM", "result : "+responseFCMKey.getResult()+", message : "+responseFCMKey.getMsg());
                }

            }

            @Override
            public void onFailure(Call<ResponseFCMKey> call, Throwable t) {
                Log.e("FCM", "requestDeleteFcmToken error : "+t.getMessage());
            }
        });
    }
}
