package com.t_oster.notenschrank.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ArchiveTester {
	
	private String[] songs = new String[]{"Adebars Reisen", "Martini", "1984 George Orwell"};;
	private String[][] voices = new String[][]{
			new String[]{"1. Klarinette in Bb", "1. Trompete in Bb"},
			new String[]{"1. Eb-Alt Saxophon"},
			new String[]{"1. Klarinette in Bb", "2. Eb-Alt Saxophon"}
	};
	
	@Before
	public void setUp() throws IOException{
		SettingsManager.getInstance().archivePath = "///tmp//archivtest";
		File arc = SettingsManager.getInstance().getArchivePath();
		arc.mkdirs();
		for (int i=0;i<songs.length;i++){
			File dir = new File(arc,songs[i]);
			dir.mkdir();
			for (String s:voices[i]){
				new File(dir,s+"."+Sheet.FILEEXTENSION).createNewFile();
			}
		}
	}
	
	@Test
	public void equalsTest(){
		for (String s:songs){
			Song a = new Song(s);
			Song b = new Song(s);
			assertEquals(a,b);
		}
		for (String[] va:voices){
			for (String v:va){
				Voice a = new Voice(v);
				Voice b = new Voice(v);
				assertEquals(a,b);
			}
		}
	}
	
	@Test
	public void testGetAvailableSongs(){
		Song[] result = Archive.getInstance().getAvailableSongs();
		assertEquals(result.length,songs.length);
		for(Song so:result){
			boolean found=false;
			for(String st:songs){
				if (so.toString().equals(st)){
					found=true;
					break;
				}
			}
			assertTrue(found);
		}
	}
	
	@Test
	public void testGetAvailableVoices(){
		
		Voice[] result = Archive.getInstance().getAvailableVoices();
		Set<String> vset = new HashSet<String>();
		for(String[] a:voices){
			for(String s:a){
				vset.add(s);
			}
		}
		assertEquals(vset.size(),result.length);
		for(Voice v:result){
			assertTrue(vset.contains(v.toString()));
		}
	}
	
	private void deleteRecursively(File f){
		if (f.isDirectory()){
			for(File ff:f.listFiles()){
				deleteRecursively(ff);
			}
			f.delete();
		}
		else{
			f.delete();
		}
	}
	
	@After
	public void tearDown(){
		deleteRecursively(new File("///tmp//archivtest"));
	}
}
