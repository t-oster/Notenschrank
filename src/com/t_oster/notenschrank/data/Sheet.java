package com.t_oster.notenschrank.data;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;

public class Sheet {
	
	public static final String FILEEXTENSION = "pdf";
	
	public Sheet (File pdfFile){
		
	}
	
	public Image getPreview(Dimension d){
		throw new RuntimeException();
	}
	
	public void print(){
		throw new RuntimeException();
	}
	
	public void print(int[] pages){
		throw new RuntimeException();
	}
	
	public int numberOfPages(){
		throw new RuntimeException();
	}

	public void addPage(Sheet s){
		throw new RuntimeException();
	}

	public void writeToFile(File out){
		throw new RuntimeException();
	}
}
