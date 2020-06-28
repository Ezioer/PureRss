package com.zxq.purerss.ui.mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.databinding.FragmentNewsBinding
import com.zxq.purerss.utils.InjectorUtil

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class MainPageFragment: Fragment() {

    private val mainViewModel: MainPageViewModel by viewModels {
        InjectorUtil.getMainFactory(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewsBinding.inflate(inflater,container,false).apply {
            val onClick = object: MainPageAdapter.FeedClick{
                override fun onClick(view: View, rss: RSSFeedEntity) {

                }
            }
            mainViewModel.getFeedsList()
            val adapter = MainPageAdapter(onClick)
            recyclerview.adapter = adapter
            adapter.setHeaderView(getHeaderView())
            adapter.setDiffCallback(RssDiffCallback())
            mainViewModel.feedsList.observe(this@MainPageFragment, Observer {
                adapter.setDiffNewData(it)
            })
        }
        return binding.root
    }

    private fun getHeaderView(): View {
        val view = layoutInflater.inflate(R.layout.header_news,null,false)
        return view
    }
}

private class RssDiffCallback: DiffUtil.ItemCallback<RSSFeedEntity>(){
    override fun areItemsTheSame(oldItem: RSSFeedEntity, newItem: RSSFeedEntity): Boolean {
        return oldItem.feedId == newItem.feedId
    }

    override fun areContentsTheSame(oldItem: RSSFeedEntity, newItem: RSSFeedEntity): Boolean {
        return oldItem == newItem
    }
}