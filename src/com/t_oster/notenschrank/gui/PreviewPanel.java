package com.t_oster.notenschrank.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.t_oster.notenschrank.data.Sheet;

public class PreviewPanel extends JPanel implements Runnable, ComponentListener, MouseListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 595838121389049252L;
	private JLabel widget;
	private Image image;
	private Sheet sheet;
	private Component parent;
	private boolean refreshing=false;
	
	public PreviewPanel(Component parent){
		widget = new JLabel("keine Datei zum einsortieren");
		this.add(widget);
		this.validate();
		this.setVisible(true);
		this.addMouseListener(this);
		this.parent=parent;
		if (this.parent!=null){
			this.parent.addComponentListener(this);
;		}
	}
	
	private int zoomLevel =0;
	public void zoom(boolean zoomIn, boolean zoomLeft){
		if (zoomLevel==0 && !zoomIn){
			return;
		}
		//TODO: Implement Zoom Ha ha
		System.out.println("ZoomIn: "+zoomIn+" left "+zoomLeft);
	}
	
	public void showSheet(Sheet s){
		synchronized (this){
			this.sheet=s;
		}
		this.refresh();
	}
	
	public Sheet getSheet(){
		synchronized(this){
			return this.sheet;
		}
	}
	
	public void refresh(){
		synchronized (this){
			if (!this.refreshing){
				this.refreshing=true;
				new Thread(this).start();
			}
			else{
				System.out.println("refresh dropped");
			}
		}
	}
	
	@Override
	public void run() {
		Sheet mysheet;
		synchronized (this){
			mysheet=sheet;
		}
		if (mysheet==null){
			this.widget.setIcon(null);
			this.widget.setText("keine Datei zum einsortieren");
		}
		else{
			try {
				Dimension size = this.getSize();
				this.widget.setText("...lade...");
				this.widget.setIcon(null);
				this.repaint();
				System.out.print("loading...");
				int width = (int) (size.getWidth()*0.8);
				int height =  (int) (size.getHeight()*0.8);
				int relheight = 100*height/width;
				this.image = sheet.getPreview(new Dimension(100,relheight), new Dimension(width,height));
				System.out.print("done...");
				widget.setText("");
				widget.setIcon(new ImageIcon(this.image));
				size=null;
			} catch (IOException e) {
				this.widget.setIcon(null);
				this.widget.setText("Fehler beim Anzeigen...\n"+e.getMessage());
			}
		}
		this.repaint();
		synchronized(this){
			this.refreshing=false;
		}
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		this.refresh();
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		boolean zoomIn=false;
		if (e.getButton()==MouseEvent.BUTTON1){
			zoomIn=true;
		}
		boolean zoomLeft = (e.getPoint().x<=this.getSize().width/2);
		this.zoom(zoomIn,zoomLeft);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
