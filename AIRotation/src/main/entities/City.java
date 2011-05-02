/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.entities;

import java.util.HashMap;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class City  {

    //Nome da cidade.
    private String name;

    //Tempo de solo
    private Integer groundTime;

    //Tempo de voo entre as cidades.
    private HashMap<City, Integer> flightTimes = new HashMap<City, Integer>();

    public City() {
    }

    public City(String name, Integer groundTime) {
        this.name = name;
        this.groundTime = groundTime;
    }

    public HashMap<City, Integer> getFlightTimes() {
        return flightTimes;
    }

    public void setFlightTimes(HashMap<City, Integer> flightTimes) {
        this.flightTimes = flightTimes;
    }

    public Integer getGroundTime() {
        return groundTime;
    }

    public void setGroundTime(Integer groundTime) {
        this.groundTime = groundTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof City)) {
            return false;
        }
        City other = (City) object;


        System.out.println("Teste " + other.getName() + " " + getName());

        if(other.getName().equals(getName())) {
            return true;
        }
        else {
            return true;
        }
    }

    @Override
    public String toString() {
        return "Nome: " + name + " GroundTime: " + groundTime;
    }


    /**
     * Adiciona o tempo de uma cidade apartir dessa.
     * @param city
     * @param time
     */
    public void addCityTime(City city, int time) {
        this.flightTimes.put(city, time);
    }

}
