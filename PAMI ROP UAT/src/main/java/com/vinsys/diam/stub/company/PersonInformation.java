//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.05.22 at 12:51:24 PM GST 
//


package com.vinsys.diam.stub.company;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PersonInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonInformation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PersonRequest" type="{urn:rop-gov-om:person}PersonRequest"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonInformation", propOrder = {
    "personRequest"
})
public class PersonInformation {

    @XmlElement(name = "PersonRequest", required = true, nillable = true)
    protected PersonRequest personRequest;

    /**
     * Gets the value of the personRequest property.
     * 
     * @return
     *     possible object is
     *     {@link PersonRequest }
     *     
     */
    public PersonRequest getPersonRequest() {
        return personRequest;
    }

    /**
     * Sets the value of the personRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonRequest }
     *     
     */
    public void setPersonRequest(PersonRequest value) {
        this.personRequest = value;
    }

}
