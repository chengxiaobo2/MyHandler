package com.example.myhandler3.handler

/**
 * looper
 *
 * @author chengxiaobo
 * @time 2019/5/6 23:35
 */
class MyLooper {

    private constructor()

    var queue: MyMessageQueue? = null

    companion object {

        private val threadlocal = ThreadLocal<MyLooper>()

        fun prepare() {
            val myLooper = MyLooper()
            threadlocal.set(myLooper)
            myLooper.queue = MyMessageQueue()

        }

        fun loop() {
            val queue = myLooper()?.queue
            while (true) {
                val myHandlerMessage = queue?.getMessage()
                myHandlerMessage?.myHandler?.handleMessage(myHandlerMessage.myMessage)
            }
        }

        fun myLooper(): MyLooper? {
            return threadlocal.get()
        }
    }
}