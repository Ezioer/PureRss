package com.zxq.purerss.ui.mainpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssFeedInfo
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.databinding.FragmentNewsBinding
import com.zxq.purerss.utils.InjectorUtil
import kotlinx.android.synthetic.main.header_news.*

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
                    val action = MainPageFragmentDirections.actionMainpageToList(RssFeedInfo(rss.feedTitle,rss.feedLink,rss.feedDesc,"",rss.feedId))
                    findNavController().navigate(action)
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
        view.findViewById<TextView>(R.id.tv_readed).setOnClickListener { onTypeClick(1) }
        view.findViewById<ImageView>(R.id.iv_readedmore).setOnClickListener { onTypeClick(1) }
        view.findViewById<TextView>(R.id.tv_collect).setOnClickListener { onTypeClick(2) }
        view.findViewById<ImageView>(R.id.iv_collectmore).setOnClickListener { onTypeClick(2) }
        view.findViewById<TextView>(R.id.tv_laterread).setOnClickListener { onTypeClick(3) }
        view.findViewById<ImageView>(R.id.iv_laterreadmore).setOnClickListener { onTypeClick(3) }
        return view
    }

    private fun onTypeClick(i: Int) {
        val action = MainPageFragmentDirections.actionMainpageToType(i)
        findNavController().navigate(action)
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