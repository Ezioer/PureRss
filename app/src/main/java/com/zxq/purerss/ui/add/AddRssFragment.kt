package com.zxq.purerss.ui.add

import android.Manifest
import android.annotation.SuppressLint
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
import com.zxq.purerss.data.entity.FilePathInfo
import com.zxq.purerss.data.entity.RssItem
import com.zxq.purerss.data.entity.RssItemInfo
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.data.entity.table.RSSSourceEntity
import com.zxq.purerss.databinding.DialogAddrssBinding
import com.zxq.purerss.listener.AddFeedClickListener
import com.zxq.purerss.listener.ItemDiffCallback
import com.zxq.purerss.listener.ItemSearchClickListener
import com.zxq.purerss.listener.RssSourceDiffCallback
import com.zxq.purerss.ui.dialog.OpmlNotiDialog
import com.zxq.purerss.utils.*
import java.util.*


/**
 *  created by xiaoqing.zhou
 *  on 2020/7/3
 *  fun
 */
class AddRssFragment : Fragment(), ActivityCompat.OnRequestPermissionsResultCallback {
    private val mViewModel: AddRssViewModel by viewModels {
        InjectorUtil.getAddRssFactory(this)
    }
    private var link = ""
    private var binding: DialogAddrssBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddrssBinding.inflate(inflater, null, false).apply {
            lifecycleOwner = viewLifecycleOwner
            ivParse.setOnClickListener {
                val text = etInput.text.toString()
                if (text.isNotEmpty()) {
                    if (text.startsWith("http") || text.startsWith("https")) {
                        link = etInput.text.toString()
                        KeyBoardUtil.hideKeyboard(etInput)
                        pbLoad.visibility = View.VISIBLE
                        mViewModel.getFeedsList(etInput.text.toString())
                    } else {
                        mViewModel.searchSource(text)
                    }
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
            val onClick = object : ItemSearchClickListener {
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

            val addClick = object : AddFeedClickListener {
                override fun onClick(view: View, rss: RSSSourceEntity) {
                    mViewModel.insertRss(
                        RSSFeedEntity(
                            0,
                            rss.feedTitle,
                            rss.feedLink,
                            "",
                            "",
                            0,
                            0
                        )
                    )
                }
            }
            val sourceAdapter = SearchSourceListAdapter(addClick)
            rvSource.itemAnimator = SpringAddItemAnimator()
            rvSource.adapter = sourceAdapter
            sourceAdapter.setDiffCallback(RssSourceDiffCallback())
            rvSource.itemAnimator = SpringAddItemAnimator()
            mViewModel.sources.observe(viewLifecycleOwner, Observer {
                rvSource.visibility = View.VISIBLE
                ctlResult.visibility = View.GONE
                sourceAdapter.setDiffNewData(it)
            })

            val adapter = SearchListAdapter(onClick, true)
            recyclerview.adapter = adapter
            recyclerview.itemAnimator = SpringAddItemAnimator()
            mViewModel.feedsList.observe(viewLifecycleOwner, Observer {
                info = it
                pbLoad.visibility = View.GONE
                rvSource.visibility = View.GONE
                ctlResult.visibility = View.VISIBLE
                ctlResult.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
                ivParse.setImageResource(R.drawable.search_64px)
                val diffResult = DiffUtil.calculateDiff(ItemDiffCallback(adapter.data, it.items))
                adapter.setDiffNewData(diffResult, it.items)
            })

            mViewModel.noThingFound.observe(viewLifecycleOwner, Observer {
                if (!it) {
                    pbLoad.visibility = View.GONE
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
                mViewModel.insertRss(
                    RSSFeedEntity(
                        0,
                        info!!.title,
                        link,
                        info!!.subTitle,
                        "",
                        0,
                        0
                    )
                )
            }

            mViewModel.addComplete.observe(viewLifecycleOwner, Observer {
                if (it) {
                    Snackbar.make(recyclerview, R.string.addsuccess, 2000).show()
                } else {
                    Snackbar.make(recyclerview, R.string.laterfail, 2000).show()
                }
            })
        }

  /*      val forward = MaterialSharedAxis.Y(MaterialSharedAxis.Y, true)
        enterTransition = forward

        val backward = MaterialSharedAxis.Y(MaterialSharedAxis.Y, false)
        returnTransition = backward*/
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) !==
                PackageManager.PERMISSION_GRANTED
            ) {
                //申请权限
                list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (list.size > 0) {
                requestPermissions(list.toTypedArray(), 11)
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

    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            var path = ""
            val uri = data?.getData()
            val resolver = activity?.getContentResolver()
            if (Build.VERSION.SDK_INT >= 29) {
                val fileDescriptor =
                    resolver?.openFileDescriptor(uri!!, "r") ?: return
                val action =
                    AddRssFragmentDirections.actionAddToOpml(FilePathInfo("", fileDescriptor))
                findNavController().navigate(action)
            } else {
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
                val action = AddRssFragmentDirections.actionAddToOpml(FilePathInfo(path, null))
                findNavController().navigate(action)
            }


        }
    }
}