package address.teruten.com.terutenaddress.ui.password;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.ui.main.MainActivity;
import address.teruten.com.terutenaddress.webkit.CustomWebChromeClient;
import address.teruten.com.terutenaddress.webkit.CustomWebView;
import address.teruten.com.terutenaddress.webkit.CustomWebViewClient;
import address.teruten.com.terutenaddress.webkit.listener.OnWebViewListener;

public class FindPasswordActivity extends AppCompatActivity implements OnWebViewListener {

    private CustomWebView webView;
    private ContentLoadingProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("비밀번호 찾기");
        setSupportActionBar(toolbar);

        loadingProgressBar = (ContentLoadingProgressBar) findViewById(R.id.password_find_web_view_load_progress_bar);
        webView = (CustomWebView) findViewById(R.id.find_password_webView);

//         Add WebView ScrollChangeListener M
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    //Log.d(TAG, "scroll " + i + ", i1 " + i1 + ", i2 " + i2 + ", i3 " + i3);
                }
            });
        }*/

        webView.setOnWebViewListener(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new CustomWebChromeClient(this, this));
        webView.setWebViewClient(new CustomWebViewClient(this));
        webView.defaultInit(WebSettings.LOAD_DEFAULT);
        webView.clearCache(true);
        webView.clearHistory();
        webView.loadUrl("http://mail.teruten.com/member/pw_search?mtype=web");

    }

    @Override
    public void onScroll(int l, int t, int oldl, int oldt) {

    }

    @Override
    public void onFinish(WebView view, String url) {

    }

    @Override
    public void onUrlChange(String url) {

    }

    @Override
    public void onProgressChanged(int newProgress) {
        loadingProgressBar.setProgress(newProgress);
        //Log.w(TAG, "onProgressChanged : "+newProgress);
        if (newProgress >= 100) {
            loadingProgressBar.setVisibility(View.GONE);
        } else if (loadingProgressBar.getVisibility() != View.VISIBLE) {
            loadingProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFileUpload(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {

    }

    @Override
    public void onFileUpload(ValueCallback<Uri> uploadMsg) {

    }


    @Override
    protected void onDestroy() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                    Log.d("sdfsdf", "removeSessionCookies aBoolean : " + aBoolean);
                }
            });
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                    Log.d("sdfsdf", "removeAllCookies aBoolean : " + aBoolean);
                }
            });
        }else{
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
        }
        super.onDestroy();
    }
}
