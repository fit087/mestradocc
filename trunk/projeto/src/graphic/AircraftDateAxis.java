/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import org.jfree.chart.axis.DateAxis;
import org.jfree.data.Range;
import org.jfree.data.time.DateRange;

/**
 *
 * DateAxis que permite "andar" no gráfico incrementando ou
 * decrementando o range.
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class AircraftDateAxis extends DateAxis {

    public AircraftDateAxis(String label) {
        super(label);
    }

    /**
     * Incrementa o range, avançando o gráfico no tempo
     * @param increment
     */
    public void incrementRange(long increment) {
        Range range = getRange();
        super.setRange(new DateRange(range.getLowerBound() + increment, range.getUpperBound() + increment));
    }

    /**
     * Decrementa o range, voltando o gráfico no tempo.
     * @param increment
     */
    public void decrementRange(long increment) {
        Range range = getRange();
        super.setRange(new DateRange(range.getLowerBound() - increment, range.getUpperBound() - increment));
    }

    /**
     * Aplica um zoom in do tempo passado.
     * @param increment
     */
    public void zoomOut(long increment){
        Range range = getRange();
        long inc = increment/2;
        super.setRange(new DateRange(range.getLowerBound() - inc, range.getUpperBound() + inc));
    }

    /**
     * Aplica um zoom out do tempo passado.
     * @param increment
     */
    public void zoomIn(long increment){
        Range range = getRange();
        long inc = increment/2;
        super.setRange(new DateRange(range.getLowerBound() + inc, range.getUpperBound() - inc));
    }
}
