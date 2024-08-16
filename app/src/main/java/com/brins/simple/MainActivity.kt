package com.brins.simple

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.eightbitlab.blurview_sample.databinding.ActivityMainBinding
import kotlin.math.max

/**
 * @author lipeilin
 * @date 2024/8/16
 * @desc
 */
class MainActivity: AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupBlurView()
        setupViewPager()
    }

    private fun setupViewPager() {
        with(binding) {
            viewPager.offscreenPageLimit = 2
            viewPager.adapter = ViewPagerAdapter(
                supportFragmentManager
            )
            tabLayout.setupWithViewPager(viewPager)
        }
    }

    private fun setupBlurView() {
        val radius = 25f
        val minBlurRadius = 4f
        val step = 4f
        with(binding) {
            topBlurView.setupWith(root)
            centerBlurView.setupWith(root)
            bottomBlurView.setupWith(root)

            //.setFrameClearDrawable(windowBackground)
            //.setBlurRadius(radius);
            val initialProgress = (radius * step).toInt()
            radiusSeekBar.progress = initialProgress

            radiusSeekBar.setOnSeekBarChangeListener(object : SeekBarListenerAdapter() {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    var blurRadius = progress / step
                    blurRadius = max(blurRadius.toDouble(), minBlurRadius.toDouble()).toFloat()
                    topBlurView.updateRadius(blurRadius)
                    centerBlurView.updateRadius(blurRadius)
                    bottomBlurView.updateRadius(blurRadius)
                }
            })
        }
    }

    class ViewPagerAdapter internal constructor(fragmentManager: FragmentManager?) :
        FragmentPagerAdapter(fragmentManager!!) {
        override fun getItem(position: Int): Fragment {
            return Page.entries[position].fragment
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return Page.entries[position].title
        }

        override fun getCount(): Int {
            return Page.entries.size
        }
    }

    enum class Page(val title: String) {
        FIRST("ScrollView") {
            override val fragment: Fragment
                get() = ScrollerFragment()
        },
        SECOND("RecyclerView") {
            override val fragment: Fragment
                get() = RecyclerViewFragment()
        },
        THIRD("Static") {
            override val fragment: Fragment
                get() = ImageFragment()
        };

        abstract val fragment: Fragment
    }
}