package com.zxq.purerss.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
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
import com.zxq.purerss.utils.SpringAddItemAnimator
import com.zxq.purerss.utils.addOnAfterChange
import com.zxq.purerss.utils.getSpValue
import kotlinx.android.synthetic.main.dialog_search.*

class SearchItemDialog(
    private val mContext: Context,
    private var mainViewModel: TypeViewModel,
    private var mainPageFragment: FragmentType,
    private var type: Int,
    private var onClick: ItemTypeClickListener
) : BaseDialog(mContext,Gravity.BOTTOM, R.style.anim_bottom2top,true,1.0,0.5) {
   private lateinit var mView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_search,null)
        setContentView(mView)
        initView()
    }

    private fun initView() {
        KeyBoardUtil.showKeyboard(et_search)
        et_search.addOnAfterChange {
            mainViewModel.searchItem(it.toString(),type)
        }
        /*val onClick = object: ItemTypeClickListener {
            override fun onClick(view: View, rss: RSSItemEntity) {
                val extra = FragmentNavigatorExtras(view to "rssdetail")
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
                view.findNavController().navigate(action,extra)
            }
        }*/
        val adapter = TypeAdapter(onClick, mContext?.getSpValue("slide", 0) == 0)
        recyclerview.adapter = adapter
        recyclerview.itemAnimator = SpringAddItemAnimator()
        adapter.setDiffCallback(ItemTypeDiffCallback())
        mainViewModel.itemList.observe(mainPageFragment, Observer {
            adapter.setDiffNewData(it)
        })
    }
}