package com.zxq.purerss.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.zxq.purerss.App
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.URLDecoder
import java.net.URLEncoder


/**
 *  created by xiaoqing.zhou
 *  on 2020/5/6
 *  fun
 */

inline fun SharedPreferences.edit(
    commit: Boolean = false,
    action: SharedPreferences.Editor.() -> Unit
) {
    val editor = edit()
    action(editor)
    if (commit)
        editor.commit()
    else
        editor.apply()
}

fun Context.sp(name: String = "EVENTHISTORY", mode: Int = Context.MODE_PRIVATE): SharedPreferences =
    getSharedPreferences(name, mode)

fun Activity.sp(
    name: String = "EVENTHISTORY",
    mode: Int = Context.MODE_PRIVATE
): SharedPreferences =
    getSharedPreferences(name, mode)


fun <T> Context.putSpValue(key: String, value: T, name: String = "EVENTHISTORY") = sp(name).edit {
    when (value) {
        is Long -> putLong(key, value)
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Boolean -> putBoolean(key, value)
        is Float -> putFloat(key, value)
        else -> putString(key, serialize(value))
    }
}

fun <T> Activity.putSpValue(key: String, value: T, name: String = "EVENTHISTORY") = sp(name).edit {
    when (value) {
        is Long -> putLong(key, value)
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Boolean -> putBoolean(key, value)
        is Float -> putFloat(key, value)
        else -> putString(key, serialize(value))
    }
}

fun <T> Context.getSpValue(key: String, default: T, name: String = "EVENTHISTORY"): T =
    sp(name).run {
        val result = when (default) {
            is Long -> getLong(key, default)
            is String -> getString(key, default)
            is Int -> getInt(key, default)
            is Boolean -> getBoolean(key, default)
            is Float -> getFloat(key, default)
            else -> deSerialization(getString(key, serialize(default)))
        }
        result as T
    }

fun <T> Activity.getSpValue(key: String, default: T, name: String = "EVENTHISTORY"): T =
    sp(name).run {
        val result = when (default) {
            is Long -> getLong(key, default)
            is String -> getString(key, default)
            is Int -> getInt(key, default)
            is Boolean -> getBoolean(key, default)
            is Float -> getFloat(key, default)
            else -> deSerialization(getString(key, serialize(default)))
        }
        return result as T
    }


private fun <T> serialize(obj: T): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    val objectOutputStream = ObjectOutputStream(
        byteArrayOutputStream
    )
    objectOutputStream.writeObject(obj)
    var serStr = byteArrayOutputStream.toString("ISO-8859-1")
    serStr = URLEncoder.encode(serStr, "UTF-8")
    objectOutputStream.close()
    byteArrayOutputStream.close()
    return serStr
}

private fun <T> deSerialization(str: String?): T {
    val redStr = URLDecoder.decode(str, "UTF-8")
    val byteArrayInputStream = ByteArrayInputStream(
        redStr.toByteArray(charset("ISO-8859-1"))
    )
    val objectInputStream = ObjectInputStream(
        byteArrayInputStream
    )
    val obj = objectInputStream.readObject() as T
    objectInputStream.close()
    byteArrayInputStream.close()
    return obj
}

fun Context.dp2px(dp: Float): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Context.px2dp(px: Float): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun View.dp2px(dp: Float): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun View.px2dp(px: Float): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun View.sp2px(value: Float): Int {
    val r: Resources = if (App.instance == null) {
        Resources.getSystem()
    } else {
        App.instance!!.resources
    }
    val spvalue: Float = value * r.displayMetrics.scaledDensity
    return (spvalue + 0.5f).toInt()
}

fun EditText.addOnAfterChange(afterChange: (Editable?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterChange(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    })
}

fun String.isPicUrl(): Boolean {
    return (this.startsWith("https") || this.startsWith("http")) && (this.endsWith("png") || this.endsWith(
        "jpeg"
    ) || this.contains("jpeg") || this.endsWith("webp")
            || this.endsWith("gif"))
}