package com.lollipop.countdown.calculator

enum class Operator {

    /**
     * 默认的，它等价于 +
     */
    DEFAULT,

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
    DIVIDE;

    fun isHighLevel(): Boolean {
        return this == MULTIPLY || this == DIVIDE
    }

}