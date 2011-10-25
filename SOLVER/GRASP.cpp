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
#include "ARPSolver.h"
#include <algorithm>
#include <climits>
#include "util/ARPUtil.h"

//################################

struct sort_pred {

    bool operator()(const std::pair<int, int> &left, const std::pair<int, int> &right) {
        return left.second < right.second;
    }
};
//################################

GRASP::GRASP(Parameters p, Instance *instance) {
    this->parameters = p;
    this->instance = instance;
}

void GRASP::setParameters(Parameters parameters) {
    this->parameters = parameters;
}

Parameters GRASP::getParameters() const {
    return parameters;
}

vector< vector<Flight> > GRASP::construct(vector<Flight*> flights) {

    vector< vector<Flight> > solution;
    vector< vector<Flight*> > currentSolution;
    vector<Flight> clonedFligts = cloneTrack(flights);
    int firstNotSelectedFlight = 0;

    // cout << " Iniciado a construção " << endl;

    for (unsigned int i = 0;; i++) {
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
        if (!canBeChosen(currentSolution, currentFlight)) {
            // cout << "CAN NOT BE CHOSEN " << firstNotSelectedFlight << endl;
            vector<Flight*> newtrack;
            newtrack.push_back(currentFlight);

            currentFlight->SetSelected(true);
            currentFlight->SetDelay(-parameters.GetMaxDelay()); //Opcional, mantem o primeiro voo ja com um adiantamento.
            currentSolution.push_back(newtrack);

            firstNotSelectedFlight++;
            continue;
        }

        int actualTrackIndex = 0; //MathUtil::getInstance().nextInt(currentSolution.size());


        vector<Flight*> &actualTrack = currentSolution[actualTrackIndex]; //Sorteia o proximo trilho a ser preenchido.
        Flight* actualFlight = actualTrack.back();

        Flight* selectedFlight = calculateNextFlight(actualFlight, clonedFligts);

        if (selectedFlight == NULL) {
            solution.push_back(cloneTrack(actualTrack));
            removeTrack(currentSolution, actualTrackIndex);
        } else {
            selectedFlight->SetSelected(true);
            actualTrack.push_back(selectedFlight);
            //}
            //else{
            //E necessario reposicionamento.
            //}
        }
    }

    for (int i = 0; i < currentSolution.size(); i++) {
        solution.push_back(cloneTrack(currentSolution[i]));
    }

    //cout << " Finalizado a construção " << endl;

    return solution;
}

vector< vector<Flight> > GRASP::localSearch(vector< vector<Flight> > flights, int baseTime, int strategy) {
    cout << "Strategia " << strategy << endl;
    vector<Flight> f;
    vector< pair<int /*Indice do trilho*/, int /*Utilização do trilho*/> > trackUtilizationVector; //Utilização dos trilhos
    vector<int> removeTracks;
    int maxSolverFlights = 350;
    bool randomic = true;

    if (!randomic) {
        //Calcula a utilização dos trilhos.
        int flightsSize = flights.size();
        for (int i = 0; i < flights.size(); i++) {
            int value = trackUtilization(baseTime, flights[i]);

            trackUtilizationVector.push_back(make_pair(i, value));
        }

        sort(trackUtilizationVector.begin(), trackUtilizationVector.end(), sort_pred());

    }
    /**
     * Pegando apenas os voos com piores aproveitamento de trilho dão resultados melhores que
     * mescla-los com os de melhor aproveitamento.
     */
    bool lowestTest = false;

    if (strategy == 2) lowestTest = !lowestTest;
    int minIndex = 0;
    int maxIndex = trackUtilizationVector.size() - 1;

    while (true) {


        int index = -1;
        pair<int, int> value;

        if (!randomic) {
            if (lowestTest) {
                index = minIndex;
                minIndex++;
            } else {
                index = maxIndex;
                maxIndex--;
            }

            if (strategy == 1) lowestTest = !lowestTest;

            value = trackUtilizationVector[index];
        }

        if (randomic) {
            index = MathUtil::getInstance().nextInt(flights.size());
            if (f.size() + flights[index].size() > maxSolverFlights) {
                cout << " Break 1 - maxSolverFlights " << maxSolverFlights << endl;
                break;
            }
        } else {
            //cout << "Index " << value.first << " Utilization: " << value.second << " NFlights: " << flights[value.first].size() <<  " Total: " << (f.size() + flights[value.first].size()) <<  endl;
            if (f.size() + flights[value.first].size() > maxSolverFlights) {
                cout << " Break 1 - maxSolverFlights " << maxSolverFlights << endl;
                break;
            }
        }



        // cout << value.first << " ";
        if (randomic) {
            ARPUtil::copyFlights(&f, &flights[index]);
            ARPUtil::removeTrack(&flights, index);
        } else {
            ARPUtil::copyFlights(&f, &flights[value.first]);
            removeTracks.push_back(value.first);
        }

        if (randomic) {
            if (flights.empty()) break;
        } else {
            if (minIndex > maxIndex) {
                cout << " Break 2 - last track added" << endl;
                break;
            }
        }
        //lowestTest = !lowestTest; // Descomentar para mesclar os trilhos de piores aproveitamento com os de melhores
    }
    //cout << endl;

    if (!randomic) {
        sort(removeTracks.begin(), removeTracks.end());

        for (int i = 0; i < removeTracks.size(); i++) {
            ARPUtil::removeTrack(&flights, removeTracks[removeTracks.size() - 1 - i]);
        }
    }




    long initTime = time(NULL);
    vector< vector<Flight> > localSolution = ARPSolver::solver(&f, instance);

    printf("Duracao do solver %d\n", (int) (time(NULL) - initTime));


    for (int i = 0; i < localSolution.size(); i++) {
        flights.push_back(localSolution[i]);
    }

    return flights;

}

