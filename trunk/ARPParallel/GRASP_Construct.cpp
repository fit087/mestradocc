/* 
 * File:   GRASP_Construct.cpp
 * Author: alexander
 * 
 * Created on September 26, 2011, 5:25 PM
 */

#include "GRASP_Construct.h"
#include "InstanceUtil.h"
#include "MathUtil.h"

vector< vector<Flight> > GRASP_Construct::construct(Instance *instance) {

    vector< vector<Flight> > result;
    vector<Flight> clonedFlights = InstanceUtil::cloneFlights(instance->GetMineFlights());
    vector<Flight> actualTrack;
    int firstNotSelectedFlight = 0;

    for (unsigned int i = 0;; i++) {
        Flight *currentFlight = &clonedFlights[firstNotSelectedFlight];
        actualTrack.clear();
        //All flights selected ?
        if (firstNotSelectedFlight == clonedFlights.size()) {
            break;
        }

        //This flight is selected?
        if (currentFlight->IsSelected()) {
            firstNotSelectedFlight++;
            continue;
        }

        currentFlight->SetSelected(true);
        actualTrack.push_back(*currentFlight);

        while (true) {
            Flight* actualFlight = &actualTrack.back();
            Flight* selectedFlight = calculateNextFlight(actualFlight, clonedFlights, instance);

            if (selectedFlight == NULL) {
                //Log::d("Adicionando trilho");
                result.push_back(actualTrack);
                break;
            } else {
                selectedFlight->SetSelected(true);
                actualTrack.push_back(*selectedFlight);
            }
        }

    }

    if(!actualTrack.empty()){
        //Log::d("Adicionando trilho no final");
        result.push_back(actualTrack);
    }


    return result;

}

vector<Flight*> GRASP_Construct::extractAdjacentFlight(Flight *actualFlight, vector<Flight> &clonedFlights, int selectedArc, Instance *instance) {
    switch (selectedArc) {
        case 0: return extractAdjacentFlightArcType1(actualFlight, clonedFlights);
        case 1: return extractAdjacentFlightArcType2(actualFlight, clonedFlights, instance);
        case 2: return extractAdjacentFlightArcType3(actualFlight, clonedFlights);
        case 3: return extractAdjacentFlightArcType4(actualFlight, clonedFlights, instance);
        default: exit(1);
    }

}

vector<Flight*> GRASP_Construct::extractAdjacentFlightArcType1(Flight* actualFlight, vector<Flight> &candidateList) {
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

vector<Flight*> GRASP_Construct::extractAdjacentFlightArcType2(Flight* actualFlight, vector<Flight> &candidateList, Instance *instance) {
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

            if (delay != 0 && delay < instance->getParameters().GetMaxDelay()) {
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

vector<Flight*> GRASP_Construct::extractAdjacentFlightArcType3(Flight* actualFlight, vector<Flight> &candidateList) {

    //sort(adjacentFlights);
    return vector<Flight*>();
}

vector<Flight*> GRASP_Construct::extractAdjacentFlightArcType4(Flight* actualFlight, vector<Flight> &candidateList, Instance *instance) {

    //sort(adjacentFlights);
    return vector<Flight*>();
}

Flight* GRASP_Construct::calculateNextFlight(Flight *actualFlight, vector<Flight> &clonedFlights, Instance *instance) {
    int selectedArc = randomizingArc(instance);
    vector<Flight*> candidateList;
    bool selectedsArcs[4];

    selectedsArcs[0] = false;
    selectedsArcs[1] = false;
    selectedsArcs[2] = false;
    selectedsArcs[3] = false;

    selectedsArcs[selectedArc] = true;

    //Seleção da lista de voos adjacentes
    while (true) {
        candidateList = extractAdjacentFlight(actualFlight, clonedFlights, selectedArc, instance);

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

    int indexRCL = RCLIndex(actualFlight, candidateList, instance->getParameters().GetAlfa(), selectedArc);

    return candidateList[MathUtil::getInstance().nextInt(indexRCL)];

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
int GRASP_Construct::randomizingArc(Instance *instance) {

    int number = MathUtil::getInstance().nextInt(100) + 1;

    if (number <= instance->getParameters().GetProbabilityArcType1()) {
        return 0;
    }

    number -= instance->getParameters().GetProbabilityArcType1();

    if (number <= instance->getParameters().GetProbabilityArcType2()) {
        return 1;
    }

    number -= instance->getParameters().GetProbabilityArcType2();

    if (number <= instance->getParameters().GetProbabilityArcType3()) {
        return 2;
    }

    number -= instance->getParameters().GetProbabilityArcType3();

    if (number <= instance->getParameters().GetProbabilityArcType4()) {
        return 3;
    }

    return -1;

}

int GRASP_Construct::RCLIndex(Flight *actualFlight, vector<Flight*> candidateList, int alfa, int selectedArc) {

    float realAlfa = alfa / 100.f;


    if (selectedArc == 0) {
        unsigned int menor = candidateList[0]->GetInitTime();
        unsigned int maior = candidateList[candidateList.size() - 1]->GetInitTime();

        unsigned int ref = menor + ((maior - menor) * realAlfa);

        for (int i = 0; i < candidateList.size(); i++) {
            if (candidateList[i]->GetInitTime() > ref) {
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

