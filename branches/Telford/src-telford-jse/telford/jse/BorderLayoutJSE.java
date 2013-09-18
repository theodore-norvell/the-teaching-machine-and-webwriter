package telford.jse;

import java.awt.BorderLayout;

import telford.common.Kit;

class BorderLayoutJSE implements telford.common.BorderLayout{
	
	BorderLayout b ;

	BorderLayoutJSE( BorderLayout b) { this.b = b ; }

	@Override
	public Object getNorth() {
		return Kit.getKit().getNorth();
	}
	
	
}
