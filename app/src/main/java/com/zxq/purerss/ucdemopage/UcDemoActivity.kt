package com.zxq.purerss.ucdemopage

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.zxq.purerss.R
import com.zxq.purerss.listener.TabLayoutViewPager
import kotlinx.android.synthetic.main.layout_ucnewsdemo.*

/**
 *  created by xiaoqing.zhou
 *  on 2021/10/14
 *  fun
 */
class UcDemoActivity : AppCompatActivity(), UcNewsHeaderPagerBehavior.OnPagerStateListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_ucnewsdemo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }
        var fragments = arrayListOf<TestFragment>()
        var titles = arrayListOf<String>()
        for (i in 0 until 4) {
            fragments.add(TestFragment.newInstance())
            titles.add("tab${i}")
        }
        var mPagerBehavior =
            (id_uc_news_header_pager.layoutParams as CoordinatorLayout.LayoutParams).behavior as UcNewsHeaderPagerBehavior
        mPagerBehavior.setPagerStateListener(this)
        id_uc_news_content.adapter = TabLayoutViewPager(supportFragmentManager, fragments, titles)
        id_uc_news_tab.setupWithViewPager(id_uc_news_content)
    }

    override fun onPagerClosed() {

    }

    override fun onPagerOpened() {

    }
}