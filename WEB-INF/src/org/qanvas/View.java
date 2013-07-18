package org.qanvas;

public class View {

	String type = "";

	final static String TYPE_PROPERTIES = "properties";
	final static String TYPE_PROPERTIES_IN = "properties-in";
	final static String TYPE_CLASSES = "classes";
	final static String TYPE_TEXT = "text";
	final static String TYPE_LIST = "list";
	final static String TYPE_LIST_COUNT = "list-count";
	final static String TYPE_ALPHABET = "alphabet";
	final static String TYPE_GEO = "geo";
	final static String TYPE_YEARS = "years";
	final static String TYPE_MONTHS = "months";
	final static String TYPE_WEEKS = "weeks";
	final static String TYPE_DESCRIBE = "describe";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
