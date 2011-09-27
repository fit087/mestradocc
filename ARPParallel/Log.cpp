/* 
 * File:   Log.cpp
 * Author: alexander
 * 
 * Created on September 25, 2011, 11:11 PM
 */

#include "Log.h"
#include "ParallelUtil.h"
#include <iomanip>


Log::Log() {
}

Log::Log(const Log& orig) {
}

Log::~Log() {
}

void Log::i(const char* logline, ...) {
    va_list argList;
    char cbuffer[1024];
    va_start(argList, logline);
    vsnprintf(cbuffer, 1024, logline, argList);
    va_end(argList);
    cout << setfill('0') << setw(2) << ParallelUtil::rank << " - [INFO] " << cbuffer << endl;
}

void Log::d(const char* logline, ...) {
    va_list argList;
    char cbuffer[1024];
    va_start(argList, logline);
    vsnprintf(cbuffer, 1024, logline, argList);
    va_end(argList);
    cout << ParallelUtil::rank << " [DEBUG] " << cbuffer << endl;
}

void Log::e(const char* logline, ...) {
    va_list argList;
    char cbuffer[1024];
    va_start(argList, logline);
    vsnprintf(cbuffer, 1024, logline, argList);
    va_end(argList);
    cout << ParallelUtil::rank << " [ERRO] " << cbuffer << endl;
}
