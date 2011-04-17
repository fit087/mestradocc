/* 
 * File:   Parameters.cpp
 * Author: alexanderdealmeidapinto
 * 
 * Created on 17 de Abril de 2011, 11:35
 */

#include "Parameters.h"

Parameters::Parameters() {
    this->maxDelay = 0;
}
Parameters::Parameters(unsigned int maxDelay) {
    this->maxDelay = maxDelay;
}

Parameters::Parameters(const Parameters& orig) {
}
void Parameters::SetMaxDelay(unsigned int maxDelay) {
    this->maxDelay = maxDelay;
}
unsigned int Parameters::GetMaxDelay() const {
    return maxDelay;
}


