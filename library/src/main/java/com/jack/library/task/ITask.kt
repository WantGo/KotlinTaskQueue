package com.jack.library.task

/**
 * @author jack
 * @version 1.0
 * @time 2021/8/23
 */

 interface ITask<T> {
    suspend fun execute(): T
}
