package com.t_oster.notenschrank.gui;

import javax.swing.JComboBox;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.Song;

public class SelectSongBox extends JComboBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5468068515401007273L;
	private boolean reloadImportant=false;
	
	
	public SelectSongBox(){
		super(Archive.getInstance().getAvailableSongs());
		//this.setEditable(true);
		AutoCompletion.enable(this);
	}
	
	public Song getSelectedSong(){
		Object o = this.getSelectedItem();
		if (o instanceof Song){
			return (Song) o;
		}
		else if(o != null && o.toString() != ""){//not in the List yet
			reloadImportant = true;
			return new Song(o.toString());
		}
		else{
			return null;
		}
	}
	
	public void reload(){
		if (reloadImportant){
			int i=this.getSelectedIndex();
			this.removeAllItems();
			for (Song s:Archive.getInstance().getAvailableSongs()){
				this.addItem(s);
			}
			this.setSelectedIndex(i);
			reloadImportant=false;
		}
		else{
			System.out.println("reload dropped");
		}
	}
}
