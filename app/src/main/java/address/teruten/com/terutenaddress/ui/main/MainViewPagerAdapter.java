package address.teruten.com.terutenaddress.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import address.teruten.com.terutenaddress.vo.FragmentInfo;

/**
 * Created by teruten on 2017-06-16.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private final List<FragmentInfo> fragmentInfoList = new ArrayList<FragmentInfo>();


    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public int getCount() {
        return fragmentInfoList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentInfoList.get(position).getFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentInfoList.get(position).getTitleText();
    }

    public void addFragment(int iconResId, String title, Fragment fragment) {
        FragmentInfo info = new FragmentInfo(iconResId, title, fragment);
        fragmentInfoList.add(info);
    }

    public FragmentInfo getFragmentInfo(int position) {
        return fragmentInfoList.get(position);
    }
}
