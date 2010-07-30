/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import main.entities.AirlineNetwork;
import main.entities.Flight;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.renderer.category.GradientBarPainter;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.ui.RectangleEdge;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class AircraftGanttRenderer extends GanttRenderer {

    private static Color flightColorBegin = new Color(0, 56, 186); //Dark Azure ;
    private static Color flightColorBeginDelayed = Color.RED;
    private static Color flightColorEnd = new Color(0, 0, 128);//Navi
    private static Paint groundColor = new Color(255, 186, 0); //Selective Yellow;
    private static Paint barStrokeColor = Color.BLACK;
    private static Stroke barStroke = new BasicStroke(1.0f);
    private final AirlineNetwork airlineNetwork;
    private final AircraftGanttCategoryDataset aircraftGanttCategoryDataset;

    public AircraftGanttRenderer(AirlineNetwork airlineNetwork, AircraftGanttCategoryDataset aircraftGanttCategoryDataset) {
        super();
        this.airlineNetwork = airlineNetwork;
        this.aircraftGanttCategoryDataset = aircraftGanttCategoryDataset;



    }

    @Override
    protected void drawTasks(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, GanttCategoryDataset dataset, int row, int column) {
        int count = dataset.getSubIntervalCount(row, column);
        if (count == 0) {
            drawTask(g2, state, dataArea, plot, domainAxis, rangeAxis,
                    dataset, row, column);
        }

        PlotOrientation orientation = plot.getOrientation();
        for (int subinterval = 0; subinterval < count; subinterval++) {

            int firstCategoryIndex = aircraftGanttCategoryDataset.getFirstCategoryIndex();
            Flight flight = airlineNetwork.getBestNetwork().get(column + firstCategoryIndex).getFlights().get(subinterval);

            RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();

            // value 0
            Number value0 = dataset.getStartValue(row, column, subinterval);
            if (value0 == null) {
                return;
            }
            Number newNumber = value0.longValue() + flight.getGroundTime() * 60 * 1000;
            double translatedValue0 = rangeAxis.valueToJava2D(
                    value0.doubleValue(), dataArea, rangeAxisLocation);

            // value 1
            Number value1 = dataset.getEndValue(row, column, subinterval);

            if (value1 == null) {
                return;
            }
            double translatedValue1 = rangeAxis.valueToJava2D(
                    value1.doubleValue(), dataArea, rangeAxisLocation);

            double translatedValue2 = rangeAxis.valueToJava2D(newNumber.doubleValue(), dataArea, rangeAxisLocation);

            if (translatedValue1 < translatedValue0) {
                double temp = translatedValue1;
                translatedValue1 = translatedValue0;
                translatedValue0 = temp;
            }

            double rectStart = calculateBarW0(plot, plot.getOrientation(),
                    dataArea, domainAxis, state, row, column);
            double rectLength = Math.abs(translatedValue1 - translatedValue0);
            double groundLength = Math.abs(translatedValue2 - translatedValue0);
            double rectBreadth = state.getBarWidth();

            // DRAW THE BARS...
            Rectangle2D bar = null;
            RectangleEdge barBase = null;
            if (plot.getOrientation() == PlotOrientation.HORIZONTAL) {
                bar = new Rectangle2D.Double(translatedValue0, rectStart,
                        rectLength, rectBreadth);
                barBase = RectangleEdge.LEFT;
            } else if (plot.getOrientation() == PlotOrientation.VERTICAL) {
                bar = new Rectangle2D.Double(rectStart, translatedValue0,
                        rectBreadth, rectLength);
                barBase = RectangleEdge.BOTTOM;
            }



//            bar.setRect(bar.getMinX(), bar.getMinY() + 10, bar.getWidth(), bar.getHeight() - 10);
//
//            if (getShadowsVisible()) {
//                //System.out.println("Teste " + getBarPainter().getClass());
//                //getBarPainter().paintBarShadow(g2, this, row, column, bar,
//                //      barBase, true);
//                g2.setPaint(Color.GRAY);
//                Rectangle2D shadowBar = new Rectangle2D.Double(bar.getX() + 5, bar.getY() + 5, bar.getWidth(), bar.getHeight());
//                g2.fill(shadowBar);
//            }
//
//            
//
//            if (flight.getDelay() != 0) {
//                g2.setPaint(new GradientPaint(midX, (float) bar.getMinY(), Color.RED, midX, (float) bar.getMaxY(), flightColorEnd));
//            } else {
//                g2.setPaint(new GradientPaint(midX, (float) bar.getMinY(), flightColorBegin, midX, (float) bar.getMaxY(), flightColorEnd));
//            }
//
//            g2.fill(bar);
//
//            Rectangle2D groundBar = new Rectangle2D.Double(bar.getX(), bar.getY(), groundLength, bar.getHeight());
//            g2.setPaint(groundColor);
//            g2.fill(groundBar);
//
//            g2.setPaint(barStrokeColor);
//            g2.setStroke(barStroke);
//            g2.draw(bar);



            boolean showCities = true;

            AircraftGanttRenderer.drawFlight(g2, bar, groundLength, true, showCities, (subinterval == count - 1), flight);



            if (subinterval == count - 1) {



                // submit the current data point as a crosshair candidate
                int datasetIndex = plot.indexOf(dataset);
                Comparable columnKey = dataset.getColumnKey(column);
                Comparable rowKey = dataset.getRowKey(row);
                double xx = domainAxis.getCategorySeriesMiddle(columnKey,
                        rowKey, dataset, getItemMargin(), dataArea,
                        plot.getDomainAxisEdge());
                updateCrosshairValues(state.getCrosshairState(),
                        dataset.getRowKey(row), dataset.getColumnKey(column),
                        value1.doubleValue(), datasetIndex, xx,
                        translatedValue1, orientation);

            }
            // collect entity and tool tip information...
            if (state.getInfo() != null) {
                EntityCollection entities = state.getEntityCollection();
                if (entities != null) {
                    addItemEntity(entities, dataset, row, column, bar);
                }
            }
        }
    }

    public static void drawFlight(Graphics2D g2, Rectangle2D bar, double groundLength, boolean shadowsVisible, boolean showCities, boolean lastFlight, Flight flight) {
        bar.setRect(bar.getMinX(), bar.getMinY() + 10, bar.getWidth(), bar.getHeight() - 10);

        if (shadowsVisible) {
            g2.setPaint(Color.GRAY);
            Rectangle2D shadowBar = new Rectangle2D.Double(bar.getX() + 5, bar.getY() + 5, bar.getWidth(), bar.getHeight());
            g2.fill(shadowBar);
        }

        float midX = (float) ((bar.getMinX() + bar.getMaxX()) / 2.0);
        float midY = (float) ((bar.getMinY() + bar.getMaxY()) / 2.0);

        if (flight.getDelay() != 0) {
            g2.setPaint(new GradientPaint(midX, (float) bar.getMinY(), flightColorBeginDelayed, midX, (float) bar.getMaxY(), flightColorEnd));
        } else {
            g2.setPaint(new GradientPaint(midX, (float) bar.getMinY(), flightColorBegin, midX, (float) bar.getMaxY(), flightColorEnd));
        }

        g2.fill(bar);

        Rectangle2D groundBar = new Rectangle2D.Double(bar.getX(), bar.getY(), groundLength, bar.getHeight());
        g2.setPaint(groundColor);
        g2.fill(groundBar);

        g2.setPaint(barStrokeColor);
        g2.setStroke(barStroke);
        g2.draw(bar);

        if (showCities) {
            g2.setPaint(Color.black);
            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
            g2.drawString(flight.getDepartureCity().getName(), (float) bar.getMinX() - 15, (float) bar.getMinY() - 1);
            
            g2.setPaint(Color.WHITE);
            g2.drawString(flight.getName(), midX, midY + 5);
            
            if (lastFlight) {
                g2.setPaint(Color.black);
                g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 11));
                g2.drawString(flight.getArrivalCity().getName(), (float) bar.getMaxX() - 15, (float) bar.getMinY() - 1);
            }
        }

    }

    public static Stroke getBarStroke() {
        return barStroke;
    }

    public static void setBarStroke(Stroke barStroke) {
        AircraftGanttRenderer.barStroke = barStroke;
    }

    public static Paint getBarStrokeColor() {
        return barStrokeColor;
    }

    public static void setBarStrokeColor(Paint barStrokeColor) {
        AircraftGanttRenderer.barStrokeColor = barStrokeColor;
    }

    public static Color getFlightColorBegin() {
        return flightColorBegin;
    }

    public static void setFlightColorBegin(Color flightColorBegin) {
        AircraftGanttRenderer.flightColorBegin = flightColorBegin;
    }

    public static Color getFlightColorEnd() {
        return flightColorEnd;
    }

    public static void setFlightColorEnd(Color flightColorEnd) {
        AircraftGanttRenderer.flightColorEnd = flightColorEnd;
    }

    public static Paint getGroundColor() {
        return groundColor;
    }

    public static void setGroundColor(Paint groundColor) {
        AircraftGanttRenderer.groundColor = groundColor;
    }

    public static Color getFlightColorBeginDelayed() {
        return flightColorBeginDelayed;
    }

    public static void setFlightColorBeginDelayed(Color flightColorBeginDelayed) {
        AircraftGanttRenderer.flightColorBeginDelayed = flightColorBeginDelayed;
    }

    
}
