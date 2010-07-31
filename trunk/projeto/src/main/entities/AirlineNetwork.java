/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.entities;

import graphic.AirlineGraphicConfigs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class AirlineNetwork {
    
    private ArrayList< Rail > bestNetwork = new ArrayList< Rail >();

    private int bestNetworkCost = Integer.MAX_VALUE;

    private ArrayList<Flight> flights = new ArrayList<Flight>();
    
    private ArrayList<City> cities = new ArrayList<City>();

    private AirlineGraphicConfigs airlineGraphicConfigs = AirlineGraphicConfigs.defaultGraphicConfig;

    public ArrayList<Rail> getBestNetwork() {
        return bestNetwork;
    }

    public void setBestNetwork(ArrayList<Rail> bestNetwork, int cost) {
        this.bestNetwork = bestNetwork;
        this.bestNetworkCost = cost;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public AirlineGraphicConfigs getAirlineGraphicConfigs() {
        return airlineGraphicConfigs;
    }

    public void setAirlineGraphicConfigs(AirlineGraphicConfigs airlineGraphicConfigs) {
        this.airlineGraphicConfigs = airlineGraphicConfigs;
    }

    public void setFlights(ArrayList<Flight> flights) {
        //Ordena em relação ao tempo de partida.
        Collections.sort(flights, new Comparator<Flight>() {
            public int compare(Flight o1, Flight o2) {
                return o1.getDepartureTime() - o2.getDepartureTime();
            }
        });
        
        this.flights = flights;
    }

    /**
     * Obtem o menor tempo de inicio de todos os voos
     * @return
     */
    public Integer getLowTime(){
        Integer lowTime = this.flights.get(0).getDepartureTime();

        for (int i = 0; i < flights.size(); i++) {
            if(flights.get(i).getDepartureTime() < lowTime){
                lowTime = flights.get(i).getDepartureTime();
            }
        }

        return lowTime;
    }

    /**
     * Obtem o maior tempo de pouso de todos os voos.
     * @return
     */
    public Integer getHighTime(){
        Integer highTime = this.flights.get(0).getArrivalTime();


        for (int i = 0; i < flights.size(); i++) {
            if(flights.get(i).getArrivalTime() > highTime){
                highTime = flights.get(i).getArrivalTime();
            }
        }

        return highTime;
    }

    public void showBestNetwork(){
        for (Rail rail : bestNetwork) {
            rail.show();
        }
    }

    /**
     * Obtem a cidade apartir do seu minemonico.
     * @param cityName
     * @return A cidade ou null caso ela não seja encontrada.
     */
    public City getCity(String cityName) {
        for (City city : cities) {
            if(city.getName().equals(cityName)) return city;
        }

        return null;
    }

    public int railsSize() {
        if(bestNetwork.isEmpty()) return Integer.MAX_VALUE;
        else return bestNetwork.size();
    }

    public ArrayList<Flight> getFlightsClone() {
        ArrayList<Flight> cloneFlights = new ArrayList<Flight>();

        for (Flight flight : flights) {
            cloneFlights.add(flight.clonedInstance());
        }

        return cloneFlights;
    }

    public boolean validadeSolution(){
        for (Rail rail : bestNetwork) {
            for(int i = 1; i < rail.getFlights().size(); i++){
                Flight first = rail.getFlights().get(i-1);
                Flight second = rail.getFlights().get(i);

                if(first.getRealArrivalTime() > (second.getRealDepartureTime() - second.getGroundTime())){
                    System.out.println("i " + i + " Delay " + first.getDelay() + " " + second.getDelay());
                    System.out.println(">>>>> " + first.getRealArrivalTime() + " " + (second.getRealDepartureTime() - second.getGroundTime()));
                    return false;
                }
            }
        }

        return true;
    }

    public Flight getFlight(String railName, long time) {
        long basetime = getAirlineGraphicConfigs().getBaseTime();
        time -= basetime;
        time/=60*1000;
        
        for (Rail rail : bestNetwork) {
            if(rail.getName().equals(railName)){
                for (Flight flight : rail.getFlights()) {
                    if(((flight.getRealDepartureTime() - flight.getGroundTime()) <= time)
                            && (flight.getRealArrivalTime() >= time)){
                        return flight;
                    }
                }
            }
        }

        return null;
    }

    public int getBestNetworkCost() {
        return bestNetworkCost;
    }

    /**
     * Calcula o custo do trilho passado no parametro.
     * Custo = Somatorio(atrasos) + NumeroDeTrilhos*1000;
     * @param network
     * @return
     */
    public static int getTotalCost(ArrayList<Rail> network){
        int totalcost = network.size()*1000;

        for (Rail rail : network) {
            totalcost += rail.getCost();
        }

        return totalcost;
    }

}
