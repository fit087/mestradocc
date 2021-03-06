/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.heuristic;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import main.heuristic.exceptions.ParameterInvalidException;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */

@Entity
public class ARPParameters implements Serializable {

    //public transient final static ARPParameters defaultParameters = new ARPParameters(15, 53, 35, 10, 2);
    public transient final static ARPParameters defaultParameters = new ARPParameters(10, 5, 9*60, 40, 53, 35, 10, 2);

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    private int probabilityType1Arc;

    private int probabilityType2Arc;

    private int probabilityType3Arc;
    
    private int probabilityType4Arc;

    private int maximumDelay;
    
    private int crewChangeTime;
    
    private int maxCrewFlights;
    
    private int maxCrewFlightTime;

    public ARPParameters() {
    }

    /**
     * Parametros do aircraft rotation.
     *
     * <ul>
     *  <li> <b> Arco Tipo 1: Não permite nem atraso nem reposicionamento.</li>
     *  <li> <b> Arco Tipo 2: Insere atraso no vôo mas não aceita reposicionamento.</li>
     *  <li> <b> Arco Tipo 3: Insere um vôo de reposicionamento mas não permite atraso.</li>
     *  <li> <b> Arco Tipo 4: Insere atraso no vôo e cria um vôo de reposicionamento.  </li>
     * </ul>
     *
     * A soma dessas probabilidades devem ser igual a 100.
     *
     * @param maximumDelay Atraso máximo permitido em um vôo.
     * @param probabilityType1Arc 
     * @param probabilityType2Arc
     * @param probabilityType3Arc
     * @param probabilityType4Arc
     * @throws ParameterInvalidException
     */
    public ARPParameters(int maximumDelay, int crewMaxFlights, int maxCrewFlightTime, int crewChangeTime, int probabilityType1Arc, int probabilityType2Arc, int probabilityType3Arc, int probabilityType4Arc) throws ParameterInvalidException {

        if((probabilityType1Arc + probabilityType2Arc + probabilityType3Arc + probabilityType4Arc) != 100){
            throw new ParameterInvalidException("A soma das probabilidades dos 4 arcos devem ser igual a 100");
        }

        this.probabilityType1Arc = probabilityType1Arc;
        this.probabilityType2Arc = probabilityType2Arc;
        this.probabilityType3Arc = probabilityType3Arc;
        this.probabilityType4Arc = probabilityType4Arc;
        this.maximumDelay = maximumDelay;
        this.crewChangeTime = crewChangeTime;
        this.maxCrewFlightTime = maxCrewFlightTime;
        this.maxCrewFlights = crewMaxFlights;

    }
    
    public int getMaximumDelay() {
        return maximumDelay;
    }

    public void setMaximumDelay(int maximumDelay) {
        this.maximumDelay = maximumDelay;
    }

    public int getMaxCrewFlightTime() {
        return maxCrewFlightTime;
    }

    public void setMaxCrewFlightTime(int maxCrewFlightTime) {
        this.maxCrewFlightTime = maxCrewFlightTime;
    }
    
    public int getMaxCrewFlights() {
        return maxCrewFlights;
    }

    public void setMaxCrewFlights(int maxCrewFlights) {
        this.maxCrewFlights = maxCrewFlights;
    }
    
    public int getCrewChangeTime() {
        return crewChangeTime;
    }

    public void setCrewChangeTime(int crewChangeTime) {
        this.crewChangeTime = crewChangeTime;
    }
    
    public int getProbabilityType1Arc() {
        return probabilityType1Arc;
    }

    public void setProbabilityType1Arc(int probabilityType1Arc) {
        this.probabilityType1Arc = probabilityType1Arc;
    }

    public int getProbabilityType2Arc() {
        return probabilityType2Arc;
    }

    public void setProbabilityType2Arc(int probabilityType2Arc) {
        this.probabilityType2Arc = probabilityType2Arc;
    }

    public int getProbabilityType3Arc() {
        return probabilityType3Arc;
    }

    public void setProbabilityType3Arc(int probabilityType3Arc) {
        this.probabilityType3Arc = probabilityType3Arc;
    }

    public int getProbabilityType4Arc() {
        return probabilityType4Arc;
    }

    public void setProbabilityType4Arc(int probabilityType4Arc) {
        this.probabilityType4Arc = probabilityType4Arc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
