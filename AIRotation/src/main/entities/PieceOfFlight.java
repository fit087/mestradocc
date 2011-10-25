/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.entities;

import java.util.ArrayList;

/**
 *  Todos os pedaços de voos devem partir e chegar no mesmo local.
 */
public class PieceOfFlight implements Comparable<PieceOfFlight> {

    private ArrayList<PseudoFlight> flights = new ArrayList<PseudoFlight>();

    public ArrayList<PseudoFlight> getFlights() {
        return flights;
    }

    public void setFlights(ArrayList<PseudoFlight> flights) {
        this.flights = flights;
    }

    public void addPseudoFlight(City departureCity, City arrivalCity, Integer duration){
        flights.add(new PseudoFlight(departureCity, arrivalCity, duration));
    }

    private void addPseudoFlight(PseudoFlight pseudoFlight){
        flights.add(pseudoFlight);
    }

    public City getDepartureCity(){
        return flights.get(0).getDepartureCity();
    }
    
    public City getArrivalCity(){
        return flights.get(flights.size() - 1).getArrivalCity();
    }
    
    public int size(){
        return flights.size();
    }

    public boolean isPossibleArriveBefore(PieceOfFlight pieceOfFlight){
        return getArrivalCity().getName().equals(pieceOfFlight.getDepartureCity().getName());
    }

    public int getTotalDuration(){
        int duration = 0;

        for (int i = 0; i < flights.size(); i++) {
            duration += flights.get(i).getTotalDuration();
        }


        return duration;
    }

    @Override
    public PieceOfFlight clone(){
        PieceOfFlight clonePieceOfFlight = new PieceOfFlight();

        for (PseudoFlight pseudoFlight : flights) {
            clonePieceOfFlight.addPseudoFlight(pseudoFlight); //Não precisa clonar o pseudoFlight.
        }
        
        return clonePieceOfFlight;
    }

    @Override
    public String toString() {
        String s = ""
                + "PseudoFlight ----- \n";

        for (PseudoFlight pseudoFlight : flights) {
            s += "\t" + pseudoFlight.toString() + "\n";
        }

        return s;
    }

    public int compareTo(PieceOfFlight t) {
        return t.getTotalDuration() - getTotalDuration();
    }

    public void addAllFlight(PieceOfFlight dest) {
        for (PseudoFlight pseudoFlight : dest.getFlights()) {
            addPseudoFlight(pseudoFlight);
        }
    }

    public int getTotalFlightTime() {
        int totalFlightTime = 0;
        
        for (PseudoFlight pseudoFlight : flights) {
            totalFlightTime += pseudoFlight.getDuration();
        }
        
        return totalFlightTime;
    }



    public class PseudoFlight {

        private City departureCity;
        private City arrivalCity;
        private Integer duration;

        public PseudoFlight(City departureCity, City arrivalCity, Integer duration) {
            this.departureCity = departureCity;
            this.arrivalCity = arrivalCity;
            this.duration = duration;
        }

        public City getArrivalCity() {
            return arrivalCity;
        }

        public void setArrivalCity(City arrivalCity) {
            this.arrivalCity = arrivalCity;
        }

        public City getDepartureCity() {
            return departureCity;
        }

        public void setDepartureCity(City departureCity) {
            this.departureCity = departureCity;
        }

        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        public int getTotalDuration(){
            return getDepartureCity().getGroundTime() + getDuration();
        }

        @Override
        public String toString() {
            return departureCity.getName() + " " + duration + " " + arrivalCity.getName();
        }

        public Integer getGroundTime() {
            return getDepartureCity().getGroundTime();
        }
    }
}
