/* 
 * File:   Instance.h
 * Author: alexander
 *
 * Created on September 25, 2011, 11:02 PM
 */

#ifndef INSTANCE_H
#define	INSTANCE_H

#include <vector>
#include <iostream>

#include "Flight.h"
#include "City.h"
#include "Log.h"
#include "Parameters.h"


using namespace std;

class Instance {
public:
    Instance(Parameters parameters);
    virtual ~Instance();
    
    vector<Flight> GetAllFlights() const {
        return allFlights;
    }

    void SetAllFlights(vector<Flight> allFlights) {
        this->allFlights = allFlights;
    }

    vector<Flight*> GetMineFlights() const {
        return mineFlights;
    }

    void SetMineFlights(vector<Flight*> mineFlights) {
        this->mineFlights = mineFlights;
    }

    vector<City> GetAllCities() const {
        return allCities;
    }

    void SetAllCities(vector<City> allCities) {
        this->allCities = allCities;
    }

    int GetFirstInitTime(){
        return allFlights[0].GetInitTime();
    }

    int GetLastInitTime(){
        return allFlights[allFlights.size() - 1].GetInitTime();
    }

    Parameters getParameters() const {
        return parameters;
    }

    void setParameters(Parameters parameters) {
        this->parameters = parameters;
    }

    void SetSlaceOfTime(int beginTime, int endTime){
        Log::d("Slace of Time (%d - %d)", beginTime, endTime);

        this->beginTime = beginTime;
        this->endTime = endTime;
        loadMineFlights();
    }

private:
    void loadMineFlights();
    int beginTime, endTime;
    vector<City> allCities;
    vector<Flight> allFlights;
    vector<Flight*> mineFlights;
    Parameters parameters;
};

#endif	/* INSTANCE_H */

