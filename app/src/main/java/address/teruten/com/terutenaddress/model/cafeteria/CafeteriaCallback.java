package address.teruten.com.terutenaddress.model.cafeteria;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.vo.CafeteriaMenu;
import address.teruten.com.terutenaddress.vo.Member;
import address.teruten.com.terutenaddress.vo.network.Cafeteria;

/**
 * Created by teruten on 2017-08-10.
 */

public interface CafeteriaCallback {
    void getNetworkResponse(ArrayList<CafeteriaMenu> menus, int status);
}
