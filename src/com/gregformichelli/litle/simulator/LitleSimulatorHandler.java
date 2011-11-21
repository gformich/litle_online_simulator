package com.gregformichelli.litle.simulator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class LitleSimulatorHandler extends AbstractHandler {
	
	AtomicLong oidGenerator = new AtomicLong(21700000000000L);

	@Override
	public void handle(String arg0, Request req, HttpServletRequest servletRequest,
			HttpServletResponse response) throws IOException, ServletException {
		
		int length = req.getContentLength();
		String request = readRequest(req, length);
		
		// TODO - log request in file
		logRequest(request);
		
		// TODO - validate request schema
		
        response.setContentType("text/xml;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        req.setHandled(true);
        response.getWriter().println(getResponseText(req));
	}

	private void logRequest(String request) {
		// TODO Auto-generated method stub
		
	}

	private String readRequest(Request req, int length) throws IOException {
		String request = "";
		if(length > 1) {
			char [] buffer = new char[length];
			req.getReader().read(buffer);
			request = new String(buffer);
		}
		return request;
	}
	
	private String getResponseText(Request req) {
		
		// TODO - parse out id, order id, and report group from the request
		// and return these on the response
		
		StringBuilder response = new StringBuilder("<?xml version=\"1.0\" ?>");
		response.append("<authResponse id=\"3\" reportGroup=\"000057\">");
		long fakeId = oidGenerator.incrementAndGet() % 59900000000000L;
	    response.append("<litleTxnId>").append(fakeId).append("</litleTxnId>");
	    response.append("<orderId>TestCaseID3</orderId>");
	    response.append("<response>000</response>");
	    
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd'T'hh:mm:ss");
	    String ts = df.format(new Date());
	    response.append("<responseTime>").append(ts).append("</responseTime>");
	    
	    response.append("<message>Approved</message>");
	    
	    response.append("<authCode>0067Z </authCode>");
	    response.append("<fraudResult>");
	    response.append("<avsResult>00</avsResult>");
	    response.append("</fraudResult>");
	    response.append("</authResponse>");
		
		return response.toString();
	}
	
	private void validateSchema(String content) throws ParserConfigurationException, SAXException, IOException {
        
		// parse an XML document into a DOM tree
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = parser.parse(content);

		// create a SchemaFactory capable of understanding WXS schemas
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		// load schema
		Source schemaFile = new StreamSource(new File("mySchema.xsd"));
		Schema schema = factory.newSchema(schemaFile);

		// create a Validator instance, which can be used to validate an
		// instance document
		Validator validator = schema.newValidator();

		// validate the DOM tree
		try {
			validator.validate(new DOMSource(document));
		} 
		catch (SAXException e) {
			// instance document is invalid!
		}
		
	}

	
}
