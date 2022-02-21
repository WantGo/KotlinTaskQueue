package com.jack.library.task

/**
 * @author:jack
 * @time:2021/12/24 11:03 上午
 * @description 任务队列接口
 */
internal interface ITaskQueue {
    /**
     * 消息入队
     */
    suspend fun enqueue(tagTask: ITagTask): Boolean

    /**
     * 消息出队
     */
    suspend fun dequeue(tag: String)

    /**
     * 循环消息
     */
    fun loop()

    /**
     * 重置数据
     */
    fun reset()
}