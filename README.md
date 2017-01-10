# Chao Open - Java open source scheduler + workflow library

	@Job(name = "hello-world")
	public class HelloWorld {
	
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

