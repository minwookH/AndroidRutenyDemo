package address.teruten.com.terutenaddress.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import address.teruten.com.terutenaddress.R;
import address.teruten.com.terutenaddress.preferences.TerutenSharedpreferences;
import address.teruten.com.terutenaddress.ui.intro.IntroActivity;

/**
 * Created by teruten on 2018-01-09.
 */

public class TerutenFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM";
    private int notiId;
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.i(TAG, "Message data payload: " + remoteMessage.getData());

            /*if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*/

        }
        Log.i(TAG, "remoteMessage.getNotification() : " + remoteMessage.getNotification());
        Log.d(TAG, "Message getMessageType : " + remoteMessage.getMessageType());
        Log.d(TAG, "Message getData : " + remoteMessage.getData());


        TerutenSharedpreferences terutenSharedpreferences = new TerutenSharedpreferences(getApplicationContext());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() == null
                && terutenSharedpreferences.getBooleanPreferences("setNotification")) {
            try {

                Log.d(TAG, "Message getData body: " + remoteMessage.getData().get("body"));
                Log.d(TAG, "Message getData title: " + remoteMessage.getData().get("title"));
                Log.d(TAG, "Message getData message: " + remoteMessage.getData().get("message"));


                Intent intent = new Intent(this, IntroActivity.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
                intent.putExtra("push", "mail");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short

                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                Notification.Builder builder = new Notification.Builder(this);

                builder.setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("message"))
                        .setSmallIcon(R.drawable.ic_stat_teruten_background_logo)
                        .setContentIntent(pIntent)
                        .setPriority(Notification.PRIORITY_HIGH)
                        //.setFullScreenIntent(pIntent, true)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_VIBRATE);

                /*if(terutenSharedpreferences.getBooleanPreferences("isNotificationVibrate")
                        && terutenSharedpreferences.getBooleanPreferences("isNotificationSound")){
                    builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                }else if(terutenSharedpreferences.getBooleanPreferences("isNotificationVibrate")
                        && !terutenSharedpreferences.getBooleanPreferences("isNotificationSound")){
                    builder.setDefaults(Notification.DEFAULT_VIBRATE);
                }else if(!terutenSharedpreferences.getBooleanPreferences("isNotificationVibrate")
                        && terutenSharedpreferences.getBooleanPreferences("isNotificationSound")){
                    builder.setDefaults(Notification.DEFAULT_SOUND);
                }*/

                Log.d("dfsdf", "setNotification : "+terutenSharedpreferences.getBooleanPreferences("setNotification"));
                Log.d("dfsdf", "isNotificationSound : "+terutenSharedpreferences.getBooleanPreferences("isNotificationSound"));
                Log.d("dfsdf", "isNotificationVibrate : "+terutenSharedpreferences.getBooleanPreferences("isNotificationVibrate"));


                Notification notification = builder.build();

                notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                Log.d("FCM", "time : "+System.currentTimeMillis());
                String key = String.valueOf(System.currentTimeMillis()).substring(5, 9);

                notiId = Integer.parseInt(key);
                notificationManager.notify(notiId, notification);


                /*Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cancelNoti();
                    }
                }, 3000);*/

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void cancelNoti(){
        notificationManager.cancel(notiId);
    }
}
