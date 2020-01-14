package com.bstek.ureport.exception;

public class ErrorcodeException extends Exception {
 	/**
	 * 
	 */
	private static final long serialVersionUID = -6484864353092579405L;
	
	/**
	 * 
	 */
	protected int errorcode;
	
	/**
	 * 
	 */
    protected String message;

    /**
     * 
     * @param code
     * @param message
     */
    public ErrorcodeException(int code, String message) {
        super();

        this.errorcode = code;
        this.message = message;
    }

    /**
     * @return the code
     */
    public int getErrorcode() {
        return errorcode;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Throwable#toString()
     */
    @Override
    public String getMessage() {
        return String.format("Code: %d message:%s", this.errorcode, this.message);
    }
}
