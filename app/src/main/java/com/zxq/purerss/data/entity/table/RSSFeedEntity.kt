package com.zxq.purerss.data.entity.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *  created by xiaoqing.zhou
 *  on 2020/5/7
 *  fun
 */
@Entity(tableName = "rssfeed")
data class RSSFeedEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "feed_id") var feedId: Long,
    @ColumnInfo(name = "feed_title") var feedTitle: String,
    @ColumnInfo(name = "feed_link") var feedLink: String,
    @ColumnInfo(name = "feed_desc") var feedDesc: String,
    @ColumnInfo(name = "feed_pic") var feedPic: String,
    @ColumnInfo(name = "parent_id") var parentId: Long,
    @ColumnInfo(name = "see_count") var seeCount: Int,
    @ColumnInfo(name = "add_time") var addTime: Long = System.currentTimeMillis()
) {
    var state = false
}

val goods = listOf(
    RSSFeedEntity(
        0,
        "少数派",
        "https://sspai.com/feed",
        "少数派致力于更好地运用数字产品或科学方法，帮助用户提升工作效率和生活品质",
        "http://www.dgtle.com/img/common/dgtlerss.jpeg",
        0,
        0
    ),
    RSSFeedEntity(
        0,
        "数字尾巴",
        "https://www.dgtle.com/rss/dgtle.xml",
        "分享美好数字生活",
        "http://www.dgtle.com/img/common/dgtlerss.jpeg",
        0,
        0
    )
    ,
    RSSFeedEntity(
        0,
        "PanSci 泛科學",
        "https://pansci.asia/feed",
        "全台最大科學知識社群",
        "http://www.dgtle.com/img/common/dgtlerss.jpeg",
        0,
        0
    ),
    RSSFeedEntity(
        0,
        "果壳精选",
        "https://www.guokr.com/handpick/rss/?display=rss",
        "果壳网是一个泛科技主题网站，提供负责任、有智趣、贴近生活的内容，你可以在这里阅读、分享、交流、提问。果壳网致力于让科技兴趣成为人们文化生活和娱乐生活的重要元素。",
        "http://www.dgtle.com/img/common/dgtlerss.jpeg",
        0,
        0
    )
    ,
    RSSFeedEntity(
        0,
        "爱范儿",
        "https://www.ifanr.com/feed",
        "让未来触手可及",
        "http://www.dgtle.com/img/common/dgtlerss.jpeg",
        0,
        0
    ),
    RSSFeedEntity(
        0,
        "我的豆瓣",
        "https://www.douban.com/feed/people/zhongxiazhixue/interests",
        "豆瓣个人动态更新",
        "http://www.dgtle.com/img/common/dgtlerss.jpeg",
        0,
        0
    ),
    RSSFeedEntity(
        0,
        "精品MAC应用分享",
        "https://xclient.info/feed/",
        "精品MAC应用分享，每天分享大量mac软件，为您提供优质的mac软件,免费软件下载服务",
        "http://www.dgtle.com/img/common/dgtlerss.jpeg",
        0,
        0
    ),
    RSSFeedEntity(
        0,
        "极客公园",
        "http://www.geekpark.net/rss",
        "极客公园",
        "http://www.dgtle.com/img/common/dgtlerss.jpeg",
        0,
        0
    ),
    RSSFeedEntity(
        0,
        "游研社",
        "https://www.yystv.cn/rss/feed",
        "无论你是游戏死忠，还是轻度的休闲玩家，在这里都能找到感兴趣的东西。",
        "http://www.dgtle.com/img/common/dgtlerss.jpeg",
        0,
        0
    )
)
