package com.lollipop.countdown.calculator

sealed class Option(val operator: Operator) {

    var value: Long = 0

    /**
     *  年
     */
    class Year(operator: Operator) : Option(operator)

    /**
     * 月
     */
    class Month(operator: Operator) : Option(operator)

    /**
     * 日
     */
    class Day(operator: Operator) : Option(operator)

    /**
     * 时
     */
    class Hour(operator: Operator) : Option(operator)

    /**
     * 分
     */
    class Minute(operator: Operator) : Option(operator)

    /**
     * 秒
     */
    class Second(operator: Operator) : Option(operator)

    /**
     * 毫秒
     */
    class Millisecond(operator: Operator) : Option(operator)

    /**
     * 数字
     */
    class Number(operator: Operator) : Option(operator)

}