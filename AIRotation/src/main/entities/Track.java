/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.entities;

import java.util.ArrayList;
import java.util.List;
import util.param.IntegerParam;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class Track {

    private int number;
    private ArrayList<Flight> flights = new ArrayList<Flight>();

    /**
     * Cria um novo trilho vazio.
     * @param number
     */
    public Track(int number) {
        this.number = number;
    }

    /**
     * Obtem o nome do voo, que é baseado no número do trilho.
     *
     * Nome do voo = Voo[numero+1]
     * @return
     */
    public String getName() {
        return "Voo[" + (number + 1) + "]";
    }

    /**
     * Obtém o numero de idenficação do trilho.
     * @return
     */
    public int getNumber() {
        return number;
    }

    /**
     * Isere o numero de identificação do trilho.
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Obtem o custo do trilho.
     * @return
     */
    public int getCost() {
        int cost = 1000;

//        if (flights.size() == 1) {
//            cost = 500;
//        }

        for (Flight flight : flights) {

            /*
             * Se um reposicionamento não é ruim para a companhia aerea
             * ( desde que ele reduza o numero de atrasos nos voos regulares )
             * então não tem sentido penaliza-lo.
             */
            if (flight.getReposition()) {
                cost += flight.getFlightTime() + flight.getGroundTime();
            }

            cost += flight.getCost();
        }

        return cost;
    }

    /**
     * Obtem os voos associados ao trilho.
     * @return
     */
    public ArrayList<Flight> getFlights() {
        return flights;
    }

    /**
     * Adiciona um voo ao trilho.
     * @param flight
     */
    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }
    
    public void addAllFlight(List<Flight> newFlights){
        this.flights.addAll(newFlights);
    }

    /**
     * Obtem o horario de inicio desse trilho ( leva em consideração o tempo de solo )
     * @return
     */
    public Integer getBeginTime() {
        Flight flight = flights.get(0);
        return flight.getRealDepartureTime() - flight.getGroundTime();
    }

    /**
     * Obtem o horario de termino desse trilho.
     * @return
     */
    public Integer getEndTime() {
        Flight flight = flights.get(flights.size() - 1);
        return flight.getRealArrivalTime();
    }
    
    public Flight getFirstFlight(){
        return flights.get(0);
    } 
    
    public Flight getLastFlight(){
        return flights.get(flights.size() - 1);
    }

    /**
     * Retorna o numero de voos que compoem esse trilho.
     * @return
     */
    public int numberOfFlights() {
        return flights.size();
    }

    /**
     * Obtem o voo indicado pelo indice.
     * @param i
     * @return
     */
    public Flight getFlight(int i) {
        return flights.get(i);
    }

    /**
     * Identifica se uma dada cidade é coberta por esse trilho.
     * @param city
     * @return
     */
    public boolean coverCity(String city) {
        for (Flight flight : flights) {
            if (flight.getArrivalCity().getName().equals(city) || flight.getDepartureCity().getName().equals(city)) {
                return true;
            }
        }

        return false;
    }

    public int getTotalDelay() {
        int totaldelay = 0;

        for (Flight flight : flights) {
            totaldelay += Math.abs(flight.getDelay());
        }

        return totaldelay;
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

    public int getMaxDelay() {
        int maxdelay = 0;

        for (Flight flight : flights) {
            if (flight.getDelay() > maxdelay) {
                maxdelay = flight.getDelay();
            }
        }

        return maxdelay;
    }

    public int getNumberOfDelayedFlights() {
        int numberOfDelayedFlights = 0;

        for (Flight flight : flights) {
            if (flight.getDelay() != 0) {
                numberOfDelayedFlights++;
            }
        }

        return numberOfDelayedFlights;
    }

    public int getNumberOfRepositions() {
        int numberOfRepositions = 0;

        for (Flight flight : flights) {
            if (flight.getName().startsWith("REPO")) {
                numberOfRepositions++;
            }
        }

        return numberOfRepositions;
    }
    
    /**
     * Obtem o número de voos consecultivos, de init para tras,
     * em que não é possível fazer a troca de tripulação
     * @param init voo inicial a ser avaliado
     * @return 
     */
    public void getNumberOfCrewFlightBehind(int init, IntegerParam numberOfCrewFlights, IntegerParam timeOfCrewFlights ) {
        
        int nOfCrewFlightsValue = 1;
        int timeOfCrewFlightsValue = 0;
        
        for(int i = init-1; i >= 0; i--){
            Flight first = flights.get(i+1);
            Flight second = flights.get(i);
            
            timeOfCrewFlightsValue += first.getDuration();
            
            if(second.hasTimeToChangeCrew(first)){
                break;
            }
            else{
                nOfCrewFlightsValue++;
            }
        }
        
        timeOfCrewFlights.setValue(timeOfCrewFlightsValue);
        numberOfCrewFlights.setValue(nOfCrewFlightsValue);
    }

    /**
     * Obtem o número de voos consecultivos, de init para frente,
     * em que não é possível fazer a troca de tripulação
     * @param init voo inicial a ser avaliado
     * @return 
     */
    public void getNumberOfCrewFlightAhead(int init, IntegerParam numberOfCrewFlights, IntegerParam timeOfCrewFlights ) {
        
        int nOfCrewFlightsValue = 1;
        int timeOfCrewFlightsValue = 0;
        
        for(int i = init+1; i < flights.size(); i++){
            Flight first = flights.get(i-1);
            Flight second = flights.get(i);
            
            timeOfCrewFlightsValue += first.getDuration();
            
            if(first.hasTimeToChangeCrew(second)){
                break;
            }
            else{
                
                nOfCrewFlightsValue++;
            }
        }
        
        timeOfCrewFlights.setValue(timeOfCrewFlightsValue);
        numberOfCrewFlights.setValue(nOfCrewFlightsValue);
    }
}
