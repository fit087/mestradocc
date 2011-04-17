/* 
 * File:   GRASP.h
 * Author: alexanderdealmeidapinto
 *
 * Created on 17 de Abril de 2011, 11:19
 */

#ifndef GRASP_H
#define	GRASP_H

#include <iostream>
#include <vector>

#include "Flight.h"
#include "Parameters.h"

using namespace std;

class GRASP {
public:
    GRASP(Parameters p);
    
    vector< vector<Flight> > construct(vector<Flight*> flights);
    vector<Flight> cloneTrack(vector<Flight*> track);
    bool canBeChosen(vector< vector<Flight*> > &tracks, Flight *flightCandidate);
    bool canSucceedDirect(Flight *initialFlight, Flight *finalFlight);
    bool canSuccedInderect(Flight *initialFlight, Flight *finalFlight);
    void setParameters(Parameters parameters);
    Parameters getParameters() const;
    static void readInput(char* file);

private:
    Parameters parameters;
    Flight* calculateNextFlight(Flight *actualFlight);
    void removeTrack(vector< vector<Flight*> > &track, int index);

};

#endif	/* GRASP_H */

