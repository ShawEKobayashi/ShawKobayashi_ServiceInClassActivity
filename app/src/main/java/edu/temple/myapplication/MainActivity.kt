package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler.createAsync
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.widget.Button
import android.widget.TextView
import java.util.Timer
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {

    private var started = false

    private val handler = android.os.Handler(Looper.getMainLooper()) {
        findViewById<TextView>(R.id.textView).text = it.what.toString()
        true
    }

    private lateinit var timerBinder : TimerService.TimerBinder

    private val serviceConnection = object: ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = service as TimerService.TimerBinder
            timerBinder.setHandler(handler)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindService(Intent(this,TimerService::class.java),serviceConnection, BIND_AUTO_CREATE)


        findViewById<Button>(R.id.startButton).setOnClickListener {
            if (!timerBinder.isRunning) {
                timerBinder.start(10)
            }
            else{
                timerBinder.pause()
            }
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            timerBinder.stop()
        }
    }
}