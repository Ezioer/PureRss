package com.zxq.purerss.ui.detail

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.TextView
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
class DetailFragment : Fragment(), SeekBar.OnSeekBarChangeListener {
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

            tvContent.text = Html.fromHtml(
                mRssItemInfo?.description,
                MImageGetter(tvContent, App.instance!!.applicationContext),
                null
            )
        }

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
    }

    private fun initListener() {
        tv_font_one.setOnClickListener {
            changeFontBg(0)
        }
        tv_font_two.setOnClickListener {
            changeFontBg(1)
        }
        tv_font_three.setOnClickListener {
            changeFontBg(2)
        }
        ll_title.setOnClickListener {
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
        iv_italic.setOnClickListener {
            isItalic = !isItalic
            activity?.putSpValue("italic", if (isItalic) 1 else 0)
            initItalicBG()
        }

        iv_bold.setOnClickListener {
            isBold = !isBold
            activity?.putSpValue("bold", if (isBold) 1 else 0)
            initBoldBG()
        }

        sb_textspac.setOnSeekBarChangeListener(this)
        sb_fontsize.setOnSeekBarChangeListener(this)

        iv_font_reduce.setOnClickListener {
            if (sb_fontsize.progress > 0) {
                val s = sb_fontsize.progress - 1
                activity?.putSpValue("readtextsize", s)
                sb_fontsize.progress = s
                setTextSize(s)
            }
        }
        iv_font_plus.setOnClickListener {
            if (sb_fontsize.progress < 5) {
                val s = sb_fontsize.progress + 1
                activity?.putSpValue("readtextsize", s)
                sb_fontsize.progress = s
                setTextSize(s)
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when {
            seekBar?.id == R.id.sb_fontsize -> setTextSize(seekBar?.progress)
            seekBar?.id == R.id.sb_textspac -> setTextSpac(seekBar?.progress)
        }
    }

    private fun setTextSpac(progress: Int) {
        var textSpac = when (progress) {
            0 -> 2
            1 -> 12
            2 -> 22
            else -> 12
        }
        tv_spac.text = textSpac.toString()
        tv_content.setLineSpacing(textSpac.toFloat(), 1.0f)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        when (seekBar?.id) {
            R.id.sb_fontsize -> activity?.putSpValue("readtextsize", seekBar.progress)
            R.id.sb_textspac -> activity?.putSpValue("readtextspac", seekBar.progress)
        }
    }

    private var bgList: Array<View>? = null
    private var bgColor: Array<Int>? = null
    private var bgReaderColor: Array<Int>? = null
    private var bgNoColor: Array<Int>? = null
    private var bgFont: Array<TextView>? = null
    private var isItalic = false
    private var isBold = false
    private fun initView() {
        tv_font_two.typeface = Typeface.createFromAsset(activity?.assets, "kaiti.ttf")
        tv_font_three.typeface = Typeface.createFromAsset(activity?.assets, "yt.ttf")
        bgFont = arrayOf(tv_font_one, tv_font_two, tv_font_three)
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

        val textSize = activity?.getSpValue("readtextsize", 2) ?: 16
        setTextSize(textSize)
        sb_fontsize.progress = textSize

        isItalic = activity?.getSpValue("italic", 0) == 1
        isBold = activity?.getSpValue("bold", 0) == 1
        initItalicBG()
        initBoldBG()

        setTextSpac(activity?.getSpValue("readtextspac", 1) ?: 1)
        when (activity?.getSpValue("readbg", 0)) {
            0 -> {
                initBg(0)
            }
            1 -> {
                initBg(1)
            }
            2 -> {
                initBg(2)
            }
            3 -> {
                initBg(3)
            }
        }

        for (item in bgList!!.indices) {
            bgList!![item].setOnClickListener {
                bgList!![item].setBackgroundResource(bgColor!![item])
                appbarlayout.setBackgroundResource(bgReaderColor!![item])
                ctl_root.setBackgroundResource(bgReaderColor!![item])
                activity?.putSpValue("readbg", item)
                for (i in bgList!!.indices) {
                    if (i != item) {
                        bgList!![i].setBackgroundResource(bgNoColor!![i])
                    }
                }
            }
        }

        val f = activity?.getSpValue("font", 0) ?: 0
        tv_content.typeface = when (f) {
            0 -> Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            1 -> tv_font_two.typeface
            2 -> tv_font_three.typeface
            else -> Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }
        changeFontBg(f)
    }

    private fun changeFontBg(i: Int) {
        activity?.putSpValue("font", i)
        for (k in bgFont!!.indices) {
            if (k == i) {
                bgFont!![i].setBackgroundResource(R.drawable.bg_tv_selected)
            } else {
                bgFont!![k].setBackgroundResource(R.drawable.bg_tv_unselect)
            }
        }

    }

    private fun setTextSize(size: Int) {
        var textSize = when (size) {
            0 -> 14
            1 -> 18
            2 -> 20
            3 -> 24
            else -> 20
        }

        tv_content.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize.toFloat())
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize.toFloat())
        tv_author.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize.toFloat())

    }

    private fun initItalicBG() {
        iv_italic.setBackgroundResource(if (isItalic) R.drawable.bg_selected else R.drawable.bg_tv_unselect)
        tv_content.setTypeface(null, if (isItalic) Typeface.ITALIC else Typeface.NORMAL)
    }

    private fun initBoldBG() {
        iv_bold.setBackgroundResource(if (isBold) R.drawable.bg_selected else R.drawable.bg_tv_unselect)
        tv_content.setTypeface(null, if (isBold) Typeface.BOLD else Typeface.NORMAL)
    }

    private fun initBg(i: Int) {
        bgList!![i].setBackgroundResource(bgColor!![i])
        ctl_root.setBackgroundResource(bgReaderColor!![i])
        appbarlayout.setBackgroundResource(bgReaderColor!![i])
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