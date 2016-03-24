package telford.cn1;

import com.codename1.util.MathUtil;

public class RandomCN1 implements telford.common.Random{

    private long seed = 8682522807148012L^System.currentTimeMillis();
    private double nextNextGaussian;
    private boolean haveNextNextGaussian = false;
    
    @Override
	public double nextGaussian() {
    	 // See Knuth, ACP, Section 3.4.1 Algorithm C.
        if (haveNextNextGaussian) {
            haveNextNextGaussian = false;
            return nextNextGaussian;
        } else {
            double v1, v2, s;
            do {
                v1 = 2 * nextDouble() - 1; // between -1 and 1
                v2 = 2 * nextDouble() - 1; // between -1 and 1
                s = v1 * v1 + v2 * v2;
            } while (s >= 1 || s == 0);
            double multiplier = MathUtil.pow((-2 * MathUtil.log(s)/s), 0.5);
            nextNextGaussian = v2 * multiplier;
            haveNextNextGaussian = true;
            return v1 * multiplier;
        }
	}

	@Override
	public boolean nextBoolean() {
		return next(1) != 0;
	}

	@Override
	public int nextInt(int n) { 
		if (n <= 0)
		     throw new IllegalArgumentException("n must be positive");

		   if ((n & -n) == n)  // i.e., n is a power of 2
		     return (int)((n * (long)next(31)) >> 31);

		   int bits, val;
		   do {
		       bits = next(31);
		       val = bits % n;
		   } while (bits - val + (n-1) < 0);
		   return val;
	}

	private int next(int bits) {
		seed = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1); 
		return (int)(seed >>> (48 - bits));
	}
	
	public double nextDouble() {
        return (((long)(next(26)) << 27) + next(27))
            / (double)(1L << 53);
    }
}

