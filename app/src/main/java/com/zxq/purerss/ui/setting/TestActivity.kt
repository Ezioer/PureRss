package com.zxq.purerss.ui.setting

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zxq.purerss.R
import com.zxq.purerss.widget.PieLayoutX
import com.zxq.purerss.widget.PileLayout

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
//        val shootView = findViewById<ShootView>(R.id.shootview)
        val clockView = findViewById<PileLayout>(R.id.rulerview)
        clockView.setAdapter(object : PileLayout.Adapter() {
            override fun getLayoutId(): Int {
                return R.layout.item_pielayoutx
            }

            override fun getItemCount(): Int {
                return 12
            }

            override fun bindView(view: View, index: Int) {
                var viewHolder = view?.tag
                if (viewHolder == null) {
                    viewHolder = ViewHolder()
                    viewHolder?.type = view?.findViewById(R.id.tv_item) as TextView
                    view.tag = viewHolder
                }
                ((viewHolder) as ViewHolder).type!!.text = "num$index"
            }

            override fun displaying(position: Int) {
            }

            override fun onItemClick(view: View, position: Int) {
            }

            override fun onMove() {
            }

            override fun getWidth(width: Int) {
            }

        })
        /*val aniOne = ObjectAnimator.ofFloat(clockView, "degreeY", 0f, -45f)
        aniOne.duration = 1000
        aniOne.startDelay = 500
        val aniTwo = ObjectAnimator.ofFloat(clockView, "degreeZ", 0f, 270f)
        aniTwo.duration = 1000
        aniTwo.startDelay = 500

        val aniThree = ObjectAnimator.ofFloat(clockView, "fixDegreeY", 0f, 30f)
        aniThree.duration = 500
        aniThree.startDelay = 500
        var animatorSet = AnimatorSet()
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                clockView.reset()
                animatorSet.start()
            }
        })
        animatorSet.playSequentially(aniOne, aniTwo, aniThree)
        animatorSet.start()*/
        /* clockView.setScaleCallback(object: RulerView.ScaleCallback{
             override fun scaleValue(scale: Float) {
                 tv_scale.text = "${scale / 10}"
             }
         })*/
        /* var index = 0
         et_cellindex.addTextChangedListener {
             if (it.toString().isNotEmpty() && it.toString().toInt() >= 0 &&
                 it.toString().toInt() <= 80
             ) {
                 index = it.toString().toInt()
             }
         }
         clockView.setListener {
             runOnUiThread {
                 if (it.toInt() == 0) {
                     tv_generation.text = "文明已灭亡"
                 } else {
                     tv_generation.text = "文明已繁衍至${it}代"
                 }
             }
         }
         tv_record.text = "最高记录" + getSpValue("record", "0")
         clockView.setRecordListener {
             runOnUiThread{
                 tv_record.text = "最高记录$it"
             }
         }
         btn_die.setOnClickListener {
             clockView.setDieOrLive(index, 0)
             et_cellindex.setText("")
         }
         btn_live.setOnClickListener {
             clockView.setDieOrLive(index, 1)
             et_cellindex.setText("")
         }
         btn_start.setOnClickListener {
             clockView.startGame()
         }*/

//        clockView.setProgress(400)
//        clockView.setGoal(2000)
//        clockView.startAni()
//        clockView.startTime()
//        shootView.startPlay()
    }

    internal inner class ViewHolder {
        var type: TextView? = null
    }
}