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
    Parameters(unsigned int maxDelay);
    Parameters(const Parameters& orig);
    void SetMaxDelay(unsigned int maxDelay);
    unsigned int GetMaxDelay() const;
    
    
private:
    unsigned int maxDelay;
};

#endif	/* PARAMETERS_H */

