package com.zxq.purerss.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.zxq.purerss.data.DataBase
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.ui.bottomdrawer.AllFeedFactory
import com.zxq.purerss.ui.chooserss.SaveDBRssModelFactory
import com.zxq.purerss.ui.feedlist.FeedListModelFactory
import com.zxq.purerss.ui.home.FeedContentListFactory
import com.zxq.purerss.ui.mainpage.MainPageModelFactory
import com.zxq.purerss.ui.type.TypeModelFactory

/**
 * Created by xiaoqing.zhou
 * on  2019/5/7
 */
object InjectorUtil {

    fun getRssFeedFactory(fragment: Fragment) = SaveDBRssModelFactory(
        RssFeedRepository.getInstance(
            DataBase.getInstance(
                fragment.requireContext()
            ).feedDao(),DataBase.getInstance(fragment.requireContext()).itemDao()
        )
    )


    fun getAllFeedFactory(fragment: Fragment) = AllFeedFactory(
        RssFeedRepository.getInstance(
            DataBase.getInstance(
                fragment.requireContext()
            ).feedDao(),DataBase.getInstance(fragment.requireContext()).itemDao()
        )
    )

    fun getFeedListFactory(fragment: Fragment) = FeedContentListFactory(
        RssFeedRepository.getInstance(
            DataBase.getInstance(
                fragment.requireContext()
            ).feedDao(),DataBase.getInstance(fragment.requireContext()).itemDao()
        )
    )

    fun getMainFactory(fragment: Fragment) = MainPageModelFactory(
        RssFeedRepository.getInstance(
            DataBase.getInstance(fragment.requireContext()
            ).feedDao(),DataBase.getInstance(fragment.requireContext()).itemDao()
        )
    )

    fun getFeedsListFactory(fragment: Fragment) = FeedListModelFactory(
        RssFeedRepository.getInstance(
            DataBase.getInstance(fragment.requireContext()
            ).feedDao(),DataBase.getInstance(fragment.requireContext()).itemDao()
        )
    )

    fun getTypeFactory(fragment: Fragment) = TypeModelFactory(
        RssFeedRepository.getInstance(
            DataBase.getInstance(fragment.requireContext()
            ).feedDao(),DataBase.getInstance(fragment.requireContext()).itemDao()
        )
    )
}