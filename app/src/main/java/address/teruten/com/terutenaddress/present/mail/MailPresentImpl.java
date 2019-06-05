package address.teruten.com.terutenaddress.present.mail;

import android.app.Activity;

/**
 * Created by teruten on 2017-06-22.
 */

public class MailPresentImpl implements MailPresent {

    private Activity activity;
    private MailPresent.View view;

    public MailPresentImpl(Activity activity, MailPresent.View view){
        this.activity = activity;
        this.view = view;
    }

    @Override
    public void onAutoLogin() {

    }

    @Override
    public void mailPageLoadingfinish() {
        view.hideIndicator();
    }

    @Override
    public void webPageLoading() {
        view.showIndicator();
    }
}
