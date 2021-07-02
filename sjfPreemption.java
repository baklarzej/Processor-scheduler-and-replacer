import java.util.ArrayList;

public class sjfPreemption {
	static int currentProcess;
	static int currentTime = 0;
	static int finishedProcesses = 0;
	static int minLengthProcess;
	static int maxLengthProcess;
	static float averageWaitTime=0;
	static ArrayList<Process> candidates = new ArrayList<>();
	
	static void printProcesses()
	{
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
				//System.out.println(candidates.get(candidates.size() - 1).label);
			}
		}
	}

	static void findMaximumLength() {
		maxLengthProcess = candidates.get(0).id;
		//System.out.println("MaxLengthProcess=" + maxLengthProcess);
		for (int i = 0; i < candidates.size(); i++) {
			if (candidates.get(i).timeRemaining > Scheduler.processTable.get(maxLengthProcess).timeRemaining) {
				maxLengthProcess = Scheduler.processTable.get(candidates.get(i).id).id;
				// System.out.println("znaleziono dluzszy");
			}
			//System.out.println("MAXlength: " + Scheduler.processTable.get(maxLengthProcess).label);
		}
	}

	static void decide() {
		minLengthProcess = Scheduler.processTable.get(maxLengthProcess).id;
		//System.out.println("MinLenghtProcess=" + minLengthProcess);
		for (int i = 0; i < candidates.size(); i++) { // SZUKANIE KANDYDATA O
														// NAJMNIEJSZYM CZASIE
														// TRWANIA
			if (candidates.get(i).timeRemaining <= Scheduler.processTable.get(minLengthProcess).timeRemaining) {
				minLengthProcess = Scheduler.processTable.get(candidates.get(i).id).id;
				// System.out.println("Znaleziono krotszy");
			}
			//System.out.println("MINlength: " + Scheduler.processTable.get(minLengthProcess).label);
		}
		for (int i = 0; i < candidates.size(); i++) { /// SPRAWDZENIE CZY NIE MA
														/// KANDYDATÓW O RÓWNYCH
														/// CZASACH TRWANIA
			if (candidates.get(i).timeRemaining == Scheduler.processTable.get(minLengthProcess).timeRemaining) {
				if (candidates.get(i).startTime < Scheduler.processTable.get(minLengthProcess).startTime) {
					// System.out.println("Candidates equality!");
					minLengthProcess = Scheduler.processTable.get(candidates.get(i).id).id;
				}
			}
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
		System.out.println("Algorytm: SJF z wywlaszczeniami");
		printProcesses();
		while (finishedProcesses < Scheduler.processTable.size()) {
			for (int i = 0; i < Scheduler.processQuantity; i++) {
				if ((!Scheduler.processTable.get(i).isFinished)&&(Scheduler.processTable.get(i).startTime<=currentTime)) {
					System.out.println(
							Scheduler.processTable.get(i).label + " " + Scheduler.processTable.get(i).startTime
									+ " " + Scheduler.processTable.get(i).entireTime+ " "+Scheduler.processTable.get(i).timeRemaining);
				}
			}
			findCandidates();
			if (candidates.size() > 0) {
				findMaximumLength();
				decide();
				currentProcess = Scheduler.processTable.get(minLengthProcess).id;
				System.out.println("Procesowi " + Scheduler.processTable.get(currentProcess).label
						+ " minela jedna jednostka czasu.");
				Scheduler.processTable.get(currentProcess).timeRemaining--;
				currentTime++;
				System.out.println("Czas:"+currentTime);
				if (Scheduler.processTable.get(currentProcess).timeRemaining == 0) {
					Scheduler.processTable.get(currentProcess).waitingTime = currentTime
							- Scheduler.processTable.get(currentProcess).startTime
							- Scheduler.processTable.get(currentProcess).entireTime;
					Scheduler.processTable.get(currentProcess).isFinished = true;
					System.out.println("Proces " + Scheduler.processTable.get(currentProcess).id + " sie skonczyl. Czas oczekiwania wyniosl "+Scheduler.processTable.get(currentProcess).waitingTime+".");
					finishedProcesses++;
					System.out.println("Skonczone procesy: " + finishedProcesses);
				}

			} else {
				currentTime++;
				System.out.println("Czas:"+currentTime);
			}
		}
		findAverageWaitTime();
		System.out.println("Sredni czas oczekiwania wyniosl "+averageWaitTime+".");
	}
}
