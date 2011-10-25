/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.heuristic.advanced;

import graphic.ARPGraphicConfigs;
import java.util.ArrayList;
import java.util.List;
import main.entities.AirlineNetwork;
import main.entities.City;
import main.entities.Flight;
import main.entities.Track;

/**
 *
 * @author alexander
 */
public class ARPAdvanced {

    private static int oneDay = 24 * 60;

    public static List<AirlineNetwork> divideAirlinesPerDay(AirlineNetwork airlineNetwork, int end_of_day) {
        ArrayList<AirlineNetwork> result = new ArrayList<AirlineNetwork>();

        
        int current_day = 1;
        AirlineNetwork currentAirlineNetwork = new AirlineNetwork();

        for (Flight flight : airlineNetwork.getFlights()) {
            int current_end_of_day = end_of_day + (current_day * oneDay);

//            System.out.println("Current Day " + current_day);
//            System.out.println("FlightDeparture = " + flight.getDepartureTime());
//            System.out.println("Current_END_OF_DAY = " + current_end_of_day);

            if (flight.getDepartureTime() >= current_end_of_day) {
                current_day++;
                result.add(currentAirlineNetwork);
                currentAirlineNetwork = new AirlineNetwork();
            }

            currentAirlineNetwork.addFlight(flight);
        }

        result.add(currentAirlineNetwork);

        for (AirlineNetwork airlineNetworkdDaily : result) {
            airlineNetworkdDaily.setCities((ArrayList<City>) airlineNetwork.getCities().clone());
            airlineNetworkdDaily.setAirlineGraphicConfigs(airlineNetwork.getAirlineGraphicConfigs());
        }

        return result;
    }

    public static AirlineNetwork convertTracksToFlights(List<AirlineNetwork> airlineNetworks) {
        AirlineNetwork retorno = new AirlineNetwork();

        if (airlineNetworks.size() < 1) {
            System.err.println("AirLineNetwork deve conter pelo menos um dias");
            System.exit(1);
        }

        retorno.setCities(airlineNetworks.get(0).getCities());
        retorno.setAirlineGraphicConfigs(airlineNetworks.get(0).getAirlineGraphicConfigs());

        int number = 0;

        for (int i = 0; i < airlineNetworks.size(); i++) {
            AirlineNetwork airlineNetwork = airlineNetworks.get(i);
            for (int j = 0; j < airlineNetwork.getBestNetwork().size(); j++) {
                Track track = airlineNetwork.getBestNetwork().get(j);

                Flight first = track.getFirstFlight();
                Flight last = track.getLastFlight();

                String name = String.format("F_%d_%d", i, j);
                Flight dummyFlight = new Flight(name, number++, first.getDepartureCity(), last.getArrivalCity(), first.getDepartureTime(), last.getArrivalTime());
                retorno.addFlight(dummyFlight);


            }
        }



        return retorno;
    }

    public static AirlineNetwork recoverFlightsOfConvertedTracks(AirlineNetwork airlineNetwork, List<AirlineNetwork> list) {
        AirlineNetwork retorno = new AirlineNetwork();
        
        retorno.setCities(airlineNetwork.getCities());
        retorno.setAirlineGraphicConfigs(airlineNetwork.getAirlineGraphicConfigs());
        retorno.getBestNetwork().clear();

        int count_umount_track = 0;
        
        for (Track track : airlineNetwork.getBestNetwork()) {
            
            Track umount_track = new Track(count_umount_track++);
            
            for (Flight flight : track.getFlights()) { //Cada voo aqui e representado por um conjunto ( para isso ele precisa estar no formato F_%d_%d )
                String [] name_parts = flight.getName().split("_");
                
                if(name_parts.length != 3){
                    airlineNetwork.addFlight(flight);
                    umount_track.addFlight(flight);
                    continue;
                }
                
                int day = new Integer(name_parts[1]);
                int pos = new Integer(name_parts[2]);
                
                Track daily_track = list.get(day).getBestNetwork().get(pos);
                
                umount_track.addAllFlight(daily_track.getFlights());
                retorno.addAllFlight(daily_track.getFlights());
            }
            
            retorno.getBestNetwork().add(umount_track);
            
        }
        
        return retorno;
    }
}
