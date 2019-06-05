package address.teruten.com.terutenaddress.present.cafeteria;

import android.content.Context;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.model.cafeteria.CafeteriaCallback;
import address.teruten.com.terutenaddress.model.cafeteria.CafeteriaModel;
import address.teruten.com.terutenaddress.vo.CafeteriaMenu;
import address.teruten.com.terutenaddress.vo.network.Cafeteria;

/**
 * Created by teruten on 2017-08-10.
 */

public class CafeteriaPresentImpl implements CafeteriaPresent , CafeteriaCallback{

    private Context context;
    private CafeteriaPresent.View view;
    private CafeteriaModel cafeteriaModel;

    public CafeteriaPresentImpl(Context context, CafeteriaPresent.View view){
        this.context = context;
        this.view = view;
        cafeteriaModel = new CafeteriaModel(context, this);
    }


    @Override
    public void loadMenu() {
        view.showIndicator();
        cafeteriaModel.requestCafeteriaMenu();
    }

    @Override
    public void getNetworkResponse(ArrayList<CafeteriaMenu> menus, int status) {
        view.setMenu(menus);
        view.hideIndicator();
    }
}
