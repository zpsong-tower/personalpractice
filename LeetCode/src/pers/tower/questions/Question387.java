package pers.tower.questions;

/**
 * 387. 字符串中的第一个唯一字符
 * 给定一个字符串，找到它的第一个不重复的字符，并返回它的索引。如果不存在，则返回 -1。
 * 你可以假定该字符串只包含小写字母。
 *
 * 思路：只包含小写字母，用一个长度26的数组表示每个字母出现个数，用另一个长度26的数组表示每个字母第一次出现的索引
 *
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/12/23 1:22
 */
public class Question387 {
	public int firstUniqChar(String s) {
		int[] letters = new int[26];
		int[] index = new int[26];
		char[] charArray = s.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			int temp = charArray[i] - 'a';
			if (letters[temp] == 0) {
				index[temp] = i;
			}
			letters[temp]++;
		}
		int result = charArray.length;
		for (int i = 0; i < 26; i++) {
			if (letters[i] == 1 && index[i] < result) {
				result = index[i];
			}
		}
		if (result != charArray.length) {
			return result;
		}
		return -1;
	}
}
