/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package graphic;

import java.awt.Font;
import java.util.GregorianCalendar;

/**
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class ARPGraphicConfigs {

    public static final ARPGraphicConfigs defaultGraphicConfig = new ARPGraphicConfigs(
            new GregorianCalendar(2010, GregorianCalendar.JULY, 30, 0, 0, 0).getTimeInMillis()
            , 0l , 24*60*60*1000l, 24*60*60*1000l, 60*60*1000l, 2*60*60*1000l);

    private Long baseTime;

    private Long lowTime;

    private Long highTime;

    private Long visibleRange;

    private Long rangeIncrement;

    private Long zoomIncrement;

    /**
     * Armazena as configurações do gráfico referentes a Escala do ARP.
     *
     * @param baseTime Horario base do grafico.
     * @param lowTime Menor tempo que pode ser exibido no gráfico (deve ser menor ou igual ao menor tempo dos voos cadastrados)
     * @param highTime Maior tempo que pode ser exibido no gráfico (deve ser maior ou igual ao maior tempo dos voos cadastrados)
     * @param visibleRange O range que deseja ser visível no gráfico.
     * @param rangeIncrement O incremento que se deseja ter ao avançar ou retroceder no gráfico.
     */
    public ARPGraphicConfigs(Long baseTime, Long lowTime, Long highTime, Long visibleRange, Long rangeIncrement, Long zoomIncrement) {
        this.baseTime = baseTime;
        this.lowTime = lowTime;
        this.highTime = highTime;
        this.visibleRange = visibleRange;
        this.rangeIncrement = rangeIncrement;
        this.zoomIncrement = zoomIncrement;
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

    public Long getVisibleRange() {
        return visibleRange;
    }

    public void setVisibleRange(Long range) {
        this.visibleRange = range;
    }

    public long getRangeIncrement() {
        return this.rangeIncrement;
    }

    public void setRangeIncrement(Long rangeIncrement) {
        this.rangeIncrement = rangeIncrement;
    }

    public Long getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(Long baseTime) {
        this.baseTime = baseTime;
    }

    public Long getZoomIncrement() {
        return zoomIncrement;
    }

    public void setZoomIncrement(Long zoomIncrement) {
        this.zoomIncrement = zoomIncrement;
    }

    public Font getReduzedFont() {
        return new Font(Font.SANS_SERIF, Font.BOLD, 9);
    }

}
