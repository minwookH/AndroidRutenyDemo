package address.teruten.com.terutenaddress.present.mail;

/**
 * Created by teruten on 2017-06-22.
 */

public interface MailPresent {

    void onAutoLogin();
    void mailPageLoadingfinish();
    void webPageLoading();

    interface View{
        void showIndicator();
        void hideIndicator();
    }
}
