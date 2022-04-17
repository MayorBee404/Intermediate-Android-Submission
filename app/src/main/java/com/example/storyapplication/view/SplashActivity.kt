package com.example.storyapplication.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.example.storyapplication.R
import com.example.storyapplication.ViewModelFactory
import com.example.storyapplication.view.authentication.AuthenticationViewModel
import com.example.storyapplication.view.authentication.MainActivity
import com.example.storyapplication.view.dashboard.DashboardActivity
import com.example.storyapplication.view.onboarding.OnBoardingActivity
import kotlinx.coroutines.delay

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    private lateinit var factory: ViewModelFactory
    private val authenticationViewModel: AuthenticationViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        factory = ViewModelFactory.getInstance(this)
        setupView()

        authenticationViewModel.getIsFirstTime().observe(this) { isFirstTime
            ->
            val delayMillis: Long = 2000
            if (isFirstTime) {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                    startActivity(intent)
                    finish()
                },delayMillis)
            }else{
                authenticationViewModel.getUserToken().observe(this){ token->
                    if (token.isNullOrEmpty() || token == "not_set_yet"){
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@SplashActivity,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        },delayMillis)
                    }else{
                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this@SplashActivity,DashboardActivity::class.java)
                            startActivity(intent)
                            finish()
                        },delayMillis)
                    }
                }
            }
        }
    }

    private fun setupView() {

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

    }

}


