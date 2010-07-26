/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package graphic;

import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.data.category.CategoryDataset;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class AircraftTooltipGenerator extends StandardCategoryToolTipGenerator {

    public AircraftTooltipGenerator() {
        super();
    }

    @Override
    public String generateToolTip(CategoryDataset dataset, int row, int column) {
        AircraftGanttCategoryDataset aircraftGanttCategoryDataset = (AircraftGanttCategoryDataset) dataset;
        return super.generateToolTip(dataset, row, column);
    }

}
