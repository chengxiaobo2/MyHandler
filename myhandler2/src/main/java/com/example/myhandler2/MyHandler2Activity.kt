package com.example.myhandler2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.myhandler2.handler.MyHandler
import com.example.myhandler2.handler.MyLooper
import com.example.myhandler2.handler.MyMessage
import com.example.myhandler2.util.loge
import kotlinx.android.synthetic.main.activity_my_handler2.*

/**
 * @author chengxiaobo
 * @time 2019/5/5 16:00
 */
class MyHandler2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_handler2)

        //1.创建looper
        var myLooper: MyLooper? = null
        val myThread1 = Thread(Runnable {
            myLooper = MyLooper()
            myLooper?.loop()
        })
        myThread1.start()

        //主线程睡眠1s钟，保证子线程的myLooper创建成功
        //myLooper在子线程创建，handler在主线程创建，handler创建需要looper，handler创建的时候，需要保证looper已经创建完
        //这里不去写了，会分析一下 HandlerThread这块是怎么处理的。
        Thread.sleep(1000)

        //2.创建MyHandler，处理消息
        val myHandler = object : MyHandler(myLooper) {
            override fun handleMessage(message: MyMessage) {
                loge("deal message ${message.WHAT}")
                Thread.sleep(1000)
            }
        }
        //3.点击一次，主线程添加10条消息到消息队列
        var i = 0
        btnAddMessage.setOnClickListener {
            for (j in 1..10) {
                loge("add message ${(i * 10) + j}")
                myHandler.sendMessage(MyMessage((i * 10) + j))
            }
            i++
        }
    }
}
