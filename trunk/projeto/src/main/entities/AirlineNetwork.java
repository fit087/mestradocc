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

    private ArrayList<Flight> flights = new ArrayList<Flight>();
    
    private ArrayList<City> cities = new ArrayList<City>();

    private AirlineGraphicConfigs airlineGraphicConfigs = AirlineGraphicConfigs.defaultGraphicConfig;

    public ArrayList<Rail> getBestNetwork() {
        return bestNetwork;
    }

    public void setBestNetwork(ArrayList<Rail> bestNetwork) {
        this.bestNetwork = bestNetwork;
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
     * Obtem o menor tempo de inicio de todos os trilhos.
     * @return
     */
    public Integer getLowTime(){
        Integer lowTime = this.bestNetwork.get(0).getBeginTime();


        for (int i = 1; i < bestNetwork.size(); i++) {
            if(bestNetwork.get(i).getBeginTime() < lowTime){
                lowTime = bestNetwork.get(i).getBeginTime();
            }
        }

        return lowTime;
    }

    /**
     * Obtem o maior tempo de pouso de todos os trilhos.
     * @return
     */
    public Integer getHighTime(){
        Integer highTime = this.bestNetwork.get(0).getEndTime();

        for (int i = 1; i < bestNetwork.size(); i++) {
            if(bestNetwork.get(i).getEndTime() > highTime){
                highTime = bestNetwork.get(i).getEndTime();
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

}
