import java.util.ArrayList;

public class FCFS {
	static int currentProcess;
	static int currentTime = 0;
	static int finishedProcesses = 0;
	static int minStartTimeProcess;
	static int maxStartTimeProcess;
	static float averageWaitTime=0;
	static ArrayList<Process> candidates = new ArrayList<>();

	static void printProcesses() {
		System.out.println("Procesy, ktore beda braly udzial w porownaniu:");
		System.out.println("Etykieta/Czas nadejscia/Czas trwania");
		for (int i = 0; i < Scheduler.processQuantity; i++) {
			System.out.println(Scheduler.processTable.get(i).label + " " + Scheduler.processTable.get(i).startTime + " "
					+ Scheduler.processTable.get(i).entireTime);
		}
		System.out.println("Ponizej momenty czasu oraz procesy, ktore sa aktualnie brane pod uwage:");
		System.out.println("Etykieta/Czas nadejscia/Czas trwania/Pozostaly czas trwania");
	}

	static void findCandidates() {
		candidates.clear();
		for (int i = 0; i < Scheduler.processTable.size(); i++) {
			if ((Scheduler.processTable.get(i).startTime <= currentTime)
					&& (!Scheduler.processTable.get(i).isFinished)) {
				candidates.add(Scheduler.processTable.get(i));
				// System.out.println(candidates.get(candidates.size()-1).label);
			}
		}
	}

	static void findMaximumStartTime() {
		maxStartTimeProcess = candidates.get(0).id;
		// System.out.println("MaxLengthProcess="+maxStartTimeProcess);
		for (int i = 0; i < candidates.size(); i++) {
			if (candidates.get(i).startTime > Scheduler.processTable.get(maxStartTimeProcess).startTime) {
				maxStartTimeProcess = Scheduler.processTable.get(candidates.get(i).id).id;
				// System.out.println("znaleziono pozniejszy");
			}
			// System.out.println("MAXstart: " +
			// workingProcesses.get(maxStartTimeProcess).label);
		}
	}

	static void decide() {
		minStartTimeProcess = Scheduler.processTable.get(maxStartTimeProcess).id;
		// System.out.println("MinStartTimeProcess="+minStartTimeProcess);
		for (int i = 0; i < candidates.size(); i++) {
			if (candidates.get(i).startTime <= Scheduler.processTable.get(minStartTimeProcess).startTime) {
				minStartTimeProcess = Scheduler.processTable.get(candidates.get(i).id).id;
				// System.out.println("Znaleziono wczesniejszy");
			}
			// System.out.println("MINstart: " +
			// workingProcesses.get(minStartTimeProcess).label );
		}
	}
	
	static void findAverageWaitTime()
	{
		for(int i=0; i<Scheduler.processTable.size();i++)
		{
			averageWaitTime+=Scheduler.processTable.get(i).waitingTime;
		}
		averageWaitTime/=Scheduler.processTable.size();
	}

	public static void work() {
		System.out.println("Algorytm: FCFS");
		printProcesses();
		while (finishedProcesses < Scheduler.processTable.size()) {
			for (int i = 0; i < Scheduler.processQuantity; i++) {
				if ((!Scheduler.processTable.get(i).isFinished)
						&& (Scheduler.processTable.get(i).startTime <= currentTime)) {
					System.out.println(Scheduler.processTable.get(i).label + " "
							+ Scheduler.processTable.get(i).startTime + " " + Scheduler.processTable.get(i).entireTime
							+ " " + Scheduler.processTable.get(i).timeRemaining);
				}
			}
			findCandidates();
			if (candidates.size() > 0) {
				findMaximumStartTime();
				decide();
				currentProcess = Scheduler.processTable.get(minStartTimeProcess).id;
				while (!Scheduler.processTable.get(currentProcess).isFinished) {
					System.out.println("Procesowi " + Scheduler.processTable.get(currentProcess).label
							+ " minela jedna jednostka czasu.");
					Scheduler.processTable.get(currentProcess).timeRemaining--;
					currentTime++;
					System.out.println("Czas:" + currentTime);
					if (Scheduler.processTable.get(currentProcess).timeRemaining == 0) {
						Scheduler.processTable.get(currentProcess).waitingTime = currentTime
								- Scheduler.processTable.get(currentProcess).startTime
								- Scheduler.processTable.get(currentProcess).entireTime;
						Scheduler.processTable.get(currentProcess).isFinished = true;
						System.out.println("Proces " + Scheduler.processTable.get(currentProcess).id
								+ " sie skonczyl. Czas oczekiwania wyniosl "
								+ Scheduler.processTable.get(currentProcess).waitingTime + ".");
						finishedProcesses++;
						System.out.println("Skonczone procesy: " + finishedProcesses);
					}
				}
			} else {
				currentTime++;
				System.out.println("Czas:" + currentTime);
			}
		}
		findAverageWaitTime();
		System.out.println("Sredni czas oczekiwania wyniosl "+averageWaitTime+".");
	}
}
