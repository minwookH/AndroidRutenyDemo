package address.teruten.com.terutenaddress.model.intro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.data.Define;
import address.teruten.com.terutenaddress.database.DbSQLiteOpenHelper;
import address.teruten.com.terutenaddress.network.DownloadService;
import address.teruten.com.terutenaddress.network.IntranetService;
import address.teruten.com.terutenaddress.network.NetworkManager;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.vo.Member;
import address.teruten.com.terutenaddress.vo.network.Login;
import address.teruten.com.terutenaddress.vo.network.ResponseMemberList;
import address.teruten.com.terutenaddress.vo.network.Version;
import address.teruten.com.terutenaddress.vo.realm.Member1;
import address.teruten.com.terutenaddress.vo.response.ResponseFCMKey;
import address.teruten.com.terutenaddress.vo.response.ResponseVersion;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by teruten on 2017-06-22.
 */

public class IntroModel {

    private Context context;
    private Activity activity;
    private IntroCallBack introCallBack;
    private TerutenSharedpreferences terutenSharedpreferences;
    private ArrayList<Integer> imageList = new ArrayList<>();
    private DbSQLiteOpenHelper dbSQLiteOpenHelper;

    private String versionNumber;

    public IntroModel(Context context, Activity activity, IntroCallBack introCallBack){
        this.context = context;
        this.activity = activity;
        this.introCallBack = introCallBack;

        imageList.add(R.drawable.intro1);
        imageList.add(R.drawable.intro2);
        imageList.add(R.drawable.intro3);
        imageList.add(R.drawable.intro4);
        imageList.add(R.drawable.intro5);
        imageList.add(R.drawable.intro6);
        imageList.add(R.drawable.intro7);
        imageList.add(R.drawable.intro8);
        imageList.add(R.drawable.intro9);
        imageList.add(R.drawable.intro10);

        dbSQLiteOpenHelper = new DbSQLiteOpenHelper(context, Define.DB_NAME, null, Define.DB_VERSION);
    }

