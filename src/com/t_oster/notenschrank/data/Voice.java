package com.t_oster.notenschrank.data;

public class Voice {
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