Flight* GRASP::calculateNextFlight(Flight *actualFlight, vector<Flight> &clonedFlights) {
    int selectedArc = randomizingArc();
    vector<Flight*> candidateList;
    bool selectedsArcs[4];

    selectedsArcs[0] = false;
    selectedsArcs[1] = false;
    selectedsArcs[2] = false;
    selectedsArcs[3] = false;

    selectedsArcs[selectedArc] = true;

    //Seleção da lista de voos adjacentes
    while (true) {
        candidateList = extractAdjacentFlight(actualFlight, clonedFlights, selectedArc);

        if (candidateList.empty()) {

            selectedsArcs[selectedArc] = true;

            if (selectedsArcs[0] == true && selectedsArcs[1] == true) {
                return NULL;
            }

            if (selectedsArcs[0] == false) {
                selectedArc = 0;
            } else {
                selectedArc = 1;
            }
        } else {
            break;
        }
    }

    int indexRCL = RCLIndex(actualFlight, candidateList, parameters.GetAlfa(), selectedArc);

    return candidateList[MathUtil::getInstance().nextInt(indexRCL)];

}

int GRASP::RCLIndex(Flight *actualFlight, vector<Flight*> candidateList, int alfa, int selectedArc) {

    float realAlfa = alfa / 100.f;


    if (selectedArc == 0) {
        unsigned int menor = candidateList[0]->GetDepartureTime();
        unsigned int maior = candidateList[candidateList.size() - 1]->GetDepartureTime();

        unsigned int ref = menor + ((maior - menor) * realAlfa);

        for (int i = 0; i < candidateList.size(); i++) {
            if (candidateList[i]->GetDepartureTime() > ref) {
                return i;
            }
        }

        return candidateList.size();

    } else {

        unsigned int menor = abs(candidateList[0]->GetDelay());
        unsigned int maior = abs(candidateList[candidateList.size() - 1]->GetDelay());

        unsigned int ref = menor + ((maior - menor) * realAlfa);

        for (int i = 0; i < candidateList.size(); i++) {
            if (abs(candidateList[i]->GetDelay()) > ref) {
                return i;
            }
        }

        return candidateList.size();

    }

}

vector<Flight*> GRASP::extractAdjacentFlight(Flight *actualFlight, vector<Flight> &clonedFlights, int selectedArc) {
    switch (selectedArc) {
        case 0: return extractAdjacentFlightArcType1(actualFlight, clonedFlights);
        case 1: return extractAdjacentFlightArcType2(actualFlight, clonedFlights);
        case 2: return extractAdjacentFlightArcType3(actualFlight, clonedFlights);
        case 3: return extractAdjacentFlightArcType4(actualFlight, clonedFlights);
        default: exit(1);
    }

}

