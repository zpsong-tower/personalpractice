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
		Question387 q = new Question387();
		System.out.println(q.firstUniqChar("leetcode")); // 0
		System.out.println(q.firstUniqChar("loveleetcode")); // 2
	}
}
