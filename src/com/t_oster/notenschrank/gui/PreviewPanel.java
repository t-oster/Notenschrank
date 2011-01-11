package com.t_oster.notenschrank.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.t_oster.notenschrank.data.Sheet;

public class PreviewPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 595838121389049252L;
	private JLabel widget;
	private Image image;
	private Sheet sheet;
	
	public PreviewPanel(){
		widget = new JLabel("keine Datei zum einsortieren");
		this.setVisible(true);
	}
	
	public void showSheet(Sheet s){
		this.sheet=s;
		this.refresh();
	}
	public void refresh(){
		if (sheet==null){
			this.widget.setIcon(null);
			this.widget.setText("keine Datei zum einsortieren");
		}
		else{
			try {
				this.image = sheet.getPreview(new Dimension(100,20), this.getSize());
				widget.setIcon(new ImageIcon(this.image));
				widget.setText("");
			} catch (IOException e) {
				this.widget.setIcon(null);
				this.widget.setText("Fehler beim Anzeigen...\n"+e.getMessage());
			}
		}
		this.validate();
	}
}
