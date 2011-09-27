/* 
 * File:   InstancesReader.cpp
 * Author: alexander
 * 
 * Created on September 25, 2011, 11:01 PM
 */

#include "InstancesReader.h"
#include "Log.h"
#include <iostream>
#include <fstream>

using namespace std;

InstancesReader::InstancesReader() {
}

InstancesReader::InstancesReader(const InstancesReader& orig) {
}

InstancesReader::~InstancesReader() {
}

Instance* InstancesReader::readInstance(char *citiesFileName, char *flightsFileName) {

    int alfa = 50;
    int probabilityArcType1 = 79;
    int probabilityArcType2 = 16;
    int probabilityArcType3 = 4;
    int probabilityArcType4 = 1;
    int maxDelay = 10;

    Parameters parameters(alfa, probabilityArcType1, probabilityArcType2, probabilityArcType3, probabilityArcType4, maxDelay);

    Instance *instance = new Instance(parameters);


    vector<City> cities = readCities(citiesFileName);
  //  Instance::showCities(cities);
    vector<Flight> flights = readFlights(flightsFileName, cities);
   // Instance::showFlights(flights);

    instance->SetAllCities(cities);
    instance->SetAllFlights(flights);

    return instance;

}

vector<City> InstancesReader::readCities(char *fileName) {
    ifstream citiesFile;

    citiesFile.open(fileName);

    if (!citiesFile) {
        Log::e("Unable to open cities file");
        exit(1); // call system to stop
    }

    vector<City> result;

    while (!citiesFile.eof()) {
        string name;
        int groundTime;

        citiesFile >> name >> groundTime;
        result.push_back(City(name, groundTime));

    }

    citiesFile.close();

    return result;
}

vector<Flight> InstancesReader::readFlights(char *fileName, vector<City> cities) {
    ifstream flightsFile;

    flightsFile.open(fileName);

    if (!flightsFile) {
        Log::e("Unable to open flights file");
        exit(1); // call system to stop
    }

    vector<Flight> result;

    int cont = 0;
    while (!flightsFile.eof()) {
        string name;
        string dTimeString, aTimeString;
        int dDay, dHour, dMinute, dTime;
        int aDay, aHour, aMinute, aTime;
        string dCityName, aCityName;

        int dCity, aCity;
        int duration;
        flightsFile >> name >> dDay >> dTimeString >> aDay >> aTimeString >> dCityName >> aCityName;

        sscanf(dTimeString.data(), "%d:%d", &dHour, &dMinute);
        sscanf(aTimeString.data(), "%d:%d", &aHour, &aMinute);

        dTime = dDay * 24 * 60 + dHour * 60 + dMinute;
        aTime = aDay * 24 * 60 + aHour * 60 + aMinute;
        duration = aTime - dTime;

        for(int i = 0; i < cities.size(); i++){
            if(cities[i].GetName() == dCityName){
                dCity = i;
            }

            if(cities[i].GetName() == aCityName){
                aCity = i;
            }
        }

        result.push_back(Flight(cont, name, dTime, duration, dCity, aCity));
        cont++;
    }

    flightsFile.close();

    return result;

}
