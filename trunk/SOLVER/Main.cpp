#include <ilcplex/ilocplex.h>
#include <iostream>
#include "ARPSolver.h"
#include "Flight.h"
#include "util/MathUtil.h"
#include "GRASP.h"
#include <cstring>
#include <vector>

using namespace std;

ILOSTLBEGIN
int main(int argc, char **argv) {
    //ARPSolver::loadFile();

    if (argc < 2) {
        cout << "Modo de usar" << endl;
        cout << "./arp <entrada> [useSolver]" << endl;
        exit(1);
    }
    
    bool useSolver = false;

   // printf("%d - |%s| << \n", argc, argv[2]);
    
    if(argc == 3){
        useSolver = strcmp(argv[2], "true") == 0;
    }
    
    // GRASP::readInput(argv[1]);
    
    if(useSolver){
        ARPSolver::readInput(argv[1]);
    }
    else{
        GRASP::readInput(argv[1]);
    }

    return 0;
}

// END main
