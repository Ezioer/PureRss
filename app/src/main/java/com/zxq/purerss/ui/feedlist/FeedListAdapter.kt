package com.zxq.purerss.ui.feedlist

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssItemInfo
import com.zxq.purerss.data.entity.table.RSSItemEntity
import com.zxq.purerss.databinding.ItemContentListBinding
import com.zxq.purerss.listener.ItemClickListener
import com.zxq.purerss.widget.SwipeMenuLayout

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class FeedListAdapter(private var slideDir: Boolean) :
    BaseQuickAdapter<RSSItemEntity, BaseViewHolder>(R.layout.item_content_list) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemContentListBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: RSSItemEntity) {
        if (item == null) {
            return
        }
        val binding = holder.getBinding<ItemContentListBinding>()
        binding?.collect?.setOnClickListener {
            (binding.root as SwipeMenuLayout).smoothClose()
            onCollectListener?.collect(item)
        }
        binding?.later?.setOnClickListener {
            (binding.root as SwipeMenuLayout).smoothClose()
            onLaterListener?.later(item)
        }
        binding?.ivReddot?.visibility = if (item.itemRead == 0) View.VISIBLE else View.GONE
        binding?.swipe?.setLeftSwipe(slideDir)
        binding?.item = item
        binding?.clickHandle = object : ItemClickListener {
            override fun onClick(view: View, rss: RSSItemEntity) {
                val extra = FragmentNavigatorExtras(view to "rssdetail")
                val action = FeedListFragmentDirections.actionListToDetail(
                    RssItemInfo(
                        rss.itemTitle,
                        rss.itemLink,
                        rss.itemDesc,
                        rss.itemDate,
                        rss.itemAuthor,
                        1L,
                        rss.feedTitle,
                        rss.itemPic
                    )
                )
                view.findNavController().navigate(action, extra)
            }
        }
        binding?.executePendingBindings()
    }

    private var onCollectListener: OnCollectListener? = null
    fun setOnCollectListener(l: OnCollectListener) {
        onCollectListener = l
    }

    interface OnCollectListener {
        fun collect(item: RSSItemEntity)
    }

    private var onLaterListener: OnLaterListener? = null
    fun setOnLaterListener(l: OnLaterListener) {
        onLaterListener = l
    }

    interface OnLaterListener {
        fun later(item: RSSItemEntity)
    }
}