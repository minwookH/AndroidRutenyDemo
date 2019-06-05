package address.teruten.com.terutenaddress.util;

import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;

import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;

/**
 * Created by teruten on 2017-08-09.
 */

public class TerutenUtil {
    public static void webViewCookieClear(){
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                    Log.d("sdfsdf", "removeSessionCookies aBoolean : " + aBoolean);
                }
            });
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                    Log.d("sdfsdf", "removeAllCookies aBoolean : " + aBoolean);
                }
            });
        }else{
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
        }
    }
}
