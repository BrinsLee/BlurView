package com.brins.simple

import android.view.LayoutInflater
import android.view.ViewGroup
import com.eightbitlab.blurview_sample.databinding.FragmentImageBinding

/**
 * @author lipeilin
 * @date 2024/8/16
 * @desc
 */
class ImageFragment: BaseBindingFragment<FragmentImageBinding>() {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentImageBinding {

        return FragmentImageBinding.inflate(inflater, container, false)
    }
}