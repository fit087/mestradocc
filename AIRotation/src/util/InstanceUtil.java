/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.entities.AirlineNetwork;
import main.entities.City;
import main.entities.Flight;
import main.entities.PieceOfFlight;
import main.entities.Track;
import main.heuristic.ARPParameters;
import main.heuristic.SolverManager;
import main.reader.ARPFileReader;
import util.param.IntegerParam;

/**
 *
 * @author alexanderdealmeidapinto
 */
public class InstanceUtil {

    private static int cont = 0;

    public static HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>> generatePieces(AirlineNetwork airlineNetwork) {
        System.out.println("Gerando \"pedaços\" de vôos");

        ArrayList<City> cities = airlineNetwork.getCities();

        ArrayList<PieceOfFlight> singleFlights = new ArrayList<PieceOfFlight>();

        //Gerando voos simples.
        for (int i = 0; i < cities.size() - 1; i++) {
            City city1 = cities.get(i);
            for (int j = (i + 1); j < cities.size(); j++) {
                City city2 = cities.get(j);

                //Se o tempo de voo entre os aeroportos não for conhecido.
                //Ele não será levado em consideração.
                if (city1.getFlightTimes().get(city2) == null || city1.getFlightTimes().get(city2) == 99999) {
                    continue;
                }

                PieceOfFlight single1 = new PieceOfFlight();
                PieceOfFlight single2 = new PieceOfFlight();

                single1.addPseudoFlight(city1, city2, city1.getFlightTimes().get(city2));
                single2.addPseudoFlight(city2, city1, city1.getFlightTimes().get(city2));

                singleFlights.add(single1);
                singleFlights.add(single2);
            }
        }

        System.out.println("Número de vôos simples gerados: " + singleFlights.size());

        /**
         * Concatenando conjuntos de voos com voos simples
         * deve-se gerar conjuntos de voos válidos.
         */
        HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>> journeys = new HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>>();

        insertFlights(cloneList(singleFlights), journeys);

        int maxSize = 3;

        ArrayList<PieceOfFlight> currentFlights = cloneList(singleFlights);

        for (int i = 0; i < maxSize; i++) {

            long init = System.currentTimeMillis();

            currentFlights = combinateList(currentFlights, singleFlights);
            insertFlights(cloneList(currentFlights), journeys);

            int time = (int) ((System.currentTimeMillis() - init));


            System.out.printf("\tDuração do passo (%d): %d\n", (i + 1), time);
            System.out.printf("\t\tVoos gerados: %d\n", currentFlights.size());

        }

        System.out.println("Geração dos \"pedaços\" finalizada");
        return journeys;
    }

    private static void insertFlights(ArrayList<PieceOfFlight> newPieces, HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>> journeys) {
        for (int i = 0; i < newPieces.size(); i++) {

            PieceOfFlight pieceOfFlight = newPieces.get(i);
            City departure = pieceOfFlight.getDepartureCity();
            City arrival = pieceOfFlight.getArrivalCity();

            HashMap<City, ArrayList<PieceOfFlight>> journeyWithDeparture = journeys.get(departure);

            /**
             * Se não existir então efetua a criação.
             */
            if (journeyWithDeparture == null) {
                journeys.put(departure, new HashMap<City, ArrayList<PieceOfFlight>>());
                journeyWithDeparture = journeys.get(departure);
            }

            ArrayList<PieceOfFlight> pieces = journeyWithDeparture.get(arrival);

            /**
             * Se não existir então efetua a criação.
             */
            if (pieces == null) {
                journeyWithDeparture.put(arrival, new ArrayList<PieceOfFlight>());
                pieces = journeyWithDeparture.get(arrival);
            }

            pieces.add(pieceOfFlight);
            Collections.sort(pieces);
        }
    }

    private static ArrayList<PieceOfFlight> combinateList(ArrayList<PieceOfFlight> target, ArrayList<PieceOfFlight> unit) {

        ArrayList<PieceOfFlight> result = new ArrayList<PieceOfFlight>();

        for (int i = 0; i < target.size(); i++) {
            PieceOfFlight targetPiece = target.get(i);

            for (int j = 0; j < unit.size(); j++) {
                PieceOfFlight unitPiece = unit.get(j);

                //Se houver a possibilidade de ligarção entre esses "pedaços" de vôos
                if (targetPiece.getArrivalCity().getName().equals(unitPiece.getDepartureCity().getName())) {
                    PieceOfFlight newPiece = targetPiece.clone();
                    newPiece.addAllFlight(unitPiece);
                    result.add(newPiece);
                }
            }
        }

        return result;
    }

