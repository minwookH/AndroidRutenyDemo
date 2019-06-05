package address.teruten.com.terutenaddress.vo.network;

import com.google.gson.annotations.SerializedName;

import address.teruten.com.terutenaddress.vo.Member;

/**
 * Created by teruten on 2017-11-30.
 */

public class Version {

    @SerializedName("VERSION_NUMBER")
    private String versionNumber;

    @SerializedName("VERSION_CODE")
    private String versionCode;

    @SerializedName("TYPE")
    private String type;


    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
