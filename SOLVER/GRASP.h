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
#include "Instance.h"
#include "util/LocalSearchUtil.h"

using namespace std;

class GRASP {
public:
    GRASP(Parameters p, Instance *instance);
    
    vector< vector<Flight> > construct(vector<Flight*> flights);
    vector< vector<Flight> > localSearch(vector< vector<Flight> > flights, int baseTime, int strategy);
    vector<Flight> cloneTrack(vector<Flight*> track);
    
    bool canBeChosen(vector< vector<Flight*> > &tracks, Flight *flightCandidate);
    bool canSucceedDirect(Flight *initialFlight, Flight *finalFlight);
    bool canSuccedInderect(Flight *initialFlight, Flight *finalFlight);
    void setParameters(Parameters parameters);
    Parameters getParameters() const;
    static int calculeBaseTime(vector<Flight*> flight);
    static int trackUtilization(int baseTime, vector<Flight> &track);
    static void readInput(char* file);
    

private:
    Instance *instance;
    Parameters parameters;
    Flight* calculateNextFlight(Flight *actualFlight, vector<Flight> &clonedFlights);
    vector<Flight*> extractAdjacentFlight(Flight *actualFlight, vector<Flight> &clonedFlights, int selectedArc);
    void removeTrack(vector< vector<Flight*> > &track, int index);
    int RCLIndex(Flight *actualFlight, vector<Flight*> candidateList, int alfa, int selectedArc);
    int randomizingArc();

    vector<Flight*> extractAdjacentFlightArcType1(Flight* actualFlight, vector<Flight> &candidateFlights);
    vector<Flight*> extractAdjacentFlightArcType2(Flight* actualFlight, vector<Flight> &candidateFlights);
    vector<Flight*> extractAdjacentFlightArcType3(Flight* actualFlight, vector<Flight> &candidateFlights);
    vector<Flight*> extractAdjacentFlightArcType4(Flight* actualFlight, vector<Flight> &candidateFlights);


};

#endif	/* GRASP_H */

