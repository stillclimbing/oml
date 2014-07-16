package me.stillclimbing.cuelib;

import java.util.Arrays;

import com.google.gson.Gson;

public class CueSheetExt {
	String label;
	String series;
	int discNumber = 1;
	int totalDisc = 1;
	String[] tags;

	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getSeries() {
		return series;
	}


	public void setSeries(String series) {
		this.series = series;
	}


	public int getDiscNumber() {
		return discNumber;
	}


	public void setDiscNumber(int discNumber) {
		this.discNumber = discNumber;
	}


	public int getTotalDisc() {
		return totalDisc;
	}


	public void setTotalDisc(int totalDisc) {
		this.totalDisc = totalDisc;
	}


	public String[] getTags() {
		return tags;
	}


	public void setTags(String[] tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return "CueSheetExt [label=" + label + ", series=" + series
				+ ", discNumber=" + discNumber + ", totalDisc=" + totalDisc
				+ ", tags=" + Arrays.toString(tags) + "]";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CueSheetExt ext = new CueSheetExt();
		ext.setLabel("Decca");
		ext.setSeries("Decca Legends");
		ext.setTags(new String[]{"penguin","grammo100"});
		
		Gson gson = new Gson();
		String json = gson.toJson(ext);
		System.out.println(json);
		
		System.out.println(gson.fromJson(json, CueSheetExt.class));

	}

}
