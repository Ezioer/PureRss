package com.zxq.purerss.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zxq.purerss.data.dao.*
import com.zxq.purerss.data.entity.table.*

/**
 *  created by xiaoqing.zhou
 *  on 2020/4/28
 *  fun
 */
@Database(
    entities = [RSSFeedEntity::class, RSSItemEntity::class, RSSLaterEntity::class, RSSReadedEntity::class, RSSCollectEntity::class, RSSSourceEntity::class, RSSFolderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DataBase : RoomDatabase() {
    abstract fun feedDao(): FeedDao
    abstract fun itemDao(): ItemDao
    abstract fun sourceDao(): SourceDao
    abstract fun folderDao(): FolderDao

    companion object {
        @Volatile
        private var INSTANCE: DataBase? = null

        fun getInstance(context: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDataBase(context).also { INSTANCE = it }
            }
        }

        private fun buildDataBase(context: Context): DataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                DataBase::class.java,

                "purerss_db"
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                }
            }).addMigrations(object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    //数据库从1升级到2时需要执行的操作，比如给某个表增加字段或增加某个表等等
                    database.execSQL("")
                }

            }).build()
        }
    }
}