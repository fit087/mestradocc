/* 
 * File:   Parameters.h
 * Author: alexander
 *
 * Created on September 26, 2011, 6:27 PM
 */

#ifndef PARAMETERS_H
#define	PARAMETERS_H

class Parameters {
public:
    Parameters(int alfa, int probabilityArcType1, int probabilityArcType2, int probabilityArcType3, int probabilityArcType4, int maxDelay);
    Parameters(const Parameters& orig);
    virtual ~Parameters();
    int GetProbabilityArcType1() const {
        return probabilityArcType1;
    }

    void SetProbabilityArcType1(int probabilityArcType1) {
        this->probabilityArcType1 = probabilityArcType1;
    }

    int GetProbabilityArcType2() const {
        return probabilityArcType2;
    }

    void SetProbabilityArcType2(int probabilityArcType2) {
        this->probabilityArcType2 = probabilityArcType2;
    }

    int GetProbabilityArcType3() const {
        return probabilityArcType3;
    }

    void SetProbabilityArcType3(int probabilityArcType3) {
        this->probabilityArcType3 = probabilityArcType3;
    }

    int GetProbabilityArcType4() const {
        return probabilityArcType4;
    }

    void SetProbabilityArcType4(int probabilityArcType4) {
        this->probabilityArcType4 = probabilityArcType4;
    }

    int GetAlfa() const {
        return alfa;
    }

    void SetAlfa(int alfa) {
        this->alfa = alfa;
    }

    int GetMaxDelay() const {
        return maxDelay;
    }

    void SetMaxDelay(int maxDelay) {
        this->maxDelay = maxDelay;
    }

private:
    int alfa;
    int probabilityArcType1, probabilityArcType2, probabilityArcType3, probabilityArcType4;
    int maxDelay;

};

#endif	/* PARAMETERS_H */

