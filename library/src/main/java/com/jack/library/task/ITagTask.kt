package com.jack.library.task


/**
 * @author jack
 * @version 1.0
 * @time 2022/01/21
 * @description
 */
 interface ITagTask : ITask<Boolean> {
    /**
     * 任务唯一标识
     */
    fun tag(): String

    /**
     * 等待处理结果
     */
    suspend fun await(): Boolean
}

