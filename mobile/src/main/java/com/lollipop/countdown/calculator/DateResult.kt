package com.lollipop.countdown.calculator

sealed class DateResult {

    companion object {
        /**
         * 默认的开始时间
         * 由于公元0年是负数，会对时间戳计算产生负担，因此默认还是从1970年开始计算
         */
        val START_TIME = Time(0)

        private const val KEY_NONE = "NONE"
        private const val KEY_TIME = "T"
        private const val KEY_DURATION = "D"

        private fun format(result: DateResult): String {
            return when (result) {
                is Duration -> {
                    "${KEY_DURATION},${result.value}"
                }

                None -> {
                    KEY_NONE
                }

                is Time -> {
                    "${KEY_TIME},${result.value}"
                }
            }
        }

        fun parse(formatValue: String): DateResult? {
            if (formatValue == KEY_NONE) {
                return None
            }
            val split = formatValue.split(",")
            if (split.size != 2) {
                return null
            }
            return when (split[0]) {
                KEY_TIME -> {
                    Time(split[1].toLong())
                }

                KEY_DURATION -> {
                    Duration(split[1].toLong())
                }

                else -> {
                    null
                }
            }
        }
    }

    abstract fun format(): String

    class Time(val value: Long) : DateResult() {
        override fun format(): String {
            return format(this)
        }
    }

    class Duration(val value: Long) : DateResult() {
        override fun format(): String {
            return format(this)
        }
    }

    object None : DateResult() {
        override fun format(): String {
            return format(this)
        }
    }

}

fun interface PreviewCallback {
    fun onPreview(result: DateResult?)
}

fun interface FormulaChangedCallback {
    fun onFormulaChanged(formula: FormulaChanged)
}
