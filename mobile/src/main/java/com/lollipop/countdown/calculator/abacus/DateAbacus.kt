package com.lollipop.countdown.calculator.abacus

import com.lollipop.countdown.calculator.DateResult
import com.lollipop.countdown.calculator.Operator
import java.util.Calendar

object DateAbacus {

    const val ONE_SECOND = 1000L
    const val ONE_MINUTE = ONE_SECOND * 60
    const val ONE_HOUR = ONE_MINUTE * 60
    const val ONE_DAY = ONE_HOUR * 24
    const val ONE_WEEK = ONE_DAY * 7
    const val ONE_MONTH = ONE_DAY * 30
    const val ONE_YEAR = ONE_DAY * 365

    fun turnYear(
        calendar: Calendar,
        target: DateResult,
        number: Long,
        operator: Operator
    ): DateResult {
        return turnAny(
            calendar = calendar,
            target = target,
            number = number.numberToInt(),
            operator = operator,
            step = ONE_YEAR,
            field = Calendar.YEAR
        )
    }

    fun turnMonth(
        calendar: Calendar,
        target: DateResult,
        number: Long,
        operator: Operator
    ): DateResult {
        return turnAny(
            calendar = calendar,
            target = target,
            number = number.numberToInt(),
            operator = operator,
            step = ONE_MONTH,
            field = Calendar.MONTH
        )
    }

    fun turnWeek(
        calendar: Calendar,
        target: DateResult,
        number: Long,
        operator: Operator
    ): DateResult {
        return turnAny(
            calendar = calendar,
            target = target,
            number = number.numberToInt(),
            operator = operator,
            step = ONE_WEEK,
            field = Calendar.WEEK_OF_YEAR
        )
    }

    fun turnDay(
        calendar: Calendar,
        target: DateResult,
        number: Long,
        operator: Operator
    ): DateResult {
        return turnAny(
            calendar = calendar,
            target = target,
            number = number.numberToInt(),
            operator = operator,
            step = ONE_DAY,
            field = Calendar.DAY_OF_MONTH
        )
    }

    fun turnHour(
        calendar: Calendar,
        target: DateResult,
        number: Long,
        operator: Operator
    ): DateResult {
        return turnAny(
            calendar = calendar,
            target = target,
            number = number.numberToInt(),
            operator = operator,
            step = ONE_HOUR,
            field = Calendar.HOUR_OF_DAY
        )
    }

    fun turnMinute(
        calendar: Calendar,
        target: DateResult,
        number: Long,
        operator: Operator
    ): DateResult {
        return turnAny(
            calendar = calendar,
            target = target,
            number = number.numberToInt(),
            operator = operator,
            step = ONE_MINUTE,
            field = Calendar.MINUTE
        )
    }

    fun turnSecond(
        calendar: Calendar,
        target: DateResult,
        number: Long,
        operator: Operator
    ): DateResult {
        return turnAny(
            calendar = calendar,
            target = target,
            number = number.numberToInt(),
            operator = operator,
            step = ONE_SECOND,
            field = Calendar.SECOND
        )
    }

    fun turnMillisecond(
        calendar: Calendar,
        target: DateResult,
        number: Long,
        operator: Operator
    ): DateResult {
        return turnAny(
            calendar = calendar,
            target = target,
            number = number.numberToInt(),
            operator = operator,
            step = 1,
            field = Calendar.MILLISECOND
        )
    }

    private fun Long.numberToInt(): Int {
        val number = this
        if (number > Int.MAX_VALUE) {
            return Int.MAX_VALUE
        }
        if (number < Int.MIN_VALUE) {
            return Int.MIN_VALUE
        }
        return number.toInt()
    }

    fun turnTime(
        target: DateResult,
        number: Long,
        operator: Operator
    ): DateResult {
        // 强制把返回结果转换为时间戳
        val timeTarget = when (target) {
            is DateResult.Time -> {
                target
            }

            is DateResult.Duration -> {
                DateResult.Time(target.value)
            }

            else -> {
                return target
            }
        }
        return DateResult.Time(
            NumberAbacus.turn(
                timeTarget.value,
                number,
                operator
            )
        )
    }

    fun turnAny(
        calendar: Calendar,
        target: DateResult,
        number: Int,
        operator: Operator,
        step: Long,
        field: Int
    ): DateResult {
        return when (target) {
            is DateResult.Duration -> {
                turnAnyDuration(
                    target = target.value,
                    number = number,
                    step = step,
                    operator = operator
                )
            }

            // 时间点不能用普通数字去做计算
            is DateResult.Time -> {
                turnAnyTime(
                    calendar = calendar,
                    target = target,
                    number = number,
                    field = field,
                    operator = operator
                )
            }

            DateResult.None -> {
                return DateResult.None
            }
        }
    }

    private fun turnAnyDuration(
        target: Long,
        number: Int,
        step: Long,
        operator: Operator
    ): DateResult.Duration {
        return DateResult.Duration(
            NumberAbacus.turn(
                target,
                number.toLong() * step,
                operator
            )
        )
    }

    private fun turnAnyTime(
        calendar: Calendar,
        target: DateResult.Time,
        number: Int,
        field: Int,
        operator: Operator
    ): DateResult.Time {
        when (operator) {
            Operator.PLUS, Operator.DEFAULT -> {
                calendar.timeInMillis = target.value
                calendar.add(field, number)
                return DateResult.Time(calendar.timeInMillis)
            }

            Operator.MINUS -> {
                calendar.timeInMillis = target.value
                calendar.add(field, (number) * -1)
                return DateResult.Time(calendar.timeInMillis)
            }

            else -> {
                return target
            }
        }
    }

}