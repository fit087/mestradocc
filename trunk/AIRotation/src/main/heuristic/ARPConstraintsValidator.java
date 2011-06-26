/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.heuristic;

import main.entities.AirlineNetwork;
import main.entities.Flight;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class ARPConstraintsValidator {

    /**
     * Verifica se os voos especificados respeitam as restrições geográficas.
     * @param firstFlight
     * @param followFlight
     * @return
     */
    public static boolean validateGeographicalConstraint(Flight firstFlight, Flight followFlight) {
//        System.out.println("First " + firstFlight + " Second " + followFlight);
        return firstFlight.getArrivalCity().getName().equals(followFlight.getDepartureCity().getName());
    }

    /**
     * Valida se os voos especificados respeitam as restrições temporais (nao leva em consideracao atrasos)
     * @param firstFlight
     * @param followFlight
     * @return .
     */
    public static boolean validateTemporalConstraintWithoutDelay(Flight firstFlight, Flight followFlight) {
        if (firstFlight.getRealArrivalTime() <= followFlight.getDepartureTime() - followFlight.getGroundTime()) {
            return true;
        } else {
            return false;
        }
    }

    public static Flight generateRepoFlight(Flight lastFlight, Flight actualFlight, AirlineNetwork airlineNetwork) {
        Flight repoFlight = new Flight();
        repoFlight.setName("REPO");
        repoFlight.setDepartureCity(lastFlight.getArrivalCity());
        repoFlight.setArrivalCity(actualFlight.getDepartureCity());
        repoFlight.setDepartureTime(lastFlight.getRealArrivalTime() + repoFlight.getGroundTime() );
        repoFlight.setArrivalTime(repoFlight.getDepartureTime() + repoFlight.getDepartureCity().getFlightTimes().get(repoFlight.getArrivalCity()));
        repoFlight.setReposition(true);


        return repoFlight;

    }
    
}
