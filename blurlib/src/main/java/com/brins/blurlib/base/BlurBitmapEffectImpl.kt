package com.brins.blurlib.base

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.google.android.renderscript.Toolkit
import kotlin.math.roundToInt

class BlurBitmapEffectImpl(): BlurEffect {

    private var lastBlurRadius = 1f
    private var height: Int = 0
    private var width: Int = 0
    private val paint = Paint(Paint.FILTER_BITMAP_FLAG)

    /**
     * @param bitmap     bitmap to be blurred
     * @param blurRadius blur radius
     * @return blurred bitmap
     */
    override fun blur(bitmap: Bitmap, blurRadius: Float): Bitmap {
        lastBlurRadius = blurRadius
        if (bitmap.height != height || bitmap.width != width) {
            height = bitmap.height
            width = bitmap.width
        }
        val blurBitmap = Toolkit.blur(bitmap, blurRadius.roundToInt())
        if (bitmap.width == blurBitmap.width && bitmap.height == blurBitmap.height) {
            val pixels = IntArray(blurBitmap.width * blurBitmap.height)
            blurBitmap.getPixels(pixels, 0, blurBitmap.width, 0, 0, blurBitmap.width, blurBitmap.height)
            bitmap.setPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        }
        return blurBitmap
//        return bitmap
    }

    override fun destroy() {
    }

    override fun render(canvas: Canvas, bitmap: Bitmap) {
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }

    override fun needModifyBitmap(): Boolean {
        return true
    }
}
