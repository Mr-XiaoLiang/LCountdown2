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

        fun parse(formatValue: String): Option? {
            val split = formatValue.split(",")
            if (split.size < 3) {
                return null
            }
            return Option().apply {
                value = split[0].toLong()
                operator = Operator.find(split[1], DEFAULT_OPERATOR)
                type = OptionType.find(split[2], DEFAULT_TYPE)
            }
        }
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

    fun format(): String {
        return "${operator.key},${type.key},$value"
    }

}