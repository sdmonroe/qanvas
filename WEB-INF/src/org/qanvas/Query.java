package org.qanvas;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.ecs.xml.XML;
import org.apache.ecs.xml.XMLDocument;

import com.monrai.cypher.util.text.Substituter;

/**
 * Class Query
 * 
 * examples of paths:
 * <ul>
 * <li>!this is raw text linked to the subject|{ggg://bind.uri.value.of.subject};a:property;{ggg://a.uri.property.com}|{the:boundValueofTheProperty};*the:inversePropertyToMatch</li>
 * <li>!anything = match any string of characters to any field</li>
 * <li>anything = match any string of charters to the rdfs:label field</li>
 * <li>this will only match text in comments${dbpedia:comment}</li>
 * <li>{a:IRI}&&{another:IRI}&&{makes:anOrderedANDListOfIRIs}</li>
 * </ul>
 * 
 * The keyword "anything" as the text will cause no restriction to be placed on the text match. The same is achieved by placing no text before the first ; path delimiter;
 * 
 * The presence of a URI in the path eliminates all criteria proceeding that URI.
 * 
 * Selectors point forward a node, filters point back a node.
 * 
 * @author smonroe
 * 
 */
public class Query {

	static org.apache.log4j.Logger l = org.apache.log4j.Logger.getLogger(org.qanvas.Query.class);

	public Query(String silo) {
		this.silo = silo;
	}

	static final String DELIMITER_NODE = ";";
	static final String DELIMITER_IRI_START = "{";
	static final String DELIMITER_IRI_END = "}";
	static final String DELIMITER_REVERSE = "*";
	static final String DELIMITER_EQ = "|";
	static final String DELIMITER_QNAME = ":";
	static final String DELIMITER_FOCUS = "?";
	static final String DELIMITER_ALL_TEXT = "!";
	static final String DELIMITER_ONLY_TEXT = "$";
	static final String DELIMITER_ORDER_ASC = "@";
	// static final String DELIMITER_NODE_CONNECTOR_AND = "&&";

	// static final String ENDPOINT_LOD_FACETS = "http://dbpedia-live.openlinksw.com/fct/service";
	static final String ENDPOINT_LOD_FACETS = "http://lod.openlinksw.com/fct/service";
	// static final String ENDPOINT_MY_FACETS = "http://nevisian.dyndns.org/fct/service";
	static final String ENDPOINT_MY_FACETS = "http://localhost:8890/fct/service";
	static final String ENDPOINT_UPDATES = "http://localhost:8890/sparql";
	static String NS_OPENLINK_FACETS = "http://openlinksw.com/services/facets/1.0";
	View view = null; // a query has at most one view, but a view can be associated with the Query or one of its Criteria, hold the view here and link to owner by their hasView field
	boolean hasView = false;
	boolean isIriList = false; // does this Query represent a list of IRIs?
	List iriList = new ArrayList();

	String silo;

	public String getSilo() {
		return silo;
	}

	public void setSilo(String silo) {
		this.silo = silo;
	}

	public List getIriList() {
		return iriList;
	}

	public void setIriList(List iriList) {
		this.iriList = iriList;
	}

	public boolean isIriList() {
		return isIriList;
	}

	public void setIriList(boolean isIRIList) {
		this.isIriList = isIRIList;
	}

	public boolean hasView() {
		return hasView;
	}

	public void setHasView(boolean hasView) {
		this.hasView = hasView;
	}

	// these two are used by the view element, but since only one view per
	// query, they are placed in this
	// class for easier acccess, since the view can move around
	int limit;
	int offset;

	Text text = null;
	List<Criteria> criteriaList = new Vector<Criteria>();
	Value value = null; // the other object participating in the triple

