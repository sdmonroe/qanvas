package org.qanvas;

import java.net.URLDecoder;

import com.monrai.cypher.util.text.Substituter;

public class Value {

	String lang = "";
	String datatype = "";
	String operator = Value.OP_EQ;
	String content;
	boolean hasIri;

	final static String OP_EQ = "=";
	final static String OP_GT = ">";
	final static String OP_LT = "<";
	final static String OP_GT_EQ = ">=";
	final static String OP_LT_EQ = "<=";

	final static String LANG_EN = "en";

	public Value() {
		this.setLang(LANG_EN); // english by default
	}

	public static final String DATATYPE_URI = "uri";

	public boolean hasIri() {
		return hasIri;
	}

	public void setHasIri(boolean hasIri) {
		this.hasIri = hasIri;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if (this.datatype.equals(Value.DATATYPE_URI)) {
			if (this.content != null) {
				this.content = URLDecoder.decode(this.content);
				this.content = Substituter.replace(" ", "%20", this.content); // decode everything except the space char
			}
		}
		this.content = content;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		if (datatype == null)
			return;
		if (datatype.equals(Value.DATATYPE_URI)) {
			if (this.content != null) {
				this.content = URLDecoder.decode(this.content);
				this.content = Substituter.replace(" ", "%20", this.content); // decode everything except the space char
			}
		}
		this.datatype = datatype;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

}
