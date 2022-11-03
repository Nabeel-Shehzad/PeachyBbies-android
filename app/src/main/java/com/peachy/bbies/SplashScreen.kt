package com.peachy.bbies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.peachy.bbies.screens.MainActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        },3000)
    }
}