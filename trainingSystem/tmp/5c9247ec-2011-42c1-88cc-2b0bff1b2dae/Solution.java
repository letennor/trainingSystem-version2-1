import java.util.*;
import java.util.stream.Collectors;class Solution {
    public int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int numOfChildren = g.length, numOfCookies = s.length;
        int count = 0;
        for (int i = 0, j = 0; i < numOfChildren && j < numOfCookies; i++, j++) {
            while (j < numOfCookies && g[i] > s[j]) {
                j++;
            }
            if (j < numOfCookies) {
                count++;
            }
        }
        return count;
    }
     public static void main(String[] args){
        Solution solution = new Solution();
    int[] augument0 = {1,2,3};int[] augument1 = {1,1};int result = solution.findContentChildren(augument0,augument1);System.out.println(result);}}