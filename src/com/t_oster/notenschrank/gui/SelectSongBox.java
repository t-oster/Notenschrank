package com.t_oster.notenschrank.gui;

import javax.swing.JComboBox;

import com.t_oster.notenschrank.data.Archive;

public class SelectSongBox extends JComboBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5468068515401007273L;
	public SelectSongBox(){
		super(Archive.getInstance().getAvailableSongs());
		this.setEditable(true);
		AutoCompletion.enable(this);
	}
}
