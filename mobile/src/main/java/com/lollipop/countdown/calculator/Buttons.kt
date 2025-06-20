package com.lollipop.countdown.calculator

interface ButtonHolder {

    val buttonKey: ButtonKey

    fun setEnable(enable: Boolean)

}

interface ButtonController {

    fun updateButton(callback: (ButtonHolder) -> Unit)

}
