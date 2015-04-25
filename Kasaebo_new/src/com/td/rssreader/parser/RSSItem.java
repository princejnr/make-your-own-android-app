package com.td.rssreader.parser;

import java.io.Serializable;

public class RSSItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private String _title = null;
	private String _description = null;
	private String _date = null;
	private String _image = null;

	public void setTitle(String title) {
		_title = title;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setDate(String pubdate) {
		_date = pubdate;
	}

	public void setImage(String image) {
		_image = image;
	}

	public String getTitle() {
		return _title;
	}

	public String getDescription() {
		return _description;
	}

	public String getDate() {
		return _date;
	}

	public String getImage() {
		return _image;
	}

}
