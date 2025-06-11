package com.lollipop.countdown.base

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lollipop.countdown.R
import com.lollipop.countdown.databinding.ActivityBasicBinding

abstract class BasicActivity : AppCompatActivity() {

    protected val basicBinding by lazy {
        ActivityBasicBinding.inflate(layoutInflater)
    }

    protected val optionButtonList = mutableListOf<OptionButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(basicBinding.root)
        basicBinding.contentPanel.addView(
            onCreateContentView(),
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        ViewCompat.setOnApplyWindowInsetsListener(basicBinding.root) { v, insets ->
            val systemBar = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft,
                v.paddingTop,
                v.paddingRight,
                systemBar.bottom
            )
            insets
        }
    }

    abstract fun onCreateContentView(): View

    protected fun addOption(
        icon: Int,
        tag: String = "",
        onClick: (OptionButton) -> Unit
    ): OptionButton {
        var optionIndex = -1
        if (tag.isNotEmpty()) {
            val index = optionButtonList.indexOfFirst { it.tag == tag }
            if (index >= 0) {
                val oldButton = optionButtonList[index]
                val viewIndex = basicBinding.optionBar.indexOfChild(oldButton.view)
                basicBinding.optionBar.removeView(oldButton.view)
                optionButtonList.remove(oldButton)
                optionIndex = viewIndex
            }
        }
        val optionButton = OptionButton.create(this, icon, tag, onClick)
        if (optionIndex >= 0) {
            optionButtonList.add(optionIndex, optionButton)
            basicBinding.optionBar.addView(optionButton.view, optionIndex)
        } else {
            optionButtonList.add(optionButton)
            basicBinding.optionBar.addView(optionButton.view)
        }
        return optionButton
    }

    protected fun removeOptionByTag(tag: String) {
        removeOption { it.tag == tag }
    }

    protected fun removeOptionById(id: Int) {
        removeOption { it.id == id }
    }

    protected fun removeOption(optionButton: OptionButton) {
        removeOption { optionButton === it }
    }

    protected fun removeOption(block: (optionButton: OptionButton) -> Boolean) {
        val removed = HashSet<OptionButton>()
        for (button in optionButtonList) {
            if (block(button)) {
                removed.add(button)
                basicBinding.optionBar.removeView(button.view)
            }
        }
        optionButtonList.removeAll(removed)
    }

    protected class OptionButton(
        val context: Context,
        val icon: Int,
        val tag: String,
        val onClick: (OptionButton) -> Unit
    ) {

        companion object {
            private fun createView(context: Context, icon: Int): AppCompatImageView {
                val size = context.resources.getDimensionPixelSize(R.dimen.option_button_size)
                val padding = context.resources.getDimensionPixelSize(R.dimen.option_button_padding)
                return AppCompatImageView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(size, size)
                    setPadding(padding, padding, padding, padding)
                    setImageResource(icon)
                    id = View.generateViewId()
                    imageTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.action_on_window
                        )
                    )
                }
            }

            fun create(
                context: Context,
                icon: Int,
                tag: String,
                onClick: (OptionButton) -> Unit
            ): OptionButton {
                return OptionButton(context, icon, tag, onClick)
            }

        }

        val id: Int
            get() {
                return view.id
            }

        val view by lazy {
            createView(context, icon).apply {
                setOnClickListener {
                    onViewClick()
                }
            }
        }

        private fun onViewClick() {
            onClick(this)
        }

    }

}