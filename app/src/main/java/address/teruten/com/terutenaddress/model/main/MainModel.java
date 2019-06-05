package address.teruten.com.terutenaddress.model.main;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import address.teruten.com.terutenaddress.data.Define;
import address.teruten.com.terutenaddress.network.IntranetService;
import address.teruten.com.terutenaddress.network.NetworkManager;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.vo.response.ResponseFCMKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by teruten on 2017-07-28.
 */

public class MainModel {

    private long backKeyPressedTime = 0;
    private TerutenSharedpreferences terutenSharedpreferences;

    public boolean onBackPressed() {
        boolean flag = false;

        Log.d("backkey", "time 1: "+System.currentTimeMillis());
        Log.d("backkey", "time 2: "+(backKeyPressedTime + 2000));
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Log.d("backkey", "time 1 start");
            flag = false;
        }else {
            Log.d("backkey", "time 2 start ");
            /*Intent t = new Intent(activity, MainActivity.class);
            activity.startActivity(t);

            activity.moveTaskToBack(true);
            activity.finish();*/
            flag = true;
        }

        return flag;
    }


    /*public void requestSendFcmToken(){
        String saveFcmToken = terutenSharedpreferences.getStringPreferences("FCM_TOKEN");
        Log.i("FCM", "token : "+saveFcmToken+", id : "+id);
        if(TextUtils.isEmpty(saveFcmToken)){
            saveFcmToken = FirebaseInstanceId.getInstance().getToken();
            Log.i("FCM", "saveFcmToken : "+saveFcmToken);
            terutenSharedpreferences.setStringPreferences("FCM_TOKEN", saveFcmToken);
        }

        IntranetService intranetService = NetworkManager.getIntance().getRetrofit(IntranetService.class, Define.INTRANET_DOMAIN_BASE_URL);
        Call<ResponseFCMKey> responseFCMKeyCall = intranetService.requestSendFcmKey(id, saveFcmToken);
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
                Log.e("FCM", "loginCall requestIntranetLogin error : "+t.getMessage());
            }
        });
    }*/
}
