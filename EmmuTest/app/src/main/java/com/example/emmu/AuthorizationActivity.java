package com.example.emmu;

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
                    token = yandexAuthToken.getValue().toString();
                Log.e("token", token);
            } catch (Exception ignore) {
            }
        }

        if (token != null && !token.equals("") && !token.equals("None")) {
            try {
                updateToken();
            } catch (Exception e) {
                Log.e("Login error", e.getMessage());
            }
        }
    }

    private void updateToken() throws JSONException, UnsupportedEncodingException {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                TextView tokenInfoTextView = findViewById(R.id.token_info_yandex);
                tokenInfoTextView.setText(R.string.token_linked_yandex);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        };

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("token", token);
        StringEntity entity = new StringEntity(jsonParams.toString());

        asyncHttpClient.post(getApplicationContext(),
                ServerInfo.buildUrl("sendtoken"),
                entity,
                "application/json", handler);
    }
}
