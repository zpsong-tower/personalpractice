package pers.tower.questions;

import java.util.Arrays;

/**
 * 316. 去除重复字母
 * 给你一个字符串 s ，请你去除字符串中重复的字母，使得每个字母只出现一次。需保证 返回结果的字典序最小（要求不能打乱其他字符的相对位置）。
 * 1 <= s.length <= 104
 * s 由小写英文字母组成
 *
 * 思路：数组入栈，每个字母入栈前与栈顶的比大小，比栈顶小则移除栈顶，比栈顶大或该字母num消耗完或空栈则进行下一轮入栈，若栈内已存在该字母则直接消耗num进行下一轮入栈。
 *
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2021/5/26 12:00
 */
public class Question316 {
	public String removeDuplicateLetters(String s) {
		int[] num = new int[26];
		boolean[] exist = new boolean[26];
		char[] chars = s.toCharArray();
		for (char c : chars) {
			num[c - 'a']++;
		}
		MyStack stack = new MyStack(chars[0]);
		exist[chars[0] - 'a'] = true;
		num[chars[0] - 'a']--;
		int letterIndex;

		// 核心逻辑
		for (int i = 1; i < chars.length; i++) {
			letterIndex = chars[i] - 'a';
			if (!exist[letterIndex]) {
				while (stack != null && stack.value > chars[i] && num[stack.value - 'a'] > 0) {
					exist[stack.value - 'a'] = false;
					stack = stack.pop();
				}
				if (stack == null) {
					stack = new MyStack(chars[i]);
				} else {
					stack = stack.push(chars[i]);
				}
				exist[letterIndex] = true;
			}
			num[letterIndex]--;
		}

		// 输出
		chars = new char[26];
		int i;
		for (i = 25; stack != null; i--) {
			chars[i] = stack.value;
			stack = stack.previous;
		}
		chars = Arrays.copyOfRange(chars, i + 1, chars.length);
		return new String(chars);
	}

	private static class MyStack {
		private MyStack previous;

		private char value;

		private MyStack(char value) {
			this(null, value);
		}

		private MyStack(MyStack previous, char value) {
			this.previous = previous;
			this.value = value;
		}

		private MyStack push(char c) {
			return new MyStack(this, c);
		}

		private MyStack pop() {
			MyStack temp = previous;
			previous = null;
			return temp;
		}
	}
}
