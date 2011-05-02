/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import main.entities.AirlineNetwork;
import main.entities.City;
import main.entities.Flight;
import main.entities.Track;

/**
 *
 * @author alexanderdealmeidapinto
 */
public class SolutionUtil {

    public static void writeFormatedSolutionForArticle(ArrayList<Track> bestNetwork, File outputFile) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

        int totalCost = AirlineNetwork.getTotalCost(bestNetwork);

        bw.write("Objective Value: " + totalCost);
        bw.write("\n\n");

        ArrayList<StringBuilder> linhas = new ArrayList<StringBuilder>();
        int cont = 0;

        for (int i = 0; i < bestNetwork.size(); i++) {
            Track track = bestNetwork.get(i);
            if (linhas.isEmpty()) {
                linhas.add(new StringBuilder());
            }

            linhas.get(0).append(String.format("Rota[%02d - %d]\t\t\t\t\t\t", (i + 1), track.numberOfFlights()));

            for (int j = 0; j < track.getFlights().size(); j++) {
                if (linhas.size() < (1 + (j + 1))) {
                    linhas.add(new StringBuilder());
                    for (int k = 0; k < cont; k++) {
                        linhas.get(j + 1).append("\t\t\t\t\t\t\t");
                    }


                }

                Flight flight = track.getFlight(j);

                if (cont > 0) {
                    linhas.get(j + 1).append("\t\t");
                }

                linhas.get(j + 1).append(flight.toString());


            }

            cont++;
            if (cont == 3) {
                cont = 0;
                for (int j = 0; j < linhas.size(); j++) {
                    bw.write(linhas.get(j).toString() + "\n");
                }

                bw.write("\n\n");

                linhas.clear();
            }


        }


        bw.close();
    }

    public static void writeFormatedSolutionForInstance(ArrayList<Track> bestNetwork, File outputFile) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

        int totalCost = AirlineNetwork.getTotalCost(bestNetwork);

        bw.write("cost=" + totalCost);
        bw.write("\n");
        bw.write("number_of_trail=" + bestNetwork.size());
        bw.write("\n\n");

        for (int i = 0; i < bestNetwork.size(); i++) {
            Track track = bestNetwork.get(i);
            bw.write("trail=" + (i + 1));
            bw.write("\n");
            bw.write("number_of_flight=" + track.numberOfFlights());
            bw.write("\n");

            for (int j = 0; j < track.getFlights().size(); j++) {
                Flight flight = track.getFlight(j);

                bw.write(flight.toString() + "\n");
            }

            if (i != bestNetwork.size() - 1) {
                bw.write("\n");
            }

        }

        bw.close();
    }

    public static int getCityPos(ArrayList<City> cities, City city) {
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getName().equals(city.getName())) {
                return i;
            }

        }

        return -1;

    }

    public static void writeFormatedAirlineNetworkForCplex(AirlineNetwork airlineNetwork, File output) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(output));

        ArrayList<Flight> flights = airlineNetwork.getFlights();
        ArrayList<City> citys = airlineNetwork.getCities();

        for (City city : citys) {
            System.out.println("CITY " + city.getName());
        }


        bw.write(flights.size() + " " + 10);
        bw.write("\n");
        bw.write("\n");

        for (Flight flight : flights) {

            bw.write((flight.getDepartureTime() - flight.getGroundTime()) + " " + (flight.getDuration() + flight.getGroundTime()) + " " + getCityPos(citys, flight.getDepartureCity()) + " " + getCityPos(citys, flight.getArrivalCity()));
            bw.write("\n");

        }




        bw.close();

    }

    public static void writeFormatedInputAirlineNetwork(AirlineNetwork airlineNetwork, File selectedFile) throws IOException {
        ArrayList<Track> solution = airlineNetwork.getBestNetwork();

        BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile));

        bw.write(solution.size() + "\n");

        for(int i = 0; i < solution.size(); i++){
            Track track = solution.get(i);

            bw.write(track.getFlights().size() + " ");

            for(int j = 0; j < track.getFlights().size(); j++){
                Flight flight = track.getFlight(j);

                bw.write(flight.getNumber() + " ");

            }

            bw.write("\n");

        }

        bw.close();
    }
}
