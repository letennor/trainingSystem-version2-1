import java.util.*;
import java.util.stream.Collectors;class Solution {
    public int heightChecker(int[] heights) {
        int n = heights.length, ans = 0;
        int[] expected = new int[n];
        System.arraycopy(heights, 0, expected, 0, n);
        Arrays.sort(expected);
        for (int i = 0; i < n; ++i) {
            if (heights[i] != expected[i]) {
                ++ans;
            }
        }
        return ans;
    }
     public static void main(String[] args){
        Solution solution = new Solution();
    int[] augument0 = {1,2,3,4,5};int result = solution.heightChecker(augument0);System.out.println(result);}}