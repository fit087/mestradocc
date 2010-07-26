/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package statistic;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import util.RandomManager;
import util.VersionManager;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */

@Entity
public class HeuristicInfo implements Serializable {

    //Identificacao
    @Id
    @GeneratedValue
    private Long id;

    // Versao do algoritmo.
    private int version;

    //Horario em que foi executado o algoritmo.
    private long executeTime;

    //Semente utilizada na randomização dos números.
    private long seed;

    //Numero de construções GRASP efetuadas.
    private int numberOfConstructions;

    //Tempo de execução do algoritmo.
    private int duracao;

    //Observacoes sobre o que esta implementado
    private String observation;

    public HeuristicInfo() {
        initialize();
    }

    public HeuristicInfo(int duracao) {
        initialize();
        this.duracao = duracao;
    }

    private void initialize(){
        this.version = VersionManager.currentVersion;
        this.executeTime = System.currentTimeMillis();
        this.seed = RandomManager.seed;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(long executeTime) {
        this.executeTime = executeTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberOfConstructions() {
        return numberOfConstructions;
    }

    public void setNumberOfConstructions(int numberOfConstructions) {
        this.numberOfConstructions = numberOfConstructions;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    

    

}
