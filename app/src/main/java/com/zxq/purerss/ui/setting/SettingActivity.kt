package com.zxq.purerss.ui.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.zxq.livedatabus.LiveDataBus
import com.zxq.purerss.R
import com.zxq.purerss.databinding.ActivitySettingBinding
import com.zxq.purerss.ui.dialog.ExportOpmlNotiDialog
import com.zxq.purerss.ui.dialog.ShortCutsDialog
import com.zxq.purerss.utils.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.time.Duration

class SettingActivity : AppCompatActivity() {
    private val mViewModel: SettingViewModel by viewModels {
        InjectorUtil.getSettingFactory(this)
    }
    var type = 1
    private lateinit var mContext: Context
    private val binding: ActivitySettingBinding by contentView(R.layout.activity_setting)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getSpValue("nightmodel", 0) != 1) {
            StatusBarUtil.StatusBarLightMode(this)
        } else {
            StatusBarUtil.StatusBarDarkMode(this)
        }
        mContext = this
        binding.apply {
            setSupportActionBar(toolbar)
            ctlLayout.title = getString(R.string.setting)
            ctlLayout.isTitleEnabled = true
            ctlLayout.setExpandedTitleColor(getColor(R.color.c_008f68))
            ctlLayout.setCollapsedTitleTextColor(getColor(R.color.c_008f68))
            toolbar.setNavigationOnClickListener { finish() }

            mViewModel.list.observe(this@SettingActivity, Observer {
                if (type == 1) {
                    val filePath = getExternalFilesDir("opml")!!.getAbsolutePath()
                    mViewModel.exportOpml(filePath + File.separator + "purerss_opml.xml", it)
                } else {
                    val dialog = ShortCutsDialog(mContext, it)
                    dialog.show()
                }
            })
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
        lifecycleScope.launch {
            delay(1000)
            startActivity(Intent(this@SettingActivity, SettingActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            LiveDataBus.get<Int>("nightmodel").postValue(i)
            finish()
        }
        putSpValue("nightmodel", i)
        AppCompatDelegate.setDefaultNightMode(if (i == 1) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }
}