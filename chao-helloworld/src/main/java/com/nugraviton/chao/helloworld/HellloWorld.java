package com.nugraviton.chao.helloworld;

import com.nugraviton.chao.annotation.Cron;
import com.nugraviton.chao.annotation.Job;
import com.nugraviton.chao.annotation.Task;
import com.nugraviton.chao.annotation.TriggeredBy;
import com.nugraviton.chao.core.Core;
import com.nugraviton.chao.core.EmbeddedCore;

@Job(name = "cron-job")
public class HellloWorld {
	
	public static void main(String[] args) throws InterruptedException{
		
		Core core = new EmbeddedCore();
		core.start();
		//
		Thread.sleep(8000);
		core.shutdown();
		core.awaitTermination();
	}
	
	@Cron(name ="start-task", jobName = "cron-job", cron="*/5 * * * * *", description="every 5 seconds")
	public void helloWorld() throws InterruptedException {
		//assuming the task takes 5 seconds.
		Thread.sleep(5000);
		System.out.println("Hello world! - start task");
	}
	
	@TriggeredBy(taskName="start-task")
	@Task(name ="second-task", jobName = "cron-job", description="a task triggered by [start-task]")
	public void trailerTask() throws InterruptedException {
		//assuming the task takes 5 seconds.
		Thread.sleep(5000);
		System.out.println("Hello world! - second task");
	}

}
