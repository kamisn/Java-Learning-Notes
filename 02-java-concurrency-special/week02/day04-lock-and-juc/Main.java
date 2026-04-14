// 1. 二叉树节点类（固定写法，不用改）
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

// 2. 解题类（你的验证BST逻辑，完全正确）
class Solution {
    public boolean isValidBST(TreeNode root) {
        // 用 Long 避免 int 溢出（关键！）
        return dfs(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    // 递归：每个节点必须在 [min, max] 范围内
    private boolean dfs(TreeNode node, long min, long max) {
        // 空节点：合法，直接返回 true
        if (node == null) {
            return true;
        }

        // 不满足 BST 规则：节点值超出 [min, max] 范围
        if (node.val <= min || node.val >= max) {
            return false;
        }

        // 递归验证左右子树：
        // 左子树：最大值 = 当前节点值（左子树所有节点必须 < 根）
        // 右子树：最小值 = 当前节点值（右子树所有节点必须 > 根）
        return dfs(node.left, min, node.val) && dfs(node.right, node.val, max);
    }
}

// 3. 测试主类（程序入口！必须有这个才能运行）
public class Main {
    public static void main(String[] args) {
        // ------------ 测试用例 1：有效的二叉搜索树 ------------
        // 树结构：
        //      2
        //     / \
        //    1   3
        TreeNode validRoot = new TreeNode(2);
        validRoot.left = new TreeNode(1);
        validRoot.right = new TreeNode(3);

        Solution solution = new Solution();
        boolean isValid1 = solution.isValidBST(validRoot);
        System.out.println("测试用例1（有效BST）：" + isValid1); // 预期输出：true


        // ------------ 测试用例 2：无效的二叉搜索树 ------------
        // 树结构：
        //      5
        //     / \
        //    1   4
        //       / \
        //      3   6
        // （4的左子树是3，3<4但3<5，违反BST规则）
        TreeNode invalidRoot = new TreeNode(5);
        invalidRoot.left = new TreeNode(1);
        invalidRoot.right = new TreeNode(4);
        invalidRoot.right.left = new TreeNode(3);
        invalidRoot.right.right = new TreeNode(6);

        boolean isValid2 = solution.isValidBST(invalidRoot);
        System.out.println("测试用例2（无效BST）：" + isValid2); // 预期输出：false
    }
}