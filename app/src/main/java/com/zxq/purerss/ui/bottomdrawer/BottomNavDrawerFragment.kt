package com.zxq.purerss.ui.bottomdrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import com.zxq.livedatabus.LiveDataBus
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.databinding.FragmentBottomNavDrawerBinding
import com.zxq.purerss.listener.ToggleStateCallBack
import com.zxq.purerss.utils.InjectorUtil

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/18
 *  fun
 */
class BottomNavDrawerFragment: Fragment() {
    private val viewM : AllFeedViewModel by viewModels {
        InjectorUtil.getAllFeedFactory(this)
    }
    private lateinit var binding: FragmentBottomNavDrawerBinding
    /*private val behavior: BottomSheetBehavior<FrameLayout> by lazy(LazyThreadSafetyMode.NONE) {
        from(binding.backgroundContainer)
    }*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomNavDrawerBinding.inflate(inflater, container, false)
        binding.navRecyclerView.setOnApplyWindowInsetsListener { view, windowInsets ->
            // Record the window's top inset so it can be applied when the bottom sheet is slide up
            // to meet the top edge of the screen.
            view.setTag(
                R.id.tag_system_window_inset_top,
                windowInsets.systemWindowInsetTop
            )
            windowInsets
        }
        var onclick: FeedClick = object: FeedClick{
            override fun onClick(view: View, rss: RSSFeedEntity) {
                LiveDataBus.get<RSSFeedEntity>("selectfeed").postValue(rss)
                close()
            }
        }
        viewM.getList()
        binding.apply {
            val mAdapter = AllFeedAdapter(context!!,onclick)
            navRecyclerView.adapter = mAdapter
            viewM.feedList.observe(this@BottomNavDrawerFragment, Observer {
                mAdapter.submitList(it)
            })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.run {
//            behavior.state = STATE_HIDDEN
        }
    }

    fun toggle() {
    /*    if (behavior.state == 2) behavior.state = STATE_HIDDEN
        when {
            behavior.state == STATE_HIDDEN -> open()
            behavior.state == STATE_HIDDEN
                    || behavior.state == STATE_HALF_EXPANDED
                    || behavior.state == STATE_EXPANDED
                    || behavior.state == STATE_COLLAPSED -> close()
        }*/
    }

    fun open() {
        callBack?.open()
//        behavior.state = STATE_HALF_EXPANDED
    }

    fun close() {
        callBack?.close()
//        behavior.state = STATE_HIDDEN
    }

    var callBack: ToggleStateCallBack? =null
    fun setToggleCallBack(callBack: ToggleStateCallBack){
        this.callBack = callBack
    }
}