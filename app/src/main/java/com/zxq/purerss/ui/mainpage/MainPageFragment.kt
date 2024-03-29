package com.zxq.purerss.ui.mainpage

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssFeedInfo
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.data.entity.table.RSSItemEntity
import com.zxq.purerss.databinding.FragmentNewsBinding
import com.zxq.purerss.listener.FolderClickListener
import com.zxq.purerss.listener.RssDiffCallback
import com.zxq.purerss.ui.RssWidget
import com.zxq.purerss.ui.dialog.EditFeedsDialog
import com.zxq.purerss.ui.dialog.FolderDialog
import com.zxq.purerss.ui.dialog.SearchFeedsDialog
import com.zxq.purerss.ui.setting.SettingActivity
import com.zxq.purerss.utils.InjectorUtil
import com.zxq.purerss.utils.SpringAddItemAnimator
import com.zxq.purerss.utils.getSpValue
import com.zxq.purerss.utils.putSpValue
import kotlinx.android.synthetic.main.fragment_news.*
import java.util.concurrent.TimeUnit

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class MainPageFragment : Fragment() {

    private val mainViewModel: MainPageViewModel by viewModels {
        InjectorUtil.getMainFactory(this)
    }
    private var binding: FragmentNewsBinding? = null
    private var showDialog = false
    private var dialogType = 0
    private var typeId = 1L
    private var sortId = 1
    private var rssItem: RSSFeedEntity? = null
    private var folderDialog: FolderDialog? = null
    private var editDialog: EditFeedsDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false).apply {
            if (arguments?.getLong("id") != null && context!!.getSpValue("fromshortcuts", 0) == 1) {
                context!!.putSpValue("fromshortcuts", 0)
                val action = MainPageFragmentDirections.actionMainpageToList(
                    RssFeedInfo(
                        arguments?.getString("title")!!,
                        arguments?.getString("link")!!,
                        arguments?.getString("des")!!,
                        "",
                        arguments?.getLong("id")!!
                    )
                )
                findNavController().navigate(action)
            }
            context!!.putSpValue("isMainPage", 0)
            val onClick = object : MainPageAdapter.FeedClick {
                override fun onClick(view: View, rss: RSSFeedEntity) {
                    mainViewModel.updateFeeds(rss.seeCount + 1, rss.feedId)
                    val action = MainPageFragmentDirections.actionMainpageToList(
                        RssFeedInfo(rss.feedTitle, rss.feedLink, rss.feedDesc, "", rss.feedId)
                    )
                    findNavController().navigate(action)
                }
            }
            toolbar.setNavigationOnClickListener {
                startActivity(Intent(activity, SettingActivity::class.java))
            }

            feedBar.setOnMenuItemClickListener {
                if (it.itemId == R.id.folder) {
                    dialogType = 1
                    showDialog = true
                    mainViewModel.getFolder()
                } else if (it.itemId == R.id.sort_addtime) {
                    sortId = 1
                    context?.putSpValue("sort", 1)
                    sortFeeds()
                } else {
                    sortId = 2
                    context?.putSpValue("sort", 2)
                    sortFeeds()
                }
                true
            }
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.addfeed) {
                    findNavController().navigate(R.id.action_mainpage_to_circle)
                }
                true
            }

            if (context?.getSpValue("initSource", 0) == 0) {
                mainViewModel.insertSource()
                context?.putSpValue("initSource", 1)
            }

            tvSearch.setOnClickListener {
                popSearchDialog()
            }
            sortId = context?.getSpValue("sort", 1) ?: 1
            mainViewModel.getFeedsList(typeId, sortId)
            val adapter = MainPageAdapter(onClick, context?.getSpValue("slide", 0) == 0)
            adapter.setOnDeleteListener(object : MainPageAdapter.OnDeleteListener {
                override fun delete(item: RSSFeedEntity) {
                    adapter.data.remove(item)
                    mainViewModel.deleteItem(item)
                }
            })
            adapter.setOnEditListener(object : MainPageAdapter.OnEditListener {
                override fun edit(item: RSSFeedEntity) {
                    showDialog = true
                    dialogType = 2
                    rssItem = item
                    mainViewModel.getFolder()
                }
            })
            recyclerview.adapter = adapter
            recyclerview.itemAnimator = SpringAddItemAnimator()
            adapter.setDiffCallback(RssDiffCallback())
            mainViewModel.feedsList.observe(this@MainPageFragment, Observer {
                adapter.setDiffNewData(it)
            })
            val onFolderClick = object : FolderClickListener {
                override fun onClick(view: View, rss: RSSFolderEntity) {
                    folderDialog?.dismiss()
                    feedBar.title = rss.folderTitle
                    typeId = rss.folderId
                    mainViewModel.getFeedsList(rss.folderId, sortId)
                }
            }

            feedBar.setOnClickListener {
                recyclerview.smoothScrollToPosition(0)
            }

            mainViewModel.folders.observe(this@MainPageFragment, Observer {
                if (dialogType == 1) {
                    if (showDialog) {
                        showDialog = false
                        folderDialog =
                            FolderDialog(context!!, it, onFolderClick, findNavController())
                        folderDialog?.show()
                    }
                } else if (dialogType == 2) {
                    if (showDialog) {
                        showDialog = false
                        editDialog = EditFeedsDialog(
                            context!!, it, rssItem!!, findNavController(), mainViewModel
                        )
                        editDialog?.show()
                    }
                }
            })
            mainViewModel.saveComplete.observe(this@MainPageFragment, Observer {
                if (it == 1) {
                    mainViewModel.getFeedsList(typeId, sortId)
                }
            })
        }
        postponeEnterTransition(10L, TimeUnit.MILLISECONDS)
      /*  val backward = MaterialSharedAxis.create(MaterialSharedAxis.Y, false)
        reenterTransition = backward

        val forward = MaterialSharedAxis.create(MaterialSharedAxis.Y, true)
        exitTransition = forward*/
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun sortFeeds() {
        mainViewModel.getFeedsList(typeId, sortId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        tv_readed.setOnClickListener { onTypeClick(1) }
        iv_readedmore.setOnClickListener { onTypeClick(1) }
        tv_collect.setOnClickListener { onTypeClick(2) }
        iv_collectmore.setOnClickListener { onTypeClick(2) }
        tv_laterread.setOnClickListener { onTypeClick(3) }
        iv_laterreadmore.setOnClickListener { onTypeClick(3) }
    }

    private var mSearchDialog: SearchFeedsDialog? = null
    private fun popSearchDialog() {
        if (mSearchDialog == null) {
            mSearchDialog = SearchFeedsDialog(context!!, mainViewModel, this@MainPageFragment)
        }
        mSearchDialog?.show()
        mSearchDialog?.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    private fun onTypeClick(i: Int) {
        val action = MainPageFragmentDirections.actionMainpageToType(i)
        findNavController().navigate(action)
    }

    private fun updateWidget(mList: MutableList<RSSItemEntity>) {
        /* val intent = Intent("action_update_ui")
         val index = if (RssWidget.WIDGET_INDEX > mList.size) 0 else mList.size
         intent.putExtra(RssWidget.WIDGET_TITLE, mList[index].itemTitle)
         intent.putExtra(RssWidget.WIDGET_PIC, mList[index].itemPic)
         intent.putExtra(RssWidget.WIDGET_DATE, mList[index].itemDate)
         intent.putExtra(RssWidget.WIDGET_FEED, mList[index].feedTitle)
         intent.component = ComponentName(context!!, RssWidget::class.java)
         context?.sendBroadcast(intent)*/
    }
}