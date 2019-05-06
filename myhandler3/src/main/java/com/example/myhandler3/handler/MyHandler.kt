package com.example.myhandler3.handler

/**
 * 处理消息
 *
 * @author chengxiaobo
 * @time 2019/5/6 13:32
 */
abstract class MyHandler(private val myLooper: MyLooper?) {
    abstract fun handleMessage(message: MyMessage)

    //往消息队列里面添加消息
    fun sendMessage(message: MyMessage) {
        myLooper?.queue?.addMessage(MyHandlerMessage(message, this))
    }
}