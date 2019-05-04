package com.example.myhandler1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.handler1.handler.MyHandler
import com.example.handler1.handler.MyMessage
import com.example.handler1.handler.MyMessageQueue
import com.example.myhandler1.util.loge
import kotlinx.android.synthetic.main.activity_my_handler1.*

/**
 * MyHandler1Activity
 *
 * @author chengxiaobo
 * @time 2019/5/2 09:30
 */
class MyHandler1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_handler1)

        //1.创建消息队列
        val myMessageQueue = MyMessageQueue()
        //2.创建MyHandler，处理消息
        val myHandler = object : MyHandler() {
            override fun handleMessage(message: MyMessage) {
                loge("deal message ${message.WHAT}")
                Thread.sleep(1000)
            }
        }
        //3.线程1，读取消息队列的消息，交给handler处理。
        val myThread1 = Thread(Runnable {
            while (true) {
                myHandler.handleMessage(myMessageQueue.getMessage())
            }
        })
        myThread1.start()

        //4.点击一次，主线程添加10条消息到消息队列
        var i = 0
        btnAddMessage.setOnClickListener {
            for (j in 1..10) {
                loge("add message ${(i * 10) + j}")
                myMessageQueue.addMessage(MyMessage((i * 10) + j))
            }
            i++
        }
    }
}