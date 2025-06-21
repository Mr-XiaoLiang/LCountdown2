package com.lollipop.countdown.calculator

class DateCalculator(
    private val callback: CalculatorCallback
) : ButtonClickListener {

    private val buttonManager = ButtonManager(this)

    val historyList = mutableListOf<Formula>()

    var currentFormula: FormulaCalculator? = null

    var previewCallback: PreviewCallback? = null

    private val previewWrapper = object : PreviewCallback {
        override fun onPreview(result: DateResult?) {
            notifyPreview(result)
        }
    }

    fun register(holder: ButtonHolder) {
        buttonManager.register(holder)
    }

    override fun onButtonClick(button: ButtonKey) {
        val focus = focus()
        focus.push(button)
        mayNeedNewFormula()
        callback.onFormulaChanged()
    }

    private fun mayNeedNewFormula() {
        val current = currentFormula
        if (current == null) {
            return
        }
        val formula = current.getFinallyFormula()
        if (formula != null) {
            historyList.add(formula)
            currentFormula = null
        }
        callback.onHistoryChanged()
    }

    private fun focus(): FormulaCalculator {
        return currentFormula ?: FormulaCalculator(buttonManager, previewWrapper).also {
            currentFormula = it
        }
    }

    private fun notifyPreview(result: DateResult?) {
        previewCallback?.onPreview(result)
    }

    interface CalculatorCallback {

        fun onHistoryChanged()
        fun onFormulaChanged()

    }

}