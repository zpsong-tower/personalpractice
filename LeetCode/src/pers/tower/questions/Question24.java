package pers.tower.questions;

/**
 * 24. 两两交换链表中的节点
 * 给定一个链表，两两交换其中相邻的节点，并返回交换后的链表。
 * 你不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
 * 链表中节点的数目在范围 [0, 100] 内
 * 0 <= Node.val <= 100
 *
 * 思路：两个一组，交换他们的节点，矫正上一组连向本组的节点，之后判空，开始以同样的逻辑处理下一组
 *
 * @author zpsong-tower <pingzisong2012@gmail.com>
 * @since 2020/12/23 3:54
 */
public class Question24 {
	public ListNode swapPairs(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}
		ListNode newHead = head.next;
		ListNode first = head;
		ListNode second = head.next;
		ListNode previous = null;
		while (true) {
			first.next = second.next;
			second.next = first;
			if (previous != null) {
				previous.next = second;
			}
			if (first.next != null && first.next.next != null) {
				previous = first;
				first = first.next;
				second = first.next;
			} else {
				break;
			}
		}
		return newHead;
	}

	/**
	 * Definition for singly-linked list.
	 */
	private static class ListNode {
		int val;

		ListNode next;

		ListNode() {
		}

		ListNode(int val) {
			this.val = val;
		}

		ListNode(int val, ListNode next) {
			this.val = val;
			this.next = next;
		}
	}
}



