package com.t_oster.notenschrank.gui;

import javax.swing.JComboBox;

import com.t_oster.notenschrank.data.Archive;

public class SelectVoiceBox extends JComboBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2217011651055295158L;

	public SelectVoiceBox(){
		super(Archive.getInstance().getAvailableVoices());
		this.setEditable(true);
		AutoCompletion.enable(this);
	}
}
