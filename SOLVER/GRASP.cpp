/* 
 * File:   GRASP.cpp
 * Author: alexanderdealmeidapinto
 * 
 * Created on 17 de Abril de 2011, 11:19
 */

#include <fstream>

#include "GRASP.h"
#include "util/MathUtil.h"
#include "Instance.h"

GRASP::GRASP(Parameters p) {
    this->parameters = p;
}

void GRASP::setParameters(Parameters parameters) {
    this->parameters = parameters;
}

Parameters GRASP::getParameters() const {
    return parameters;
}

vector< vector<Flight> > GRASP::construct(vector<Flight*> flights) {
    vector< vector<Flight> > solution;
    vector< vector<Flight*> > current;
    vector<Flight> clonedFligts = cloneTrack(flights);
    int firstNotSelectedFlight = 0;

    for (unsigned int i = 0;; i++) {
        //cout << " Iteration " << i << endl;
        Flight *currentFlight = &clonedFligts[firstNotSelectedFlight];

        if (firstNotSelectedFlight == clonedFligts.size()) {
            break;
        }

        if (currentFlight->IsSelected()) {
            //cout << " IS SELECTED " << firstNotSelectedFlight << endl;
            firstNotSelectedFlight++;
            continue;
        }

        /*
         * Se o voo nao pode ser escolhido para nenhum dos trilhos correntes
         * entao cria-se um novo trilho com ele.
         */
        if (!canBeChosen(current, currentFlight)) {
            // cout << "CAN NOT BE CHOSEN " << firstNotSelectedFlight << endl;
            vector<Flight*> newtrack;
            newtrack.push_back(currentFlight);

            currentFlight->SetSelected(true);
            currentFlight->SetDelay(-parameters.GetMaxDelay()); //Opcional, mantem o primeiro voo ja com um adiantamento.
            current.push_back(newtrack);

            firstNotSelectedFlight++;
            continue;
        }

        int actualTrackIndex = MathUtil::getInstance().nextInt(current.size());


        vector<Flight*> &actualTrack = current[actualTrackIndex]; //Sorteia o proximo trilho a ser preenchido.
        Flight* actualFlight = actualTrack.back();

        Flight* selectedFlight = calculateNextFlight(actualFlight);

        if (selectedFlight == NULL) {
            solution.push_back(cloneTrack(actualTrack));
            removeTrack(current, actualTrackIndex);
        }
    }

    for (int i = 0; i < current.size(); i++) {
        solution.push_back(cloneTrack(current[i]));
    }

    return solution;
}

Flight* GRASP::calculateNextFlight(Flight *actualFlight) {
    return NULL;
}

bool GRASP::canBeChosen(vector< vector<Flight*> > &tracks, Flight *flightCandidate) {

    // cout << " SIze " << tracks.size() << endl;

    for (unsigned int i = 0; i < tracks.size(); i++) {
        Flight *lastFlight = tracks[i][tracks[i].size() - 1]; //Ultimo voo do trilho.



        if (canSucceedDirect(lastFlight, flightCandidate) ||
                canSuccedInderect(lastFlight, flightCandidate)) {
            return true;
        }

    }

    return false;
}

bool GRASP::canSucceedDirect(Flight *initialFlight, Flight *finalFlight) {
    unsigned int maxDelay = this->parameters.GetMaxDelay();

    if (initialFlight->validateGeographicalConstraint(finalFlight) &&
            initialFlight->validateTemporalConstraint(finalFlight, (int) maxDelay)) {
        return true;
    }

    return false;
}

bool GRASP::canSuccedInderect(Flight *initialFlight, Flight *finalFlight) {
    return false;
}

void GRASP::removeTrack(vector< vector<Flight*> > &track, int index) {
    vector< vector<Flight*> >::iterator i = track.begin();
    advance(i, index);
    track.erase(i);
}

vector<Flight> GRASP::cloneTrack(vector<Flight*> track) {
    vector<Flight> retorno;

    for (unsigned int i = 0; i < track.size(); i++) {
        Flight f = Flight((*track[i]));
        //cout << "VOO " << f.IsSelected() << endl;
        retorno.push_back(f); //Clona o voo
    }

    return retorno;
}

void GRASP::readInput(char *file) {

    ifstream ifs(file, ifstream::in);

    Instance inst = Instance::read(ifs);

    ifs.close();

    vector<Flight*> flights = inst.getFlights();
    

    Parameters p(inst.getMaxDelay());
    GRASP grasp(p);
    vector< vector<Flight> > result = grasp.construct(flights);

    cout << "Numero de trilhos " << result.size() << endl;

}