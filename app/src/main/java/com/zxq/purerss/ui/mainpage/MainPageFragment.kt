package com.zxq.purerss.ui.mainpage

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
import com.zxq.purerss.databinding.FragmentNewsBinding
import com.zxq.purerss.listener.RssDiffCallback
import com.zxq.purerss.ui.dialog.SearchFeedsDialog
import com.zxq.purerss.ui.setting.SettingActivity
import com.zxq.purerss.utils.InjectorUtil
import com.zxq.purerss.utils.SpringAddItemAnimator
import com.zxq.purerss.utils.getSpValue
import kotlinx.android.synthetic.main.fragment_news.*
import java.util.concurrent.TimeUnit

/**
 *  created by xiaoqing.zhou
 *  on 2020/6/28
 *  fun
 */
class MainPageFragment: Fragment() {

    private val mainViewModel: MainPageViewModel by viewModels {
        InjectorUtil.getMainFactory(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNewsBinding.inflate(inflater,container,false).apply {
            val onClick = object: MainPageAdapter.FeedClick{
                override fun onClick(view: View, rss: RSSFeedEntity) {
                    val action = MainPageFragmentDirections.actionMainpageToList(RssFeedInfo(rss.feedTitle,rss.feedLink,rss.feedDesc,"",rss.feedId))
                    findNavController().navigate(action)
                }
            }
            toolbar.setNavigationOnClickListener { startActivity(Intent(activity,SettingActivity::class.java)) }
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.addfeed) {
                    findNavController().navigate(R.id.action_mainpage_to_add)
                }
                true
            }
            tvSearch.setOnClickListener {
                popSearchDialog()
            }
            mainViewModel.getFeedsList()
            val adapter = MainPageAdapter(onClick, context?.getSpValue("slide", 0) == 0)
            adapter.setOnDeleteListener(object : MainPageAdapter.OnDeleteListener {
                override fun delete(item: RSSFeedEntity) {
                    adapter.data.remove(item)
                    mainViewModel.deleteItem(item)
                }
            })
            recyclerview.adapter = adapter
            recyclerview.itemAnimator = SpringAddItemAnimator()
            adapter.setDiffCallback(RssDiffCallback())
            mainViewModel.feedsList.observe(this@MainPageFragment, Observer {
                adapter.setDiffNewData(it)
            })
        }
        postponeEnterTransition(10L, TimeUnit.MILLISECONDS)
        val backward = MaterialSharedAxis.create(MaterialSharedAxis.Y, false)
        reenterTransition = backward

        val forward = MaterialSharedAxis.create(MaterialSharedAxis.Y, true)
        exitTransition = forward
        return binding.root
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
}