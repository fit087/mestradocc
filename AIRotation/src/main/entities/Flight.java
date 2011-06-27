/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.entities;

import util.TimeUtil;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class Flight implements Comparable<Flight> {

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
    private Integer number;
    //Numero do trilho
    private Integer trackNumber;
    //Atraso no voo
    private Integer delay;
    //É um voo de reposicionamento.
    private Boolean reposition;
    //Voo selecionado
    private Boolean selected;
    //Voo excluido
    private Boolean excluded;

    public Flight(String name, Integer number, City departureCity, City arrivalCity, Integer departureTime, Integer arrivalTime) {
        this.name = name;
        this.number = number;
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.trackNumber = -1;
        this.delay = 0;
        this.reposition = false;
        this.selected = false;
        this.excluded = false;
    }

    public Flight(){
        name = "NEW";
        number = -1;
        departureCity = null;
        arrivalCity = null;
        departureTime = 0;
        arrivalTime = 0;
        trackNumber = -1;
        delay = 0;
        reposition = false;
        selected = false;
        excluded = false;
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
        return Math.abs(delay);
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

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    /**
     * Obtem o tempo de voo
     * @return
     */
    public int getFlightTime() {
        return getArrivalTime() - getDepartureTime();
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
    public int getGroundTime() {
        return getDepartureCity().getGroundTime();
    }

    public Boolean getReposition() {
        return reposition;
    }

    public void setReposition(Boolean reposition) {
        this.reposition = reposition;
    }

    @Override
    public String toString() {
//        System.out.println("departureCity " + departureCity);
//        System.out.println("arrivalCity " + arrivalCity);
//        System.out.println("Name " + name);
//        System.out.println("Number " + number);
//        System.out.println("DepartureTime " + departureTime);
//        System.out.println("arrivalTime " + arrivalTime);

        String r = String.format("%-6s", getName()) + " " + TimeUtil.getFormatedTime(getDepartureTime()) + " " + TimeUtil.getFormatedTime(getArrivalTime()) + " " + departureCity.getName() + " " + arrivalCity.getName();
//        String r = String.format("%5s[%3d] | %4s (%4d) ->  %4s (%4d)", name, number, departureCity.getName(), departureTime, arrivalCity.getName(), arrivalTime);

//        if (getDelay() != 0) {
//            r += String.format("(%+d)", getDelay());
//        }

        //r+= " GROUND: " + getGroundTime() + " | " + getRealDepartureTime() + " " + getRealArrivalTime();
        return r;

        // return "Id: " + id + " Number:" + flightNumber + " Name:" + flightName + " Origem: " + departureCity.getName() + " Inicio: " + departureTime + " Destino: " + arrivalCity.getName() + " Fim: " + arrivalTime;
    }

    protected Flight clonedInstance() {
        return new Flight(name, number, departureCity, arrivalCity, departureTime, arrivalTime);
    }

    public int compareTo(Flight o) {
        return getCost() - o.getCost();
    }

    public void configure(Flight flight) {
        this.name = flight.getName();
        this.departureCity = flight.getDepartureCity();
        this.arrivalCity = flight.getArrivalCity();
        this.departureTime = flight.getDepartureTime();
        this.arrivalTime = flight.getArrivalTime();
        this.delay = flight.getDelay();
        this.trackNumber = flight.getTrackNumber();
        this.reposition = flight.getReposition();
    }

    public Integer getDuration() {
        return getArrivalTime() - getDepartureTime();
    }
}
