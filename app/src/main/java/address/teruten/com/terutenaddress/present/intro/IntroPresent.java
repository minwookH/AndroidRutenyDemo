package address.teruten.com.terutenaddress.present.intro;

import java.util.HashMap;

import address.teruten.com.terutenaddress.vo.Member;

/**
 * Created by teruten on 2017-06-22.
 */

public interface IntroPresent {

    void onInfoCheck();
    void onRandomIntroImage();
    void getMemberList();
    void versionCheck();
    void apkDownload();

    interface View{
        void moveMainPage(Member member);
        void moveLoginPage();
        void setIntroImage(HashMap<String, Integer> imageRandom);
        void showDialog(int flag, String text);
        void startProgress();
        void stopProgress();
        void startInterval();
    }
}
