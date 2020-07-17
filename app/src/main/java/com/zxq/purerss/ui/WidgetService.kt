package com.zxq.purerss.ui

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.zxq.purerss.R
import java.util.*


/**
 *  created by xiaoqing.zhou
 *  on 2020/7/17
 *  fun
 */
class WidgetService : RemoteViewsService() {
    companion object {
        val TAG = "widget_service"
    }

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return ViewFactory(this.applicationContext, intent)
    }


    private class ViewFactory(private var context: Context, intent: Intent?) : RemoteViewsFactory {
        private var mInstanceId = AppWidgetManager.INVALID_APPWIDGET_ID
        override fun onCreate() {
            Log.i(TAG, "onCreate()")
        }

        override fun onDataSetChanged() {
            Log.i(TAG, "onDataSetChanged()")
        }

        override fun onDestroy() {
            Log.i(TAG, "onDestroy()")
        }

        override fun getCount(): Int {
            Log.i(TAG, "getCount() ")
            return 5
        }

        override fun getViewAt(position: Int): RemoteViews {
            Log.i(TAG, "getViewAt()$position")
            val count = "这是第" + position + "'个页面ssssssssssssssssssssssssssssssssssssssssssssssssss"
            val page = RemoteViews(context.packageName, R.layout.rss_widget_fliper)
            page.setTextViewText(R.id.widget_title, count)
            page.setTextViewText(R.id.widget_time, "0707")
            page.setTextViewText(R.id.widget_feed, "ssp")
            return page
        }

        override fun getLoadingView(): RemoteViews? {
            Log.i(TAG, "getLoadingView()")
            return null
        }

        override fun getViewTypeCount(): Int {
            Log.i(TAG, "getViewTypeCount()")
            return 1
        }

        override fun getItemId(position: Int): Long {
            Log.i(TAG, "getItemId()")
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            Log.i(TAG, "hasStableIds()")
            return true
        }

        init {
            mInstanceId = intent!!.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
    }
}