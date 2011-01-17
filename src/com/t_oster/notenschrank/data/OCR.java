package com.t_oster.notenschrank.data;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.VolatileImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

public class OCR {

	/*
	 * This method returns the index of the String which is most probably
	 * contained in the given Image
	 */
	public static int findStringInImage(String[] possible, Image image) {
		if (System.getenv("os.name").equals("Linux")) {
			return 0;
		} else {
			throw new RuntimeException("Not implemented for your OS ("
					+ System.getenv("os.name") + ") yet");
		}
	}

	public static boolean isAvailable(){
		//TODO
		return true;
	}
	
	private static RenderedImage toBufferedImage(Image image) {
		if (image instanceof RenderedImage){
			System.out.println("no conversion needed");
			return (RenderedImage) image;
		}
		BufferedImage buffImg = new BufferedImage(image.getWidth(null),
				image.getHeight(null), BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2 = buffImg.createGraphics();
		g2.drawImage(image, null, null);
		g2.dispose();
		return buffImg;
	}

	public static String getStringsInImage(Image image) {
		String result=null;
		File png = SettingsManager.getInstance().getTempFile("png");
		File tif = SettingsManager.getInstance().getTempFile("tif");
		File txt = SettingsManager.getInstance().getTempFile("txt");

		try {
			ImageIO.write(toBufferedImage(image), "png", png);
			if (!png.exists()){
				System.err.println("Error writing png");
				return null;
			}
			Process p = Runtime.getRuntime().exec(new String[]{"/usr/bin/convert",png.getAbsolutePath(),"-depth", "4",tif.getAbsolutePath()});
			InputStream is = p.getErrorStream();
			int i;
			do{
				i = is.read();
				if (0<=i && i<=255)
				System.err.write(i);
			}while (i>=0);
			p.waitFor();
			png.delete();
			if (!tif.exists()){
				System.err.println("Error: tif was not created (ImageMagic installed??)");
				return null;
			}
			String txtname = txt.getAbsolutePath();
			txtname = txtname.substring(0,txtname.length()-4);
			p = Runtime.getRuntime().exec(new String[]{"tesseract",tif.getAbsolutePath(),txtname,"-l","deu"});
			is = p.getErrorStream();
			i=0;
			do{
				i = is.read();
				if (0<=i && i<=255)
				System.err.write(i);
			}while (i>=0);
			p.waitFor();
			tif.delete();
			if (txt.exists()){
				BufferedReader rd = new BufferedReader(new FileReader(txt));
				String s=null;
				result="";
				do{
					s =rd.readLine();
					result= s==null?result:result+s;
				}while(s!=null);
				rd.close();
				txt.delete();
			}
			else{
				System.err.println("Error: txt wasnt created. Tessearact failed??");
				return null;
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
}
