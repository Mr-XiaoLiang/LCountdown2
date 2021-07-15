package com.lollipop.lcountdown.activity

import android.os.Bundle
import com.lollipop.lcountdown.databinding.ActivityAdjustmentBinding
import com.lollipop.lcountdown.utils.lazyBind

class AdjustmentActivity : BaseActivity() {

    private val binding: ActivityAdjustmentBinding by lazyBind()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding)
    }
}