    private static ArrayList<PieceOfFlight> cloneList(ArrayList<PieceOfFlight> list) {
        ArrayList<PieceOfFlight> clone = new ArrayList<PieceOfFlight>(list.size());
        for (PieceOfFlight item : list) {
            clone.add(item.clone());
        }
        return clone;
    }

    public static PieceOfFlight obtainBestMidPieceOfFlight(City orig, City dest, long availableTime, HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>> allPieces,
            IntegerParam numberOfCrewFlightsAhead, IntegerParam timeOfCrewFlightsAhead, IntegerParam numberOfCrewFlightsBehind, IntegerParam timeOfCrewFlightsBehind) {
        ArrayList<PieceOfFlight> pieces = allPieces.get(orig).get(dest);

        PieceOfFlight bestPieceFlight = null;



        for (PieceOfFlight pieceOfFlight : pieces) {

            int totalNumberOfCrewFlights = numberOfCrewFlightsAhead.getValue() + numberOfCrewFlightsBehind.getValue() + pieceOfFlight.size();
            int totalTimeOfCrewFlights = timeOfCrewFlightsAhead.getValue() + timeOfCrewFlightsBehind.getValue() + pieceOfFlight.getTotalFlightTime();

            totalNumberOfCrewFlights /= ARPParameters.defaultParameters.getMaxCrewFlights();
            totalTimeOfCrewFlights /= ARPParameters.defaultParameters.getMaxCrewFlightTime();

            int deduceTime = totalNumberOfCrewFlights > totalTimeOfCrewFlights ? totalNumberOfCrewFlights : totalTimeOfCrewFlights;
            if (deduceTime != 0) {

                deduceTime *= ARPParameters.defaultParameters.getCrewChangeTime();

            }

            long realAvailableTime = availableTime - deduceTime;


            if (pieceOfFlight.getTotalDuration() <= realAvailableTime) {
                bestPieceFlight = pieceOfFlight;
            }
        }

        return bestPieceFlight;
    }

    private static PieceOfFlight obtainBestFirstPieceOfFlight(City departureCity, long availableTime, HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>> allPieces, int numberOfCrewFlights, int timeOfCrewFlights) {
        Set<City> allDepartureCities = allPieces.keySet();
        PieceOfFlight bestPieceOfFlight = null;


        for (City city : allDepartureCities) {
            ArrayList<PieceOfFlight> pieces = allPieces.get(city).get(departureCity);

            for (PieceOfFlight pieceOfFlight : pieces) {
                int deduceTimeMaxFlights = (numberOfCrewFlights + pieceOfFlight.size()) / ARPParameters.defaultParameters.getMaxCrewFlights();
                int deduceTimeMaxTime = (timeOfCrewFlights + pieceOfFlight.getTotalFlightTime()) / ARPParameters.defaultParameters.getMaxCrewFlightTime();

                int deduceTime = deduceTimeMaxFlights > deduceTimeMaxTime ? deduceTimeMaxFlights : deduceTimeMaxTime;
                if (deduceTime != 0) {

                    deduceTime *= ARPParameters.defaultParameters.getCrewChangeTime();

                }

                long realAvailableTime = availableTime - deduceTime;

                if (pieceOfFlight.getTotalDuration() <= realAvailableTime) {
                    if (bestPieceOfFlight == null) {
                        bestPieceOfFlight = pieceOfFlight;
                    } //Tenta obter a peça de voo com melhor aproveitamento.
                    else if (pieceOfFlight.getTotalDuration() > bestPieceOfFlight.getTotalDuration()) {
                        bestPieceOfFlight = pieceOfFlight;
                    }
                }

            }
        }

        return bestPieceOfFlight;
    }

