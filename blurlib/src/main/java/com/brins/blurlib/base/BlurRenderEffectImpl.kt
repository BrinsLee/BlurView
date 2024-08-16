package com.brins.blurlib.base

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RenderEffect
import android.graphics.RenderNode
import android.graphics.Shader
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.S)
class BlurRenderEffectImpl() : BlurEffect {

    private val node = RenderNode("BlurViewNode")

    private var height: Int = 0
    private var width: Int = 0
    private var lastBlurRadius = 1f

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
            node.setPosition(0, 0, width, height)
        }
        val canvas: Canvas = node.beginRecording()
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        node.endRecording()
        node.setRenderEffect(
            RenderEffect.createBlurEffect(
                blurRadius,
                blurRadius,
                Shader.TileMode.MIRROR
            )
        )
        return bitmap
    }

    override fun destroy() {
        node.discardDisplayList()
    }

    override fun render(canvas: Canvas, bitmap: Bitmap) {
        if (canvas.isHardwareAccelerated) {
            canvas.drawRenderNode(node)
        }
    }


    override fun needModifyBitmap(): Boolean {
        return false
    }
}