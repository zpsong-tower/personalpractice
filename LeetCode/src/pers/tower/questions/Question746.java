package pers.tower.questions;

/**
 * 746. 使用最小花费爬楼梯
 * 数组的每个索引作为一个阶梯，第 i个阶梯对应着一个非负数的体力花费值 cost[i](索引从0开始)。
 * 每当你爬上一个阶梯你都要花费对应的体力花费值，然后你可以选择继续爬一个阶梯或者爬两个阶梯。
 * 您需要找到达到楼层顶部的最低花费。在开始时，你可以选择从索引为 0 或 1 的元素作为初始阶梯。
 * cost 的长度将会在 [2, 1000]。
 * 每一个 cost[i] 将会是一个Integer类型，范围为 [0, 999]。
 *
 * 思路：动态规划，阶梯 i 只有阶梯 i - 1 和阶梯 i - 2 能到达，只考虑到达 i - 1 和 i - 2 的体力花费即可
 *
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/12/23 1:00
 */
public class Question746 {
	public int minCostClimbingStairs(int[] cost) {
		int cl = cost.length;
		int a = cost[0];
		int b = cost[1];
		for (int i = 2; i < cl - 1; i += 2) {
			a = a < b ? cost[i] + a : cost[i] + b;
			b = b < a ? cost[i + 1] + b : cost[i + 1] + a;
		}
		if (cl % 2 == 1) {
			a += cost[cl - 1];
		}
		return a < b ? a : b;
	}
}