vector<Flight*> GRASP::extractAdjacentFlightArcType1(Flight* actualFlight, vector<Flight> &candidateList) {
    //Como o candidato tem que partir depois do voo atual.
    int candidateNumber = actualFlight->GetIndex() + 1;
    vector<Flight> &flights = candidateList;
    vector<Flight*> adjacentFlights;
    int numberOfFlights = flights.size();
    Flight* candidate = NULL;


    /*
     * Para todos os voos faca:
     * 	se ainda nao esta em nenhum trilho
     * 	     se a ligacao e direta
     *                e nao e necessario atraso
     * 		       adicione este voo aos voos_adjacentes
     */
    while (candidateNumber < numberOfFlights) {
        candidate = &flights[candidateNumber];

        if (!candidate->IsSelected()
                && actualFlight->validateGeographicalConstraint(candidate)
                && actualFlight->validateTemporalConstraint(candidate, 0)) {

            //            int delay = candidate->GetDepartureTime() - actualFlight->GetRealArrivalTime();
            //
            //            if(delay >= parameters.GetMaxDelay()){
            //                candidate->SetDelay(-parameters.GetMaxDelay());
            //            }
            //            else{
            //                candidate->SetDelay(-delay);
            //            }


            candidate->SetDelay(0);
            adjacentFlights.push_back(candidate);


        }

        candidateNumber++;
    }


    //sort(adjacentFlights);
    /**
     * Como os voos já vem ordenado por tempo de partida essa lista já se encontra ordenada.
     */
    return adjacentFlights;
}

vector<Flight*> GRASP::extractAdjacentFlightArcType2(Flight* actualFlight, vector<Flight> &candidateList) {
    //Como o candidato tem que partir depois do voo atual.
    int candidateNumber = actualFlight->GetIndex() + 1;
    vector<Flight> &flights = candidateList;
    vector<Flight*> adjacentFlights;
    int numberOfFlights = flights.size();
    Flight* candidate = NULL;


    /*
     * Para todos os voos faca:
     * 	se ainda nao esta em nenhum trilho
     * 	     se a ligacao e direta
     *                e nao e necessario atraso
     * 		       adicione este voo aos voos_adjacentes
     */
    while (candidateNumber < numberOfFlights) {
        candidate = &flights[candidateNumber];

        if (!candidate->IsSelected()
                && actualFlight->validateGeographicalConstraint(candidate)) {

            int delay = actualFlight->requiredDelay(candidate);

            if (delay != 0 && delay < parameters.GetMaxDelay()) {
                candidate->SetDelay(delay);
                adjacentFlights.push_back(candidate);
            }

        }

        candidateNumber++;
    }

    //sort(adjacentFlights);
    /**
     * Como os voos já vem ordenado por tempo de partida essa lista já se encontra ordenada.
     */
    return adjacentFlights;
}

vector<Flight*> GRASP::extractAdjacentFlightArcType3(Flight* actualFlight, vector<Flight> &candidateList) {

    //sort(adjacentFlights);
    return vector<Flight*>();
}

vector<Flight*> GRASP::extractAdjacentFlightArcType4(Flight* actualFlight, vector<Flight> &candidateList) {

    //sort(adjacentFlights);
    return vector<Flight*>();
}

