package main.java.phase03.day20260418;

/**
 * 206. 反转链表 - 递归法 独立可运行 Demo
 * 思路：先递归到尾部，回溯时再反转指针
 */
public class ReverseList_Recursion {

    // 链表节点定义
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    // ===================== 递归法核心代码 =====================
    public ListNode reverseList(ListNode head) {
        // 递归终止条件
        // 空链表 或 只剩一个节点，直接返回
        if (head == null || head.next == null) {
            return head;
        }

        // 1. 先递归到链表最后一个节点（新头节点）
        ListNode newHead = reverseList(head.next);

        // 2. 回溯时反转指针：让下一个节点指向自己
        head.next.next = head;

        // 3. 断开旧指针，防止成环
        head.next = null;

        // 4. 全程返回真正的新头节点
        return newHead;
    }
    // ==========================================================

    // 打印链表
    public static void printList(ListNode head) {
        ListNode cur = head;
        while (cur != null) {
            System.out.print(cur.val + " -> ");
            cur = cur.next;
        }
        System.out.println("null");
    }

    // 测试主函数
    public static void main(String[] args) {
        // 构建链表 1->2->3->4->5
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);

        ReverseList_Recursion solution = new ReverseList_Recursion();

        System.out.println("原链表：");
        printList(head);

        ListNode newHead = solution.reverseList(head);
        System.out.println("递归法反转后：");
        printList(newHead);
    }
}