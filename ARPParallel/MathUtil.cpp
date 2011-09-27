/*
 * File:   MathUtil.cpp
 * Author: alexanderdealmeidapinto
 *
 * Created on 17 de Abril de 2011, 12:25
 */


#include <stdlib.h>

#include "MathUtil.h"

MathUtil MathUtil::instance = MathUtil();

MathUtil::MathUtil() {
    this->seed = time(NULL);
    //srandom(seed);
    srand(seed);
}

int MathUtil::nextInt(int n){
    //return random()%n;
    return rand()%n;
}

MathUtil MathUtil::getInstance() {
    return instance;
}
