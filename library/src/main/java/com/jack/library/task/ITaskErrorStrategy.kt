package com.jack.library.task


/**
 * @author lingko
 * @version 1.0
 * @time 2022/01/23
 * @description
 */
 interface ITaskErrorStrategy {
    fun handleError(e: Throwable)
}