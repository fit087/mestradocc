/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.heuristic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import main.entities.AirlineNetwork;
import main.entities.Flight;
import main.entities.Track;
import util.SolutionUtil;

/**
 *
 * @author alexanderdealmeidapinto
 */
public class SolverManager {

    private static final String solverPath = "/Users/alexander/Documents/Mestrado/dissertacao/trunk/SOLVER/";
    

    public static void executeSolver(AirlineNetwork airlineNetwork, boolean useSolver, int max_delay) throws IOException, InterruptedException {
        File inputFile = new File("entrada.txt");
        File outputFile = new File("saida.txt");

        inputFile.delete();
        outputFile.delete();

        SolutionUtil.writeFormatedAirlineNetworkForCplex(airlineNetwork, inputFile, max_delay);

        ArrayList<String> commands = new ArrayList<String>();
        commands.add(solverPath + "main");
        commands.add(inputFile.getAbsolutePath());  
        if (useSolver) {
            commands.add("true");
        }
        String[] args = commands.toArray(new String[0]);

        System.out.println("[SolverManager.executeSolver] Comando utilizado: " + Arrays.toString(args));
        Process process = Runtime.getRuntime().exec(args);

        
        showStream(process.getInputStream(), "-------------- Saída padrão ------------- ");
    
        showStream(process.getErrorStream(), "-------------- Saída de erros ------------- ");
        
        process.waitFor();
        
        

        System.out.println("[SolverManager.executeSolver] Construindo a solução");

        constructFromFile(airlineNetwork, outputFile);

        System.out.println("[SolverManager.executeSolver] Finalizado");
    }
    
    public static void showStream(InputStream is, String text) throws IOException{
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;

        System.out.printf("%s:\n", text);

        while ((line = br.readLine()) != null) {
            System.out.println("\t--" + line);
        }
        
        System.out.println("-------");
        
        isr.close();
    }

    public static void constructFromFile(AirlineNetwork airlineNetwork, File file) throws FileNotFoundException {
        Scanner scan = new Scanner(file);

        int nt = scan.nextInt();
        ArrayList<Track> network = new ArrayList<Track>();

        //System.out.println("Numero de trilhos = " + nt);

        for (int i = 0; i < nt; i++) {
            int n = scan.nextInt();

//            System.out.println("Numero de voos = " + n);

            Track track = new Track(i);
            Flight lastFlight = null;
            for (int j = 0; j < n; j++) {
                int k = scan.nextInt();
                int d = scan.nextInt();

                //  System.out.println("\tVoo = " + k);

                Flight actualFlight = null;

                actualFlight = airlineNetwork.getFlights().get(k);

                actualFlight.setDelay(d);

                if (lastFlight != null) {

                    if (ARPConstraintsValidator.validateGeographicalConstraint(lastFlight, actualFlight)) {
                        int delay = lastFlight.getRealArrivalTime() - (actualFlight.getDepartureTime() - actualFlight.getGroundTime());

                        if (delay > 0) {
                            actualFlight.setDelay(delay);
                        }
                    } else {

                        //     System.out.println("[SolverManager.constructFromFile] Criando voo de reposicionamento...");
                        Flight repoFlight = ARPConstraintsValidator.generateRepoFlight(lastFlight, actualFlight, airlineNetwork);

                        int delay = repoFlight.getRealArrivalTime() - (actualFlight.getDepartureTime() - actualFlight.getGroundTime());

                        if (delay > 0) {
                            actualFlight.setDelay(delay);
                        }

                        track.addFlight(repoFlight);

                    }



                }

                track.addFlight(actualFlight);
                lastFlight = actualFlight;
            }

            network.add(track);
        }

        scan.close();

        // ARPOtimizator.relaxAllDelays(network);

        airlineNetwork.setBestNetwork(network, AirlineNetwork.getTotalCost(network));
    }
}
