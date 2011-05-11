/* 
 * File:   ARPSolver.h
 * Author: daniel
 *
 * Created on April 10, 2011, 12:43 PM
 */

#ifndef ARPSOLVER_H
#define	ARPSOLVER_H

#include <vector>
#include <map>
#include <ilcplex/ilocplex.h>
#include "Flight.h"
#include "Instance.h"

using namespace std;

class ARPSolver {
public:
    ARPSolver();
    ARPSolver(const ARPSolver& orig);
    virtual ~ARPSolver();
    static vector< vector<Flight> > solver(vector<Flight> *v, Instance *instance, int maxDelay);
    static void readInput(char *file);
    static void test();
    static void test2();
private:
    static void adjustTime();
    static int costOfArc(vector<Flight> *v, map< int, map< int, int > > *distanceTimes, int orig, int dest, int maxDelay);
    static void showVariables(IloCplex &model, IloIntVarArray vars[],IloIntVar delay[], int n);
    static void showCosts(IloNumArray cost[], int n);
    static vector< vector<Flight> > assembleResult(vector<Flight> *flight, Instance *instance,  IloCplex &cplex, IloBoolVarArray vars[], IloIntArray cost[], IloIntVar delay[], int n);
    static void finalizeTrail(vector<Flight> *flight, vector<Flight> *trail, IloCplex &cplex, IloBoolVarArray vars[], IloIntArray cost[], IloIntVar delay[], int n);
    
};

#endif	/* ARPSOLVER_H */

