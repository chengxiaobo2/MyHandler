package com.example.myhandler3.handler

import com.example.myhandler2.util.loge
import java.util.*

/**
 * 消息队列
 *
 * @author chengxiaobo
 * @time 2019/5/6 12:31
 */
class MyMessageQueue {

    private val messageList = LinkedList<MyHandlerMessage>()
    private val condition = Object()

    /**
     * 主线程添加消息，如果添加消息之前是空队列，通知挂起的线程
     */
    fun addMessage(message: MyHandlerMessage) {
        synchronized(condition) {
            messageList.add(message)
            if (messageList.size == 1) {
                condition.notify()
            }
        }
    }

    /**
     * 线程1 读取消息，如果消息队列没有消息，则线程挂起
     */
    fun getMessage(): MyHandlerMessage {
        synchronized(condition) {
            if (messageList.size == 0) {
                loge("线程挂起")
                condition.wait()
            }
            return messageList.removeAt(0)
        }
    }
}