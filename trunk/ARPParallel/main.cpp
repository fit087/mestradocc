/* 
 * File:   main.cpp
 * Author: alexander
 *
 * Created on September 25, 2011, 10:53 PM
 */

#include <iostream>
#include <cstdlib>
#include <mpi.h>
#include "InstancesReader.h"
#include "ParallelUtil.h"
#include "Log.h"
#include "GRASP_Construct.h"
#include "InstanceUtil.h"

using namespace std;

/*
 * 
 */
int main(int argc, char** argv) {

    int size, rank;


    /* Initialize MPI */
    MPI_Init(&argc, &argv);

    /* Find out this processor number */
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    /* Find out the number of processors */
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    if (rank == 0) {
        Log::i("Size: %d", size);
    }

    ParallelUtil::rank = rank;

    Log::i("My rank: %d", rank);

    char flightInput[30] = "instances/riosul_flights.txt";
    char citiesInput[30] = "instances/riosul_cities.txt";


    Instance *instance = InstancesReader::readInstance(citiesInput, flightInput);
    int slice = instance->GetLastInitTime() - instance->GetFirstInitTime();
    slice /= size;
    int initTime = instance->GetFirstInitTime() + slice*rank;
    instance->SetSlaceOfTime(initTime, initTime + slice);

    //InstanceUtil::showFlights(instance->GetMineFlights());

    vector< vector<Flight> > v = GRASP_Construct::construct(instance);

    //InstanceUtil::showResult(v);

    MPI_Barrier(MPI_COMM_WORLD);

    if (rank == 0) {
        Log::i("Instance cities: %s", citiesInput);
        Log::i("Instance flights: %s", flightInput);
        Log::i("Slice of Time: %d", slice);
    }
    
    Log::i("RESULT: %d", v.size());

    /* Shut down MPI */
    MPI_Finalize();
    return 0;
}

