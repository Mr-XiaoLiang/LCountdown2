package com.lollipop.countdown.calculator

import com.lollipop.countdown.base.ListenerManager

class DateCalculator(
    private val callback: CalculatorCallback
) : ButtonClickListener {

    private val buttonManager = ButtonManager(this)

    private val changedCallbackManager = ListenerManager<FormulaChangedCallback>()
    private val previewCallbackManager = ListenerManager<PreviewCallback>()
    private val changedCallbackWrapper = object : FormulaChangedCallback {
        override fun onFormulaChanged(formula: FormulaChanged) {
            notifyFormulaChanged(formula)
        }
    }

    private val previewCallbackWrapper = object : PreviewCallback {
        override fun onPreview(result: DateResult?) {
            notifyPreview(result)
        }
    }

    var currentFormula: FormulaCalculator? = null
        private set

    private fun notifyFormulaChanged(formula: FormulaChanged) {
        callback.onFormulaChanged(formula)
        changedCallbackManager.notify {
            it.onFormulaChanged(formula)
        }
    }

    private fun notifyPreview(result: DateResult?) {
        callback.onPreview(result)
        previewCallbackManager.notify {
            it.onPreview(result)
        }
    }

    fun registerChanged(changedCallback: FormulaChangedCallback) {
        changedCallbackManager.register(changedCallback)
    }

    fun registerPreview(previewCallback: PreviewCallback) {
        previewCallbackManager.register(previewCallback)
    }

    fun unregisterChanged(changedCallback: FormulaChangedCallback) {
        changedCallbackManager.unregister(changedCallback)
    }

    fun unregisterPreview(previewCallback: PreviewCallback) {
        previewCallbackManager.unregister(previewCallback)
    }

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

    fun createFormulaListDelegate(): FormulaOptionListDelegate {
        return FormulaOptionListDelegate(this)
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
            changedCallback = changedCallbackWrapper,
            previewCallback = previewCallbackWrapper
        )
    }

    interface CalculatorCallback : PreviewCallback, FormulaChangedCallback {

        fun onNewHistory(formula: Formula)

    }

}