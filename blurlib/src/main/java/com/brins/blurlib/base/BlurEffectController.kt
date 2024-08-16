package com.brins.blurlib.base

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.ColorInt
import com.brins.blurlib.style.BlurViewStyle
import com.brins.blurlib.util.SizeScaleHelper

class BlurEffectController(
    val blurView: View,
    var rootView: ViewGroup?,
    val style: BlurViewStyle
) : BlurController {

    companion object {
        @ColorInt
        val TRANSPARENT = 0
    }

    // 内部模糊bitmap
    private lateinit var internalBitmap: Bitmap
    // 内部模糊画布
    private lateinit var internalCanvas: BlurViewCanvas
    private var blurEnabled = true
    private var initialized = false

    private val rootLocation = IntArray(2)
    private val blurViewLocation = IntArray(2)
    private val blurEffect: BlurEffect
    // 用于绘制圆角
    private lateinit var path: Path
    private lateinit var rect: RectF

    private val drawListener = ViewTreeObserver.OnPreDrawListener { // Not invalidating a View here, just updating the Bitmap.
        // This relies on the HW accelerated bitmap drawing behavior in Android
        // If the bitmap was drawn on HW accelerated canvas, it holds a reference to it and on next
        // drawing pass the updated content of the bitmap will be rendered on the screen
        updateBlur()
        true
    }
    init {
        val measureWidth = blurView.measuredWidth
        val measureHeight = blurView.measuredHeight
        blurEffect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            BlurRenderEffectImpl()
        } else {
            BlurBitmapEffectImpl()
        }
        initBlurViewSize(measureWidth, measureHeight)
    }

    /**
     * 初始化BlurView宽高
     */
    private fun initBlurViewSize(measureWidth: Int, measureHeight: Int) {
        setBlurAutoUpdate(true)
        val scaleHelper: SizeScaleHelper = SizeScaleHelper(style.scaleFactor)
        if (scaleHelper.isZeroSized(measureWidth, measureHeight)) {
            // 宽或高为0，不绘制
            blurView.setWillNotDraw(true)
            return
        }
        blurView.setWillNotDraw(false)
        val bitmapSize = scaleHelper.scale(measureWidth, measureHeight)
        Log.d("BlurEffectController", "initBlurViewSize scaleFactorW: ${measureWidth / bitmapSize.width}")
        Log.d("BlurEffectController", "initBlurViewSize scaleFactorH: ${measureHeight / bitmapSize.height}")

        internalBitmap = Bitmap.createBitmap(bitmapSize.width, bitmapSize.height, Bitmap.Config.ARGB_8888)
        internalCanvas = BlurViewCanvas(internalBitmap)
        rect = RectF(0f, 0f, measureWidth.toFloat(), measureHeight.toFloat())
        path = Path()
        path.addRoundRect(
            rect,
            floatArrayOf(style.topLeftRadius,style.topLeftRadius,
                style.topRightRadius,style.topRightRadius,
                style.bottomRightRadius, style.bottomRightRadius,
                style.bottomLeftRadius, style.bottomLeftRadius
            ),
            Path.Direction.CW
        )
        initialized = true
        updateBlur()
    }

    private fun updateBlur() {
        if (!initialized) {
            return
        }
        // 填充透明色
        internalBitmap.eraseColor(Color.TRANSPARENT)
        internalCanvas.save()
        setupInternalCanvasMatrix()
        rootView?.draw(internalCanvas)
        internalCanvas.restore()

        blurAndSave()
    }

    private fun blurAndSave() {
        blurEffect.blur(internalBitmap, style.radius.toFloat())

    }

    /**
     * 将画布移动到blurView位置
     */
    private fun setupInternalCanvasMatrix() {
        if (rootView == null) {
            return
        }
        rootView!!.getLocationOnScreen(rootLocation)
        blurView.getLocationOnScreen(blurViewLocation)

        val left = blurViewLocation[0] - rootLocation[0]
        val top = blurViewLocation[1] - rootLocation[1]

        val scaleFactorH = blurView.height.toFloat() / internalBitmap.height
        val scaleFactorW = blurView.width.toFloat() / internalBitmap.width

        val scaledLeftPosition = -left / scaleFactorW
        val scaledTopPosition = -top / scaleFactorH

        internalCanvas.translate(scaledLeftPosition, scaledTopPosition)
        internalCanvas.scale(1 / scaleFactorW, 1 / scaleFactorH)
    }

    override fun setBlurAutoUpdate(autoUpdate: Boolean) {
        if (rootView == null) {
            return
        }
        rootView?.apply {
            rootView.viewTreeObserver.removeOnPreDrawListener(drawListener)
            blurView.viewTreeObserver.removeOnPreDrawListener(drawListener)
            if (autoUpdate) {
                rootView.viewTreeObserver.addOnPreDrawListener(drawListener)
                // Track changes in the blurView window too, for example if it's in a bottom sheet dialog
                if (rootView.windowId !== blurView.windowId) {
                    blurView.viewTreeObserver.addOnPreDrawListener(drawListener)
                }
            }
        }

    }

    override fun setupRootView(rootView: ViewGroup) {
        this.rootView = rootView
        val measureWidth = blurView.measuredWidth
        val measureHeight = blurView.measuredHeight
        initBlurViewSize(measureWidth, measureHeight)
    }

    override fun setBlurEnable(enable: Boolean) {
        blurEnabled = enable
    }

    override fun updateRadius(radius: Float) {
        this.style.radius = radius
    }

    override fun updateOverlayColor(overlayColor: Int) {
        this.style.overlayColorInt = overlayColor
    }

    override fun applyBlurEffect(canvas: Canvas): Boolean {
        if (!initialized || !blurEnabled) {
            return true
        }

        if (canvas is BlurViewCanvas) {
            return false
        }

        val scaleFactorH = blurView.height.toFloat() / internalBitmap.height
        val scaleFactorW = blurView.width.toFloat() / internalBitmap.width
        Log.d("BlurEffectController", "applyBlurEffect scaleFactorH: $scaleFactorH")
        canvas.clipPath(path)
        canvas.save()
        canvas.scale(scaleFactorW, scaleFactorH)
        blurEffect.render(canvas, internalBitmap)
        canvas.restore()

        if (style.overlayColorInt != TRANSPARENT) {
            canvas.drawColor(style.overlayColorInt)
        }
        return true
    }

    override fun updateBlurViewSize() {
        val measuredWidth = blurView.measuredWidth
        val measuredHeight = blurView.measuredHeight

        initBlurViewSize(measuredWidth, measuredHeight)
    }

    override fun destroy() {
        setBlurAutoUpdate(false)
        blurEffect.destroy()
        initialized = false
    }

}