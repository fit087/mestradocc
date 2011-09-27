/* 
 * File:   InstanceUtil.cpp
 * Author: alexander
 * 
 * Created on September 26, 2011, 6:06 PM
 */

#include "InstanceUtil.h"
#include "Log.h"
#include "City.h"
#include <string>

using namespace std;

vector<Flight> InstanceUtil::cloneFlights(vector<Flight*> flights) {
    vector<Flight> retorno;

    for (unsigned int i = 0; i < flights.size(); i++) {
        Flight f = Flight((*flights[i]));
        retorno.push_back(f);
    }

    return retorno;
}

void InstanceUtil::showFlights(vector<Flight> &flights){
    for(int i = 0; i < flights.size(); i++){
        Flight &f = flights[i];
        Log::d("\t[%d] %s %d %d %d %d", f.GetIndex() ,f.GetName().data(), f.GetInitTime(), f.GetDuration(), f.GetDCity(), f.GetACity());
    }
}
void InstanceUtil::showFlights(vector<Flight*> flights){

    Log::d("Size of flights: %d", flights.size());
    for(int i = 0; i < flights.size(); i++){
        Flight *f = flights[i];
        Log::d("\t[%d] %s %d %d %d %d", f->GetIndex() ,f->GetName().data(), f->GetInitTime(), f->GetDuration(), f->GetDCity(), f->GetACity());
    }
}

void InstanceUtil::showCities(vector<City> &cities){
    Log::d("Cidades [Size: %d]", cities.size());
    for(int i = 0; i < cities.size(); i++){
        City &c = cities[i];
        Log::d("\t %s %d", c.GetName().data(), c.GetGroundTime());
    }
}

void InstanceUtil::showResult(vector< vector<Flight> > &result){
    Log::d("Resultado -> Trilhos: %d", result.size());
    for(int i = 0; i < result.size(); i++){
        Log::d("Trilho %d", i);
        for(int j = 0; j < result[i].size(); j++){
            Flight &f = result[i][j];
            Log::d("\t Flight = %d", f.GetIndex());
        }
        
    }
}

