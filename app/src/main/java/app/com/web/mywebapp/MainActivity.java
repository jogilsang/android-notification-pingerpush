package app.com.web.mywebapp;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.fingerpush.android.FingerPushManager;
import com.fingerpush.android.NetworkUtility;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    WebView mWebView;
    TextView errorVeiw;

    static final int REQUEST_CODE_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);
            } else {
                setDevice();
            }
        } else {
            setDevice();
        }


        FingerPushManager.getInstance(getApplicationContext()).setDevice(new NetworkUtility.ObjectListener() {
            @Override
            public void onComplete(String code, String message, JSONObject jsonObject) {
                // code 200, 201 이 반환된 경우 정상적으로 디바이스가 등록된 것입니다.
            }

            @Override
            public void onError(String code, String message) {
                // 코드 504 가 반환된 경우 이미 디바이스가 등록된 상태입니다.
            }
        });


        // 언제든지 푸시 데이터가 오면 푸시가 실행될수 있도록 해줍니다. (항상 대기..) 여기서 notice는 푸시 받을 곳의 이름 입니다.
        //FirebaseMessaging.getInstance().subscribeToTopic("notice");


        errorVeiw = (TextView) findViewById(R.id.net_error_view);

        mWebView = (WebView) findViewById(R.id.activity_main_webview);


        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);


        mWebView.setWebViewClient(new WebViewClient() {

            @Override

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);

                return true;

            }

            //네트워크연결에러

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                switch (errorCode) {

                    case ERROR_AUTHENTICATION:
                        break;               // 서버에서 사용자 인증 실패

                    case ERROR_BAD_URL:
                        break;                           // 잘못된 URL

                    case ERROR_CONNECT:
                        break;                          // 서버로 연결 실패

                    case ERROR_FAILED_SSL_HANDSHAKE:
                        break;    // SSL handshake 수행 실패

                    case ERROR_FILE:
                        break;                                  // 일반 파일 오류

                    case ERROR_FILE_NOT_FOUND:
                        break;               // 파일을 찾을 수 없습니다

                    case ERROR_HOST_LOOKUP:
                        break;           // 서버 또는 프록시 호스트 이름 조회 실패

                    case ERROR_IO:
                        break;                              // 서버에서 읽거나 서버로 쓰기 실패

                    case ERROR_PROXY_AUTHENTICATION:
                        break;   // 프록시에서 사용자 인증 실패

                    case ERROR_REDIRECT_LOOP:
                        break;               // 너무 많은 리디렉션

                    case ERROR_TIMEOUT:
                        break;                          // 연결 시간 초과

                    case ERROR_TOO_MANY_REQUESTS:
                        break;     // 페이지 로드중 너무 많은 요청 발생

                    case ERROR_UNKNOWN:
                        break;                        // 일반 오류

                    case ERROR_UNSUPPORTED_AUTH_SCHEME:
                        break; // 지원되지 않는 인증 체계

                    case ERROR_UNSUPPORTED_SCHEME:
                        break;          // URI가 지원되지 않는 방식

                }

                super.onReceivedError(view, errorCode, description, failingUrl);


                mWebView.setVisibility(View.GONE);

                errorVeiw.setVisibility(View.VISIBLE);

            }

        });

        mWebView.setWebChromeClient(new WebChromeClient() {


            //alert 처리

            @Override

            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

                new AlertDialog.Builder(view.getContext())

                        .setTitle("알림")

                        .setMessage(message)

                        .setPositiveButton(android.R.string.ok,

                                new AlertDialog.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                        result.confirm();

                                    }

                                })

                        .setCancelable(false)

                        .create()

                        .show();

                return true;

            }


            //confirm 처리

            @Override
            public boolean onJsConfirm(WebView view, String url, String message,

                                       final JsResult result) {

                new AlertDialog.Builder(view.getContext())

                        .setTitle("알림")

                        .setMessage(message)

                        .setPositiveButton("Yes",

                                new AlertDialog.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                        result.confirm();

                                    }

                                })

                        .setNegativeButton("No",

                                new AlertDialog.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                        result.cancel();

                                    }

                                })

                        .setCancelable(false)

                        .create()

                        .show();

                return true;

            }

        });


        mWebView.loadUrl("http://m.blog.naver.com/jogilsang");


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_STORAGE:
                setDevice();
                break;
        }
    }

    public void setDevice(){

        FingerPushManager.getInstance(getApplicationContext()).setDevice(new NetworkUtility.ObjectListener() {
            @Override
            public void onComplete(String code, String message, JSONObject jsonObject) {
                // code 200, 201 이 반환된 경우 정상적으로 디바이스가 등록된 것입니다.
            }

            @Override
            public void onError(String code, String message) {
                // 코드 504 가 반환된 경우 이미 디바이스가 등록된 상태입니다.
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);

    }


}