	static final HashMap<String, String> qnameMap = new HashMap<String, String>();
	static {
		qnameMap.put("rdf", Constants.NS_RDF);
		qnameMap.put("contact", Constants.NS_CONTACT);
		qnameMap.put("dc", Constants.NS_DC);
		qnameMap.put("dcterms", Constants.NS_DCTERMS);
		qnameMap.put("gr", Constants.NS_GR);
		qnameMap.put("rdfs", Constants.NS_RDFS);
		qnameMap.put("xsd", Constants.NS_XSD);
		qnameMap.put("q", Constants.NS_QANVAS);
		qnameMap.put("foaf", Constants.NS_FOAF);
		qnameMap.put("dbpprop", Constants.NS_DBPROP);
		qnameMap.put("dbp", Constants.NS_DBPROP);
		qnameMap.put("dbpedia", Constants.NS_DBPEDIA);
		qnameMap.put("sioc", Constants.NS_SIOC);
		qnameMap.put("moat", Constants.NS_MOAT);
		qnameMap.put("owl", Constants.NS_OAT);
		qnameMap.put("terms", Constants.NS_TERMS);
		qnameMap.put("skos", Constants.NS_SKOS);
		qnameMap.put("wn2", Constants.NS_WN2);
		qnameMap.put("wn3", Constants.NS_WN3);
		qnameMap.put("free", Constants.NS_FREE);
	}
	int timeout = 8000;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public static Object[] toQuery(String path, String datatype, String webid) {

		// SentenceTokenizer st = new SentenceTokenizer(path,
		// DELIMITER_IRI_START + DELIMITER_IRI_END);
		// check for syntax <uri>.node1.node2..., i.e. is the first node a URI?

		boolean iriRootHasFocus = false;

		try {
			URI uri = new URI(path); // is valid IRI syntax?
			if (uri.getHost() != null && uri.getScheme() != null)
				return toQuery(path, "", iriRootHasFocus, datatype, webid);
		}
		catch (URISyntaxException uriex) {
			// continue to attempt to parse the IRI
		}

		// first, if the DELIMIT_FOCUS was assigned to a IRI subject, remove it for parsing purposes
		// then inform toQuery() through parameter
		if (path.indexOf(DELIMITER_FOCUS + DELIMITER_IRI_START) == 0) {
			path = path.substring(1);
			iriRootHasFocus = true;
		}
		if (path.indexOf(DELIMITER_IRI_START) != 0) {
			return toQuery(null, path, iriRootHasFocus, datatype, webid);
		}
		else {
			// path spans from index after first start-delimit, to index after
			// first end-delimit
			// the path will be the remainder of the string, make path null-safe
			return toQuery(path.substring(1, path.indexOf(DELIMITER_IRI_END)), (path.indexOf(DELIMITER_IRI_END) == path.length() - 1) ? "" : path.substring(path.indexOf(DELIMITER_IRI_END) + 1), iriRootHasFocus, datatype, webid);
		}
	}

