package address.teruten.com.terutenaddress.present.main;

/**
 * Created by teruten on 2017-07-28.
 */

public interface Main {

    void onBackbutton();
    void uploadFCMToken();

    interface View{
        void showToast(String text);
        void actvityFinish();
    }
}
