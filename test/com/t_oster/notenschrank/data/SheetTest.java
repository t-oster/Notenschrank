package com.t_oster.notenschrank.data;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.junit.After;
import org.junit.Test;

import com.itextpdf.text.DocumentException;
import com.t_oster.notenschrank.data.Sheet;


public class SheetTest {
	@Test
	public void getImageTest() throws IOException{
		
		//show the image in a frame
        JFrame frame = new JFrame("PDF Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Sheet sheet = new Sheet(new File("data//b.pdf"));
        Image img = sheet.getPreview(new Dimension(100,30), new Dimension(300,200));
        assertEquals(img.getWidth(null),300);
        assertEquals(img.getHeight(null),200);
        assertNotNull(img);
        
		frame.add(new JLabel(new ImageIcon(img)));
        frame.pack();
        frame.setVisible(true);
        JOptionPane.showMessageDialog(null, "you should see the PDF stuff", "bla", JOptionPane.OK_OPTION);
	}
	
	@After
	public void tearDown(){
		if (new File("/tmp//c.pdf").exists()){
			new File("/tmp//c.pdf").delete();
		}
	}
	
	@Test
	public void appendTest() throws IOException, DocumentException{
		File a = new File("data//a.pdf");
		File b = new File("data//b.pdf");
		File c = new File("///tmp//c.pdf");
		
		assertTrue(a.exists());
		assertTrue(b.exists());
		assertFalse(c.exists());
		Sheet sa = new Sheet(a);
		sa.writeToFile(c);
		assertTrue(c.exists());
		Sheet sb = new Sheet(b);
		Sheet sc = new Sheet(c);
		sc.addPage(sb);
		assertEquals(sc.numberOfPages(),sa.numberOfPages()+sb.numberOfPages());
		
	}

	@Test
	public void rotateTest() throws IOException, DocumentException{
		Sheet s = new Sheet(new File("data//a.pdf"));
		File tmp = new File("///tmp//c.pdf");
		s.writeToFile(tmp);
		s = new Sheet(tmp);
		s.rotatePage(1, 2);
		//show the image in a frame
        JFrame frame = new JFrame("PDF Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image img = s.getPreview(new Dimension(100,30), new Dimension(300,200));
        assertEquals(img.getWidth(null),300);
        assertEquals(img.getHeight(null),200);
        assertNotNull(img);
        
        JLabel lab = new JLabel(new ImageIcon(img));
		frame.add(lab);
        frame.pack();
        frame.setVisible(true);
        JOptionPane.showMessageDialog(null, "you should see the PDF stuff", "bla", JOptionPane.OK_OPTION);
        s.rotatePage(1, 1);
        img = s.getPreview(new Dimension(100,30), new Dimension(300,200));
        assertEquals(img.getWidth(null),300);
        assertEquals(img.getHeight(null),200);
        assertNotNull(img);
        frame.remove(lab);
        lab = new JLabel(new ImageIcon(img));
		frame.add(lab);
        frame.pack();
        JOptionPane.showMessageDialog(null, "you should see the PDF stuff", "bla", JOptionPane.OK_OPTION);
	}
}
