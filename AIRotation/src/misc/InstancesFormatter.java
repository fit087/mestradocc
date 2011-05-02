/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import javax.swing.JFileChooser;
import main.entities.AirlineNetwork;
import main.entities.City;
import main.entities.Flight;
import main.reader.ARPFileReader;
import util.TimeUtil;

/**
 *
 * @author alexanderdealmeidapinto
 */
public class InstancesFormatter {


    public static void main(String [] args) throws FileNotFoundException{
        String path = "/Users/alexanderdealmeidapinto/Documents/Mestrado/Projeto/svn/InstanciaTAM/";
        JFileChooser chooserSave = new JFileChooser();

            
            AirlineNetwork airlineNetwork = new AirlineNetwork(null);
            ARPFileReader.readCityFromFile(new File(path + "cidades.txt"), airlineNetwork);
            ARPFileReader.readFlightFromFile(new File(path + "voos.txt"), airlineNetwork);

            for (Flight flight : airlineNetwork.getFlights()) {
                System.out.println(flight.getName() + " " + 
                        TimeUtil.getFormatedTime(flight.getDepartureTime()) + " " +
                        TimeUtil.getFormatedTime(flight.getArrivalTime()) + " " +
                        flight.getDepartureCity().getName() + " " +
                        flight.getArrivalCity().getName()) ;
            }

            generateMaxFlightTime(airlineNetwork);

    }

    public static void generateMaxFlightTime(AirlineNetwork airlineNetwork){
        ArrayList<City> cities = airlineNetwork.getCities();
        HashMap<String, HashMap<String,Integer> > flightTimes = new HashMap<String, HashMap<String, Integer>>();

        for(int i = 0; i < cities.size() - 1; i++){
            City origem = cities.get(i);

            HashMap<String, Integer> map = new HashMap<String, Integer>();
            flightTimes.put(origem.getName(), map);
            for(int j = i+1; j < cities.size(); j++){
                City destino = cities.get(j);

                map.put(destino.getName(), 0);
                System.out.println("Cities " + origem.getName() + " " + destino.getName());
            }
        }

        for (Flight flight : airlineNetwork.getFlights()) {
            int duration = flight.getDuration();

            if(duration < 0){
                System.out.println("Flight with error " + flight.toString());
                System.exit(1);
            }

            String city1 = flight.getDepartureCity().getName();
            String city2 = flight.getArrivalCity().getName();

            if(city1.compareTo(city2) > 0){
                String temp = city1;
                city1 = city2;
                city2 = temp;
            }

            int value = flightTimes.get(city1).get(city2);
            if(duration > value){
                flightTimes.get(city1).put(city2, duration);
            }

            //System.out.println(">>> " + city1 + " " + city2 );
        }

        Set<String> origens = flightTimes.keySet();
        ArrayList<String> origenslist = new ArrayList<String>(origens);

        Collections.sort(origenslist);

        for (String origem : origenslist) {
            HashMap<String, Integer> map = flightTimes.get(origem);
            Set<String> destinos = map.keySet();
            ArrayList<String> destinoslist = new ArrayList<String>(destinos);


            Collections.sort(destinoslist);
            for (String destino : destinoslist) {
                int value = map.get(destino);

                if(value == 0){
                    value = 99999;
                }

                System.out.println(origem + " " + destino + " " + value);
            }
        }
    }

}
