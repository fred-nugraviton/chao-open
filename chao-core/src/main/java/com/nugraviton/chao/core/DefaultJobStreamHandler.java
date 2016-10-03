package com.nugraviton.chao.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nugraviton.chao.job.JobStreamHandler;
import com.nugraviton.chao.job.ProcessInputReader;

/**
 * Handles input streams form job processes. A dedicated thread 
 * takes case of all streams from all jobs.
 * 
 * Currently just log them.
 * 
 * @author fred.wang@nuGraviton.com
 *
 */
public final class DefaultJobStreamHandler implements JobStreamHandler{

	private static Logger log = LoggerFactory.getLogger(DefaultJobStreamHandler.class);
	
	private Object lock = new Object();
	
	/*
	 * Map<SessionId, ProcessInputReader>
	 */
	private Map<UUID, ProcessInputReader> map = new HashMap<>();

	@Override
	public void run(){
		
		synchronized(lock){
			
			for( Entry<UUID, ProcessInputReader> entry : map.entrySet()){
				ProcessInputReader theReader = entry.getValue();
				BufferedReader reader = theReader.getReader();
				UUID id = entry.getKey();
				try {
					
					if(reader.ready()){
						String processOutput = reader.readLine();
						if(log.isTraceEnabled()){
							log.trace("{} - [{}]", id, processOutput);
						}
					}
				} catch (IOException e) {
					if(reader != null){
						try {
							reader.close();
						} catch (IOException e1) {
							//ignore
						}
						
					}
				}
			}
		}
	}
	
	/**
	 * @param appName
	 * @param jobProcessId
	 * @param reader
	 */
	@Override
	public void registerStream(ProcessInputReader reader){
		synchronized(lock){
			map.put(reader.getSessionId(), reader);
			if(log.isTraceEnabled()){
				log.trace("registerred stream: {}", reader.getSessionId());
				log.trace("number of streams: {}", map.size());
			}
		}
	}

	@Override
	public void deregisterStream(UUID sessionId) {
		synchronized(lock){
			ProcessInputReader reader = map.remove(sessionId);
			
			if(reader == null && log.isTraceEnabled()){
				log.trace("deregisterred stream: {}", sessionId);
				log.trace("number of streams: {}", map.size());
			}
		}
	}

}
