package pers.tower.problems

/**
 * 9. 回文数
 * 判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 * 你能不将整数转为字符串来解决这个问题吗？
 *
 * 思路：频繁使用常量数组，分奇偶位数从中间截断，反转后半段进行比较
 *
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/11/6 3:11
 */
class Problem9 {
    // 尝试与数组内依次比大小而不是求余的方式算出数字位数
    private val mSizeTable = intArrayOf(9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, 0x7fffffff)

    fun isPalindrome(x: Int): Boolean {
        // 负数，不是回文数
        if (x < 0) {
            return false
        }

        // 数字位数
        val numSize = getNumSize(x)

        // 是否是偶数
        val isEven = numSize % 2 == 0






        return true
    }

    /**
     * 获取数字的位数
     */
    private fun getNumSize(x: Int): Int {
        mSizeTable.forEachIndexed { index, i ->
            if (x <= i) return index + 1
        }
        return 0
    }
}