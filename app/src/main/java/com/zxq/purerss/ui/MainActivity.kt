package com.zxq.purerss.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.RequestCallback
import com.zxq.livedatabus.LiveDataBus
import com.zxq.purerss.R
import com.zxq.purerss.databinding.ActivityMainBinding
import com.zxq.purerss.utils.StatusBarUtil
import com.zxq.purerss.utils.contentView
import com.zxq.purerss.utils.getSpValue
import com.zxq.purerss.utils.putSpValue
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private val binding: ActivityMainBinding by contentView(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        ViewUtils.settranslucent(this)
        val i = getSpValue("nightmodel", 0)
        if (i == 0) {
            StatusBarUtil.StatusBarLightMode(this)
        } else {
            StatusBarUtil.StatusBarDarkMode(this)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            getWallpaperBitmap()
        }
        val arraylist: Array<TextView> = arrayOf(TextView(this), TextView(this), TextView(this))
        val imge = ImageView(this)
        val li = LinearLayout(this)
        val anylist: Array<Any> = arrayOf("1", "2", "3")
        //out 定义的范型必须是赋值的父类
        val anys: Array<out View> = arraylist
        val str: Any = anys.get(0)
        //in 定义的范型必须是赋值的子类
        val lll: Array<in String> = anylist
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

        PermissionX.init(this).permissions(Manifest.permission.MANAGE_EXTERNAL_STORAGE).request(
            RequestCallback { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    }
                }
            })
    }

    fun getNet() {
        val okhttpClient = OkHttpClient.Builder()
            .readTimeout(5000L, TimeUnit.MILLISECONDS)
            .build()
        val request = Request.Builder()
            .addHeader("", "")
            .url("")
            .build()
        okhttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O_MR1)
    @SuppressLint("MissingPermission")
    fun getWallpaperBitmap() {
        val wallpaperManager = WallpaperManager.getInstance(this)//获取WallpaperManager实例
        val mParcelFileDescriptor = wallpaperManager.drawable
        val fileDescriptor = (mParcelFileDescriptor as BitmapDrawable).bitmap
        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.spring)
        val palette = Palette.from(bitmap).generate {
            val vibrantColor = it?.getDarkVibrantColor(Color.GREEN)
            val dominantColor = it?.getDominantColor(Color.GREEN)
            putSpValue("bgcolor", vibrantColor)
        }
    }
}