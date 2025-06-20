package com.lollipop.countdown.calculator

class DateCalculator : ButtonController {

    private val buttonList = mutableListOf<ButtonHolder>()

    val formulaList = mutableListOf<Formula>()

    var currentFormula: FormulaCalculator? = null

    fun register(button: ButtonHolder) {
        buttonList.add(button)
    }

    override fun updateButton(callback: (ButtonHolder) -> Unit) {
        buttonList.forEach { callback(it) }
    }

}