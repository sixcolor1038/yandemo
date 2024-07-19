package com.yan.demo.algorithms;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @Author: sixcolor
 * @Date: 2023-01-19 22:11
 * @Description: 两数之和
 */
public class Demo001 {
    /**
     * 给定一个整数数组 nums 和一个整数目标值 target，
     * 请你在该数组中找出 和为目标值 target  的那 两个 整数，并返回它们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
     * 你可以按任意顺序返回答案。
     * 示例 1：
     * 输入：nums = [2,7,11,15], target = 9
     * 输出：[0,1]
     * 解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
     * 示例 2：
     * 输入：nums = [3,2,4], target = 6
     * 输出：[1,2]
     * 示例 3：
     * 输入：nums = [3,3], target = 6
     * 输出：[0,1]
     * 提示：
     * 2 <= nums.length <= 104
     * -109 <= nums[i] <= 109
     * -109 <= target <= 109
     * 只会存在一个有效答案
     */
    public static void main(String[] args) {

        int[] nums = {8, 12, 5, 3, 17};
        System.out.println(Arrays.toString(twoSum2(nums, 8)));
    }

    /**
     * 哈希表法
     */
    public static int[] twoSum2(int[] nums, int target) {
        int len = nums.length;
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < len; i++) {
            if (hashMap.containsKey(target - nums[i])) {
                return new int[]{hashMap.get(target - nums[i]), i};
            }
            hashMap.put(nums[i], i);
        }
        return null;
    }

    /**
     * 暴力求解
     */
    public static int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
}
