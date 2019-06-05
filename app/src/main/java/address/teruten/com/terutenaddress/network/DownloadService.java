package address.teruten.com.terutenaddress.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by teruten on 2017-11-28.
 */

public interface DownloadService {

    //http://sample.e-teruten.com/organization/android/TerutenOrganization_v1.0.2-release.apk

    @GET("organization/android/TerutenOrganization-v{version}-release.apk")
    Call<ResponseBody> requestDownloadAPK(@Path("version") String version);
}
