package com.zxq.purerss.ui.friends

import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.ImageItemInfo
import com.zxq.purerss.databinding.ItemAddpicBinding
import com.zxq.purerss.utils.PixelUtil

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class AddPicAdapter() :
    BaseQuickAdapter<ImageItemInfo, BaseViewHolder>(R.layout.item_addpic) {

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        DataBindingUtil.bind<ItemAddpicBinding>(viewHolder.itemView)
    }

    override fun convert(holder: BaseViewHolder, item: ImageItemInfo) {
        val binding = holder.getBinding<ItemAddpicBinding>()
        val layoutParams = binding!!.rlRoot.layoutParams
        layoutParams.width =
            (PixelUtil.getScreenWidth(context) - PixelUtil.dp2px(context, 40f) - 20) / 3
        layoutParams.height =
            (PixelUtil.getScreenWidth(context) - PixelUtil.dp2px(context, 40f) - 20) / 3
        binding!!.rlRoot.layoutParams = layoutParams
        if (item!!.cropUrl != null && item!!.cropUrl!!.isNotEmpty()) {
            Glide.with(binding!!.root).load(item!!.cropUrl).override(
                (PixelUtil.getScreenWidth(context) - PixelUtil.dp2px(
                    context,
                    40f
                ) - 20) / 3
            ).into(binding.ivPic);
        } else {
            if (item.uri != null) {
                Glide.with(binding!!.root).load(item.uri).override(
                    (PixelUtil.getScreenWidth(context) - PixelUtil.dp2px(
                        context,
                        40f
                    ) - 20) / 3
                ).into(binding.ivPic);
            } else if (!item.filePath.isNullOrEmpty()) {
                Glide.with(binding!!.root).load(item.filePath).override(
                    (PixelUtil.getScreenWidth(context) - PixelUtil.dp2px(
                        context,
                        40f
                    ) - 20) / 3
                ).into(binding.ivPic);
            } else {
                Glide.with(binding!!.root).load(R.drawable.add_100px).into(binding.ivPic)
            }
        }
        binding?.executePendingBindings()
    }
}