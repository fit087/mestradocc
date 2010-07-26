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

    /**
     * Identifica se o actualFlight tem ligação direta com o voo candidato.
     * Uma ligação direta acontece quando a cidade de chegada do voo atual é
     * igual a cidade de partida do voo candidato.
     * @param actualFlight
     * @param candidate
     * @return
     */
    public static boolean isDirectFlight(Flight actualFlight, Flight candidate) {
        if (actualFlight.getArrivalCity().getName().equals(candidate.getDepartureCity().getName())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Indica se tem tempo para fazer essa sequencia de voos dado um atraso maximo.
     * @param actualFlight
     * @param candidate
     * @param maxDelay
     * @return
     */
    public static boolean hasTime(Flight actualFlight, Flight candidate, int maxDelay) {
        if (actualFlight.getRealArrivalTime() <= candidate.getDepartureTime() - candidate.getDepartureCity().getGroundTime()) {
            return true;
        } else {
            return false;
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //Nome do voo
    private String name;
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
    private Integer number;
    //Numero do trilho
    @Transient
    private Integer railNumber;
    //Atraso no voo
    @Transient
    private Integer delay;
    //Possivel atraso do voo
    @Transient
    private Integer possibleDelay;
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

    public Flight(String name, Integer number, City departureCity, City arrivalCity, Integer departureTime, Integer arrivalTime) {
        this.name = name;
        this.number = number;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.railNumber = -1;
        this.delay = 0;
        this.possibleDelay = 0;
        this.cost = 0;
        this.previousFlight = null;
        this.nextFlight = null;
        this.selected = false;
        this.excluded = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Flight getNextFlight() {
        return nextFlight;
    }

    public void setNextFlight(Flight nextFlight) {
        this.nextFlight = nextFlight;
    }

    public Integer getPossibleDelay() {
        return possibleDelay;
    }

    public void setPossibleDelay(Integer possibleDelay) {
        this.possibleDelay = possibleDelay;
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

    /**
     * Obtem o horario de saida acrescido de atrasos.
     * @return
     */
    public int getRealDepartureTime() {
        return getDepartureTime() + getDelay();
    }

    /**
     * Obtem o horario de pouso acrescido de atrasos.
     * @return
     */
    public int getRealArrivalTime() {
        return getArrivalTime() + getDelay();
    }

    /**
     * Obtem o tempo de solo desse voo.
     * @return
     */
    public int getGroundTime(){
        return getDepartureCity().getGroundTime();
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
        String r = String.format("%5s[%3d] | %4s (%4d) ->  %4s (%4d)", name, number, departureCity.getName(), departureTime, arrivalCity.getName(), arrivalTime);
        r += " [ + " + delay + " ]";
        r += "\n";
        return r;

        // return "Id: " + id + " Number:" + flightNumber + " Name:" + flightName + " Origem: " + departureCity.getName() + " Inicio: " + departureTime + " Destino: " + arrivalCity.getName() + " Fim: " + arrivalTime;
    }

}
