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
        unsigned int departureCity, unsigned int arrivalCity) {
    this->index = index;
    this->departureTime = departureTime;
    this->duration = duration;
    this->departureCity = departureCity;
    this->arrivalCity = arrivalCity;
    this->selected = false;
    this->delay = 0;
}

Flight::Flight(const Flight& orig) {
    this->index = orig.GetIndex();
    this->departureTime = orig.GetDepartureTime();
    this->duration = orig.GetDuration();
    this->departureCity = orig.GetDepartureCity();
    this->arrivalCity = orig.GetArrivalCity();
    this->selected = orig.IsSelected();
    this->delay = orig.GetDelay();
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

unsigned int Flight::GetRealArrivalTime() {
    return GetDepartureTime() + GetDuration() + GetDelay();
}

void Flight::SetDelay(int delay) {
    this->delay = delay;
}

int Flight::GetDelay() const {
    return delay;
}

void Flight::SetSelected(bool selected) {
    this->selected = selected;
}

bool Flight::IsSelected() const {
    return selected;
}

//#####################FIM DOS GETRS E SETRS#######################

bool Flight::validateGeographicalConstraint(Flight *other) {
    return (this->GetArrivalCity() == other->GetDepartureCity());
}

bool Flight::validateTemporalConstraint(Flight *other, int maxDelay) {


    int delay = (other->GetDepartureTime() - this->GetRealArrivalTime());

    delay += maxDelay;

    if (delay >= 0) {
        return true;
    }

    return false;
}

void Flight::toString() {
    cout << "Index:" << index << " Inicio:" << departureTime << " Duration:" << duration << " Init:" << departureCity << " Finish:" << arrivalCity;
}



