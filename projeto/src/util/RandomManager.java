/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.util.Random;

/**
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class RandomManager {

    public static final long seed = System.currentTimeMillis();
    
    private static Random random = new Random(seed);

    /**
     * Retorna uma valor na faixa especificada.
     * O valor max deve ser maior ou igual ao min com a finalidade de
     * melhorar a performance essa verificação não é feita no código.
     * @param min Menor valor aceitável (inclusive)
     * @param max Maior valor aceitável (inclusive)
     * @return
     */
    public static int getNext(int min, int max){
        
        int diff = max - min;
        return random.nextInt(diff+1) + min;

    }

    /**
     * Obtem um valor aleatório variando de 0 até n-1;
     * @param n
     * @return
     */
    public static int getNext(int n){
        return random.nextInt(n);
    }

}
