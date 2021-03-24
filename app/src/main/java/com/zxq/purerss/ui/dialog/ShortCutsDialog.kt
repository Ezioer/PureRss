package com.zxq.purerss.ui.dialog

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.chad.library.adapter.base.listener.OnItemSwipeListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import com.zxq.purerss.listener.ShortCutsDiffCallback
import com.zxq.purerss.ui.MainActivity
import kotlinx.android.synthetic.main.dialog_appshortcuts.*
import kotlinx.android.synthetic.main.dialog_folder.tv_ok
import java.util.*


class ShortCutsDialog(
    private val mContext: Context,
    private val feedList: MutableList<RSSFeedEntity>
) : BaseDialog(mContext, Gravity.CENTER, R.style.anim_fade2fade, true, 0.8, 0.0) {
    private lateinit var mView: View
    private lateinit var mManager: ShortcutManager
    private lateinit var mList: MutableList<ShortcutInfo>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_appshortcuts, null)
        setContentView(mView)
        mManager = mContext.getSystemService(ShortcutManager::class.java)
        mList = mManager.dynamicShortcuts
        initView()
    }

    private fun initView() {
        tv_ok.setOnClickListener {
            mManager.setDynamicShortcuts(mList)
            dismiss()
        }
        val adapter = ShortcutsAdapter()
        val onDragListener = object : OnItemDragListener {
            override fun onItemDragMoving(
                source: RecyclerView.ViewHolder?,
                from: Int,
                target: RecyclerView.ViewHolder?,
                to: Int
            ) {
                val tempFrom = mList[from]
                val tempTo = mList[to]
                val fromInfo = ShortcutInfo.Builder(mContext, "ID${from}")
                    .setShortLabel(tempFrom.shortLabel!!)
                    .setLongLabel(tempFrom.longLabel!!)
                    .setIcon(Icon.createWithResource(mContext, R.mipmap.ic_launcher))
                    .setIntent(tempFrom.intent!!)
                    .setRank(tempTo.rank)
                    .build()

                val toInfo = ShortcutInfo.Builder(mContext, "ID${to}")
                    .setShortLabel(tempTo.shortLabel!!)
                    .setLongLabel(tempTo.longLabel!!)
                    .setIcon(Icon.createWithResource(mContext, R.mipmap.ic_launcher))
                    .setIntent(tempTo.intent!!)
                    .setRank(tempFrom.rank)
                    .build()
                mManager.updateShortcuts(arrayListOf(fromInfo, toInfo))
            }

            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                val holder = viewHolder as BaseViewHolder
                // 开始时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                // 开始时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                val startColor: Int = Color.WHITE
                val endColor: Int = Color.rgb(245, 245, 245)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val v = ValueAnimator.ofArgb(startColor, endColor)
                    v.addUpdateListener { animation -> holder.itemView.setBackgroundColor(animation.animatedValue as Int) }
                    v.duration = 300
                    v.start()
                }
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                val holder = viewHolder as BaseViewHolder
                // 结束时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                // 结束时，item背景色变化，demo这里使用了一个动画渐变，使得自然
                val startColor = Color.rgb(245, 245, 245)
                val endColor = Color.WHITE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val v = ValueAnimator.ofArgb(startColor, endColor)
                    v.addUpdateListener { animation -> holder.itemView.setBackgroundColor(animation.animatedValue as Int) }
                    v.duration = 300
                    v.start()
                }
            }

        }
        val onSwipeListener = object : OnItemSwipeListener {
            override fun clearView(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
            }

            override fun onItemSwiped(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                mManager.removeDynamicShortcuts(Arrays.asList("ID${pos}"))
            }

            override fun onItemSwipeStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
            }

            override fun onItemSwipeMoving(
                canvas: Canvas?,
                viewHolder: RecyclerView.ViewHolder?,
                dX: Float,
                dY: Float,
                isCurrentlyActive: Boolean
            ) {
            }

        }

        adapter.draggableModule.isSwipeEnabled = true
        adapter.draggableModule.isDragEnabled = true
        adapter.draggableModule.setOnItemSwipeListener(onSwipeListener)
        adapter.draggableModule.setOnItemDragListener(onDragListener)
        adapter.draggableModule.itemTouchHelperCallback.setSwipeMoveFlags(ItemTouchHelper.START or ItemTouchHelper.END)
        adapter.draggableModule.itemTouchHelperCallback.setDragMoveFlags(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP or ItemTouchHelper.DOWN)
        tv_addcuts.setOnClickListener {
            val selectDialog = SelectShortCutsDialog(mContext, mList, feedList)
            selectDialog.setListener {
                mList.clear()
                for (item in it) {
                    val intent = Intent(mContext, MainActivity::class.java)
                    intent.putExtra("feedid", item.feedId)
                    intent.putExtra("link", item.feedLink)
                    intent.putExtra("title", item.feedTitle)
                    intent.putExtra("des", item.feedDesc)
                    intent.setAction("android.intent.action.shortcuts")
                    mList.add(
                        ShortcutInfo.Builder(mContext, "ID${it.indexOf(item)}")
                            .setLongLabel(item.feedTitle)
                            .setShortLabel(item.feedTitle)
                                .setIntent(intent)
                                .setIcon(Icon.createWithResource(mContext, R.mipmap.ic_launcher))
                                .build()
                        )
                    }
                    adapter.notifyDataSetChanged()
                }

            selectDialog.show()
        }

        rv_shortcuts.adapter = adapter
        adapter.setDiffCallback(ShortCutsDiffCallback())
        adapter.setDiffNewData(mList)
    }
}