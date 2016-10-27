# chao-open  open source Java scheduler + workflow

	@Job(name = "hello-world")
	public class HellloWorld {
	
		public static void main(String[] args) throws InterruptedException{
			Core core = new EmbeddedCore();
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

