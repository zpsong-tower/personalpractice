package pers.tower.questions;

/**
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/12/11 1:28
 */
public class Question11 {

	/**
	 * 给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0) 。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
	 * 思路：for嵌套，大循环：向右移动左坐标，比之前出现的都高才进入小循环。小循环：向左移动右坐标，比之前出现的都高才计算面积并比较；当右坐标高度大于等于左坐标高度，结束当前小循环。
	 * @see Question11_1 优化为双指针对撞
	 */

	public int maxArea(int[] height) {
		int endX = height.length - 1;
		int leftMaxHeight = 0;
		int rightMaxHeight = 0;
		int result = 0;
		int area;
		for (int i = 0; i < endX; i++) {
			if (height[i] <= leftMaxHeight) {
				continue;
			}
			leftMaxHeight = height[i];
			for (int j = endX; j > i; j--) {
				if (height[j] > rightMaxHeight) {
					rightMaxHeight = height[j];
					if (leftMaxHeight > rightMaxHeight) {
						area = (j - i) * rightMaxHeight;
						if (area > result) {
							result = area;
						}
					} else {
						area = (j - i) * leftMaxHeight;
						if (area > result) {
							result = area;
						}
						break;
					}
				}
			}
			rightMaxHeight = 0;
		}
		return result;
	}
}
