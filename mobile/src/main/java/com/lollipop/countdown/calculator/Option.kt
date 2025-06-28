package com.lollipop.countdown.calculator

class Option {

    companion object {
        const val DEFAULT_VALUE = 0L

        @JvmStatic
        val DEFAULT_OPERATOR = Operator.DEFAULT

        @JvmStatic
        val DEFAULT_TYPE = OptionType.None

        @JvmStatic
        val EMPTY = Option()
    }

    var value: Long = DEFAULT_VALUE

    var operator: Operator = DEFAULT_OPERATOR

    var type: OptionType = DEFAULT_TYPE

    fun isEmpty(): Boolean {
        if (value != DEFAULT_VALUE) {
            return false
        }
        if (operator != Operator.DEFAULT) {
            return false
        }
        if (type != DEFAULT_TYPE) {
            return false
        }
        return true
    }

}