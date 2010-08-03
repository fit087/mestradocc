/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package statistic;

import main.heuristic.ARPParameters;
import main.heuristic.GRASPParameters;
import util.RandomManager;
import util.VersionManager;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */

public class HeuristicInfo {

    
    // Versao do algoritmo.
    private int version;

    //Semente utilizada na randomização dos números.
    private long seed;

    //Tempo de execução do algoritmo.
    private long duration;

    //Parametros do ARP que foram utilizados
   // @OneToOne
    private ARPParameters aRPParameters;

    //Parametros do GRASP que foram utilizados
   // @OneToOne
    private GRASPParameters gRASPParameters;

    //Melhor valor obtido.
    private int bestValue;

    //Observacoes sobre o que esta implementado
    private String observation;

    public HeuristicInfo() {
        initialize();
    }

    public HeuristicInfo(long duration, ARPParameters aRPParameters, GRASPParameters gRASPParameters, int bestValue, String observation) {
        this.duration = duration;
        this.aRPParameters = aRPParameters;
        this.gRASPParameters = gRASPParameters;
        this.bestValue = bestValue;
        this.observation = observation;
        initialize();
    }

    private void initialize(){
        this.version = VersionManager.currentVersion;
        this.seed = RandomManager.seed;
    }

    public ARPParameters getaRPParameters() {
        return aRPParameters;
    }

    public void setaRPParameters(ARPParameters aRPParameters) {
        this.aRPParameters = aRPParameters;
    }

    public int getBestValue() {
        return bestValue;
    }

    public void setBestValue(int bestValue) {
        this.bestValue = bestValue;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public GRASPParameters getgRASPParameters() {
        return gRASPParameters;
    }

    public void setgRASPParameters(GRASPParameters gRASPParameters) {
        this.gRASPParameters = gRASPParameters;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

}
