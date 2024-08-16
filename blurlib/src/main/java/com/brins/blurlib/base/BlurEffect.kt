package com.brins.blurlib.base

import android.graphics.Bitmap
import android.graphics.Canvas

interface BlurEffect {

    /**
     * @param bitmap     bitmap to be blurred
     * @param blurRadius blur radius
     * @return blurred bitmap
     */
    fun blur(bitmap: Bitmap, blurRadius: Float): Bitmap

    /**
     * Frees allocated resources
     */
    fun destroy()

    /**
     * draw the blurred bitmap to canvas
     */
    fun render(canvas: Canvas, bitmap: Bitmap)

    /**
     * @return false if this algorithm returns the same instance of bitmap as it accepted
     *       true if it creates a new instance.
     */
    fun needModifyBitmap(): Boolean
}