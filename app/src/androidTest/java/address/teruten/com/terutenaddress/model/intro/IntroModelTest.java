package address.teruten.com.terutenaddress.model.intro;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.ui.intro.IntroActivity;

import static org.junit.Assert.*;

/**
 * Created by teruten on 2018-01-16.
 */
@RunWith(AndroidJUnit4.class)
public class IntroModelTest {
    private Context context;
    private IntroModel introModel;
    private Activity activity;
    private IntroCallBack introCallBack;
    private TerutenSharedpreferences terutenSharedpreferences;
    @Rule
    public ActivityTestRule<IntroActivity> mActivityRule = new ActivityTestRule<>(IntroActivity.class);

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getContext();
        activity = mActivityRule.getActivity();
        introCallBack = null;
        introModel = new IntroModel(context, activity, introCallBack);
        terutenSharedpreferences = new TerutenSharedpreferences(context);
    }

    @Test
    public void isSaveInfoExist() throws Exception {
        //introModel.isSaveInfoExist();
        //assertTrue("aaa", introModel.isSaveInfoExist());
        assertFalse("bbb", introModel.isSaveInfoExist());
    }

}