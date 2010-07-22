/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.heuristic.exceptions;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class ParameterInvalidException extends RuntimeException{

    public ParameterInvalidException(String message) {
       super(message);
    }

}
