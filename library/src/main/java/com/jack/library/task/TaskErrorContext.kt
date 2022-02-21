package com.jack.library.task


/**
 * @author jack
 * @version 1.0
 * @time 2022/01/23
 * @description  错误处理上下文
 */
 class TaskErrorContext(private var errorStrategy: ITaskErrorStrategy) {
    fun handleError(e: Throwable) {
        errorStrategy.handleError(e)
    }

    /**
     * 默认处理错误策略，暂时先上报，后面可能有其他方式，比如日志等
     */
    internal class DefaultTaskErrorStrategy : ITaskErrorStrategy {
        override fun handleError(e: Throwable) {
            loge("task", e.message)
        }
    }
}
