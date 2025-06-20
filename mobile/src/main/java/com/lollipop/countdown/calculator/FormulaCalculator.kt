package com.lollipop.countdown.calculator

import android.util.Log

class FormulaCalculator(
    private val previewCallback: PreviewCallback
) {

    /**
     * 运算操作的列表
     */
    val optionList = mutableListOf<Option>()

    /**
     * 按下一个按键的时候
     */
    fun push(key: ButtonKey) {
        // 先分发按钮
        dispatch(key)
        // 预览
        preview()
    }

    /**
     * 获取当前焦点的运算操作
     */
    fun optFocus(): Option {
        // 没有就从列表里取出来最后一个
        if (optionList.isNotEmpty()) {
            return optionList.last()
        }
        // 最后还是没有，就创建一个
        val option = Option()
        optionList.add(option)
        return option
    }

    /**
     * 推送按键为不同的操作
     */
    private fun dispatch(key: ButtonKey) {
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

    /**
     * 按一个运算符的时候
     */
    private fun pushOperator(operator: Operator) {
        val focus = optFocus()
        if (focus.operator == Operator.DEFAULT) {
            focus.operator = operator
        } else {
            pushNewOption().operator = operator
        }
    }

    /**
     * 按一个数字的时候
     */
    private fun pushNumber(number: Int) {
        try {
            optFocus().apply {
                val newValue = value.times(10).plus(number)
                if (newValue < 0 || newValue > 999999999999999999) {
                    return
                }
                value = newValue
            }
        } catch (e: Throwable) {
            logE("pushNumber error", e)
        }
    }

    /**
     * 按一个运算符的时候
     */
    private fun pushOption(value: OptionType) {
        val focus = optFocus()
        if (focus.type == OptionType.None) {
            focus.type = value
        } else {
            pushNewOption().type = value
        }
    }

    /**
     * 必要的时候，增加一个运算操作
     */
    private fun pushNewOption(): Option {
        val newOption = Option()
        optionList.add(newOption)
        return newOption
    }

    /**
     * 按下退格键，我们需要按照优先级顺序，从后往前删除
     */
    private fun backspace() {
        val option = optFocus()
        // 值不为0的时候，就减掉一个
        if (option.value > 0) {
            option.value = option.value.div(10)
            return
        }
        // 类型不为none的时候，就设置为none
        if (option.type != OptionType.None) {
            option.type = OptionType.None
            return
        }
        // 运算符不为none的时候，就设置为none
        if (option.operator != Operator.DEFAULT) {
            option.operator = Operator.DEFAULT
            return
        }
        // 如果不是optionList的最后一个，就删除这个option
        if (optionList.size > 1) {
            optionList.removeAt(optionList.lastIndex)
        }
    }

    private fun calculate() {
        // TODO
    }

    /**
     * 清空
     */
    private fun clear() {
        optionList.clear()
    }

    private fun preview() {
        // TODO
    }

    private fun logE(message: String, e: Throwable) {
        Log.e("FormulaCalculator", message, e)
    }

    private fun logD(message: String) {
        Log.e("FormulaCalculator", message)
    }

}