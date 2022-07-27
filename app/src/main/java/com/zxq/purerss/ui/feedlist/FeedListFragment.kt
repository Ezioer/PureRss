package com.zxq.purerss.ui.feedlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.zxq.purerss.R
import com.zxq.purerss.data.Constant
import com.zxq.purerss.data.entity.RssFeedInfo
import com.zxq.purerss.data.entity.table.RSSItemEntity
import com.zxq.purerss.databinding.FragmentFeedlistBinding
import com.zxq.purerss.listener.RssItemDiffCallback
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
    private var binding: FragmentFeedlistBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedlistBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@FeedListFragment
            mInfo = args.feedinfo
            feedinfo = mInfo
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            viewM.getFeedsList(mInfo!!.link, mInfo!!.id, false)
            val mAdapter = FeedListAdapter(context?.getSpValue("slide", 0) == 0)
            mAdapter.setOnLaterListener(object : FeedListAdapter.OnLaterListener {
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
            viewM.feedsList.observe(this@FeedListFragment, Observer {
                status = Constant.SUCCESS
                val diffResult =
                    DiffUtil.calculateDiff(RssItemDiffCallback(mAdapter.data, it), false)
                mAdapter.setDiffNewData(diffResult, it)
                if (refreshlayout.isRefreshing) {
                    refreshlayout.isRefreshing = false
                    recyclerview.smoothScrollToPosition(0)
                }
            })

            viewM.collectResult.observe(this@FeedListFragment, Observer {
                if (it == 1) {
                    Snackbar.make(recyclerview, R.string.collectsuccess, 600).show()
                } else {
                    Snackbar.make(recyclerview, R.string.collectfail, 600).show()
                }
            })
            viewM.laterResult.observe(this@FeedListFragment, Observer {
                if (it == 1) {
                    Snackbar.make(recyclerview, R.string.latersuccess, 600).show()
                } else {
                    Snackbar.make(recyclerview, R.string.laterfail, 600).show()
                }
            })
            refreshlayout.setOnRefreshListener {
                viewM.getFeedsList(mInfo!!.link, mInfo!!.id, true)
            }

            viewM.status.observe(this@FeedListFragment, Observer {
                status = it
            })


        }
        /*val forward = MaterialSharedAxis.create(MaterialSharedAxis.Y, true)
        enterTransition = forward

        val backward = MaterialSharedAxis.create(MaterialSharedAxis.Y, false)
        returnTransition = backward*/
        postponeEnterTransition(6L, TimeUnit.MILLISECONDS)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}