package address.teruten.com.terutenaddress;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import address.teruten.com.terutenaddress.webkit.CustomWebChromeClient;
import address.teruten.com.terutenaddress.webkit.CustomWebView;
import address.teruten.com.terutenaddress.webkit.CustomWebViewClient;
import address.teruten.com.terutenaddress.webkit.listener.OnWebViewListener;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TextView.OnEditorActionListener, View.OnKeyListener, OnWebViewListener {

    private CustomWebView webView;
    private TextView tvUrlView;
    private ContentLoadingProgressBar loadingProgressBar;

    private FloatingActionButton floatingActionButton;

    private static final String TAG = "teruten";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvUrlView = (TextView) findViewById(R.id.tv_url_input);
        tvUrlView.setOnEditorActionListener(this);

        loadingProgressBar = (ContentLoadingProgressBar) findViewById(R.id.web_view_load_progress_bar);

        webView = (CustomWebView) findViewById(R.id.webView);



//         Add WebView ScrollChangeListener M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            webView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    //Log.d(TAG, "scroll " + i + ", i1 " + i1 + ", i2 " + i2 + ", i3 " + i3);
                }
            });
        }
/*

        // Enable pinch to zoom without the zoom buttons
        webView.getSettings().setBuiltInZoomControls(true);
        // Enable pinch to zoom without the zoom buttons
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
        // Hide the zoom controls for HONEYCOMB+
            webView.getSettings().setDisplayZoomControls(false);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            webView.getSettings().setTextZoom(100);
        }
*/


        webView.setOnWebViewListener(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new CustomWebChromeClient(this, this));
        webView.setWebViewClient(new CustomWebViewClient(this));
        webView.defaultInit(WebSettings.LOAD_DEFAULT);
        webView.loadUrl("http://www.naver.com");


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (webView.canGoBack()) {
                webView.goBack();

            }else{
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            switch (i) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }


    @Override
    public void onScroll(int l, int t, int oldl, int oldt) {
        //Log.d(TAG, "onScroll l : "+l+", t : "+t+", oldl : "+oldl+", oldt : "+oldt);

        int height = (int) Math.floor(webView.getContentHeight() * webView.getScale());
        int webViewHeight = webView.getMeasuredHeight();
        Log.i("THE END", "height : "+height+", webViewHeight : "+webViewHeight);
        if(webView.getScrollY() + webViewHeight >= height){
            Log.i("THE END", "reached");
        }

    }

    @Override
    public void onFinish(WebView view, String url) {
        Log.w(TAG, "onFinish url : "+url);
        tvUrlView.setText(url);
    }

    @Override
    public void onUrlChange(String url) {
        Log.w(TAG, "onUrlChange url : "+url);
    }

    @Override
    public void onProgressChanged(int newProgress) {
        loadingProgressBar.setProgress(newProgress);
        Log.w(TAG, "onProgressChanged : "+newProgress);
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
}
