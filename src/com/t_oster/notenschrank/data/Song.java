package com.t_oster.notenschrank.data;

public class Song {
	private String name;
	
	public Song(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
	
	public boolean equals(Object o){
		if (o instanceof Song){
			return (this.name.equals(((Song) o).name));
		}
		else{
			return super.equals(o);
		}
	}
}
