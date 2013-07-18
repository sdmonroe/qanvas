
function loadMe(data, path, viewContent, sourceViewId){
	viewContent.attr("class", "dashboard");
	//alert('here is here');
	var tab = jQuery(document.createElement("table"));
	tab.attr("class", "items");
	tab.attr("cellspacing", "0");
	viewContent.append(tab);
	
	var row = jQuery(document.createElement("tr"));
	tab.append(row);
	
	var cell = jQuery(document.createElement("td"));
	cell.attr("class", "item");
	cell.attr("id", Math.random());
	var getShopping = path + DELIMIT_NODE + DELIMIT_REVERSE + "foaf:depiction" + DELIMIT_NODE + "gr:seeks";
	var linkid = Math.random();
	cell.append("<a id=\""+linkid+"\" href=\"#\" onclick=\"javascript:getViewForLink('"+sourceViewId+"', '', 'http://www.google.com', 'list');return false;\"></a><br/><h1>Profile</h1>");
	var path;
	var webid = document.getElementById("webid").value;
	jQuery.get(baseUrl + "?tid="+linkid+"&limit=25&offset=0&viewType=list&path=" + DELIMIT_IRI_START + webid + DELIMIT_IRI_END + DELIMIT_NODE + "foaf:img", {}, loadPhoto, 'xml');
	row.append(cell);

	cell = jQuery(document.createElement("td"));
	cell.attr("class", "item");
	cell.attr("id", Math.random());
	var getFriends = path + DELIMIT_NODE + "foaf:knows";
	cell.append("<a href=\"#\" onclick=\"javascript:getViewForLink('"+sourceViewId+"', '', '" + getFriends + "', 'list');return false;\"><img src=\"img/friends2.png\"/></a><br/><h1>Friends</h1>");
	row.append(cell);

	// add a second row
	row = jQuery(document.createElement("tr"));
	tab.append(row);
	
	cell = jQuery(document.createElement("td"));
	cell.attr("class", "item");
	cell.attr("id", Math.random());
	var getNote = path + DELIMIT_NODE + DELIMIT_REVERSE + "foaf:maker" + DELIMIT_NODE + "rdf:type" + DELIMIT_EQ + DELIMIT_IRI_START + "http://purl.org/ontology/bibo/Note" + DELIMIT_IRI_END;
	cell.append("<a href=\"#\" onclick=\"javascript:getViewForLink('"+sourceViewId+"', '', '" + getNote + "', 'list');return false;\"><img src=\"img/notes.png\"/></a><br/><h1>Writings</h1>");
	row.append(cell);
	
	cell = jQuery(document.createElement("td"));
	cell.attr("class", "item");
	cell.attr("id", Math.random());
	var getBookmarks = path + DELIMIT_NODE + DELIMIT_REVERSE + "foaf:maker" + DELIMIT_NODE + "rdf:type" + DELIMIT_EQ + "q:Bookmark";
	cell.append("<a href=\"#\" onclick=\"javascript:getViewForLink('"+sourceViewId+"', '', '" + getBookmarks + "', 'list');return false;\"><img src=\"img/bookmarks.png\"/></a><br/><h1>Bookmarks</h1>");
	row.append(cell);

	// add a third row
	row = jQuery(document.createElement("tr"));
	tab.append(row);
	
	cell = jQuery(document.createElement("td"));
	cell.attr("class", "item");
	cell.attr("id", Math.random());
	var getFavs = path + DELIMIT_NODE + DELIMIT_REVERSE + "foaf:maker" + DELIMIT_NODE + DELIMIT_IRI_START + "tag:decoy@iki.fi,2000:rdf:foaf-ext:1.0:likes" + DELIMIT_IRI_END;
	cell.append("<a href=\"#\" onclick=\"javascript:getViewForLink('"+sourceViewId+"', '', '" + getFavs + "', 'list');return false;\"><img src=\"img/favorites.png\"/></a><br/><h1>Likes</h1>");
	row.append(cell);
	
	cell = jQuery(document.createElement("td"));
	cell.attr("class", "item");
	cell.attr("id", Math.random());
	var getShopping = path + DELIMIT_NODE + DELIMIT_REVERSE + "foaf:maker" + DELIMIT_NODE + "gr:seeks";
	cell.append("<a href=\"#\" onclick=\"javascript:getViewForLink('"+sourceViewId+"', '', '" + getShopping + "', 'list');return false;\"><img src=\"img/shopping.png\"/></a><br/><h1>Wants & Needs</h1>");
	row.append(cell);

	// add a fourth row
	row = jQuery(document.createElement("tr"));
	tab.append(row);
	cell = jQuery(document.createElement("td"));
	cell.attr("class", "item");
	cell.attr("id", Math.random());
	var getShopping = path + DELIMIT_NODE + DELIMIT_REVERSE + "foaf:maker" + DELIMIT_NODE + "gr:seeks";
	cell.append("<a href=\"#\" onclick=\"javascript:getViewForLink('"+sourceViewId+"', '', 'http://www.google.com', 'list');return false;\"><img width=\"92\" src=\"img/google.png\"/></a><br/><h1>Web Search</h1>");
	row.append(cell);

	cell = jQuery(document.createElement("td"));
	cell.attr("class", "item");
	cell.attr("id", Math.random());
	var getHistory = path + DELIMIT_NODE + "q:visited";
	cell.append("<a href=\"#\" onclick=\"javascript:getViewForLink('"+sourceViewId+"', '', '" + getHistory + "', 'list');return false;\"><img src=\"img/history2.png\"/></a><br/><h1>History</h1>");
	row.append(cell);
}

