package com.lollipop.countdown.calculator

class Formula(
    val options: List<Option>,
    val result: DateResult
) {

    companion object {
        private const val FORMAT_SEPARATOR = ";"

        fun parse(formatOption: String, formatResult: String): Formula {
            val optionValueList = formatOption.split(FORMAT_SEPARATOR)
                .effectiveMap { Option.parse(it) }
            val resultValue = DateResult.parse(formatResult) ?: DateResult.None
            return Formula(optionValueList, resultValue)
        }

        private fun <T> List<String>.effectiveMap(map: (String) -> T?): List<T> {
            val result = mutableListOf<T>()
            val valueList = this
            for (value in valueList) {
                val item = map(value)
                if (item != null) {
                    result.add(item)
                }
            }
            return result
        }

    }

    var dbId: Long = -1

    fun formatOption(): String {
        return options.joinToString(FORMAT_SEPARATOR) { it.format() }
    }

    fun formatResult(): String {
        return result.format()
    }

}