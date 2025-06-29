package com.lollipop.countdown.calculator

enum class Operator(val key: String) {

    /**
     * 默认的，它等价于 +
     */
    DEFAULT("p"),

    /**
     * 加号
     */
    PLUS("p"),

    /**
     * 减号
     */
    MINUS("s"),

    /**
     * 乘号
     */
    MULTIPLY("m"),

    /**
     * 除号
     */
    DIVIDE("d");

    fun isHighLevel(): Boolean {
        return this == MULTIPLY || this == DIVIDE
    }

    companion object {
        fun find(key: String, def: Operator): Operator {
            return entries.find { it.key == key } ?: def
        }
    }

}