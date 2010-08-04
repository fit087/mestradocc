/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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

    public HeuristicInformation() {
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
