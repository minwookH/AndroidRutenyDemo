package address.teruten.com.terutenaddress.FCM;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;

import address.teruten.com.terutenaddress.data.Define;
import address.teruten.com.terutenaddress.network.IntranetService;
import address.teruten.com.terutenaddress.network.NetworkManager;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.vo.response.ResponseFCMKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by teruten on 2018-01-09.
 */

public class TerutenFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private TerutenSharedpreferences terutenSharedpreferences;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        terutenSharedpreferences = new TerutenSharedpreferences(getApplicationContext());
        Log.d("FCM", "Refreshed token: " + refreshedToken);

        String fcm_token = terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.FCM_TOKEN);

        if(!TextUtils.equals(refreshedToken,fcm_token )){
            requestSendFcmToken(fcm_token);
        }

        terutenSharedpreferences.setStringPreferences(TerutenSharedpreferences.FCM_TOKEN, refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(refreshedToken);
    }

    public void requestSendFcmToken(String token){
        String userId = terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_ID);

        if(!TextUtils.isEmpty(userId)){
            IntranetService intranetService = NetworkManager.getIntance().getRetrofit(IntranetService.class, Define.INTRANET_DOMAIN_BASE_URL);
            Call<ResponseFCMKey> responseFCMKeyCall = intranetService.requestSendFcmKey(userId, token, "android");
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
        }
    }

    public TerutenFirebaseInstanceIDService() {
        super();
        Log.d("FCM", "TerutenFirebaseInstanceIDService START ");
    }
}
