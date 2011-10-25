/* 
 * File:   LocalSearchUtil.h
 * Author: alexander
 *
 * Created on September 5, 2011, 8:26 AM
 */

#ifndef LOCALSEARCHUTIL_H
#define	LOCALSEARCHUTIL_H

#include <vector>
#include <algorithm>
#include "../Flight.h"


using namespace std;

class LocalSearchUtil {
public:
    LocalSearchUtil();
    static void crossOver(vector< vector<Flight> > &solution);
    static void swapParts(int track1, int candidate1_index, int track2, int candidate2_index, vector< vector<Flight> > &solution);
    LocalSearchUtil(const LocalSearchUtil& orig);
    virtual ~LocalSearchUtil();
    
private:

};

#endif	/* LOCALSEARCHUTIL_H */

