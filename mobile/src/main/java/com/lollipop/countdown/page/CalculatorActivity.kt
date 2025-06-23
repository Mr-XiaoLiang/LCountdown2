package com.lollipop.countdown.page

import android.os.Bundle
import android.view.View
import com.lollipop.countdown.base.BasicActivity
import com.lollipop.countdown.calculator.ButtonHolder
import com.lollipop.countdown.calculator.ButtonKey
import com.lollipop.countdown.calculator.DateCalculator
import com.lollipop.countdown.databinding.ActivityCalculatorBinding
import com.lollipop.countdown.databinding.SubCalculatorKeyboardBinding

class CalculatorActivity : BasicActivity(), DateCalculator.CalculatorCallback {

    private val dateCalculator by lazy {
        DateCalculator(this)
    }

    private val binding by lazy {
        ActivityCalculatorBinding.inflate(layoutInflater)
    }

    override fun onCreateContentView(): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindButton(binding.keyboardPanel)
    }

    private fun bindButton(b: SubCalculatorKeyboardBinding) {
        registerKey(ButtonKey.DIVIDE, b.divideButton)
        registerKey(ButtonKey.MULTIPLY, b.multiplyButton)
        registerKey(ButtonKey.MINUS, b.minusButton)
        registerKey(ButtonKey.PLUS, b.plusButton)
        registerKey(ButtonKey.EQUALS, b.equalsButton)
        registerKey(ButtonKey.BACKSPACE, b.backspaceButton)
        registerKey(ButtonKey.CLEAR, b.clearAllButton)
        registerKey(ButtonKey.NUMBER_0, b.number0Button)
        registerKey(ButtonKey.NUMBER_1, b.number1Button)
        registerKey(ButtonKey.NUMBER_2, b.number2Button)
        registerKey(ButtonKey.NUMBER_3, b.number3Button)
        registerKey(ButtonKey.NUMBER_4, b.number4Button)
        registerKey(ButtonKey.NUMBER_5, b.number5Button)
        registerKey(ButtonKey.NUMBER_6, b.number6Button)
        registerKey(ButtonKey.NUMBER_7, b.number7Button)
        registerKey(ButtonKey.NUMBER_8, b.number8Button)
        registerKey(ButtonKey.NUMBER_9, b.number9Button)
        registerKey(ButtonKey.YEAR, b.yearButton)
        registerKey(ButtonKey.MONTH, b.monthButton)
        registerKey(ButtonKey.DAY, b.dayButton)
        registerKey(ButtonKey.HOUR, b.hourButton)
        registerKey(ButtonKey.MINUTE, b.minuteButton)
        registerKey(ButtonKey.SECOND, b.secondButton)
        registerKey(ButtonKey.MILLISECOND, b.millisecondButton)
    }

    override fun onHistoryChanged() {
        TODO("Not yet implemented")
    }

    override fun onFormulaChanged() {
        TODO("Not yet implemented")
    }

    private fun registerKey(key: ButtonKey, button: View) {
        dateCalculator.register(KeyHolder(key, button))
    }

    private class KeyHolder(key: ButtonKey, val button: View) : ButtonHolder(key) {
        override fun setEnable(enable: Boolean) {
            button.alpha = if (enable) {
                1f
            } else {
                0.5f
            }
        }
    }

}