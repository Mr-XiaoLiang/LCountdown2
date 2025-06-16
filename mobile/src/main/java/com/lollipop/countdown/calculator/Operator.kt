package com.lollipop.countdown.calculator

sealed class Operator(val level: Level) {

    /**
     *  加
     */
    object Add : Operator(Level.L1)

    /**
     * 减
     */
    object Sub : Operator(Level.L1)

    /**
     * 乘
     */
    object Mul : Operator(Level.L2)

    /**
     * 除
     */
    object Div : Operator(Level.L2)

    /**
     * 等于
     */
    object Equals : Operator(Level.L0)

    enum class Level {
        L0,
        L1,
        L2,
    }

}