package com.zxq.purerss.ui.feedlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.databinding.FragmentFeedlistBinding
import com.zxq.purerss.ui.mainpage.MainPageAdapter
import com.zxq.purerss.utils.InjectorUtil

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class FeedListFragment: Fragment() {
    private val viewM: FeedListViewModel by viewModels {
        InjectorUtil.getFeedListFactory(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedlistBinding.inflate(inflater,container,false).apply {
            val onClick = object: FeedListAdapter.FeedItemClick{
                override fun onClick(view: View, rss: RssItem) {

                }
            }
            viewM.getFeedsList("",0L)
            val adapter = FeedListAdapter(onClick)
            recyclerview.adapter = adapter
            adapter.setDiffCallback(ItemDiffCallback())
            viewM.feedsList.observe(this@FeedListFragment, Observer {
                adapter.setDiffNewData(it)
            })
        }
        return binding.root
    }
}

private class ItemDiffCallback: DiffUtil.ItemCallback<RssItem>(){
    override fun areItemsTheSame(oldItem: RssItem, newItem: RssItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: RssItem, newItem: RssItem): Boolean {
        return oldItem == newItem
    }
}