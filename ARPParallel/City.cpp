/* 
 * File:   City.cpp
 * Author: alexander
 * 
 * Created on September 26, 2011, 3:13 PM
 */

#include "City.h"

City::City(string name, int groundTime) : name(name), groundTime(groundTime) {
}

City::City(const City& orig) {
    this->name = orig.GetName();
    this->groundTime = orig.GetGroundTime();
}

City::~City() {
}

