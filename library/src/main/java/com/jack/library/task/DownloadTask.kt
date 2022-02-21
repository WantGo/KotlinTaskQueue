package com.jack.library.task

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * @author lingko
 * @version 1.0
 * @time 2022/02/20
 * @description 模拟一个下载任务
 */
class DownloadTask(var tag: String) : ITagTask {
    private var deferred = CompletableDeferred<Boolean>()
    override fun tag(): String {
        return tag
    }

    override suspend fun await(): Boolean {
        return deferred.await()
    }

    override suspend fun execute(): Boolean {
        delay(Random.nextInt(10) * 1000L + 1000L)
        val downloadResult = Random.nextBoolean()//随机生成一个下载任务结果
        deferred.complete(downloadResult)
        return downloadResult
    }

}