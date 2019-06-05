package address.teruten.com.terutenaddress.vo.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import address.teruten.com.terutenaddress.vo.network.Version;

/**
 * Created by teruten on 2018-01-30.
 */

public class ResponseFCMKey {
    private String result;
    private String msg;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
