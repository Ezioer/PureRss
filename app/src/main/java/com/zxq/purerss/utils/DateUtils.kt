package com.zxq.purerss.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by xiaoqing.zhou
 * on  2019/1/10
 */
class DateUtils {
    companion object {

        const val YMDHMS = "yyyy-MM-dd HH:mm:ss"
        const val YMD = "yyyy-MM-dd"
        const val MD = "MM-dd"

        fun getCurrentSystemTime(): Long {
            return System.currentTimeMillis() / 1000
        }

        fun convertTimestamp(timestamp: Long, pattern: String): String {
            return dateToStr(Date(timestamp * 1000L), pattern)
        }

        fun dateToStr(date: Date, pattern: String): String {
            val format = SimpleDateFormat(pattern)
            return format.format(date)
        }

        fun getFormatDate(timeStamp: Long, pattern: String): String {
            var timeStamp = timeStamp
            var pattern = pattern
            val time: String
            if (Math.abs(timeStamp).toString().length < 11) {
                timeStamp *= 1000
            }
            if (pattern.isNullOrEmpty()) {
                pattern = YMDHMS
            }
            val format = SimpleDateFormat(pattern)
            format.timeZone = TimeZone.getDefault()
            time = format.format(timeStamp)
            return time
        }

        fun getNextDayTimeStamp(time: Long, day: Int): Long {
            var timeStamp: Long = 0
            val cal = Calendar.getInstance()
            val date = Date(time)
            val sdf = SimpleDateFormat(YMD)
            try {
                cal.time = date
                cal.add(Calendar.DATE, 1)
                timeStamp = getStringToTimeStamp(sdf.format(cal.time))
            } catch (e: Exception) {
                return timeStamp
            }
            return timeStamp
        }

        fun getStringToTimeStamp(stringDate: String): Long {
            val simpleDateFormat = SimpleDateFormat(YMD)
            val date: Date
            var timeStamp: Long = 0
            if (stringDate.isNullOrEmpty()) {
                return timeStamp
            }
            try {
                date = simpleDateFormat.parse(stringDate)
                timeStamp = date.time
            } catch (e: ParseException) {
                return timeStamp
            }
            return timeStamp
        }

        fun handleDate(date: String): String {
            var time = ""
            var result = 0L
            try {
                var pattern = "yyyy-MM-dd'T'HH:mm:ssZ"
                var sdf = SimpleDateFormat(pattern, Locale.CHINA)
                time = sdf.format(Date(date));//将传入的字符串转ISO 8601格式
                result = sdf.parse(time).getTime()
                time = convertTimestamp(result, MD)
            } catch (e: ParseException) {
                e.printStackTrace();
            }
            return time
        }
    }
}