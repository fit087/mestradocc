/* 
 * File:   Flight.cpp
 * Author: daniel
 * 
 * Created on April 10, 2011, 12:43 PM
 */

#include <iostream>
#include "Flight.h"

using namespace std;

Flight::Flight(unsigned int index, unsigned int departureTime, unsigned int duration,
            unsigned int departureCity, unsigned int arrivalCity){
    this->index = index;
    this->departureTime = departureTime;
    this->duration = duration;
    this->departureCity = departureCity;
    this->arrivalCity = arrivalCity;
}

Flight::Flight(const Flight& orig) {
    this->index = orig.GetIndex();
    this->departureTime = orig.GetDepartureTime();
    this->duration = orig.GetDuration();
    this->departureCity = orig.GetDepartureCity();
    this->arrivalCity = orig.GetArrivalCity();
}

Flight::~Flight() {
}

unsigned int Flight::GetIndex() const {
    return index;
}

unsigned int Flight::GetArrivalCity() const {
    return arrivalCity;
}
unsigned int Flight::GetDepartureCity() const {
    return departureCity;
}
unsigned int Flight::GetDuration() const {
    return duration;
}
unsigned int Flight::GetDepartureTime() const {
    return departureTime;
}

void Flight::toString(){
    cout << "Index:" << index << " Inicio:" << departureTime <<  " Duration:" << duration << " Init:" << departureCity << " Finish:" << arrivalCity;
}


