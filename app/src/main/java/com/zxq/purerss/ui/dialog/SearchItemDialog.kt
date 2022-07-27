package com.zxq.purerss.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.zxq.purerss.R
import com.zxq.purerss.listener.ItemTypeClickListener
import com.zxq.purerss.listener.ItemTypeDiffCallback
import com.zxq.purerss.ui.type.FragmentType
import com.zxq.purerss.ui.type.TypeAdapter
import com.zxq.purerss.ui.type.TypeViewModel
import com.zxq.purerss.utils.KeyBoardUtil
import com.zxq.purerss.utils.SpringAddItemAnimator
import com.zxq.purerss.utils.addOnAfterChange
import com.zxq.purerss.utils.getSpValue

class SearchItemDialog(
    private val mContext: Context,
    private var mainViewModel: TypeViewModel,
    private var mainPageFragment: FragmentType,
    private var type: Int,
    private var onClick: ItemTypeClickListener
) : BaseDialog(mContext,Gravity.BOTTOM, R.style.anim_bottom2top,true,1.0,0.5) {
    private lateinit var mView: View
    private lateinit var etSearch: EditText
    private lateinit var pageType: TextView
    private lateinit var ivClose: ImageView
    private lateinit var rvList: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_search, null)
        setContentView(mView)
        etSearch = findViewById(R.id.et_search)
        pageType = findViewById(R.id.page_type)
        ivClose = findViewById(R.id.iv_close)
        rvList = findViewById(R.id.recyclerview)
        initView()
    }

    private fun initView() {
        KeyBoardUtil.showKeyboard(etSearch)
        etSearch.addOnAfterChange {
            mainViewModel.searchItem(it.toString(), type)
        }
        pageType.text = mContext.getString(R.string.searchtype)
        ivClose.setOnClickListener { dismiss() }
        val adapter = TypeAdapter(onClick, mContext?.getSpValue("slide", 0) == 0)
        rvList.adapter = adapter
        rvList.itemAnimator = SpringAddItemAnimator()
        adapter.setDiffCallback(ItemTypeDiffCallback())
        mainViewModel.itemList.observe(mainPageFragment, Observer {
            adapter.setDiffNewData(it)
        })
    }
}