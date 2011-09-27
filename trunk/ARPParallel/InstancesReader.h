/* 
 * File:   InstancesReader.h
 * Author: alexander
 *
 * Created on September 25, 2011, 11:01 PM
 */

#ifndef INSTANCESREADER_H
#define	INSTANCESREADER_H

#include "Instance.h"

class InstancesReader {
public:
    InstancesReader();
    InstancesReader(const InstancesReader& orig);
    virtual ~InstancesReader();
    static Instance* readInstance(char *citiesFileName, char *flightsFileName);
private:
    static vector<City> readCities(char *fileName);
    static vector<Flight> readFlights(char *fileName, vector<City> cities);
};

#endif	/* INSTANCESREADER_H */

