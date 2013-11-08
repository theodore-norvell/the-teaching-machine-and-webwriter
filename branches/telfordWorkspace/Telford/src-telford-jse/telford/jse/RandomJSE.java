package telford.jse;

import java.util.Random;

public class RandomJSE implements telford.common.Random {
	
	Random random = new Random();
	
	@Override
	public double nextGaussian() {
		return random.nextGaussian();
	}

	@Override
	public boolean nextBoolean() {
		return random.nextBoolean();
	}

	@Override
	public int nextInt(int i) {
		return random.nextInt(i);
	}

}
