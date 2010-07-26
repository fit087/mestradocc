/**
 * Responsável por mostrar logs durante a execucao.
 */

package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe para escrever na saída padrão.
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class LogManager {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm:ss a");

    /**
     * Escreve a msg na saida padrão com o horário da sua ocorrencia lhe precedendo.
     * @param msg
     */
    public static void writeMsg(String msg){
        System.out.printf("[%s]: %s\n", simpleDateFormat.format(new Date(System.currentTimeMillis())), msg);
    }
    
}
