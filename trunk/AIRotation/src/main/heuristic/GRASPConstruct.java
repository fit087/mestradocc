/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.heuristic;

import database.entity.HeuristicInformation;
import database.entity.HeuristicInformationJpaController;
import gui.JFrameGraphicTest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.entities.AirlineNetwork;
import main.entities.Flight;
import main.entities.Rail;
import util.LogManager;
import util.RandomManager;

/**
 *
 * Responsável por construir uma malha de uma determinada frota,
 * especificando a quantidade minima "conseguida" para cobrir os seus voos,
 * o seu custo operacional e qual a sequência de vôos de cada aeronave.
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class GRASPConstruct {

    private Boolean resolved = false;
    private AirlineNetwork airlineNetwork;
    private GRASPParameters gRASPParameters;
    private ARPParameters aRPParameters;
    private Integer numberOfReposisions;

    /**
     * Constroi uma malha
     * @param airlineNetwork Dados do problema.
     * @param gRASPParameters Parametros do GRASPl
     * @param arpParameters Parametros do ARP (Aircraft Rotation Problem)
     */
    public GRASPConstruct(AirlineNetwork airlineNetwork, GRASPParameters gRASPParameters, ARPParameters arpParameters) {
        this.airlineNetwork = airlineNetwork;
        this.gRASPParameters = gRASPParameters;
        this.aRPParameters = arpParameters;
        this.resolved = false;
    }

    public void GRASPResolve() {

        System.out.println("Numero de voos " + airlineNetwork.getFlights().size());

        LogManager.writeMsg("Começou a resolver...");
        Long beginTime = System.currentTimeMillis();

        for (int i = 0; i < gRASPParameters.getNumberOfRepetitions(); i++) {
            int percentComplete = (100 * (i + 1)) / gRASPParameters.getNumberOfRepetitions();
            JFrameGraphicTest.setPercentComplete(percentComplete);

            for (int j = 0; j < gRASPParameters.getNumberOfConstructions(); j++) {

                ArrayList<Rail> network = GRASPConstruction();
                int relaxedDelays = ARPOtimizator.relaxAllDelays(network);
                int cost = AirlineNetwork.getTotalCost(network);
                if (cost < airlineNetwork.getBestNetworkCost())
                {
                    System.out.println("Delays relaxados = " + relaxedDelays);
                    Long actualTime = System.currentTimeMillis();
                    LogManager.writeMsg(String.format("Melhorou a solução (%s - %s) [%d s]\n", airlineNetwork.getBestNetworkCost(), cost, ((actualTime - beginTime)/1000)));
                    airlineNetwork.setBestNetwork(network, cost);
                    
                    if (!airlineNetwork.validadeSolution()) {
                        LogManager.writeMsg("ERRO");
                        System.exit(1);
                    }
                }
            }
        }

        HeuristicInformation heuristicInformation = new HeuristicInformation(gRASPParameters, aRPParameters, airlineNetwork.getPathInstance());
        heuristicInformation.setDuration((int)((System.currentTimeMillis()-beginTime)/1000));
        heuristicInformation.setBestValue(airlineNetwork.getBestNetworkCost());
        HeuristicInformationJpaController heuristicInformationJpaController = HeuristicInformationJpaController.getInstance();
        try {
            heuristicInformationJpaController.create(heuristicInformation);
        } catch (Exception ex) {
            Logger.getLogger(GRASPConstruct.class.getName()).log(Level.SEVERE, null, ex);
        }


        System.out.println("Solucao com custo " + airlineNetwork.getBestNetworkCost());
        Long actualTime = System.currentTimeMillis();
        System.out.println("Tempo total " + ((actualTime - beginTime)/1000) + " s");
    }

    /**
     * Efetua uma construção do GRASP
     */
    public ArrayList<Rail> GRASPConstruction() {
        //LogManager.writeMsg("Iniciando a construção GRASP da malha.");

        ArrayList<Flight> clonedFlights = airlineNetwork.getFlightsClone();
        ArrayList<Rail> network = new ArrayList<Rail>();

        for (int i = 0; i < clonedFlights.size(); i++) {

            //Lista de candidatos a primeiro voo.
            ArrayList<Flight> firstFlightCandidates = new ArrayList<Flight>();

            /**
             * Monta os candidatos a primeiro vôo, eles não podem estar
             * já alocados em outro trilho.
             *
             * 4 Voos já é suficiente para ter um candidato. O aumento desse
             * número aumenta a aleatóriedade da solução final.
             */
            for (Flight flightCandidate : clonedFlights) {
                if (flightCandidate.getRailNumber() == -1) {
                    flightCandidate.setDelay(0);
                    firstFlightCandidates.add(flightCandidate);
                }

                if (firstFlightCandidates.size() == 4) {
                    break;
                }
            }

            //Finaliza o algoritmo caso não tenha mais voos desalocados.
            if (firstFlightCandidates.isEmpty()) {
                break;
            }

            Flight actualFlight = firstFlightCandidates.get(RandomManager.getNext(firstFlightCandidates.size()));
            actualFlight.setDelay(-aRPParameters.getMaximumDelay());

            Rail rail = new Rail(network.size());
            network.add(rail);

            while (actualFlight != null) {
                actualFlight.setRailNumber(rail.getNumber());
                rail.addFlight(actualFlight);
                //Flight repoFlight = new Flight();
                //repoFlight.setName(null);
                Flight f = calculateNextFlight(actualFlight, clonedFlights);

//                if(repoFlight.getName() != null){
//                    repoFlight.setRailNumber(rail.getNumber());
//                    repoFlight.setReposition(true);
//                    rail.addFlight(repoFlight);
//                }

                actualFlight = f;
            }

            //rail.calculateCost();

        }

        resolved = true;

        // LogManager.writeMsg("Finalizando a construção GRASP da malha.");

        return network;
    }

    /**
     * Informa se a malha já foi montada.
     * @return
     */
    public Boolean getResolved() {
        return resolved;
    }

    /**
     * Obtem o proximo vôo candidato a partir de um dado vôo.
     * @param flightCandidate
     * @return null caso não haja mais nenhum vôo que possa sucede-lo
     */
    private Flight calculateNextFlight(Flight actualFlight, ArrayList<Flight> clonedFlights) {
        int arcType;
        int cicleNumber = 0;

        arcType = randomizingArc();
        ArrayList<Flight> adjacentFlight;
        boolean [] selectedFligths = new boolean[]{false,false,false,false};
        selectedFligths[arcType] = true;

//        System.out.println("------------------");

        while (true) {

            adjacentFlight = extractAdjacentFlight(actualFlight, arcType, clonedFlights);

            if (adjacentFlight.isEmpty()) {

                cicleNumber++;
                if (cicleNumber == 4) {
                    return null;
                }

                int oldArcType = arcType;
                
                for (int i = 0; i < selectedFligths.length; i++) {
                    if(!selectedFligths[i]){
                        selectedFligths[i] = true;
                        arcType = i;
                        break;
                    }

                }

                //arcType = (arcType + 1) % 4;

                //System.out.printf("Mudando de %d para %d\n", oldArcType, arcType);

            } else {
                break;
            }



        }

        int range = (int) Math.ceil(adjacentFlight.size() * gRASPParameters.getAlfa());

        Flight selectedFlight = adjacentFlight.get(RandomManager.getNext(range));

        
//        if(arcType == 2 || arcType == 3){
//            int newArrivalTime = selectedFlight.getRealDepartureTime() - selectedFlight.getGroundTime();
//            int fligthTime = actualFlight.getArrivalCity().getFlightTimes().get(selectedFlight.getDepartureCity());
//            int newDepartureTime = newArrivalTime - fligthTime;
//
//            repositionFlight.configure(new Flight("REPO", clonedFlights.size(), actualFlight.getArrivalCity(), selectedFlight.getDepartureCity()
//                    , newDepartureTime, newArrivalTime));
//
//        }

        return selectedFlight;
    }

    /**
     * Retorna o tipo do arco sorteado de acordo com as probabilidades
     * definidas no AircraftRotationParameters.
     *
     * @return
     *
     * <ul>
     *  <li> 0  - Arco tipo 1 </li>
     *  <li> 1  - Arco tipo 2 </li>
     *  <li> 2  - Arco tipo 3 </li>
     *  <li> 3  - Arco tipo 4 </li>
     * </ul>
     *
     * -1 indica um erro na configuração das probabilidades dos tipos de arcos.
     */
    private int randomizingArc() {

        int number = RandomManager.getNext(100) + 1;

        if (number <= aRPParameters.getProbabilityType1Arc()) {
            return 0;
        }

        number -= aRPParameters.getProbabilityType1Arc();

        if (number <= aRPParameters.getProbabilityType2Arc()) {
            return 1;
        }

        number -= aRPParameters.getProbabilityType2Arc();

        if (number <= aRPParameters.getProbabilityType3Arc()) {
            return 2;
        }

        number -= aRPParameters.getProbabilityType3Arc();

        if (number <= aRPParameters.getProbabilityType4Arc()) {
            return 3;
        }

        return -1;

    }

    /**
     * Obtem todos os vôos adjacents ao actualFlight respeitando o tipo de arco passado.
     * @param actualFlight
     * @param arcType
     * @return
     *
     * Lista de Voos adjacentes permitidos.
     */
    private ArrayList<Flight> extractAdjacentFlight(Flight actualFlight, int arcType, ArrayList<Flight> clonedFlights) {
        switch (arcType) {
            case 0:
                return extractAdjacentFlightArcType1(actualFlight, clonedFlights);
            case 1:
                return extractAdjacentFlightArcType2(actualFlight, clonedFlights);
            case 2:
                return extractAdjacentFlightArcType3(actualFlight, clonedFlights);
            case 3:
                return extractAdjacentFlightArcType4(actualFlight, clonedFlights);
            default:
                throw new RuntimeException("Tipo de arco invalido.");

        }
    }

    /**
     * Obtem uma lista de voos que possam ser adjacentes ao voo atual
     * sem violar as restrições do arco do tipo 1.
     * @param actualFlight
     * @return
     */
    private ArrayList<Flight> extractAdjacentFlightArcType1(Flight actualFlight, ArrayList<Flight> clonedFlights) {
        //Como o candidato tem que partir depois do voo atual.
        int candidateNumber = actualFlight.getNumber() + 1;
        ArrayList<Flight> flights = clonedFlights;
        ArrayList<Flight> adjacentFlights = new ArrayList<Flight>();
        int numberOfFlights = flights.size();
        Flight candidate = null;


        /*
         * Para todos os voos faca:
         * 	se ainda nao esta em nenhum trilho
         * 	     se a ligacao e direta
         *                e nao e necessario atraso
         * 		       adicione este voo aos voos_adjacentes
         */
        while (candidateNumber < numberOfFlights) {
            candidate = flights.get(candidateNumber);

            if ((candidate.getRailNumber() == -1)
                    && (ARPConstraintsValidator.validateGeographicalConstraint(actualFlight, candidate))
                        &&  ARPConstraintsValidator.validateTemporalConstraintWithoutDelay(actualFlight, candidate)) {

                candidate.setDelay(0);
                adjacentFlights.add(candidate);
                /*int delay = actualFlight.getRealArrivalTime() - (candidate.getDepartureTime() - candidate.getGroundTime());

                if (delay <= 0) {
                    if((-delay) > aircraftRotationParameters.getMaximumDelay()){
                        delay = -aircraftRotationParameters.getMaximumDelay();
                    }

                    candidate.setDelay(delay);
                    adjacentFlights.add(candidate);
                }*/

            }

            candidateNumber++;
        }

        /**
         * Como os voos já vem ordenado por tempo de partida essa lista já se encontra ordenada.
         */
        return adjacentFlights;
    }

    private ArrayList<Flight> extractAdjacentFlightArcType2(Flight actualFlight, ArrayList<Flight> clonedFlights) {
        //Como o candidato tem que partir depois do voo atual.
        int candidateNumber = actualFlight.getNumber() + 1;
        ArrayList<Flight> flights = clonedFlights;
        ArrayList<Flight> adjacentFlights = new ArrayList<Flight>();
        int numberOfFlights = flights.size();
        Flight candidate = null;

        /*
         * Para todos os voos faca:
         * 	se ainda nao esta em nenhum trilho
         * 	     se a ligacao e direta
         *                e nao e necessario atraso
         * 		       adicione este voo aos voos_adjacentes
         */
        while (candidateNumber < numberOfFlights) {
            candidate = flights.get(candidateNumber);

            if ((candidate.getRailNumber() == -1)
                    && ARPConstraintsValidator.validateGeographicalConstraint(actualFlight, candidate)) {

                int delay = actualFlight.getRealArrivalTime() - (candidate.getDepartureTime() - candidate.getGroundTime());

                /*if ((delay > 0) && (delay <= aircraftRotationParameters.getMaximumDelay())) {
                    candidate.setDelay(delay);
                    adjacentFlights.add(candidate);
                }*/

                if(delay != 0 && Math.abs(delay) <= aRPParameters.getMaximumDelay()){
                    candidate.setDelay(delay);
                    adjacentFlights.add(candidate);
                }
            }

            candidateNumber++;
        }


        /**
         * Ordena em relação ao custo
         */
        Collections.sort(adjacentFlights);
        return adjacentFlights;
    }

    private ArrayList<Flight> extractAdjacentFlightArcType3(Flight actualFlight, ArrayList<Flight> clonedFlights) {

        if(true) return new ArrayList<Flight>();
        //Como o candidato tem que partir depois do voo atual.
        int candidateNumber = actualFlight.getNumber() + 1;
        ArrayList<Flight> flights = clonedFlights;
        ArrayList<Flight> adjacentFlights = new ArrayList<Flight>();
        int numberOfFlights = flights.size();
        Flight candidate = null;


        /*
         * Para todos os voos faca:
         * 	se ainda nao esta em nenhum trilho
         * 	     se a ligacao e direta
         *                e nao e necessario atraso
         * 		       adicione este voo aos voos_adjacentes
         */
        while (candidateNumber < numberOfFlights) {
            candidate = flights.get(candidateNumber);

            if ((candidate.getRailNumber() == -1)
                    && (!ARPConstraintsValidator.validateGeographicalConstraint(actualFlight, candidate))) {

                candidate.setDelay(0);

               int timeSpace = (candidate.getDepartureTime() - candidate.getGroundTime()) - actualFlight.getRealArrivalTime();
               int needTime = actualFlight.getArrivalCity().getFlightTimes().get(candidate.getDepartureCity()) + actualFlight.getArrivalCity().getGroundTime();

               if(timeSpace >= needTime){
                adjacentFlights.add(candidate);
               }

            }

            candidateNumber++;
        }

        /**
         * Como os voos já vem ordenado por tempo de partida essa lista já se encontra ordenada.
         */
        return adjacentFlights;
    }

    private ArrayList<Flight> extractAdjacentFlightArcType4(Flight actualFlight, ArrayList<Flight> clonedFlights) {
        return new ArrayList<Flight>();
    }
}
