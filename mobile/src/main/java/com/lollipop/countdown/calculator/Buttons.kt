package com.lollipop.countdown.calculator

import android.util.Log
import android.view.View

class ButtonManager(
    private val buttonClickListener: ButtonClickListener
) : ButtonProvider {

    val buttonList = mutableListOf<ButtonHolder>()

    fun register(holder: ButtonHolder) {
        holder.clickListener = buttonClickListener
        buttonList.add(holder)
    }

    override fun updateButtons(controller: ButtonController) {
        for (button in buttonList) {
            try {
                controller.updateButton(button)
            } catch (e: Throwable) {
                Log.e("ButtonManager", "updateButtons error", e)
            }
        }
    }

}

interface ButtonProvider {

    fun updateButtons(controller: ButtonController)

}

interface ButtonController {

    fun updateButton(holder: ButtonHolder)

}

abstract class ButtonHolder(
    val buttonKey: ButtonKey,
) {

    var clickListener: ButtonClickListener? = null

    protected fun bind(view: View) {
        view.setOnClickListener { notifyClick() }
    }

    fun notifyClick() {
        clickListener?.onButtonClick(buttonKey)
    }

    abstract fun setEnable(enable: Boolean)

}

fun interface ButtonClickListener {
    fun onButtonClick(button: ButtonKey)
}

