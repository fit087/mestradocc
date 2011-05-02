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
#include "../Flight.h"

using namespace std;

class ARPUtil {
public:
    static bool validateSolution(vector< vector<Flight> > &solution);
    static void showSolution(vector< vector<Flight> > &solution);
    static int calculeCost(vector< vector<Flight> > &r);
    static void relaxDelays(vector< vector<Flight> > &r);
    static void removeTrack(vector< vector<Flight> > *flights, int track);
    static void copyFlights(vector<Flight> *target, vector<Flight> *source);
private:

};

#endif	/* ARPUTIL_H */

