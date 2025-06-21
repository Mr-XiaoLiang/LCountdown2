package com.lollipop.countdown.calculator.abacus

import com.lollipop.countdown.calculator.DateResult
import com.lollipop.countdown.calculator.Operator

object NumberAbacus {

    fun turn(target: DateResult, number: Long, operator: Operator): DateResult {
        return when (target) {
            is DateResult.Duration -> {
                DateResult.Duration(turn(target.value, number, operator))
            }

            // 时间点不能用普通数字去做计算
            is DateResult.Time -> {
                target
            }

            DateResult.None -> {
                target
            }
        }
    }

    fun turn(target: Long, number: Long, operator: Operator): Long {
        when (operator) {
            Operator.DEFAULT, Operator.PLUS -> {
                return target + number
            }

            Operator.MINUS -> {
                return target - number
            }

            Operator.MULTIPLY -> {
                return target * number
            }

            Operator.DIVIDE -> {
                return target / number
            }
        }
    }

}