/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.entities;

import java.util.ArrayList;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class AirlineNetwork {
    
    private ArrayList< ArrayList<Flight> > bestNetwork = new ArrayList<ArrayList<Flight>>();

    private ArrayList<Flight> flights = new ArrayList<Flight>();
    
    private ArrayList<City> cities = new ArrayList<City>();

    public ArrayList<ArrayList<Flight>> getBestNetwork() {
        return bestNetwork;
    }

    public void setBestNetwork(ArrayList<ArrayList<Flight>> bestNetwork) {
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

    public void setFlights(ArrayList<Flight> flights) {
        this.flights = flights;
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
