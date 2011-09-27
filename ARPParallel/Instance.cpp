/* 
 * File:   Instance.cpp
 * Author: alexander
 * 
 * Created on September 25, 2011, 11:02 PM
 */

#include "Instance.h"
#include "ParallelUtil.h"

#include <iostream>

using namespace std;

Instance::Instance(Parameters parameters) : parameters(parameters){

}

Instance::~Instance() {
}

void Instance::loadMineFlights(){

    mineFlights.clear();

    for(int i = 0; i < allFlights.size(); i++){
        Flight *f = &allFlights[i];
        if(f->GetInitTime() >= beginTime && f->GetInitTime() <= endTime){
            mineFlights.push_back(f);
        }
    }
}


