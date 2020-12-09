package pers.tower.questions;

/**
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/12/10 4:40
 */
public class Question1550 {

	// 给你一个整数数组 arr，请你判断数组中是否存在连续三个元素都是奇数的情况：如果存在，请返回 true ；否则，返回 false 。
	// 执行用时：0 ms, 在所有 Java 提交中击败了 100% 的用户
	// 内存消耗：38 MB, 在所有 Java 提交中击败了 86.71% 的用户

	public boolean threeConsecutiveOdds(int[] arr) {
		boolean lastSign = true;
		boolean lastSignState = false;
		if (arr.length >= 3) {
			int lastNum = arr.length - arr.length % 3 - 2;
			for (int i = 1; i <= lastNum; i += 3) {
				if (arr[i] % 2 == 1) {
					if (arr[i - 1] % 2 == 1) {
						if (lastSign) {
							if (lastSignState) {
								return true;
							}
						} else {
							if (arr[i - 2] % 2 == 1) {
								return true;
							}
						}
						lastSignState = arr[i + 1] % 2 == 1 ? true : false;
						if (lastSignState) {
							return true;
						}
						lastSign = true;
					}
				} else {
					lastSign = false;
				}
			}
			int count = 0;
			for (int i = arr.length - 3; i < arr.length; i++) {
				if (arr[i] % 2 == 1) {
					count++;
					if (count == 3) {
						return true;
					}
				} else {
					return false;
				}
			}
		}
		return false;
	}
}
