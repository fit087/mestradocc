/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import main.entities.AirlineNetwork;
import main.entities.Flight;
import main.entities.Rail;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class DataSetGenerator {

    

    public static TaskSeriesCollection getDataSet(AirlineNetwork airlineNetwork) {
        TaskSeriesCollection tsc = new TaskSeriesCollection();

        TaskSeries taskSeries = new TaskSeries("Aircraft");

        ArrayList<Rail> rails = airlineNetwork.getBestNetwork();

        Long baseTime = airlineNetwork.getAirlineGraphicConfigs().getBaseTime();

        long minTomili = 60*1000;

        Long lowTime = baseTime + (Long) ((airlineNetwork.getLowTime() - 60) * minTomili);
        Long highTime = baseTime + (Long) ((airlineNetwork.getHighTime() + 60) * minTomili);

        for (int i = 0; i < rails.size(); i++) {
            Rail rail = rails.get(i);
            Task task = new Task(String.format("Voo[%2s]", (i+1)), new Date(lowTime), new Date(highTime));

            for (int j = 0; j < rail.getFlights().size(); j++) {
                Flight flight = rail.getFlights().get(j);
                long departureTimeWithGround = (flight.getRealDepartureTime() - flight.getGroundTime()) * minTomili;
                Task subTask = new Task(flight.getName(), new Date(baseTime + departureTimeWithGround), new Date(baseTime + flight.getRealArrivalTime()*minTomili));
                subTask.setPercentComplete(0.1);
                task.addSubtask(subTask);
            }

            taskSeries.add(task);
        }
        tsc.add(taskSeries);

        return tsc;
    }
}
