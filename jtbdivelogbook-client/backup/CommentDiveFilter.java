package be.vds.jtbdive.client.core.filters;


public class CommentDiveFilter extends StringDiveFilter {

	@Override
	public DiveFilterType getDiveFilterType() {
		return DiveFilterType.COMMENT;
	}
	
}
