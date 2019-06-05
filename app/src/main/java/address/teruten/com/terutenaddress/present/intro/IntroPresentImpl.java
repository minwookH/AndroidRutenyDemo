package address.teruten.com.terutenaddress.present.intro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.Random;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.data.Define;
import address.teruten.com.terutenaddress.model.intro.IntroCallBack;
import address.teruten.com.terutenaddress.model.intro.IntroModel;
import address.teruten.com.terutenaddress.vo.Member;
import address.teruten.com.terutenaddress.vo.network.Version;

/**
 * Created by teruten on 2017-06-22.
 */

public class IntroPresentImpl implements IntroPresent, IntroCallBack{

    private Context context;
    private Activity activity;
    private IntroPresent.View view;
    private IntroModel introModel;
    private ArrayList<Integer> imageList = new ArrayList<>();


    public IntroPresentImpl(Context context, Activity activity, IntroPresent.View view){
        this.context = context;
        this.view = view;
        introModel = new IntroModel(context, activity, this);
    }

    @Override
    public void onInfoCheck() {
        if(introModel.isSaveInfoExist()){
            //introModel.requestMemberInfo();
            introModel.requestMemberList();
        }else{
            view.moveLoginPage();
        }
    }

    @Override
    public void onRandomIntroImage() {
        //introModel.getImageRandom();
        view.setIntroImage(introModel.getImageRandom());
        //return introModel.getImageRandom();
    }

    @Override
    public void getMemberList() {
        introModel.requestMemberList();
    }

    @Override
    public void versionCheck() {
        introModel.requestVersionInfo();
    }

    @Override
    public void apkDownload() {
        view.startProgress();
        introModel.requestDownloadApk();
    }

    @Override
    public void getNetworkResponse(Object data, int status) {
        switch (status){
            case 200:
                view.moveMainPage((Member) data);
                break;
            case 250:
                if((Boolean) data){
                    view.showDialog(Define.DIALOG_VERSION_UPDATE, context.getString(R.string.version_check_dialog_contents));
                }else{
                    view.startInterval();
                }
                break;
            case 251:
                view.stopProgress();
                break;
            case 450:
                break;
            case 451:
                view.stopProgress();
                break;
            case Define.NETWORK_ERROR_TIMEOUT:
                view.showDialog(Define.NETWORK_ERROR_TIMEOUT, "인트라넷 서버 접속 시간을 초과하였습니다.\n서버관리자에게 문의하시기 바랍니다.");
                break;
            case Define.NETWORK_ERROR_ETC:
                view.showDialog(Define.NETWORK_ERROR_ETC, "인트라넷 서버 접속에 실패하였습니다.\n서버관리자에게 문의하시기 바랍니다.");
                break;
            default:
                break;
        }
    }
}
