package com.jack.library.task

/**
 * @author lingko
 * @version 1.0
 * @time 2021/12/27
 * @description 任务管理类
 */
internal interface TaskManager<T> {
    /**
     * 添加任务
     */
    fun addTask(task: T): Boolean

    /**
     *移除任务
     */
    fun removeTask(tag: String)

    /**
     * 获取任务
     */
    fun getTask(tag: String): T?

    /**
     * 是否已经有该任务
     */
    fun hasTak(tag: String): Boolean

    /**
     * 获取任务大小
     */
    fun getTaskSize(): Int

    /**
     * 清理任务
     */
    fun clear()

    fun getTaskList(): Collection<T>
}