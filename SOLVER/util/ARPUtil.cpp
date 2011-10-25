/* 
 * File:   ARPUtil.cpp
 * Author: alexanderdealmeidapinto
 * 
 * Created on 19 de Abril de 2011, 14:17
 */

#include "ARPUtil.h"
#include <cmath>

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

int ARPUtil::configureSolution(vector< vector<Flight> > &solution, Instance *instance) {
    int cost = 0;
    for (int i = 0; i < solution.size(); i++) {
        cost += configureTrack(solution[i], instance);
    }

    return cost;
}

int ARPUtil::configureTrack(vector<Flight> &track, Instance *instance) {

    int cost = 1000;

    if (track.size() == 1) {
        track[0].SetDelay(0);
    }

    if (track.size() > 1) {
        int minDelay = track[1].GetRealDepartureTime() - (track[0].GetDepartureTime() + track[0].GetDuration());
        if (minDelay < 0) {
            track[0].SetDelay(minDelay);
        } else {
            track[0].SetDelay(0);
        }
    }

    for (int i = 0; i < track.size() - 1; i++) {
        Flight &first = track[i];
        Flight &second = track[i + 1];

        int uniqueCost = 0;

        //        if(first.GetIndex() == 45 && second.GetIndex() == 54){
        //            cout << "RESULT " << first.GetArrivalCity() << " | " << second.GetDepartureCity() << endl;
        //        }

        if (first.validateGeographicalConstraint(&second)) {

            int delay = first.requiredDelay(&second);
            //            if(first.GetIndex() == 45 && second.GetIndex() == 54){
            //                cout << "TIME " << first.GetRealArrivalTime() << " " << (first.GetDepartureTime() + first.GetDuration()) << endl;
            //                cout << "TIME2 " << second.GetRealArrivalTime() << " " << (second.GetDepartureTime() + second.GetDuration()) << endl;
            //                cout << "DELAY " << delay << endl;
            //            }
            second.SetDelay(delay);

            cost += abs(delay);
            uniqueCost = abs(delay);
        }//É necessário a criação de um voo de reposicionamento.
        else {

            int diff = (second.GetDepartureTime()) - (first.GetRealDepartureTime());
            int origIndex = first.GetArrivalCity();
            int destIndex = second.GetDepartureCity();


            int flightTime = (*instance->getTimes())[origIndex][destIndex];
            diff -= flightTime;
            if (diff >= 0) {
                second.SetCost(flightTime);
                cost += flightTime;
            } else {


                second.SetCost(flightTime);
                second.SetDelay(-diff);
                cost += (flightTime - diff);
            }
        }

        //printf(">>> configureTrack [%d][%d] = %d\n", first.GetIndex(), second.GetIndex(), uniqueCost);
    }
    return cost;
}

void ARPUtil::showTrack(vector<Flight> &track){
    for(int i = 0; i < track.size(); i++){
        printf("%4d ", track[i].GetIndex());
    }
    printf("\n");
}

void ARPUtil::showSolution(vector< vector<Flight> > &solution) {
    cout << "N Trilho: " << solution.size() << endl;
    int cost = 0;
    int delay = 0;
    for (int i = 0; i < solution.size(); i++) {
        cout << solution[i].size() << " ";
        for (int j = 0; j < solution[i].size(); j++) {
            cout << solution[i][j].GetIndex() << " ";
            if (solution[i][j].GetCost() != 0) {
                printf("(%+2d)  ", solution[i][j].GetDelay());
                cost += solution[i][j].GetCost();
                delay += solution[i][j].GetDelay();
            }


        }
        cout << endl;
    }

    cout << " COST " << cost << endl;
    cout << " DELAY " << delay << endl;

}

int ARPUtil::calculeCost(vector<Flight> &track, Instance &instance) {
    int cost = 0;

    for (int i = 0; i < track.size(); i++) {
        Flight &first = track[i];
        cost += abs(first.GetCost());

        if (i != track.size() - 1) {
            Flight &next = track[i + 1];
            if (!first.validateGeographicalConstraint(&next)) {
                cost += instance.getFlightTime(first, next);
            }
        }

    }


    return cost;
}

int ARPUtil::calculeCost(vector< vector<Flight> > &r, Instance &instance) {
    int cost = r.size()*1000;


    for (int i = 0; i < r.size(); i++) {
        cost += calculeCost(r[i], instance);
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
                        if (minDelay < flight.GetDelay()) {
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

void ARPUtil::removeTrack(vector< vector<Flight> > *flights, int track) {
    vector< vector<Flight> >::iterator iRemove = flights->begin();
    advance(iRemove, track);
    flights->erase(iRemove);
}

void ARPUtil::copyFlights(vector<Flight> *target, vector<Flight> *source) {
    for (int i = 0; i < source->size(); i++) {
        target->push_back((*source)[i]);
    }
}

void ARPUtil::writeSolution(vector< vector<Flight> > * solution, ostream &saida) {
    saida << solution->size() << endl;

    for (int i = 0; i < solution->size(); i++) {
        saida << (*solution)[i].size();
        for (int j = 0; j < (*solution)[i].size(); j++) {
            Flight &f = (*solution)[i][j];
            saida << " " << f.GetIndex() << " " << f.GetDelay();
        }
        saida << endl;
    }
}

void ARPUtil::removeDelays(vector< vector <Flight> > * solution) {

    bool isImproving = false;
    do {
        
        //Trilho de origem
        for (int i = 0; i < solution->size() - 1; i++) {

            vector<Flight> &source_track = (*solution)[i];

            //Procurando voos com delays
            for (int i_2 = 1; i_2 < source_track.size(); i_2++) {
                //Flight &source_previous_flight = source_track[i_2 - 1];
                Flight &source_current_flight = source_track[i_2];
                //Flight *source_next_flight = (i_2 == (source_track.size() - 1)) ? NULL : &source_track[i_2 + 1];

                if (source_current_flight.GetDelay() != 0) {
                    
                    //Trilho de destino
                    for (int j = (i + 1); j < solution->size(); j++) {
                        vector<Flight> &target_track = (*solution)[j];
                        
                        //Procura por um voo compatível
                        for(int j_2 = 0; j_2 < target_track.size(); j_2++){
                            Flight &target_current_flight = target_track[j_2];
                            
                       //     if()
                        }
                    }
                }
            }



        }
    } while (isImproving);
}