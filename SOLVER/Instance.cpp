/* 
 * File:   Instance.cpp
 * Author: alexanderdealmeidapinto
 * 
 * Created on 17 de Abril de 2011, 15:41
 */

#include "Instance.h"
#include "Parameters.h"
#include "GRASP.h"

Instance::Instance(vector<Flight*> flights, map<unsigned int, map<unsigned int, unsigned int> > times, unsigned int maxDelay){
    this->flights=flights;
    this->times=times;
    this->maxDelay=maxDelay;
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

    cout << "Lendo voos" << endl;
    for (int i = 0; i < nFlights; i++) {
        unsigned int departureTime;
        unsigned int duration;
        unsigned int departureCity;
        unsigned int arrivalCity;

        stream >> departureTime >> duration >> departureCity >> arrivalCity;

        Flight *flight = new Flight(i, departureTime, duration, departureCity, arrivalCity);
        flights.push_back(flight);

        Parameters p(maxDelay);
        GRASP grasp(p);
        vector< vector<Flight> > result = grasp.construct(flights);

        cout << "Numero de trilhos " << result.size() << endl;
    }

    int nTimes;
    map<unsigned int, map<unsigned int, unsigned int> > times;
    /*cout << "Lendo tempos" << endl;

    stream >> nTimes;
    

    for(int i = 0; i < nTimes; i++){
        int orig, dest, time;
        stream >> orig >> dest >> time;
        cout << "TESTE " << &times[orig] << endl;
        //this->times[orig][dest] = time;
    }*/

    return Instance(flights, times, maxDelay);
}
int Instance::getMaxDelay() const {
    return maxDelay;
}
map<unsigned int, map<unsigned int, unsigned int> > Instance::getTimes() const {
    return times;
}
vector<Flight*> Instance::getFlights() const {
    return flights;
}

