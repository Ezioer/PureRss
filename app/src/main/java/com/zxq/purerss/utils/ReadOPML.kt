package com.zxq.purerss.utils

import android.content.Context
import com.zxq.purerss.data.entity.RssOpmlInfo
import com.zxq.purerss.data.entity.table.RSSFeedEntity
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.dom4j.io.XMLWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter


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

        /**
         * 写入操作
         * @param fileName
         */
        fun write(fileName: String?, list: MutableList<RSSFeedEntity>) {
            val document = DocumentHelper.createDocument() //建立document对象，用来操作xml文件
            val booksElement: Element = document.addElement("head") //建立根节点
            booksElement.addComment("Rss Opml file from PureRss ") //加入一行注释
            val bookElement = booksElement.addElement("body") //添加一个book节点
            bookElement.addAttribute("show", "yes") //添加属性内容
            val titleElement = bookElement.addElement("outline") //添加文本节点
            titleElement.text = "ajax in action" //添加文本内容
            try {
                val writer = XMLWriter(FileWriter(File(fileName)))
                writer.write(document)
                writer.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}