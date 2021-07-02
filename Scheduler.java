import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.System;
import java.text.SimpleDateFormat;
import java.util.Random;

import org.apache.commons.io.output.TeeOutputStream;

public class Scheduler {
	public static int processQuantity;
	public static int priorityTreshold;
	static ArrayList<Process> processTable;
	static Date date = new Date() ;
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
	static File raport = new File("raport-procesy"+dateFormat.format(date)+".txt");

	//PLIK Z KTOREGO CHCEMY IMPORTOWAC PROCESY MA MIEC W KOLEJNYCH LINIJKACH PROCESY W FORMACIE:
	//CZAS_TRWANIA CZAS_STARTU ETYKIETA OPCJONALNIE_PRIORYTET
	//(ROZDZIELONE SPACJAMI)
	public static void main(String[] args) throws IOException {

		processTable = new ArrayList<>();
		// procesTabela = new ArrayList<Process>();
		Scanner reader = new Scanner(System.in);
		System.out.println(
				"Wpisz numer algorytmu; 1-FCFS, 2-SJF bez wywlaszczen, 3-SJF z wywlaszczeniami, 4-priorytetowy");
		int algorithm = reader.nextInt();
		System.out.println("Wpisz tryb dzialania; 1-losowy, 2-z pliku");
		int mode = reader.nextInt();
		if(algorithm==4)
		{
			System.out.println("Podaj pułap zagłodzenia");
			priorityTreshold = reader.nextInt();
		}
		// TRYB PROCESOW LOSOWY
		if (mode == 1) {
			System.out.println("Podaj ilosc procesow");
			processQuantity = reader.nextInt();
			for (int i = 0; i < processQuantity; i++) {
				processTable.add(new Process(i, (int) (Math.random() * 20) + 1, (int) (Math.random() * 10)+1,
						(int) (Math.random() * 5 * processQuantity + 1)));
			}
		}
		// TRYB PROCESOW Z PLIKU
		if (mode == 2) {
			String textLine;
			String[] processCreator;
			ArrayList<String[]> processCreators = new ArrayList<>();
			FileReader fileReader = new FileReader("import_procesy.txt");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			// ODCZYTYWANIE DANYCH O PROCESACH Z PLIKU DO ARRAYLISTY TABEL
			try {
				processQuantity = 0;
				textLine = bufferedReader.readLine();
				do {
					processCreator = textLine.split(" ");
					processCreators.add(processCreator);
					processQuantity++;
					textLine = bufferedReader.readLine();
				} while (textLine != null);
			} finally {
				bufferedReader.close();
				fileReader.close();
			}
			// KONSTRUOWANIE PROCESOW Z ARRAYLISTY TABEL
			for (int i = 0; i < processQuantity; i++) {
				if (processCreator.length == 3) {
					processTable.add(new Process(i, Integer.parseInt(processCreators.get(i)[0]),
							Integer.parseInt(processCreators.get(i)[1]), processCreators.get(i)[2]));
				}
				if (processCreator.length == 4) {
					processTable.add(new Process(i, Integer.parseInt(processCreators.get(i)[0]),
							Integer.parseInt(processCreators.get(i)[1]), processCreators.get(i)[2],
							Integer.parseInt(processCreators.get(i)[3])));
				}
			}
		}
		try {
		    FileOutputStream fos = new FileOutputStream(raport);
		    TeeOutputStream myOut=new TeeOutputStream(System.out, fos);
		    PrintStream ps = new PrintStream(myOut, true);
		    System.setOut(ps);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		// WYBÓR ALGORYTMU
		if (algorithm == 1) {
			FCFS.work();
		}
		if (algorithm == 2) {
			sjfNoPreemption.work();
		}
		if (algorithm == 3) {
			sjfPreemption.work();
		}
		if (algorithm == 4) {
			priority.work();
		}

		reader.close();

	}

}
