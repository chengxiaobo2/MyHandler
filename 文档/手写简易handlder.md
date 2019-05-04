<font size=4 color=#D2691E> 手写简易handler 2019年05月02日 </font>

### 1.谈谈我对Handler的理解
Handler是为了解决线程间通讯。一个线程2发送消息给另外一个线程1，线程1拿到消息后，执行对应的操作。<br>
<img src="pic/handler_1.png" width =450><br>
就会有一个问题，线程2发给线程1消息的时候，线程1正在执行任务，不能立刻执行线程2新发来的消息对应的任务，怎么办呢？就需要将消息存起来。<br>
<img src="pic/handler_2.png" width =450><br>
对应的实现就是，有一个容器去盛放这些消息，可以往这个容器中添加消息，有个循环不断的从容器中取消息，执行消息对应的任务。

### 2.第一步，要写一个消息处理模型（模拟添加消息，读取消息，处理消息的过程）
```java
1.有个容器盛放消息，有消息时，循环读消息执行任务，没消息时循环停止。
```
### Moudle - myhandler1 的实现为：
点击"添加消息"主线程往消息队列添加10条消息，线程1读消息，交给handler处理。处理完消息以后，线程1挂起，主线程添加消息后，notify挂起的线程1。<br>
myhanlder1 主要就是为了模拟添加消息，读取消息，处理消息的过程。
代码见 Moudle - myhandler1


MyMessage
```java
/**
 * 消息
 *
 * @author chengxiaobo
 * @time 2019/5/2 08:30
 */
class MyMessage(val WHAT: Int)
```
MyMessageQueue

```java
/**
 * 消息队列
 *
 * @author chengxiaobo
 * @time 2019/5/2 08:31
 */
class MyMessageQueue {

    private val messageList = LinkedList<MyMessage>()
    private val condition = Object()

    /**
     * 主线程添加消息，如果添加消息之前是空队列，通知挂起的线程
     */
    fun addMessage(message: MyMessage) {
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
    fun getMessage(): MyMessage {
        synchronized(condition) {
            if (messageList.size == 0) {
                loge("线程挂起")
                condition.wait()
            }
            return messageList.removeAt(0)
        }
    }
}
```
MyHandler

```java
/**
 * 处理消息
 *
 * @author chengxiaobo
 * @time 2019/5/2 08:32
 */
abstract class MyHandler {
    abstract fun handleMessage(message: MyMessage)
}
```
MyHandler1Activity  

```java
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
```
运行结果

```java
2019-02-14 16:31:41.065 30033-30727/com.example.myhandler1 E/myHandler1: 线程挂起
2019-02-14 16:31:42.274 30033-30033/com.example.myhandler1 E/myHandler1: add message 1
2019-02-14 16:31:42.274 30033-30033/com.example.myhandler1 E/myHandler1: add message 2
2019-02-14 16:31:42.275 30033-30033/com.example.myhandler1 E/myHandler1: add message 3
2019-02-14 16:31:42.275 30033-30727/com.example.myhandler1 E/myHandler1: deal message 1
2019-02-14 16:31:42.275 30033-30033/com.example.myhandler1 E/myHandler1: add message 4
2019-02-14 16:31:42.275 30033-30033/com.example.myhandler1 E/myHandler1: add message 5
2019-02-14 16:31:42.275 30033-30033/com.example.myhandler1 E/myHandler1: add message 6
2019-02-14 16:31:42.275 30033-30033/com.example.myhandler1 E/myHandler1: add message 7
2019-02-14 16:31:42.275 30033-30033/com.example.myhandler1 E/myHandler1: add message 8
2019-02-14 16:31:42.275 30033-30033/com.example.myhandler1 E/myHandler1: add message 9
2019-02-14 16:31:42.275 30033-30033/com.example.myhandler1 E/myHandler1: add message 10
2019-02-14 16:31:43.276 30033-30727/com.example.myhandler1 E/myHandler1: deal message 2
2019-02-14 16:31:44.278 30033-30727/com.example.myhandler1 E/myHandler1: deal message 3
2019-02-14 16:31:45.279 30033-30727/com.example.myhandler1 E/myHandler1: deal message 4
2019-02-14 16:31:46.281 30033-30727/com.example.myhandler1 E/myHandler1: deal message 5
2019-02-14 16:31:47.284 30033-30727/com.example.myhandler1 E/myHandler1: deal message 6
2019-02-14 16:31:48.285 30033-30727/com.example.myhandler1 E/myHandler1: deal message 7
2019-02-14 16:31:49.287 30033-30727/com.example.myhandler1 E/myHandler1: deal message 8
2019-02-14 16:31:50.289 30033-30727/com.example.myhandler1 E/myHandler1: deal message 9
2019-02-14 16:31:51.291 30033-30727/com.example.myhandler1 E/myHandler1: deal message 10
2019-02-14 16:31:52.293 30033-30727/com.example.myhandler1 E/myHandler1: 线程挂起
```