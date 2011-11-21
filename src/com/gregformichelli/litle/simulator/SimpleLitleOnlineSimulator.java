package com.gregformichelli.litle.simulator;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;


public class SimpleLitleOnlineSimulator {
	
	private static int DEFAULT_PORT = 8080;
	
	public static void main(String[] args) throws Exception
	{
		
	    Server server = new Server(getListenPort());
	    ContextHandler ctx = new ContextHandler("/vap/communicator/online");
	    ctx.setHandler(new LitleSimulatorHandler());
	    server.setHandler(ctx);	 
	    server.start();
	    server.join();
	    
	    // TODO - serve a static content page so the user can navigate to something
	    // add in a viewer that can be used for seeing what was submitted
	}
	
	private static int getListenPort() {
		
		int port = DEFAULT_PORT;
		try {
			port = Integer.parseInt( System.getProperty("litle.simulator.port") );
		}
		catch(Exception e) {
			port = DEFAULT_PORT;
		}
		return port;
	}

}
