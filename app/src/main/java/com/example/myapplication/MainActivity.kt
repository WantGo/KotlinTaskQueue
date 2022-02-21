package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jack.library.task.DownloadTask
import com.jack.library.task.ITagTask
import com.jack.library.task.TaskQueue
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private val taskQueue = TaskQueue()
    private var index = 0
    private val scope = CoroutineScope(SupervisorJob())
    var sb = StringBuilder()

    private suspend fun addTask(): ITagTask {
        val task = DownloadTask("task${++index}")
        taskQueue.enqueue(task)
        return task
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        radio_group.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.radio_1 -> {
                    taskQueue.isAsync = false
                }
                R.id.radio_2 -> {
                    taskQueue.isAsync = true
                }
            }
        }
        add_task.setOnClickListener {
            scope.launch {
                val task = addTask()
                sb.append("${task.tag()} 执行结果:${task.await()}\n")
                withContext(Dispatchers.Main) {
                    log_list.text = sb.toString()
                }
            }
        }
    }
}