	public static Object[] toQuery(String iri, String path, boolean iriRootHasFocus, String datatype, String webid) {
		Object[] ret = new Object[3];
		Query q = new Query(ENDPOINT_LOD_FACETS);
		StringTokenizer st = new StringTokenizer(path, DELIMITER_NODE);

		// ensure at least one iri or one path node
		if (!st.hasMoreTokens() && iri == null) {
			l.error("Path can not be empty.");
			return ret;
		}

		// the iri is the bound subject of the query
		if (iri != null) {
			Value value = new Value();
			value.setContent(iri);
			value.setDatatype(Value.DATATYPE_URI);
			q.setValue(value);

			// check if the iri root was assigned the focus
			q.setHasView(iriRootHasFocus);
		}

		// if iri=null, then there is no value bound to the subject (the first node in the path), then
		// assume that the entire path was passed as the 'path' argument of this method, and that that the first node is the text for this path
		// note: if the path stared with {http://some.uri.here}, then it was stripped and passed as the iri argument of this method
		// and the suffix was passed as path
		else {
			String root = st.nextToken();
			root = root.trim();

			// TODO: these next two blocks don't support qname syntax for uris, but it should
			// check if a iri is bound to the root
			// strip any iri values from root before processing
			// iri can only come after the EQ operand, never before!
			if (root.indexOf(DELIMITER_EQ) > 0) {
				Value value = new Value();
				// start delimit for uri must follow the eq delimit
				if (root.indexOf(Query.DELIMITER_IRI_START) > root.indexOf(DELIMITER_EQ) && root.lastIndexOf(Query.DELIMITER_IRI_END) >= root.length() - 1) {
					String propIri = root.substring(root.indexOf(Query.DELIMITER_IRI_START) + 1, root.length() - 1);
					value.setContent(propIri);
					value.setDatatype(Value.DATATYPE_URI);
					q.setValue(value);
					root = root.substring(0, root.indexOf(Query.DELIMITER_EQ));

					ret[1] = propIri; // for the purpose of the UI, a text restriction counts as a property unless overriden by a subsequent property
				}
			}

			String only_text_uri = null;
			// processing the DELIMIT ONLY TEXT is like processing EQ
			// strip out the iri and save it for later after the Text object is created
			// remove the iri from the root
			if (root.indexOf(DELIMITER_ONLY_TEXT) > 0) {
				// start delimit for uri must follow the eq delimit
				if (root.indexOf(Query.DELIMITER_IRI_START) > root.indexOf(DELIMITER_ONLY_TEXT) && root.lastIndexOf(Query.DELIMITER_IRI_END) >= root.length() - 1) {
					only_text_uri = root.substring(root.indexOf(Query.DELIMITER_IRI_START) + 1, root.length() - 1);
					root = root.substring(0, root.indexOf(Query.DELIMITER_ONLY_TEXT));

					ret[1] = only_text_uri; // for the purpose of the UI, a text restriction counts as a property unless overriden by a subsequent property
				}
			}

			Text t = new Text();
			// set text after root was checked and appropriately stripped
			q.setText(t);
			q.getText().setRestrictToLabels(true); // search all text fields by default

			while (root.startsWith(DELIMITER_FOCUS) || root.startsWith(DELIMITER_ALL_TEXT)) {
				// check if root node was given explicit focus, otherwise, last node
				// gets focus
				if (root.startsWith(DELIMITER_FOCUS)) {
					root = root.substring(1); // remove DELIMIT_FOCUS operand
					q.setHasView(true);
				}

				// check if rdfs:label restriction on root was removed
				// if not, restrict text to rdfs:label by default
				// perform after instaintiating the Text object above so that we can
				// access the restricted flag, but before setting the text

				// let DELIMIT_ONLY_TEXT override DELIMIT_ALL_TEXT since it occurs after
				if (root.startsWith(DELIMITER_ALL_TEXT)) {
					root = root.substring(1); // remove operand
					q.getText().setRestrictToLabels(false); // restrict filter to rdfs:label
				}
			}

			// let DELIMIT_ONLY_TEXT override DELIMIT_ALL_TEXT since it occurs after
			// therefore this check occurs after the ALL_TEXT restriction is set
			if (only_text_uri != null) {
				StringTokenizer prop = new StringTokenizer(only_text_uri, DELIMITER_QNAME);
				if (prop.countTokens() == 2) {
					String qname = prop.nextToken();
					String suffix = prop.nextToken();
					String prefix = Query.qnameMap.get(qname);
					t.setPropertyIri(prefix + suffix);
					// nodeValue.setContent(propIri);
					// nodeValue.setDatatype(Value.DATATYPE_URI);
				}
				else {
					t.setPropertyIri(only_text_uri);
				}
				t.setRestrictToLabels(false); // turn off the label restriction also
			}

			// finally, set the text of the Text object to the scrubbed 'root'
			t.setContent(root);

		}

		// root has been taken off from the tokenizer,
		// if only a root was specified, then set this root as the focus
		if (!st.hasMoreTokens()) {
			q.setHasView(true);
		}

		List<Criteria> targetList = q.getCriteriaList();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			// check for property direction reversal
			Criteria c = new Criteria(q);

			// check if this node has focus, otherwise, last node gets focus
			if (token.startsWith(DELIMITER_FOCUS)) {
				token = token.substring(1);
				c.setHasView(true);
				q.setHasView(false);
			}

			// check if the property is reversed
			if (token.startsWith(DELIMITER_REVERSE)) {
				token = token.substring(1);
				c.setReverse(true);
			}

			// check if the property is reversed
			if (token.startsWith(DELIMITER_ORDER_ASC)) {
				token = token.substring(1);
				c.setIsAsc(true);
			}

			// if eq marker found set the value
			// only check of index of EQ after index 0, not allowed at 0, i.e.
			// it must have a left-hand operand
			token = token.trim();
			boolean isBound = false;

			// proceed to set the value portion of the criteria
			if (isBound = token.indexOf(DELIMITER_EQ) > 0) {
				String value = token.substring(token.indexOf(DELIMITER_EQ) + 1);
				token = token.substring(0, token.indexOf(DELIMITER_EQ));
				StringTokenizer prop = new StringTokenizer(value, DELIMITER_QNAME);
				String propIri = "";
				Value nodeValue = new Value();
				// strip off initial and trailing URI delimiters
				if (value.indexOf(Query.DELIMITER_IRI_START) == 0 && value.indexOf(Query.DELIMITER_IRI_END) > 0) {
					propIri = value.substring(1, value.indexOf(Query.DELIMITER_IRI_END));
					nodeValue.setContent(propIri);
					nodeValue.setDatatype(Value.DATATYPE_URI);
				}
				// since the dual check was done above, we can assume the qname
				// was
				// given
				else if (prop.countTokens() == 2) {
					String qname = prop.nextToken();
					String suffix = prop.nextToken();
					String prefix = Query.qnameMap.get(qname);
					propIri = prefix + suffix;
					nodeValue.setContent(propIri);
					nodeValue.setDatatype(Value.DATATYPE_URI);
				}
				else {
					nodeValue.setDatatype(datatype); // set datatype to the value specified
					nodeValue.setContent(value);
				}
				c.setValue(nodeValue);
			}

			// proceed to process the property portion of the criteria
			StringTokenizer prop = new StringTokenizer(token, DELIMITER_QNAME);
			String propIri = "";
			if (prop.countTokens() < 2 && (token.indexOf(Query.DELIMITER_IRI_START) != 0 || token.indexOf(Query.DELIMITER_IRI_END) != 0)) {
				l.error("Qname syntax or URI expected.");
				return ret;
			}
			// strip off initial and trailing URI delimiters
			if (token.indexOf(Query.DELIMITER_IRI_START) == 0 && token.lastIndexOf(Query.DELIMITER_IRI_END) >= token.length() - 1) {
				propIri = token.substring(1, token.length() - 1);
				c.setPropertyIri(propIri);
			}
			// since the dual check was done above, we can assume the qname was
			// given
			else {
				String qname = prop.nextToken();
				String suffix = prop.nextToken();
				String prefix = Query.qnameMap.get(qname);
				propIri = prefix + suffix;
				c.setPropertyIri(propIri);
			}
			targetList.add(c);

			// remember the last none-bound property in the path
			// overwrite the previous stored value with the latest occurring non-bound property node
			if (!isBound) {
				ret[1] = c;

				// nest each node in the path by passing the parent Criteria to the next iteration
				// but only in the case of a non-bound node, nodes that bound values are not
				// directional path nodes, they don't point forward, instead, such a node sets a restriction
				// on the node before it; so keep this next line inside this !isBound condition
				targetList = c.getCriteriaList();
			}
			// remember last property node
			ret[2] = c;
		}

