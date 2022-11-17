package pers.tower

import pers.tower.questions.*

/**
 * 测试类，测试当前题目输出是否符合预期
 *
 * @author zpsong-tower <pingzisong2012></pingzisong2012>@gmail.com>
 * @since 2020/11/6 3:01
 */
object Test {
    @JvmStatic
    fun main(str: Array<String>) {
        val q = Question316()
        println(q.removeDuplicateLetters("bcabc")) // abc
        println(q.removeDuplicateLetters("cbacdcbc")) // acdb
    }
}