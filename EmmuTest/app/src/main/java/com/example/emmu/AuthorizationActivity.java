package com.example.emmu;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yandex.authsdk.YandexAuthLoginOptions;
import com.yandex.authsdk.YandexAuthOptions;
import com.yandex.authsdk.YandexAuthSdk;
import com.yandex.authsdk.YandexAuthToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class AuthorizationActivity extends AppCompatActivity {
    private static final int REQUEST_LOGIN_SDK = 1;
    private String token = "";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private YandexAuthSdk sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization_layout);

        Context context = this;

        sdk = new YandexAuthSdk(context, new YandexAuthOptions(context, true, 0));

        Button loginButton = findViewById(R.id.button_login_yandex);
        loginButton.setOnClickListener(v -> {
            ActivityCompat.startActivityForResult(this,
                    sdk.createLoginIntent(new YandexAuthLoginOptions.Builder().build()),
                    REQUEST_LOGIN_SDK,
                    null);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOGIN_SDK) {
            try {
                YandexAuthToken yandexAuthToken = sdk.extractToken(resultCode, data);

                if (yandexAuthToken != null)
                    setToken(yandexAuthToken.getValue());

                Log.e("token", token);
            } catch (Exception ignore) {
            }
        }

        if (token != null && !token.equals("") && !token.equals("None")) {
            try {
                //updateToken();
                sendPostRequest();
            } catch (Exception e) {
                Log.e("Login error", e.getMessage());
            }
        }
    }


    public void sendPostRequest() {
        AsyncHttpClient client = new AsyncHttpClient();

        String fullUrl = "http://192.168.43.69:5000/sendtoken" + "?token=" + getToken();

        client.post(fullUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                // Обработка успешного ответа сервера
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                // Обработка ошибки
            }
        });
    }

}
