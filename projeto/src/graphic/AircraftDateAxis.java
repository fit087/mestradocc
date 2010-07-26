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
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class AircraftDateAxis extends DateAxis {

    public AircraftDateAxis(String label) {
        super(label);
    }

    public void incrementRange(long increment) {
        Range range = getRange();
        super.setRange(new DateRange(range.getLowerBound() + increment, range.getUpperBound() + increment));
    }

    public void decrementRange(long increment) {
        Range range = getRange();
        super.setRange(new DateRange(range.getLowerBound() - increment, range.getUpperBound() - increment));
    }
}
