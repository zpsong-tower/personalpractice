package pers.tower.questions;

/**
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/11/6 3:11
 */
public class Question9 {

	// 判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
	// 你能不将整数转为字符串来解决这个问题吗？
	// 执行用时：9 ms, 在所有 Java 提交中击败了 99.46% 的用户
	// 内存消耗：37.7 MB, 在所有 Java 提交中击败了 92.71% 的用户

	final static int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, 0x7fffffff};

	final static int[] tenTable1 = {10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

	final static int[] tenTable2 = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000};

	public boolean isPalindrome(int x) {
		if (x < 0) {
			return false;
		}
		int tenNum = stringSize(x);
		int halfTenNum = tenNum / 2;
		int remTenNum = tenNum % 2;
		int result = 0;
		int mul = 0;
		int a;
		int b;
		if (remTenNum == 0) {
			a = x / tenTable2[halfTenNum];
		} else {
			a = x / tenTable1[halfTenNum];
		}
		b = x % tenTable2[halfTenNum];
		while (--halfTenNum >= 0) {
			result += b / tenTable2[halfTenNum] * tenTable2[mul++];
			b %= tenTable2[halfTenNum];
		}
		if (a != result) {
			return false;
		}
		return true;
	}

	static int stringSize(int x) {
		for (int i = 0; ; i++)
			if (x <= sizeTable[i]) return i + 1;
	}
}
