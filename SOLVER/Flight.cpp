/* 
 * File:   Flight.cpp
 * Author: daniel
 * 
 * Created on April 10, 2011, 12:43 PM
 */

#include <iostream>
#include <cmath>
#include "Flight.h"

using namespace std;

Flight::Flight(int index, int departureTime, int duration,
        int departureCity, int arrivalCity) {
    this->index = index;
    this->departureTime = departureTime;
    this->duration = duration;
    this->departureCity = departureCity;
    this->arrivalCity = arrivalCity;
    this->selected = false;
    this->delay = 0;
    this->repoFlight = false;
    this->cost = 0;
}

Flight::Flight(const Flight& orig) {
    this->index = orig.GetIndex();
    this->departureTime = orig.GetDepartureTime();
    this->duration = orig.GetDuration();
    this->departureCity = orig.GetDepartureCity();
    this->arrivalCity = orig.GetArrivalCity();
    this->selected = orig.IsSelected();
    this->delay = orig.GetDelay();
    this->ilogIndex = orig.GetIlogIndex();
    this->cost = 0;
    this->repoFlight = orig.IsRepoFlight();
}

Flight::~Flight() {
}

int Flight::GetIndex() const {
    return index;
}

int Flight::GetArrivalCity() const {
    return arrivalCity;
}

int Flight::GetDepartureCity() const {
    return departureCity;
}

int Flight::GetDuration() const {
    return duration;
}

int Flight::GetDepartureTime() const {
    return departureTime;
}

int Flight::GetRealArrivalTime() {
    return GetRealDepartureTime() + GetDuration();
}

int Flight::GetRealDepartureTime() {
    return GetDepartureTime() + GetDelay();
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
    //cout << "TEMP " << this->GetArrivalCity() << " " << other->GetDepartureCity() << endl;
    return (this->GetArrivalCity() == other->GetDepartureCity());
}

bool Flight::validateTemporalConstraint(Flight *other, int maxDelay) {


    if (requiredDelay(other) > maxDelay) {
        return false;
    } else {
        return true;
    }

}

int Flight::requiredDelay(Flight *other) {
    int diff = (other->GetDepartureTime() - this->GetRealArrivalTime());

    if (diff >= 0) {
        return 0;
    } else {
        return -diff;
    }
}

void Flight::SetIlogIndex(int ilogIndex) {
    this->ilogIndex = ilogIndex;
}

int Flight::GetIlogIndex() const {
    return ilogIndex;
}

void Flight::SetRepoFlight(bool repoFlight) {
    this->repoFlight = repoFlight;
}

bool Flight::IsRepoFlight() const {
    return repoFlight;
}

int Flight::GetCost() {
    return cost + abs(delay);
}

void Flight::SetCost(int cost) {
    this->cost = cost;
}

void Flight::toString() {
    cout << "Index:" << index << " Inicio:" << departureTime << " Duration:" << duration << " Init:" << departureCity << " Finish:" << arrivalCity;
}



