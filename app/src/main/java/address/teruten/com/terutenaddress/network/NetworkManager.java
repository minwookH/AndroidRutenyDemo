package address.teruten.com.terutenaddress.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import address.teruten.com.terutenaddress.data.Define;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class NetworkManager {
    private static final NetworkManager networkManager = new NetworkManager();

    private NetworkManager() { }

    public static NetworkManager getIntance() {
        return networkManager;
    }

    public <T> T getRetrofit(Class<T> service, String baseUrl) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit.create(service);
    }

}
