/* 
 * File:   Parameters.cpp
 * Author: alexander
 * 
 * Created on September 26, 2011, 6:27 PM
 */

#include "Parameters.h"

Parameters::Parameters(int alfa, int probabilityArcType1, int probabilityArcType2, int probabilityArcType3, int probabilityArcType4, int maxDelay) :
alfa(alfa), probabilityArcType1(probabilityArcType1), probabilityArcType2(probabilityArcType2), probabilityArcType3(probabilityArcType3),
probabilityArcType4(probabilityArcType4), maxDelay(maxDelay) {
    
}

Parameters::Parameters(const Parameters& orig) {
    this->alfa = orig.GetAlfa();
    this->probabilityArcType1 = orig.GetProbabilityArcType1();
    this->probabilityArcType2 = orig.GetProbabilityArcType2();
    this->probabilityArcType3 = orig.GetProbabilityArcType3();
    this->probabilityArcType4 = orig.GetProbabilityArcType4();
    this->maxDelay = orig.GetMaxDelay();
}

Parameters::~Parameters() {
}

