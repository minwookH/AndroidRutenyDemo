package address.teruten.com.terutenaddress.ui.board;

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
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.webkit.CustomWebChromeClient;
import address.teruten.com.terutenaddress.webkit.CustomWebView;
import address.teruten.com.terutenaddress.webkit.CustomWebViewClient;
import address.teruten.com.terutenaddress.webkit.listener.OnWebViewListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BoardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardFragment extends Fragment implements OnWebViewListener, View.OnKeyListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private CustomWebView webView;
    private ContentLoadingProgressBar loadingProgressBar;
    private TerutenSharedpreferences terutenSharedpreferences;

    private boolean isInit = true;


    public static final int BOARD_FILECHOOSER_RESULTCODE   = 10;
    public final static int BOARD_FILECHOOSER_LOLLIPOP_REQ_CODE = 20;
    private ValueCallback<Uri> mUploadMessage = null;
    private ValueCallback<Uri[]> filePathCallbackLollipop;

    public BoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoardFragment newInstance(String param1, String param2) {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        terutenSharedpreferences = new TerutenSharedpreferences(getContext());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_board, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        loadingProgressBar = (ContentLoadingProgressBar) getActivity().findViewById(R.id.board_web_view_load_progress_bar);
        webView = (CustomWebView) getActivity().findViewById(R.id.board_webView);

        webView.setOnWebViewListener(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new CustomWebChromeClient(getActivity(), this));
        webView.setWebViewClient(new CustomWebViewClient(this));
        webView.defaultInit(WebSettings.LOAD_NO_CACHE);
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
        webView.loadUrl("http://mail.teruten.com/board");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
//실제로 사용자 눈에 보이는경우
        }
        else
        {
// 전페이지에서 Preload 될 경우
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
        Log.i("board", "height : "+height+", webViewHeight : "+webViewHeight);
        if(webView.getScrollY() + webViewHeight >= height){
            Log.i("board", "reached");
        }
    }

    private String tempUrl;

    @Override
    public void onFinish(WebView view, String url) {
        String id = terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_ID);
        String password = terutenSharedpreferences.getStringPreferences(TerutenSharedpreferences.USER_PW);

        String decodeUrl = null;
        try {
            decodeUrl = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(!TextUtils.equals(tempUrl, decodeUrl)){
            tempUrl = decodeUrl;
            if(!TextUtils.isEmpty(decodeUrl) && decodeUrl.contains("http://mail.teruten.com/member/login?t=")){
                if(isInit){
                    view.loadUrl("javascript:document.getElementById('cid').value = '"+id+"';document.getElementById('cpw').value='"+password+"';void(0);");
                    view.loadUrl("javascript:document.forms['frmlogin'].submit();");
                }

            }else if(!TextUtils.isEmpty(decodeUrl) && decodeUrl.contains("http://mail.teruten.com/member/login?result=")){
                //pass
                //http://mail.teruten.com
            }else if(!TextUtils.isEmpty(decodeUrl) && decodeUrl.contains("http://mail.teruten.com/webmail/lists")
                    && isInit){
                //pass
                view.loadUrl("http://mail.teruten.com/board");
            }else if(!TextUtils.isEmpty(decodeUrl)
                    //&& isInit
                    && decodeUrl.contains("http://mail.teruten.com/board")
                    && !decodeUrl.contains("\"base_list\"")){
                //isInit = false;
                view.clearHistory();

            }else if(
                //!isInit &&
                    decodeUrl.contains("http://mail.teruten.com/webmail/lists")){
                view.loadUrl("http://mail.teruten.com/board");
                if(mListener != null){
                    //mListener.goMailTab();
                }

            }
        }
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
        if (filePathCallbackLollipop != null) {
            filePathCallbackLollipop.onReceiveValue(null);
            filePathCallbackLollipop = null;
        }
        filePathCallbackLollipop = filePathCallback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        getActivity().startActivityForResult(Intent.createChooser(intent, "File Chooser"), BOARD_FILECHOOSER_LOLLIPOP_REQ_CODE);
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
        getActivity().startActivityForResult(Intent.createChooser(intent, "File Chooser"), BOARD_FILECHOOSER_RESULTCODE);
    }


    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction()!=KeyEvent.ACTION_DOWN) {
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                getActivity().onBackPressed();
            }
            return true;
        }
        return false;
    }

    public void onWebBoardFileUpload(int resultCode, Intent data){
        if (filePathCallbackLollipop == null)
            return ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            filePathCallbackLollipop.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
        }
        filePathCallbackLollipop = null;
    }

    public void onWebBoardFileUploadKitkat(int resultCode, Intent data){
        if (mUploadMessage == null)
            return ;

        mUploadMessage.onReceiveValue(data.getData());
        mUploadMessage = null;
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

        void goMailTab();
    }
}
