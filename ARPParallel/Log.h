/* 
 * File:   Log.h
 * Author: alexander
 *
 * Created on September 25, 2011, 11:11 PM
 */

#ifndef LOG_H
#define	LOG_H

#include <iostream>


using namespace std;

class Log {
public:
    Log();
    Log(const Log& orig);
    virtual ~Log();
    static void i(const char* logline, ...);
    static void d(const char* logline, ...);
    static void e(const char* logline, ...);

private:

};

#endif	/* LOG_H */

