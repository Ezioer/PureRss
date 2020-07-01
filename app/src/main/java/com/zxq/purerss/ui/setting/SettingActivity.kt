package com.zxq.purerss.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.zxq.purerss.R
import com.zxq.purerss.databinding.ActivitySettingBinding
import com.zxq.purerss.utils.contentView
import com.zxq.purerss.utils.getSpValue
import com.zxq.purerss.utils.putSpValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingActivity : AppCompatActivity(){
    private val binding: ActivitySettingBinding by contentView(R.layout.activity_setting)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            switchDark.isChecked = getSpValue("darktheme", 0) == 1
            llDark.setOnClickListener {
                lifecycleScope.launch {
                    delay(170)
                    startActivity(Intent(this@SettingActivity, SettingActivity::class.java))
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    finish()
                }

                switchDark.isChecked = !switchDark.isChecked
                putSpValue("darktheme", if (switchDark.isChecked) 1 else 0)
                AppCompatDelegate.setDefaultNightMode(if (switchDark.isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}