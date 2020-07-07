package com.zxq.purerss.ui.feedlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialSharedAxis
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssFeedInfo
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssItemInfo
import com.zxq.purerss.data.entity.table.RSSItemEntity
import com.zxq.purerss.databinding.FragmentFeedlistBinding
import com.zxq.purerss.listener.ItemClickListener
import com.zxq.purerss.listener.ItemRssDiffCallback
import com.zxq.purerss.utils.InjectorUtil
import com.zxq.purerss.utils.SpringAddItemAnimator
import com.zxq.purerss.utils.getSpValue
import java.util.concurrent.TimeUnit

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class FeedListFragment: Fragment() {
    private val args: FeedListFragmentArgs by navArgs()
    private var mInfo: RssFeedInfo? = null
    private val viewM: FeedListViewModel by viewModels {
        InjectorUtil.getFeedsListFactory(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedlistBinding.inflate(inflater,container,false).apply {
            lifecycleOwner = this@FeedListFragment
            mInfo = args.feedinfo
            feedinfo = mInfo
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            viewM.getFeedsList(mInfo!!.link, mInfo!!.id, false)
            val mAdapter = FeedListAdapter(context?.getSpValue("slide", 0) == 0)
            mAdapter.setOnLaterListener(object: FeedListAdapter.OnLaterListener{
                override fun later(item: RSSItemEntity) {
                    viewM.later(item)
                }
            })

            mAdapter.setOnCollectListener(object: FeedListAdapter.OnCollectListener{
                override fun collect(item: RSSItemEntity) {
                    viewM.collectItem(item)
                }
            })
            recyclerview.adapter = mAdapter
            recyclerview.itemAnimator = SpringAddItemAnimator()
            mAdapter.setDiffCallback(ItemRssDiffCallback())
            viewM.feedsList.observe(this@FeedListFragment, Observer {
                mAdapter.setDiffNewData(it)
                if (refreshlayout.isRefreshing) {
                    refreshlayout.isRefreshing = false
                    recyclerview.smoothScrollToPosition(0)
                }
            })

            viewM.collectResult.observe(this@FeedListFragment, Observer {
                if (it == 1) {
                    Toast.makeText(context, R.string.collectsuccess, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, R.string.collectfail, Toast.LENGTH_SHORT).show()
                }
            })
            viewM.laterResult.observe(this@FeedListFragment, Observer {
                if (it == 1) {
                    Toast.makeText(context, R.string.latersuccess, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, R.string.laterfail, Toast.LENGTH_SHORT).show()
                }
            })
            refreshlayout.setOnRefreshListener {
                viewM.getFeedsList(mInfo!!.link, mInfo!!.id, true)
            }
        }
        val forward = MaterialSharedAxis.create(MaterialSharedAxis.Y, true)
        enterTransition = forward

        val backward = MaterialSharedAxis.create(MaterialSharedAxis.Y, false)
        returnTransition = backward
        postponeEnterTransition(6L, TimeUnit.MILLISECONDS)
        return binding.root
    }
}