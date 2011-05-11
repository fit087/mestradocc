/* 
 * File:   ARPUtil.h
 * Author: alexanderdealmeidapinto
 *
 * Created on 19 de Abril de 2011, 14:17
 */

#ifndef ARPUTIL_H
#define	ARPUTIL_H

#include <iostream>
#include <vector>
#include <map>
#include "../Instance.h"
#include "../Flight.h"

using namespace std;

class ARPUtil {
public:
    static bool validateSolution(vector< vector<Flight> > &solution);

    /**
     * Configura um trilho, inserindo atrasos e voos de reposicionamento se necessário.
     * 
     * @param track Trilho a ser configurado
     * @return O custo do trilho.
     */
    static int configureTrack(vector<Flight> &track, Instance *instance);
    static int configureSolution(vector< vector<Flight> > &solution, Instance *instance);
    static void showSolution(vector< vector<Flight> > &solution);
    static int calculeCost(vector< vector<Flight> > &r, Instance *instance);
    static int calculeCost(vector<Flight> &track, Instance *instance);
    static void relaxDelays(vector< vector<Flight> > &r);
    static void removeTrack(vector< vector<Flight> > *flights, int track);
    static void copyFlights(vector<Flight> *target, vector<Flight> *source);
private:

};

#endif	/* ARPUTIL_H */

