package com.t_oster.notenschrank.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.SettingsManager;
import com.t_oster.notenschrank.data.Sheet;

public class PrintWizzardDialog extends JDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2581319034184146056L;
	private JButton bPrintOne;
	private JButton bPrintSong;
	private JButton bPrintVoice;
	private JFrame parent;

	public PrintWizzardDialog(JFrame parent) {
		super(parent, "Notenschrank "
				+ SettingsManager.getInstance().getProgramVersion());
		this.parent = parent;
		bPrintOne = new JButton("Eine Stimme für ein Stück drucken");
		bPrintSong = new JButton("Einen kompletten Satz drucken");
		bPrintVoice = new JButton("Eine komplette Mappe drucken");

		bPrintOne.addActionListener(this);
		bPrintSong.addActionListener(this);
		bPrintVoice.addActionListener(this);

		JPanel b = new JPanel();
		b.setLayout(new GridLayout(0, 1));
		b.add(bPrintOne);
		b.add(bPrintSong);
		b.add(bPrintVoice);
		this.setContentPane(b);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	public void showDialog() {
		if (Archive.getInstance().getAvailableSongs().length == 0) {
			JOptionPane
					.showMessageDialog(
							parent,
							"Es wurden noch keine Noten eingescannt.\nEs können also auch keine gedruckt werden.");
			return;
		}
		this.setModal(true);
		this.setVisible(true);
	}

	private void printOneClicked() {
		PrintOneDialogPanel choose = new PrintOneDialogPanel();
		if (JOptionPane.showConfirmDialog(this, choose, this.getTitle(),
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			ProgressWindow pw = null;
			try {
				Sheet s = choose.getSelectedSheet();

				if (s == null) {
					throw new IOException();
				}
				if (choose.isPreviewSelected()) {
					s.openInReader();
				} else {
					int times = choose.getSelectedNumber();
					pw = new ProgressWindow(this.getTitle(),
							"Drucke...", 0, times);
					for (int i = 0; i < times; i++) {
						s.print();
						pw.setState(i);
					}
					pw.close();
				}
			} catch (Exception e) {
				if (pw!=null){
					pw.close();
				}
				JOptionPane.showConfirmDialog(this, "Es ist ein Fehler aufgetreten: \n"+e.getMessage());
				return;
			}
		}
	}

	private void printSetClicked() {
		PrintSetDialogPanel choose = new PrintSetDialogPanel();
		if (JOptionPane.showConfirmDialog(this, choose, this.getTitle(),
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			ProgressWindow pw=null;
			try {
				Sheet[] sheets = choose.getSelectedSheets();
				int[] numbers = choose.getSelectedNumbers();
				boolean somethingChecked = false;
				for (int i : numbers) {
					if (i > 0) {
						somethingChecked = true;
						break;
					}
				}
				if (!somethingChecked) {
					JOptionPane.showMessageDialog(this,
							"Sie haben nichts ausgewählt");
					return;
				}

				if (choose.isPreviewSelected()) {
					// merge all sheets into one big one

					pw = new ProgressWindow(this.getTitle(),
							"erstelle Vorschau...", 0, sheets.length);
					File tmpfile = SettingsManager.getInstance().getTempFile();
					tmpfile.createNewFile();
					Sheet tmp = new Sheet(tmpfile);
					for (int i = 0; i < sheets.length; i++) {
						for (int j = 0; j < numbers[i]; j++) {
							tmp.addPage(sheets[i]);
						}
						pw.setState(i);
					}
					pw.close();
					tmp.openInReader();

				} else {
					pw = new ProgressWindow(this.getTitle(),
							"drucke...", 0, sheets.length);
					for (int i = 0; i < sheets.length; i++) {
						for (int j = 0; i < numbers[i]; j++) {
							sheets[i].print();
						}
						pw.setState(i);
					}
					pw.close();
					JOptionPane.showMessageDialog(this,
							"Daten wurden an den Drucker gesendet.");
				}
			} catch (Exception e) {
				if (pw!=null){
					pw.close();
				}
				JOptionPane.showConfirmDialog(this, "Es ist ein Fehler aufgetreten: \n"+e.getMessage());
				return;
			}
		}
	}

	private void printVoiceClicked() {
		PrintVoiceDialogPanel choose = new PrintVoiceDialogPanel();
		if (JOptionPane.showConfirmDialog(this, choose, this.getTitle(),
				JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
			ProgressWindow pw=null;
			try {
				Sheet[] sheets = choose.getSelectedSheets();
				if (sheets.length==0){
					JOptionPane.showMessageDialog(this,"Sie haben nichts ausgewählt");
					return;
				}
				if (choose.isPreviewSelected()) {
					// merge all sheets into one big one
					pw = new ProgressWindow(this.getTitle(),
							"erstelle Vorschau...", 0, sheets.length);
					File tmpfile = SettingsManager.getInstance().getTempFile();
					tmpfile.createNewFile();
					Sheet tmp = new Sheet(tmpfile);
					for (Sheet s : sheets) {
						tmp.addPage(s);
						pw.incrementState();
					}
					pw.close();
					tmp.openInReader();
				} else {
					pw = new ProgressWindow(this.getTitle(),
							"drucke...", 0, sheets.length);
					for (Sheet s : sheets) {
						s.print();
						pw.incrementState();
					}
					pw.close();
					JOptionPane.showMessageDialog(this,
							"Daten wurden an den Drucker gesendet.");
				}
			} catch (Exception e) {
				if (pw!=null){
					pw.close();
				}
				JOptionPane.showConfirmDialog(this, "Es ist ein Fehler aufgetreten: \n"+e.getMessage());
				return;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(bPrintOne)) {
			this.printOneClicked();
		} else if (e.getSource().equals(bPrintSong)) {
			this.printSetClicked();
		} else if (e.getSource().equals(bPrintVoice)) {
			this.printVoiceClicked();
		}
	}
}
