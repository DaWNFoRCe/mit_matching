package mit.matching.algorithm;

import java.util.Arrays;

class StableMatch {
    private final int[] s;
    private final int[] r;

    public StableMatch(int[] s, int[] r) {
        this.s = s.clone();
        this.r = r.clone();
    }

    public void printPrefs() {
        System.out.println(Arrays.toString(s));
        System.out.println(Arrays.toString(r));
    }
}