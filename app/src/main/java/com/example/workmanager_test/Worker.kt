package com.example.workmanager_test

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class Worker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    lateinit var manager: NotificationManager
    lateinit var builder: NotificationCompat.Builder

    companion object {
        private const val TAG = "Worker"

        private const val D_CHANNEL_ID: String = "Notification Channel ID"
        private const val D_CHANNEL_NAME: String = "Notification Channel NAME"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        Log.d(TAG, "onDoWork")
        val context = applicationContext

        manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(
            NotificationChannel(
                D_CHANNEL_ID, D_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            )
        )

        builder = NotificationCompat.Builder(context, D_CHANNEL_ID)

        builder.apply {
            setContentTitle("TEST")
            setContentText("TESTEST")
            setSmallIcon(R.drawable.ic_launcher_foreground)
            priority = NotificationCompat.PRIORITY_HIGH
            setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            setAutoCancel(true)
        }

        val notification: Notification = builder.build()
        manager.notify(1, notification)

        return Result.success()
    }

}