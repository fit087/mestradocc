/* 
 * File:   Parameters.h
 * Author: alexanderdealmeidapinto
 *
 * Created on 17 de Abril de 2011, 11:35
 */

#ifndef PARAMETERS_H
#define	PARAMETERS_H

class Parameters {
public:
    Parameters();
    Parameters(unsigned int maxDelay, unsigned int probabilityArcType1,
        unsigned int probabilityArcType2, unsigned int probabilityArcType3,
        unsigned int probabilityArcType4, unsigned int alfa);
    void SetMaxDelay(unsigned int maxDelay);
    unsigned int GetMaxDelay() const;
    unsigned int GetProbabilityArcType4() const;
    unsigned int GetProbabilityArcType3() const;
    unsigned int GetProbabilityArcType2() const;
    unsigned int GetProbabilityArcType1() const;
    unsigned int GetAlfa() const;
    
    
private:
    unsigned int maxDelay;
    unsigned int alfa;
    unsigned int probabilityArcType1;
    unsigned int probabilityArcType2;
    unsigned int probabilityArcType3;
    unsigned int probabilityArcType4;
};

#endif	/* PARAMETERS_H */

