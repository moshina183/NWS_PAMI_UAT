
package om.gov.rop.trafficinformation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CodeDesc complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CodeDesc"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="desc_en" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="desc_ar" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CodeDesc", propOrder = {
    "code",
    "descEn",
    "descAr"
})
public class CodeDesc {

    @XmlElement(required = true, nillable = true)
    protected String code;
    @XmlElement(name = "desc_en", required = true, nillable = true)
    protected String descEn;
    @XmlElement(name = "desc_ar", required = true, nillable = true)
    protected String descAr;

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the descEn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescEn() {
        return descEn;
    }

    /**
     * Sets the value of the descEn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescEn(String value) {
        this.descEn = value;
    }

    /**
     * Gets the value of the descAr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescAr() {
        return descAr;
    }

    /**
     * Sets the value of the descAr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescAr(String value) {
        this.descAr = value;
    }

}
