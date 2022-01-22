package com.example.workmanager_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.workmanager_test.databinding.ActivityMainBinding
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            doWorkManager()
        }
    }

    private fun doWorkManager() {
        Log.d(TAG, "onDoWorkManager")
        val calendar = Calendar.getInstance()
        val delay = calculateDelay(calendar)

        val periodWorkRequest = PeriodicWorkRequestBuilder<Worker>(15, TimeUnit.MINUTES)
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .addTag("TestWorker")
            .build()
        WorkManager.getInstance(this).enqueue(periodWorkRequest)

    }

    private fun calculateDelay(startCalendar: Calendar): Long {
        // 오늘 알람이 울릴 시각 설정
        val todayCalendar = Calendar.getInstance()
        todayCalendar.set(Calendar.HOUR_OF_DAY, 17)
        todayCalendar.set(Calendar.MINUTE, 40)
        todayCalendar.add(Calendar.DAY_OF_YEAR, 0)

        var delay = (todayCalendar.time.time - startCalendar.time.time) / 1000

        // 만약에 시작한 시점의 시간의 값을 뺐을 때 음수가 나오면 시간이 제대로 설정되지 않으므로
        // todayCalendar에 24시간을 추가해서 현재 시각(startCalendar)의 값을 빼더라도
        // 알림을 등록된 시각에 맞춰서 설정.
        // 현재 시각이 13시, 알림으로 지정된 시각이 4시라면 (알림시각) = 4 + 24 = 28
        // 28 - 13 = 15시간 후에 알림.. 현재 시각은 13시니 13 + 15 = 28 = 새벽 4시
        return if (delay < 0) {
            todayCalendar.add(Calendar.DAY_OF_YEAR, 1)
            (todayCalendar.time.time - startCalendar.time.time) / 1000
        } else {
            delay
        }
    }

}