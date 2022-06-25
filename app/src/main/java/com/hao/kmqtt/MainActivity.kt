package com.hao.kmqtt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //同步 自己调度线程
        Thread {
            mqttTest()
        }.start()

        setContentView(R.layout.activity_main)
        /**
         * TODO MQTT AndroidServer 支持到AndroidX 后再使用一下方案
         */
//        val aMqtt = AndroidMQTT()
//        aMqtt.connect(this)
//        aMqtt.publish("TEST", msg = "Hello", qos = 2)
//        aMqtt.subscribe("TEST")
    }

    fun mqttTest() {
        val subTopic = "TEST"
        val pubTopic = "TEST"
        val content = "Hello World"
        val qos = 2
        val broker = "tcp://hao88.cloud:1883"
        val clientId = "android 001"
        val persistence = MemoryPersistence()
        try {
            val client = MqttClient(broker, clientId, persistence)

            // MQTT 连接选项
            val connOpts = MqttConnectOptions()
            //connOpts.userName = "emqx_test"
            //connOpts.password = "emqx_test_password".toCharArray()
            // 保留会话
            connOpts.isCleanSession = true

            // 设置回调
            client.setCallback(object : MqttCallback {
                override fun connectionLost(e: Throwable) {
                    // 连接丢失后，一般在这里面进行重连
                    Log.d(TAG, "连接断开，可以做重连");
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "连接断开，可以做重连",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    e.printStackTrace()
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    // subscribe后得到的消息会执行到这里面
                    Log.d(
                        TAG,
                        "接收消息主题:$topic Qos: ${message!!.qos}接收消息内容: ${String(message.payload)}"
                    );
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            String(message.payload),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (String(message.payload) == "hello")
                        client.publish(topic, MqttMessage("Hi".toByteArray()))
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    Log.d(TAG, "deliveryComplete---------" + token!!.isComplete)
                }

            })

            // 建立连接
            Log.d(TAG, "Connecting to broker: $broker")
            client.connect(connOpts)
            Log.d(TAG, "Connected")
            Log.d(TAG, "Publishing message: $content")

            // 订阅
            client.subscribe(subTopic)

            // 消息发布所需参数
            val message = MqttMessage(content.toByteArray())

            //message.qos = qos
            //client.publish(pubTopic, message)
            Log.d(TAG, "Message published")
            //client.disconnect()
            Log.d(TAG, "Disconnected")
            //client.close()
            //System.exit(0)
        } catch (me: MqttException) {
            Log.d(TAG, "reason " + me.reasonCode)
            Log.d(TAG, "msg " + me.message)
            Log.d(TAG, "loc " + me.localizedMessage)
            Log.d(TAG, "cause " + me.cause)
            Log.d(TAG, "excep $me")
            me.printStackTrace()
        }
        Thread.sleep(100000)
    }
}