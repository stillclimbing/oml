package me.xm.oml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jwbroek.cuelib.CueSheet;

public class Album {
	long pathHash;
	
	String name;
	String artist;
	String label;
	String folderName;
	String coverThumbFile;
	String coverFile;
	String discId;
	long lastModified;
	String desc;
	int totalDiscs;
	
	public int getTotalDiscs() {
		return totalDiscs;
	}

	public void setTotalDiscs(int totalDiscs) {
		this.totalDiscs = totalDiscs;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	List<String> tags;
	String tagsStr;
	
	public long getPathHash() {
		return pathHash;
	}

	public void setPathHash(long pathHash) {
		this.pathHash = pathHash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public String getCoverThumbFile() {
		return coverThumbFile;
	}

	public void setCoverThumbFile(String coverThumbFile) {
		this.coverThumbFile = coverThumbFile;
	}

	public String getCoverFile() {
		return coverFile;
	}

	public void setCoverFile(String coverFile) {
		this.coverFile = coverFile;
	}

	public String getDiscId() {
		return discId;
	}

	public void setDiscId(String discId) {
		this.discId = discId;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTagsStr(String tagStr) {
		if(tagStr!=null){
			String[] ss = tagStr.split(",");
			for(String s:ss)
				this.addTag(s);
		}
	}
	
	public String getTagsStr(){
		if(this.tags==null) return null;
		StringBuffer sb = new StringBuffer();
		for(String s:this.tags){
			sb.append(s+",");
		}
		String s = sb.toString();
		if(sb.length()>0)
			return s.substring(0,s.length()-1);
		return null;
	}

	public void addTag(String tag) {
		if(this.tags==null)
			this.tags = new ArrayList<String>();
		this.tags.add(tag);
	}

	@Override
	public String toString() {
		  StringBuilder result = new StringBuilder();
		  String newLine = System.getProperty("line.separator");

		  result.append( this.getClass().getName() );
		  result.append( " Object {" );
		  result.append(newLine);

		  //determine fields declared in this class only (no fields of superclass)
		  Field[] fields = this.getClass().getDeclaredFields();

		  //print field names paired with their values
		  for ( Field field : fields  ) {
		    result.append("  ");
		    try {
		      result.append( field.getName() );
		      result.append(": ");
		      //requires access to private field:
		      result.append( field.get(this) );
		    } catch ( IllegalAccessException ex ) {
		      System.out.println(ex);
		    }
		    result.append(newLine);
		  }
		  result.append("}");

		  return result.toString();
	}
	
}
