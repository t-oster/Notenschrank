package com.t_oster.notenschrank.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerListModel;
import javax.swing.table.AbstractTableModel;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.Sheet;
import com.t_oster.notenschrank.data.Voice;

public class PrintSetDialogPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2646439482396979485L;
	private SelectSongBox bSong;
	private JTable tVoices;
	private AbstractTableModel tVoicesModel;
	//private JSpinner sNumber;
	//private SpinnerListModel lm;
	private Voice[] availableVoices;
	private int[] selectedNumbers;
	
	
	public PrintSetDialogPanel(){
		//this.setLayout(new GridLayout(0,2));
		bSong = new SelectSongBox(false);
		bSong.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				availableVoices = Archive.getInstance().getAvailableVoices(bSong.getSelectedSong());
				selectedNumbers = new int[availableVoices.length];
				tVoicesModel.fireTableDataChanged();
			}
			
		});
		tVoicesModel = new AbstractTableModel(){

			public String getColumnName(int column){
				return column==0?"Stimme":"Anzahl";
			}
			
			public boolean isCellEditable(int r, int c){
				return (c==1);
			}
			
			 public void setValueAt(Object value, int row, int col) {
				 	if (col==1){
				        selectedNumbers[row]=Integer.parseInt((String) value);
				        fireTableCellUpdated(row, col);
				 	}
			    }
			
			@Override
			public int getColumnCount() {
				return 2;
			}

			@Override
			public int getRowCount() {
				return availableVoices==null?0:availableVoices.length;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				if (columnIndex==0){
					return availableVoices[rowIndex];
				}
				else{
					return selectedNumbers[rowIndex];
				}
			}
			
		};
		tVoices = new JTable(tVoicesModel);
		//lm=new SpinnerListModel(new String[]{"1","2","3","4","5","6","7","8","9","10"});
		//sNumber = new JSpinner(lm);
		add(new JLabel("Stück"));
		add(bSong);
		tVoices.setFillsViewportHeight(true);
		add(new JScrollPane(tVoices));
	}
	
}
