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
                    dialog.setListener(object : NewFolderDialog.AddFolderListener {
                        override fun success(title: String) {
                            mTitle = title
                            mViewModel.newFolder(title)
                        }
                    })
                    dialog.show()
                    dialog?.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                }
                true
            }
            val adapter = ManageFolderAdapter()
            recyclerview.adapter = adapter
            adapter.setDiffCallback(RssFolderDiffCallback())
            recyclerview.itemAnimator = SpringAddItemAnimator()
            mViewModel.folders.observe(this@ManageFolderFragment, Observer {
                adapter.setDiffNewData(it)
            })

            mViewModel.folder.observe(this@ManageFolderFragment, Observer {
                adapter.addData(RSSFolderEntity(it, mTitle))
            })
        }
        return binding.root
    }
}