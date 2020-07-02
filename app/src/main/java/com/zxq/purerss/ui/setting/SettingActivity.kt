package com.zxq.purerss.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.zxq.purerss.R
import com.zxq.purerss.databinding.ActivitySettingBinding
import com.zxq.purerss.utils.StatusBarUtil
import com.zxq.purerss.utils.contentView
import com.zxq.purerss.utils.getSpValue
import com.zxq.purerss.utils.putSpValue
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity() {
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
            if (getSpValue("nightmodel", 0) == 1) {
                rbDark.isChecked = true
            } else {
                rbDay.isChecked = true
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
                rbRight.isChecked = false
            }

            rbRight.setOnClickListener {
                rbLeft.isChecked = false
                slide(1)
            }
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