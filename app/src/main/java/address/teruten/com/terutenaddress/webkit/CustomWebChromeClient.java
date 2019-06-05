package address.teruten.com.terutenaddress.webkit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import address.teruten.com.terutenaddress.webkit.listener.OnWebViewListener;


/**
 * Created by Tae-hwan on 8/4/16.
 */

public class CustomWebChromeClient extends WebChromeClient {

    private Activity activity;

    private OnWebViewListener webViewListener;


    public CustomWebChromeClient(Activity activity, OnWebViewListener listener) {
        this.activity = activity;
        this.webViewListener = listener;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
            }
        }).setCancelable(false).create();

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                result.cancel();
            }
        });

        if (activity.isFinishing()) {
            result.confirm();
        } else {
            alertDialog.show();
        }

        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.confirm();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.cancel();
            }
        }).setCancelable(false).create();

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                result.cancel();
            }
        });

        if (activity.isFinishing()) {
            result.cancel();

        } else {
            alertDialog.show();
        }

        return true;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);

        if (webViewListener != null) {
            webViewListener.onProgressChanged(newProgress);
        }
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

        Log.d("FILEupload", "onShowFileChooser start");
        if (webViewListener != null) {
            webViewListener.onFileUpload(webView, filePathCallback, fileChooserParams);
        }

        return true;
    }

    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType) {
        openFileChooser(uploadFile);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        if (webViewListener != null) {
            webViewListener.onFileUpload(uploadMsg);
        }

    }
}
