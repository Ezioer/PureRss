package com.zxq.purerss.listener

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.zxq.purerss.ucdemopage.TestFragment

/**
 * Created by xiaoqing.zhou on 5/6/2016.
 */
class TabLayoutViewPager(
    fragmentManager: FragmentManager,
    private val fragments: ArrayList<TestFragment>,
    private val title: ArrayList<String>
) : FragmentPagerAdapter(fragmentManager) {
    private var mCurrentFragment: Fragment? = null
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        mCurrentFragment = `object` as Fragment
        super.setPrimaryItem(container, position, `object`)
    }

    fun getCurrentFragment(): Fragment {
        return mCurrentFragment!!
    }

    /*  override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
          container.removeView(`object` as View)
      }*/
}
