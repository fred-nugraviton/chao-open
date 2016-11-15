package com.nugraviton.chao.helloworld;

import com.nugraviton.chao.Chao;
import com.nugraviton.chao.annotation.Cron;
import com.nugraviton.chao.annotation.Job;
import com.nugraviton.chao.annotation.Task;
import com.nugraviton.chao.annotation.TriggeredBy;

@Job(name = "hello-world")
public class HellloWorld {
	
	public static void main(String[] args) throws InterruptedException{
		Chao core = Chao.create();
		core.start();
		Thread.sleep(16000);
		core.shutdown();
		core.awaitTermination();
	}
	
	@Cron(name ="start-task", jobName = "hello-world", cron="*/5 * * * * *", description="every 5 seconds")
	public void helloWorld() throws InterruptedException {
		//assuming the task takes 5 seconds.
		Thread.sleep(5000);
		System.out.println("Hello world! - start task");
	}
	
	@TriggeredBy(taskName="start-task")
	@Task(name ="second-task", jobName = "hello-world", description="a task triggered by [start-task]")
	public void trailerTask() throws InterruptedException {
		//assuming the task takes 5 seconds.
		Thread.sleep(5000);
		System.out.println("Hello world! - second task");
	}
}