    public boolean isSaveInfoExist(){
        terutenSharedpreferences = new TerutenSharedpreferences(context);
        boolean result = false;

        String userId = terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_ID);
        String userPw = terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_PW);

        if(!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(userPw)
                && terutenSharedpreferences.getBooleanPreferences(TerutenSharedpreferences.AUTO_LOGIN)){
            result = true;
        }

        return result;
    }

    public void requestMemberInfo(){
        IntranetService intranetService = NetworkManager.getIntance().getRetrofit(IntranetService.class, Define.INTRANET_DOMAIN_BASE_URL);

        Call<Login> loginCall = intranetService.requestIntranetLogin(terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_ID));
        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login result = response.body();
                if(TextUtils.equals(result.getResult(), "1")){
                    Member member = result.getMember();
                    //Log.d("LoginGet", "loginCall requestIntranetLogin getDepartment : "+member.getDepartment());
                    //Log.d("LoginGet", "loginCall requestIntranetLogin getName : "+member.getName());

                    introCallBack.getNetworkResponse(member, 200);
                }else{
                    introCallBack.getNetworkResponse(null, 420);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.e("intro", "intro requestIntranetLogin error : "+t.getMessage());
            }
        });
    }


    /*public void requestSendFcmToken(){
        String saveFcmToken = terutenSharedpreferences.getStringPreferences("FCM_TOKEN");
        //Log.i("FCM", "token : "+saveFcmToken+", id : "+id);
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

    public void requestMemberList(){
        IntranetService intranetService = NetworkManager.getIntance().getRetrofit(IntranetService.class, Define.INTRANET_DOMAIN_BASE_URL);

        Call<ResponseMemberList> responseMemberListCall = intranetService.requestIntranetMemberList();
        responseMemberListCall.enqueue(new Callback<ResponseMemberList>() {
            @Override
            public void onResponse(Call<ResponseMemberList> call, Response<ResponseMemberList> response) {
                ResponseMemberList responseMemberList = response.body();
                if(TextUtils.equals(responseMemberList.getResult(), Define.NETWORK_RESULT_SUCCESS)){
                    ArrayList<Member> members = responseMemberList.getMember();
                    Log.d("intro", "intro list size : "+ members.size());
                    addDbMemberList(members);
                    Log.d("intro", "1111111111111111111111111111111111111111111 : ");
                    Member member = dbSQLiteOpenHelper.selectItem(terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_ID));
                    Log.d("intro", "intro member.getName() : "+ member.getName());
                    introCallBack.getNetworkResponse(member, Define.NETWORK_SUCCESS_MEMBER_LIST);
                }else{
                    introCallBack.getNetworkResponse(null, Define.NETWORK_ERROR_MEMBER_LIST);
                }
            }

            @Override
            public void onFailure(Call<ResponseMemberList> call, Throwable t) {
                Log.e("intro", "intro requestIntranetLogin error : "+t.getMessage());
                introCallBack.getNetworkResponse(null, Define.NETWORK_ERROR_MEMBER_LIST);
            }
        });
    }

    public HashMap<String, Integer> getImageRandom(){
        Random random = new Random();
        int randomIndex = random.nextInt(imageList.size());
        Log.d("intro", "randomIndex : "+randomIndex + ", size : "+imageList.size());

        HashMap<String, Integer> introImageMap = new HashMap<>();
        introImageMap.put("id", imageList.get(randomIndex));
        /*if(randomIndex == 3){
            introImageMap.put("border", 1);
        }else{
            introImageMap.put("border", 0);
        }*/
        introImageMap.put("border", 0);

        return introImageMap;
    }

    public void addDbMemberList(ArrayList<Member> members){
        Log.d("intro", "addDbMemberList start!!!!!");
        dbSQLiteOpenHelper.insert(members);
    }

    public void requestVersionInfo(){
        Log.d("intro", "requestVersionInfo start!!!!!");
        IntranetService intranetService = NetworkManager.getIntance().getRetrofit(IntranetService.class, Define.INTRANET_DOMAIN_BASE_URL);

        Call<ResponseVersion> requestVersionInfo = intranetService.requestVersionInfo();
        requestVersionInfo.enqueue(new Callback<ResponseVersion>() {
            @Override
            public void onResponse(Call<ResponseVersion> call, Response<ResponseVersion> response) {
                Log.i("versionGET", "onResponse isSuccessful : "+ response.isSuccessful());
                Log.i("versionGET", "onResponse code : "+ response.code());
                if(response.isSuccessful()){

                    ResponseVersion responseVersion = response.body();

                    Log.d("versionGET", "getResult : "+responseVersion.getResult());
                    Log.d("versionGET", "getVersionList().size() : "+responseVersion.getVersionList().size());
                    Log.d("versionGET", "getVersionList().getType() : "+responseVersion.getVersionList().get(0).getType());

                    String versionCode = null;

                    if(TextUtils.equals(responseVersion.getResult(),  Define.NETWORK_RESULT_SUCCESS)){
                        for (Version versionInfo:
                                responseVersion.getVersionList()) {
                            if(TextUtils.equals("android", versionInfo.getType())){
                                versionCode = versionInfo.getVersionNumber();
                                versionNumber = versionInfo.getVersionCode();
                            }
                        }

                        introCallBack.getNetworkResponse(checkVersion(Integer.parseInt(versionCode)), Define.NETWORK_SUCCESS_VERSION);
                    }else{

                    }
                }else{

                }

            }

            @Override
            public void onFailure(Call<ResponseVersion> call, Throwable t) {
                int result = 0;
                Log.e("versionGET", "requestVersionInfo error : "+t.getMessage());

                if(t instanceof SocketTimeoutException){
                    Log.e("versionGET", "SocketTimeoutException!!! : ");
                    result = Define.NETWORK_ERROR_TIMEOUT;
                }else if(t instanceof IOException){
                    Log.e("versionGET", "Etc IOException!!! : ");
                    result = Define.NETWORK_ERROR_IOEXCEPTION;
                }else if(t instanceof IllegalStateException){
                    Log.e("versionGET", "IllegalStateException!!! : ");
                }else{
                    Log.e("versionGET", "Etc Exception!!! : ");
                    result = Define.NETWORK_ERROR_ETC;
                }

                introCallBack.getNetworkResponse(null, result);
            }
        });
    }

    public boolean checkVersion(int versionCode){
        boolean result = false;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            if(packageInfo.versionCode < versionCode){
                result = true;
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }


    public void requestDownloadApk(){
        DownloadService downloadService = NetworkManager.getIntance().getRetrofit(DownloadService.class, Define.APK_DOWNLOAD_URL);
        Call<ResponseBody> requestDownloadAPK = downloadService.requestDownloadAPK(versionNumber);
        requestDownloadAPK.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.d("DownloadApk", "getExternalStorageDirectory : "+ Environment.getExternalStorageDirectory());
                File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "TerutenOrganization_v"+versionNumber+"-release.apk");
                Log.d("DownloadApk", "getPath : "+ apkFile.getPath());
                Log.d("DownloadApk", "getAbsolutePath : "+ apkFile.getAbsolutePath());
                boolean writtenToDisk = writeResponseBodyToDisk(response.body(), apkFile);
                Log.d("DownloadApk", "download apk : "+ writtenToDisk);

                if(writtenToDisk){
                    introCallBack.getNetworkResponse(null, Define.NETWORK_SUCCESS_APK_DOWNLOAD);
                    apkFileInstall(apkFile);
                }else{
                    introCallBack.getNetworkResponse(null, Define.NETWORK_ERROR_APK_DOWNLOAD);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("DownloadApk", "requestDownloadAPK error : "+ t.getLocalizedMessage());
                introCallBack.getNetworkResponse(null, Define.NETWORK_ERROR_APK_DOWNLOAD);
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, File apkFile) {
        try {
            if(apkFile.exists()){
                apkFile.delete();
            }

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(apkFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void apkFileInstall(File apkFile){
        Uri uriForFile = FileProvider.getUriForFile(context, context.getPackageName(), apkFile);

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
            intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }

        activity.startActivityForResult(intent, 110);
    }

}
