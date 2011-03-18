package com.t_oster.notenschrank.data;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
 		if (SettingsManager.getInstance().getArchivePath()==null || !SettingsManager.getInstance().getArchivePath().exists()){
 			return new Song[0];
 		}
		LinkedList<Song> result = new LinkedList<Song>();
		File[] files = SettingsManager.getInstance().getArchivePath().listFiles();
		Arrays.sort(files, alphabetical);
		for(File f:files){
			if (f.isDirectory()){
				result.add(new Song(f.getName()));
			}
		}
		return result.toArray(new Song[0]);
		
	}
	
 	public Song[] getAvailableSongs(Voice selectedVoice) {
 		LinkedList<Song> result = new LinkedList<Song>();
		for (Song s:this.getAvailableSongs()){
			if (this.contains(s, selectedVoice)){
				result.add(s);
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
	
	/**
	 * If s is null all possible Voices are returned.
	 * @param s
	 * @return
	 */
	public Voice[] getAvailableVoices(Song s){
		if (s==null){
			return this.getAvailableVoices();
		}
		LinkedList<Voice> result = new LinkedList<Voice>();
		File[] files = calculatePath(s).listFiles();
		Arrays.sort(files,alphabetical);
		for(File f:files){
			if (f.isFile()){
				result.add(new Voice(getVoiceName(f)));
			}
		}
		return result.toArray(new Voice[0]);
	}
	
	public Sheet[] getSheets(Song s) throws IOException{
		LinkedList<Sheet> result = new LinkedList<Sheet>();
		File path = new File(SettingsManager.getInstance().getArchivePath(),s.toString());
		File[] files = path.listFiles();
		Arrays.sort(files,alphabetical);
		for(File f:files){
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
		File result = this.calculatePath(s, v);
		if (result.exists()){
			return new Sheet(result);
		}
		else{
			return null;
		}
	}
	
	private static final Comparator<File> lastModified = new Comparator<File>() {
		@Override
		public int compare(File o1, File o2) {
			return o1.lastModified() == o2.lastModified() ? 0 : (o1.lastModified() > o2.lastModified() ? 1 : -1 ) ;
		}
	};
	
	private static final Comparator<File> alphabetical = new Comparator<File>() {
		@Override
		public int compare(File o1, File o2) {
			return o1.getName().compareToIgnoreCase(o2.getName());
		}
	};
	
	public List<Sheet> getUnsortedSheets() throws IOException{
		File[] files = SettingsManager.getInstance().getStackPath().listFiles();
		Arrays.sort(files, lastModified);
		LinkedList<Sheet> result = new LinkedList<Sheet>();
		for(File f:files){
			if (f.isFile()){
				result.add(new Sheet(f));
			}
		}
		return result;
	}
	
	public boolean contains(Song s, Voice v){
		return calculatePath(s,v).exists();
	}
	
	public void addToArchive(Sheet s, Song song, Voice v) throws IOException, DocumentException{
		File dir = calculatePath(song);
		if (!dir.exists()){
			dir.mkdirs();
		}
		File target = calculatePath(song,v);
		s.writeToFile(target);
	}

	
	

	
}
