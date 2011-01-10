package com.t_oster.notenschrank.data;

public class Song {
	private String name;
	
	public Song(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
	
	public boolean equals(Song s){
		return (this.name.equals(s.name));
	}
}
