package com.brins.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.eightbitlab.blurview_sample.databinding.FragmentListBinding

/**
 * @author lipeilin
 * @date 2024/8/16
 * @desc
 */
class RecyclerViewFragment: BaseBindingFragment<FragmentListBinding>() {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentListBinding {
        return FragmentListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        views?.apply {
            recyclerView.adapter = ExampleListAdapter(context)
            recyclerView.layoutManager = LinearLayoutManager(context)
        }
    }
}