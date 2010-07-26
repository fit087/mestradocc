/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package graphic;

import java.util.GregorianCalendar;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class GraphicConfigs {

    public static final GraphicConfigs defaultGraphicConfig = new GraphicConfigs(
            new GregorianCalendar(2010, GregorianCalendar.JULY, 20, 0, 0, 0).getTimeInMillis()
            , new GregorianCalendar(2010, GregorianCalendar.JULY, 20, 23, 59, 59).getTimeInMillis()
            , 24*60*60*1000l, 60*60*1000l);

    public Long lowTime;

    public Long highTime;

    public Long range;

    private Long rangeIncrement;

    public GraphicConfigs(Long lowTime, Long highTime, Long range, Long rangeIncrement) {
        this.lowTime = lowTime;
        this.highTime = highTime;
        this.range = range;
        this.rangeIncrement = rangeIncrement;
    }

    public Long getHighTime() {
        return highTime;
    }

    public void setHighTime(Long highTime) {
        this.highTime = highTime;
    }

    public Long getLowTime() {
        return lowTime;
    }

    public void setLowTime(Long lowTime) {
        this.lowTime = lowTime;
    }

    public Long getRange() {
        return range;
    }

    public void setRange(Long range) {
        this.range = range;
    }

    public long getRangeIncrement() {
        return this.rangeIncrement;
    }

    public void setRangeIncrement(Long rangeIncrement) {
        this.rangeIncrement = rangeIncrement;
    }
    
}
