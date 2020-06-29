package com.zxq.purerss.ui.type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zxq.purerss.data.entity.RssItemInfo
import com.zxq.purerss.data.entity.table.RSSItemEntity
import com.zxq.purerss.databinding.FragmentTypeBinding
import com.zxq.purerss.listener.ItemTypeClickListener
import com.zxq.purerss.listener.ItemTypeDiffCallback
import com.zxq.purerss.ui.feedlist.FeedListFragmentDirections
import com.zxq.purerss.utils.InjectorUtil

class FragmentType: Fragment() {
    private val viewM: TypeViewModel by viewModels {
        InjectorUtil.getTypeFactory(this)
    }
    private val args: FragmentTypeArgs by navArgs()
    private var type = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTypeBinding.inflate(inflater,container,false).apply {
            type = args.type
            when (type) {
                1 -> {
                    pageType.text = "已读"
                }
                2 -> {
                    pageType.text = "收藏"
                }
                else -> {
                    pageType.text = "稍后阅读"
                }
            }
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            viewM.getFeedsList(type)
            val onClick = object : ItemTypeClickListener {
                override fun onClick(view: View, rss: RSSItemEntity) {
                    val action = FeedListFragmentDirections.actionListToDetail(
                        RssItemInfo(
                            rss.itemTitle,
                            rss.itemLink,
                            rss.itemDesc,
                            rss.itemDate,
                            rss.itemAuthor,
                            rss.itemFeed,
                            rss.feedTitle
                        )
                    )
                    findNavController().navigate(action)
                }
            }
            val adapter = TypeAdapter(onClick)
            adapter.setOnRemoveListener(object : TypeAdapter.OnRemoveListener {
                override fun onRemove(item: RSSItemEntity) {
                    adapter.data.remove(item)
                }
            })
            recyclerview.adapter = adapter
            adapter.setDiffCallback(ItemTypeDiffCallback())
            viewM.feedsList.observe(this@FragmentType, Observer {
                adapter.setDiffNewData(it)
            })
        }
        return binding.root
    }
}