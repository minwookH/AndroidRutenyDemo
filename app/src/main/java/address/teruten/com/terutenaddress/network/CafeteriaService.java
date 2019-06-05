package address.teruten.com.terutenaddress.network;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.vo.CafeteriaMenu;
import address.teruten.com.terutenaddress.vo.network.Cafeteria;
import address.teruten.com.terutenaddress.vo.network.Login;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by teruten on 2017-08-10.
 */

public interface CafeteriaService {

    @Headers({"User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.60 Safari/537.1 CoolNovo/2.0.4.16" +
                ", Referer:http://static.nid.naver.com"})
    @GET("cafeteria")
    Call<ArrayList<Cafeteria>> requestCafeteriaMenu();

    @Headers({"User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.60 Safari/537.1 CoolNovo/2.0.4.16" +
            ", Referer:http://static.nid.naver.com"})
    @GET("cafeteria")
    Call<Cafeteria> requestCafeteriaMenu1();
}
