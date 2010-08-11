/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.entities;

import graphic.ARPGraphicConfigs;
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


    private String pathInstance;
    
    private ArrayList< Track > bestNetwork = new ArrayList< Track >();

    private int bestNetworkCost = Integer.MAX_VALUE;

    private ArrayList<Flight> flights = new ArrayList<Flight>();
    
    private ArrayList<City> cities = new ArrayList<City>();

    private ARPGraphicConfigs airlineGraphicConfigs = ARPGraphicConfigs.defaultGraphicConfig;

    public AirlineNetwork(String pathInstance) {
        this.pathInstance = pathInstance;
    }

    public String getPathInstance() {
        return pathInstance;
    }

    public void setPathInstance(String pathInstance) {
        this.pathInstance = pathInstance;
    }

    public ArrayList<Track> getBestNetwork() {
        return bestNetwork;
    }

    public void setBestNetwork(ArrayList<Track> bestNetwork, int cost) {
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

    public ARPGraphicConfigs getAirlineGraphicConfigs() {
        return airlineGraphicConfigs;
    }

    public void setAirlineGraphicConfigs(ARPGraphicConfigs airlineGraphicConfigs) {
        this.airlineGraphicConfigs = airlineGraphicConfigs;
    }

    public Integer getNumberOfRepo() {

        int nFlights = 0;

        for (Track track : bestNetwork) {
            nFlights += track.numberOfFlights();
        }

        return nFlights - flights.size();
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

    /**
     * Mostra na saida padrão, todos os trilhos que compoem
     * o melhor resultado obtido.
     */
    public void showBestNetwork(){
        for (Track track : bestNetwork) {
            track.show();
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

    /**
     * Numero de trilho que compoem o melhor resultado composto.
     * @return
     */
    public int numberOfTracks() {
        if(bestNetwork.isEmpty()) return Integer.MAX_VALUE;
        else return bestNetwork.size();
    }

    /**
     * Obtem um clone dos voos da companhia aérea indicada.
     * @return
     */
    public ArrayList<Flight> getFlightsClone() {
        ArrayList<Flight> cloneFlights = new ArrayList<Flight>();

        for (Flight flight : flights) {
            cloneFlights.add(flight.clonedInstance());
        }

        return cloneFlights;
    }

    public boolean validadeSolution(){
        for (Track track : bestNetwork) {
            for(int i = 1; i < track.getFlights().size(); i++){
                Flight first = track.getFlights().get(i-1);
                Flight second = track.getFlights().get(i);

                if(!first.getArrivalCity().getName().equals(second.getDepartureCity().getName())){
                    System.out.println("Cidades diferentes");
                    return false;
                }

                if(first.getRealArrivalTime() > (second.getRealDepartureTime() - second.getGroundTime())){
                    System.out.println("Sobreposição de tempos");
                    System.out.println("i " + i + " Delay " + first.getDelay() + " " + second.getDelay());
                    System.out.println(">>>>> " + first.getRealArrivalTime() + " " + (second.getRealDepartureTime() - second.getGroundTime()));
                    return false;
                }
            }
        }

        return true;
    }

    public Flight getFlight(String trackName, long time) {
        long basetime = getAirlineGraphicConfigs().getBaseTime();
        time -= basetime;
        time/=60*1000;
        
        for (Track track : bestNetwork) {
            if(track.getName().equals(trackName)){
                for (Flight flight : track.getFlights()) {
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
    public static int getTotalCost(ArrayList<Track> network){
        int totalcost = 0;

        for (Track track : network) {
            totalcost += track.getCost();

        }

        return totalcost;
    }

    public int getTracksInACity(String city){
        int acumulador = 0;

        for (Track track : bestNetwork) {
            if(track.coverCity(city)) acumulador++;
        }

        return acumulador;
    }

}