bool GRASP::canBeChosen(vector< vector<Flight*> > &tracks, Flight *flightCandidate) {

    //cout << " SIze " << tracks.size() << endl;

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

    //cout << ">>>> " << initialFlight->validateGeographicalConstraint(finalFlight) << endl;
    //cout << ">>>> " << initialFlight->validateTemporalConstraint(finalFlight, 0) << endl;

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

/**
 * Obtem o grau de utilização de um trilho.
 * @param baseTime O tempo maximo de utilização de um trilho.
 * @param track O trilho a ser avaliado.
 * @return O grau de utilização do trilho, o valor estará entre [baseTime, 0);
 */
int GRASP::trackUtilization(int baseTime, vector<Flight> &track) {
    for (int i = 0; i < track.size(); i++) {
        baseTime -= track[i].GetDuration();
    }

    return baseTime;
}

int GRASP::calculeBaseTime(vector<Flight*> flights) {
    int minTime = INT_MAX, maxTime = 0;

    for (int i = 0; i < flights.size(); i++) {
        Flight *f = flights[i];

        if (f->GetDepartureTime() < minTime) {
            minTime = f->GetDepartureTime();
        }

        if ((f->GetDepartureTime() + f->GetDuration()) > maxTime) {
            maxTime = f->GetDepartureTime() + f->GetDuration();
        }
    }

    return maxTime - minTime;

}

void GRASP::readInput(char *file) {

    cout << " Lendo entrada " << endl;
    ifstream ifs(file, ifstream::in);

    Instance instance = Instance::read(ifs);

    ifs.close();

    cout << " Finalizado leitura de entrada " << endl;

    vector<Flight*> * flights = instance.getFlights();
    int baseTime = calculeBaseTime(*flights);
    unsigned int alfa = 50;
    unsigned int probabilityArcType1 = 79;
    unsigned int probabilityArcType2 = 16;
    unsigned int probabilityArcType3 = 4;
    unsigned int probabilityArcType4 = 1;

    Parameters p(instance.getMaxDelay(), probabilityArcType1,
            probabilityArcType2, probabilityArcType3,
            probabilityArcType4, alfa);

    cout << " Criado o GRASP " << endl;
    GRASP grasp(p, &instance);

    vector< vector<Flight> > bestresult;
    int bestCost = 999999;
    int bestIter = 0;

    for (int i = 0; i < 1; i++) {
         
        cout << "Construindo a solução (Iteracao " << i << ")" << endl;
        
        vector< vector<Flight> > result = grasp.construct(*flights);
        ARPUtil::relaxDelays(result);
        
        cout << "Delays relaxados" << endl;

        int initCost = ARPUtil::calculeCost(result, instance);
        int finalCost;
        int nLocalSearch = 0;
        int strategy = 0;
        
        cout << "Custos calculados" << endl;
        
        if(true){
            cout << "Inicinado a nova busca local" << endl;
            LocalSearchUtil::crossOver(result);
           // break;
        }
        
        if(false)
        while (true) {
            nLocalSearch++;
            result = grasp.localSearch(result, baseTime, strategy);


            finalCost = ARPUtil::calculeCost(result, instance);

            cout << " InitialCost =  " << initCost << " - FinalCost: " << finalCost << endl;
            if (finalCost >= initCost) {
                if (strategy < 5) {
                    strategy++;
                    cout << "Mudando a estrategia " << strategy << endl;
                } else {
                    break;
                }
            } else {
                strategy = 0;
            }

            initCost = finalCost;

        }

        cout << "FIM LOCAL SEARCH( " << i << "): Try " << nLocalSearch << " times." << endl;


        if (finalCost < bestCost) {
            bestCost = finalCost;
            bestresult = result;
            bestIter = i;
            long diff = clock() / (CLOCKS_PER_SEC / 1000);
            cout << "Best Iter:" << i << " Best:" << bestCost << " Tempo:" << diff << endl;
        }
    }

    ofstream ofs("saida.txt", ofstream::out);
    ARPUtil::writeSolution(&bestresult, ofs);
    ofs.flush();

    cout << " Finalizado o GRASP " << endl;
    cout << file << " " << bestIter << " " << bestCost << endl;

    ARPUtil::showSolution(bestresult);

}

/**
 * Retorna o tipo do arco sorteado de acordo com as probabilidades
 * definidas no AircraftRotationParameters.
 *
 * @return
 *
 * <ul>
 *  <li> 0  - Arco tipo 1 </li>
 *  <li> 1  - Arco tipo 2 </li>
 *  <li> 2  - Arco tipo 3 </li>
 *  <li> 3  - Arco tipo 4 </li>
 * </ul>
 *
 * -1 indica um erro na configuração das probabilidades dos tipos de arcos.
 */
int GRASP::randomizingArc() {

    int number = MathUtil::getInstance().nextInt(100) + 1;

    if (number <= this->parameters.GetProbabilityArcType1()) {
        return 0;
    }

    number -= this->parameters.GetProbabilityArcType1();

    if (number <= this->parameters.GetProbabilityArcType2()) {
        return 1;
    }

    number -= this->parameters.GetProbabilityArcType2();

    if (number <= this->parameters.GetProbabilityArcType3()) {
        return 2;
    }

    number -= this->parameters.GetProbabilityArcType3();

    if (number <= this->parameters.GetProbabilityArcType4()) {
        return 3;
    }

    return -1;

}
