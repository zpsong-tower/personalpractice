package pers.tower;

import pers.tower.questions.*;

/**
 * 测试类，测试当前题目输出是否符合预期
 *
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/11/6 3:01
 */
public class Test {
	public static void main(String[] str) {
		Question746 q = new Question746();
		int[] cost1 = {10, 15, 20};
		int[] cost2 = {1, 100, 1, 1, 1, 100, 1, 1, 100, 1};
		System.out.println(q.minCostClimbingStairs(cost1)); // 15
		System.out.println(q.minCostClimbingStairs(cost2)); // 6
	}
}
