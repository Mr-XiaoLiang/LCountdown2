package com.lollipop.countdown

import android.os.Bundle
import android.view.View
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

        }
        binding.timerButton.setOnClickListener {

        }
        binding.countdownButton.setOnClickListener {

        }
    }

}