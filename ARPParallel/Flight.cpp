/* 
 * File:   Flight.cpp
 * Author: alexander
 * 
 * Created on September 26, 2011, 2:57 PM
 */

#include <iostream>

#include "Flight.h"

Flight::Flight(int index, string name, int initTime, int duration, int dCity, int aCity){
    this->index = index;
    this->name = name;
    this->initTime = initTime;
    this->duration = duration;
    this->dCity = dCity;
    this->aCity = aCity;
    this->delay = 0;
    this->selected = false;
}

Flight::Flight(const Flight& orig) {
    this->name = orig.GetName();
    this->initTime = orig.GetInitTime();
    this->duration = orig.GetDuration();
    this->dCity = orig.GetDCity();
    this->aCity = orig.GetACity();
    this->index = orig.GetIndex();
    this->delay = orig.GetDelay();
    this->selected = orig.IsSelected();
}

Flight::~Flight() {
}

