import java.util.*;
import java.util.stream.Collectors;class Solution {
    int count = 0;

    public int findTargetSumWays(int[] nums, int target) {
        backtrack(nums, target, 0, 0);
        return count;
    }

    public void backtrack(int[] nums, int target, int index, int sum) {
        if (index == nums.length) {
            if (sum == target) {
                count++;
            }
        } else {
            backtrack(nums, target, index + 1, sum + nums[index]);
            backtrack(nums, target, index + 1, sum - nums[index]);
        }
    }
     public static void main(String[] args){
        Solution solution = new Solution();
    int[] augument0 = {1};int augument1 = 1;int result = solution.findTargetSumWays(augument0,augument1);System.out.println(result);}}