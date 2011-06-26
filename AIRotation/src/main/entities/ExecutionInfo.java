/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.entities;

/**
 *
 * @author alexanderdealmeidapinto
 */
public class ExecutionInfo {

    private String instanceName;
    private Integer numberOfFlights;
    private Integer objectiveFunctionValue;
    private Long executionTime;
    private Integer numberOfTracks;
    private Integer totalDelayed;
    private Integer maxDelayed;
    private Integer numberOfDelayedFlights;
    private Integer numberOfRepositions;

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Integer getMaxDelayed() {
        return maxDelayed;
    }

    public void setMaxDelayed(Integer maxDelayed) {
        this.maxDelayed = maxDelayed;
    }

    public Integer getNumberOfDelayedFlights() {
        return numberOfDelayedFlights;
    }

    public void setNumberOfDelayedFlights(Integer numberOfDelayedFlights) {
        this.numberOfDelayedFlights = numberOfDelayedFlights;
    }

    public Integer getNumberOfFlights() {
        return numberOfFlights;
    }

    public void setNumberOfFlights(Integer numberOfFlights) {
        this.numberOfFlights = numberOfFlights;
    }

    public Integer getNumberOfRepositions() {
        return numberOfRepositions;
    }

    public void setNumberOfRepositions(Integer numberOfRepositions) {
        this.numberOfRepositions = numberOfRepositions;
    }

    public Integer getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(Integer numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public Integer getObjectiveFunctionValue() {
        return objectiveFunctionValue;
    }

    public void setObjectiveFunctionValue(Integer objectiveFunctionValue) {
        this.objectiveFunctionValue = objectiveFunctionValue;
    }

    public Integer getTotalDelayed() {
        return totalDelayed;
    }

    public void setTotalDelayed(Integer totalDelayed) {
        this.totalDelayed = totalDelayed;
    }

    public void configure(AirlineNetwork airlineNetwork, Long executionTime){
        this.instanceName = airlineNetwork.getPathInstance();
        this.numberOfFlights = airlineNetwork.getFlights().size();
        this.objectiveFunctionValue = AirlineNetwork.getTotalCost(airlineNetwork.getBestNetwork());
        this.executionTime = executionTime;
        this.numberOfTracks = airlineNetwork.getBestNetwork().size();
        this.totalDelayed = AirlineNetwork.getTotalDelay(airlineNetwork.getBestNetwork());
        this.maxDelayed = AirlineNetwork.getMaxDelay(airlineNetwork.getBestNetwork());
        this.numberOfDelayedFlights = AirlineNetwork.getNumberOfDelayedFlights(airlineNetwork.getBestNetwork());
        this.numberOfRepositions = AirlineNetwork.getNuberOfRepositions(airlineNetwork.getBestNetwork());
    }
    
}
