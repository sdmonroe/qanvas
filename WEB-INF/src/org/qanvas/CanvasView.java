package org.qanvas;

import java.net.URLDecoder;

import com.monrai.cypher.util.text.Substituter;

public class CanvasView {

	int limit;
	int offset;
	String viewType;
	String path;
	boolean reload;
	String targetId;
	String sourceModuleId;
	boolean forcedReverse;
	boolean append;
	String datatype;
	boolean maximize;

	public boolean maximize() {
		return maximize;
	}

	public void setMaximize(boolean maximize) {
		this.maximize = maximize;
	}

	public String getDatatype() {
		if (datatype == null || datatype.equalsIgnoreCase("undefined"))
			return "http://www.w3.org/2001/XMLSchema#string"; // TODO for now, assume the value is a string when no explicit value is specified
		// need a way to make specify datatype in the path where the DELIMIT_EQ is specified
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = desanitize(datatype);
	}

	public boolean append() {
		return append;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}

	public boolean forcedReverse() {
		return forcedReverse;
	}

	public void setForcedReverse(boolean filterValues) {
		this.forcedReverse = filterValues;
	}

	public String getSourceModuleId() {
		return sourceModuleId;
	}

	public void setSourceModuleId(String sourceId) {
		this.sourceModuleId = sourceId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetNodeId) {
		this.targetId = targetNodeId;
	}

	public boolean reload() {
		return reload;
	}

	public void setReload(boolean reload) {
		this.reload = reload;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = desanitize(path);
	}

	static String desanitize(String path) {
		path = URLDecoder.decode(path);
		path = Substituter.replace("---___apos___---", "\'", path);
		path = Substituter.replace("---___hasg___---", "#", path);
		path = Substituter.replace("&#39;", "\'", path);
		// path = Substituter.replace(" ", "+", path); // decode everything accept the space characters
		return path; // hash must be encoded before arriving to server or else the server will strip it, this replace should happen in service.jsp
	}
}
