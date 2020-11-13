package com.zxq.purerss.ui.setting

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.zxq.livedatabus.LiveDataBus
import com.zxq.purerss.R
import com.zxq.purerss.databinding.ActivitySettingBinding
import com.zxq.purerss.ui.dialog.ExportOpmlNotiDialog
import com.zxq.purerss.ui.dialog.ShortCutsDialog
import com.zxq.purerss.utils.*
import kotlinx.android.synthetic.main.activity_setting.*
import java.io.File

class SettingActivity : AppCompatActivity() {
    private val mViewModel: SettingViewModel by viewModels {
        InjectorUtil.getSettingFactory(this)
    }
    var type = 1
    private lateinit var mContext: Context
    private val binding: ActivitySettingBinding by contentView(R.layout.activity_setting)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (Build.VERSION.SDK_INT >= 28) {
            //适配P以上的全面屏
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }*/
       /* if (getSpValue("nightmodel", 0) != 1) {
            StatusBarUtil.StatusBarLightMode(this)
        } else {
            StatusBarUtil.StatusBarDarkMode(this)
        }*/
        StatusBarUtils.transparencyBar(this)
//        StatusBarUtils.showOrHide(false,this)
        mContext = this

        binding.apply {
            mViewModel.list.observe(this@SettingActivity, Observer {
                if (type == 1) {
                    val filePath = getExternalFilesDir("opml")!!.getAbsolutePath()
                    mViewModel.exportOpml(filePath + File.separator + "purerss_opml.xml", it)
                } else {
                    val dialog = ShortCutsDialog(mContext, it)
                    dialog.show()
                }
            })

            tvBack.setOnClickListener {
//                onBackPressed()
                startActivity(Intent(this@SettingActivity,TestActivity::class.java))
            }
            mViewModel.success.observe(this@SettingActivity, Observer {
                if (it) {
                    Snackbar.make(root, "导出成功", 600).show()
                }
            })
            tvExport.setOnClickListener {
                type = 1
                exportOpml()
            }
            tvAppshortcuts.setOnClickListener {
                type = 2
                mViewModel.getAllFeeds()
            }
            if (getSpValue("nightmodel", 0) == 1) {
                rbDark.isChecked = true
            } else {
                rbDay.isChecked = true
            }
            if (getSpValue("slide", 0) == 1) {
                rbLeft.isChecked = true
            } else {
                rbRight.isChecked = true
            }
            rbDay.setOnClickListener {
                checkMode(0)
            }
            ivDay.setOnClickListener {
                checkMode(0)
            }

            ivDark.setOnClickListener {
                checkMode(1)
            }

            rbDark.setOnClickListener {
                checkMode(1)
            }
            rbLeft.setOnClickListener {
                slide(0)
                putSpValue("slide", 1)
                rbRight.isChecked = false
            }

            rbRight.setOnClickListener {
                putSpValue("slide", 0)
                rbLeft.isChecked = false
                slide(1)
            }
        }
    }

    private var mDialog: ExportOpmlNotiDialog? = null
    private fun exportOpml() {
        if (!getSpValue("exportopmlnoti", false)) {
            if (mDialog == null) {
                mDialog = ExportOpmlNotiDialog(this)
            }
            mDialog?.show()
            mDialog?.setOnDismissListener {
                mViewModel.getAllFeeds()
            }
        } else {
            mViewModel.getAllFeeds()
        }
    }

    private fun slide(i: Int) {
        if (i == 0) {
            swipe_left.smoothExpand()
            swipe_right.smoothClose()
        } else {
            swipe_right.smoothExpand()
            swipe_left.smoothClose()
        }
    }

    private fun checkMode(i: Int) {
        if (i == 0) {
            rb_day.isChecked = true
            rb_dark.isChecked = false
        } else {
            rb_day.isChecked = false
            rb_dark.isChecked = true
        }
        RippleAnimation.create(if (i == 0) rb_day else rb_dark).setDuration(1000).start()
        if (i == 0) {
            StatusBarUtil.StatusBarLightMode(this)
        } else {
            StatusBarUtil.StatusBarDarkMode(this)
        }
        if (i == 0) nsv.setBackgroundColor(Color.WHITE) else nsv.setBackgroundColor(Color.BLACK)
        if (i == 0) {
            tv_theme.setTextColor(Color.BLACK)
            tv_slide.setTextColor(Color.BLACK)
            tv_export.setTextColor(Color.BLACK)
            tv_appshortcuts.setTextColor(Color.BLACK)
            tv_select.setTextColor(Color.BLACK)
        } else {
            tv_theme.setTextColor(Color.WHITE)
            tv_slide.setTextColor(Color.WHITE)
            tv_export.setTextColor(Color.WHITE)
            tv_appshortcuts.setTextColor(Color.WHITE)
            tv_select.setTextColor(Color.WHITE)
        }
        putSpValue("nightmodel", i)
        /*    lifecycleScope.launch {
                delay(1000)
                startActivity(Intent(this@SettingActivity, SettingActivity::class.java))
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            }*/
        LiveDataBus.get<Int>("nightmodel").postValue(i)
//        AppCompatDelegate.setDefaultNightMode(if (i == 1) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }

}