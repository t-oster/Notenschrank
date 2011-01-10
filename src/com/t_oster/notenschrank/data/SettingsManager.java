package com.t_oster.notenschrank.data;

import java.io.File;
import java.io.Serializable;

public class SettingsManager implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2627505348844681752L;
	//Static part for Singelton design pattern
	private static SettingsManager instance;
	public static SettingsManager getInstance(){
		if (instance == null){
			instance = new SettingsManager();
		}
		return instance;
	}
	
	//Attributes for Program Settings
	private String programVersion = "0.1";
	private String archivePath = "Archive";
	private String stackPath = "Stack";
	
	private SettingsManager(){
		
	}
	
	
	
	public String getProgramVersion(){
		return programVersion;
	}
	
	public File getArchivePath(){
		return new File(archivePath);
	}
	
	public File getStackPath(){
		return new File(stackPath);
	}
	
}