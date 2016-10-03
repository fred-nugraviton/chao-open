package com.nugraviton.chao.job.rmi;
public class RmiProcessInfoImpl implements RmiProcessInfo{
	private static final long serialVersionUID = -6864459782846337090L;
	
	private Long totalMemory;
	private Long freeMemory;
	private Long maxMemory;
	
	public RmiProcessInfoImpl(Long totalMemory, Long freeMemory, Long maxMemory) {
		this.totalMemory = totalMemory;
		this.freeMemory = freeMemory;
		this.maxMemory = maxMemory;
	}

	@Override
	public Long getTotalMemory() {
		return totalMemory;
	}

	@Override
	public Long getFreeMemory() {
		return freeMemory;
	}

	@Override
	public Long getMaxMemory() {
		return maxMemory;
	}
}
