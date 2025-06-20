package com.lollipop.countdown.calculator

import android.view.View

class DateCalculator : ButtonClickListener {

    private val buttonManager = ButtonManager(this)

    val historyList = mutableListOf<Formula>()

    var currentFormula: FormulaCalculator? = null

    var previewCallback: PreviewCallback? = null

    private val previewWrapper = object : PreviewCallback {
        override fun onPreview(result: DateResult?) {
            notifyPreview(result)
        }
    }

    fun register(key: ButtonKey, view: View) {
        buttonManager.register(key, view)
    }

    override fun onButtonClick(button: ButtonKey) {
        focus().push(button)
    }

    private fun focus(): FormulaCalculator {
        return currentFormula ?: FormulaCalculator(previewWrapper).also {
            currentFormula = it
        }
    }

    private fun notifyPreview(result: DateResult?) {
        previewCallback?.onPreview(result)
    }

}