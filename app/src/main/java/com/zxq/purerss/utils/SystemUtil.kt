package com.zxq.purerss.utils

import android.annotation.SuppressLint
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.view.View
import com.zxq.purerss.widget.ObservableArrayList
import java.util.*

class SystemUtil {
    companion object {
        @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
        fun enableGray(enable: Boolean) {
            if (!enable) {
                return
            }
            try {
                //灰色调的paint
                var paint = Paint()
                var colorMatrix = ColorMatrix()
                colorMatrix.setSaturation(0f)
                paint.colorFilter = ColorMatrixColorFilter(colorMatrix)

                //反射获取类
                val windowManagerGlobal = Class.forName("android.view.WindowManagerGlobal")
                //获取getinstance方法
                val getInstanceMethod = windowManagerGlobal.getDeclaredMethod("getInstance")
                //私有方法或变量修改需要给权限，否则会报错
                getInstanceMethod.isAccessible = true
                //执行getinstance方法，获取实例对象
                val invoke = getInstanceMethod.invoke(windowManagerGlobal)

                //获取成员变量
                val viewsField = windowManagerGlobal.getDeclaredField("mViews")
                //成员变量可修改
                viewsField.isAccessible = true
                //获取成员变量值
                val viewsObject = viewsField.get(invoke)
                val observableList = ObservableArrayList<View>()
                observableList.addOnListChangedListener(object :
                    ObservableArrayList.OnListChangeListener {
                    override fun onChange(list: ArrayList<*>?, index: Int, count: Int) {

                    }

                    override fun onRemove(list: ArrayList<*>?, start: Int, count: Int) {
                    }

                    override fun onAdd(list: ArrayList<*>?, start: Int, count: Int) {
                        var view = list?.get(start) as View
                        view?.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
                    }

                })

                observableList.addAll(viewsObject as ArrayList<View>)
                //设置成员变量值为observableList
                viewsField.set(invoke, observableList)
            } catch (e: Exception) {

            }

        }
    }
}