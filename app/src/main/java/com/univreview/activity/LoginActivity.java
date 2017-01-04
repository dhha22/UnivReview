package com.univreview.activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.univreview.BaseActivity;
import com.univreview.R;
import com.univreview.log.Logger;
import com.univreview.util.Util;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Base64;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2016. 12. 25..
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.facebook_login_btn) Button facebookLoginBtn;
    @BindView(R.id.kakao_login_btn) Button kakaoLoginBtn;
    @BindView(R.id.text) TextView textView;
    private CallbackManager facebookCallbackManager;
    private ProgressDialog progressDialog;
    private String SALT_KEY = "e7b35739643f6f595e1a3231666138ca";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //facebook
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginBtn.setOnClickListener(v -> facebookLogin());
        makeAPIToken(SALT_KEY);

    }

    private void makeAPIToken(String saltKey){
        String timeStamp = "20170104102418";
        String appVersion = "0.1";
        String tokenToS = "";
        Logger.v("encrypt: " + Util.cryptoSHA3(appVersion + ';' + timeStamp + ';' + saltKey + tokenToS));
        Logger.v("encrypt uri encode: " + Util.cryptoSHA3(appVersion + ';' + timeStamp + ';' + saltKey + tokenToS));
    }



    private void facebookLogin(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(facebookCallbackManager, facebookCallback);
    }

    private void kakaoLogin(){

    }

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(final LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                Logger.v("facebook response" + response.toString());
                try {
                    loginResult.getAccessToken().getToken();    //facebookAccessToken
                    object.getString("id");   //facebook user id
                    object.getJSONObject("picture").getJSONObject("data").getString("url"); //facebook profile image
                } catch (Exception e) {
                    Logger.e(e.toString());
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, picture.type(large)");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException error) {
            progressDialog.dismiss();
            Logger.e(error);
            if (error instanceof FacebookAuthorizationException) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    LoginManager.getInstance().logOut();
                    facebookLogin();
                } else {

                }
            }
        }
    };
}
