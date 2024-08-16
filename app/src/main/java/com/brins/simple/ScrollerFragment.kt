package com.brins.simple

import android.view.LayoutInflater
import android.view.ViewGroup
import com.eightbitlab.blurview_sample.databinding.FragmentScrollBinding

/**
 * @author lipeilin
 * @date 2024/8/16
 * @desc
 */
class ScrollerFragment: BaseBindingFragment<FragmentScrollBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentScrollBinding {
        return FragmentScrollBinding.inflate(inflater, container, false)
    }
}