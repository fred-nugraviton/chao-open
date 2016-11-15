package com.nugraviton.chao.job;

import java.net.SocketException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.UUID;

import com.nugraviton.chao.job.rmi.RmiTaskExecutor;
import com.nugraviton.chao.schedule.TaskStartException;
import com.nugraviton.chao.job.TaskExecutionInfoImpl;
import com.nugraviton.chao.TaskExecutionInfo;
import com.nugraviton.chao.job.ContainerException;
import com.nugraviton.chao.job.JobHandle;
import com.nugraviton.chao.job.rmi.RmiProcessInfo;

/**
 * Represents a running job session.
 * Job Container calls this to execute its tasks.
 * it's not reusable after use, neither success nor failure.
 * 
 * It wraps 
 * 1. JVM process.
 * 2. Task execution history
 * 3. Current running tasks.
 * 
 *  
 * @author fred.wang@nuGraviton.com
 *
 */
final class DefaultJobHandle implements JobHandle{
	
	
	private final UUID sessionId;
	private final Process process;
	private final LocalDateTime startTime = LocalDateTime.now();
	private Integer prograss;
	private final RmiTaskExecutor taskExecutor;
	
	DefaultJobHandle(UUID sessionId, Process process, RmiTaskExecutor taskExecutor){
		this.sessionId = sessionId;
		this.taskExecutor = taskExecutor;
		this.process = process;
	}

	@Override
	public UUID getJobHandleId() {
		return UUID.fromString(sessionId.toString());
	}
	
	@Override
	public TaskExecutionInfo getExecutionInfo() {
		TaskExecutionInfoImpl eii =
				new TaskExecutionInfoImpl()
				.withSessionId(sessionId)
				.withExecutionId(sessionId)
				.withPrograss(prograss)
				.withStartTime(startTime);
		
		try {
			RmiProcessInfo rpf = this.taskExecutor.getStatus();
			
			eii
			.withTotalMemory(rpf.getTotalMemory())
			.withMaxMemory(rpf.getMaxMemory())
			.withFreeMemory(rpf.getFreeMemory());
			
		} catch (RemoteException e) {
			throw new ContainerException(e);
		}
		
		return eii;
	}

	public AsyncContext taskStart(RmiTask rmiTask) {
		//RmiTask rmiTask = command.getTaskDef();
		try {
			AsyncContext context = taskExecutor.start(rmiTask);
			return context;
		} catch (RemoteException e) {
			throw new TaskStartException(rmiTask);
		}
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
		DefaultJobHandle other = (DefaultJobHandle) obj;
		if (sessionId == null) {
			if (other.sessionId != null)
				return false;
		} else if (!sessionId.equals(other.sessionId))
			return false;
		return true;
	}

	@Override
	public void close() {
		try {
			this.taskExecutor.exit(0);
		} catch (RemoteException e) {
			if(!(e.getCause() instanceof SocketException)){
				if(process != null && process.isAlive()){
					this.process.destroyForcibly();
				}
			}
		}
	}

	
}
