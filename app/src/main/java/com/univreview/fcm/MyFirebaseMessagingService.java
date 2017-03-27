package com.univreview.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.univreview.R;
import com.univreview.activity.MainActivity;
import com.univreview.log.Logger;
import com.univreview.util.Util;


/**
 * Created by DavidHa on 2016. 7. 13..
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("service on create");

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.d("From: " + remoteMessage.getFrom());
        Logger.d("Notification: " + remoteMessage.getData().toString());
        sendNotification(0, null, null, null, remoteMessage.getData().toString());
       /* final long id = Long.parseLong(remoteMessage.getData().get("object_id"));
        final String action = remoteMessage.getData().get("action");
        final String type = remoteMessage.getData().get("object_type");
        final String profileURL = remoteMessage.getData().get("image_url");
        final String messageBody = remoteMessage.getData().get("body");*/

       /* Bitmap bitmap;
        try {
            URL url = new URL(profileURL);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            bitmap = ImageUtils.centerCrop(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_profile);
        }
*/
       // sendNotification(id, action, type, null, messageBody);



    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(long id, String action, String type, Bitmap profileBitmap, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        intent.putExtra("action", action);


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("대학리뷰")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
