/* 
 * File:   ARPSolver.h
 * Author: daniel
 *
 * Created on April 10, 2011, 12:43 PM
 */

#ifndef ARPSOLVER_H
#define	ARPSOLVER_H

#define MAX_FLIGHT 1000

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
    static vector< vector<Flight> > solver(vector<Flight> *v, Instance *instance);
    static vector< vector<Flight> > solver2(vector<Flight> *v, Instance *instance);
    static void readInput(char *file);
    static void test();
    static void test2();
private:
    static void adjustTime();
    static int costOfArc(vector<Flight> *v, map< int, map< int, int > > *distanceTimes, int orig, int dest, int maxDelay);
    static int costOfArc1(vector<Flight> *v, int orig, int dest);
    static int costOfArc2(vector<Flight> *v, int orig, int dest, int maxDelay);
    static int costOfArc3(vector<Flight> *v, map< int, map< int, int > > *distanceTimes, int orig, int dest);
    static int costOfArc4(vector<Flight> *v, map< int, map< int, int > > *distanceTimes, int orig, int dest, int maxDelay);
    static void showVariables(IloCplex &model, IloIntVarArray vars[],IloIntVar delay[], int n);
    static void showCosts(IloNumArray cost[], int n);
    static vector< vector<Flight> > assembleResult(vector<Flight> *flight, Instance *instance,  IloCplex &cplex, IloBoolVarArray vars[], IloIntArray cost[], IloIntVar delay[], int n);
    static vector< vector<Flight> > assembleResult2(vector<Flight> *flight, Instance *instance,  IloCplex &cplex, IloBoolVarArray** vars, IloIntArray** cost, int n);
    static void finalizeTrail(vector<Flight> *flight, vector<Flight> *trail, IloCplex &cplex, IloBoolVarArray vars[], IloIntArray cost[], IloIntVar delay[], int n);
    static void finalizeTrail2(vector<Flight> *flight, vector<Flight> *trail, IloCplex &cplex, IloBoolVarArray** vars, IloIntArray** cost, int n);
    
};

#endif	/* ARPSOLVER_H */

