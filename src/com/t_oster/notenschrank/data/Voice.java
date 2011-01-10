package com.t_oster.notenschrank.data;

public class Voice {
	private String name;
	
	public Voice(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
	
	public boolean equals(Voice v){
		return (this.name.equals(v.name));
	}
}
