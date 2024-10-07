
package com.oracle.ouaf;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for listAction.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * &lt;pre&gt;
 * &amp;lt;simpleType name="listAction"&amp;gt;
 *   &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&amp;gt;
 *     &amp;lt;enumeration value="delete"/&amp;gt;
 *     &amp;lt;enumeration value="add"/&amp;gt;
 *     &amp;lt;enumeration value="update"/&amp;gt;
 *   &amp;lt;/restriction&amp;gt;
 * &amp;lt;/simpleType&amp;gt;
 * &lt;/pre&gt;
 * 
 */
@XmlType(name = "listAction")
@XmlEnum
public enum ListAction {

    @XmlEnumValue("delete")
    DELETE("delete"),
    @XmlEnumValue("add")
    ADD("add"),
    @XmlEnumValue("update")
    UPDATE("update");
    private final String value;

    ListAction(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ListAction fromValue(String v) {
        for (ListAction c: ListAction.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
