package com.lollipop.countdown.view

import android.content.Context
import android.graphics.Outline
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.lollipop.countdown.R

class ClipFrameLayout @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet) {

    private val customOutlineProvider = OutlineProvider()

    var radius: Float
        get() {
            return customOutlineProvider.radius
        }
        set(value) {
            customOutlineProvider.radius = value
            invalidateOutline()
        }

    init {
        clipToOutline = true
        outlineProvider = customOutlineProvider
        context.withStyledAttributes(attributeSet, R.styleable.ClipFrameLayout) {
            radius = getDimension(R.styleable.ClipFrameLayout_clipRadius, 0f)
        }
    }

    private class OutlineProvider : ViewOutlineProvider() {

        var radius = 0f

        override fun getOutline(view: View, outline: Outline) {
            outline.setRoundRect(0, 0, view.width, view.height, radius)
        }
    }

}