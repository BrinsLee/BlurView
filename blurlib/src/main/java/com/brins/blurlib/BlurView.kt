package com.brins.blurlib

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.brins.blurlib.base.BlurController
import com.brins.blurlib.base.BlurEffectController
import com.brins.blurlib.base.getActivityDecorView
import com.brins.blurlib.style.BlurViewStyle

open class BlurView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null, def: Int = 0
) : FrameLayout(context, attributeSet, def) {

    private val blurEffectController: BlurController
    private var style: BlurViewStyle

    companion object {

        private val TAG = this::class.simpleName
    }


    init {
        style = BlurViewStyle(context, attributeSet)
        blurEffectController = BlurEffectController(this,
            null, style)
//        setBackgroundColor(ContextCompat.getColor(context, R.color.color_80ffffff))
    }



    fun updateRadius(radius: Float) {
        if (style.radius != radius) {
            style.radius = radius
            blurEffectController.updateRadius(radius)
            invalidate()
        }
    }

    fun updateOverlayColor(@ColorInt color: Int) {
        if (style.overlayColorInt != color) {
            style.overlayColorInt = color
            blurEffectController.updateOverlayColor(color)
            invalidate()
        }
    }

    fun setBlurEnable(enable: Boolean) {
        blurEffectController.setBlurEnable(enable)
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        require(isHardwareAccelerated) {
            "BlurView can't be used in not hardware-accelerated window!"
        }
        blurEffectController.setBlurAutoUpdate(true)
    }

    override fun draw(canvas: Canvas) {
        val shouldDraw = blurEffectController.applyBlurEffect(canvas)
        if (shouldDraw) {
            super.draw(canvas)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        blurEffectController.updateBlurViewSize()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        blurEffectController.setBlurAutoUpdate(false)
    }


    fun setupWith(rootView: ViewGroup) {
        Log.d(TAG, "setupWith")
        blurEffectController.setupRootView(rootView)
    }



}