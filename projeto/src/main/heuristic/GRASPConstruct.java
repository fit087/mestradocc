/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.heuristic;

import gui.JFrameGraphicTest;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import main.entities.AirlineNetwork;
import main.entities.Flight;
import main.entities.Rail;
import main.reader.ARPFileReader;
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
    private ARPParameters aircraftRotationParameters;
    private Integer numberOfReposisions;

    /**
     * Constroi uma malha
     * @param airlineNetwork Dados do problema.
     * @param gRASPParameters Parametros do GRASPl
     * @param aircraftRotationParameters Parametros do ARP (Aircraft Rotation Problem)
     */
    public GRASPConstruct(AirlineNetwork airlineNetwork, GRASPParameters gRASPParameters, ARPParameters aircraftRotationParameters) {
        this.airlineNetwork = airlineNetwork;
        this.gRASPParameters = gRASPParameters;
        this.aircraftRotationParameters = aircraftRotationParameters;
        this.resolved = false;
    }

    public void GRASPResolve() {

//        City city = new City("Teste", 239);
//        CityJpaController cityJpaController = new CityJpaController();
//        cityJpaController.create(city);

        LogManager.writeMsg("Começou a resolver...");
        Long beginTime = System.currentTimeMillis();

        for (int i = 0; i < gRASPParameters.getNumberOfRepetitions(); i++) {
            int percentComplete = (100 * (i + 1)) / gRASPParameters.getNumberOfRepetitions();
            JFrameGraphicTest.instance.setPercentComplete(percentComplete);

            for (int j = 0; j < gRASPParameters.getNumberOfConstructions(); j++) {

                ArrayList<Rail> network = GRASPConstruction();
                int relaxedDelays = ARPOtimizator.relaxAllDelays(network);
                //System.out.println("Delays relaxados = " + relaxedDelays);
                int cost = AirlineNetwork.getTotalCost(network);
                if (cost < airlineNetwork.getBestNetworkCost())
                {
                    LogManager.writeMsg(String.format("Melhorou a solução (%s - %s)\n", airlineNetwork.getBestNetworkCost(), cost));
                    Long actualTime = System.currentTimeMillis();
                    System.out.println("Tempo " + (actualTime - beginTime) + " s");
                    airlineNetwork.setBestNetwork(network, cost);
                    if (!airlineNetwork.validadeSolution()) {
                        LogManager.writeMsg("ERRO");
                        System.exit(1);
                    }
                }
            }
        }

//        HeuristicInfo heuristicInfo = new HeuristicInfo(System.currentTimeMillis() - beginTime, aircraftRotationParameters, gRASPParameters, airlineNetwork.getBestNetworkCost(), "Teste");
//        HeuristicInfoJpaController hijc = new HeuristicInfoJpaController();
//        hijc.create(heuristicInfo);

        System.out.println("Solucao com custo " + airlineNetwork.getBestNetworkCost());
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
            actualFlight.setDelay(-aircraftRotationParameters.getMaximumDelay());

            Rail rail = new Rail(network.size());
            network.add(rail);

            while (actualFlight != null) {
                actualFlight.setRailNumber(rail.getNumber());
                rail.addFlight(actualFlight);
                Flight f = calculateNextFlight(actualFlight, clonedFlights);

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

        while (true) {

            adjacentFlight = extractAdjacentFlight(actualFlight, arcType, clonedFlights);

            if (adjacentFlight.isEmpty()) {

                cicleNumber++;
                if (cicleNumber == 3) {
                    return null;
                }

                arcType = (arcType + cicleNumber) % 4;

            } else {
                break;
            }



        }

        int range = (int) Math.ceil(adjacentFlight.size() * gRASPParameters.getAlfa());

        Flight selectedFlight = adjacentFlight.get(RandomManager.getNext(range));

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

        if (number <= aircraftRotationParameters.getProbabilityType1Arc()) {
            return 0;
        }

        number -= aircraftRotationParameters.getProbabilityType1Arc();

        if (number <= aircraftRotationParameters.getProbabilityType2Arc()) {
            return 1;
        }

        number -= aircraftRotationParameters.getProbabilityType2Arc();

        if (number <= aircraftRotationParameters.getProbabilityType3Arc()) {
            return 2;
        }

        number -= aircraftRotationParameters.getProbabilityType3Arc();

        if (number <= aircraftRotationParameters.getProbabilityType4Arc()) {
            return 3;
        }

        return -1;

    }

    public static void main(String[] args) throws FileNotFoundException {
        AirlineNetwork airlineNetwork = new AirlineNetwork();
        ARPFileReader.readDataFromFile("instances/01", airlineNetwork);

        GRASPConstruct gRASPNetWorkConstruct = new GRASPConstruct(airlineNetwork, GRASPParameters.defaultParameters, ARPParameters.defaultParameters);
        gRASPNetWorkConstruct.GRASPResolve();
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

                if(delay != 0 && Math.abs(delay) <= aircraftRotationParameters.getMaximumDelay()){
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
        return new ArrayList<Flight>();
    }

    private ArrayList<Flight> extractAdjacentFlightArcType4(Flight actualFlight, ArrayList<Flight> clonedFlights) {
        return new ArrayList<Flight>();
    }
}
