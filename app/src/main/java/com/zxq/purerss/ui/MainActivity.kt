package com.zxq.purerss.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.zxq.livedatabus.LiveDataBus
import com.zxq.purerss.R
import com.zxq.purerss.databinding.ActivityMainBinding
import com.zxq.purerss.utils.*

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private val binding: ActivityMainBinding by contentView(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.settranslucent(this)
        val i = getSpValue("nightmodel", 0)
        if (i == 0) {
            StatusBarUtil.StatusBarLightMode(this)
        } else {
            StatusBarUtil.StatusBarDarkMode(this)
        }
        binding.apply {
            LiveDataBus.get<Int>("nightmodel").observe(this@MainActivity, Observer {
                putSpValue("isMainPage", 1)
                AppCompatDelegate.setDefaultNightMode(if (it == 1) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            })
            val nav = Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)
            if (intent.action == "android.intent.action.shortcuts") {
                putSpValue("fromshortcuts", 1)
                val bundle = Bundle().apply {
                    putLong("id", intent.getLongExtra("feedid", 0))
                    putString("link", intent.getStringExtra("link"))
                    putString("title", intent.getStringExtra("title"))
                    putString("des", intent.getStringExtra("des"))
                }
                nav.navigate(R.id.mainpage, bundle)
            } else {
                if (getSpValue("isMainPage", 0) == 1) {
//                    nav.navigate(R.id.mainpage)
                } else {
                    if (true) {
                        nav.navigate(R.id.launch)
                    } else {
                        nav.navigate(R.id.mainpage)
                    }
                }
            }
        }
    }
}