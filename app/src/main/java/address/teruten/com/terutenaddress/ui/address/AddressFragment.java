package address.teruten.com.terutenaddress.ui.address;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.data.Define;
import address.teruten.com.terutenaddress.webkit.CustomWebChromeClient;
import address.teruten.com.terutenaddress.webkit.CustomWebView;
import address.teruten.com.terutenaddress.webkit.CustomWebViewClient;
import address.teruten.com.terutenaddress.webkit.listener.OnWebViewListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressFragment extends Fragment implements OnWebViewListener, View.OnKeyListener {
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

    private boolean isWebDetailPopup = false;

    public AddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        loadingProgressBar = (ContentLoadingProgressBar) getActivity().findViewById(R.id.address_web_view_load_progress_bar);
        webView = (CustomWebView) getActivity().findViewById(R.id.address_webView);

        webView.setOnWebViewListener(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new CustomWebChromeClient(getActivity(), this));
        webView.setWebViewClient(new CustomWebViewClient(this));
        webView.defaultInit(WebSettings.LOAD_NO_CACHE);
        webView.clearCache(true);
        webView.clearHistory();
        webView.setOnKeyListener(this);
        webView.addJavascriptInterface(this, "AndroidBridge");
        webView.loadUrl(Define.INTRANET_DOMAIN_BASE_URL+"department/orgChart.do");
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
    public void onResume() {
        super.onResume();
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
        if (keyEvent.getAction()!=KeyEvent.ACTION_DOWN) {
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isWebDetailPopup){
                webView.loadUrl("javascript:modalClose();");
                isWebDetailPopup = false;
            }else if (webView.canGoBack()) {
                webView.goBack();
            } else {
                getActivity().onBackPressed();
            }

            return true;
        }
        return false;
    }

    @Override
    public void onScroll(int l, int t, int oldl, int oldt) {
        int height = (int) Math.floor(webView.getContentHeight() * webView.getScale());
        int webViewHeight = webView.getMeasuredHeight();
        if(webView.getScrollY() + webViewHeight >= height){
            Log.i("THE END", "reached");
        }
    }

    @Override
    public void onFinish(WebView view, String url) {
        Intent intent = null;
        if(url.startsWith("tel:")){
            webView.goBack();
            intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            startActivity(intent);

        }else if(url.startsWith("sms:")){
            webView.goBack();
            intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            startActivity(intent);

        }else if(url.startsWith("mailto:")){
            webView.goBack();
            intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            startActivity(intent);

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

    }

    @Override
    public void onFileUpload(ValueCallback<Uri> uploadMsg) {

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

        void goMailWrite(String recipient);
    }

    @JavascriptInterface
    public void phoneNumberShare(String name, String phoneNumber){
        Toast.makeText(getActivity(), "name : "+ name+ ", phone : "+phoneNumber, Toast.LENGTH_SHORT).show();

        String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getContext()); //Need to change the build to API 19

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, name+" "+phoneNumber);

        if (defaultSmsPackageName != null)//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
        {
            sendIntent.setPackage(defaultSmsPackageName);
        }
        startActivity(sendIntent);

    }

    @JavascriptInterface
    public void shareMail(String text){
        mListener.goMailWrite(text+"@teruten.com");
    }

    @JavascriptInterface
    public void shareSMS(String text){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:"+text));
        startActivity(intent);
    }

    @JavascriptInterface
    public void sharePhoneNumber(String text){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+text));
        startActivity(intent);
    }

    @JavascriptInterface
    public void showWebDetailPopup(boolean flag){
        isWebDetailPopup = flag;
    }

    @JavascriptInterface
    public void showTestToast(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
