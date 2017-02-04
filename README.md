## Introduction

Chao Open is an open source Java scheduling and work flow library. It provides a small set of simple and intuitive annotation based API and aiming to run enterprise level jobs.


## Key Features

### Simple API.

Only 6 annotations: @Job, @FixedRate, @FixedDelay, @Cron, @Task and @TriggeredBy. There is no inheritance, special class or method signature required.

### Three ways to schedule: fixedRate, fixedDelay and Cron.

FixedRate and FixedDelay provide quick simple scheduling. More advanced scheduling is handled by Cron.   

### Chain tasks to create work flows.

Quickly chain up tasks to build tree pattern work flows.

### Jobs are running in separate process.

Jobs are running in separate process, this will eliminate memory leak from improper coding. 
 
### Jobs are loaded by auto scan.

Either of two convenient factory method will scan class path to load all jobs.
 
### Very light small code base.

Built from ground up with very few dependencies so it won't pollute your code. 


## Terminologies and Concepts

### Job

@Job annotation defines a job which has 1 or many tasks. It can have only one start task which must be one of three schedule tasks: FixedRate, FixedDelay and Cron. A job is kicked off by its scheduled start task.

@Job can be annotated on any class in the class path, it does not have to be in same class with it's tasks.

### Task

There are two kinds of tasks: schedule tasks and trailer tasks. There are three schedule tasks: @FixedRate, @FixedDelay and @Cron. Schedule tasks can only be start task. @Task is the only trailer task, it cannot be start task, it can only be chained to a schedule task or another trailer task. 

### Work flow

A job can have one and only one work flow which consists of many tasks. The work flow starts with a start task which must be one of three schedule tasks. Using @TriggeredBy annotation to chain a task to another one to construct the work flow.  

### Session

A job run starts by its scheduled start task is triggered by Java. Each job run creates a OS process which is called session. The session ends when all tasks are completed or one of task fails.


## Hello World   

The hello-world job consists of two tasks, start-task and second-task. The start-task is a Cron task which is scheduled to run in a new session every 5 seconds, after it completes, the second-task will run within the same session.    

	@Job(name = "hello-world")
	public class HellloWorld {
	
		public static void main(String[] args) throws InterruptedException{
			Chao chao = Chao.create();
			chao.start();
			Thread.sleep(16000);
			chao.shutdown();
			chao.awaitTermination();
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
