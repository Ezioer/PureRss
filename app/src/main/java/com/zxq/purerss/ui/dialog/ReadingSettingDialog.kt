package com.zxq.purerss.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssItemInfo
import com.zxq.purerss.data.entity.table.RSSItemEntity
import com.zxq.purerss.listener.ItemTypeClickListener
import com.zxq.purerss.listener.ItemTypeDiffCallback
import com.zxq.purerss.ui.feedlist.FeedListFragmentDirections
import com.zxq.purerss.ui.type.FragmentType
import com.zxq.purerss.ui.type.TypeAdapter
import com.zxq.purerss.ui.type.TypeViewModel
import com.zxq.purerss.utils.KeyBoardUtil
import com.zxq.purerss.utils.addOnAfterChange
import kotlinx.android.synthetic.main.dialog_search.*

class ReadingSettingDialog(
    private val mContext: Context
) : BaseDialog(mContext,Gravity.BOTTOM, R.style.anim_bottom2top,true,1.0,0.0) {
   private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_readsetting,null)
        setContentView(mView)
        initView()
    }

    private fun initView() {
    }
}