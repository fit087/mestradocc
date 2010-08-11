/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.heuristic;

import java.util.ArrayList;
import main.entities.AirlineNetwork;
import main.entities.Flight;
import main.entities.Track;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class ARPOtimizator {

    /**
     * Relaxa todos os atrasos que não afetam a inviabilidade da solução.
     * @param network
     * @return
     *
     * O tempo total de atraso que foi relaxado.
     */
    public static int relaxAllDelays(ArrayList<Track> network) {
        int relaxedDelay = 0;

        for (Track track : network) {

            for (int i = track.numberOfFlights() - 1; i >= 0; i--) {

                Flight flight = track.getFlight(i);

                if (flight.getDelay() < 0) {
                    Flight nextFlight = (i == track.numberOfFlights() - 1) ? null : track.getFlight(i + 1);
                    if (nextFlight == null) {
                        relaxedDelay += (-flight.getDelay());
                        flight.setDelay(0);
                    } else {
                        int minDelay = (nextFlight.getRealDepartureTime() - nextFlight.getGroundTime()) - flight.getArrivalTime();
                        if (minDelay > 0) {
                            relaxedDelay += (-flight.getDelay());
                            flight.setDelay(0);
                        } else {
                            if(minDelay < flight.getDelay()) System.exit(10);
                            relaxedDelay += (-flight.getDelay() + minDelay);
                            flight.setDelay(minDelay);
                        }
                    }

                } else if (flight.getDelay() > 0) {
                    Flight previosFlight = (i == 0) ? null : track.getFlight(i - 1);
                    if (previosFlight == null) {
                        relaxedDelay += Math.abs(flight.getDelay());
                        flight.setDelay(0);
                    } else {

                        int minDelay = previosFlight.getRealArrivalTime() - (flight.getDepartureTime() - flight.getGroundTime());
                        if (minDelay < 0) {
                            relaxedDelay += flight.getDelay();
                            flight.setDelay(0);
                        } else {
                            if(minDelay != flight.getDelay()) System.exit(1);
                            relaxedDelay += (flight.getDelay() - minDelay);
                            flight.setDelay(minDelay);
                        }
                    }
                }
            }

        }

        return relaxedDelay;
    }
}
