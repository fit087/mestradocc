/* 
 * File:   LocalSearchUtil.cpp
 * Author: alexander
 * 
 * Created on September 5, 2011, 8:26 AM
 */

#include "LocalSearchUtil.h"
#include "ARPUtil.h"

LocalSearchUtil::LocalSearchUtil() {
}

LocalSearchUtil::LocalSearchUtil(const LocalSearchUtil& orig) {
}

LocalSearchUtil::~LocalSearchUtil() {
}

void LocalSearchUtil::crossOver(vector< vector<Flight> > &solution) {
    for (unsigned int i = 0; i < solution.size(); i++) {

        cout << "Avaliando o trilho " << i << endl;
        vector<Flight> &trackI = solution[i];
        bool changeSucess = false;

        for (unsigned k = 0; k < trackI.size(); k++) {
            Flight &candidate1 = trackI[k];
            if (candidate1.GetDelay() < 0) {
                Flight &nextFlight = trackI[k + 1];

            } 
            
            /**
             * 
             * O objetivo é a remocao do atraso de candidate1
             * 
             *   [A|backFlight1|B]
             *                  >>[B|candidate1|C]
             * 
             * [X|backFlight2|B]  [B|candidate2|Y]
             * 
             * 1. A cidade de partida dos candidatos são iguais
             * 2. O candidato2 pode efetuar ligação com backFlight1 (com o tempo dele, inclui o atraso dele)
             * 3. O candidato1 pode efetuar ligação com o backFligh2 (se houver backFlight2)
             * 4. 
             * 
             */
            else if (candidate1.GetDelay() > 0) {
                Flight &backFlight1 = trackI[k - 1];

                for (unsigned int j = 0; j < solution.size(); j++) {
                    if (i == j) continue;
                    vector<Flight> &trackJ = solution[j];
                    
                    for(unsigned int m = 0; m < trackJ.size(); m++){
                        Flight &candidate2 = trackJ[m];
                        
                        if(candidate2.GetDepartureCity() == candidate1.GetDepartureCity()){
                            if(candidate2.GetRealDepartureTime() >= backFlight1.GetRealArrivalTime()){
                                if(m != 0){
                                    Flight backFlight2 = trackJ[m-1];
                                    //Se o candidato 1 não puder ligar de forma direta (sem atraso) com esse voo
                                    //Pode-se utilizar como objetivo a redução do atraso
                                    //No caso atual apenas a remoção é levada em consideração.
                                    if(!(backFlight2.GetRealArrivalTime() <= candidate1.GetDepartureTime())){
                                        continue;
                                    }
                                }
                                
                                cout << "Efetuando um crossover " << endl;
                                
                                /**
                                 * Faz a troca dos trechos (cross-over)
                                 * @param solution
                                 */
                                //Remove atraso do candidate1
                                candidate1.SetDelay(0);
                                swapParts(i, k, j, m, solution);
                                
                                changeSucess = true;
                                break;
                                
                            }
                        }
                        
                    }
                    
                    if(changeSucess) break;

                }
            }
            
            if(changeSucess) break;
        }

        //Descomente a linha abaixo para a re-executar com o trilho que foi modificado.
        //if(changeSucess){ i--; } 
    }
}

void LocalSearchUtil::swapParts(int t1_i, int f1_i, int t2_i, int f2_i, vector< vector<Flight> > &solution){
    vector<Flight> temp;
    cout << "t1 " << endl;
    vector<Flight> &track1 = solution[t1_i];
    cout << "t2 " << endl;
    vector<Flight> &track2 = solution[t2_i];
    cout << "t3 " << endl;
    
    
    //DEBUG//
    //Inicial
    
    printf("------- SWAP ------\n");
    printf("Track %d:", t1_i);
    ARPUtil::showTrack(track1);
    printf("Track %d:", t2_i);
    ARPUtil::showTrack(track2);
    cout << "t4 " << endl;
    
    copy(track1.begin()+f1_i, track1.end(), temp.begin());
    
    cout << "T5" << endl;
    track1.erase(track1.begin() + f1_i, track1.end());
    
    cout << "T6" << endl;
    copy(track2.begin() + f2_i, track2.end(), track1.begin() + f1_i);
    
    cout << "T7" << endl;
    track2.erase(track2.begin() + f2_i, track2.end());
    
    cout << "T8" << endl;
    copy(temp.begin(), temp.end(), track2.begin() + f2_i);
    
    //DEBUG FINAL
    printf("Apos o swap\n");
    printf("Track %d:", t1_i);
    ARPUtil::showTrack(track1);
    printf("Track %d:", t2_i);
    ARPUtil::showTrack(track2);
    printf("------- ---- ------\n");
    
}

