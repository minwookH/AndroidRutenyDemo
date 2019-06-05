package address.teruten.com.terutenaddress.model.login;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import address.teruten.com.terutenaddress.data.Define;
import address.teruten.com.terutenaddress.database.DbSQLiteOpenHelper;
import address.teruten.com.terutenaddress.network.IntranetService;
import address.teruten.com.terutenaddress.network.NetworkManager;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.vo.Member;
import address.teruten.com.terutenaddress.vo.network.Login;
import address.teruten.com.terutenaddress.vo.network.ResponseMemberList;
import address.teruten.com.terutenaddress.vo.response.ResponseFCMKey;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by teruten on 2017-06-22.
 */

public class LoginModel {

    private Context context;
    private TerutenSharedpreferences terutenSharedpreferences;
    // 이메일정규식
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private String id;
    private String password;
    private LoginCallBack loginCallBack;

    private DbSQLiteOpenHelper dbSQLiteOpenHelper;

    public LoginModel(Context context, LoginCallBack loginCallBack){
        this.context = context;
        terutenSharedpreferences = new TerutenSharedpreferences(context);
        this.loginCallBack = loginCallBack;
        dbSQLiteOpenHelper = new DbSQLiteOpenHelper(context, Define.DB_NAME, null, Define.DB_VERSION);
    }

    public void requestMailPlugLogin(final String id, final String password){
        this.id = id;
        this.password = password;

        IntranetService retrofit = NetworkManager.getIntance().getRetrofit(IntranetService.class, Define.MAILPLUG_BASE_URL);

        Call<ResponseBody> mailPlugLoginCall = retrofit.requestMailPlugLogin(id, password, "1", "mail.teruten.com");
        mailPlugLoginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String string = null;
                try {
                    string = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("network1", "mailPlugLoginCall result string : \n"+string);

                boolean contains = string.contains("\"/main\";");
                if(string.contains("\"/main\";")){
                    //requestIntranetLogin(id);
                    requestMemberList();
                }else{
                    loginCallBack.getNetworkResponse(null, 410);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loginCallBack.getNetworkResponse(null, 110);
            }
        });
    }

    public void requestIntranetLogin(String userId){
        IntranetService intranetService = NetworkManager.getIntance().getRetrofit(IntranetService.class, Define.INTRANET_DOMAIN_BASE_URL);

        Call<Login> loginCall = intranetService.requestIntranetLogin(userId);
        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login result = response.body();
                if(TextUtils.equals(result.getResult(), "1")){
                    Member member = result.getMember();
                    //Log.d("LoginGet", "loginCall requestIntranetLogin getDepartment : "+member.getDepartment());
                    //Log.d("LoginGet", "loginCall requestIntranetLogin getName : "+member.getName());

                    loginCallBack.getNetworkResponse(member, 200);
                    setPreferenceInfo(id, password);
                }else{
                    loginCallBack.getNetworkResponse(null, 420);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.e("LoginGet", "loginCall requestIntranetLogin error : "+t.getMessage());
            }
        });
    }

    //직원 리스트 가져오기(외근메일 보내기 기능에서 직원리스트를 가져오기 위해)
    public void requestMemberList(){
        IntranetService intranetService = NetworkManager.getIntance().getRetrofit(IntranetService.class, Define.INTRANET_DOMAIN_BASE_URL);

        Call<ResponseMemberList> responseMemberListCall = intranetService.requestIntranetMemberList();
        responseMemberListCall.enqueue(new Callback<ResponseMemberList>() {
            @Override
            public void onResponse(Call<ResponseMemberList> call, Response<ResponseMemberList> response) {
                ResponseMemberList responseMemberList = response.body();
                if(TextUtils.equals(responseMemberList.getResult(), "1")){
                    ArrayList<Member> members = responseMemberList.getMember();
                    Log.d("LoginGet", "loginCall list size : "+ members.size());
                    addDbMemberList(members);

                    setPreferenceInfo(id, password);
                    Member member = getLoginUserInfo();
                    if(member == null){
                        loginCallBack.getNetworkResponse(null, 420);
                    }else{
                        Log.d("LoginGet", "loginCall member.getName() : "+ member.getName());

                        requestSendFcmToken();

                        loginCallBack.getNetworkResponse(member, 200);
                    }
                }else{
                    loginCallBack.getNetworkResponse(null, 420);
                }
            }

            @Override
            public void onFailure(Call<ResponseMemberList> call, Throwable t) {
                Log.e("LoginGet", "loginCall requestIntranetLogin error : "+t.getMessage());
                loginCallBack.getNetworkResponse(null, 450);
            }
        });
    }

    public void requestSendFcmToken(){
        String saveFcmToken = terutenSharedpreferences.getStringPreferences("FCM_TOKEN");
        Log.i("FCM", "token : "+saveFcmToken+", id : "+id);
        if(TextUtils.isEmpty(saveFcmToken)){
            saveFcmToken = FirebaseInstanceId.getInstance().getToken();
            Log.i("FCM", "saveFcmToken : "+saveFcmToken);
            terutenSharedpreferences.setStringPreferences("FCM_TOKEN", saveFcmToken);
        }

        IntranetService intranetService = NetworkManager.getIntance().getRetrofit(IntranetService.class, Define.INTRANET_DOMAIN_BASE_URL);
        Call<ResponseFCMKey> responseFCMKeyCall = intranetService.requestSendFcmKey(id, saveFcmToken, "android");
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

    public Member getLoginUserInfo(){
        Member member = dbSQLiteOpenHelper.selectItem(getLoginId());

        return member;
    }

    public boolean isEmailVaildation(String value){
        boolean result = false;
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(value);
        if(!TextUtils.isEmpty(value) && matcher.find()){
            result = true;
        }

        return result;
    }

    public boolean isPasswordVaildation(String value){
        boolean result = false;

        if(!TextUtils.isEmpty(value)){
            result = true;
        }

        return result;
    }

    public void setPreferenceInfo(String id, String password){
        terutenSharedpreferences.setStringPreferences(TerutenSharedpreferences.USER_ID, id);
        terutenSharedpreferences.setStringPreferences(TerutenSharedpreferences.USER_PW, password);
    }

    public void setAutoLoginPreferences(boolean flag){
        terutenSharedpreferences.setBooleanPreferences(TerutenSharedpreferences.AUTO_LOGIN, flag);
    }

    public boolean isAutoLoginPreferences(){
        return terutenSharedpreferences.getBooleanPreferences(TerutenSharedpreferences.AUTO_LOGIN);
    }

    public void setSaveLoginIdPreferences(boolean flag){
        terutenSharedpreferences.setBooleanPreferences(TerutenSharedpreferences.LOGIN_ID_SAVE, flag);
    }

    public boolean isSaveLoginIdPreferences(){
        return terutenSharedpreferences.getBooleanPreferences(TerutenSharedpreferences.LOGIN_ID_SAVE);
    }

    public String getLoginId(){
        return terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_ID);
    }


    public void addDbMemberList(ArrayList<Member> members){
        Log.d("LoginGet", "addDbMemberList start!!!!!");
        dbSQLiteOpenHelper.insert(members);
    }
}
