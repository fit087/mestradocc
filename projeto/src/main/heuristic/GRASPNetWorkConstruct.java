/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.heuristic;

import main.entities.AirlineNetwork;

/**
 *
 * Responsável por construir uma malha de uma determinada frota,
 * especificando a quantidade minima "conseguida" para cobrir os seus voos,
 * o seu custo operacional e qual a sequência de vôos de cada aeronave.
 *
 * Desenvolvido por: Alexander de Almeida Pinto
 *
 * @author alexanderdealmeidapinto
 */
public class GRASPNetWorkConstruct {
    
    private Boolean resolved = false;

    private AirlineNetwork airlineNetwork;

    private GRASPParameters gRASPParameters;

    /**
     * Constroi uma malha
     * @param airlineNetwork Dados do problema.
     * @param gRASPParameters Parametros do GRASPl
     */
    public GRASPNetWorkConstruct(AirlineNetwork airlineNetwork, GRASPParameters gRASPParameters) {
        this.airlineNetwork = airlineNetwork;
        this.gRASPParameters = gRASPParameters;
        this.resolved = false;
    }

    public void resolve(){
        
    }

    /**
     * Informa se a malha já foi montada.
     * @return
     */
    public Boolean getResolved() {
        return resolved;
    }
    
    
}
