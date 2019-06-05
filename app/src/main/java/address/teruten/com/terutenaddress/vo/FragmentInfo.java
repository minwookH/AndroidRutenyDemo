package address.teruten.com.terutenaddress.vo;


import android.support.v4.app.Fragment;

/**
 * Created by teruten on 2017-06-16.
 */

public class FragmentInfo {
    private int iconResId;
    private String text;
    private Fragment fragment;

    public FragmentInfo(int iconResId, String text, Fragment fragment) {
        this.iconResId = iconResId;
        this.text = text;
        this.fragment = fragment;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitleText() {
        return text;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
