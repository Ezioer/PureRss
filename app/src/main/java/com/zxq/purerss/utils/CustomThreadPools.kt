package com.zxq.purerss.utils

import java.util.concurrent.BlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.locks.ReentrantLock

class CustomThreadPools constructor(
    minSize: Int, maxSize: Int, keepAliveTime: Long, unit: TimeUnit
    , workQueue: BlockingQueue<Runnable>
) {
    private val lock: ReentrantLock = ReentrantLock()
    private var minSize = 0
    private var maxSize = 5
    private var keepAliveTime = 1000L
    private lateinit var unit: TimeUnit
    private lateinit var workQueue: BlockingQueue<Runnable>
    private lateinit var works: ConcurrentHashSet<Work>

    /**
     * 是否关闭线程池标志
     */
    private val isShutDown: AtomicBoolean = AtomicBoolean(false)

    /**
     * 提交到线程池中的任务总数
     */
    private val totalTask: AtomicInteger = AtomicInteger()

    /**
     * 线程池任务全部执行完毕后的通知组件
     */
    private val shutDownNotify = Any()

    init {
        this.minSize = minSize
        this.maxSize = maxSize
        this.keepAliveTime = keepAliveTime
        this.unit = unit
        this.workQueue = workQueue
        works = ConcurrentHashSet()
    }

    fun excute(runnable: Runnable) {
        if (runnable == null) {
            return
        }
        if (isShutDown.get()) {
            return
        }
        totalTask.incrementAndGet()
        //如果当前工作线程小于最小核心线程，则直接添加任务
        if (works.size < minSize) {
            addWork(runnable)
            return
        }

        //如果当前工作线程大于最小核心线程，是否能够写入到队列中（是否已满）
        val offer = workQueue.offer(runnable)
        if (!offer) {
            //不能写入时，如果工作线程小于最大数量，则添加任务，否则阻塞写入
            if (works.size < maxSize) {
                addWork(runnable)
                return
            } else {
                try {
                    workQueue.put(runnable)
                } catch (e: Exception) {

                }
            }
        }
    }

    private fun addWork(runnable: Runnable) {
        val work = Work(runnable, true)
        work.startTask()
        works.add(work)
    }

    inner class Work(runnable: Runnable, isNewTask: Boolean) : Thread() {

        private lateinit var task: Runnable
        private lateinit var thread: Thread
        private var isNewTask: Boolean = false

        init {
            this.task = runnable
            this.isNewTask = isNewTask
            thread = this
        }

        fun startTask() {
            thread.start()
        }

        fun close() {
            thread.interrupt()
        }

        override fun run() {
            var task: Runnable? = null
            if (!isNewTask) {
                task = this.task
            }
            try {
                while ((task != null) || (getTask().also { task = it } != null)) {
                    try {
                        task?.run()
                    } catch (e: Exception) {

                    } finally {
                        task = null
                    }
                }
            } finally {
                works.remove(this)
                tryCLose(true)
            }

        }
    }

    fun getTask(): Runnable? {
        if (isShutDown.get() && totalTask.get() == 0) {
            return null
        }
        lock.lock()
        try {
            var task: Runnable? = null
            task = if (works.size > minSize) {
                workQueue.poll(keepAliveTime, unit)
            } else {
                workQueue.take()
            }

            if (task != null) {
                return task
            }
        } catch (e: Exception) {
            return null
        } finally {
            lock.unlock()
        }
        return null
    }

    fun shutDownNow() {
        isShutDown.set(true)
        tryCLose(false)
    }

    fun shutDown() {
        isShutDown.set(true)
        tryCLose(true)
    }

    private fun tryCLose(isTry: Boolean) {
        if (!isTry) {
            closeAllTask()
        } else {
            if (isShutDown.get() && totalTask.get() == 0) {
                closeAllTask()
            }
        }
    }

    private fun closeAllTask() {
        for (work in works) {
            work.close()
        }
    }
}