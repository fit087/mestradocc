package main.controler;

import main.entities.Flight;
import main.entities.City;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
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
public class AircraftFileReader {

    private static AircraftFileReader instance = new AircraftFileReader();

    public static AircraftFileReader getIntance(){
        return instance;
    }

    private AircraftFileReader() {
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
    public void readDataFromFile(String path, AirlineNetwork airlineNetwork) throws FileNotFoundException{
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
    private void readCityFromFile(File file, AirlineNetwork airlineNetwork) throws FileNotFoundException{

        ArrayList<City> cities = new ArrayList<City>();

        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()){
            String cityName = scanner.next();
            Integer groundTime = scanner.nextInt();
            cities.add(new City(cityName, groundTime));
        }

        scanner.close();

        airlineNetwork.setCities(cities);
    }

    /**
     * Ler os voos de um arquivo preenchendo os dados.
     * 
     * @param file Arquivo que contem os dados dos voos.
     * @param airlineNetwork AirlineNetword a ser preenchida
     * @throws FileNotFoundException
     */
    private void readFlightFromFile(File file, AirlineNetwork airlineNetwork) throws FileNotFoundException {
        ArrayList<Flight> flights = new ArrayList<Flight>();

        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()){

            String fligthName = scanner.next();
            int departureDay = scanner.nextInt();

            String [] aux = scanner.next().split(":"); //Obtem o horario no formato HH:MM
            int departureTime = departureDay*24*60 + (new Integer(aux[0]) * 60) + (new Integer(aux[1]));

            int arrivalDay = scanner.nextInt();

            aux = scanner.next().split(":");
            int arrivalTime = arrivalDay*24*60 + (new Integer(aux[0])*60) + new Integer(aux[1]);

            City departureCity = airlineNetwork.getCity(scanner.next());

            City arrivalCity = airlineNetwork.getCity(scanner.next());

            flights.add(new Flight(fligthName, flights.size(), departureCity, arrivalCity, departureTime, arrivalTime));

        }

        scanner.close();

        airlineNetwork.setFlights(flights);
    }

    /**
     * Ler os tempos de um arquivo preenchendo os dados.
     *
     * @param file Arquivo que contem os dados dos voos.
     * @param airlineNetwork AirlineNetword a ser preenchida
     * @throws FileNotFoundException
     */
    private void readTimesFromFile(File file, AirlineNetwork airlineNetwork) throws FileNotFoundException{

        Scanner scanner = new Scanner(file);

        while(scanner.hasNext()){

            String departureCityName = scanner.next();
            String arrivalCityName = scanner.next();
            int time = scanner.nextInt();

            City departureCity = airlineNetwork.getCity(departureCityName);
            City arrivalCity = airlineNetwork.getCity(arrivalCityName);

            departureCity.addCityTime(arrivalCity, time);
            arrivalCity.addCityTime(departureCity, time);

        }

        scanner.close();
        
    }

}
