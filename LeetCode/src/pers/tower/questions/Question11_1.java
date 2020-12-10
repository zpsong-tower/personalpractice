package pers.tower.questions;

/**
 * 11. 盛最多水的容器
 * 给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0) 。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 *
 * 思路：双指针对撞，只动高的坐标的话，容量只会变小，于是依次向内移动较低的坐标
 *
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/12/11 2:39
 */
public class Question11_1 {
	public int maxArea(int[] height) {
		int leftX = 0;
		int rightX = height.length - 1;
		int area;
		int result = 0;
		while (leftX < rightX) {
			if (height[leftX] > height[rightX]) {
				area = (rightX - leftX) * height[rightX];
				rightX--;
			} else {
				area = (rightX - leftX) * height[leftX];
				leftX++;
			}
			if (area > result) {
				result = area;
			}
		}
		return result;
	}
}
