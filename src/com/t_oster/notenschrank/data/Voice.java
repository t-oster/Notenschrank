package com.t_oster.notenschrank.data;

import java.io.Serializable;

public class Voice implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6337860926449182115L;
	private String name;
	
	public Voice(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
	
	public boolean equals(Object o){
		if (o instanceof Voice){
			return (this.name.equals(((Voice) o).name));
		}
		else{
			return super.equals(o);
		}
	}
}
