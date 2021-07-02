public class Process {
	int id;
	int entireTime;
	int priority;
	int timeRemaining;
	int startTime;
	int waitingTime;
	boolean isFinished;
	String label;
	
	public Process(int id, int entireTime, int priority, int startTime)
	{
		this.id = id;
		this.entireTime = entireTime;
		this.priority = priority;
		this.timeRemaining = entireTime;
		this.startTime = startTime;
		this.isFinished = false;
		this.label = "P"+id;
		this.waitingTime=0;
	}
	public Process(int id, int entireTime, int startTime, String label, int priority)
	{
		this.id = id;
		this.entireTime = entireTime;
		this.priority = priority;
		this.timeRemaining = entireTime;
		this.startTime = startTime;
		this.isFinished = false;
		this.label = label;
		this.waitingTime=0;
	}
	public Process(int id, int entireTime, int startTime, String label)
	{
		this.id = id;
		this.entireTime = entireTime;
		this.priority = 0;
		this.timeRemaining = entireTime;
		this.startTime = startTime;
		this.isFinished = false;
		this.label = label;
		this.waitingTime=0;
	}
	
}
