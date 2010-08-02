/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.heuristic;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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

    //Identificacao
    @Id
    @GeneratedValue
    private Long id;

    public final static ARPParameters defaultParameters = new ARPParameters(10, 53, 35, 10, 2);

    private int probabilityType1Arc;

    private int probabilityType2Arc;

    private int probabilityType3Arc;
    
    private int probabilityType4Arc;

    private int maximumDelay;

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
    public ARPParameters(int maximumDelay, int probabilityType1Arc, int probabilityType2Arc, int probabilityType3Arc, int probabilityType4Arc) throws ParameterInvalidException {

        if((probabilityType1Arc + probabilityType2Arc + probabilityType3Arc + probabilityType4Arc) != 100){
            throw new ParameterInvalidException("A soma das probabilidades dos 4 arcos devem ser igual a 100");
        }

        this.probabilityType1Arc = probabilityType1Arc;
        this.probabilityType2Arc = probabilityType2Arc;
        this.probabilityType3Arc = probabilityType3Arc;
        this.probabilityType4Arc = probabilityType4Arc;
        this.maximumDelay = maximumDelay;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getMaximumDelay() {
        return maximumDelay;
    }

    public void setMaximumDelay(int maximumDelay) {
        this.maximumDelay = maximumDelay;
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

}