    private static PieceOfFlight obtainBestEndPieceOfFlight(City arrivalCity, long availableTime, HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>> allPieces, int numberOfCrewFlights, int timeOfCrewFlights) {

        PieceOfFlight bestPieceOfFlight = null;

        HashMap<City, ArrayList<PieceOfFlight>> piecesOfArrival = allPieces.get(arrivalCity);
        Set<City> allArrivalCity = piecesOfArrival.keySet();

        for (City city : allArrivalCity) {
            ArrayList<PieceOfFlight> pieces = piecesOfArrival.get(city);

            for (PieceOfFlight pieceOfFlight : pieces) {
                int deduceTimeMaxFlights = (numberOfCrewFlights + pieceOfFlight.getFlights().size()) / ARPParameters.defaultParameters.getMaxCrewFlights();
                int deduceTimeMaxTime = (timeOfCrewFlights + pieceOfFlight.getTotalFlightTime()) / ARPParameters.defaultParameters.getMaxCrewFlightTime();

                int deduceTime = deduceTimeMaxFlights > deduceTimeMaxTime ? deduceTimeMaxFlights : deduceTimeMaxTime;
                if (deduceTime != 0) {

                    // System.out.println("Deduce Time 1 (!= 0): " + deduceTime);
                    // System.out.println("CrewChangeTime (!= 0): " + ARPParameters.defaultParameters.getCrewChangeTime());
                    //Numero de trocas de tripulacoes necessarias para essa quantidade de voos
                    deduceTime *= ARPParameters.defaultParameters.getCrewChangeTime();
                    // System.out.println("Deduce Time 2 (!= 0): " + deduceTime);

                }

                long realAvailableTime = availableTime - deduceTime;

                if (pieceOfFlight.getTotalDuration() <= realAvailableTime) {
                    if (bestPieceOfFlight == null) {
                        bestPieceOfFlight = pieceOfFlight;
                    } //Tenta obter a peça de voo com melhor aproveitamento.
                    else if (pieceOfFlight.getTotalDuration() > bestPieceOfFlight.getTotalDuration()) {
                        bestPieceOfFlight = pieceOfFlight;
                    }
                }
            }

        }

        return bestPieceOfFlight;
    }

    public static void completeMidFlights(AirlineNetwork airlineNetwork, HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>> allPieces) {
        System.out.println("Iniciando o preechimento internos dos trilhos");
        ArrayList<Track> solution = airlineNetwork.getBestNetwork();

        //Tenta inserir voos entre os voos intermediarios
        for (Track track : solution) {
            ArrayList<Flight> trackFlights = track.getFlights();

            for (int i = 1; i < trackFlights.size(); i++) {
                Flight f1 = trackFlights.get(i - 1);
                Flight f2 = trackFlights.get(i);

                City city = f1.getArrivalCity();
                long availableTime = (f2.getRealDepartureTime() - f2.getGroundTime()) - (f1.getRealArrivalTime());

                IntegerParam numberOfCrewFlightsAhead = new IntegerParam();
                IntegerParam timeOfCrewFlightsAhead = new IntegerParam();

                IntegerParam numberOfCrewFlightsBehind = new IntegerParam();
                IntegerParam timeOfCrewFlightsBehind = new IntegerParam();

                track.getNumberOfCrewFlightAhead(i, numberOfCrewFlightsAhead, timeOfCrewFlightsAhead);
                track.getNumberOfCrewFlightBehind(i - 1, numberOfCrewFlightsBehind, timeOfCrewFlightsBehind);

                PieceOfFlight bestPieceFlight = obtainBestMidPieceOfFlight(city, city, availableTime, allPieces, numberOfCrewFlightsAhead, timeOfCrewFlightsAhead, numberOfCrewFlightsBehind, timeOfCrewFlightsBehind);
                if (bestPieceFlight != null) {
                    
                    ArrayList<Flight> newsFlighs = generateFlights(bestPieceFlight, f1.getArrivalTime(), numberOfCrewFlightsBehind.getValue(), timeOfCrewFlightsBehind.getValue());

                    System.out.println("Adicionando " + newsFlighs.size() + " voos no trilho " + track.getNumber());
                    for (Flight flight : newsFlighs) {
                        System.out.println("\t " + flight.toString());
                    }
                    trackFlights.addAll(i, newsFlighs);

                    System.out.println("----------");

                }
            }
        }
    }

    private static void completeFlightsOnBegin(AirlineNetwork airlineNetwork, HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>> allPieces) {
        System.out.println("Iniciando o preechimento inicial dos trilhos");

        ArrayList<Track> solution = airlineNetwork.getBestNetwork();
        int beginTime = airlineNetwork.getFlights().get(0).getDepartureTime();
        for (Track track : solution) {
            Flight firstFlight = track.getFirstFlight();
            long availableTime = (firstFlight.getRealDepartureTime() - firstFlight.getGroundTime()) - (beginTime);

            IntegerParam numberOfCrewFlights = new IntegerParam();
            IntegerParam timeOfCrewFlights = new IntegerParam();

            track.getNumberOfCrewFlightAhead(0, numberOfCrewFlights, timeOfCrewFlights);

            PieceOfFlight bestPieceFlight = obtainBestFirstPieceOfFlight(firstFlight.getDepartureCity(), availableTime, allPieces, numberOfCrewFlights.getValue(), timeOfCrewFlights.getValue());
            if (bestPieceFlight != null) {
                ArrayList<Flight> newsFlighs = generateFlights(bestPieceFlight, beginTime, 0, 0);

                System.out.println("Adicionando, no inicio, " + newsFlighs.size() + " voos no trilho " + track.getNumber());
                for (Flight flight : newsFlighs) {
                    System.out.println("\t " + flight.toString());
                }
                track.getFlights().addAll(0, newsFlighs);

                System.out.println("----------");
            }
        }
    }

