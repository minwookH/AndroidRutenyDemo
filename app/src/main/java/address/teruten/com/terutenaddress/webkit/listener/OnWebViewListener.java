package address.teruten.com.terutenaddress.webkit.listener;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by tae-hwan on 8/14/16.
 */

public interface OnWebViewListener {

    void onScroll(int l, int t, int oldl, int oldt);

    void onFinish(WebView view, String url);

    void onUrlChange(String url);

    void onProgressChanged(int newProgress);

    void onFileUpload(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams);

    void onFileUpload(ValueCallback<Uri> uploadMsg);

}
