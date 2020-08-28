package com.zxq.purerss.ui.managefolder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFolderEntity
import com.zxq.purerss.databinding.FragmentManagefolderBinding
import com.zxq.purerss.listener.RssFolderDiffCallback
import com.zxq.purerss.ui.dialog.NewFolderDialog
import com.zxq.purerss.utils.InjectorUtil
import com.zxq.purerss.utils.SpringAddItemAnimator

/**
 *  created by xiaoqing.zhou
 *  on 2020/7/14
 *  fun
 */
class ManageFolderFragment : Fragment() {
    private val mViewModel: ManageFolderViewModel by viewModels {
        InjectorUtil.getFolderFactory(this)
    }
    private var mTitle = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentManagefolderBinding.inflate(inflater, container, false).apply {
            mViewModel.getAllFolder()
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.menu_newfolder) {
                    val dialog = NewFolderDialog(context!!)
                    dialog.setListener {
                        mTitle = it
                        mViewModel.newFolder(it)
                    }
                    dialog.show()
                    dialog?.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                }
                true
            }
            val mAdapter = ManageFolderAdapter()
            recyclerview.adapter = mAdapter
            mAdapter.setOnItemChildClickListener { adapter, view, position ->
                if (view.id == R.id.iv_edit) {
                    val dialog = NewFolderDialog(context!!)
                    dialog.setListener(object : NewFolderDialog.AddFolderListener {
                        override fun success(title: String) {
                            mViewModel.updateFolder(mAdapter.data[position].folderId, title)
                            mAdapter.data[position].folderTitle = title
                            mAdapter.notifyItemChanged(position)
                        }
                    })
                    dialog.show()
                } else {
                    val data = mAdapter.data[position]
                    mAdapter.data.remove(data)
                    mAdapter.notifyItemRemoved(position)
                    mViewModel.deleteFolder(data.folderId)
                }
            }
            mAdapter.setDiffCallback(RssFolderDiffCallback())
            recyclerview.itemAnimator = SpringAddItemAnimator()
            mViewModel.folders.observe(this@ManageFolderFragment, Observer {
                mAdapter.setDiffNewData(it)
            })

            mViewModel.folder.observe(this@ManageFolderFragment, Observer {
                mAdapter.addData(RSSFolderEntity(it, mTitle))
            })
        }
        return binding.root
    }
}