    private static void completeFlightsOnEnd(AirlineNetwork airlineNetwork, HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>> allPieces) {
        System.out.println("Iniciando o preechimento final dos trilhos");

        ArrayList<Track> solution = airlineNetwork.getBestNetwork();

        int endTime = airlineNetwork.getFlights().get(airlineNetwork.getFlights().size() - 1).getArrivalTime();
        for (Track track : solution) {
            Flight lastFlight = track.getLastFlight();
            long availableTime = endTime - lastFlight.getArrivalTime();

            IntegerParam numberOfCrewFlights = new IntegerParam();
            IntegerParam timeOfCrewFlights = new IntegerParam();

            track.getNumberOfCrewFlightBehind(0, numberOfCrewFlights, timeOfCrewFlights);

            PieceOfFlight bestPieceFlight = obtainBestEndPieceOfFlight(lastFlight.getArrivalCity(), availableTime, allPieces, numberOfCrewFlights.getValue(), timeOfCrewFlights.getValue());

            if (bestPieceFlight != null) {
                ArrayList<Flight> newsFlighs = generateFlights(bestPieceFlight, lastFlight.getArrivalTime(), numberOfCrewFlights.getValue(), timeOfCrewFlights.getValue());

                System.out.println("Adicionando, no fim, " + newsFlighs.size() + " voos no trilho " + track.getNumber());
                for (Flight flight : newsFlighs) {
                    System.out.println("\t " + flight.toString());
                }
                track.getFlights().addAll(0, newsFlighs);

                System.out.println("----------");
            }
        }
    }

    public static void completeSolution(AirlineNetwork airlineNetwork) {
        cont = 0;
        HashMap<City, HashMap<City, ArrayList<PieceOfFlight>>> allPieces = generatePieces(airlineNetwork);

        completeMidFlights(airlineNetwork, allPieces);
        completeFlightsOnBegin(airlineNetwork, allPieces);
        completeFlightsOnEnd(airlineNetwork, allPieces);


    }

    public static void main(String[] args) throws Exception {
        AirlineNetwork airlineNetwork = new AirlineNetwork("/Users/alexander/Documents/Mestrado/dissertacao/trunk/AIRotation/instances/tam/");

        try {
            ARPFileReader.readDataFromFile(airlineNetwork.getPathInstance(), airlineNetwork);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(InstanceUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Dados lidos com sucesso!");

        SolverManager.executeSolver(airlineNetwork, true, 0);

        completeSolution(airlineNetwork);

    }

    private static ArrayList<Flight> generateFlights(PieceOfFlight bestPieceFlight, Integer arrivalTime, int numberOfCrewFlights, int timeOfCrewFlights) {
        ArrayList<Flight> response = new ArrayList<Flight>();
        int nFlightsCont = numberOfCrewFlights;
        int totalFlightTime = timeOfCrewFlights;


        for (PieceOfFlight.PseudoFlight pseudoFlight : bestPieceFlight.getFlights()) {

            nFlightsCont++;
            totalFlightTime += pseudoFlight.getDuration();

            if (nFlightsCont >= ARPParameters.defaultParameters.getMaxCrewFlights()
                    || totalFlightTime >= ARPParameters.defaultParameters.getMaxCrewFlightTime()) {

                arrivalTime += ARPParameters.defaultParameters.getCrewChangeTime() - pseudoFlight.getDepartureCity().getGroundTime();
                nFlightsCont = 0;
                totalFlightTime = 0;

            }

            int pseudoDepartureTime = arrivalTime + pseudoFlight.getGroundTime();
            int pseudoArrivalTime = pseudoDepartureTime + pseudoFlight.getDuration();
            response.add(new Flight("PSEUDO" + cont, cont, pseudoFlight.getDepartureCity(), pseudoFlight.getArrivalCity(), pseudoDepartureTime, pseudoArrivalTime));
            cont++;

            arrivalTime = pseudoArrivalTime;


        }

        return response;
    }
}
