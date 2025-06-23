package com.lollipop.countdown.calculator

sealed class FormulaChanged {

    class Changed(val index: Int) : FormulaChanged()

    class Added(val index: Int) : FormulaChanged()

    class Removed(val index: Int) : FormulaChanged()

    object All : FormulaChanged()

}