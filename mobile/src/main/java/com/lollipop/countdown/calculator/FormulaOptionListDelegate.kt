package com.lollipop.countdown.calculator

class FormulaOptionListDelegate(
    private val dateCalculator: DateCalculator,
) {

    val size: Int
        get() {
            return dateCalculator.focus().optionList.size
        }

    operator fun get(index: Int): Option {
        return dateCalculator.focus().optionList[index]
    }

}