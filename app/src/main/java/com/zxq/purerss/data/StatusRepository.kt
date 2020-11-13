package com.zxq.purerss.data

import android.net.Uri
import com.zxq.purerss.data.dao.CircleDao
import com.zxq.purerss.data.entity.ImageItemInfo
import com.zxq.purerss.data.entity.StatusInfo
import com.zxq.purerss.data.entity.table.CircleItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *  created by xiaoqing.zhou
 *  on 2020/4/28
 *  fun
 */
class StatusRepository private constructor(
    private val circleDao: CircleDao
) {

    suspend fun insertStatus(content: String, pics: MutableList<ImageItemInfo>): Long =
        withContext(Dispatchers.IO) {
            var pic = ""
            for (item in pics) {
                if (item.uri != null) {
                    pic += (item.uri.toString() + ",")
                }
            }
            circleDao.insertOneStatus(
                CircleItemEntity(
                    0,
                    content,
                    "Ezio",
                    System.currentTimeMillis(),
                    0,
                    if (pic.isNullOrEmpty()) 0 else 1,
                    pic
                )
            )
        }


    suspend fun insertStatus(statusInfo: StatusInfo): Long =
        withContext(Dispatchers.IO) {
            var pic = ""
            for (items in statusInfo.pics!!) {
                pic = pic + items + ","
            }
            circleDao.insertOneStatus(
                CircleItemEntity(
                    statusInfo.id,
                    statusInfo.content,
                    statusInfo.name,
                    statusInfo.time,
                    statusInfo.like,
                    statusInfo.isHasPic,
                    pic
                )
            )
        }

    suspend fun getStatusFromDb(): MutableList<StatusInfo> = withContext(Dispatchers.IO) {
        val list = circleDao.getAllStatusFromDb()
        val status = mutableListOf<StatusInfo>()
        for (item in list) {
            val result = StatusInfo(
                item.itemId,
                item.statusName,
                item.statusContent,
                item.statusTime,
                item.statusLike,
                item.statusPic
            )
            val data = mutableListOf<ImageItemInfo>()
            for (pic in item.statusPics.split(",")) {
                if (!pic.isNullOrEmpty()) {
                    data.add(ImageItemInfo("", Uri.parse(pic), ""))
                }
            }
            result.pics = data
            status.add(result)
        }
        status
    }

    suspend fun deleteOne(statusInfo: StatusInfo) = withContext(Dispatchers.IO) {
        circleDao.deleteStatus(statusInfo.id)
    }

    companion object {

        @Volatile
        private var instance: StatusRepository? = null

        fun getInstance(circleDao: CircleDao) =
            instance ?: synchronized(this) {
                instance ?: StatusRepository(circleDao).also { instance = it }
            }
    }
}