package hellointerview.intervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class FallingSquares {

  static class SegTree {
    int n;
    int[] max;
    int[] lazy;

    SegTree(int n) {
      this.n = n;
      this.max = new int[4*n];
      this.lazy = new int[4*n];
      Arrays.fill(lazy, -1);
    }

    void update(int idx, int l, int r, int ql, int qr, int val) {
      if(ql>=r || qr<=l) return;
      if(ql<=l && qr>=r) {
        apply(idx, val);
        return;
      }
      push(idx);
      int m = (l+r)/2;
      update(2*idx, l, m, ql, qr, val);
      update(2*idx+1, m, r, ql, qr, val);
      max[idx] = Math.max(max[2*idx], max[2*idx+1]);
    }

    void push(int idx) {
      if(lazy[idx]!=-1) {
        apply(2*idx, lazy[idx]);
        apply(2*idx+1, lazy[idx]);
        lazy[idx] = -1;
      }
    }

    void apply(int idx, int val) {
      max[idx] = val;
      lazy[idx] = val;
    }

    int query(int idx, int l, int r, int ql, int qr) {
      if(ql>=r || qr<=l) return 0;
      if(ql<=l && qr >=r) {
        return max[idx];
      }
      push(idx);
      int m = (l+r)/2;
      return Math.max(query(2*idx, l, m, ql, qr), query(2*idx+1, m, r, ql, qr));
    }
  }

  public List<Integer> fallingSquareHeight(int[][] positions) {
    int n = positions.length;

    //1) Collect coords
    TreeSet<Integer> set = new TreeSet<>();
    for(int[] p : positions) {
      int L = p[0];
      int R = L + p[1];
      set.add(L);
      set.add(R);
    }
    //2)Compress X
    int[] X = set.stream().mapToInt(Integer::intValue).toArray();
    Map<Integer, Integer> xToIdx = new HashMap<>();
    for(int i=0; i<X.length; i++) {
      xToIdx.put(X[i], i);
    }
    SegTree st = new SegTree(X.length);
    int currMax = 0;
    List<Integer> res = new ArrayList<>();
    for(int[] p: positions) {
      int L = p[0];
      int size = p[1];
      int R = L + size;
      int l = xToIdx.get(L);
      int r = xToIdx.get(R);
      int base = st.query(1, 0, st.n, l, r);
      System.out.println("l : " + l + " r: " + r + " base: " + base);
      int top = base + size;
      st.update(1, 0, st.n, l, r, top);
      currMax = Math.max(currMax, top);
      res.add(currMax);
    }
    return res;
  }

  public static void main(String[] args) {
    FallingSquares sol = new FallingSquares();
    int[][] pos = {
        {1, 2},
        {2, 3},
        {6, 1}
    };
    System.out.println(sol.fallingSquareHeight(pos)); // [2,5,5]
  }
}
