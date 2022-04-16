package com.example.storyapplication.view.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.storyapplication.R
import com.example.storyapplication.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

    }
}