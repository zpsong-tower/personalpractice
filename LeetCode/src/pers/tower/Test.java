package pers.tower;

import pers.tower.questions.*;

/**
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/11/6 3:01
 */
public class Test {
	public static void main(String[] str) {
		Question11_1 q = new Question11_1();
		int[] arr0 = {1, 8, 6, 2, 5, 4, 8, 3, 7};
		int[] arr1 = {1, 1};
		int[] arr2 = {4, 3, 2, 1, 4};
		int[] arr3 = {1, 2, 1};
		System.out.println(q.maxArea(arr0)); // 49
		System.out.println(q.maxArea(arr1)); // 1
		System.out.println(q.maxArea(arr2)); // 16
		System.out.println(q.maxArea(arr3)); // 2
	}
}
