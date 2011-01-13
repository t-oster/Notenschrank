package com.t_oster.notenschrank.gui;

import javax.swing.JComboBox;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.Voice;

public class SelectVoiceBox extends JComboBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2217011651055295158L;


	public SelectVoiceBox(){
		super(Archive.getInstance().getAvailableVoices());
		//this.setEditable(true);
		AutoCompletion.enable(this);
	}
	
	public Voice getSelectedVoice(){
		Object o = this.getSelectedItem();
		if (o instanceof Voice){
			return (Voice) o;
		}
		else if(o != null && o.toString() != ""){//not in the List yet
			Voice v = new Voice(o.toString());
			this.addItem(v);
			//TODO: sort
			this.setSelectedItem(v);
			return v;
		}
		else{
			return null;
		}
	}
	
}
