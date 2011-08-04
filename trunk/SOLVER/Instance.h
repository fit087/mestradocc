/* 
 * File:   Instance.h
 * Author: alexanderdealmeidapinto
 *
 * Created on 17 de Abril de 2011, 15:41
 */

#ifndef INSTANCE_H
#define	INSTANCE_H

#include <iostream>
#include <vector>
#include <map>

#include "Flight.h"

using namespace std;

class Instance {
public:

    virtual ~Instance();
    int getMaxDelay() const;
    vector<Flight*>* getFlights();

    static Instance read(istream &stream);
    void setFlights(vector<Flight*> flights);
    map<int, map<int, int> >* getTimes() ;
    int getFlightTime(Flight &init, Flight &dest);

private:
    Instance(vector<Flight*> flights, map< int, map< int, int> > times, unsigned int maxDelay);
    vector<Flight*> flights;
    map< int, map< int, int > > times;
    int maxDelay;

};

#endif	/* INSTANCE_H */

