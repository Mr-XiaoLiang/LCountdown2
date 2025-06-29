package com.lollipop.countdown.page

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lollipop.countdown.R
import com.lollipop.countdown.base.BasicActivity
import com.lollipop.countdown.databinding.ActivityMainBinding

class MainActivity : BasicActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreateContentView(): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        addOption(R.drawable.baseline_calculate_24) {
            startActivity(Intent(this, CalculatorActivity::class.java))
        }
        binding.timerButton.setOnClickListener {
            startActivity(Intent(this, TimerCreateActivity::class.java))
        }
        binding.countdownButton.setOnClickListener {
            startActivity(Intent(this, CountdownCreateActivity::class.java))
        }
    }

}