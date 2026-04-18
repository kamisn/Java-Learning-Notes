package main.java.phase03.day20260418;

/**
 * 206. 反转链表 - 迭代法 独立可运行 Demo
 * 三指针：prev、cur、next
 */
public class ReverseList_Iteration {

    // 链表节点定义
    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    // ===================== 迭代法核心代码 =====================
    public ListNode reverseList(ListNode head) {
        // prev：已经反转好的链表头，初始为 null
        ListNode prev = null;
        // cur：当前正在处理的节点
        ListNode cur = head;

        while (cur != null) {
            // 1. 先保存下一个节点，防止断链
            ListNode next = cur.next;

            // 2. 核心：反转当前节点的指针 → 指向已反转部分
            cur.next = prev;

            // 3. prev 前进：当前节点变成已反转部分的新头部
            prev = cur;

            // 4. cur 前进：处理下一个节点
            cur = next;
        }

        // 最终 prev 就是反转后的新头节点
        return prev;
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

        ReverseList_Iteration solution = new ReverseList_Iteration();

        System.out.println("原链表：");
        printList(head);

        ListNode newHead = solution.reverseList(head);
        System.out.println("迭代法反转后：");
        printList(newHead);
    }
}