		// if a path node was found that was unbound, then
		// set the focus to the last non-bound property in the path, if present;
		// also, remove view from root as appropriate
		if (ret[1] != null && ret[1] instanceof Criteria) {
			((Criteria) ret[1]).setHasView(true);
			q.setHasView(false);
		}
		else {
			q.setHasView(true);
		}
		ret[0] = q;
		return ret;
	}

	public String execute() {

		StringBuffer results = new StringBuffer();
		results.append("<?xml version=\"1.0\"?>\n");

		if (isIriList()) {
			// convert the iri list into fct:facet xml results
			XMLDocument document = new XMLDocument();

			XML f = new XML("fct:facet");
			XML fr = new XML("fct:row");
			XML fc = new XML("fct:column");
			return results.toString();
		}

		// String stylesheet = "facets.xsl";
		// results.append("<?xml-stylesheet type=\"text/xsl\" href=\"" +
		// stylesheet + "\"?>\n");
		try {

			DataInputStream input = getInputStream();
			String str;
			// System.out.println("BROWSER.EMIT(): RESULTS");
			while (null != ((str = input.readLine()))) {
				// System.out.println(str + "\n");
				// str = URLDecoder.decode(str);
				str = Substituter.replace("0<fct:row>", "<fct:row>", str);
				if (str.indexOf("<fct:result>") >= 0) {
					String vt = this.getView().getType();
					str = Substituter.replace("<fct:result>", "<fct:result type=\"" + vt + "\">", str);
				}
				results.append(str + "\n");
			}
			input.close();
		}
		catch (Exception ioex) {
			results = new StringBuffer();
			results.append("<?xml version=\"1.0\"?>\n");
			results.append("<error>Error: " + ioex.getMessage());
			results.append("</error>");
			l.error(ioex);
			return results.toString();
		}

		// l.info("Result: " + results.toString() + "");
		// l.info("Query.execute() Done building results");
		return results.toString();
	}

	DataInputStream getInputStream() throws MalformedURLException, IOException, ProtocolException {
		HttpURLConnection c = null;
		XMLDocument document = this.toXML();
		URL url = new URL(this.getSilo());
		c = (HttpURLConnection) url.openConnection();
		c.setRequestMethod("POST");
		c.setRequestProperty("Content-Type", "text/xml; charset=\"UTF-8\"");
		// c.setRequestProperty("Content-Type", "text/xml");
		c.setDoOutput(true);
		// OutputStreamWriter out = new
		// OutputStreamWriter(c.getOutputStream(), "UTF8");
		l.info("Query xml string: ");
		l.info(document.toString());
		document.output(c.getOutputStream()); // takes stream or printwriter
		// out.write(xmlText);
		c.getOutputStream().close();

		DataInputStream input = new DataInputStream(c.getInputStream());
		return input;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public List<Criteria> getCriteriaList() {
		return criteriaList;
	}

	public void setCriteriaList(List<Criteria> criteriaList) {
		this.criteriaList = criteriaList;
	}

	public XMLDocument toXML() {
		XMLDocument document = new XMLDocument();

		XML q = new XML("query");
		XMLDocument qxml = document.addElement(q.addXMLAttribute("xmlns", NS_OPENLINK_FACETS).addXMLAttribute("timeout", this.getTimeout() + ""));
		if (getText() != null && !getText().getContent().equals("anything") && getText().getContent().length() > 0) {
			XML text = new XML("text");
			if (getText().restrictToLabels()) {
				text.addXMLAttribute("property", "http://www.w3.org/2000/01/rdf-schema#label");
			}
			else if (getText().getPropertyIri() != null) {
				text.addXMLAttribute("property", getText().getPropertyIri());
			}

			text.setTagText(getText().getContent());

			qxml.addElement(text);
		}

		if (getValue() != null) {
			q.addElement(new XML("value").addXMLAttribute("datatype", getValue().getDatatype())
			// .addXMLAttribute("xml:lang", getValue().getLang())
			// .addXMLAttribute("op", getValue().getOperator())
					.setTagText(getValue().getContent()));
		}

		boolean rootNodeSet = false;

		for (int i = 0; i < getCriteriaList().size(); i++) {
			Criteria c = getCriteriaList().get(i);
			String propName = (c.isReverse()) ? "property-of" : "property";

			XML propxml = new XML(propName);

			qxml.addElement(propxml

			.addXMLAttribute("iri", c.getPropertyIri())

			);

			if (c.getValue() != null) {
				if (c.getValue().hasIri() && c.getValue().getContent() != null && c.getValue().getContent().length() > 0) {
					propxml.addElement(new XML("value").addXMLAttribute("op", c.getValue().getOperator()).addXMLAttribute("datatype", c.getValue().getDatatype()).addXMLAttribute("order", ((c.isAsc()) ? "asc" : "desc")).setTagText(c.getValue().getContent()));
				}
				else {
					// when availibe, set node "title" here
					propxml.addElement(new XML("value").addXMLAttribute("op", c.getValue().getOperator()).addXMLAttribute("datatype", c.getValue().getDatatype()).addXMLAttribute("order", ((c.isAsc()) ? "asc" : "desc")).setTagText(c.getValue().getContent()));
				}
			}

			c.toXML(propxml);

		}

		if (hasView()) {
			qxml.addElement(new XML("view")

			.addXMLAttribute("type", getView().getType()).addXMLAttribute("limit", getLimit() + "").addXMLAttribute("offset", getOffset() + "")

			);
		}
		return document;
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

	public Value getValue() {
		return value;
	}

	public void setValue(Value node) {
		this.value = node;
	}

}
