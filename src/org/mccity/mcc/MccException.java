package org.mccity.mcc;

public class MccException extends Exception {

	private static final long serialVersionUID = 3875929502575876506L;

	public MccException(){
		super();
	}
	
	public MccException(String message){
		super(message);
	}
	
	public MccException(Throwable t){
		super(t);
	}

}
