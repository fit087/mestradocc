/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
@Entity
public class City implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Nome da cidade.
    private String name;

    //Tempo de solo
    private Integer groundTime;

    //Tempo de voo entre as cidades.
    @Transient
    private HashMap<City, Integer> flightTimes = new HashMap<City, Integer>();

    public City() {
    }

    public City(String name, Integer groundTime) {
        this.name = name;
        this.groundTime = groundTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        
        if(other.getName().equals(getName())) {
            return true;
        }
        else {
            return true;
        }
    }

    @Override
    public String toString() {
        return "ID: " + id + " Nome: " + name + " GroundTime: " + groundTime;
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
