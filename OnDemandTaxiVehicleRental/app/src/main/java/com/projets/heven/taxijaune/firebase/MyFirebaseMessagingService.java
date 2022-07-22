package com.projets.heven.taxijaune.firebase;

/**
 * Created by Woumtana Pingdiwindé Youssouf 03/2019
 * Tel: +226 63 86 22 46 - 73 35 41 41
 * Email: issoufwoumtana@gmail.com
 **/

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.projets.heven.taxijaune.R;
import com.projets.heven.taxijaune.activity.MainActivity;
import com.projets.heven.taxijaune.activity.TchatActivity;
import com.projets.heven.taxijaune.fragment.FragmentMesRequetes;
import com.projets.heven.taxijaune.fragment.FragmentRequete;
import com.projets.heven.taxijaune.helper.Helper;
import com.projets.heven.taxijaune.model.M;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private String NOTIFICATION_TITLE = "Notification Sample App";
    private String CONTENT_TEXT = "Expand me to see a detailed message!";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();
        String tag = remoteMessage.getNotification().getTag();

//        Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();


//        if (!Helper.isAppRunning(getBaseContext(), getResources().getString(R.string.package_name))) {
//            // App is not running
//            if(M.isPushNotify(getBaseContext()) && !M.getID(getBaseContext()).equals(""))
//                sendNotification(title, message);
//        } else {
//            // App is running
////            Intent localMessage = new Intent(CurrentActivityReceiver.CURRENT_ACTIVITY_ACTION);
////            LocalBroadcastManager.getInstance(getBaseContext()).
////                    sendBroadcast(localMessage);
//        }
        sendNotification(title, message, tag);
    }

    @SuppressLint("WrongConstant")
    public void sendNotification(String title, String messageBody, String tag) {
        String tabMessage[] = {};
        Intent intent = new Intent(this, MainActivity.class);
        if(tag.length() != 0) {
            if (tag.equals("requete"))
                intent.putExtra("fragment_name", "requete");
            else if (tag.equals("mes_requetes"))
                intent.putExtra("fragment_name", "mes_requetes");
            else if(tag.equals("reservation"))
                intent.putExtra("fragment_name", "");
            else if(tag.equals("payment_msg"))
                intent.putExtra("fragment_name", "");
            else if(tag.equals("all_customers"))
                intent.putExtra("fragment_name", "");
            else {
                intent = new Intent(this, TchatActivity.class);
                tabMessage = tag.split("_");
                String id_envoyeur = tabMessage[0];
                String id_receveur = tabMessage[1];
                String id_requete = tabMessage[2];
                String nom_receveur = tabMessage[3];
                intent.putExtra("id_envoyeur",id_envoyeur);
                intent.putExtra("id_receveur",Integer.parseInt(id_receveur));
                intent.putExtra("id_requete",Integer.parseInt(id_requete));
                intent.putExtra("nom_receveur",nom_receveur);
            }
        }else{
            intent.putExtra("fragment_name", "");
        }

        if(!M.getID(getBaseContext()).equals("")) {
            if (MainActivity.getCurrentFragment().getTag().equals("requete") && FragmentRequete.connectionDetector.isConnectingToInternet()) {
                new FragmentRequete.getRequete().execute();
            } else if (MainActivity.getCurrentFragment().getTag().equals("mes_requetes") && FragmentMesRequetes.connectionDetector.isConnectingToInternet()) {
                new FragmentMesRequetes.getMesRequete().execute();
            }
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channel_id=this.getResources().getString(R.string.app_name);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        @SuppressLint("WrongConstant") NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channel_id)
                .setContentTitle(title)
                .setSubText("")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setChannelId(channel_id)
                .setContentIntent(pendingIntent);

        //Vibration
        long[] v = {500,1000};
        notificationBuilder.setVibrate(v);
        //LED
        notificationBuilder.setLights(Color.RED, 3000, 3000);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.ic_car_top_view)
                    .setBadgeIconType(R.drawable.ic_car_top_view);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.ic_car_top_view)
                    .setBadgeIconType(R.drawable.ic_car_top_view);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(channel_id, channel_id, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//        Log.i("MyFirebaseMsgService",messageBody);
    }
}