/* 
 * File:   InstanceUtil.h
 * Author: alexander
 *
 * Created on September 26, 2011, 6:06 PM
 */

#ifndef INSTANCEUTIL_H
#define	INSTANCEUTIL_H

#include "Flight.h"
#include "City.h"
#include <vector>

using namespace std;


class InstanceUtil {
public:
    static vector<Flight> cloneFlights(vector<Flight*> flights);
    static void showFlights(vector<Flight> &flights);
    static void showFlights(vector<Flight*> flights);
    static void showResult(vector< vector<Flight> > &flights);
    static void showCities(vector<City> &cities);
private:

};

#endif	/* INSTANCEUTIL_H */

