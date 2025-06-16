package com.lollipop.countdown.calculator

class Formula {

    val options = mutableListOf<Option>()
    var stateCallback: StateCallback? = null

    var result: Result? = null

    fun put(option: Option) {
        options.add(option)
        stateCallback?.onLastAdd()
    }

    fun backspace() {
        val last = options.lastOrNull() ?: return
        val value = last.value
        if (value == 0) {
            options.remove(last)
            stateCallback?.onLastRemove()
            return
        }
        last.value = value / 10
        stateCallback?.onLastChanged()
    }

    interface StateCallback {
        fun onLastRemove()
        fun onLastChanged()
        fun onLastAdd()
    }

}