package com.example.myhandler2.handler

/**
 * looper
 *
 * @author chengxiaobo
 * @time 2019/5/6 13:35
 */
class MyLooper {

    var queue = MyMessageQueue()

    fun loop() {
        while (true) {
            val myHandlerMessage = queue.getMessage()
            myHandlerMessage.myHandler.handleMessage(myHandlerMessage.myMessage)
        }
    }
}