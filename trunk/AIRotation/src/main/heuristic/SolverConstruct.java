/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.heuristic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import main.entities.AirlineNetwork;
import main.entities.Flight;
import main.entities.Track;

/**
 *
 * @author alexanderdealmeidapinto
 */
public class SolverConstruct {
    public static void constructFromFile(AirlineNetwork airlineNetwork, File file) throws FileNotFoundException{
        Scanner scan = new Scanner(file);

        int nt = scan.nextInt();
        ArrayList<Track> network = new ArrayList<Track>();

        System.out.println("Numero de trilhos = " + nt);

        for(int i = 0; i < nt; i++){
            int n = scan.nextInt();

            System.out.println("Numero de voos = " + n);

            Track track = new Track(i);
            Flight lastFlight = null;
            for(int j = 0; j < n; j++){
                int k = scan.nextInt();

            System.out.println("\tVoo = " + k);

                Flight actualFlight = airlineNetwork.getFlights().get(k);


                if(lastFlight != null){
                    int delay = lastFlight.getRealArrivalTime() - (actualFlight.getDepartureTime() - actualFlight.getGroundTime());

                    if(delay > 0)
                    actualFlight.setDelay(delay);

                }

                track.addFlight(actualFlight);
                lastFlight = actualFlight;
            }

            network.add(track);
        }

        scan.close();

        airlineNetwork.setBestNetwork(network, AirlineNetwork.getTotalCost(network));
    }
}
