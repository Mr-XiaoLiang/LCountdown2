package com.lollipop.countdown.page

import android.view.View
import com.lollipop.countdown.base.BasicActivity
import com.lollipop.countdown.databinding.ActivityCalculatorBinding

class CalculatorActivity: BasicActivity() {

    private val binding by lazy {
        ActivityCalculatorBinding.inflate(layoutInflater)
    }

    override fun onCreateContentView(): View {
        return binding.root
    }
}