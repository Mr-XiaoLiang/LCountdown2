package com.lollipop.countdown.calculator

sealed class DateResult {

    companion object {
        /**
         * 默认的开始时间
         * 由于公元0年是负数，会对时间戳计算产生负担，因此默认还是从1970年开始计算
         */
        val START_TIME = Time(0)

    }

    class Time(val value: Long) : DateResult()

    class Duration(val value: Long) : DateResult()

    object None : DateResult()

}

fun interface PreviewCallback {
    fun onPreview(result: DateResult?)
}

fun interface FormulaChangedCallback {
    fun onFormulaChanged(formula: FormulaChanged)
}
