/* 
 * File:   Flight.h
 * Author: daniel
 *
 * Created on April 10, 2011, 12:43 PM
 */

#ifndef FLIGHT_H
#define	FLIGHT_H



class Flight {
public:
    Flight(unsigned int index, unsigned int departureTime, unsigned int duration,
            unsigned int departureCity, unsigned int arrivalCity);
    Flight(const Flight& orig);
    virtual ~Flight();

    unsigned int GetIndex() const;
    unsigned int GetArrivalCity() const;
    unsigned int GetDepartureCity() const;
    unsigned int GetDuration() const;
    unsigned int GetDepartureTime() const;
    unsigned int GetRealArrivalTime();
    void SetDelay(int delay);
    int GetDelay() const;


    void toString();
    
    void SetSelected(bool selected);
    bool IsSelected() const;
    bool validateGeographicalConstraint(Flight *other);
    bool validateTemporalConstraint(Flight *other, int maxDelay);
    
    
    
private:
    unsigned int index;
    unsigned int departureTime;
    unsigned int duration;
    unsigned int departureCity;
    unsigned int arrivalCity;
    int delay;
    bool selected;
};

#endif	/* FLIGHT_H */

