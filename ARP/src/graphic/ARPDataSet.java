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
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.data.time.TimePeriod;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class ARPDataSet extends TaskSeriesCollection {

    public static ARPDataSet configure(AirlineNetwork airlineNetwork){

        ARPDataSet aircraftDataSet = new ARPDataSet(airlineNetwork);

        for (int i = 0; i < airlineNetwork.getBestNetwork().size(); i++) {
            Rail rail = airlineNetwork.getBestNetwork().get(i);
            aircraftDataSet.createNewRail(rail);

            for(int j = 0; j < rail.getFlights().size(); j++){
                aircraftDataSet.addNewFlight(rail.getFlights().get(j), i);
            }
        }

        return aircraftDataSet;
    }

    private TaskSeries taskSeries = new TaskSeries("Aircraft");
    private Long lowTime;
    private Long highTime;
    private Long minTomili;
    private Long baseTime;
    private TimePeriod maxTimePeriod;

    public ARPDataSet(AirlineNetwork airlineNetwork) {
        this.baseTime = airlineNetwork.getAirlineGraphicConfigs().getBaseTime();
        this.minTomili = 60 * 1000l;
        this.lowTime = baseTime + (Long) ((airlineNetwork.getLowTime() - 80) * minTomili);
        this.highTime = baseTime + (Long) ((airlineNetwork.getHighTime() + 80) * minTomili);
        this.maxTimePeriod = new SimpleTimePeriod(lowTime, highTime);
        add(taskSeries);
    }

    public void createNewRail(Rail rail) {
        taskSeries.add(new Task(String.format(rail.getName(), (taskSeries.getTasks().size() + 1)), maxTimePeriod));
    }

    public void addNewFlight(Flight flight, int row){
        long realDepartureTimeWithGround = (flight.getRealDepartureTime() - flight.getGroundTime())*minTomili;
        long realArrivalTime = flight.getRealArrivalTime()*minTomili;
        Task subTask = new Task(flight.getName(), new Date(baseTime + realDepartureTimeWithGround), new Date(baseTime + realArrivalTime));
        
        ((Task)taskSeries.getTasks().get(row)).addSubtask(subTask);
    }

}
