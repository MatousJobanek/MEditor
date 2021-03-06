/*
 * Metadata Editor
 * @author Jiri Kremser
 * 
 * 
 * 
 * Metadata Editor - Rich internet application for editing metadata.
 * Copyright (C) 2011  Jiri Kremser (kremser@mzk.cz)
 * Moravian Library in Brno
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * 
 */

package org.fedora.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * Java class for FieldSearchQuery complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="FieldSearchQuery">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="conditions">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="condition" type="{http://www.fedora.info/definitions/1/0/types/}Condition" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="terms" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FieldSearchQuery", propOrder = {"conditions", "terms"})
public class FieldSearchQuery {

    /** The conditions. */
    @XmlElementRef(name = "conditions", type = JAXBElement.class)
    protected JAXBElement<FieldSearchQuery.Conditions> conditions;

    /** The terms. */
    @XmlElementRef(name = "terms", type = JAXBElement.class)
    protected JAXBElement<String> terms;

    /**
     * Gets the value of the conditions property.
     * 
     * @return the conditions possible object is {@link JAXBElement }{@code <}
     *         {@link FieldSearchQuery.Conditions }{@code >}
     */
    public JAXBElement<FieldSearchQuery.Conditions> getConditions() {
        return conditions;
    }

    /**
     * Sets the value of the conditions property.
     * 
     * @param value
     *        allowed object is {@link JAXBElement }{@code <}
     *        {@link FieldSearchQuery.Conditions }{@code >}
     */
    public void setConditions(JAXBElement<FieldSearchQuery.Conditions> value) {
        this.conditions = (value);
    }

    /**
     * Gets the value of the terms property.
     * 
     * @return the terms possible object is {@link JAXBElement }{@code <}
     *         {@link String }{@code >}
     */
    public JAXBElement<String> getTerms() {
        return terms;
    }

    /**
     * Sets the value of the terms property.
     * 
     * @param value
     *        allowed object is {@link JAXBElement }{@code <}{@link String }
     *        {@code >}
     */
    public void setTerms(JAXBElement<String> value) {
        this.terms = (value);
    }

    /**
     * <p>
     * Java class for anonymous complex type.
     * <p>
     * The following schema fragment specifies the expected content contained
     * within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="condition" type="{http://www.fedora.info/definitions/1/0/types/}Condition" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"condition"})
    public static class Conditions {

        /** The condition. */
        protected List<Condition> condition;

        /**
         * Gets the value of the condition property.
         * <p>
         * This accessor method returns a reference to the live list, not a
         * snapshot. Therefore any modification you make to the returned list
         * will be present inside the JAXB object. This is why there is not a
         * <CODE>set</CODE> method for the condition property.
         * <p>
         * For example, to add a new item, do as follows:
         * 
         * <pre>
         * getCondition().add(newItem);
         * </pre>
         * <p>
         * Objects of the following type(s) are allowed in the list
         * 
         * @return the condition {@link Condition }
         */
        public List<Condition> getCondition() {
            if (condition == null) {
                condition = new ArrayList<Condition>();
            }
            return this.condition;
        }

    }

}
