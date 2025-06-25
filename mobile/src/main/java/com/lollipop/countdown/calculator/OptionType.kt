package com.lollipop.countdown.calculator

import android.content.Context
import com.lollipop.countdown.R

enum class OptionType(val formulaSuffix: Int) {
    None(0),
    Year(R.string.formula_year),
    Month(R.string.formula_month),
    Week(R.string.formula_week),
    Day(R.string.formula_day),
    Hour(R.string.formula_hour),
    Minute(R.string.formula_minute),
    Second(R.string.formula_second),
    Millisecond(R.string.formula_millisecond),
    Time(0);

    fun getValue(context: Context): String {
        if (formulaSuffix == 0) {
            return ""
        }
        return context.getString(formulaSuffix)
    }

}