package com.lollipop.countdown.calculator

import android.util.Log
import com.lollipop.countdown.calculator.abacus.DateAbacus
import com.lollipop.countdown.calculator.abacus.NumberAbacus
import java.util.Calendar

class FormulaCalculator(
    private val buttonProvider: ButtonProvider,
    private val changedCallback: FormulaChangedCallback,
    private val previewCallback: PreviewCallback
) : ButtonController {

    companion object {
        /**
         * 1970年之前的时间，在日历里显示为负数，所以累加时间的时候，得叠加上
         */
        const val TIME_BUFFER = 62167507200000
    }

    /**
     * 运算操作的列表
     */
    val optionList = mutableListOf<Option>()

    var finallyResult: DateResult? = null
        private set

    val isComplete: Boolean
        get() {
            return finallyResult != null
        }

    private val calendar by lazy {
        Calendar.getInstance()
    }

    fun reset(options: List<Option>) {
        optionList.clear()
        optionList.addAll(options)
        finallyResult = null
        notifyAllChanged()
    }

    fun updateCurrent(block: (Option) -> Unit) {
        block(optFocus())
        notifyFocusOptionChanged()
    }

    fun fetch() {
        // 检查当前状态
        updateButton()
        // 更新预览
        preview()
    }

    fun getFinallyFormula(): Formula? {
        val dateResult = finallyResult
        return if (dateResult != null) {
            Formula(optionList, dateResult)
        } else {
            null
        }
    }

    fun toFormula(): Formula {
        return Formula(optionList, finallyResult ?: DateResult.None)
    }

    /**
     * 按下一个按键的时候
     */
    fun push(key: ButtonKey) {
        logD("push $key")
        // 先分发按钮
        dispatch(key)
        fetch()
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
        return pushNewOption()
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

            ButtonKey.WEEK -> {
                pushOption(OptionType.Week)
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

            ButtonKey.NOW -> {
                pushNow()
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
        if (focus.operator == Operator.DEFAULT || focus.value == 0L) {
            focus.operator = operator
            notifyFocusOptionChanged()
        } else {
            pushNewOption().operator = operator
            notifyPushNewOption()
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
                notifyFocusOptionChanged()
            }
        } catch (e: Throwable) {
            logE("pushNumber error", e)
        }
    }

    /**
     * 按一个now的时候,放入当前的时间
     */
    private fun pushNow() {
        val focus = optFocus()
        var timeValue = System.currentTimeMillis()
        optionList.forEach {
            if (it.type == OptionType.Time) {
                // 需要累加1970年之前的时间，否则数量不够
                timeValue += TIME_BUFFER
            }
        }
        if (focus.type == OptionType.None) {
            focus.type = OptionType.Time
            focus.value = timeValue
            if (focus.operator == Operator.DEFAULT) {
                focus.operator = Operator.PLUS
            }
            notifyFocusOptionChanged()
        } else {
            val option = pushNewOption()
            option.type = OptionType.Time
            option.value = timeValue
            option.operator = Operator.PLUS
            notifyPushNewOption()
        }
        pushNewOption()
        notifyPushNewOption()
    }

    /**
     * 按一个运算符的时候
     */
    private fun pushOption(type: OptionType) {
        val focus = optFocus()
        if (focus.type == OptionType.None) {
            focus.type = type
            notifyFocusOptionChanged()
        } else {
            pushNewOption().type = type
            notifyPushNewOption()
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
        if (option.type == OptionType.Time) {
            option.value = 0
            option.type = OptionType.None
            notifyFocusOptionChanged()
            return
        }
        // 值不为0的时候，就减掉一个
        if (option.value > 0) {
            option.value = option.value.div(10)
            notifyFocusOptionChanged()
            return
        }
        // 类型不为none的时候，就设置为none
        if (option.type != OptionType.None) {
            option.type = OptionType.None
            notifyFocusOptionChanged()
            return
        }
        // 运算符不为none的时候，就设置为none
        if (option.operator != Operator.DEFAULT) {
            option.operator = Operator.DEFAULT
            notifyFocusOptionChanged()
            return
        }
        // 如果不是optionList的最后一个，就删除这个option
        if (optionList.size > 1) {
            val index = optionList.lastIndex
            optionList.removeAt(index)
            notifyFocusOptionRemoved(index)
        }
    }

    private fun notifyPushNewOption() {
        changedCallback.onFormulaChanged(FormulaChanged.Added(optionList.lastIndex))
    }

    private fun notifyFocusOptionChanged() {
        changedCallback.onFormulaChanged(FormulaChanged.Changed(optionList.lastIndex))
    }

    private fun notifyFocusOptionRemoved(index: Int) {
        changedCallback.onFormulaChanged(FormulaChanged.Removed(index))
    }

    private fun notifyAllChanged() {
        changedCallback.onFormulaChanged(FormulaChanged.All)
    }

    fun calculate(): Result<DateResult> {
        val result = calculateImpl()
        result.getOrNull()?.let {
            if (it != DateResult.None) {
                this.finallyResult = it
            }
        }
        return result
    }

    /**
     * 清空
     */
    private fun clear() {
        optionList.clear()
        notifyAllChanged()
    }

    private fun preview() {
        if (optionList.size < 2) {
            previewCallback.onPreview(null)
            return
        }
        val result = calculateImpl().getOrNull()
        logD("preview: $result")
        previewCallback.onPreview(result)
    }

    private fun calculateImpl(): Result<DateResult> {
        return try {
            finallyResult?.let {
                return Result.success(it)
            }
            if (optionList.isEmpty()) {
                return Result.success(DateResult.None)
            }
            var result: DateResult = if (optionList[0].type == OptionType.Time) {
                DateResult.START_TIME
            } else {
                DateResult.Duration(0)
            }
            optionList.forEach { option ->
                result = summation(result, option)
            }
            Result.success(result)
        } catch (e: Throwable) {
            logE("calculate error", e)
            Result.failure(e)
        }
    }

    private fun summation(source: DateResult, option: Option): DateResult {
        val type = option.type
        val operator = option.operator
        val value = option.value
        when (type) {
            OptionType.None -> {
                return NumberAbacus.turn(source, value, operator)
            }

            OptionType.Year -> {
                return DateAbacus.turnYear(
                    calendar = calendar,
                    target = source,
                    number = value,
                    operator = operator
                )
            }

            OptionType.Month -> {
                return DateAbacus.turnMonth(
                    calendar = calendar,
                    target = source,
                    number = value,
                    operator = operator
                )
            }

            OptionType.Week -> {
                return DateAbacus.turnWeek(
                    calendar = calendar,
                    target = source,
                    number = value,
                    operator = operator
                )
            }

            OptionType.Day -> {
                return DateAbacus.turnDay(
                    calendar = calendar,
                    target = source,
                    number = value,
                    operator = operator
                )
            }

            OptionType.Hour -> {
                return DateAbacus.turnHour(
                    calendar = calendar,
                    target = source,
                    number = value,
                    operator = operator
                )
            }

            OptionType.Minute -> {
                return DateAbacus.turnMinute(
                    calendar = calendar,
                    target = source,
                    number = value,
                    operator = operator
                )
            }

            OptionType.Second -> {
                return DateAbacus.turnSecond(
                    calendar = calendar,
                    target = source,
                    number = value,
                    operator = operator
                )
            }

            OptionType.Millisecond -> {
                return DateAbacus.turnMillisecond(
                    calendar = calendar,
                    target = source,
                    number = value,
                    operator = operator
                )
            }

            OptionType.Time -> {
                return DateAbacus.turnTime(
                    target = source,
                    number = value,
                    operator = operator
                )
            }
        }
    }

    private fun updateButton() {
        buttonProvider.updateButtons(this)
    }

    override fun updateButton(holder: ButtonHolder) {
        if (optionList.size < 2) {
            holder.setEnable(true)
            return
        }
        var focus = optFocus()
        if (isComplete) {
            focus = Option.EMPTY
        }
        val buttonKey = holder.buttonKey
        var isEnable = true
        // 没有输入运算符，那么要求先输入运算符
        if (focus.operator == Operator.DEFAULT) {
            isEnable = if (buttonKey.isMathOperator()) {
                true
            } else if (buttonKey.isBackspace()) {
                optionList.size > 1
            } else {
                false
            }
        }
        if (isEnable && buttonKey.isDateOperator()) {
            // 如果操作符是高阶函数，比如乘法和除法，就不能输入日期
            isEnable = !(focus.operator.isHighLevel())
        }
        if (isEnable && buttonKey.isMathHighLevelOperator()) {
            // 如果当前不是日期类型，那么就可以使用高阶函数
            holder.setEnable(focus.type == OptionType.None)
        }
        if (isEnable && buttonKey.isBackspace()) {
            holder.setEnable(optionList.size > 1 || !(focus.isEmpty()))
        }
        holder.setEnable(isEnable)
    }

    private fun logE(message: String, e: Throwable) {
        Log.e("FormulaCalculator", message, e)
    }

    private fun logD(message: String) {
        Log.e("FormulaCalculator", message)
    }


}