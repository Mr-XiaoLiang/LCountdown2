package com.lollipop.countdown.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lollipop.countdown.R
import com.lollipop.countdown.base.BasicActivity
import com.lollipop.countdown.calculator.ButtonHolder
import com.lollipop.countdown.calculator.ButtonKey
import com.lollipop.countdown.calculator.DateCalculator
import com.lollipop.countdown.calculator.DateResult
import com.lollipop.countdown.calculator.Formula
import com.lollipop.countdown.calculator.FormulaChanged
import com.lollipop.countdown.calculator.FormulaOptionListDelegate
import com.lollipop.countdown.calculator.Operator
import com.lollipop.countdown.calculator.Option
import com.lollipop.countdown.calculator.OptionType
import com.lollipop.countdown.calculator.PreviewDelegate
import com.lollipop.countdown.calculator.TimeFormat
import com.lollipop.countdown.databinding.ActivityCalculatorBinding
import com.lollipop.countdown.databinding.ItemCalculatorFormulaBinding
import com.lollipop.countdown.databinding.SubCalculatorKeyboardBinding
import java.util.Date

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

    private val formulaAdapter by lazy {
        FormulaAdapter(dateCalculator.createFormulaListDelegate())
    }

    private val previewDelegate by lazy {
        PreviewDelegate(binding.resultView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindButton(binding.keyboardPanel)
        binding.formulaListView.layoutManager = LinearLayoutManager(
            this, RecyclerView.VERTICAL, false
        )
        binding.formulaListView.adapter = formulaAdapter
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
        registerKey(ButtonKey.NOW, b.nowButton)
    }

    private fun registerKey(key: ButtonKey, button: View) {
        dateCalculator.register(KeyHolder(key, button))
    }

    override fun onNewHistory(formula: Formula) {
        // TODO("Not yet implemented")
    }

    override fun afterButtonClick() {
        previewDelegate.previewMode(!dateCalculator.isEnd)
    }

    override fun onPreview(result: DateResult?) {
        previewDelegate.update(result)
    }

    override fun onFormulaChanged(formula: FormulaChanged) {
        when (formula) {
            is FormulaChanged.Added -> {
                notifyOptionAdded(formula.index)
            }

            FormulaChanged.All -> {
                notifyFormulaChanged()
            }

            is FormulaChanged.Changed -> {
                notifyOptionChanged(formula.index)
            }

            is FormulaChanged.Removed -> {
                notifyOptionRemoved(formula.index)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        notifyFormulaChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyFormulaChanged() {
        formulaAdapter.notifyDataSetChanged()
    }

    private fun notifyOptionChanged(index: Int) {
        formulaAdapter.notifyItemChanged(index)
    }

    private fun notifyOptionRemoved(index: Int) {
        formulaAdapter.notifyItemRemoved(index)
    }

    private fun notifyOptionAdded(index: Int) {
        formulaAdapter.notifyItemInserted(index)
        binding.formulaListView.scrollToPosition(index)
    }

    private class FormulaAdapter(
        private val formulaList: FormulaOptionListDelegate
    ) : RecyclerView.Adapter<FormulaItemHolder>() {

        private var layoutInflater: LayoutInflater? = null

        private fun getLayoutInflater(parent: ViewGroup): LayoutInflater {
            return layoutInflater ?: LayoutInflater.from(parent.context).also {
                layoutInflater = it
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): FormulaItemHolder {
            return FormulaItemHolder(
                ItemCalculatorFormulaBinding.inflate(
                    getLayoutInflater(parent),
                    parent,
                    false
                )
            )
        }

        private fun isSelected(position: Int): Boolean {
            return position == formulaList.size - 1
        }

        override fun onBindViewHolder(
            holder: FormulaItemHolder,
            position: Int
        ) {
            holder.bind(formulaList[position], isSelected(position))
        }

        override fun getItemCount(): Int {
            return formulaList.size
        }

    }

    private class FormulaItemHolder(
        val binding: ItemCalculatorFormulaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val sdf = TimeFormat.Millisecond

        fun bind(option: Option, selected: Boolean) {
            binding.optionIconView.setImageResource(getOperatorIcon(option.operator))
            binding.formulaValueView.text = getFormulaValue(option.type, option.value)
            binding.root.elevation = if (selected) {
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    6f,
                    itemView.resources.displayMetrics
                )
            } else {
                0f
            }
        }

        private fun getFormulaValue(type: OptionType, value: Long): String {
            when (type) {
                OptionType.None -> {
                    return value.toString()
                }

                OptionType.Year -> {
                    return "$value ${getFormulaSuffix(type)}"
                }

                OptionType.Month -> {
                    return "$value ${getFormulaSuffix(type)}"
                }

                OptionType.Week -> {
                    return "$value ${getFormulaSuffix(type)}"
                }

                OptionType.Day -> {
                    return "$value ${getFormulaSuffix(type)}"
                }

                OptionType.Hour -> {
                    return "$value ${getFormulaSuffix(type)}"
                }

                OptionType.Minute -> {
                    return "$value ${getFormulaSuffix(type)}"
                }

                OptionType.Second -> {
                    return "$value ${getFormulaSuffix(type)}"
                }

                OptionType.Millisecond -> {
                    return "$value ${getFormulaSuffix(type)}"
                }

                OptionType.Time -> {
                    return sdf.format(Date(value))
                }
            }
        }

        private fun getFormulaSuffix(type: OptionType): String {
            return type.getValue(binding.root.context)
        }

        private fun getOperatorIcon(operator: Operator): Int {
            return when (operator) {
                Operator.DEFAULT, Operator.PLUS -> R.drawable.ic_glyph_op_add
                Operator.MINUS -> R.drawable.ic_glyph_op_sub
                Operator.MULTIPLY -> R.drawable.ic_glyph_op_mul
                Operator.DIVIDE -> R.drawable.ic_glyph_op_div
            }
        }

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