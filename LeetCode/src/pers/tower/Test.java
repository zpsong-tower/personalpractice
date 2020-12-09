package pers.tower;

import pers.tower.questions.*;

/**
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/11/6 3:01
 */
public class Test {
	public static void main(String[] str) {
		Question1550 q = new Question1550();
		int[] arr1 = {2, 6, 4, 1};
		int[] arr2 = {1, 2, 34, 3, 4, 5, 7, 23, 12};
		boolean a1 = q.threeConsecutiveOdds(arr1);
		boolean a2 = q.threeConsecutiveOdds(arr2);
		System.out.println(a1); // false
		System.out.println(a2); // true
	}
}
