package com.zxq.purerss.utils

import android.util.Log
import com.zxq.purerss.data.entity.RssFeed
import com.zxq.purerss.data.entity.RssItem
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler

/**
 * created by xiaoqing.zhou
 * on 2020/6/16
 * fun
 */
internal class RssHandler : DefaultHandler() {
    var rssFeed: RssFeed? = null
    var rssItem: RssItem? = null
    var lastElementName = "" // 标记变量，用于标记在解析过程中我们关心的几个标签，若不是我们关心的标签记做0
    val RSS_TITLE = 1 // 若是 title 标签，记做1，注意有两个title，但我们都保存在item的成员变量中
    val RSS_LINK = 2 // 若是 link 标签，记做2
    val RSS_DESCRIPTION = 3 // 若是 description 标签，记做3
    val RSS_CATEGORY = 4 // 若是category标签,记做 4
    val RSS_PUBDATE = 5 // 若是pubdate标签,记做5,注意有两个pubdate,但我们都保存在item的pubdate成员变量中
    val RSS_AUTHOR = 6
    var currentFlag = 0

    @Throws(SAXException::class)
    override fun startDocument() {
        super.startDocument()
        rssFeed = RssFeed()
    }

    @Throws(SAXException::class)
    override fun characters(ch: CharArray, start: Int, length: Int) {
        super.characters(ch, start, length)
        // 获取字符串
        val text = String(ch, start, length)
        Log.i("i", "要获取的内容：$text")
        when (currentFlag) {
            RSS_TITLE -> {
                if (rssItem == null) {
                    rssFeed?.title = text
                }
                rssItem?.title = text
                currentFlag = 0 // 设置完后，重置为开始状态
            }
            RSS_PUBDATE -> {
                rssItem?.pubdate = text
                currentFlag = 0 // 设置完后，重置为开始状态
            }
            RSS_LINK -> {
                if (rssItem == null) {
                    rssFeed?.link = text
                }
                rssItem?.link = text
                currentFlag = 0 // 设置完后，重置为开始状态
            }
            RSS_DESCRIPTION -> {
                if (rssItem == null) {
                    rssFeed?.subTitle = text
                }
                if (currentFlag == RSS_DESCRIPTION) {
                    if (text.isContainPicUrl() && rssItem!!.albumPic.isNullOrEmpty()) {
                        val list = text.split("\"")
                        for (item in list) {
                            if (item.isPicString()) {
                                rssItem?.albumPic = item
                                break
                            }
                        }
                    }
                    rssItem?.description = rssItem?.description + text
                }
            }
            RSS_AUTHOR ->{
                rssItem?.author = text
                currentFlag = 0
            }
            else -> {
            }
        }
    }

    @Throws(SAXException::class)
    override fun startElement(
        uri: String,
        localName: String,
        qName: String,
        attributes: Attributes
    ) {
        super.startElement(uri, localName, qName, attributes)
        if ("channel" == localName || "feed" == localName) {
            // 这个标签内没有我们关心的内容，所以不作处理，currentFlag=0
            currentFlag = 0
            return
        }
        if ("item" == localName || "entry" == localName) {
            rssItem = RssItem()
            return
        }
        if ("title" == localName) {
            currentFlag = RSS_TITLE
            return
        }
        if ("description" == localName || "content" == localName) {
            currentFlag = RSS_DESCRIPTION
            return
        }
        if ("link" == localName) {
            currentFlag = RSS_LINK
            return
        }
        if ("pubDate" == localName || "updated" == localName) {
            currentFlag = RSS_PUBDATE
            return
        }
        if ("category" == localName) {
            currentFlag = RSS_CATEGORY
            return
        }

        if ("author" == localName || "name" == localName) {
            currentFlag = RSS_AUTHOR
            return
        }
    }

    @Throws(SAXException::class)
    override fun endElement(
        uri: String,
        localName: String,
        qName: String
    ) {
        super.endElement(uri, localName, qName)
        // 如果解析一个item节点结束，就将rssItem添加到rssFeed中。
        if ("item" == localName || "entry" == localName) {
            rssFeed?.addItem(rssItem!!)
            return
        }
    }

    @Throws(SAXException::class)
    override fun endDocument() {
        super.endDocument()
    }

}