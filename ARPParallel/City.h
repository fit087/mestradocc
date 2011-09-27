/* 
 * File:   City.h
 * Author: alexander
 *
 * Created on September 26, 2011, 3:13 PM
 */

#ifndef CITY_H
#define	CITY_H

#include <string>

using namespace std;

class City {
public:
    City(string name, int groundTime);
    City(const City& orig);
    virtual ~City();

    int GetGroundTime() const {
        return groundTime;
    }

    string GetName() const {
        return name;
    }

private:
    string name;
    int groundTime;
};

#endif	/* CITY_H */

