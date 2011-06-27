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


        StringBuilder sb = new StringBuilder();
        int cont = 0;
        ArrayList<City> cities = airlineNetwork.getCities();
        for (int i = 0; i < cities.size() - 1; i++) {
            City orig = cities.get(i);
            for (int j = (i + 1); j < cities.size(); j++) {
                City dest = cities.get(j);
                Integer duration = orig.getFlightTimes().get(dest);
                if (duration == null) {
                    duration = 99999;
                }


                sb.append(i + " " + j + " " + (duration + orig.getGroundTime()) + "\n");
                cont++;
            }
        }

        bw.write("\n");
        bw.write(cont + "\n");
        bw.write("\n");
        bw.write(sb.toString());


        bw.close();

    }

    public static void writeFormatedAirlineNetworkForCplexWeekExtended(AirlineNetwork airlineNetwork, File output) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(output));

        int numberOfDays = 7;
        int oneday = 24 * 60 * 60;

        ArrayList<Flight> flights = airlineNetwork.getFlights();
        ArrayList<City> citys = airlineNetwork.getCities();

        for (City city : citys) {
            System.out.println("CITY " + city.getName());
        }


        bw.write(flights.size() * numberOfDays + " " + 10);
        bw.write("\n");
        bw.write("\n");


        //Escrece voos

        for (int i = 0; i < numberOfDays; i++) {
            for (Flight flight : flights) {

                Integer departureTime = flight.getDepartureTime() * i * oneday;
                bw.write((departureTime - flight.getGroundTime()) + " " + (flight.getDuration() + flight.getGroundTime()) + " " + getCityPos(citys, flight.getDepartureCity()) + " " + getCityPos(citys, flight.getArrivalCity()));
                bw.write("\n");

            }
        }


        StringBuilder sb = new StringBuilder();
        int cont = 0;
        ArrayList<City> cities = airlineNetwork.getCities();
        for (int i = 0; i < cities.size() - 1; i++) {
            City orig = cities.get(i);
            for (int j = (i + 1); j < cities.size(); j++) {
                City dest = cities.get(j);
                Integer duration = orig.getFlightTimes().get(dest);
                if (duration == null) {
                    duration = 99999;
                }


                sb.append(i + " " + j + " " + (duration + orig.getGroundTime()) + "\n");
                cont++;
            }
        }

        bw.write("\n");
        bw.write(cont + "\n");
        bw.write("\n");
        bw.write(sb.toString());


        bw.close();

    }

    public static void writeFormatedInputFlightsTimes(AirlineNetwork airlineNetwork, File selectedFile) throws IOException {
        ArrayList<StringBuilder> sb = new ArrayList<StringBuilder>();

        boolean divided = false;
        int index = 0;
        int nOfInput = 0;
        for (int i = 0; i < airlineNetwork.getCities().size() - 1; i++) {
            for (int j = (i + 1); j < airlineNetwork.getCities().size(); j++) {
                City origCity = airlineNetwork.getCities().get(i);
                City destCity = airlineNetwork.getCities().get(j);
                Integer time = origCity.getFlightTimes().get(destCity);
                
                if(time != 99999)
                nOfInput++;
            }
        }
        
        int divPos = nOfInput/4;
        int moreColunsIn = nOfInput%4;
        int numberOfResets = 0;

        for (int i = 0; i < airlineNetwork.getCities().size() - 1; i++) {
            for (int j = (i + 1); j < airlineNetwork.getCities().size(); j++) {
                City origCity = airlineNetwork.getCities().get(i);
                City destCity = airlineNetwork.getCities().get(j);
                Integer time = origCity.getFlightTimes().get(destCity);

                
                if(time == 99999) continue;
                if(divPos <= index){
                    
                    if(moreColunsIn != 0){
                        if(divPos < index){
                            numberOfResets++;
                            index =0;
                            moreColunsIn--;
                            divided = true;
                        }
                    }
                    else{
                        index = 0;
                        numberOfResets++;
                        divided = true;
                    }
                }
                
                if (!divided) {
                    sb.add(new StringBuilder());
                }

                if (divided) {
                    sb.get(index).append(" & ");
                }

                sb.get(index).append(String.format("%s %s %04d", origCity.getName(), destCity.getName(), time));

            

                index++;
            }
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile));
            
            for (StringBuilder stringBuilder : sb) {
                bw.write(stringBuilder + " \\\\\n");
            }
            
            bw.close();
        }
    }

    public static void writeFormatedInputFlights(AirlineNetwork airlineNetwork, File selectedFile) throws IOException {
        ArrayList<StringBuilder> sb = new ArrayList<StringBuilder>();


        int divPos = airlineNetwork.getFlights().size() / 2;

        if (airlineNetwork.getFlights().size() % 2 == 1) {
            divPos++;
        }

        boolean notDivided = true;
        int index = 0;

        for (int i = 0; i < airlineNetwork.getFlights().size(); i++) {
            Flight f = airlineNetwork.getFlights().get(i);

            if (i == divPos) {
                notDivided = false;
            }

            if (notDivided) {
                sb.add(new StringBuilder());
                sb.get(i).append(String.format("%03d %s", (i + 1), f.toString()));
            } else {
                sb.get(index).append(String.format(" & & & %03d %s \\\\", (i + 1), f.toString()));
                index++;
            }
        }


        BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile));
        for (StringBuilder stringBuilder : sb) {
            bw.write(stringBuilder.toString() + "\r\n\r\n");
        }
        bw.close();
    }

    public static void writeFormatedInputAirlineNetwork(AirlineNetwork airlineNetwork, File selectedFile) throws IOException {
        ArrayList<Track> solution = airlineNetwork.getBestNetwork();

        BufferedWriter bw = new BufferedWriter(new FileWriter(selectedFile));

        bw.write(solution.size() + "\n");

        for (int i = 0; i < solution.size(); i++) {
            Track track = solution.get(i);

            bw.write(track.getFlights().size() + " ");

            for (int j = 0; j < track.getFlights().size(); j++) {
                Flight flight = track.getFlight(j);

                bw.write(flight.getNumber() + " ");

            }

            bw.write("\n");

        }

        bw.close();
    }
}
