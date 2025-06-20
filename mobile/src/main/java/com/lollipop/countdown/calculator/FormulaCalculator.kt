package com.lollipop.countdown.calculator

class FormulaCalculator(private val buttonController: ButtonController) {

    val optionList = mutableListOf<Option>()

    var pendingOperator: Operator? = null
        private set

    var pendingValue: Long? = null

    var pendingOptionType: OptionType = OptionType.None

    fun push(key: ButtonKey) {
        when (key) {
            ButtonKey.NUMBER_0 -> {
                pushNumber(0)
            }

            ButtonKey.NUMBER_1 -> {
                pushNumber(1)
            }

            ButtonKey.NUMBER_2 -> {
                pushNumber(2)
            }

            ButtonKey.NUMBER_3 -> {
                pushNumber(3)
            }

            ButtonKey.NUMBER_4 -> {
                pushNumber(4)
            }

            ButtonKey.NUMBER_5 -> {
                pushNumber(5)
            }

            ButtonKey.NUMBER_6 -> {
                pushNumber(6)
            }

            ButtonKey.NUMBER_7 -> {
                pushNumber(7)
            }

            ButtonKey.NUMBER_8 -> {
                pushNumber(8)
            }

            ButtonKey.NUMBER_9 -> {
                pushNumber(9)
            }

            ButtonKey.YEAR -> {
                pushOption(OptionType.Year)
            }

            ButtonKey.MONTH -> {
                pushOption(OptionType.Month)
            }

            ButtonKey.DAY -> {
                pushOption(OptionType.Day)
            }

            ButtonKey.HOUR -> {
                pushOption(OptionType.Hour)
            }

            ButtonKey.MINUTE -> {
                pushOption(OptionType.Minute)
            }

            ButtonKey.SECOND -> {
                pushOption(OptionType.Second)
            }

            ButtonKey.MILLISECOND -> {
                pushOption(OptionType.Millisecond)
            }

            ButtonKey.PLUS -> {
                pushOperator(Operator.PLUS)
            }

            ButtonKey.MINUS -> {
                pushOperator(Operator.MINUS)
            }

            ButtonKey.MULTIPLY -> {
                pushOperator(Operator.MULTIPLY)
            }

            ButtonKey.DIVIDE -> {
                pushOperator(Operator.DIVIDE)
            }

            ButtonKey.BACKSPACE -> {
                backspace()
            }

            ButtonKey.EQUALS -> {
                calculate()
            }

            ButtonKey.CLEAR -> {
                clear()
            }
        }
    }

    private fun pushOperator(operator: Operator) {
        // TODO
    }

    private fun pushNumber(number: Int) {
        // TODO
    }

    private fun pushOption(value: OptionType) {
        // TODO
    }

    private fun backspace() {
        // TODO
    }

    private fun calculate() {
        // TODO
    }

    private fun clear() {
        // TODO
    }

    enum class OptionType {
        None,
        Year,
        Month,
        Day,
        Hour,
        Minute,
        Second,
        Millisecond

    }

}