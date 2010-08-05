/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import main.heuristic.ARPParameters;
import main.heuristic.GRASPParameters;
import util.VersionManager;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
@Entity
public class HeuristicInformation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private long seed;
    private Integer version;

    @Lob 
    private String observation;
    private Integer duration;
    private Integer bestValue;
    private String pathInstance;

    @OneToOne(cascade=CascadeType.PERSIST)
    private ARPParameters aRPParameters;

    @OneToOne(cascade=CascadeType.PERSIST)
    private GRASPParameters gRASPParameters;

    public HeuristicInformation() {
        initData();
    }

    public HeuristicInformation(GRASPParameters gRASPParameters, ARPParameters aRPParameters, String pathInstance) {
        initData();
        this.gRASPParameters = gRASPParameters;
        this.gRASPParameters.setId(null);
        this.aRPParameters = aRPParameters;
        this.aRPParameters.setId(null);
        this.pathInstance = pathInstance;
    }

    private void initData(){
        this.seed = System.currentTimeMillis();
        this.version = VersionManager.currentVersion;
        this.observation = VersionManager.observation;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public Integer getBestValue() {
        return bestValue;
    }

    public void setBestValue(Integer bestValue) {
        this.bestValue = bestValue;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ARPParameters getaRPParameters() {
        return aRPParameters;
    }

    public void setaRPParameters(ARPParameters aRPParameters) {
        this.aRPParameters = aRPParameters;
    }

    public GRASPParameters getgRASPParameters() {
        return gRASPParameters;
    }

    public void setgRASPParameters(GRASPParameters gRASPParameters) {
        this.gRASPParameters = gRASPParameters;
    }

    public String getPathInstance() {
        return pathInstance;
    }

    public void setPathInstance(String pathInstance) {
        this.pathInstance = pathInstance;
    }

    @Override
    public int hashCode() {
        return new Long(seed).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (((HeuristicInformation) object).getSeed() == getSeed()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "database.entity.HeuristicInformation[id=" + seed + "]";
    }
}
