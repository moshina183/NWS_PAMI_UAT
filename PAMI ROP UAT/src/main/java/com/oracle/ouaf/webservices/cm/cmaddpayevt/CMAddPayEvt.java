
package com.oracle.ouaf.webservices.cm.cmaddpayevt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Java class for anonymous complex type.
 * 
 * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
 * 
 * &lt;pre&gt;
 * &amp;lt;complexType&amp;gt;
 *   &amp;lt;complexContent&amp;gt;
 *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *       &amp;lt;sequence&amp;gt;
 *         &amp;lt;element name="input" minOccurs="0"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="paymentEvent" minOccurs="0"&amp;gt;
 *                     &amp;lt;complexType&amp;gt;
 *                       &amp;lt;complexContent&amp;gt;
 *                         &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                           &amp;lt;sequence&amp;gt;
 *                             &amp;lt;element name="payorAccountId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                             &amp;lt;element name="legacyAccountId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                             &amp;lt;element name="serviceProviderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                             &amp;lt;element name="paymentAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                             &amp;lt;element name="paymentDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                             &amp;lt;element name="matchType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                             &amp;lt;element name="matchValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                             &amp;lt;element name="paymentSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                             &amp;lt;element name="paymentStation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                             &amp;lt;element name="paymentReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                             &amp;lt;element name="RRN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                             &amp;lt;element name="notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                             &amp;lt;element name="tenderAuthorizationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                             &amp;lt;element name="paymentMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                           &amp;lt;/sequence&amp;gt;
 *                         &amp;lt;/restriction&amp;gt;
 *                       &amp;lt;/complexContent&amp;gt;
 *                     &amp;lt;/complexType&amp;gt;
 *                   &amp;lt;/element&amp;gt;
 *                 &amp;lt;/sequence&amp;gt;
 *               &amp;lt;/restriction&amp;gt;
 *             &amp;lt;/complexContent&amp;gt;
 *           &amp;lt;/complexType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *         &amp;lt;element name="output" minOccurs="0"&amp;gt;
 *           &amp;lt;complexType&amp;gt;
 *             &amp;lt;complexContent&amp;gt;
 *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
 *                 &amp;lt;sequence&amp;gt;
 *                   &amp;lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="statusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                   &amp;lt;element name="paymentEventId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
 *                 &amp;lt;/sequence&amp;gt;
 *               &amp;lt;/restriction&amp;gt;
 *             &amp;lt;/complexContent&amp;gt;
 *           &amp;lt;/complexType&amp;gt;
 *         &amp;lt;/element&amp;gt;
 *       &amp;lt;/sequence&amp;gt;
 *     &amp;lt;/restriction&amp;gt;
 *   &amp;lt;/complexContent&amp;gt;
 * &amp;lt;/complexType&amp;gt;
 * &lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "input",
    "output"
})
@XmlRootElement(name = "CMAddPayEvt")
public class CMAddPayEvt {

    protected CMAddPayEvt.Input input;
    protected CMAddPayEvt.Output output;

    /**
     * Gets the value of the input property.
     * 
     * @return
     *     possible object is
     *     {@link CMAddPayEvt.Input }
     *     
     */
    public CMAddPayEvt.Input getInput() {
        return input;
    }

    /**
     * Sets the value of the input property.
     * 
     * @param value
     *     allowed object is
     *     {@link CMAddPayEvt.Input }
     *     
     */
    public void setInput(CMAddPayEvt.Input value) {
        this.input = value;
    }

    /**
     * Gets the value of the output property.
     * 
     * @return
     *     possible object is
     *     {@link CMAddPayEvt.Output }
     *     
     */
    public CMAddPayEvt.Output getOutput() {
        return output;
    }

    /**
     * Sets the value of the output property.
     * 
     * @param value
     *     allowed object is
     *     {@link CMAddPayEvt.Output }
     *     
     */
    public void setOutput(CMAddPayEvt.Output value) {
        this.output = value;
    }


