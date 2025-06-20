package com.lollipop.countdown.calculator

import android.view.View

class ButtonManager(
    private val buttonClickListener: ButtonClickListener
) {

    val buttonList = mutableListOf<ButtonHolder>()

    fun register(key: ButtonKey, button: View) {
        buttonList.add(ButtonHolder(button, key, buttonClickListener))
    }

}


class ButtonHolder(
    val view: View,
    val buttonKey: ButtonKey,
    val clickListener: ButtonClickListener
) {

    init {
        view.setOnClickListener {
            clickListener.onButtonClick(buttonKey)
        }
    }

}

fun interface ButtonClickListener {
    fun onButtonClick(button: ButtonKey)
}

