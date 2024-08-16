package com.brins.simple

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * @author lipeilin
 * @date 2024/8/16
 * @desc
 */
abstract class BaseBindingFragment<VB: ViewBinding> : Fragment() {

    val TAG = this::class.java.simpleName

    private var _binding: VB? = null

    // This property is only valid between onCreateView and onDestroyView.
    protected val views: VB
        get() = _binding!!

    abstract fun getBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): android.view.View? {
        _binding = getBinding(inflater, container)
        return views.root
    }
}