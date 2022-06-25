//package com.hao.mqttlibrary
//
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
//
///**
// * TODO
// * @date 2022/6/25
// * @author 锅得铁
// * @since v1.0
// */
//interface Element {
//    fun render(builder: StringBuilder, indent: String)
//}
//
//class TextElement(val text: String) : Element {
//    override fun render(builder: StringBuilder, indent: String) {
//        builder.append("$indent$text\n")
//    }
//}
//
//@DslMarker
//annotation class MQTTTagMarker
//
//@MQTTTagMarker
//abstract class Tag(val name: String) : Element {
//    val children = arrayListOf<Element>()
//    val attributes = hashMapOf<String, String>()
//
//    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
//        tag.init()
//        children.add(tag)
//        return tag
//    }
//
//    override fun render(builder: StringBuilder, indent: String) {
//        builder.append("$indent<$name${renderAttributes()}>\n")
//        for (c in children) {
//            c.render(builder, indent + "  ")
//        }
//        builder.append("$indent</$name>\n")
//    }
//
//    private fun renderAttributes(): String {
//        val builder = StringBuilder()
//        for ((attr, value) in attributes) {
//            builder.append(" $attr=\"$value\"")
//        }
//        return builder.toString()
//    }
//
//    override fun toString(): String {
//        val builder = StringBuilder()
//        render(builder, "")
//        return builder.toString()
//    }
//}
//
//abstract class TagWithText(name: String) : Tag(name) {
//    operator fun String.unaryPlus() {
//        children.add(TextElement(this))
//    }
//}
//
//class MQTT : TagWithText("html") {
//    fun broker(init: Broker.() -> Unit) = initTag(Broker(), init)
//
//    fun body(init: Body.() -> Unit) = initTag(Body(), init)
//}
//
//class Broker : TagWithText("head") {
//    fun title(init: Title.() -> Unit) = initTag(Title(), init)
//}
//
//class Title : TagWithText("title")
//
//abstract class BodyTag(name: String) : TagWithText(name) {
//    fun b(init: B.() -> Unit) = initTag(B(), init)
//    fun p(init: P.() -> Unit) = initTag(P(), init)
//    fun h1(init: H1.() -> Unit) = initTag(H1(), init)
//    fun a(href: String, init: A.() -> Unit) {
//        val a = initTag(A(), init)
//        a.href = href
//    }
//}
//
//class Body : BodyTag("body")
//class B : BodyTag("b")
//class P : BodyTag("p")
//class H1 : BodyTag("h1")
//
//class A : BodyTag("a") {
//    var href: String
//        get() = attributes["href"]!!
//        set(value) {
//            attributes["href"] = value
//        }
//}
//
//fun mqtt(init: MQTT.() -> Unit): MQTT {
//    val html = MQTT()
//    html.init()
//    return html
//}
//
//
//fun testa() {
//
//    mqtt {
//        broker {
//            title { +"XML encoding with Kotlin" }
//        }
//        body {
//            h1 { +"XML encoding with Kotlin" }
//            p { +"this format can be used as an alternative markup to XML" }
//
//            // 一个具有属性和文本内容的元素
//            a(href = "http://kotlinlang.org") { +"Kotlin" }
//
//            // 混合的内容
//            p {
//                +"This is some"
//                b { +"mixed" }
//                +"text. For more see the"
//                a(href = "http://kotlinlang.org") { +"Kotlin" }
//                +"project"
//            }
//            p { +"some text" }
//
//            // 以下代码生成的内容
//            p {
//                for (arg in args)
//                    +arg
//            }
//        }
//    }
//}