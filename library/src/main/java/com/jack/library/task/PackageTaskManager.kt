package com.jack.library.task


/**
 * @author jack
 * @version 1.0
 * @time 2021/12/27
 * @description taskmanager 的默认实现类
 */
internal object PackageTaskManager : TaskManager<ITagTask> {
    /**
     * 任务容器
     */
    private val taskMap = mutableMapOf<String, ITagTask>()
    override fun addTask(task: ITagTask): Boolean {
        if (!hasTak(task.tag())) {
            taskMap[task.tag()] = task
            return true
        }
        return false
    }

    override fun removeTask(tag: String) {
        taskMap.remove(tag)
    }

    override fun hasTak(tag: String): Boolean {
        return taskMap.contains(tag)
    }

    override fun getTask(tag: String): ITagTask? {
        return taskMap[tag]
    }

    override fun getTaskSize(): Int {
        return taskMap.size
    }

    override fun clear() {
        taskMap.clear()
    }

    override fun getTaskList(): Collection<ITagTask> {
        return taskMap.values
    }
}

