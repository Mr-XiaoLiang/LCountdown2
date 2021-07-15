package com.lollipop.lcountdown.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * @author lollipop
 * @date 2021/7/15 23:05
 */
open class BaseActivity: AppCompatActivity() {

    protected fun setContentView(binding: ViewBinding) {
        setContentView(binding.root)
    }

}