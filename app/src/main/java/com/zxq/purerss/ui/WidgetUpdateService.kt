package com.zxq.purerss.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.util.*
import kotlin.concurrent.timerTask

class WidgetUpdateService : Service() {
    private var mHandle: Handler? = null
    private var mRunnable: Runnable? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        var notificationChannel =
            NotificationChannel("channel", "forservicechannel", NotificationManager.IMPORTANCE_MIN)
        if (null != manager) {
            manager.createNotificationChannel(notificationChannel)
            var channel = NotificationCompat.Builder(this, "channel").build()
            startForeground(1, channel);
        }
        /*   mHandle?.postDelayed({
               val intent = Intent("action_update_ui")
               intent.component = ComponentName(this, RssWidget::class.java)
               sendBroadcast(intent)
               Log.e("WidgetUpdateService", "time count")
               mHandle?.postDelayed(mRunnable!!, 1000)
           }, 3000)*/
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        if (mHandle == null) {
            mHandle = Handler()
        }
        var checkTokenTimer = Timer()
        checkTokenTimer.schedule(timerTask {
            val intent = Intent("action_update_ui_text")
            intent.component = ComponentName(this@WidgetUpdateService, RssWidgetText::class.java)
            sendBroadcast(intent)
            Log.e("WidgetUpdateService", "time count")
        }, 3000, 1000)
        /*  mRunnable = Runnable {
              val intent = Intent("action_update_ui")
              intent.component = ComponentName(this, RssWidget::class.java)
              sendBroadcast(intent)
              Log.e("WidgetUpdateService", "time count")
              mHandle?.postDelayed(mRunnable!!, 1000)
          }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandle?.removeCallbacksAndMessages(null)
        Log.e("WidgetUpdateService", "service stop")
    }
}