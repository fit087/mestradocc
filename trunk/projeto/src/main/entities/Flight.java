/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.entities;

import java.io.Serializable;
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
public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Nome do voo
    private String flightName;

    //Cidade de saida
    private City departureCity;

    //Cidade de chegada
    private City arrivalCity;

    //Horario de saida
    private Integer departureTime;

    //Horario de chegada
    private Integer arrivalTime;

    //Numero do voo
    @Transient
    private Integer flightNumber;

    //Numero do trilho
    @Transient
    private Integer railNumber;

    //Atraso no voo
    @Transient
    private Integer flightDelay;

    //Possivel atraso do voo
    @Transient
    private Integer possibleFlightDelay;

    //Custo do voo
    @Transient
    private Integer cost;

    //Proximo voo.
    @Transient
    private Flight nextFlight;

    //Voo anterior
    @Transient
    private Flight previousFlight;

    //Voo selecionado
    @Transient
    private Boolean selected;

    //Voo excluido
    @Transient
    private Boolean excluded;

    public Flight() {
    }

    public Flight(String flightName, Integer flightNumber, City departureCity, City arrivalCity, Integer departureTime, Integer arrivalTime) {
        this.flightName = flightName;
        this.flightNumber = flightNumber;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public City getArrivalCity() {
        return arrivalCity;
    }

    public void setArrivalCity(City arrivalCity) {
        this.arrivalCity = arrivalCity;
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Integer arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public City getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(City departureCity) {
        this.departureCity = departureCity;
    }

    public Integer getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Integer departureTime) {
        this.departureTime = departureTime;
    }

    public Boolean getExcluded() {
        return excluded;
    }

    public void setExcluded(Boolean excluded) {
        this.excluded = excluded;
    }

    public Integer getFlightDelay() {
        return flightDelay;
    }

    public void setFlightDelay(Integer flightDelay) {
        this.flightDelay = flightDelay;
    }

    public Integer getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(Integer flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Flight getNextFlight() {
        return nextFlight;
    }

    public void setNextFlight(Flight nextFlight) {
        this.nextFlight = nextFlight;
    }

    public Integer getPossibleFlightDelay() {
        return possibleFlightDelay;
    }

    public void setPossibleFlightDelay(Integer possibleFlightDelay) {
        this.possibleFlightDelay = possibleFlightDelay;
    }

    public Flight getPreviousFlight() {
        return previousFlight;
    }

    public void setPreviousFlight(Flight previousFlight) {
        this.previousFlight = previousFlight;
    }

    public Integer getRailNumber() {
        return railNumber;
    }

    public void setRailNumber(Integer railNumber) {
        this.railNumber = railNumber;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Flight)) {
            return false;
        }
        Flight other = (Flight) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Id: " + id + " Number:" + flightNumber + " Name:" + flightName + " Origem: " + departureCity.getName() + " Duracao: " + departureTime + " Destino: " + arrivalCity.getName() + " Duracao: " + arrivalTime;
    }

}
