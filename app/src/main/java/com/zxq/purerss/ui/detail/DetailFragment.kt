package com.zxq.purerss.ui.detail

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zxq.purerss.App
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssItemInfo
import com.zxq.purerss.databinding.FragmentDetailBinding
import com.zxq.purerss.utils.MImageGetter
import com.zxq.purerss.utils.getSpValue
import com.zxq.purerss.utils.putSpValue
import kotlinx.android.synthetic.main.dialog_readsetting.*
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/18
 *  fun
 */
class DetailFragment : Fragment() {
    private val arg: DetailFragmentArgs by navArgs()
    private var mRssItemInfo: RssItemInfo? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bind = FragmentDetailBinding.inflate(inflater, container, false).apply {
            mRssItemInfo = arg.itemcontent
            lifecycleOwner = this@DetailFragment
            info = mRssItemInfo
            bottomAppBar.setNavigationOnClickListener { findNavController().navigateUp() }
            bottomAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_readsetting -> {
                        popReadSettinggDialog()
                    }
                    R.id.menu_share -> {
                        share()
                    }
                }
                true
            }
            initListener()
            initView()
            tvContent.text = Html.fromHtml(
                mRssItemInfo?.description,
                MImageGetter(tvContent, App.instance!!.applicationContext),
                null
            )
        }
        return bind.root
    }

    private fun initListener() {
        coordinator_layout.setOnClickListener {
            if (include_readsetting.visibility == View.VISIBLE) {
                include_readsetting.visibility = View.GONE
                include_readsetting.startAnimation(
                    AnimationUtils.loadAnimation(
                        context,
                        R.anim.dialog_exit
                    )
                )
            }
        }
    }

    private var bgList: Array<View>? = null
    private var bgColor: Array<Int>? = null
    private var bgReaderColor: Array<Int>? = null
    private var bgNoColor: Array<Int>? = null
    private fun initView() {
        bgList = arrayOf(view_c_one, view_c_three, view_c_four, view_c_six)
        bgColor = arrayOf(
            R.drawable.bg_r_one, R.drawable.bg_r_three, R.drawable.bg_r_four, R.drawable.bg_r_six
        )
        bgReaderColor = arrayOf(
            R.color.c_r_one, R.color.c_r_three, R.color.c_r_four, R.color.c_r_six
        )
        bgNoColor = arrayOf(
            R.drawable.bg_r_one_n,
            R.drawable.bg_r_three_n,
            R.drawable.bg_r_four_n,
            R.drawable.bg_r_six_n
        )
        when (activity?.getSpValue("readbg", 1)) {
            1 -> {
                initBg(1)
            }
            2 -> {
                initBg(2)
            }
            3 -> {
                initBg(3)
            }
            4 -> {
                initBg(4)
            }
        }

        for (item in bgList!!.indices) {
            bgList!![item].setOnClickListener {
                bgList!![item].setBackgroundResource(bgColor!![item])
                coordinator_layout.setBackgroundResource(bgReaderColor!![item])
                activity?.putSpValue("readbg", item)
                for (i in bgList!!.indices) {
                    if (i != item) {
                        bgList!![i].setBackgroundResource(bgNoColor!![i])
                    }
                }
            }
        }
    }

    private fun initBg(i: Int) {
        bgList!![i].setBackgroundResource(bgColor!![i])
        coordinator_layout.setBackgroundResource(bgReaderColor!![i])
    }

    private fun share() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, mRssItemInfo?.link)
        intent.setType("text/plain")
        activity?.startActivity(Intent.createChooser(intent, "share to......"))
    }


    private fun popReadSettinggDialog() {
        include_readsetting.visibility = View.VISIBLE
        include_readsetting.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.dialog_enter
            )
        )
    }
}