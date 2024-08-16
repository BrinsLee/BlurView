package com.brins.blurlib.style

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.brins.blurlib.R

data class BlurViewStyle(
    /**
     * 模糊半径
     */
    var radius: Float,
    /**
     * 叠加颜色
     */
    @ColorInt var overlayColorInt: Int,
    /**
     * 模糊布局缩放因子
     */
    var scaleFactor: Float,
    val topLeftRadius: Float,
    val topRightRadius: Float,
    val bottomLeftRadius: Float,
    val bottomRightRadius: Float
) {
    companion object {
        operator fun invoke(context: Context, attrs: AttributeSet?): BlurViewStyle {
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.BlurView,
                0,
                R.style.Blur_BlurView
            )
            val radius = a.getFloat(R.styleable.BlurView_blurViewRadius, 25.0f)
            val overlayColor = a.getColor(R.styleable.BlurView_blurViewOverlayColor, ContextCompat.getColor(context, R.color.color_80ffffff))
            val scaleFactor = a.getFloat(R.styleable.BlurView_blurViewScaleFactor, 6f)
            val topLeftRadius = a.getDimension(R.styleable.BlurView_blurViewTopLeftRadius, 0f)
            val topRightRadius = a.getDimension(R.styleable.BlurView_blurViewTopRightRadius, 0f)
            val bottomLeftRadius = a.getDimension(R.styleable.BlurView_blurViewBottomLeftRadius, 0f)
            val bottomRightRadius = a.getDimension(R.styleable.BlurView_blurViewBottomRightRadius, 0f)
            a.recycle()
            return BlurViewStyle(radius, overlayColor, scaleFactor, topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius)
        }
    }
}