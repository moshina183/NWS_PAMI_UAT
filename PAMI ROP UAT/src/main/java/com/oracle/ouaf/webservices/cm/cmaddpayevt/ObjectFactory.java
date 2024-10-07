
package com.oracle.ouaf.webservices.cm.cmaddpayevt;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.ouaf.webservices.cm.cmaddpayevt package. 
 * &lt;p&gt;An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.ouaf.webservices.cm.cmaddpayevt
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CMAddPayEvt }
     * 
     */
    public CMAddPayEvt createCMAddPayEvt() {
        return new CMAddPayEvt();
    }

    /**
     * Create an instance of {@link CMAddPayEvt.Input }
     * 
     */
    public CMAddPayEvt.Input createCMAddPayEvtInput() {
        return new CMAddPayEvt.Input();
    }

    /**
     * Create an instance of {@link CMAddPayEvt.Output }
     * 
     */
    public CMAddPayEvt.Output createCMAddPayEvtOutput() {
        return new CMAddPayEvt.Output();
    }

    /**
     * Create an instance of {@link CMAddPayEvt.Input.PaymentEvent }
     * 
     */
    public CMAddPayEvt.Input.PaymentEvent createCMAddPayEvtInputPaymentEvent() {
        return new CMAddPayEvt.Input.PaymentEvent();
    }

}
