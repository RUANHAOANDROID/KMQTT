package com.hao.mqttlibrary

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.hao.mqttlibrary.test", appContext.packageName)
    }

    @Test
    fun mqttTest() {
        val subTopic = "TEST"
        val pubTopic = "TEST"
        val content = "Hello World"
        val qos = 2
        val broker = "tcp://hao88.cloud:1883"
        val clientId = "001"
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
                override fun connectionLost(cause: Throwable?) {
                    // 连接丢失后，一般在这里面进行重连
                    println("连接断开，可以做重连");
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    // subscribe后得到的消息会执行到这里面
                    println("接收消息主题:" + topic);
                    println("接收消息Qos:" + message!!.qos)
                    println("接收消息内容:" + message!!.payload)
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    println("deliveryComplete---------" + token!!.isComplete)
                }

            })

            // 建立连接
            println("Connecting to broker: $broker")
            client.connect(connOpts)
            println("Connected")
            println("Publishing message: $content")

            // 订阅
            client.subscribe(subTopic)

            // 消息发布所需参数
            val message = MqttMessage(content.toByteArray())
            message.qos = qos
            client.publish(pubTopic, message)
            println("Message published")
            client.disconnect()
            println("Disconnected")
            client.close()
            System.exit(0)
        } catch (me: MqttException) {
            println("reason " + me.reasonCode)
            println("msg " + me.message)
            println("loc " + me.localizedMessage)
            println("cause " + me.cause)
            println("excep $me")
            me.printStackTrace()
        }
        Thread.sleep(10000)
    }
}