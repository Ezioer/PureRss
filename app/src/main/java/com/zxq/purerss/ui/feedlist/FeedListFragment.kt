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
import com.zxq.purerss.data.entity.RssFeedInfo
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssItemInfo
import com.zxq.purerss.databinding.FragmentFeedlistBinding
import com.zxq.purerss.listener.ItemClickListener
import com.zxq.purerss.listener.ItemDiffCallback
import com.zxq.purerss.utils.InjectorUtil

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
            val onClick = object: ItemClickListener{
                override fun onClick(view: View, rss: RssItem) {
                    viewM.readed(rss)
                    val action = FeedListFragmentDirections.actionListToDetail(RssItemInfo(rss.title,rss.link,rss.description,rss.pubdate,rss.author,1L,rss.title))
                    findNavController().navigate(action)
                }
            }
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            viewM.getFeedsList(mInfo!!.link, mInfo!!.id)
            val mAdapter = FeedListAdapter(onClick)
            mAdapter.setOnLaterListener(object: FeedListAdapter.OnLaterListener{
                override fun later(item: RssItem) {
                    viewM.later(item)
                }
            })

            mAdapter.setOnCollectListener(object: FeedListAdapter.OnCollectListener{
                override fun collect(item: RssItem) {
                    viewM.collectItem(item)
                }
            })
            recyclerview.adapter = mAdapter
            mAdapter.setDiffCallback(ItemDiffCallback())
            viewM.feedsList.observe(this@FeedListFragment, Observer {
                mAdapter.setDiffNewData(it)
            })
        }
        return binding.root
    }
}