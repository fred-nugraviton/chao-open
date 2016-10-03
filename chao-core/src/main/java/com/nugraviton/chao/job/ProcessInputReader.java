package com.nugraviton.chao.job;

import java.io.BufferedReader;
import java.util.UUID;

public interface ProcessInputReader {

	String getJobName();

	UUID getSessionId();

	BufferedReader getReader();

}