package com.lollipop.countdown.calculator

sealed class Option(val operator: Operator, value: Int) {

    open val maxValue: Int = Int.MAX_VALUE
    open val supportedOperator: Operator.Level = Operator.Level.L2

    var value: Int = 0

    /**
     *  年
     */
    class Year(operator: Operator) : Option(operator)

    /**
     * 月
     */
    class Month(operator: Operator) : Option(operator) {
        override val maxValue: Int = 12
    }

    /**
     * 日
     */
    class Day(operator: Operator) : Option(operator) {
        override val maxValue: Int = 31
    }

    /**
     * 时
     */
    class Hour(operator: Operator) : Option(operator) {
        override val maxValue: Int = 24
    }

    /**
     * 分
     */
    class Minute(operator: Operator) : Option(operator) {
        override val maxValue: Int = 60
    }

    /**
     * 秒
     */
    class Second(operator: Operator) : Option(operator) {
        override val maxValue: Int = 60
    }

    /**
     * 毫秒
     */
    class Millisecond(operator: Operator) : Option(operator) {
        override val maxValue: Int = 1000
    }

    /**
     * 数字
     */
    class Number(operator: Operator, val value: Int) : Option(operator)

}