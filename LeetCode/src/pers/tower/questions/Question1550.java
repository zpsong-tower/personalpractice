package pers.tower.questions;

/**
 * 1550. 存在连续三个奇数的数组
 * 给你一个整数数组 arr，请你判断数组中是否存在连续三个元素都是奇数的情况：如果存在，请返回 true ；否则，返回 false 。
 *
 * 思路：三个数一组，只进行少量判断便可跳过多组数据，未跳过则保留上一组计算过的有效数据，尽量减少求余计算
 *
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/12/10 4:40
 */
public class Question1550 {
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
