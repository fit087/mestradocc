package main.reader;

import main.entities.Flight;
import main.entities.City;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import javax.crypto.Cipher;
import main.entities.AirlineNetwork;

/**
 *
 * Responsável por inserir os dados sobre voos, cidades e tempos lidos dos 
 * arquivos passados.
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class ARPFileReader {

    private ARPFileReader() {
    }

    /**
     * Ler todas as informacoes apartir da pasta informada.
     * Os arquivos devem estar no formato:
     * Voos: voos.txt
     * Cidades: cidades.txt
     * Tempos: tempos.txt
     * @param airlineNetwork AirlineNetword a ser preenchida*
     * @param path Path do caminho de onde deve estar as instancias
     */
    public static void readDataFromFile(String path, AirlineNetwork airlineNetwork) throws FileNotFoundException{
        String flight = "voos.txt";
        String city = "cidades.txt";
        String times = "tempos.txt";

        readCityFromFile(new File(path + File.separator + city), airlineNetwork);
        readFlightFromFile(new File(path + File.separator + flight), airlineNetwork);
        readTimesFromFile(new File(path + File.separator + times), airlineNetwork);
    }

    /**
     * Ler as cidades de um arquivo preenchendo os nomes e os tempos de solos
     * das cidades.
     *
     * @param file Arquivo que contem os dados das cidades.
     * @param airlineNetwork AirlineNetword a ser preenchida
     * @throws FileNotFoundException
     */
    public static void readCityFromFile(File file, AirlineNetwork airlineNetwork) throws FileNotFoundException{

        ArrayList<City> cities = new ArrayList<City>();

        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()){
            String cityName = scanner.next();
            Integer groundTime = scanner.nextInt();
            cities.add(new City(cityName, groundTime));
        }

        scanner.close();

        Collections.sort(cities, new Comparator<City>() {
            public int compare(City t, City t1) {
                return t.getName().compareTo(t1.getName());
            }
        });

        airlineNetwork.setCities(cities);
    }

    /**
     * Ler os voos de um arquivo preenchendo os dados.
     * 
     * @param file Arquivo que contem os dados dos voos.
     * @param airlineNetwork AirlineNetword a ser preenchida
     * @throws FileNotFoundException
     */
    public static void readFlightFromFile(File file, AirlineNetwork airlineNetwork) throws FileNotFoundException {
        ArrayList<Flight> flights = new ArrayList<Flight>();

        Set<String> cities = new TreeSet<String>();

        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()){

            String fligthName = scanner.next();
            int departureDay = scanner.nextInt();

            String [] aux = scanner.next().split(":"); //Obtem o horario no formato HH:MM
            int departureTime = departureDay*24*60 + (new Integer(aux[0]) * 60) + (new Integer(aux[1]));

            int arrivalDay = scanner.nextInt();

            aux = scanner.next().split(":");
            int arrivalTime = arrivalDay*24*60 + (new Integer(aux[0])*60) + new Integer(aux[1]);

            String dCity = scanner.next();
            String aCity = scanner.next();

            cities.add(dCity);
            cities.add(aCity);

            City departureCity = airlineNetwork.getCity(dCity);
            City arrivalCity = airlineNetwork.getCity(aCity);

            flights.add(new Flight(fligthName, flights.size(), departureCity, arrivalCity, departureTime, arrivalTime));

        }

        scanner.close();

        

        for (String string : cities) {
            System.out.println(string);
        }

        Collections.sort(flights, new Comparator<Flight>() {

            public int compare(Flight t, Flight t1) {
                return t.getDepartureTime() - t1.getDepartureTime();
            }
        });

        airlineNetwork.setFlights(flights);
    }

    /**
     * Ler os tempos de um arquivo preenchendo os dados.
     *
     * @param file Arquivo que contem os dados dos voos.
     * @param airlineNetwork AirlineNetword a ser preenchida
     * @throws FileNotFoundException
     */
    private static void readTimesFromFile(File file, AirlineNetwork airlineNetwork) throws FileNotFoundException{

        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()){

            String departureCityName = scanner.next();
            String arrivalCityName = scanner.next();
            int time = scanner.nextInt();

            City departureCity = airlineNetwork.getCity(departureCityName);
            City arrivalCity = airlineNetwork.getCity(arrivalCityName);

            if(departureCity == null || arrivalCity == null) {
                System.out.println("Cidade inexistente: " + departureCity == null ? departureCityName : arrivalCityName);
                continue;
            }

            departureCity.addCityTime(arrivalCity, time);
            arrivalCity.addCityTime(departureCity, time);

        }

        scanner.close();
        
    }

}
