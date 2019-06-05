package address.teruten.com.terutenaddress.vo.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.vo.network.Version;

/**
 * Created by teruten on 2017-11-30.
 */

public class ResponseVersion {

    private String result;

    @SerializedName("version")
    private ArrayList<Version> versionList;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Version> getVersionList() {
        return versionList;
    }

    public void setVersionList(ArrayList<Version> versionList) {
        this.versionList = versionList;
    }


}
