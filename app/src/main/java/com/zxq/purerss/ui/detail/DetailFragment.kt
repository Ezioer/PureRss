package com.zxq.purerss.ui.detail

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.zxq.purerss.App
import com.zxq.purerss.data.entity.RssItemInfo
import com.zxq.purerss.databinding.FragmentDetailBinding
import com.zxq.purerss.utils.MImageGetter

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/18
 *  fun
 */
class DetailFragment : Fragment() {
    private val arg: DetailFragmentArgs by navArgs()
    private var mRssItemInfo: RssItemInfo? =null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bind = FragmentDetailBinding.inflate(inflater, container, false).apply {
            mRssItemInfo = arg.itemcontent
            lifecycleOwner = this@DetailFragment
            info = mRssItemInfo
            tvContent.text = Html.fromHtml(
                mRssItemInfo?.description,
                MImageGetter(tvContent, App.instance!!.applicationContext),
                null
            )
        }
        return bind.root
    }
}