package net.oijon.oling.soundchange;

/**
 * @deprecated This will be part of Phonutate instead
 */
public class InvalidCategorySyntaxException extends Exception {

	private static final long serialVersionUID = 8041544740448654301L;

	public InvalidCategorySyntaxException(Exception e) {
		super(e);
	}
	
	public InvalidCategorySyntaxException (String s) {
		super(s);
	}
	
	public InvalidCategorySyntaxException() {
		super();
	}
	
}
