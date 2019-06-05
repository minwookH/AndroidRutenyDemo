package address.teruten.com.terutenaddress.network;

import address.teruten.com.terutenaddress.vo.network.Login;
import address.teruten.com.terutenaddress.vo.network.ResponseMemberList;
import address.teruten.com.terutenaddress.vo.response.ResponseFCMKey;
import address.teruten.com.terutenaddress.vo.response.ResponseVersion;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface IntranetService {

    @FormUrlEncoded
    @POST("auth_sso")
    Call<ResponseBody> requestMailPlugLogin(@Field("cid") String cid, @Field("cpw") String cpw
            , @Field("pw_type") String passwordType, @Field("cdomain") String cdomain);
    //id=minw.2123423ook&cpw=!alsdnr1&pw_type=1&cdomain=mail.teruten.com&host_domain=teruten.com

    @GET("getUser")
    Call<Login> requestIntranetLogin(@Query("ID") String id);

    @GET("getUserList")
    Call<ResponseMemberList> requestIntranetMemberList();

    @GET("getVersion")
    Call<ResponseVersion> requestVersionInfo();

    @FormUrlEncoded
    @POST("setFcmKey")
    Call<ResponseFCMKey> requestSendFcmKey(@Field("id") String id, @Field("key") String key, @Field("os") String osType);

    @FormUrlEncoded
    @POST("deleteFcmToken")
    Call<ResponseFCMKey> requestDeleteFcmToken(@Field("id") String id);
}
