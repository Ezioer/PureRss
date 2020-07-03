package com.zxq.purerss.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialSharedAxis
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssItemInfo
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.databinding.DialogAddrssBinding
import com.zxq.purerss.listener.ItemClickListener
import com.zxq.purerss.listener.ItemDiffCallback
import com.zxq.purerss.ui.feedlist.FeedListAdapter
import com.zxq.purerss.utils.*

/**
 *  created by xiaoqing.zhou
 *  on 2020/7/3
 *  fun
 */
class AddRssFragment : Fragment() {
    private val mViewModel: AddRssViewModel by viewModels {
        InjectorUtil.getAddRssFactory(this)
    }
    private var link = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogAddrssBinding.inflate(inflater, null, false).apply {
            lifecycleOwner = this@AddRssFragment
            ivParse.setOnClickListener {
                if (etInput.text.toString().isNotEmpty()) {
                    link = etInput.text.toString()
                    KeyBoardUtil.hideKeyboard(etInput)
                    mViewModel.getFeedsList(etInput.text.toString())
                } else {
                    MotionUtil.startLeftRightAnimation(ivParse)
                }
            }
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            val onClick = object : ItemClickListener {
                override fun onClick(view: View, rss: RssItem) {
                    val extra = FragmentNavigatorExtras(view to "rssdetail")
                    val action = AddRssFragmentDirections.actionAddToDetail(
                        RssItemInfo(
                            rss.title,
                            rss.link,
                            rss.description,
                            rss.pubdate,
                            rss.author,
                            1L,
                            rss.title,
                            rss.albumPic
                        )
                    )
                    findNavController().navigate(action, extra)
                }
            }
            val adapter = FeedListAdapter(onClick, true)
            recyclerview.adapter = adapter
            recyclerview.itemAnimator = SpringAddItemAnimator()
            mViewModel.feedsList.observe(this@AddRssFragment, Observer {
                info = it
                ctlResult.visibility = View.VISIBLE
                ctlResult.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
                val diffResult = DiffUtil.calculateDiff(ItemDiffCallback(adapter.data, it.items))
                adapter.setDiffNewData(diffResult, it.items)
            })
            mViewModel.noThingFound.observe(this@AddRssFragment, Observer {
                if (!it) {
                    ivParse.setImageResource(R.drawable.nothing_found_64px)
                    Snackbar.make(recyclerview, R.string.nothingfound, 2000).show()
                }
            })

            etInput.addOnAfterChange {
                if (it.toString().isNullOrEmpty()) {
                    ctlResult.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_out))
                    ctlResult.visibility = View.GONE
                    ivParse.setImageResource(R.drawable.search_64px)
                }
            }
            ivAdd.setOnClickListener {
                mViewModel.insertRss(RSSFeedEntity(0, info!!.title, link, info!!.subTitle, ""))
            }

            mViewModel.addComplete.observe(this@AddRssFragment, Observer {
                if (it) {
                    Snackbar.make(recyclerview, R.string.addsuccess, 2000).show()
                } else {
                    Snackbar.make(recyclerview, R.string.laterfail, 2000).show()
                }
            })
        }

        val forward = MaterialSharedAxis.create(MaterialSharedAxis.Y, true)
        enterTransition = forward

        val backward = MaterialSharedAxis.create(MaterialSharedAxis.Y, false)
        returnTransition = backward
        return binding.root
    }
}