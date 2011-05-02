/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import main.entities.AirlineNetwork;
import main.entities.City;
import main.entities.Flight;
import main.entities.PieceOfFlight;

/**
 *
 * @author alexanderdealmeidapinto
 */
public class InstanceUtil {


    public static void generatePieces(AirlineNetwork airlineNetwork) {
        System.out.println("Entrou em generatePieces() ");
        ArrayList<City> cities = airlineNetwork.getCities();

        ArrayList<PieceOfFlight> singleFlights = new ArrayList<PieceOfFlight>();

        for (int i = 0; i < cities.size() - 1; i++) {
            City city1 = cities.get(i);
            for (int j = (i + 1); j < cities.size(); j++) {
                City city2 = cities.get(j);


                if (city1.getFlightTimes().get(city2) == 99999) {
                    continue;
                }

                PieceOfFlight single1 = new PieceOfFlight();
                PieceOfFlight single2 = new PieceOfFlight();
                //PieceOfFlight single3 = new PieceOfFlight();

                single1.addPseudoFlight(city1, city2, city1.getFlightTimes().get(city2));
                single2.addPseudoFlight(city2, city1, city1.getFlightTimes().get(city2));
                //single3.addPseudoFlight(city2, city1, city1.getFlightTimes().get(city2) + 30);

                singleFlights.add(single1);
                singleFlights.add(single2);
                //singleFlights.add(single3);
                //System.out.println("Cities [" + city1.getName() + " -> " + city2.getName() + " = " + city1.getFlightTimes().get(city2) + "]" );
            }
        }

        System.out.println("Single Flights = " + singleFlights.size());

        int cont = 0;
        for (PieceOfFlight pieceOfFlight : singleFlights) {
            System.out.println("Index " + cont);
            cont++;
            System.out.println(pieceOfFlight.toString());
        }

        HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>> journeys = new HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>>();

        updateJourney(journeys, cloneList(singleFlights));

        int maxSize = 5;
        ArrayList<PieceOfFlight> currentFlights = cloneList(singleFlights);

        for (int i = 0; i < maxSize; i++) {

            long init = System.currentTimeMillis();
            currentFlights = combinateList(currentFlights, singleFlights);
            System.out.println("Current " + currentFlights.size() + " teste " + currentFlights.get(0).size());
            updateJourney(journeys, cloneList(currentFlights));

            int time = (int) ((System.currentTimeMillis() - init) / 1000);

            System.out.println("Step "+ (i+1) + " Duracao " + time);
        }

        System.out.println("Finalizado a geração");
    }

    private static void updateJourney(HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>> journeys, ArrayList<PieceOfFlight> newPieces) {
        for (int i = 0; i < newPieces.size(); i++) {
            PieceOfFlight pieceOfFlight = newPieces.get(i);
            City departure = pieceOfFlight.getDepartureCity();
            City arrival = pieceOfFlight.getArrivalCity();


            HashMap<City, ArrayList<PieceOfFlight>> journeyWithDeparture = journeys.get(departure);
            if (journeyWithDeparture == null) { //Se nao tiver adicione
                journeys.put(departure, new HashMap<City, ArrayList<PieceOfFlight>>());
                journeyWithDeparture = journeys.get(departure);
            }

            ArrayList<PieceOfFlight> pieces = journeyWithDeparture.get(arrival);

            if (pieces == null) { //Se nao tiver adicione
                journeyWithDeparture.put(arrival, new ArrayList<PieceOfFlight>());
                pieces = journeyWithDeparture.get(arrival);
            }

            pieces.add(pieceOfFlight);

            Collections.sort(pieces);

//
//            if (pieces.size() > 1) {
//                System.out.println(" ############  ");
//                System.out.println(" ############  ");
//
//                System.out.println("Piece Size " + pieces.size());
//
//
//                for (PieceOfFlight pieceOfFlight1 : pieces) {
//                    System.out.println("--------");
//                    for (PieceOfFlight.PseudoFlight pseudoFlight : pieceOfFlight1.getFlights()) {
//                        System.out.println("Teste " + pseudoFlight.toString());
//                    }
//                    System.out.println("--------");
//                }
//            }

            
        }
    }

    private static ArrayList<PieceOfFlight> combinateList(ArrayList<PieceOfFlight> target, ArrayList<PieceOfFlight> unit) {

        ArrayList<PieceOfFlight> result = new ArrayList<PieceOfFlight>();

        for (int i = 0; i < target.size(); i++) {
            PieceOfFlight pieceOfFlight = target.get(i);
            for (int j = 0; j < unit.size(); j++) {
                PieceOfFlight dest = unit.get(j);
                if (pieceOfFlight.isPossibleArriveBefore(dest)) {
                    PieceOfFlight newPiece = pieceOfFlight.clone();
                    newPiece.addAllFlight(dest);
                    result.add(newPiece);
                }
            }
        }

        return result;
    }

    private static ArrayList<PieceOfFlight> cloneList(ArrayList<PieceOfFlight> list) {
        ArrayList<PieceOfFlight> clone = new ArrayList<PieceOfFlight>(list.size());
        for (PieceOfFlight item : list) {
            clone.add(item.clone());
        }
        return clone;
    }
}
