package com.jack.library.task

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach


/**
 * @author lingko
 * @version 1.0
 * @time 2022/01/20
 * @param isAsync 是否是异步执行
 * @description
 */
class TaskQueue(var isAsync: Boolean = false) : ITaskQueue {
    /**
     * 任务管道，任务需要缓冲，防止任务过大漏消息的情况
     */
    var channel = Channel<ITagTask>(Channel.UNLIMITED)
    var taskErrorContext = TaskErrorContext(TaskErrorContext.DefaultTaskErrorStrategy())
    private var coroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            taskErrorContext.handleError(e)//SupervisorJob 防止子协程抛异常，影响其他协程
        })

    init {
        loop()
    }

    /**
     * 入队
     * 入队前先检查队列状态是否是激活状态，如果是激活状态，需要重新创建队列，并重新发送缓冲任务
     */
    override suspend fun enqueue(tagTask: ITagTask): Boolean {
        return if (checkQueueActive()) {
            trySend(tagTask)
        } else {
            logi("TaskQueue", "${tagTask.tag()} channel not active !!!")
            reset()//管道损坏，重置管道
            senCacheTaskIfNeed()//重新发送缓存任务
            trySend(tagTask)//最后发送本次需要执行的任务
        }
    }

    /**
     * 缓存任务,重新入队
     */
    private suspend fun senCacheTaskIfNeed() {
        if (PackageTaskManager.getTaskSize() > 0) {
            var taskList = mutableListOf<ITagTask>()
            PackageTaskManager.getTaskList().forEach {
                taskList.add(it)
            }
            PackageTaskManager.clear()//先清理
            taskList.forEach {
                trySend(it)
            }
        }
    }

    /**
     * 发送任务,并去重
     */
    private suspend fun trySend(tagTask: ITagTask): Boolean {
        try {
            return if (checkQueueActive()) {
                if (PackageTaskManager.addTask(tagTask)) {//去重
                    channel.trySend(tagTask)//这个方法在管道异常情况下，会抛异常
                    logi(
                        "TaskQueue",
                        "${tagTask.tag()} task add,task queue size:${PackageTaskManager.getTaskSize()}"
                    )
                    true
                } else {
                    logi("TaskQueue", "${tagTask.tag()} already add!!!")
                    false
                }
            } else {
                logi("TaskQueue", "queue not active")
                dequeue(tagTask.tag())
                false
            }
        } catch (e: Exception) {
            taskErrorContext.handleError(e)
            dequeue(tagTask.tag())
            return false
        }
    }

    /**
     * 检查管道是否激活
     */
    private fun checkQueueActive(): Boolean {
        return !channel.isClosedForReceive && !channel.isClosedForSend
    }

    /**
     * 消息出队
     */
    override suspend fun dequeue(tag: String) {
        PackageTaskManager.removeTask(tag)
        logi("TaskQueue", "$tag task remove,task queue size:${PackageTaskManager.getTaskSize()}")
    }

    /**
     * 消息循环
     */
    override fun loop() {
        coroutineScope.launch(CoroutineName("queue_loop")) {
            logi("TaskQueue", "start loop")
            channel.consumeEach {
                if (isAsync) launch { tryReceive(it) } else tryReceive(it)
            }
        }
    }

    /**
     * 重置数据
     */
    override fun reset() {
        logi("TaskQueue", "reset queue")
        channel = Channel(Channel.BUFFERED)
        loop()
    }

    /**
     * 接收消息，并处理
     */
    private suspend fun tryReceive(it: ITagTask) {
        try {//这里执行必须要catch 异常，否则通道发生异常会被关闭
            logi("TaskQueue", "start execute task ${it.tag()}")
            handleTask(it)
        } catch (e: Exception) {
            taskErrorContext.handleError(e)
        } finally {
            dequeue(it.tag())//出队
        }
    }

    private suspend fun handleTask(it: ITagTask) {
        it.execute()
    }
}


