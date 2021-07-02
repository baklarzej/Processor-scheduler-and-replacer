import java.util.ArrayList;

public class priority {
	static int currentProcess;
	static int currentTime = 0;
	static int finishedProcesses = 0;
	static int minPriorityProcess;
	static int maxPriorityProcess;
	static float averageWaitTime=0;
	static ArrayList<Process> candidates = new ArrayList<>();
	static ArrayList<Integer> candidatesDelay = new ArrayList<>();

	// TEN ALGORYTM POSTARZA PROCES, JEŻELI MA ON PRZED SOBĄ TYLE PROCESÓW (O
	// LEPSZYM - "NIŻSZYM" PRIORYTECIE), CO PUŁAP ZAGŁODZENIA (PODANY PRZEZ
	// UZYTKOWNIKA)

	static void printProcesses() {
		System.out.println("Procesy, ktore beda braly udzial w porownaniu:");
		System.out.println("Etykieta/Czas nadejscia/Czas trwania/Priorytet");
		for (int i = 0; i < Scheduler.processQuantity; i++) {
			System.out.println(Scheduler.processTable.get(i).label + " " + Scheduler.processTable.get(i).startTime + " "
					+ Scheduler.processTable.get(i).entireTime + " "+Scheduler.processTable.get(i).priority);
		}
		System.out.println("Ponizej momenty czasu oraz procesy, ktore sa aktualnie brane pod uwage:");
		System.out.println("Etykieta/Czas nadejscia/Czas trwania/Priorytet/Pozostaly czas trwania");
	}

	static void findCandidates() {
		candidatesDelay.clear();
		for (int i = 0; i < Scheduler.processTable.size(); i++) {
			candidatesDelay.add(0);
		}
		candidates.clear();
		for (int i = 0; i < Scheduler.processTable.size(); i++) {
			if ((Scheduler.processTable.get(i).startTime <= currentTime)
					&& (!Scheduler.processTable.get(i).isFinished)) {
				candidates.add(Scheduler.processTable.get(i));
				// System.out.println(candidates.get(candidates.size() -
				// 1).label);
			}
		}
	}

	static void findMaximumLength() {
		maxPriorityProcess = candidates.get(0).id;
		// System.out.println("MaxPriorityProcess=" + maxPriorityProcess);
		for (int i = 0; i < candidates.size(); i++) {
			if (candidates.get(i).priority > Scheduler.processTable.get(maxPriorityProcess).priority) {
				maxPriorityProcess = Scheduler.processTable.get(candidates.get(i).id).id;
				// System.out.println("znaleziono o wiekszym priorytecie");
			}
			// System.out.println("MAXprior: " +
			// Scheduler.processTable.get(maxPriorityProcess).label);
		}
	}

	static void decide() {
		minPriorityProcess = Scheduler.processTable.get(maxPriorityProcess).id;
		// System.out.println("MinPriorityProcess=" + minPriorityProcess);
		for (int i = 0; i < candidates.size(); i++) { // SZUKANIE KANDYDATA O
														// NAJMNIEJSZYM CZASIE
														// TRWANIA
			if (candidates.get(i).entireTime <= Scheduler.processTable.get(minPriorityProcess).entireTime) {
				minPriorityProcess = Scheduler.processTable.get(candidates.get(i).id).id;
				// System.out.println("Znaleziono krotszy");
			}
			// System.out.println("MINprior: " +
			// Scheduler.processTable.get(minPriorityProcess).label);
		}
		for (int i = 0; i < candidates.size(); i++) { /// SPRAWDZENIE CZY NIE MA
														/// KANDYDATÓW O RÓWNYCH
														/// PRIORYTETACH
			if (candidates.get(i).entireTime == Scheduler.processTable.get(minPriorityProcess).entireTime) {
				if (candidates.get(i).startTime < Scheduler.processTable.get(minPriorityProcess).startTime) {
					// System.out.println("Candidates equality!");
					minPriorityProcess = Scheduler.processTable.get(candidates.get(i).id).id;
				}
			}
		}
	}

	static void age() {
		for (int i = 0; i < candidates.size(); i++) {
			if ((candidatesDelay.get(i) > Scheduler.priorityTreshold) && (candidates.get(i).priority > 0)) {
				candidates.get(i).priority--;
			}
		}
	}

	static void countDelay() {
		for (int i = 0; i < candidates.size(); i++) {
			for (int j = 0; j < candidates.size(); j++) {
				if ((candidates.get(i).priority < candidates.get(j).priority) && (i != j)) {
					candidatesDelay.set(i, candidatesDelay.get(i).intValue() + 1);
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
		System.out.println("Algorytm: Priorytetowy");
		printProcesses();
		while (finishedProcesses < Scheduler.processTable.size()) {
			findCandidates();
			if (candidates.size() > 0) {
				findMaximumLength();
				decide();
				countDelay();
				age();
				currentProcess = Scheduler.processTable.get(minPriorityProcess).id;
				for (int i = 0; i < Scheduler.processQuantity; i++) {
					if ((!Scheduler.processTable.get(i).isFinished)
							&& (Scheduler.processTable.get(i).startTime <= currentTime)) {
						/*
						 * System.out.println(Scheduler.processTable.get(i).id +
						 * " " + Scheduler.processTable.get(i).timeRemaining +
						 * " " + Scheduler.processTable.get(i).priority + " " +
						 * Scheduler.processTable.get(i).startTime + " " +
						 * Scheduler.processTable.get(i).label + " " +
						 * candidatesDelay.get(i));
						 */
						System.out.println(
								Scheduler.processTable.get(i).label + " " + Scheduler.processTable.get(i).startTime
										+ " " + Scheduler.processTable.get(i).entireTime + " "
										+ Scheduler.processTable.get(i).priority + " "
										+ Scheduler.processTable.get(i).timeRemaining);
					}
				}
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
				countDelay();
				age();
				currentTime++;
				System.out.println("Czas:" + currentTime);
			}
		}
		findAverageWaitTime();
		System.out.println("Sredni czas oczekiwania wyniosl "+averageWaitTime+".");
	}
}
