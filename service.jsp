<%String path = request.getParameter("path");
	String viewType = request.getParameter("viewType");
	String offset = request.getParameter("offset");
	String limit = request.getParameter("limit");
	String reload = request.getParameter("reload");
	String tid = request.getParameter("tid");
	String smid = request.getParameter("smid");
	String webid = request.getParameter("webid");
	String append = request.getParameter("append");
	String datatype = request.getParameter("datatype");
	String maximize = request.getParameter("maximize");

	org.qanvas.CanvasView cv = new org.qanvas.CanvasView();
	cv.setViewType(viewType);
	cv.setPath(path);
	cv.setTargetId(tid);
	cv.setSourceModuleId(smid);
	if(request.getParameter("offset") != null) cv.setOffset(Integer.parseInt(offset));
	if(request.getParameter("limit") != null) cv.setLimit(Integer.parseInt(limit));
	if(request.getParameter("reload") != null) cv.setReload(request.getParameter("reload").equals("true"));
	if(request.getParameter("fr") != null) cv.setForcedReverse(request.getParameter("fr").equals("true"));
	if(datatype != null) cv.setDatatype(datatype);
	if(maximize != null) cv.setMaximize(maximize.equals("true"));
	cv.setAppend(append != null);
	if(path != null && path.length() > 0){
		response.setContentType("text/xml");
		String resolver = org.qanvas.G3PathResolver.getViewData(cv, webid);
		out.write(resolver);
		return;
	}%>
