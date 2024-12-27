package com.acagribahar.bitirme

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Bildirim bilgilerini al(başlık ve icerik)
        val title = remoteMessage.notification?.title ?: "Varsayılan Başlık"
        val body = remoteMessage.notification?.body ?: "Varsayılan İçerik"

        //Başlığı ve içeriği loglarda inceleyebiliriz.

        Log.e("Başlık",title)
        Log.e("İçerik",body)

        //Fonksiyon çağrısı
        bildirimleriOlustur(title,body)

        /*
        // Burada olusturdugumuz fsm tokenini logcatte goruntuleyebiliriz.
        Log.d("FCM", "Bildirim Başlığı: $title")
        Log.d("FCM", "Bildirim İçeriği: $body")

        // Bildirimi göster
        showNotification(title, body)

         */
    }

    //Bildirimleri oluşturan fonksiyonumuz.
    fun bildirimleriOlustur(title : String,body : String){

        val builder : NotificationCompat.Builder

        val bildirimYoneticisi = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this@MyFirebaseMessagingService,MainActivity::class.java)

        val gidilecekIntent = PendingIntent.getActivity(this@MyFirebaseMessagingService,1,intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        //Android versiyon kontolü(oreo ve üstü için gerekli)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            //channel bilgileri
            val kanalId = "kanalId"
            val kanalAd = "kanalAd"
            val kanalTanitim = "kanalTanitim"
            val kanalOnceligi = NotificationManager.IMPORTANCE_HIGH

            var kanal : NotificationChannel? = bildirimYoneticisi.getNotificationChannel(kanalId)

            if (kanal == null){

                kanal = NotificationChannel(kanalId,kanalAd,kanalOnceligi)
                kanal.description = kanalTanitim
                bildirimYoneticisi.createNotificationChannel(kanal)
            }
            builder = NotificationCompat.Builder(this@MyFirebaseMessagingService,kanalId)

            builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.notificationicon)
                .setContentIntent(gidilecekIntent)
                .setAutoCancel(true)

        }else{

            builder = NotificationCompat.Builder(this@MyFirebaseMessagingService)

            builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.notificationicon)
                .setContentIntent(gidilecekIntent)
                .setAutoCancel(true)
                .priority = Notification.PRIORITY_HIGH
        }
        bildirimYoneticisi.notify(1,builder.build())


    }

    /*
    private fun showNotification(title: String, message: String) {
        val channelId = "default_channel_id"
        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Android 8.0 ve üstü için bildirim kanalı oluştur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Bildirimi oluştur ve göster
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // icon
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        notificationManager.notify(0, notificationBuilder.build())
    }

     */
}