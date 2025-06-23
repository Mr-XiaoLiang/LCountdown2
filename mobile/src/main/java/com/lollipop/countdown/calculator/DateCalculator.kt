package com.lollipop.countdown.calculator

class DateCalculator(
    private val callback: CalculatorCallback
) : ButtonClickListener {

    private val buttonManager = ButtonManager(this)

    var currentFormula: FormulaCalculator? = null
        private set

    fun register(holder: ButtonHolder) {
        buttonManager.register(holder)
    }

    fun resume(formula: Formula) {
        val current = currentFormula
        if (current != null) {
            val current = current.toFormula()
            if (current.options.isNotEmpty()) {
                callback.onNewHistory(current)
            }
            currentFormula = null
        }
        val newCalculator = createFormulaCalculator()
        newCalculator.reset(formula.options)
        newCalculator.fetch()
        currentFormula = newCalculator
    }

    override fun onButtonClick(button: ButtonKey) {
        val focus = focus()
        focus.push(button)
        mayNeedNewFormula()
    }

    private fun mayNeedNewFormula() {
        val current = currentFormula
        if (current == null) {
            return
        }
        val formula = current.getFinallyFormula()
        if (formula != null) {
            callback.onNewHistory(formula)
            currentFormula = createFormulaCalculator()
            currentFormula?.fetch()
        }
    }

    fun focus(): FormulaCalculator {
        return currentFormula ?: createFormulaCalculator().also {
            currentFormula = it
        }
    }

    private fun createFormulaCalculator(): FormulaCalculator {
        return FormulaCalculator(
            buttonProvider = buttonManager,
            changedCallback = callback,
            previewCallback = callback
        )
    }

    interface CalculatorCallback : PreviewCallback, FormulaChangedCallback {

        fun onNewHistory(formula: Formula)

    }

}