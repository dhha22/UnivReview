package com.univreview.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

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

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DavidHa on 2016. 12. 25..
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.facebook_login_btn) Button facebookLoginBtn;
    private CallbackManager facebookCallbackManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //facebook
        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginBtn.setOnClickListener(v -> facebookLogin());

    }

    private void facebookLogin(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(facebookCallbackManager, facebookCallback);
    }

    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(final LoginResult loginResult) {
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), (object, response) -> {
                Logger.v(response.toString());
                try {

                    loginResult.getAccessToken().getToken();    //facebookAccessToken
                    object.getString("id");   //facebook user id
                    object.getJSONObject("picture").getJSONObject("data").getString("url"); //facebook profile image
                } catch (Exception e) {
                    Logger.e(e.toString());
                }

                //facebook email이 없는 경우
                try {
                    object.getString("email");  //get user email
                } catch (Exception e) {
                    Logger.e(e.toString());
                }


                //call facebook login api (send facebook user id & facebook access token)

            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, email, picture.type(large)");
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
