package net.oijon.oling.soundchange;

/**
 * @deprecated This will be part of Phonutate instead
 */
public class InvalidRuleSyntaxException extends Exception {

	private static final long serialVersionUID = 1466969619682772857L;

	public InvalidRuleSyntaxException(Exception e) {
		super(e);
	}
	
	public InvalidRuleSyntaxException (String s) {
		super(s);
	}
	
	public InvalidRuleSyntaxException() {
		super();
	}
	
}
