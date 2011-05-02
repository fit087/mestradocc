/* 
 * File:   Parameters.cpp
 * Author: alexanderdealmeidapinto
 * 
 * Created on 17 de Abril de 2011, 11:35
 */

#include "Parameters.h"


Parameters::Parameters(){

}

Parameters::Parameters(unsigned int maxDelay, unsigned int probabilityArcType1,
        unsigned int probabilityArcType2, unsigned int probabilityArcType3,
        unsigned int probabilityArcType4, unsigned int alfa) {
    this->maxDelay = maxDelay;
    this->probabilityArcType1=probabilityArcType1;
    this->probabilityArcType2=probabilityArcType2;
    this->probabilityArcType3=probabilityArcType3;
    this->probabilityArcType4=probabilityArcType4;
    this->alfa = alfa;
}

void Parameters::SetMaxDelay(unsigned int maxDelay) {
    this->maxDelay = maxDelay;
}
unsigned int Parameters::GetMaxDelay() const {
    return maxDelay;
}
unsigned int Parameters::GetProbabilityArcType4() const {
    return probabilityArcType4;
}
unsigned int Parameters::GetProbabilityArcType3() const {
    return probabilityArcType3;
}
unsigned int Parameters::GetProbabilityArcType2() const {
    return probabilityArcType2;
}
unsigned int Parameters::GetProbabilityArcType1() const {
    return probabilityArcType1;
}
unsigned int Parameters::GetAlfa() const {
    return alfa;
}


