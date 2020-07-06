package com.zxq.purerss.utils

import android.content.Context
import com.zxq.purerss.data.entity.RssOpmlInfo
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.File
import java.io.FileInputStream

/**
 *  created by xiaoqing.zhou
 *  on 2020/7/6
 *  fun
 */
class ReadOPML {
    companion object {
        fun read(context: Context): MutableList<RssOpmlInfo>? {
            val list = mutableListOf<RssOpmlInfo>()
            val reader = SAXReader()
            val fis = context.assets.open("opml.xml")
            val doc = reader.read(fis)
            val elm = doc.rootElement
            val body = elm.selectSingleNode("body") as Element
            val elementIterator = body.elementIterator("outline")
            for (item in elementIterator) {
                val title = item.attributeValue("title")
                val url = item.attributeValue("xmlUrl")
                list.add(RssOpmlInfo(title, url))
            }
            return list
        }

        fun read(filepath: String): MutableList<RssOpmlInfo>? {
            val file = File(filepath)
            if (!file.exists()) {
                return null
            }
            val list = mutableListOf<RssOpmlInfo>()
            val reader = SAXReader()
            val fis = FileInputStream(file)
            val doc = reader.read(fis)
            val elm = doc.rootElement
            val body = elm.selectSingleNode("body") as Element
            val elementIterator = body.elementIterator("outline")
            for (item in elementIterator) {
                val title = item.attributeValue("title")
                val url = item.attributeValue("xmlUrl")
                list.add(RssOpmlInfo(title, url))
            }
            return list
        }
    }
}