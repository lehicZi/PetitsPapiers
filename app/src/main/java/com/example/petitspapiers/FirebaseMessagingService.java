package com.example.petitspapiers;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.petitspapiers.constants.FilmizStatus;
import com.example.petitspapiers.objects.Filmiz;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private List<Filmiz> tireList;

    private static final String CANAL = "NotificationCanal" ;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String myMessage = remoteMessage.getNotification().getBody();
        Log.d("FirebaseMessage", "Notif : " + myMessage);

        

        //Création de la notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CANAL);
        notificationBuilder.setContentTitle("Film à télécharger !");
        notificationBuilder.setContentText("Des films présents dans la liste des films tirés sont à dl !");
        notificationBuilder.setSmallIcon(R.drawable.water_glass);

        //Envoi de la notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelID = "01";
            String channelTitle = "First notification channel";
            String channelDescription = "Notifications for film DL";
            NotificationChannel channel = new NotificationChannel(channelID,channelTitle,NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channelDescription);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelID);
        }
        notificationManager.notify(1, notificationBuilder.build());

    }
}
