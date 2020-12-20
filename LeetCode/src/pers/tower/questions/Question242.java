package pers.tower.questions;

/**
 * 242. 有效的字母异位词
 * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
 * 你可以假设字符串只包含小写字母。
 *
 * 思路：只包含小写字母，用长度26的数组表示每个字母出现个数。循环时，第一个字符串每次在对应的字母个数上加一，第二个字符串减一，结束循环时判断数组是否都为0
 *
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/12/20 17:16
 */
public class Question242 {
	public boolean isAnagram(String s, String t) {
		if (s.length() != t.length()) {
			return false;
		}
		int[] letters = new int[26];
		char[] ss = s.toCharArray();
		char[] tt = t.toCharArray();
		for (int i = 0; i < ss.length; i++) {
			letters[ss[i] - 'a']++;
			letters[tt[i] - 'a']--;
		}
		for (int i = 0; i < 26; i++) {
			if (letters[i] != 0) {
				return false;
			}
		}
		return true;
	}
}
