package address.teruten.com.terutenaddress.present.setting;

import android.content.Context;
import android.text.TextUtils;

import address.teruten.com.terutenaddress.model.setting.SettingModel;
import address.teruten.com.terutenaddress.util.TerutenUtil;
import address.teruten.com.terutenaddress.vo.Member;

/**
 * Created by teruten on 2017-06-23.
 */

public class SettingPresentImpl implements SettingPresent {

    private Context context;
    private SettingPresent.View view;
    private SettingModel settingModel;

    public SettingPresentImpl(Context context, SettingPresent.View view){
        this.context = context;
        this.view = view;
        settingModel = new SettingModel(context);
    }

    @Override
    public void logout() {
        settingModel.deleteUserInfo();
        TerutenUtil.webViewCookieClear();
        settingModel.webViewCookieClear();
        settingModel.requestDeleteFcmToken();
        view.moveLoginPage();
    }

    @Override
    public void getUserId() {
        view.setUserId(settingModel.getPreferencesUserId());
    }

    @Override
    public void isReportUsed() {
        Member member = settingModel.selectMember(settingModel.getPreferencesUserId());
        if(TextUtils.equals("Y", member.getReportUse())){
            view.setReportUseButtonVisible(true);
        }else{
            view.setReportUseButtonVisible(false);
        }
    }
}
