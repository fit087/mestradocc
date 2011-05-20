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
    Flight(int index, int departureTime, int duration,
            int departureCity, int arrivalCity);
    Flight(const Flight& orig);
    virtual ~Flight();

    int GetIndex() const;
    int GetArrivalCity() const;
    int GetDepartureCity() const;
    int GetDuration() const;
    int GetDepartureTime() const;
    int GetRealArrivalTime();
    int GetRealDepartureTime();
    void SetDelay(int delay);
    int GetDelay() const;

    void toString();
    
    void SetSelected(bool selected);
    bool IsSelected() const;
    bool validateGeographicalConstraint(Flight *other);
    bool validateTemporalConstraint(Flight *other, int maxDelay);
    int requiredDelay(Flight *other);
    void SetIlogIndex(int ilogIndex);
    int GetIlogIndex() const;
    void SetRepoFlight(bool repoFlight);
    bool IsRepoFlight() const;
    int GetCost();

    void SetCost(int cost);

    

    
    
    
private:
    int index;
    int ilogIndex;
    int departureTime;
    int duration;
    int cost;
    int departureCity;
    int arrivalCity;
    int delay;
    bool selected;
    bool repoFlight;
};

#endif	/* FLIGHT_H */

