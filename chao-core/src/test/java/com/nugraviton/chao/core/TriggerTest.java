package com.nugraviton.chao.core;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TriggerTest {

	public static void main(String[] args) throws InterruptedException {

		ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				System.out.println("test.....");
			}
		};
		pool.scheduleAtFixedRate(task, 3, 10, TimeUnit.SECONDS);
		
		pool.awaitTermination(3000000, TimeUnit.SECONDS);
	}

}
