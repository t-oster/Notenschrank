package com.t_oster.notenschrank.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.itextpdf.text.DocumentException;
import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.SettingsManager;
import com.t_oster.notenschrank.data.Sheet;
import com.t_oster.notenschrank.data.Song;
import com.t_oster.notenschrank.data.Voice;

public class SortingFrame extends JDialog implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2699371667100327736L;
	private PreviewPanel previewpanel;
	private PreviewPanel current;
	private JPanel mainPanel;
	private SelectSongBox cbSong;
	private SelectVoiceBox cbVoice;
	private JButton bOk;
	private JButton bAddPage;
	private JButton bRotatePage;
	private List<Sheet> stackSheets;

	public SortingFrame(JFrame parent){
		super(parent,"Notenschrank "+SettingsManager.getInstance().getProgramVersion());
		this.previewpanel = new PreviewPanel(this);
		this.current = new PreviewPanel(this);
		this.cbSong = new SelectSongBox();
		this.cbSong.setMaximumSize(new Dimension(500,35));
		this.cbVoice = new SelectVoiceBox();
		this.cbVoice.setMaximumSize(new Dimension(500,35));
		this.bOk = new JButton("Speichern");
		this.bOk.addActionListener(this);
		this.bAddPage = new JButton("weitere Seite");
		this.bAddPage.addActionListener(this);
		this.bRotatePage = new JButton("Seite drehen");
		this.bRotatePage.addActionListener(this);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.mainPanel = new JPanel();
		this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
		Box b = Box.createHorizontalBox();
		b.add(this.previewpanel);
		b.add(this.current);
		this.mainPanel.add(b);
		this.mainPanel.add(cbSong);
		this.mainPanel.add(cbVoice);
		Box box = Box.createHorizontalBox();
		box.add(bRotatePage);
		box.add(bAddPage);
		box.add(bOk);
		this.mainPanel.add(box);
		this.setContentPane(mainPanel);
		this.pack();
	}
	
	public void showDialog(){
		
		try{
			this.stackSheets = Archive.getInstance().getUnsortedSheets();
			if (stackSheets.size()==0){
				JOptionPane.showConfirmDialog(null, "Keine Noten zum sortieren vorhanden\nBitte scannen Sie zuerst welche ein");
				this.dispose();
				return;
			}
			Sheet cur = stackSheets.remove(0);
			this.current.showSheet(cur);
			if (stackSheets.size()>0){
				Sheet nxt = stackSheets.remove(0);
				this.previewpanel.showSheet(nxt);
			}
			this.setModal(true);
			//bocks invoking Thread until disposed
			this.setVisible(true);
		}
		catch (Exception e){
			
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.bRotatePage)){
			try {
				Sheet s = this.current.getSheet();
				s.rotatePage(s.numberOfPages(), 1);
				this.current.refresh();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		else if (e.getSource().equals(this.bAddPage)){
			try {
				Sheet current = this.current.getSheet();
				Sheet preview = this.previewpanel.getSheet();
				if (preview != null){
					current.addPage(this.previewpanel.getSheet());
					this.current.refresh();
					preview.delete();
					if (this.stackSheets.size()>0){
						this.previewpanel.showSheet(this.stackSheets.remove(0));
					}
					else{
						this.previewpanel.showSheet(null);
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if (e.getSource().equals(this.bOk)){
			Sheet s = current.getSheet();
			Song song = cbSong.getSelectedSong();
			Voice v = cbVoice.getSelectedVoice();
			try {
				Archive.getInstance().addToArchive(s, song, v);
				s.delete();
				cbSong.reload();
				cbVoice.reload();
				Sheet p = previewpanel.getSheet();
				this.current.showSheet(p);
				if (this.stackSheets.size()>0){
					this.previewpanel.showSheet(this.stackSheets.remove(0));
				}
				else{
					this.previewpanel.showSheet(null);
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}
}
