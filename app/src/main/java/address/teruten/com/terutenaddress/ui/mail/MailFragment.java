package address.teruten.com.terutenaddress.ui.mail;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.present.mail.MailPresent;
import address.teruten.com.terutenaddress.present.mail.MailPresentImpl;
import address.teruten.com.terutenaddress.ui.main.MainActivity;
import address.teruten.com.terutenaddress.webkit.CustomWebChromeClient;
import address.teruten.com.terutenaddress.webkit.CustomWebView;
import address.teruten.com.terutenaddress.webkit.CustomWebViewClient;
import address.teruten.com.terutenaddress.webkit.listener.OnWebViewListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MailFragment extends Fragment implements OnWebViewListener, View.OnKeyListener, MailPresent.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private WebView kitkatWebView;
    private CustomWebView webView;
    private TextView tvUrlView;
    private ContentLoadingProgressBar loadingProgressBar;

    private TerutenSharedpreferences terutenSharedpreferences;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private boolean isInit = true;
    private String fromMail = "";

    private AVLoadingIndicatorView avi;
    private RelativeLayout indicatorLayout;
    private MailPresentImpl mailPresentImpl;


    public static final int FILECHOOSER_RESULTCODE   = 1;
    public final static int FILECHOOSER_LOLLIPOP_REQ_CODE = 2;
    private ValueCallback<Uri> mUploadMessage = null;
    private ValueCallback<Uri[]> filePathCallbackLollipop;

    public MailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MailFragment newInstance(String param1, String param2) {
        MailFragment fragment = new MailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("mailfragment", "onCreate start");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("mailfragment", "onCreateView start");
        terutenSharedpreferences = new TerutenSharedpreferences(getContext());
        return inflater.inflate(R.layout.fragment_mail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d("mailfragment", "onActivityCreated start");
        loadingProgressBar = (ContentLoadingProgressBar) getActivity().findViewById(R.id.mail_web_view_load_progress_bar);
        webView = (CustomWebView) getActivity().findViewById(R.id.mail_webView);

        avi= (AVLoadingIndicatorView) getActivity().findViewById(R.id.mail_indicator);
        indicatorLayout = (RelativeLayout) getActivity().findViewById(R.id.indicator_layout);

        mailPresentImpl = new MailPresentImpl(getActivity(), this);
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
        webView.setWebChromeClient(new CustomWebChromeClient(getActivity(), this));
        webView.setWebViewClient(new CustomWebViewClient(this));
        webView.defaultInit(WebSettings.LOAD_DEFAULT);
        webView.clearCache(true);
        webView.clearHistory();
        webView.setOnKeyListener(this);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Log.w("filedownload", "download start");
                try{

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                    request.setMimeType("application/octet-stream");
                    //------------------------COOKIE!!------------------------
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    //------------------------COOKIE!!------------------------
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription("Downloading file...");
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, "application/octet-stream"));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, "application/octet-stream"));
                    DownloadManager dm = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                    Toast.makeText(getContext(), "Downloading File", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        webView.loadUrl("http://mail.teruten.com");
        mailPresentImpl.webPageLoading();
    }

    @Override
    public void onResume() {
        super.onResume();
        //webView.loadUrl("http://mail.teruten.com");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onScroll(int l, int t, int oldl, int oldt) {
        int height = (int) Math.floor(webView.getContentHeight() * webView.getScale());
        int webViewHeight = webView.getMeasuredHeight();
        Log.i("THE END", "height : "+height+", webViewHeight : "+webViewHeight);
        if(webView.getScrollY() + webViewHeight >= height){
            Log.i("THE END", "reached");
        }
    }

    @Override
    public void onFinish(WebView view, String url) {
        Log.w("mailFrag", "mailFragment  onFinish url : "+url);

        String id = terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_ID);
        String password = terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_PW);

        String decodeUrl = null;
        try {
            decodeUrl = URLDecoder.decode(url, "utf-8");
            Log.w("mailFrag", "decodeUrl : "+decodeUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(!TextUtils.isEmpty(decodeUrl) && decodeUrl.contains("http://mail.teruten.com/member/login?t=")){
            Log.i("mailFrag", "javascript isInit : "+isInit);

            if(isInit){
                //String password="!alefsdnr1dfsdfsdf";
                view.loadUrl("javascript:document.getElementById('cid').value = '"+id+"';document.getElementById('cpw').value='"+password+"';void(0);");

                view.loadUrl("javascript:document.forms['frmlogin'].submit();");
                //view.loadUrl("javascript:document.getElementsByClassName('mb-auth-login-button').click();");
            }else {
                Log.i("mailFrag", "mail isInit not!! : ");
                mailPresentImpl.mailPageLoadingfinish();
            }

        }else if(!TextUtils.isEmpty(decodeUrl) && decodeUrl.contains("http://mail.teruten.com/board")){
            Log.i("mailFrag", "mail board page move : ");
            view.loadUrl("http://mail.teruten.com/webmail/lists");
            mListener.goBoardTab();
            mailPresentImpl.mailPageLoadingfinish();
        }else if(!TextUtils.isEmpty(decodeUrl) //&& isInit
                                                && decodeUrl.contains("http://mail.teruten.com/webmail/lists")
                                                && decodeUrl.contains("\"list\":true")
                                                && decodeUrl.contains("\"write\":null")){
            Log.i("mailFrag", "mail isInit!! : ");
            isInit = false;
            view.clearHistory();
            mailPresentImpl.mailPageLoadingfinish();
        }else if(!TextUtils.isEmpty(decodeUrl) && decodeUrl.contains("http://mail.teruten.com/webmail/lists#")
                                                && decodeUrl.contains("\"write\":\"%2Fwebmail%2Fwrite\"")){
            Log.i("mailFrag", "mail write!! : ");
            //$('#write_btn').trigger('click');
            Log.i("mailFrag", "mail isMailWriteInit : "+isMailWriteInit);
            Log.i("mailFrag", "mail fromMail : "+fromMail);
            if(isMailWriteInit){
                view.loadUrl("javascript:$('#write_btn').trigger('click');");
                isMailWriteInit = false;
            }else {
                //view.loadUrl("javascript:document.getElementById('mail_to_input-selectized').value = '"+fromMail+"';void(0);");
                /*String value = "javascript:document.getElementById('mail_to_input-selectized').value = '"+fromMail+"';";
                Log.i("sdfs", "mail value : "+value);
                view.loadUrl(value);*/


                /* new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String value = "javascript:$('#mail_to_input-selectized').val('"+ fromMail + "');";
                        view.loadUrl(value);
                    }
                }, 1000);*/
                String value = "javascript:setTimeout(function() {$('#mail_to_input-selectized').val('"+ fromMail + "');},1000);";
                view.loadUrl(value);


            }
            mailPresentImpl.mailPageLoadingfinish();
        }else {
            Log.i("mailFrag", "mail else!! : ");
            mailPresentImpl.mailPageLoadingfinish();
        }


        /*if(url.contains("http://mail.teruten.com/webmail")
                || url.contains("http://mail.teruten.com/webmail")){
            Log.i("board", "메일");
            //view.loadUrl("javascript:document.getElementsByClassName('calendar').style.display=\"none\";void(0);");
            //view.loadUrl("javascript:$(\'.calendar\').css(\'display\',\'none\');");
        }*/

    }

    private boolean isMailWriteInit = true;

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

        if (filePathCallbackLollipop != null) {
            filePathCallbackLollipop.onReceiveValue(null);
            filePathCallbackLollipop = null;
        }
        filePathCallbackLollipop = filePathCallback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getActivity().startActivityForResult(Intent.createChooser(intent, "File Chooser"), FILECHOOSER_LOLLIPOP_REQ_CODE);
        //getActivity().startActivityForResult(intent, FILECHOOSER_LOLLIPOP_REQ_CODE);
    }

    @Override
    public void onFileUpload(ValueCallback<Uri> uploadMsg) {
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(null);
            mUploadMessage = null;
        }
        mUploadMessage = uploadMsg;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getActivity().startActivityForResult(Intent.createChooser(intent, "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction()!=KeyEvent.ACTION_DOWN) {
            return true;
        }
        Log.i("backKey", "mail onKey back");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                Log.d("backKey", "canGoBack");
            } else {
                Log.d("backKey", "canNotGoBack");
                getActivity().onBackPressed();
            }
            return true;
        }
        return false;
    }

    @Override
    public void showIndicator() {
        indicatorLayout.setVisibility(View.VISIBLE);
        avi.show();
    }

    @Override
    public void hideIndicator() {
        avi.hide();
        indicatorLayout.setVisibility(View.GONE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);


        void goBoardTab();
    }

    public void setFromMail(String fromMail){
        Log.d("backKey", "setFromMail : "+fromMail);
        this.fromMail = fromMail;
    }

    public void onMailSendPage(){
        Log.d("backKey", "onMailSendPage start : ");
        isMailWriteInit = true;
        webView.post(new Runnable() {
            @Override
            public void run() {
                Log.d("backKey", "onMailSendPage post start !!!! ");
                long time = System.currentTimeMillis();
                Log.i("sdfs", "time : "+time);
                //webView.loadUrl("http://mail.teruten.com/webmail/write");
                String value = "http://mail.teruten.com/webmail/lists#%7B%22s_fnum%22%3A%221%22%2C%22s_mread%22%3A%22%22%2C%22view%22%3A%22%22%2C%22list%22%3Atrue%2C%22type%22%3A%22%22%2C%22write%22%3A%22%252Fwebmail%252Fwrite%22%2C%22page%22%3A0%2C%22t%22%3A1503476850399%7D";

                webView.loadUrl(value);

                //webView.loadUrl("http://mail.teruten.com/webmail/lists#{\"s_fnum\":\"1\",\"s_mread\":\"\",\"view\":\"\",\"list\":true,\"type\":\"\",\"write\":\"%2Fwebmail%2Fwrite\",\"t\":"+time+",\"url\":\"%2Fwebmail%2Flists\"}");
            }
        });
    }

    public void onWebMailFileUpload(int resultCode, Intent data){
        Log.d("FILEupload", "onWebMailFileUpload start");
        if (filePathCallbackLollipop == null)
            return ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            filePathCallbackLollipop.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
        }
        filePathCallbackLollipop = null;
    }

    public void onWebMailFileUploadKitkat(int resultCode, Intent data){
        Log.d("FILEupload", "onWebMailFileUploadKitkat start");
        if (mUploadMessage == null)
            return ;

        mUploadMessage.onReceiveValue(data.getData());
        mUploadMessage = null;
    }
}
