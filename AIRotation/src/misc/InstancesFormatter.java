/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import main.entities.AirlineNetwork;
import main.entities.Flight;
import main.reader.ARPFileReader;
import util.TimeUtil;

/**
 *
 * @author alexanderdealmeidapinto
 */
public class InstancesFormatter {


    public static void main(String [] args) throws FileNotFoundException{
        String path = "/Users/alexanderdealmeidapinto/Documents/Mestrado/Projeto/svn/InstanciaTAM/";
        JFileChooser chooserSave = new JFileChooser();

            
            AirlineNetwork airlineNetwork = new AirlineNetwork(null);
            ARPFileReader.readCityFromFile(new File(path + "cidades.txt"), airlineNetwork);
            ARPFileReader.readFlightFromFile(new File(path + "voos.txt"), airlineNetwork);

            for (Flight flight : airlineNetwork.getFlights()) {
                System.out.println(flight.getName() + " " + 
                        TimeUtil.getFormatedTime(flight.getDepartureTime()) + " " +
                        TimeUtil.getFormatedTime(flight.getArrivalTime()) + " " +
                        flight.getDepartureCity().getName() + " " +
                        flight.getArrivalCity().getName()) ;
            }




    }

}
