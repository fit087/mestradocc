1#include <ilcplex/ilocplex.h>
#include <iostream>
#include "ARPSolver.h"
#include "Flight.h"
#include "util/MathUtil.h"
#include "GRASP.h"
#include <vector>

using namespace std;

ILOSTLBEGIN
int main(int argc, char **argv) {
    //ARPSolver::loadFile();

    if(argc < 2){
        cout << "Modo de usar" << endl;
        cout << "./arp <entrada> [alfa]" << endl;
        exit(1);
    }
    
   // GRASP::readInput(argv[1]);
    ARPSolver::readInput(argv[1]);

    return 0;
}

// END main
