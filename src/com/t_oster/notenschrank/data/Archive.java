package com.t_oster.notenschrank.data;

import java.io.File;
import java.util.LinkedList;

public class Archive {
	private static Archive instance;
	public static Archive getInstance(){
		if (instance == null){
			instance = new Archive();
		}
		return instance;
	}
	
	private Archive(){
		
	}
	
	public Song[] getAvailableSongs(){
		LinkedList<Song> result = new LinkedList<Song>();
		for(File f:SettingsManager.getInstance().getArchivePath().listFiles()){
			if (f.isDirectory()){
				result.add(new Song(f.getName()));
			}
		}
		return result.toArray(new Song[0]);
		
	}
	
	public Voice[] getAvailableVoices(){
		
	}
	
	public Voice[] getAvailableVoices(Song s){
		
	}
	
	public Sheet[] getSheets(Song s){
		
	}
	
	public Sheet[] getSheets(Voice v){
		
	}
	
	public Sheet getSheet(Song s, Voice v){
		
	}
	
	public void addToArchive(Sheet s){
		
	}
}
