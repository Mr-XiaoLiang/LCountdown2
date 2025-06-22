package com.lollipop.countdown.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isGone
import com.lollipop.countdown.R

class KeyboardLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet) {

    private var intervalHorizontal = 0
    private var intervalVertical = 0

    /**
     * 列数
     */
    private var countX = 1

    /**
     * 行数
     */
    private var countY = 1

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val itemWidth = (widthSize - (intervalHorizontal * (countX - 1))) / countX
        val itemHeight = (heightSize - (intervalVertical * (countY - 1))) / countY

        val count = childCount
        for (index in 0 until count) {
            val child = getChildAt(index)
            if (child.isGone) {
                continue
            }
            val lp = child.layoutParams as LayoutParams
            val childWidth = getChildWidth(itemWidth, lp.columnSpan)
            val childHeight = getChildHeight(itemHeight, lp.rowSpan)
            child.measure(
                MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
            )
        }
        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val itemWidth = getItemWidth(width)
        val itemHeight = getItemHeight(height)

        val count = childCount
        var indexX = 0
        var indexY = 0
        for (index in 0 until count) {
            val child = getChildAt(index)
            if (child.isGone) {
                continue
            }
            val lp = child.layoutParams as LayoutParams
            // TODO
        }
    }

    private fun getItemWidth(widthSize: Int): Int {
        return (widthSize - paddingLeft - paddingRight - (intervalHorizontal * (countX - 1))) / countX
    }

    private fun getItemHeight(heightSize: Int): Int {
        return (heightSize - paddingTop - paddingBottom - (intervalVertical * (countY - 1))) / countY
    }

    private fun getChildLeft(itemWidth: Int, indexX: Int): Int {
        return (itemWidth + intervalHorizontal) * indexX + paddingLeft
    }

    private fun getChildTop(itemHeight: Int, indexY: Int): Int {
        return (itemHeight + intervalVertical) * indexY + paddingTop
    }

    private fun getChildWidth(itemWidth: Int, spanX: Int): Int {
        if (spanX < 1) {
            return 0
        }
        if (spanX == 1) {
            return itemWidth
        }
        var span = spanX
        if (span > countX) {
            span = countX
        }
        return itemWidth * span + (intervalHorizontal * (span - 1))
    }

    private fun getChildHeight(itemHeight: Int, spanY: Int): Int {
        if (spanY < 1) {
            return 0
        }
        if (spanY == 1) {
            return itemHeight
        }
        var span = spanY
        if (span > countY) {
            span = countY
        }
        return itemHeight * span + (intervalVertical * (span - 1))
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): LayoutParams {
        lp ?: return generateDefaultLayoutParams()
        return LayoutParams(lp)
    }

    class LayoutParams : FrameLayout.LayoutParams {

        var columnSpan = 1
        var rowSpan = 1

        constructor(
            context: Context,
            attributeSet: AttributeSet?
        ) : super(context, attributeSet) {
            context.withStyledAttributes(attributeSet, R.styleable.KeyboardLayout_Layout) {
                columnSpan = getInt(R.styleable.KeyboardLayout_Layout_keyColumnSpan, 1)
                rowSpan = getInt(R.styleable.KeyboardLayout_Layout_keyRowSpan, 1)
            }
        }

        constructor(
            width: Int,
            height: Int
        ) : super(width, height)

        constructor(
            lp: ViewGroup.LayoutParams
        ) : super(lp) {
            when (lp) {
                is LayoutParams -> {
                    columnSpan = lp.columnSpan
                    rowSpan = lp.rowSpan
                }
            }
        }

    }

}