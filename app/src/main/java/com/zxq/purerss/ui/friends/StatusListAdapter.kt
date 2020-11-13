package com.zxq.purerss.ui.friends

import android.app.Activity
import android.graphics.Rect
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.DraggableModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.previewlibrary.GPreviewBuilder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.ImageItemInfo
import com.zxq.purerss.data.entity.StatusInfo
import com.zxq.purerss.databinding.ItemEventBinding
import com.zxq.purerss.listener.StatusClickListener
import com.zxq.purerss.widget.GridItemDecoration


/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class StatusListAdapter() :
    BaseQuickAdapter<StatusInfo, BaseViewHolder>(R.layout.item_event), DraggableModule {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemEventBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: StatusInfo) {
        val binding = holder.getBinding<ItemEventBinding>()
        binding?.item = item
        binding?.clickHandle = object : StatusClickListener {
            override fun onClick(view: View, rss: StatusInfo) {

            }
        }
        if (!item.pics.isNullOrEmpty()) {
            val mAdapter = StatusPicAdapter()
            val rvPics = holder.getView<RecyclerView>(R.id.rv_pics)
            rvPics.adapter = mAdapter
            val mLayout = object : GridLayoutManager(context!!, 3) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            mAdapter.setOnItemClickListener { adapter, view, position ->
                computeBoundsBackward(rvPics, item.pics!!.toList());//组成数据
                GPreviewBuilder.from(context as Activity)
                    .setData(item?.pics!!.toList())
                    .setIsScale(true)
                    .setCurrentIndex(position)
                    .setType(GPreviewBuilder.IndicatorType.Dot)
                    .start() //启动

            }
            rvPics.layoutManager = mLayout
            rvPics.addItemDecoration(
                GridItemDecoration.Builder(context!!).verSize(10).horSize(10).build()
            )
            rvPics.isNestedScrollingEnabled = false
            mAdapter.setNewData(item.pics)
        }
        binding?.executePendingBindings()
    }

    /**
     * 查找信息
     * @param list 图片集合
     */
    private fun computeBoundsBackward(rv: RecyclerView, list: List<ImageItemInfo>) {
        for (i in 0 until rv.childCount) {
            val itemView: View? = rv.getChildAt(i)
            val bounds = Rect()
            if (itemView != null) {
                val thumbView: ImageView = itemView.findViewById(R.id.iv_pic) as ImageView
                thumbView.getGlobalVisibleRect(bounds)
            }
            list[i].setBounds(bounds)
            list[i].uri = Uri.parse(list[i].getUrl())
        }
    }
}