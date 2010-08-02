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
public class ARPTooltipGenerator extends StandardCategoryToolTipGenerator {

    public ARPTooltipGenerator() {
        super();
    }

    @Override
    public String generateToolTip(CategoryDataset dataset, int row, int column) {
        ARPGanttCategoryDataset aircraftGanttCategoryDataset = (ARPGanttCategoryDataset) dataset;
        return super.generateToolTip(dataset, row, column);
    }

}
