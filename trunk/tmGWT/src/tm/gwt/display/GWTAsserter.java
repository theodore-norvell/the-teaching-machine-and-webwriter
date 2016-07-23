package tm.gwt.display;

import tm.interfaces.AsserterI;

public class GWTAsserter implements AsserterI {

	@Override
	public void check(Throwable e) {
		check(false, e);
	}

	@Override
	public void check(boolean proposition, Throwable e) {
		if (!proposition)
			throw new AssertionError(e);
	}

	@Override
	public void check(boolean proposition) {
		check(proposition, "Assertion failed");
	}

	@Override
	public void check(boolean proposition, String message) {
		if (!proposition)
			throw new AssertionError(message);

	}

	@Override
	public void check(String message) {
		throw new AssertionError(message);

	}

	@Override
	public void toBeDone() {
		check(false, "TO BE DONE");
	}

	@Override
	public void error(String message) {
	}
}
