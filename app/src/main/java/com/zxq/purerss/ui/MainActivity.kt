package com.zxq.purerss.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.zxq.livedatabus.LiveDataBus
import com.zxq.purerss.R
import com.zxq.purerss.databinding.ActivityMainBinding
import com.zxq.purerss.ui.add.AddRssFragment
import com.zxq.purerss.utils.*

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
            LiveDataBus.get<Int>("nightmodel").observe(this@MainActivity, Observer {
                recreate()
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
                if (true) {
                    nav.navigate(R.id.launch)
                } else {
                    nav.navigate(R.id.mainpage)
                }
            }
        }
    }
}