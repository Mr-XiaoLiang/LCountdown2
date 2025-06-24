package com.lollipop.countdown.calculator

enum class ButtonKey {

    /**
     * 数字0
     */
    NUMBER_0,

    /**
     * 数字1
     */
    NUMBER_1,

    /**
     * 数字2
     */
    NUMBER_2,

    /**
     * 数字3
     */
    NUMBER_3,

    /**
     * 数字4
     */
    NUMBER_4,

    /**
     * 数字5
     */
    NUMBER_5,

    /**
     * 数字6
     */
    NUMBER_6,

    /**
     * 数字7
     */
    NUMBER_7,

    /**
     * 数字8
     */
    NUMBER_8,

    /**
     * 数字9
     */
    NUMBER_9,

    /**
     * 年
     */
    YEAR,

    /**
     * 月
     */
    MONTH,

    /**
     * 日
     */
    DAY,

    /**
     * 小时（24小时制）
     */
    HOUR,

    /**
     * 分钟
     */
    MINUTE,

    /**
     * 秒
     */
    SECOND,

    /**
     * 毫秒
     */
    MILLISECOND,

    /**
     *  现在
     */
    NOW,

    /**
     * 加号
     */
    PLUS,

    /**
     * 减号
     */
    MINUS,

    /**
     * 乘号
     */
    MULTIPLY,

    /**
     * 除号
     */
    DIVIDE,

    /**
     * 退格
     */
    BACKSPACE,

    /**
     * 等于
     */
    EQUALS,

    /**
     * 清除
     */
    CLEAR;

    fun isNumber(): Boolean {
        return this in arrayOf(
            NUMBER_0,
            NUMBER_1,
            NUMBER_2,
            NUMBER_3,
            NUMBER_4,
            NUMBER_5,
            NUMBER_6,
            NUMBER_7,
            NUMBER_8,
            NUMBER_9
        )
    }

    fun isMathHighLevelOperator(): Boolean {
        return this in arrayOf(MULTIPLY, DIVIDE)
    }

    fun isMathLowLevelOperator(): Boolean {
        return this in arrayOf(PLUS, MINUS)
    }

    fun isDateOperator(): Boolean {
        return this in arrayOf(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, MILLISECOND)
    }

}