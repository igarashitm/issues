package org.switchyard.quickstarts.bean.service;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.MessageFormat;

import javax.net.ssl.SSLHandshakeException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.transport.http.HTTPException;
import org.switchyard.HandlerException;

/**
 * @author z8a006 date 17/04/2015
 * 
 */
public class ExceptionProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Exception exception = (Exception) exchange.getProperties().get(
                Exchange.EXCEPTION_CAUGHT);
        String errCode = "";
        String errMsg = "";
        String errResponse = "";
        String title="";
        String errorFormat = System.getProperty("ICO001.audascan.err.format");

        exchange.getOut().setFault(true);
        exchange.setProperty(Exchange.ERRORHANDLER_HANDLED, false);
        exchange.removeProperty("CamelExceptionCaught");
        if (exception instanceof HandlerException) {

            exception = (Exception) exception.getCause().getCause();
            if (exception != null) {
                exception = (Exception) exception.getCause();
            }
            if (exception instanceof HTTPException) {
                errCode = System.getProperty("audascan.ico001.connect.errcd");
                errMsg = System.getProperty("audascan.ico001.connect");
                errResponse = MessageFormat
                        .format(errorFormat, errCode,title, errMsg);

            } else if (exception instanceof SocketException || exception instanceof SSLHandshakeException) {
                errCode = System.getProperty("audascan.ssl.error.code");
                errMsg = System.getProperty("audascan.ssl.error.msg");
                errResponse = MessageFormat
                        .format(errorFormat, errCode,title, errMsg);

            }
            else if (exception instanceof SocketTimeoutException) {
                errCode = System.getProperty("audascan.abz.timeout.error");
                errMsg = System.getProperty("rdw.timeout");
                errResponse = MessageFormat
                        .format(errorFormat, errCode,title, errMsg);

            } else {
                errCode = System.getProperty("audascan.ico001.connect.errcd");
                errMsg = System.getProperty("audascan.ico001.connect");
                errResponse = MessageFormat
                        .format(errorFormat, errCode,title, errMsg);
            }
/*        } else if (exception instanceof InsufficientInputException) {
            errCode = ((InsufficientInputException) exception).getErrCode();
            errMsg = ((InsufficientInputException) exception).getErrMsg();
            title=((InsufficientInputException) exception).getTitle();
            errResponse = MessageFormat.format(errorFormat, errCode,title, errMsg);
*/
        } else {
            errCode = System.getProperty("audascan.mmt4.mapping.error");
            errMsg = System.getProperty("rdw.mapping.error");
            errResponse = MessageFormat.format(errorFormat, errCode,title, errMsg);

        }
        exchange.getOut().setBody(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + errResponse);
        exchange.setProperty(Exchange.ERRORHANDLER_HANDLED, true);
        exchange.removeProperty("CamelExceptionCaught");
    }

}
