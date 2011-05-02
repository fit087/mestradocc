/* 
 * File:   ARPUtil.cpp
 * Author: alexanderdealmeidapinto
 * 
 * Created on 19 de Abril de 2011, 14:17
 */

#include "ARPUtil.h"

bool ARPUtil::validateSolution(vector< vector<Flight> > &solution) {
    for (int i = 0; i < solution.size(); i++) {
        for (int j = 0; j < solution[i].size() - 1; j++) {
            Flight &first = solution[i][j];
            Flight &second = solution[i][j + 1];

            if (!first.validateGeographicalConstraint(&second)) {
                cout << "NAO VALIDA 1 (" << first.GetArrivalCity() << " , " << second.GetDepartureCity() << " )" << endl;
                return false;
            }

            if (first.GetRealArrivalTime() > second.GetRealDepartureTime()) {
                cout << "NAO VALIDA 2 ( " << first.GetRealArrivalTime() << " , " << second.GetRealDepartureTime() << ")" << endl;
                return false;
            }
        }
    }

    return true;
}

void ARPUtil::showSolution(vector< vector<Flight> > &solution) {
    cout << "N Trilho: " << solution.size() << endl;
    for (int i = 0; i < solution.size(); i++) {
        cout << solution[i].size() << " ";
        for (int j = 0; j < solution[i].size(); j++) {
            cout << solution[i][j].GetIndex() << " ";
            if (solution[i][j].GetDelay() != 0) {
                printf("(%+2d)  ", solution[i][j].GetDelay());
            }
        }
        cout << endl;
    }
}

int ARPUtil::calculeCost(vector< vector<Flight> > &r) {
    int cost = r.size()*1000;

    for (int i = 0; i < r.size(); i++) {
        for (int j = 0; j < r[i].size(); j++) {
            cost += abs(r[i][j].GetDelay());
        }
    }

    return cost;
}

void ARPUtil::relaxDelays(vector< vector<Flight> > &r) {
    int relaxedDelay = 0;
    for (int i = 0; i < r.size(); i++) {
        vector<Flight> &track = r[i];
        //cout << " Track Size " << track.size() << endl;
        for (int j = track.size() - 1; j >= 0; j--) {

            Flight &flight = track[j];

            if (flight.GetDelay() < 0) {
                Flight *nextFlight = (j == track.size() - 1) ? NULL : &track[j + 1];
                
                if (nextFlight == NULL) {
                    relaxedDelay += (-flight.GetDelay());
                    flight.SetDelay(0);
                } else {
                    int minDelay = nextFlight->GetRealDepartureTime() - (flight.GetDepartureTime() + flight.GetDuration());
                    
                   // cout << " Size " << track.size() << " - " << j << " " << (j+1) << endl;
                    // cout << " Teste " << flight.GetIndex() << " " << nextFlight->GetIndex() << endl;

                    if (minDelay > 0) {
                        relaxedDelay += (-flight.GetDelay());
                        flight.SetDelay(0);
                    } else {
                        if (minDelay < flight.GetDelay()){
                            cout << "Invalido " << minDelay << " " << flight.GetDelay() << endl;
                            exit(1); //A solucao era invalida
                        }
                        relaxedDelay += (-flight.GetDelay() + minDelay);
                        flight.SetDelay(minDelay);
                    }
                }
            } else if (flight.GetDelay() > 0) {
                //Nunca vai ser liberado esse delay.
            }
        }
    }

        //cout << "Relaxed Delays: " << relaxedDelay << endl;
}