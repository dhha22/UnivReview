package com.univreview.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.univreview.App;
import com.univreview.log.Logger;
import com.univreview.model.UpdateUser;
import com.univreview.network.Retro;
import com.univreview.util.ErrorUtils;


import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by DavidHa on 2016. 7. 13..
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.d("Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param refreshedToken The new token.
     */
    private void sendRegistrationToServer(String refreshedToken) {
        Logger.v("registration to server");
        UpdateUser user = new UpdateUser();
        user.setFcmId(refreshedToken);
        Retro.instance.getUserService().updateUserInfo(App.getHeader(), user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Logger::v, ErrorUtils::parseError);
    }


}
