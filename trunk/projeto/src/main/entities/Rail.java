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
public class Rail {

    private int number;

    private ArrayList<Flight> flights = new ArrayList<Flight>();

    public Rail(int number) {
        this.number = number;
    }

    public String getName(){
        return "Voo[" + (number+1) + "]";
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCost() {
        int cost = 0;

        for (Flight flight : flights) {
            cost += flight.getCost();
        }

        return cost;
    }

    public ArrayList<Flight> getFlights() {
        return flights;
    }

    public void setFlights(ArrayList<Flight> flights) {
        this.flights = flights;
        calculateCost();
    }

    /**
     * Calcula o custo do trilho associado.
     */
    public void calculateCost() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Mostra o trilho na saida padrão.
     */
    public void show() {
        System.out.printf("Trilho %d:\n", number);

        for (Flight flight : flights) {
            System.out.println("\t" + flight.toString());
        }

        System.out.println("--------------------------------------------\n");
    }

    /**
     * Adiciona um voo ao trilho.
     * @param flight
     */
    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }

    /**
     * Obtem o tempo de inicio desse trilho ( leva em consideração o tempo de solo )
     * @return
     */
    public Integer getBeginTime() {
        Flight flight = flights.get(0);
        return flight.getRealDepartureTime() - flight.getGroundTime();
    }

    /**
     * Obtem o tempo de termino desse trilho.
     * @return
     */
    public Integer getEndTime() {
        Flight flight = flights.get(flights.size() - 1);
        return flight.getRealArrivalTime();
    }

    
}
