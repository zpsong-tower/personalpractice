package pers.tower;

import pers.tower.questions.*;

/**
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/11/6 3:01
 */
public class Test {
	public static void main(String[] stra) {
		Question9 test = new Question9();
		System.out.println(test.isPalindrome(49694)); // false
		System.out.println(test.isPalindrome(0)); // true
		System.out.println(test.isPalindrome(4)); // true
		System.out.println(test.isPalindrome(2332)); // true
		System.out.println(test.isPalindrome(92366329)); // true
		System.out.println(test.isPalindrome(10000000)); // false
		System.out.println(test.isPalindrome(1231)); // false
		System.out.println(test.isPalindrome(12312)); // false
		System.out.println(test.isPalindrome(-12321)); // false
		System.out.println(test.isPalindrome(-1)); // false
	}
}
