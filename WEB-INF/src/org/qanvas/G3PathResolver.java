package org.qanvas;

import java.io.Serializable;
import java.net.URLDecoder;

import org.apache.log4j.BasicConfigurator;

import com.monrai.cypher.util.text.Substituter;

public class G3PathResolver implements Serializable {

	static org.apache.log4j.Logger l = org.apache.log4j.Logger.getLogger(org.qanvas.G3PathResolver.class);
	static {
		BasicConfigurator.configure();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -6716303956843546896L;

	public static String getViewData(CanvasView cv, String webid) {
		try {

			String path = cv.getPath();
			// substitute special tokens
			// TODO, need to implemented Query.toString() to replace the need for this String-based substitution
			if (path.startsWith("me")) {
				path = Query.DELIMITER_IRI_START + webid + Query.DELIMITER_IRI_END + path.substring(2);
			}
			else if (path.startsWith("I")) {
				path = Query.DELIMITER_IRI_START + webid + Query.DELIMITER_IRI_END + path.substring(1);
			}

			Object[] goodies = Query.parse(path, cv.getDatatype(), webid);
			Query q = (Query) goodies[0];
			q.setOffset(cv.getOffset());
			q.setLimit(cv.getLimit());

			View v = new View();
			v.setType(cv.getViewType());
			q.setView(v);

			// add a type criteria if the rules for a view are not satistied
			if ((q.getText() == null && q.getCriteriaList().size() == 0) && q.getView().getType().equals(View.TYPE_LIST)) {
				Criteria c = new Criteria(q);
				c.setPropertyIri(Constants.NS_RDF + "type");
				q.getCriteriaList().add(c);
			}
			String data = q.execute();
			if (path != null) {
				path = URLDecoder.decode(path);
				path = Substituter.replace(" ", "+", path); // decode everything except the space char
			}

			// adding a non bound property to the end of a path moves the focus to that trailing node
			// TODO: if there is a non-bound property trailing the path, then that
			// property becomes the new focus, this was done properly by altering the Query and Criteria
			// objects in Query.toQuery(), but that method doesn't have property access to modify the
			// path we're using here (it does a mess of tokening the path); so for now, just strip the DELIMIT_FOCUS from the first token,
			// later, let's rebuild the path using Query.toString()
			// attempt to remove DELIMIT_FOCUS from path string also

			// TODO: this hack assumes that only the root node can have a DELIMIT_FOCUS and therefore, it only checks if the delimiter
			// is present, and if so, removes it from the path; this is not correct syntax
			// since all nodes may have the focus delimiter
			if (goodies[1] != null) {
				path = Substituter.replace(Query.DELIMITER_FOCUS, "", path);
			}

			String lastNonboundProperty = (goodies[1] != null && goodies[1] instanceof Criteria) ? ("lnp=\"" + ((Criteria) goodies[1]).getPropertyIri() + "\" ") : ""; // only include attribute if there was a lnp, otherwise lnp="null" is sent and counted as a lnp
			String lastNonboundPropertyReversed = (goodies[1] != null && goodies[1] instanceof Criteria && ((Criteria) goodies[1]).isReverse()) ? ("lnpr=\"" + ((Criteria) goodies[1]).isReverse() + "\" ") : ""; // need to know if lnp is reversed, check if lnpr is true before forming the string
			String lastProperty = (goodies[2] != null) ? ("lp=\"" + ((Criteria) goodies[2]).getPropertyIri() + "\" ") : "";
			String lastPropertyReversed = (goodies[2] != null && ((Criteria) goodies[2]).isReverse()) ? ("lpr=\"" + ((Criteria) goodies[2]).isReverse() + "\" ") : "";
			String filterValues = (cv.forcedReverse()) ? ("fr=\"" + cv.forcedReverse() + "\" ") : ""; // was the shorthand query used to filter the values of this path?
			String isTextSearch = (q.getText() != null) ? ("isTextSearch=\"true\" ") : ""; // was a text pattern included
			String maximize = (cv.maximize()) ? ("maximize=\"" + cv.maximize() + "\" ") : ""; // fullscreen view?

			// XmlMerge xmlMerge = new DefaultXmlMerge();
			// org.w3c.dom.Document doc = documentBuilder.parse(
			// xmlMerge.merge(
			// new FileInputStream("file1.xml"),
			// servletRequest.getInputStream()));
			// String resultsCount = "0";
			// q.getView().setType(View.TYPE_LIST_COUNT);
			// resultsCount = q.execute();

			// GET THE RESULTS COUNT USING LIST-COUNT
			// // get the factory
			// DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			//
			// try {
			//
			// // Using factory get an instance of document builder
			// DocumentBuilder db = dbf.newDocumentBuilder();
			// // parse using builder to get DOM representation of the XML file
			// org.w3c.dom.Document dom = db.parse(q.getInputStream());
			//
			// // get the root element
			// Element docEle = dom.getDocumentElement();
			// NodeList nl = docEle.getElementsByTagName("facets");
			// if (nl != null && nl.getLength() > 0) {
			// for (int i = 0; i < nl.getLength(); i++) {
			//
			// // get the employee element
			// Element el = (Element) nl.item(i);
			//
			// // get the Employee object
			// // Employee e = getEmployee(el);
			//
			// // add it to list
			// // myEmpls.add(e);
			// }
			// }
			//
			// }
			// catch (ParserConfigurationException pce) {
			// pce.printStackTrace();
			// }
			// catch (SAXException se) {
			// se.printStackTrace();
			// }
			// catch (IOException ioe) {
			// ioe.printStackTrace();
			// }

			if (goodies[1] != null && goodies[1] instanceof String) {
				// for the purpose of the UI, a text restriction counts as a property unless overriden by a subsequent property
				lastProperty = ("lp=\"" + (String) goodies[1] + "\" ");
			}
			// if(lastNonboundProperty.isEmpty()) {
			// // let text properties count as last non-bound properties since they represent multiple results
			// (goodies[1] != null) ? ("lnp=\"" + ((Criteria) goodies[1]).getPropertyIri() + "\" ") : "";
			// }

			String tid = (cv.getTargetId() != null && cv.getTargetId().length() > 0) ? ("tid=\"" + cv.getTargetId() + "\" ") : ""; // only include attribute if there was a tid, otherwise tid="null" is set and counted as a tid
			String smid = (cv.getSourceModuleId() != null && cv.getSourceModuleId().length() > 0) ? ("smid=\"" + cv.getSourceModuleId() + "\" ") : ""; // only include attribute if there was a sid, otherwise sid="null" is set and counted as a sid

			// data = fetchPersonalDataSpaceTriples(q, data);
			data = Substituter.replace("</fct:facets>", "<metadata " + smid + tid + lastNonboundProperty + lastNonboundPropertyReversed + lastProperty + lastPropertyReversed + filterValues + isTextSearch + maximize + "viewType=\"" + cv.getViewType() + "\" reload=\"" + cv.reload() + "\" path=\"" + path + "\" offset=\"" + cv.getOffset() + "\" limit=\"" + cv.getLimit() + "\" append=\"" + cv.append() + "\"/>\n</fct:facets>", data);

			l.info(data);
			return data;

		}
		catch (Exception ex) {
			l.error(ex);
			return "<error>Error occurred: " + ex.getMessage() + ". Check query string.</error>";
		}
	}

	private static String fetchPersonalDataSpaceTriples(Query q, String data) {
		q.setSilo(Query.ENDPOINT_MY_FACETS);
		String myData = q.execute();
		if (myData != null && myData.length() > 0 && myData.indexOf("</fct:result>") >= 0) {
			myData = myData.substring(myData.indexOf("<fct:result") + "<fct:result".length());
			myData = myData.substring(myData.indexOf(">") + 1);
			myData = myData.substring(0, myData.indexOf("</fct:result>"));
			data = Substituter.replace("</fct:facets>", myData + "\n</fct:facets>", data);
		}
		return data;
	}
}
