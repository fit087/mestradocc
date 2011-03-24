/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 *
 * @author alexanderdealmeidapinto
 */
public class TimeUtil {

    public static String getFormatedTime(int minuteTime){
        int days = minuteTime/(24*60);
        minuteTime %= (24*60);
        int hours = minuteTime/60;
        int minutes = minuteTime%60;

        return days + " " + getTimeWith2Character(hours) + ":" + getTimeWith2Character(minutes);
    }

    private static String getTimeWith2Character(int time){
        if(time < 10){
            return "0" + time;
        }

        return time + "";
    }

}
