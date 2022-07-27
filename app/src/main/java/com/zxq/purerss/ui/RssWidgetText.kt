package com.zxq.purerss.ui

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.zxq.purerss.R
import com.zxq.purerss.utils.getSpValue
import com.zxq.purerss.utils.putSpValue


/**
 * Implementation of App Widget functionality.
 */
class RssWidgetText : AppWidgetProvider() {
    companion object {
        val NEXT_ACTION = "NEXT"
        var ACTION_UPDATE_UI = "action_update_ui_text"
        var WIDGET_TITLE = "widget_title"
        var WIDGET_DATE = "widget_date"
        var WIDGET_FEED = "widget_feed"
        var WIDGET_PIC = "widget_pic"
        var WIDGET_INDEX = 0
        var views: RemoteViews? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (action == ACTION_UPDATE_UI) {
            onUpdate(
                context!!, AppWidgetManager.getInstance(context), context?.getSpValue(
                    "appwidgetids",
                    intArrayOf()
                )
            )
            /*   val title = intent.getStringExtra(WIDGET_TITLE)
               val date = intent.getStringExtra(WIDGET_DATE)
               val feed = intent.getStringExtra(WIDGET_FEED)
               val pic = intent.getStringExtra(WIDGET_PIC)
               Log.d("broadcast", title + date + pic)
               updateRemoteView(context, title, date, pic, feed)*/
        } else if (action!!.contains(Intent.CATEGORY_ALTERNATIVE)) {
            /* val actionIntent = Intent(MusicManageService.ACTION_CONTROL_PLAY)
             context?.sendBroadcast(actionIntent)*/
        } else if (action == NEXT_ACTION) {
            val rv = RemoteViews(
                context!!.packageName,
                R.layout.rss_widget
            )

//            rv.showNext(R.id.avp)

            AppWidgetManager.getInstance(context).partiallyUpdateAppWidget(
                intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
                ), rv
            )
        }
        super.onReceive(context, intent)
    }

    fun updateRemoteView(
        context: Context?,
        title: String?,
        date: String?,
        pic: String?,
        feed: String?
    ) {
        val views = RemoteViews(context?.packageName, R.layout.rss_widget)
//        views.setTextViewText(R.id.widget_title, title)
//        views.setTextViewText(R.id.widget_feed, feed)
//        views.setTextViewText(R.id.widget_time, date)
//        views.setOnClickPendingIntent(R.id.iv_next, getPendingIntent(context!!))
        Glide.with(context!!).asBitmap().load(pic).centerCrop()
            .into(object : SimpleTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    views.setImageViewBitmap(R.id.widget_pic, resource)
                    val componentName = ComponentName(context, RssWidgetText::class.java)
                    AppWidgetManager.getInstance(context).updateAppWidget(componentName, views)
                }
            })
    }

    private fun getPendingIntent(context: Context): PendingIntent? {
        /*     WIDGET_INDEX++
             val intent = Intent()
             //注意这个intent构造的是显式intent，直接将这个广播发送给MyAppWidgetProvider
             intent.setClass(context, RssWidgetText::class.java)
             intent.addCategory(Intent.CATEGORY_ALTERNATIVE)
             val pendingIntent =
                 PendingIntent.getBroadcast(context, 0, intent, FLAG_IMMUTABLE)
             return pendingIntent*/
        return null
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        context?.putSpValue("num$appWidgetIds", appWidgetIds)
        for (appWidgetId in appWidgetIds) {
//            updateRemoteView(context, "", "", "", "")
            updateAppWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
//        context.startService(Intent(context, WidgetUpdateService::class.java))
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
//        context.stopService(Intent(context, WidgetUpdateService::class.java))
    }

    fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        views = RemoteViews(
            context.packageName,
            R.layout.rss_widge_text
        )

        drawViews(context)
        val intent = Intent(context, WidgetService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)))
//    views.setRemoteAdapter(R.id.avp, intent)

        val nextIntent = Intent(
            context,
            RssWidgetText::class.java
        )
        nextIntent.action = RssWidget.NEXT_ACTION
        nextIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        /*   val nextPendingIntent = PendingIntent
               .getBroadcast(
                   context, 0, nextIntent,
                   PendingIntent.FLAG_UPDATE_CURRENT
               )*/
//    views.setOnClickPendingIntent(R.id.iv_next, nextPendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    fun drawViews(context: Context) {
        //绘制图片
        var bitmapclock = DrawUtils.drawClock(context, 1)
        //更新图片
        views?.setImageViewBitmap(R.id.widget_pic_text, bitmapclock)
    }
}