    /**
     * &lt;p&gt;Java class for anonymous complex type.
     * 
     * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
     * 
     * &lt;pre&gt;
     * &amp;lt;complexType&amp;gt;
     *   &amp;lt;complexContent&amp;gt;
     *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *       &amp;lt;sequence&amp;gt;
     *         &amp;lt;element name="paymentEvent" minOccurs="0"&amp;gt;
     *           &amp;lt;complexType&amp;gt;
     *             &amp;lt;complexContent&amp;gt;
     *               &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *                 &amp;lt;sequence&amp;gt;
     *                   &amp;lt;element name="payorAccountId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                   &amp;lt;element name="legacyAccountId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                   &amp;lt;element name="serviceProviderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                   &amp;lt;element name="paymentAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                   &amp;lt;element name="paymentDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                   &amp;lt;element name="matchType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                   &amp;lt;element name="matchValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                   &amp;lt;element name="paymentSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                   &amp;lt;element name="paymentStation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                   &amp;lt;element name="paymentReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                   &amp;lt;element name="RRN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                   &amp;lt;element name="notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                   &amp;lt;element name="tenderAuthorizationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                   &amp;lt;element name="paymentMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *                 &amp;lt;/sequence&amp;gt;
     *               &amp;lt;/restriction&amp;gt;
     *             &amp;lt;/complexContent&amp;gt;
     *           &amp;lt;/complexType&amp;gt;
     *         &amp;lt;/element&amp;gt;
     *       &amp;lt;/sequence&amp;gt;
     *     &amp;lt;/restriction&amp;gt;
     *   &amp;lt;/complexContent&amp;gt;
     * &amp;lt;/complexType&amp;gt;
     * &lt;/pre&gt;
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "paymentEvent"
    })
    public static class Input {

        protected CMAddPayEvt.Input.PaymentEvent paymentEvent;

        /**
         * Gets the value of the paymentEvent property.
         * 
         * @return
         *     possible object is
         *     {@link CMAddPayEvt.Input.PaymentEvent }
         *     
         */
        public CMAddPayEvt.Input.PaymentEvent getPaymentEvent() {
            return paymentEvent;
        }

        /**
         * Sets the value of the paymentEvent property.
         * 
         * @param value
         *     allowed object is
         *     {@link CMAddPayEvt.Input.PaymentEvent }
         *     
         */
        public void setPaymentEvent(CMAddPayEvt.Input.PaymentEvent value) {
            this.paymentEvent = value;
        }


