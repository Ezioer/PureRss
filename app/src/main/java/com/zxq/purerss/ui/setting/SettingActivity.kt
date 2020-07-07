package com.zxq.purerss.ui.setting

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.zxq.purerss.R
import com.zxq.purerss.data.entity.table.goods
import com.zxq.purerss.databinding.ActivitySettingBinding
import com.zxq.purerss.ui.dialog.ExportOpmlNotiDialog
import com.zxq.purerss.utils.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {
    private val mViewModel: SettingViewModel by viewModels {
        InjectorUtil.getSettingFactory(this)
    }
    private val binding: ActivitySettingBinding by contentView(R.layout.activity_setting)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getSpValue("nightmodel", 0) != 1) {
            StatusBarUtil.StatusBarLightMode(this)
        } else {
            StatusBarUtil.StatusBarDarkMode(this)
        }
        binding.apply {
            setSupportActionBar(toolbar)
            ctlLayout.title = getString(R.string.setting)
            ctlLayout.isTitleEnabled = true
            ctlLayout.setExpandedTitleColor(getColor(R.color.c_008f68))
            ctlLayout.setCollapsedTitleTextColor(getColor(R.color.c_008f68))
            toolbar.setNavigationOnClickListener { finish() }
            tvExport.setOnClickListener {
                exportOpml()
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
            mViewModel.list.observe(this@SettingActivity, Observer {
                ReadOPML.write(
                    Environment.getExternalStorageDirectory().getPath() + "/OPML/opml.xml", it
                )
            })
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
            delay(200)
            startActivity(Intent(this@SettingActivity, SettingActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }
        putSpValue("nightmodel", i)
        AppCompatDelegate.setDefaultNightMode(if (i == 1) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }
}