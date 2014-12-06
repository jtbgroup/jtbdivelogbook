package be.vds.jtbdive.client.core.filters;

import be.vds.jtbdive.core.core.Dive;


public class DurationDiveFilter extends NumericDiveFilter {

	@Override
	public DiveFilterType getDiveFilterType() {
		return DiveFilterType.DIVE_TIME;
	}

	@Override
	public double getDiveCriteria(Dive dive) {
		return dive.getDiveTime();
	}

}
