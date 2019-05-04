package com.example.handler1.handler

/**
 * 处理消息
 *
 * @author chengxiaobo
 * @time 2019/5/2 08:32
 */
abstract class MyHandler {
    abstract fun handleMessage(message: MyMessage)
}