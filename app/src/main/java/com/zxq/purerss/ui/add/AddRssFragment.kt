package com.zxq.purerss.ui.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.zxq.purerss.ui.dialog.OpmlNotiDialog
import com.zxq.purerss.ui.feedlist.FeedListAdapter
import com.zxq.purerss.utils.*
import java.util.*


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
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.importopml) {
                    checkPri()
                }
                true
            }
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
                ivParse.setImageResource(R.drawable.search_64px)
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

    private fun addOpml() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("*/*")
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, 1)
    }

    private var mDialog: OpmlNotiDialog? = null
    private fun popNoti() {
        if (!context!!.getSpValue("opmlnoti", false)) {
            if (mDialog == null) {
                mDialog = OpmlNotiDialog(context!!)
            }
            mDialog?.show()
            mDialog?.setOnDismissListener {
                addOpml()
            }
        } else {
            addOpml()
        }
    }

    private fun checkPri() {
        val list = ArrayList<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) !==
                PackageManager.PERMISSION_GRANTED
            ) {
                //申请权限
                list.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (list.size > 0) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    list.toTypedArray(), 11
                )
            } else {
                popNoti()
            }
        } else {
            popNoti()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 11) {
            popNoti()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            var path = ""
            val uri = data?.getData()
            val resolver = activity?.getContentResolver()
            val cursor = resolver?.query(uri!!, null, null, null, null)
            if (cursor == null) {
                path = uri?.path ?: ""
            } else {
                if (cursor!!.moveToFirst()) {
                    // 多媒体文件，从数据库中获取文件的真实路径
                    path = cursor!!.getString(cursor!!.getColumnIndex("_data"))
                }
            }
            if (path.isNullOrEmpty()) {
                return
            }
            val action = AddRssFragmentDirections.actionAddToOpml(path)
            findNavController().navigate(action)

        }

    }
}