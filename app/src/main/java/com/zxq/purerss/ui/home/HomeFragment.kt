package com.zxq.purerss.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.zxq.livedatabus.LiveDataBus
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssFeed
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.databinding.FragmentHomeBinding
import com.zxq.purerss.listener.ToggleStateCallBack
import com.zxq.purerss.ui.bottomdrawer.BottomNavDrawerFragment
import com.zxq.purerss.ui.dialog.MenuListBottomDialog
import com.zxq.purerss.utils.InjectorUtil
import com.zxq.purerss.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_home.*

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/18
 *  fun
 */
class HomeFragment: Fragment() {
    private val viewM : FeedContentViewModel by viewModels {
        InjectorUtil.getFeedListFactory(this)
    }
    private var list = ArrayList<RSSFeedEntity>()
    /*private val bottomNavDrawer: BottomNavDrawerFragment by lazy(LazyThreadSafetyMode.NONE) {
        childFragmentManager?.findFragmentById(R.id.bottom_nav_drawer) as BottomNavDrawerFragment
    }*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bind = FragmentHomeBinding.inflate(inflater,container,false).apply {
            bottomAppBar.setOnMenuItemClickListener {
                    true
            }
            bottomAppBarContentContainer.setOnClickListener {
               toggle()
            }

           setCallBack()
            viewM.getAllFeeds()
            viewM.feeds.observe(this@HomeFragment, Observer {
                list.addAll(it)
                viewM.getFeedList(it[0].feedLink,it[0].feedId)
            })
            val adapter = FeedContentAdapter(context!!)
            recyclerView.adapter = adapter
            viewM.feedData.observe(this@HomeFragment, Observer {
                adapter.submitList(it.items)
            })

            LiveDataBus.get<RSSFeedEntity>("selectfeed").observe(this@HomeFragment, Observer {
                viewM.getFeedList(it.feedLink,it.feedId)
            })
        }
        return bind.root
    }

    private fun setCallBack() {
       /* bottomNavDrawer.setToggleCallBack(object: ToggleStateCallBack{
            override fun open() {
                animationImg(true)
            }

            override fun close() {
                animationImg(false)
            }

        })*/
    }

    private var mDialog: MenuListBottomDialog? =null
    private fun toggle() {
        if (mDialog == null){
            mDialog = MenuListBottomDialog(context!!,list)
        }
        mDialog?.show()
    }

    private fun animationImg(isOpen: Boolean){
      if (isOpen){
          ViewUtils.expanAni(bottom_app_bar_chevron)
      } else {
          ViewUtils.collAni(bottom_app_bar_chevron)
      }
    }
}