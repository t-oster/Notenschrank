package com.t_oster.notenschrank.data;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import com.itextpdf.text.DocumentException;

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
	
	private File calculatePath(Song s){
		return new File(SettingsManager.getInstance().getArchivePath(),s.toString());
	}
	
	private File calculatePath(Song s, Voice v){
		return new File(calculatePath(s),v.toString()+"."+Sheet.FILEEXTENSION);
	}
	
	private String getVoiceName(File voiceFile){
		return voiceFile.getName().split("."+Sheet.FILEEXTENSION)[0];
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
		//Using HashMap of String and not Voice because it doesn't work properly
		Set<String> result = new HashSet<String>();
		for (Song s:this.getAvailableSongs()){
			for (Voice v:this.getAvailableVoices(s)){
				result.add(v.toString());
			}
		}
		Voice[] rresult = new Voice[result.size()];
		int i=0;
		for(String s:result){
			rresult[i++]=new Voice(s);
		}
		return rresult;
	}
	
	public Voice[] getAvailableVoices(Song s){
		LinkedList<Voice> result = new LinkedList<Voice>();
		for(File f:calculatePath(s).listFiles()){
			if (f.isFile()){
				result.add(new Voice(getVoiceName(f)));
			}
		}
		return result.toArray(new Voice[0]);
	}
	
	public Sheet[] getSheets(Song s) throws IOException{
		LinkedList<Sheet> result = new LinkedList<Sheet>();
		File path = new File(SettingsManager.getInstance().getArchivePath(),s.toString());
		for(File f:path.listFiles()){
			if (f.isFile()){
				result.add(new Sheet(f));
			}
		}
		return result.toArray(new Sheet[0]);
	}
	
	public Sheet[] getSheets(Voice v) throws IOException{
		LinkedList<Sheet> result = new LinkedList<Sheet>();
		for(Song s:this.getAvailableSongs()){
			Sheet tmp = this.getSheet(s, v);
			if (tmp!=null){
				result.add(tmp);
			}
		}
		return result.toArray(new Sheet[0]);
	}
	
	public Sheet getSheet(Song s, Voice v) throws IOException{
		File result = new File(
				SettingsManager.getInstance().getArchivePath(),
				s.toString()+File.pathSeparator+v.toString()+"."+Sheet.FILEEXTENSION);
		if (result.exists()){
			return new Sheet(result);
		}
		else{
			return null;
		}
	}
	
	public Sheet[] getUnsortedSheets() throws IOException{
		LinkedList<Sheet> result = new LinkedList<Sheet>();
		for(File f:SettingsManager.getInstance().getStackPath().listFiles()){
			if (f.isFile()){
				result.add(new Sheet(f));
			}
		}
		return result.toArray(new Sheet[0]);
	}
	
	public boolean contains(Song s, Voice v){
		return calculatePath(s,v).exists();
	}
	
	public void addToArchive(Sheet s, Song song, Voice v) throws IOException, DocumentException{
		File target = calculatePath(song,v);
		s.writeToFile(target);
	}
}
