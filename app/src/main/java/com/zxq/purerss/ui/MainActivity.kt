package com.zxq.purerss.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import com.zxq.purerss.R
import com.zxq.purerss.databinding.ActivityMainBinding
import com.zxq.purerss.ui.add.AddRssFragment
import com.zxq.purerss.utils.StatusBarUtil
import com.zxq.purerss.utils.ViewUtils
import com.zxq.purerss.utils.contentView
import com.zxq.purerss.utils.getSpValue

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private val binding: ActivityMainBinding by contentView(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.settranslucent(this)
        if (getSpValue("nightmodel", 0) != 1) {
            StatusBarUtil.StatusBarLightMode(this)
        } else {
            StatusBarUtil.StatusBarDarkMode(this)
        }
        binding.apply {
            val nav = Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)
            if (true) {
                nav.navigate(R.id.chooserssfragment)
            } else {
                nav.navigate(R.id.mainpage)
            }
        }
    }
}