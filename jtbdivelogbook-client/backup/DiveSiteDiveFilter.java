package be.vds.jtbdive.client.core.filters;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.DiveSite;

public class DiveSiteDiveFilter extends DiveFilter {

	public DiveSiteDiveFilter() {
		super();
		setOperator(DiveSiteOperator.EQUALS);
	}

	public void setDiveSiteCriteria(DiveSite diveSite) {
		getCriteria()[0] = diveSite;
		getCriteria()[1] = null;
	}

	@Override
	public boolean isValid(Dive dive) {
		DiveSite diveSite = getDiveSiteCriteria();
//		if (diveSite == null || dive.getDiveSite() == null)
//			return false;

		DiveSiteOperator op = (DiveSiteOperator) getOperator();
		switch (op) {
		case EQUALS:
			return (diveSite == dive.getDiveSite());
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Dive site ");
		DiveSiteOperator op = (DiveSiteOperator) getOperator();

		switch (op) {
		case EQUALS:
			sb.append("equals ");
			break;
		}

		DiveSite d = getDiveSiteCriteria();
		sb.append(d == null ? "" : d.getName());

		return sb.toString();
	}

	public DiveSite getDiveSiteCriteria() {
		return (DiveSite) getCriteria()[0];
	}

	@Override
	public DiveFilterType getDiveFilterType() {
		return DiveFilterType.DIVESITE;
	}

}
