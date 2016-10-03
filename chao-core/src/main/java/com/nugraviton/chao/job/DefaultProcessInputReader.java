package com.nugraviton.chao.job;

import java.io.BufferedReader;
import java.util.UUID;

public class DefaultProcessInputReader implements ProcessInputReader{

	private final String jobName;
	private final UUID sessionId;
	private final BufferedReader reader;
	
	public DefaultProcessInputReader(String jobName, UUID sessionId, BufferedReader reader) {
		this.jobName = jobName;
		this.sessionId = sessionId;
		this.reader = reader;
	}
	
	/* (non-Javadoc)
	 * @see com.nugraviton.chao.app.ProcessInputReader#getJobId()
	 */
	@Override
	public String getJobName() {
		return jobName;
	}
	
	/* (non-Javadoc)
	 * @see com.nugraviton.chao.app.ProcessInputReader#getJobHandleId()
	 */
	@Override
	public UUID getSessionId() {
		return sessionId;
	}

	/* (non-Javadoc)
	 * @see com.nugraviton.chao.app.ProcessInputReader#getReader()
	 */
	@Override
	public BufferedReader getReader() {
		return reader;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sessionId == null) ? 0 : sessionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultProcessInputReader other = (DefaultProcessInputReader) obj;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DefaultProcessInputReader [jobName=" + jobName + ", sessionId=" + sessionId + "]";
	}
	
}
