/**
 * LeetCode 621. 任务调度器
 * 功能：计算完成所有任务的最短时间
 */
public class TaskScheduler {

    // 核心算法方法
    public int leastInterval(char[] tasks, int n) {
        // 统计26个大写字母的出现次数
        int[] cnt = new int[26];
        for (char c : tasks) {
            cnt[c - 'A']++;
        }

        // 找到出现次数最多的任务
        int maxCount = 0;
        for (int x : cnt) {
            maxCount = Math.max(maxCount, x);
        }

        // 统计有多少个任务达到了最大次数
        int maxKinds = 0;
        for (int x : cnt) {
            if (x == maxCount) {
                maxKinds++;
            }
        }

        // 计算最短时间：公式计算结果 和 任务总数 取最大值
        return Math.max(tasks.length, (maxCount - 1) * (n + 1) + maxKinds);
    }

    // 主方法：本地测试运行入口
    public static void main(String[] args) {
        TaskScheduler solution = new TaskScheduler();

        // ==================== 测试用例1 ====================
        char[] tasks1 = {'A','C','A','B','D','B'};
        int n1 = 1;
        System.out.println("测试用例1 输出：" + solution.leastInterval(tasks1, n1));
        // 预期输出：6

        // ==================== 测试用例2 ====================
        char[] tasks2 = {'A','A','A','B','B','B'};
        int n2 = 2;
        System.out.println("测试用例2 输出：" + solution.leastInterval(tasks2, n2));
        // 预期输出：8
    }
}