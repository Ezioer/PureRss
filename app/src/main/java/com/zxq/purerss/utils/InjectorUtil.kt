package com.zxq.purerss.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.zxq.purerss.data.DataBase
import com.zxq.purerss.data.FolderRepository
import com.zxq.purerss.data.RssFeedRepository
import com.zxq.purerss.data.entity.SourceRepository
import com.zxq.purerss.ui.add.AddRssModelFactory
import com.zxq.purerss.ui.chooserss.SaveDBRssModelFactory
import com.zxq.purerss.ui.detail.DetailModelFactory
import com.zxq.purerss.ui.feedlist.FeedListModelFactory
import com.zxq.purerss.ui.mainpage.MainPageModelFactory
import com.zxq.purerss.ui.managefolder.ManageFolderModelFactory
import com.zxq.purerss.ui.opml.OpmlModelFactory
import com.zxq.purerss.ui.setting.SettingModelFactory
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
            ).feedDao(),
            DataBase.getInstance(fragment.requireContext()).itemDao(),
            DataBase.getInstance(fragment.requireContext()).folderDao()
        )
    )

    fun getMainFactory(fragment: Fragment) = MainPageModelFactory(
        RssFeedRepository.getInstance(
            DataBase.getInstance(
                fragment.requireContext()
            ).feedDao(),
            DataBase.getInstance(fragment.requireContext()).itemDao(),
            DataBase.getInstance(fragment.requireContext()).folderDao()
        ), SourceRepository.getInstance(DataBase.getInstance(fragment.requireContext()).sourceDao())
    )

    fun getFeedsListFactory(fragment: Fragment) = FeedListModelFactory(
        RssFeedRepository.getInstance(
            DataBase.getInstance(
                fragment.requireContext()
            ).feedDao(),
            DataBase.getInstance(fragment.requireContext()).itemDao(),
            DataBase.getInstance(fragment.requireContext()).folderDao()
        )
    )

    fun getAddRssFactory(fragment: Fragment) = AddRssModelFactory(
        RssFeedRepository.getInstance(
            DataBase.getInstance(
                fragment.requireContext()
            ).feedDao(),
            DataBase.getInstance(fragment.requireContext()).itemDao(),
            DataBase.getInstance(fragment.requireContext()).folderDao()
        ), SourceRepository.getInstance(DataBase.getInstance(fragment.requireContext()).sourceDao())
    )

    fun getDetailFactory(fragment: Fragment) = DetailModelFactory(
        RssFeedRepository.getInstance(
            DataBase.getInstance(
                fragment.requireContext()
            ).feedDao(),
            DataBase.getInstance(fragment.requireContext()).itemDao(),
            DataBase.getInstance(fragment.requireContext()).folderDao()
        )
    )

    fun getTypeFactory(fragment: Fragment) = TypeModelFactory(
        RssFeedRepository.getInstance(
            DataBase.getInstance(
                fragment.requireContext()
            ).feedDao(),
            DataBase.getInstance(fragment.requireContext()).itemDao(),
            DataBase.getInstance(fragment.requireContext()).folderDao()
        )
    )

    fun getOpmlFactory(fragment: Fragment) = OpmlModelFactory(
        RssFeedRepository.getInstance(
            DataBase.getInstance(
                fragment.requireContext()
            ).feedDao(),
            DataBase.getInstance(fragment.requireContext()).itemDao(),
            DataBase.getInstance(fragment.requireContext()).folderDao()
        )
    )

    fun getSettingFactory(activity: FragmentActivity) = SettingModelFactory(
        RssFeedRepository.getInstance(
            DataBase.getInstance(activity).feedDao(),
            DataBase.getInstance(activity).itemDao(),
            DataBase.getInstance(activity).folderDao()
        )
    )

    fun getFolderFactory(activity: Fragment) = ManageFolderModelFactory(
        FolderRepository.getInstance(
            DataBase.getInstance(activity.requireContext()).folderDao()
        )
    )
}