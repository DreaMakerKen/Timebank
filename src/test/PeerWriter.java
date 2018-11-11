package test;

import java.io.File;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PeerWriter extends Thread {
	private static final Logger LOGGER = LoggerFactory.getLogger(PeerWriter.class);

	private Socket socket;
	private ArrayList<Object> outputBuffer;
	private boolean runFlag = true;

	public PeerWriter(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			
			outputBuffer = new ArrayList<Object>();
			ObjectOutputStream  out = new ObjectOutputStream(socket.getOutputStream());
			while (runFlag) {
				if (!outputBuffer.isEmpty() && outputBuffer.get(0) != null) {
                    for ( Object line : outputBuffer) {
                    	System.out.println("Sending " +line + " to " + socket.getInetAddress()+":"+socket.getPort());
                        out.writeObject(line);
                    }
                    outputBuffer = new ArrayList<Object>();
                    outputBuffer.add(null);
                }
				outputBuffer = new ArrayList<Object>();
				outputBuffer.add(null);
				TimeUnit.MILLISECONDS.sleep(2000);
				
			}
		} catch (Exception e) {
			System.out.printf("Peer " + socket.getInetAddress() + " disconnected."+e.getMessage()); 
		}
	}


	public  void write(Object data) {
		if (!outputBuffer.isEmpty()) {
			if (outputBuffer.get(0) == null) {
				outputBuffer.remove(0);
			}
		}
		outputBuffer.add(data);
	}
	public ArrayList<Object> getOutbuffer()
	{
		return this.outputBuffer;
	}

	public void shutdown() {
		runFlag = false;
	}
}