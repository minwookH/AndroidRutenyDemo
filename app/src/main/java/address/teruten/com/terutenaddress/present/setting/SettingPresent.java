package address.teruten.com.terutenaddress.present.setting;

/**
 * Created by teruten on 2017-06-23.
 */

public interface SettingPresent {

    void logout();
    void getUserId();
    void isReportUsed();

    interface View{
        void moveLoginPage();
        void setUserId(String id);
        void setReportUseButtonVisible(boolean flag);
    }
}
