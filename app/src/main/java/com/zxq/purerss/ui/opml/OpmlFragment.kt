package com.zxq.purerss.ui.opml

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.FilePathInfo
import com.zxq.purerss.databinding.FragmentOpmlBinding
import com.zxq.purerss.listener.OpmlItemDiffCallback
import com.zxq.purerss.utils.InjectorUtil
import com.zxq.purerss.utils.SpringAddItemAnimator
import kotlinx.android.synthetic.main.fragment_opml.*
import java.io.FileDescriptor

/**
 *  created by xiaoqing.zhou
 *  on 2020/7/6
 *  fun
 */
class OpmlFragment : Fragment() {
    private val arg: OpmlFragmentArgs by navArgs()
    private val mViewModel: OpmlViewModel by viewModels {
        InjectorUtil.getOpmlFactory(this)
    }
    private var filePath: FilePathInfo? = null
    private var mAdapter: OpmlAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOpmlBinding.inflate(inflater, container, false).apply {
            filePath = arg.filepath
            toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
            mAdapter = OpmlAdapter()
            recyclerview.itemAnimator = SpringAddItemAnimator()
            recyclerview.adapter = mAdapter
            mAdapter?.setDiffCallback(OpmlItemDiffCallback())
            if (!filePath!!.filepath.isNullOrEmpty()) {
                mViewModel.read(filePath?.filepath ?: "")
            } else {
                mViewModel.read(filePath?.des?.fileDescriptor ?: FileDescriptor())
            }
            mViewModel.opml.observe(this@OpmlFragment, Observer {
                toolbar.title = "共${it.size}条订阅源"
                mAdapter?.setDiffNewData(it)
            })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_importopml -> {
                    import()
                }
                R.id.menu_selectall -> {
                    selectAll()
                }
                R.id.menu_selecctnone -> {
                    selectNone()
                }
            }
            true
        }
    }

    private fun import() {
        mViewModel.insertFeed(mAdapter?.data?.filter { it.state }?.toMutableList())
        Snackbar.make(root, "成功导入", 600).show()
    }

    private fun selectNone() {
        val data = mAdapter!!.data
        for (i in data.indices) {
            data[i].state = false
        }
        mAdapter?.notifyDataSetChanged()
    }

    private fun selectAll() {
        val data = mAdapter!!.data
        for (i in data.indices) {
            data[i].state = true
        }
        mAdapter?.notifyDataSetChanged()
    }
}