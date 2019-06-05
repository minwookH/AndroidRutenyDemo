package address.teruten.com.terutenaddress.present.cafeteria;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.vo.CafeteriaMenu;
import address.teruten.com.terutenaddress.vo.network.Cafeteria;

/**
 * Created by teruten on 2017-08-10.
 */

public interface CafeteriaPresent {

    void loadMenu();

    interface View{
        void setMenu(ArrayList<CafeteriaMenu> menus);
        void hideIndicator();
        void showIndicator();
    }
}
