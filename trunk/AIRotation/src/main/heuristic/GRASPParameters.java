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
public class GRASPParameters implements Serializable {

        public transient final static GRASPParameters defaultParameters = new GRASPParameters(1000, 1000, 0.5f);
        
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Integer numberOfConstructions;

    private Integer numberOfRepetitions;

    private Float alfa; //Grau de aleatóriedade da fase de construção.

    public GRASPParameters() {
    }

    /**
     * Define os parametros do GRASP
     * @param numberOfConstructions Numero de construções.
     * @param numberOfRepetitions Número de repetições.
     * @param alfa  Gráu de Aleatoriedade da solução (0.0f - 1.0f]
     */
    public GRASPParameters(Integer numberOfConstructions, Integer numberOfRepetitions, Float alfa) throws ParameterInvalidException {
        this.numberOfConstructions = numberOfConstructions;
        this.numberOfRepetitions = numberOfRepetitions;

        if(alfa <= 0.0f || alfa > 1.0f){
            throw new ParameterInvalidException("O valor de alfa deve estar no intervalo (0.0f - 1.0f]");
        }
        
        this.alfa = alfa;
    }

    public Float getAlfa() {
        return alfa;
    }

    public void setAlfa(Float alfa) {
        this.alfa = alfa;
    }

    public Integer getNumberOfConstructions() {
        return numberOfConstructions;
    }

    public void setNumberOfConstructions(Integer numberOfConstructions) {
        this.numberOfConstructions = numberOfConstructions;
    }

    public Integer getNumberOfRepetitions() {
        return numberOfRepetitions;
    }

    public void setNumberOfRepetitions(Integer numberOfRepetitions) {
        this.numberOfRepetitions = numberOfRepetitions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
