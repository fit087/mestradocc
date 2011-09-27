/* 
 * File:   Flight.h
 * Author: alexander
 *
 * Created on September 26, 2011, 2:57 PM
 */

#ifndef FLIGHT_H
#define	FLIGHT_H

#include <string>

using namespace std;

class Flight {
public:
    Flight(int index, string name, int initTime, int duration, int dCity, int aCity);
    Flight(const Flight& orig);
    virtual ~Flight();

    int GetACity() const {
        return aCity;
    }

    void SetACity(int aCity) {
        this->aCity = aCity;
    }

    int GetDCity() const {
        return dCity;
    }

    void SetDCity(int dCity) {
        this->dCity = dCity;
    }

    int GetDuration() const {
        return duration;
    }

    void SetDuration(int duration) {
        this->duration = duration;
    }

    int GetInitTime() const {
        return initTime;
    }

    int GetEndTime() {
        return initTime + duration;
    }

    int GetRealEndTime(){
        return GetEndTime() + delay;
    }

    void SetInitTime(int initTime) {
        this->initTime = initTime;
    }

    string GetName() const {
        return name;
    }

    void SetName(string name) {
        this->name = name;
    }

    bool IsSelected() const {
        return selected;
    }

    void SetSelected(bool selected) {
        this->selected = selected;
    }

    int GetIndex() const {
        return index;
    }

    void SetIndex(int index) {
        this->index = index;
    }

    int GetDelay() const {
        return delay;
    }

    void SetDelay(int delay) {
        this->delay = delay;
    }

    bool validateGeographicalConstraint(Flight *other) {
        return (this->GetACity() == other->GetDCity());
    }

    bool validateTemporalConstraint(Flight *other, int maxDelay) {
        if (requiredDelay(other) > maxDelay) {
            return false;
        } else {
            return true;
        }
    }

    int requiredDelay(Flight *other) {
        int diff = (other->GetInitTime() - this->GetRealEndTime());

        if (diff >= 0) {
            return 0;
        } else {
            return -diff;
        }
    }

private:
    int index;
    int dCity, aCity;
    int initTime, duration;
    int delay;
    string name;
    bool selected;


};

#endif	/* FLIGHT_H */

