package org.qanvas;

import java.util.List;
import java.util.Vector;

import org.apache.ecs.xml.XML;

import com.monrai.cypher.util.text.Substituter;

public class Criteria {

	public Criteria(Query q) {
		this.query = q;
	}

	Query query; // need access to the offset, limit and view

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	String type = ""; // used for the <class> element
	String propertyIri = ""; // used for <property and <property-of elements
	boolean isReverse;
	boolean sameAs;
	String inference;

	// version 2.0 properties
	boolean isAsc = false;

	public boolean isAsc() {
		return this.isAsc;
	}

	public void setIsAsc(boolean isAsc) {
		this.isAsc = isAsc;
	}

	public String getInference() {
		return inference;
	}

	public void setInference(String inference) {
		this.inference = inference;
	}

	List<Criteria> criteriaList = new Vector<Criteria>();
	Value value = null; // the other object participating in the triple
	boolean hasView = false;

	public boolean hasView() {
		return hasView;
	}

	public void setHasView(boolean hasView) {
		this.hasView = hasView;
	}

	public boolean sameAs() {
		return sameAs;
	}

	public void setSameAs(boolean sameAs) {
		this.sameAs = sameAs;
	}

	public boolean isReverse() {
		return isReverse;
	}

	public void setReverse(boolean isReverse) {
		this.isReverse = isReverse;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPropertyIri() {
		return propertyIri;
	}

	public void setPropertyIri(String propertyIri) {
		this.propertyIri = Substituter.replace(" ", "+", propertyIri);
	}

	public List<Criteria> getCriteriaList() {
		return criteriaList;
	}

	public void setCriteriaList(List<Criteria> criteriaList) {
		this.criteriaList = criteriaList;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public XML toXML(XML element) {
		for (int i = 0; i < getCriteriaList().size(); i++) {
			Criteria c = getCriteriaList().get(i);
			String propName = (c.isReverse()) ? "property-of" : "property";

			XML propxml = new XML(propName);

			element.addElement(propxml

			.addXMLAttribute("iri", c.getPropertyIri())

			);
			if (c.getValue() != null) {
				if (c.getValue().hasIri() && c.getValue().getContent() != null && c.getValue().getContent().length() > 0) {
					XML value = new XML("value");
					propxml.addElement(value.addXMLAttribute("op", c.getValue().getOperator()).addXMLAttribute("datatype", c.getValue().getDatatype()).addXMLAttribute("order", ((c.isAsc()) ? "asc" : "desc")).setTagText(c.getValue().getContent()));
					if (sameAs())
						value.addXMLAttribute("same_as", "yes");
				}
				else {
					// when availibe, set node "title" here
					XML value = new XML("value");
					propxml.addElement(value.addXMLAttribute("op", c.getValue().getOperator()).addXMLAttribute("datatype", c.getValue().getDatatype()).addXMLAttribute("order", ((c.isAsc()) ? "asc" : "desc")).setTagText(c.getValue().getContent()));
					if (sameAs())
						value.addXMLAttribute("same_as", "yes");
				}
			}

			c.toXML(propxml);

		}
		if (hasView()) { // be sure the object or subject are not the
							// root
			element.addElement(new XML("view")

			.addXMLAttribute("type", getQuery().getView().getType()).addXMLAttribute("limit", getQuery().getLimit() + "").addXMLAttribute("offset", getQuery().getOffset() + "")

			);
		}
		return element;
	}
}
