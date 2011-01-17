package com.t_oster.notenschrank.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.DocumentException;
import com.t_oster.notenschrank.Notenschrank;
import com.t_oster.notenschrank.data.SettingsManager;
import com.t_oster.notenschrank.gui.PreviewPanel;
import com.t_oster.notenschrank.gui.SortingDialog;


public class UserTest {

	private File stack,arc;
	
	@Test
	public void testTmpDir(){
		assertEquals(
				JOptionPane.showConfirmDialog(null, 
						"Is '"+SettingsManager.getInstance().getTempFile("test")+"' a valid temp file?", 
						"Question",
						JOptionPane.YES_NO_OPTION),
						JOptionPane.YES_OPTION);
		System.out.println();
	}
	
	@Test
	public void PreviewPanelTest() throws IOException{
		JDialog d = new JDialog();
		PreviewPanel p = new PreviewPanel(d);
		p.showSheet(Archive.getInstance().getUnsortedSheets().get(0));
		d.add(p);
		d.pack();
		d.setModal(true);
		d.setVisible(true);
	}
	
	@Test
	public void scanTest(){
		new SortingDialog(null).showDialog();
	}
	
	@Before
	public void setUp() throws IOException, DocumentException{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Couldn't load system style. running in java style");
		}
		SettingsManager.getInstance().setArchivePath("///tmp//archivtest");
		arc = SettingsManager.getInstance().getArchivePath();
		arc.mkdirs();
		SettingsManager.getInstance().setStackPath("///tmp//stack");
		stack = SettingsManager.getInstance().getStackPath();
		stack.mkdirs();
		System.out.println("preparing test stack");
		for (File f:new File("data//testdata").listFiles()){
			new Sheet(f).writeToFile(new File(stack, f.getName()));
		}
		System.out.println("done");
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
	@Test
	public void AcceptanceTest(){
		Notenschrank.main(null);
	}
	
	@After
	public void tearDown(){
		System.out.println("deleting tmp arc");
		deleteRecursively(stack);
		deleteRecursively(arc);
		System.out.println("done");
	}
}
