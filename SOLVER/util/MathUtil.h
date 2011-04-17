/* 
 * File:   MathUtil.h
 * Author: alexanderdealmeidapinto
 *
 * Created on 17 de Abril de 2011, 12:25
 */

#ifndef MATHUTIL_H
#define	MATHUTIL_H

#include <ctime>

class MathUtil {
public:


    
    int nextInt(int n);
    static MathUtil getInstance();
private:
    MathUtil();
    static MathUtil instance;
    time_t seed;

};

#endif	/* MATHUTIL_H */

