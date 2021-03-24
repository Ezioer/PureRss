package com.zxq.purerss.ui.friends

import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.Hold
import com.ypx.imagepicker.ImagePicker
import com.ypx.imagepicker.bean.MimeType
import com.ypx.imagepicker.bean.SelectMode
import com.ypx.imagepicker.data.OnImagePickCompleteListener
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.StatusInfo
import com.zxq.purerss.databinding.FragmentFriendscircleBinding
import com.zxq.purerss.listener.StatusItemDiffCallback
import com.zxq.purerss.utils.InjectorUtil
import com.zxq.purerss.utils.SpringAddItemAnimator
import com.zxq.purerss.utils.getSpValue
import com.zxq.purerss.utils.putSpValue
import kotlinx.android.synthetic.main.fragment_friendscircle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FriendsCircleFragment : Fragment() {

    private val viewM: FriendsCircleViewModel by viewModels {
        InjectorUtil.getFriendsFactory(this)
    }
    private var item: StatusInfo? = null
    private var binding: FragmentFriendscircleBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendscircleBinding.inflate(inflater, container, false).apply {
            fabAdd.setOnClickListener {
                val extras = FragmentNavigatorExtras(it to "intoadd")
                findNavController().navigate(R.id.action_list_to_add, null, null, extras)
            }
            lifecycleOwner = this@FriendsCircleFragment
            MainScope().launch(Dispatchers.IO) {
                withContext(Dispatchers.IO) {
                    ivTest.setImageResource(R.drawable.men)
                }
            }
            viewM.getAllStatus()
            val mAdapter = StatusListAdapter()
            // 侧滑监听
            val onItemSwipeListener: OnItemSwipeListener = object : OnItemSwipeListener {
                override fun onItemSwipeStart(
                    viewHolder: RecyclerView.ViewHolder,
                    pos: Int
                ) {
                    Log.d("slidedelete", "view swiped start: $pos")
                    val holder = viewHolder as BaseViewHolder
                    item = mAdapter.data[pos]
                }

                override fun clearView(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                    Log.d("slidedelete", "View reset: $pos")
                    val holder = viewHolder as BaseViewHolder
                }

                override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder, pos: Int) {
                    Log.d("slidedelete", "View Swiped: $pos")

                    viewM.deleteOneStatus(item!!)
                    Snackbar.make(rvEvent, "删除成功", 2000).setAction("撤销删除?") {
                        mAdapter.data.add(pos, item!!)
                        viewM.insertOneStatus(item!!)
                        mAdapter.notifyItemInserted(pos)
                        rvEvent.smoothScrollToPosition(pos)
                    }.show()
                }

                override fun onItemSwipeMoving(
                    canvas: Canvas,
                    viewHolder: RecyclerView.ViewHolder?,
                    dX: Float,
                    dY: Float,
                    isCurrentlyActive: Boolean
                ) {
                    canvas.drawColor(
                        ContextCompat.getColor(
                            context!!,
                            R.color.c_008f68
                        )
                    )
                }
            }

            mAdapter.draggableModule.isSwipeEnabled = true
            mAdapter.draggableModule.setOnItemSwipeListener(onItemSwipeListener)
            mAdapter.draggableModule.itemTouchHelperCallback.setSwipeMoveFlags(ItemTouchHelper.START or ItemTouchHelper.END)
            rvEvent.adapter = mAdapter
            rvEvent.itemAnimator = SpringAddItemAnimator()
            viewM.statusList.observe(this@FriendsCircleFragment, Observer {
                val diffResult =
                    DiffUtil.calculateDiff(StatusItemDiffCallback(mAdapter.data, it), false)
                mAdapter.setDiffNewData(diffResult, it)
                rvEvent.smoothScrollToPosition(0)

            })
            Glide.with(context!!).load(Uri.parse(context!!.getSpValue("avatar", "")))
                .apply(RequestOptions().error(R.drawable.men)).into(ivAvatar);
            Glide.with(context!!).load(Uri.parse(context!!.getSpValue("avatar", "")))
                .apply(RequestOptions().error(R.drawable.men)).into(ivAvatar1);
            Glide.with(context!!).load(Uri.parse(context!!.getSpValue("bg", "")))
                .apply(RequestOptions().error(R.drawable.spring)).into(ivBg);
            ivAvatar.setOnLongClickListener {
                changePic(1)
                true
            }
            ivBg.setOnClickListener {
                changePic(0)
            }
        }
        exitTransition = Hold()
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun changePic(type: Int) {
        ImagePicker.withMulti(WeChatPresenter()) //指定presenter
            .setMaxCount(1)  //设置要加载的文件类型，可指定单一类型
            .mimeTypes(MimeType.ofAll()) //设置需要过滤掉加载的文件类型
            .filterMimeTypes(MimeType.ofVideo())
            .showCamera(true) //显示拍照
            .setPreview(true)
            .setOriginal(true) //显示原图
            //设置单选模，当maxCount==1时，可执行单选（下次选中会取消上一次选中）
            .setSelectMode(SelectMode.MODE_SINGLE)
            .setMinVideoDuration(60000L) //设置上一次操作的图片列表，下次选择时默认恢复上一次选择的状态
            .pick(activity, OnImagePickCompleteListener {
                //图片选择回调，主线程
                if (type == 1) {
                    Glide.with(iv_avatar).load(it[0].uri).into(iv_avatar);
                    context!!.putSpValue("avatar", it[0].uri.toString())
                } else {
                    Glide.with(iv_bg).load(it[0].uri).into(iv_bg);
                    context!!.putSpValue("bg", it[0].uri.toString())
                }
            })
    }
}