package com.jinncyapps.activitymanager.activities

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.databinding.DataBindingUtil
import com.jinncyapps.activitymanager.R
import com.jinncyapps.activitymanager.databinding.ActivitySplashBinding
import com.jinncyapps.activitymanager.firebase.FirestoreClass

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        val font: Typeface = Typeface.createFromAsset(assets, "MotionPicture_PersonalUseOnly.ttf")
        binding.tvAppName.typeface = font

        Handler().postDelayed({
            val currentUserID = FirestoreClass().getCurentUserID()
            // Start the Intro Activity

            if (currentUserID.isNotEmpty()) {
                // Start the Main Activity
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                // Start the Intro Activity
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
            }
            finish() }, 2500)
    }
}