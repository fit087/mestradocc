/* 
 * File:   GRASP_Construct.h
 * Author: alexander
 *
 * Created on September 26, 2011, 5:25 PM
 */

#ifndef GRASP_CONSTRUCT_H
#define	GRASP_CONSTRUCT_H

#include <vector>

#include "Instance.h"

using namespace std;

class GRASP_Construct {
public:
    static vector< vector<Flight> > construct(Instance *instance);

private:
    static vector<Flight*> extractAdjacentFlight(Flight *actualFlight, vector<Flight> &clonedFlights, int selectedArc, Instance *instance);
    static Flight* calculateNextFlight(Flight *actualFlight, vector<Flight> &clonedFlights, Instance *instance);
    static int randomizingArc(Instance *instance);
    static int RCLIndex(Flight *actualFlight, vector<Flight*> candidateList, int alfa, int selectedArc);

    static vector<Flight*> extractAdjacentFlightArcType1(Flight* actualFlight, vector<Flight> &candidateList);
    static vector<Flight*> extractAdjacentFlightArcType2(Flight* actualFlight, vector<Flight> &candidateList, Instance *instance);
    static vector<Flight*> extractAdjacentFlightArcType3(Flight* actualFlight, vector<Flight> &candidateList);
    static vector<Flight*> extractAdjacentFlightArcType4(Flight* actualFlight, vector<Flight> &candidateList, Instance *instance);
    
};

#endif	/* GRASP_CONSTRUCT_H */

