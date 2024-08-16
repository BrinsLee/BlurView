package com.brins.blurlib.base

import android.graphics.Canvas
import android.view.ViewGroup

interface BlurController {

    /**
     * 启用禁用模糊，默认为true @param启用true启用false禁用
     *
     * @param enable true to enable / false to disable
     */
    fun setBlurEnable(enable: Boolean)

    /**
     * 设置一个根视图开始模糊。可以是Activity的根视图，也可以是任何你想要模糊的视图
     */
    fun setupRootView(rootView: ViewGroup)

    /**
     * 可用于停止模糊自动更新或恢复
     *
     *
     */
    fun setBlurAutoUpdate(enabled: Boolean)

    /**
     *
     * 更新模糊半径默认值10 {@link BlurViewStyle.radius}
     */
    fun updateRadius(radius: Float)

    /**
     *
     * 更新覆盖颜色默认值4DFFFFFF
     */
    fun updateOverlayColor(overlayColor: Int)

    /**
     * 应用模糊效果
     */
    fun applyBlurEffect(canvas: Canvas): Boolean

    /**
     * 用于在BlurView的大小发生变化时进行修改
     */
    fun updateBlurViewSize()

    /**
     * 释放资源
     */
    fun destroy()
}