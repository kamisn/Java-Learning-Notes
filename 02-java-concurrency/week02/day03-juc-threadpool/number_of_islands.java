public class number_of_islands {
    class Solution {
        public int numIslands(char[][] grid) {
            // 防御性处理：如果网格为空，直接返回 0
            if (grid == null || grid.length == 0) {
                return 0;
            }

            int count = 0;
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (grid[i][j] == '1') {
                        count++;
                        dfs(grid, i, j);
                    }
                }
            }
            return count;
        }

        void dfs(char[][] grid, int i, int j) {
            // 边界检查或当前格子不是岛屿时直接返回
            if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] != '1') {
                return;
            }

            grid[i][j] = '0'; // 标记为已访问（淹没岛屿）

            // 向四个方向递归探索
            dfs(grid, i + 1, j);
            dfs(grid, i - 1, j);
            dfs(grid, i, j + 1);
            dfs(grid, i, j - 1);
        }
    }

    public static void main(String[] args) {
        // 创建外部类实例，再通过它创建内部类 Solution 的实例
        number_of_islands outer = new number_of_islands();
        Solution solution = outer.new Solution();

        // 测试用例 1：一个岛屿
        char[][] grid1 = {
                {'1','1','1','1','0'},
                {'1','1','0','1','0'},
                {'1','1','0','0','0'},
                {'0','0','0','0','0'}
        };
        System.out.println("岛屿数量1: " + solution.numIslands(grid1)); // 预期 1

        // 测试用例 2：三个岛屿
        char[][] grid2 = {
                {'1','1','0','0','0'},
                {'1','1','0','0','0'},
                {'0','0','1','0','0'},
                {'0','0','0','1','1'}
        };
        System.out.println("岛屿数量2: " + solution.numIslands(grid2)); // 预期 3

        // 测试用例 3：空网格
        char[][] grid3 = {};
        System.out.println("岛屿数量3: " + solution.numIslands(grid3)); // 预期 0

        // 测试用例 4：全为 '0'，没有岛屿
        char[][] grid4 = {
                {'0','0','0'},
                {'0','0','0'}
        };
        System.out.println("岛屿数量4: " + solution.numIslands(grid4)); // 预期 0
    }
}