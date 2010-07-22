/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;
import database.HeuristicInfoJpaController;
import statistic.HeuristicInfo;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class Main {

    public static void main(String [] args) {
//        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
//        String strURL = "jdbc:derby:MyDB;create=true;user=test;password=1234";
//
//        Connection connection = DriverManager.getConnection(strURL);

        HeuristicInfo heuristicInfo = new HeuristicInfo(100);
        heuristicInfo.setObservation("teste3");
//


        HeuristicInfoJpaController hijc = new HeuristicInfoJpaController();
//        hijc.create(heuristicInfo);

        
        System.out.println("Size " + hijc.findHeuristicInfoEntities().size());
        for (HeuristicInfo heuristicInfo1 : hijc.findHeuristicInfoEntities()) {
            System.out.println("Teste >>>>> " + heuristicInfo1.getObservation());
        }

        
    }

}