        /**
         * &lt;p&gt;Java class for anonymous complex type.
         * 
         * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
         * 
         * &lt;pre&gt;
         * &amp;lt;complexType&amp;gt;
         *   &amp;lt;complexContent&amp;gt;
         *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
         *       &amp;lt;sequence&amp;gt;
         *         &amp;lt;element name="payorAccountId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *         &amp;lt;element name="legacyAccountId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *         &amp;lt;element name="serviceProviderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *         &amp;lt;element name="paymentAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *         &amp;lt;element name="paymentDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *         &amp;lt;element name="matchType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *         &amp;lt;element name="matchValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *         &amp;lt;element name="paymentSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *         &amp;lt;element name="paymentStation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *         &amp;lt;element name="paymentReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *         &amp;lt;element name="RRN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *         &amp;lt;element name="notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *         &amp;lt;element name="tenderAuthorizationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *         &amp;lt;element name="paymentMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
         *       &amp;lt;/sequence&amp;gt;
         *     &amp;lt;/restriction&amp;gt;
         *   &amp;lt;/complexContent&amp;gt;
         * &amp;lt;/complexType&amp;gt;
         * &lt;/pre&gt;
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "payorAccountId",
            "legacyAccountId",
            "serviceProviderId",
            "paymentAmount",
            "paymentDate",
            "matchType",
            "matchValue",
            "paymentSource",
            "paymentStation",
            "paymentReference",
            "rrn",
            "notes",
            "tenderAuthorizationCode",
            "paymentMethod"
        })
        public static class PaymentEvent {

            protected String payorAccountId;
            protected String legacyAccountId;
            protected String serviceProviderId;
            protected String paymentAmount;
            protected String paymentDate;
            protected String matchType;
            protected String matchValue;
            protected String paymentSource;
            protected String paymentStation;
            protected String paymentReference;
            @XmlElement(name = "RRN")
            protected String rrn;
            protected String notes;
            protected String tenderAuthorizationCode;
            protected String paymentMethod;

            /**
             * Gets the value of the payorAccountId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPayorAccountId() {
                return payorAccountId;
            }

            /**
             * Sets the value of the payorAccountId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPayorAccountId(String value) {
                this.payorAccountId = value;
            }

            /**
             * Gets the value of the legacyAccountId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getLegacyAccountId() {
                return legacyAccountId;
            }

            /**
             * Sets the value of the legacyAccountId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setLegacyAccountId(String value) {
                this.legacyAccountId = value;
            }

            /**
             * Gets the value of the serviceProviderId property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getServiceProviderId() {
                return serviceProviderId;
            }

            /**
             * Sets the value of the serviceProviderId property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setServiceProviderId(String value) {
                this.serviceProviderId = value;
            }

            /**
             * Gets the value of the paymentAmount property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPaymentAmount() {
                return paymentAmount;
            }

            /**
             * Sets the value of the paymentAmount property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPaymentAmount(String value) {
                this.paymentAmount = value;
            }

            /**
             * Gets the value of the paymentDate property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPaymentDate() {
                return paymentDate;
            }

            /**
             * Sets the value of the paymentDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPaymentDate(String value) {
                this.paymentDate = value;
            }

            /**
             * Gets the value of the matchType property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMatchType() {
                return matchType;
            }

            /**
             * Sets the value of the matchType property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMatchType(String value) {
                this.matchType = value;
            }

            /**
             * Gets the value of the matchValue property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMatchValue() {
                return matchValue;
            }

            /**
             * Sets the value of the matchValue property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMatchValue(String value) {
                this.matchValue = value;
            }

            /**
             * Gets the value of the paymentSource property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPaymentSource() {
                return paymentSource;
            }

            /**
             * Sets the value of the paymentSource property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPaymentSource(String value) {
                this.paymentSource = value;
            }

            /**
             * Gets the value of the paymentStation property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPaymentStation() {
                return paymentStation;
            }

            /**
             * Sets the value of the paymentStation property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPaymentStation(String value) {
                this.paymentStation = value;
            }

            /**
             * Gets the value of the paymentReference property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPaymentReference() {
                return paymentReference;
            }

            /**
             * Sets the value of the paymentReference property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPaymentReference(String value) {
                this.paymentReference = value;
            }

            /**
             * Gets the value of the rrn property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRRN() {
                return rrn;
            }

            /**
             * Sets the value of the rrn property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRRN(String value) {
                this.rrn = value;
            }

            /**
             * Gets the value of the notes property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getNotes() {
                return notes;
            }

            /**
             * Sets the value of the notes property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setNotes(String value) {
                this.notes = value;
            }

            /**
             * Gets the value of the tenderAuthorizationCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTenderAuthorizationCode() {
                return tenderAuthorizationCode;
            }

            /**
             * Sets the value of the tenderAuthorizationCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTenderAuthorizationCode(String value) {
                this.tenderAuthorizationCode = value;
            }

            /**
             * Gets the value of the paymentMethod property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getPaymentMethod() {
                return paymentMethod;
            }

            /**
             * Sets the value of the paymentMethod property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setPaymentMethod(String value) {
                this.paymentMethod = value;
            }

        }

    }


    /**
     * &lt;p&gt;Java class for anonymous complex type.
     * 
     * &lt;p&gt;The following schema fragment specifies the expected content contained within this class.
     * 
     * &lt;pre&gt;
     * &amp;lt;complexType&amp;gt;
     *   &amp;lt;complexContent&amp;gt;
     *     &amp;lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&amp;gt;
     *       &amp;lt;sequence&amp;gt;
     *         &amp;lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="statusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *         &amp;lt;element name="paymentEventId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&amp;gt;
     *       &amp;lt;/sequence&amp;gt;
     *     &amp;lt;/restriction&amp;gt;
     *   &amp;lt;/complexContent&amp;gt;
     * &amp;lt;/complexType&amp;gt;
     * &lt;/pre&gt;
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "status",
        "statusCode",
        "message",
        "paymentEventId"
    })
    public static class Output {

        protected String status;
        protected String statusCode;
        protected String message;
        protected String paymentEventId;

        /**
         * Gets the value of the status property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStatus() {
            return status;
        }

        /**
         * Sets the value of the status property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStatus(String value) {
            this.status = value;
        }

        /**
         * Gets the value of the statusCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStatusCode() {
            return statusCode;
        }

        /**
         * Sets the value of the statusCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStatusCode(String value) {
            this.statusCode = value;
        }

        /**
         * Gets the value of the message property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMessage() {
            return message;
        }

        /**
         * Sets the value of the message property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMessage(String value) {
            this.message = value;
        }

        /**
         * Gets the value of the paymentEventId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPaymentEventId() {
            return paymentEventId;
        }

        /**
         * Sets the value of the paymentEventId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPaymentEventId(String value) {
            this.paymentEventId = value;
        }

    }

}
