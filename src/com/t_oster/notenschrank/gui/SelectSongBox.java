package com.t_oster.notenschrank.gui;

import com.t_oster.notenschrank.data.Archive;
import com.t_oster.notenschrank.data.Song;
import javax.swing.JComboBox;

public class SelectSongBox extends JComboBox {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5468068515401007273L;	
	
	public SelectSongBox(boolean autocomplete, boolean editable){
		super(Archive.getInstance().getAvailableSongs());
		if (autocomplete){
			AutoCompletion.enable(this, editable);
		}
	}
	
	public Song getSelectedSong(){
		Object o = this.getSelectedItem();
		if (o instanceof Song){
			return (Song) o;
		}
		else if(o != null && o.toString() != ""){//not in the List yet
			Song s = new Song(o.toString());
			this.addItem(s);
			//TODO: sort
			this.setSelectedItem(s);
			return s;
		}
		else{
			return null;
		}
	}
	
}
