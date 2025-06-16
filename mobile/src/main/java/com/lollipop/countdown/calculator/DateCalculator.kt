package com.lollipop.countdown.calculator

class DateCalculator(val stateCallback: StateCallback) {

    val formulaList = mutableListOf<Formula>()

    var currentFormula: Formula? = null

    private fun getFormula(): Formula {
        val formula = currentFormula
        if (formula != null) {
            return formula
        }
        val newFormula = Formula()
        newFormula.stateCallback = stateCallback
        currentFormula = newFormula
        return newFormula
    }

    fun put(option: Option) {
        getFormula().put(option)
    }

    fun backspace() {
        getFormula().backspace()
    }

    fun calculate() {
        val formula = currentFormula ?: return
        val result = CalculatorImpl.calculate(formula)
        formulaList.add(formula)
        currentFormula = null
        stateCallback.onFormulaCalculate(formula, result)
    }

    interface StateCallback : Formula.StateCallback {

        fun onFormulaCalculate(formula: Formula, result: Result)

    }

}