package com.brins.blurlib.util

import kotlin.math.ceil

class SizeScaleHelper(private val scaleFactor: Float) {

    companion object {
        private const val ROUNDING_VALUE = 64
    }

    fun scale(width: Int, height: Int): Size {

        val nonRoundedScaledWidth = downscaleSize(width.toFloat())
        val scaledWidth = roundSize(nonRoundedScaledWidth)
        //Only width has to be aligned to ROUNDING_VALUE
        val roundingScaleFactor = width.toFloat() / scaledWidth
        //Ceiling because rounding or flooring might leave empty space on the View's bottom
        val scaledHeight = ceil((height / roundingScaleFactor).toDouble()).toInt()

        return Size(
            scaledWidth,
            scaledHeight,
            roundingScaleFactor
        )
    }


    fun isZeroSized(measuredWidth: Int, measuredHeight: Int): Boolean {
        return downscaleSize(measuredHeight.toFloat()) == 0 || downscaleSize(measuredWidth.toFloat()) == 0
    }

    /**
     * 将值调整到ROUNDING_VALUE的倍数
     */
    private fun roundSize(value: Int): Int {
        return if (value % ROUNDING_VALUE == 0) {
            value
        } else value - value % ROUNDING_VALUE + ROUNDING_VALUE
    }

    /**
     * 根据缩放比例获取缩小后的尺寸
     */
    private fun downscaleSize(value: Float): Int {
        // 向上取整
        return ceil((value / scaleFactor)).toInt()
    }
}

data class Size(val width: Int, val height: Int, val scaleFactor: Float) {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val size: Size = o as Size
        if (width != size.width) return false
        return if (height != size.height) false else java.lang.Float.compare(
            size.scaleFactor, scaleFactor
        ) == 0
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result =
            31 * result + if (scaleFactor != +0.0f) java.lang.Float.floatToIntBits(scaleFactor) else 0
        return result
    }

    override fun toString(): String {
        return "Size{width=$width, height=$height, scaleFactor=$scaleFactor}"
    }
}