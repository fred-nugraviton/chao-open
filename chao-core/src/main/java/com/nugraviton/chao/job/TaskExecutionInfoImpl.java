package com.nugraviton.chao.job;

import java.time.LocalDateTime;
import java.util.UUID;

import com.nugraviton.chao.core.TaskExecutionInfo;

/*
 * A snapshot view of a task execution.
 */
public class TaskExecutionInfoImpl implements TaskExecutionInfo{

	private Long maxMemory;
	private Long freeMemory;
	private Long totalMemory;
	private Integer prograss;
	private LocalDateTime startTime;
	private UUID executionId;
	private String jobName;
	private UUID sessionId;

	public TaskExecutionInfoImpl() {
	}

	@Override
	public String getJobName() {
		return this.jobName;
	}
	
	public TaskExecutionInfoImpl withJobName(String jobName){
		this.jobName = jobName;
		return this;
	}

	@Override
	public UUID getExecutionId() {
		return this.executionId;
	}
	
	public TaskExecutionInfoImpl withExecutionId(UUID executionId){
		this.executionId = executionId;
		return this;
	}

	@Override
	public LocalDateTime getStartTime() {
		return this.startTime;
	}
	
	public TaskExecutionInfoImpl withStartTime(LocalDateTime startTime){
		this.startTime = startTime;
		return this;
	}

	@Override
	public Integer getPrograss() {
		return this.prograss;
	}
	
	public TaskExecutionInfoImpl withPrograss(Integer prograss){
		this.prograss = prograss;
		return this;
	}

	@Override
	public Long getTotalMemory() {
		return this.totalMemory;
	}

	public TaskExecutionInfoImpl withTotalMemory(Long totalMemory){
		this.totalMemory = totalMemory;
		return this;
	}
	
	@Override
	public Long getFreeMemory() {
		return this.freeMemory;
	}
	
	public TaskExecutionInfoImpl withFreeMemory(Long freeMemory){
		this.freeMemory = freeMemory;
		return this;
	}

	@Override
	public Long getMaxMemory() {
		return this.maxMemory;
	}
	
	public TaskExecutionInfoImpl withMaxMemory(Long maxMemory){
		this.maxMemory = maxMemory;
		return this;
	}

	public TaskExecutionInfoImpl withSessionId(UUID sessionId) {
		this.sessionId = sessionId;
		return this;
	}
	
	@Override
	public UUID getSessionId() {
		return this.sessionId;
	}
	
	@Override
	public String toString() {
		return "ExecutionInfoImpl [maxMemory=" + maxMemory + ", freeMemory=" + freeMemory + ", totalMemory="
				+ totalMemory + ", prograss=" + prograss + ", startTime=" + startTime + ", executionId=" + executionId
				+ ", jobName=" + jobName + "]";
	}

}
