package com.t_oster.notenschrank.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.SettingsManager;
import com.t_oster.notenschrank.data.Song;
import com.t_oster.notenschrank.data.Voice;


public class SettingsDialog extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1489049124905695755L;
	private JTextField tfArchPath = new JTextField(SettingsManager.getInstance().getArchivePath().getAbsolutePath());
	private JButton bBrowsAP = new JButton("browse");
	private JTextField tfStackPath = new JTextField(SettingsManager.getInstance().getStackPath().getAbsolutePath());
	private JButton bBrowsSP = new JButton("browse");
	//Use String instead of Voice, because HashMap has Problems
	private Map<String,JTextField> numberFields;
	private Map<String,JCheckBox> isDefaultFields;
	private JPanel thiss;
	
	private SettingsDialog(){
		this.setLayout(new GridLayout(0,3));
		this.add(new JLabel("Archiv:"));
		this.add(tfArchPath);
		this.add(bBrowsAP);
		this.add(new JLabel("Gescannte Dateien:"));
		this.add(tfStackPath);
		this.add(bBrowsSP);
		//TODO: pack this onto scroll panes or new Dialogs
		Map<String, Integer> map = SettingsManager.getInstance().getPredefinedNumbers();
		Voice[] voices = Archive.getInstance().getAvailableVoices();
		numberFields = new LinkedHashMap<String, JTextField>();
		if (map!=null && map.size()>0){
			for (Entry<String, Integer> e:map.entrySet()){
				this.add(new JLabel("Standard Anzahl für "));
				this.add(new JLabel(e.getKey()));
				JTextField tf = new JTextField();
				tf.setText(e.getValue().toString());
				this.add(tf);
				numberFields.put(e.getKey().toString(),tf);
			}
		}
		if (voices != null && voices.length >0){
			for (Voice v:voices){
				if (!numberFields.containsKey(v.toString())){
					this.add(new JLabel("Standard Anzahl für "));
					this.add(new JLabel(v.toString()));
					JTextField tf = new JTextField();
					tf.setText("1");
					this.add(tf);
					numberFields.put(v.toString(),tf);
				}
			}
		}
		Set<String> defaultSongs = SettingsManager.getInstance().getDefaultSongs();
		Song[] songs = Archive.getInstance().getAvailableSongs();
		isDefaultFields = new LinkedHashMap<String,JCheckBox>();
		if (defaultSongs != null){
			for (String s:defaultSongs){
				this.add(new JLabel("In aktueller Mappe enthalten"));
				this.add(new JLabel(s));
				JCheckBox b = new JCheckBox();
				b.setSelected(true);
				this.add(b);
				isDefaultFields.put(s,b);
			}
		}
		for (Song s:songs){
			if (!isDefaultFields.containsKey(s.toString())){
				this.add(new JLabel("In aktueller Mappe enthalten"));
				this.add(new JLabel(s.toString()));
				JCheckBox b = new JCheckBox();
				b.setSelected(false);
				this.add(b);
				isDefaultFields.put(s.toString(),b);
			}
		}
		
		this.thiss=this;
		
		bBrowsAP.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Bitte wählen Sie den Archivordner aus");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    //
			    // disable the "All files" option.
			    //
			    chooser.setAcceptAllFileFilterUsed(false);
			    //    
			    if (chooser.showOpenDialog(thiss) == JFileChooser.APPROVE_OPTION) { 
				      tfArchPath.setText(chooser.getSelectedFile().getAbsolutePath());
			      }
			    else {
			      }
			}
			
		});
		
		bBrowsSP.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Bitte wählen Sie den Ordner mit den gescannten Daten aus");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    //
			    // disable the "All files" option.
			    //
			    chooser.setAcceptAllFileFilterUsed(false);
			    //    
			    if (chooser.showOpenDialog(thiss) == JFileChooser.APPROVE_OPTION) { 
				      tfStackPath.setText(chooser.getSelectedFile().getAbsolutePath());
			      }
			    else {
			      }
			}
			
		});
		
	}
	
	/**
	 * Shows the settings dialog and applies changes
	 * if OK pressed
	 * 
	 * this method blocks the current Thread until the Dialog is closed
	 * @param parent
	 */
	public static void showDialog(JFrame parent, String title){
		SettingsDialog sd = new SettingsDialog();
		if (JOptionPane.showConfirmDialog(parent, sd, title, JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
			sd.applySettings();
		}
	}
	
	public void applySettings(){
		SettingsManager sm = SettingsManager.getInstance();
		sm.setArchivePath(tfArchPath.getText());
		sm.setStackPath(tfStackPath.getText());
		Map<String,Integer> nm = new LinkedHashMap<String,Integer>();
		for (Entry<String, JTextField> e:numberFields.entrySet()){
			try{
				nm.put(e.getKey(), Integer.parseInt(e.getValue().getText()));
			}
			catch(NumberFormatException ee){
				nm.put(e.getKey(), 1);
			}
			
		}
		sm.setPredefinedNumbers(nm);
		Set<String> defaultSongs = new LinkedHashSet<String>();
		for (Entry<String, JCheckBox> e:isDefaultFields.entrySet()){
			if (e.getValue().isSelected()){
				defaultSongs.add(e.getKey());
			}
		}
		sm.setDefaultSongs(defaultSongs);
		
		try {
			sm.save();
		} catch (IOException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Einstellungen\nDie Änderrungen sind nur bis zum Beenden des Programms wirksam.");
		}
 	}
}
