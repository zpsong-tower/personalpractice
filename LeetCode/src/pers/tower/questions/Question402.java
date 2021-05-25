package pers.tower.questions;

import java.util.Arrays;
import java.util.Stack;

/**
 * 402. 移掉K位数字
 * 给定一个以字符串表示的非负整数 num，移除这个数中的 k 位数字，使得剩下的数字最小。
 * num 的长度小于 10002 且 ≥ k。
 * num 不会包含任何前导零。
 *
 * 思路：数组入栈，每个数字入栈前与栈顶的比大小，比栈顶小则移除栈顶，比栈顶大或k消耗完或空栈则进行下一轮入栈，所有入栈结束后若k有剩余则进行剩余k次出栈操作。（需要考虑前导0的问题）
 *
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2021/5/25 1:38
 */
public class Question402 {
	public String removeKdigits(String num, int k) {
		if (num.length() == k) {
			return "0";
		}
		if (k == 0) {
			return num;
		}
		char[] chars = num.toCharArray();
		int lastZero = -1;
		int zeroNum = 0;
		int newK = k + 1;
		for (int i = 0; i < newK; i++) {
			if (i == chars.length) {
				return "0";
			}
			if (chars[i] == '0') {
				newK++;
				lastZero = i;
				zeroNum++;
			}
		}
		if (lastZero != -1) {
			int kNum = k - lastZero - 1 + zeroNum;
			char[] newChars = Arrays.copyOfRange(chars, lastZero + 1, chars.length);
			if (kNum == 0) {
				return new String(newChars);
			}
			return handle(newChars, kNum);
		}
		return handle(chars, k);
	}

	private String handle(char[] chars, int k) {
		Stack stack = new Stack();
		for (int i = 0; i < chars.length; i++) {
			while (stack.size() != 0 && k > 0) {
				if ((char) stack.peek() > chars[i]) {
					stack.pop();
					k--;
				} else {
					break;
				}
			}
			stack.push(chars[i]);
		}
		while (k > 0) {
			stack.pop();
			k--;
		}
		char[] returnChars = new char[stack.size()];
		for (int i = stack.size() - 1; i >= 0; i--) {
			returnChars[i] = (char) stack.peek();
			stack.pop();
		}
		return new String(returnChars);
	}
}
