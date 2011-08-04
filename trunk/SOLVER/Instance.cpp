/* 
 * File:   Instance.cpp
 * Author: alexanderdealmeidapinto
 * 
 * Created on 17 de Abril de 2011, 15:41
 */

#include "Instance.h"
#include "Parameters.h"
#include "GRASP.h"

Instance::Instance(vector<Flight*> flights, map< int, map< int, int> > times, unsigned int maxDelay) {
    this->flights = flights;
    this->times = times;
    this->maxDelay = maxDelay;
}

Instance::~Instance() {
    for (int i = 0; i < this->flights.size(); i++) {
        delete flights[i];
    }
}

Instance Instance::read(istream &stream) {
    int nFlights, maxDelay;

    stream >> nFlights >> maxDelay;

    vector<Flight*> flights;

    cout << "\tLendo voos" << endl;
    for (int i = 0; i < nFlights; i++) {
        int departureTime;
        int duration;
        int departureCity;
        int arrivalCity;

        stream >> departureTime >> duration >> departureCity >> arrivalCity;

        Flight *flight = new Flight(i, departureTime, duration, departureCity, arrivalCity);
        flights.push_back(flight);

    }

    int nd;
    map<int, map<int, int> > distanceTimes;

    stream >> nd;

    for(int i = 0; i < nd; i++){
        int o, d, time;
        stream >> o >> d >> time;
        distanceTimes[o][d] = time;
    }
   
    return Instance(flights, distanceTimes, maxDelay);
}
void Instance::setFlights(vector<Flight*> flights) {
    this->flights = flights;
}
map<int, map<int, int> >* Instance::getTimes() {
    return &times;
}

int Instance::getMaxDelay() const {
    return maxDelay;
}

vector<Flight*> *Instance::getFlights() {
    return &flights;
}

int Instance::getFlightTime(Flight &init, Flight &dest){
    int city1 = init.GetArrivalCity();
    int city2 = dest.GetDepartureCity();
    
    if(city2 < city1){ //SWAP (Menor primeiro)
        int t = city1;
        city1 = city2;
        city2 = t;
    }
    
    return times[city1][city2];
}

