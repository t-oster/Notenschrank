package com.t_oster.notenschrank.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.SettingsManager;
import com.t_oster.notenschrank.data.Sheet;
import com.t_oster.notenschrank.data.Song;
import com.t_oster.notenschrank.data.Voice;

public class PrintVoiceDialogPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2646439482396979485L;
	private SelectVoiceBox bVoice;
	private JTable tSongs;
	private AbstractTableModel tSongsModel;
	private JCheckBox cbPreview;
	private Song[] availableSongs;
	private boolean[] selectedNumbers;
	private Voice[] selectedVoices;
	
	public PrintVoiceDialogPanel(){
		//this.setLayout(new GridLayout(0,2));
		bVoice = new SelectVoiceBox(false);
		availableSongs = Archive.getInstance().getAvailableSongs();
		selectedNumbers = new boolean[availableSongs.length];
		selectedVoices = new Voice[availableSongs.length];
		for (int i=0;i<availableSongs.length;i++){
			if (Archive.getInstance().contains(availableSongs[i], bVoice.getSelectedVoice())){
				selectedVoices[i]=bVoice.getSelectedVoice();
			}
		}
		//do preselection
		Set<String> defaultSongs = SettingsManager.getInstance().getDefaultSongs();
		for (int i=0;i<selectedNumbers.length;i++){
			selectedNumbers[i]=( defaultSongs.contains(availableSongs[i].toString()) );
		}
		bVoice.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				availableSongs = Archive.getInstance().getAvailableSongs(bVoice.getSelectedVoice());
				selectedNumbers = new boolean[availableSongs.length];
				selectedVoices = new Voice[availableSongs.length];
				for (int i=0;i<availableSongs.length;i++){
					if (Archive.getInstance().contains(availableSongs[i], bVoice.getSelectedVoice())){
						selectedVoices[i]=bVoice.getSelectedVoice();
					}
				}
				//do preselection
				Set<String> defaultSongs = SettingsManager.getInstance().getDefaultSongs();
				for (int i=0;i<selectedNumbers.length;i++){
					selectedNumbers[i]=( defaultSongs.contains(availableSongs[i].toString()) );
				}
				tSongsModel.fireTableDataChanged();
			}
			
		});
		tSongsModel = new AbstractTableModel(){

			/**
			 * 
			 */
			private static final long serialVersionUID = -1328906876906673675L;
			private String[] columns={"Stück","auswählen","Stimme"};
			@SuppressWarnings("rawtypes")
			private Class[] classes={String.class,Boolean.class,Voice.class};
			
			
			public String getColumnName(int column){
				return columns[column];
			}
			
			public boolean isCellEditable(int r, int c){
				if (c==0){
					return false;
				} else if (c==1){
					return true;
				}
				else if (c==2){
					return (selectedVoices[r]==null || selectedVoices[r]!=bVoice.getSelectedVoice());
				}
				return false;
			}
			
			 public void setValueAt(Object value, int row, int col) {
				 	if (col==1 && value instanceof Boolean){
				        selectedNumbers[row]=(Boolean) value;
				        fireTableCellUpdated(row, col);
				 	}
				 	else if (col==2){
				 		if (value instanceof Voice){
				 			selectedVoices[row]=(Voice) value;
				 		}
				 		else if (value instanceof String){
				 			selectedVoices[row]=null;
				 		}
				 		fireTableCellUpdated(row,col);
				 	}
			    }
			
			@Override
			public int getColumnCount() {
				return 3;
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int c) {
			        return classes[c];
			}
			
			@Override
			public int getRowCount() {
				return availableSongs==null?0:availableSongs.length;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if (columnIndex==0){
					return availableSongs[rowIndex];
				}
				else if (columnIndex==1){
					return selectedNumbers[rowIndex];
				}
				else {
					return selectedVoices[rowIndex]==null?"Bitte auswählen":selectedVoices[rowIndex];
				}
			}
			
		};
		tSongs = new JTable(tSongsModel){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public TableCellEditor getCellEditor(int row, int column){
				if (column==2){
					JComboBox edit=new JComboBox();
					edit.addItem("Bitte auswählen");
					for (Voice v:Archive.getInstance().getAvailableVoices(availableSongs[row])){
						edit.addItem(v);
					}
					return new DefaultCellEditor(edit);
				}
				return super.getCellEditor(row,column);
			}
		};
		tSongs.setFillsViewportHeight(true);
		cbPreview = new JCheckBox("Druckvorschau");
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel tmp = new JPanel();
		tmp.add(new JLabel("Stimme"));
		tmp.add(bVoice);
		add(tmp);
		add(new JScrollPane(tSongs));
		add(cbPreview);
	}
	
	public boolean isPreviewSelected(){
		return cbPreview.isSelected();
	}
	
	public Sheet[] getSelectedSheets() throws IOException{
		LinkedList<Sheet> result = new LinkedList<Sheet>();
		for (int i=0;i<availableSongs.length;i++){
			if (selectedNumbers[i] && selectedVoices[i]!=null){
				result.add(Archive.getInstance().getSheet(availableSongs[i], selectedVoices[i]));
			}
		}
		return result.toArray(new Sheet[0]);
	}
	
}
