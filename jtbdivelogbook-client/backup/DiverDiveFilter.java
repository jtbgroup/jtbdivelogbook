package be.vds.jtbdive.client.core.filters;

import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.Diver;
import be.vds.jtbdive.core.core.Palanquee;

public class DiverDiveFilter extends DiveFilter {

	public DiverDiveFilter() {
		super();
		setOperator(DiverOperator.CONTAINS);
	}

	public void setDiverCriteria(Diver diver) {
		getCriteria()[0] = diver;
		getCriteria()[1] = null;
	}

	@Override
	public boolean isValid(Dive dive) {
		Diver diver = getDiverCriteria();
		if (diver == null)
			return false;

		Palanquee p = dive.getPalanquee();
		if (p == null || p.getPalanqueeEntries().size() == 0)
			return false;

		DiverOperator op = (DiverOperator) getOperator();
		switch (op) {
		case CONTAINS:
			return p.isDiverPresent(diver);
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Buddies ");
		DiverOperator op = (DiverOperator) getOperator();

		switch (op) {
		case CONTAINS:
			sb.append("contains ");
			break;
		}

		Diver d = getDiverCriteria();
		sb.append(d == null ? "" : d.getFullName());

		return sb.toString();
	}

	public Diver getDiverCriteria() {
		return (Diver) getCriteria()[0];
	}

	@Override
	public DiveFilterType getDiveFilterType() {
		return DiveFilterType.DIVER;
	}

}
