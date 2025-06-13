package com.lollipop.countdown.helper

import android.graphics.Color
import java.security.MessageDigest
import kotlin.math.max
import kotlin.math.min

/**
 * 一个字符串转颜色的工具类
 * @author Lollipop
 */
object GenerateColorHelper {

    fun generate(text: String, alphaLimit: Boolean): Int {
        return try {
            val md5 = text.toByteArray().md5().toHexString()
            val red = md5.substring(0, 8).toLong(16).getColor()
            val green = md5.substring(8, 16).toLong(16).getColor()
            val blue = md5.substring(16, 24).toLong(16).getColor()
            var alpha = md5.substring(24, 31).toLong(16).getColor()
            if (alphaLimit) {
                alpha = alpha % 128 + 128
            }
            Color.argb(alpha, red, green, blue)
        } catch (e: Throwable) {
            Color.WHITE
        }
    }

    fun generate(text: String, alphaLimit: Float = 1F): Int {
        try {
            val longArray = text.toByteArray().md5().toLongArray()
            val color = when (longArray.size) {
                0 -> {
                    return Color.TRANSPARENT
                }

                1 -> {
                    val long = longArray[0]
                    Color.argb(
                        long.shr(48).getColor(),
                        long.shr(32).getColor(),
                        long.shr(16).getColor(),
                        long.getColor()
                    )
                }

                2 -> {
                    val long1 = longArray[0]
                    val long2 = longArray[1]
                    Color.argb(
                        long1.shr(32).getColor(),
                        long1.getColor(),
                        long2.shr(32).getColor(),
                        long2.getColor(),
                    )
                }

                3 -> {
                    Color.argb(
                        255,
                        longArray[0].getColor(),
                        longArray[1].getColor(),
                        longArray[2].getColor()
                    )
                }

                else -> {
                    Color.argb(
                        longArray[0].getColor(),
                        longArray[1].getColor(),
                        longArray[2].getColor(),
                        longArray[3].getColor()
                    )
                }
            }
            val alpha = min(max((255 * alphaLimit).toInt(), 0), 255)
            return color.and(0xFFFFFF).or(alpha.shl(24))
        } catch (e: Throwable) {
            return Color.WHITE
        }
    }

    private fun Long.getColor(): Int {
        val intValue = this.toInt()
        return intValue % 256
    }

    //把字节数组转换成md5
    private fun ByteArray.md5(): ByteArray {
        val input = this
        //创建一个提供信息摘要算法的对象，初始化为md5算法对象
        val md = MessageDigest.getInstance("MD5")
        //计算后获得字节数组
        val buff = md.digest(input)
        //把数组每一字节换成16进制连成md5字符串
        return buff
    }

    //把字节数组转成16进位制数
    private fun ByteArray.toHexString(): String {
        val bytes = this
        val md5str = StringBuffer()
        //把数组每一字节换成16进制连成md5字符串
        var digital: Int
        for (i in bytes.indices) {
            digital = bytes[i].toInt()
            if (digital < 0) {
                digital += 256
            }
            if (digital < 16) {
                md5str.append("0")
            }
            md5str.append(Integer.toHexString(digital))
        }
        return md5str.toString()
    }

    private fun ByteArray.toLong(): Long {
        val bytes = this
        var result = 0L
        for (byte in bytes) {
            var byteValue = byte.toLong()
            if (byteValue < 0) {
                byteValue += 256
            }
            byteValue = byteValue.and(0xFF)
            result = result.shl(8).or(byteValue)
        }
        return result
    }

    private fun ByteArray.toLongArray(): LongArray {
        val bytes = this
        val longArray = LongArray(bytes.size / 8)
        val max = bytes.size
        for (i in bytes.indices step 8) {
            val long = bytes.sliceArray(i until min(max, i + 8)).toLong()
            longArray[i / 8] = long
        }
        return longArray
    }

}