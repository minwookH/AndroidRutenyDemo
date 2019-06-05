package address.teruten.com.terutenaddress.model.cafeteria;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import address.teruten.com.terutenaddress.data.Define;
import address.teruten.com.terutenaddress.network.CafeteriaService;
import address.teruten.com.terutenaddress.network.IntranetService;
import address.teruten.com.terutenaddress.network.NetworkManager;
import address.teruten.com.terutenaddress.vo.CafeteriaMenu;
import address.teruten.com.terutenaddress.vo.network.Cafeteria;
import address.teruten.com.terutenaddress.vo.network.Login;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by teruten on 2017-08-10.
 */

public class    CafeteriaModel {
    private Context context;
    private CafeteriaCallback cafeteriaCallback;

    public CafeteriaModel(Context context, CafeteriaCallback cafeteriaCallback){
        this.context = context;
        this.cafeteriaCallback = cafeteriaCallback;
    }

    public void requestCafeteriaMenu(){
        CafeteriaService retrofit = NetworkManager.getIntance().getRetrofit(CafeteriaService.class, Define.CAFETERIA_MENU_URL);
        Call<ArrayList<Cafeteria>> cafeteriaCall = retrofit.requestCafeteriaMenu();
        /*cafeteriaCall.enqueue(new Callback<ArrayList<Cafeteria>>() {
            @Override
            public void onResponse(Call<ArrayList<Cafeteria>> call, Response<ArrayList<Cafeteria>> response) {
                if(response.isSuccessful()){
                    Log.d("cafeteria", "response success !");
                    ArrayList<Cafeteria> body = response.body();
                    cafeteriaCallback.getNetworkResponse(body, 200);
                }else{
                    try {
                        Log.e("cafeteria", "response error : "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Cafeteria>> call, Throwable t) {
                Log.e("cafeteria", t.getLocalizedMessage());
                cafeteriaCallback.getNetworkResponse(null, 400);
            }
        });*/


        Call<Cafeteria> responseBodyCall = retrofit.requestCafeteriaMenu1();
        responseBodyCall.enqueue(new Callback<Cafeteria>() {
            @Override
            public void onResponse(Call<Cafeteria> call, Response<Cafeteria> response) {
                Cafeteria body = response.body();
                ArrayList<CafeteriaMenu> menus = new ArrayList<CafeteriaMenu>();

                try {
                    JSONObject jsonObject;
                    for (String menu : body.getMenus()) {
                        jsonObject = new JSONObject(menu);
                        CafeteriaMenu cafeteriaMenu = new CafeteriaMenu();
                        cafeteriaMenu.setDate((String) jsonObject.get("date"));

                        JSONArray lunchMenus = new JSONArray(jsonObject.get("lunch").toString());
                        ArrayList<String> lunch = new ArrayList<String>();
                        for(int i = 0; i < lunchMenus.length(); i++) {
                            lunch.add(lunchMenus.get(i).toString());
                            Log.d("cafeteria", "lunch : "+lunchMenus.get(i).toString());

                        }

                        JSONArray dinnerMenus = new JSONArray(jsonObject.get("dinner").toString());
                        ArrayList<String> dinner = new ArrayList<String>();
                        for(int i = 0; i < dinnerMenus.length(); i++) {
                            dinner.add(dinnerMenus.get(i).toString());
                            Log.d("cafeteria", "dinnerMenus : "+dinnerMenus.get(i).toString());

                        }
                        cafeteriaMenu.setLunch(lunch);
                        cafeteriaMenu.setDinner(dinner);

                        menus.add(cafeteriaMenu);
                    }

                    cafeteriaCallback.getNetworkResponse(menus, 200);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Cafeteria> call, Throwable t) {
                Log.e("cafeteria", t.getLocalizedMessage());
            }
        });

    }
}
