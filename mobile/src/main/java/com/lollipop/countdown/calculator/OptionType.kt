package com.lollipop.countdown.calculator

import android.content.Context
import com.lollipop.countdown.R

enum class OptionType(val formulaSuffix: Int, val key: String) {
    None(0, ""),
    Year(R.string.formula_year, "y"),
    Month(R.string.formula_month, "m"),
    Week(R.string.formula_week, "w"),
    Day(R.string.formula_day, "d"),
    Hour(R.string.formula_hour, "h"),
    Minute(R.string.formula_minute, "i"),
    Second(R.string.formula_second, "s"),
    Millisecond(R.string.formula_millisecond, "ms"),
    Time(0, "t");

    fun getValue(context: Context): String {
        if (formulaSuffix == 0) {
            return ""
        }
        return context.getString(formulaSuffix)
    }

    companion object {
        fun find(key: String, def: OptionType): OptionType {
            for (type in entries) {
                if (type.key == key) {
                    return type
                }
            }
            return def
        }
    }

}