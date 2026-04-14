class Solution03 {
    public int search(int[] arr, int target) {
        // 左右指针初始化
        int left = 0;
        int right = arr.length - 1;

        // 二分查找核心循环
        while (left <= right) {
            // 计算中间索引（避免溢出，等价于(left+right)/2）
            int mid = left + (right - left) / 2;

            // 找到目标值，直接返回索引
            if (arr[mid] == target) {
                return mid;
            }

            // 情况1：左半区间 [left, mid] 是有序的
            if (arr[left] <= arr[mid]) {
                // target 在左半有序区间内，收缩右指针
                if (arr[left] <= target && target < arr[mid]) {
                    right = mid - 1;
                }
                // target 不在左半，收缩左指针
                else {
                    left = mid + 1;
                }
            }
            // 情况2：右半区间 [mid, right] 是有序的
            else {
                // target 在右半有序区间内，收缩左指针
                if (arr[mid] < target && target <= arr[right]) {
                    left = mid + 1;
                }
                // target 不在右半，收缩右指针
                else {
                    right = mid - 1;
                }
            }
        }
        // 循环结束没找到，返回-1
        return -1;
    }
}