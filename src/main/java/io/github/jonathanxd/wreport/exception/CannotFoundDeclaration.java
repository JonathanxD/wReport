package io.github.jonathanxd.wreport.exception;

public class CannotFoundDeclaration extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1209104778055978288L;

	public CannotFoundDeclaration(String declaration) {
		super(String.format("Cannot get declaration '%s', probably null.", declaration));
	}
}
