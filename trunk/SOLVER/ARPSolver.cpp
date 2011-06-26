/* 
 * File:   ARPSolver.cpp
 * Author: daniel
 * 
 * Created on April 10, 2011, 12:43 PM
 */

#include "ARPSolver.h"
#include "util/ARPUtil.h"
//#include <cmath>
#include <iostream>

#define MAX_COST 99999

#define EXTENDED false
#define ISDEBUG false
#define ISDEBUG2 false


ARPSolver::ARPSolver() {
}

ARPSolver::ARPSolver(const ARPSolver& orig) {
}

ARPSolver::~ARPSolver() {
}

void ARPSolver::adjustTime() {
    int r1 = system("sudo date +%H%M > dt.tmp");
    int r2 = system("sudo date 1215$(cat dt.tmp)2005 > dt.tmp");
    int r3 = system("sudo rm dt.tmp");

    if (r1 == -1 || r2 == -1 || r3 == -1) {
        printf("Erro ao ajustar o tempo");
        exit(1);
    }
}

/**
 * Calcula o custo minimo de um arco que saida da orig para o dest
 * @param l Lista que contem os voos
 * @param orig Indice do voo de origem
 * @param dest Indice do voo de destino
 * @return O custo do arco ou MAX_COST caso nao seja possivel a ligacao.
 */
int ARPSolver::costOfArc(vector<Flight> *v, map< int, map< int, int > > *distanceTimes, int orig, int dest, int maxDelay) {
    Flight &o = (*v)[orig];
    Flight &d = (*v)[dest];

    if (o.GetArrivalCity() == d.GetDepartureCity()) {
        int diff = (d.GetDepartureTime()) - (o.GetDepartureTime() + o.GetDuration());

        //Se tem tempo de ligar direto (custo 0 - arco do tipo 1)
        if (diff >= 0) {
            return 0;
        } else {
            
            diff += maxDelay;
            //Se tem tempo de ligar com o delay (custo 0 - arco do tipo 1)
            if (diff >= 0) {
                if(EXTENDED) return 0;
                else return maxDelay - diff;
            } else { //Se nao tem tempo (CUSTO MAXIMO)
                return MAX_COST;
            }
        }
    }//Se as cidades forem diferentes.
    else {
        int diff = (d.GetDepartureTime()) - (o.GetDepartureTime() + o.GetDuration());
        int origIndex = o.GetArrivalCity();
        int destIndex = d.GetDepartureCity();

        if (destIndex < origIndex) {
            int temp = origIndex;
            origIndex = destIndex;
            destIndex = temp;
        }

        int flightTime = (*distanceTimes)[origIndex][destIndex];
        diff -= flightTime;

        if (diff >= 0) {
            return flightTime;
        } else {

            //return MAX_COST; // Essa versao nao permite reposicionamento.

            diff += maxDelay;

            if (diff >= 0) {
                if(EXTENDED) return flightTime;
                return flightTime + (maxDelay - diff);
            }
        }
    }

    return MAX_COST;
}

int ARPSolver::costOfArc1(vector<Flight> *v, int orig, int dest) {
    Flight &o = (*v)[orig];
    Flight &d = (*v)[dest];

    if (o.GetArrivalCity() == d.GetDepartureCity()) {
        int diff = (d.GetDepartureTime()) - (o.GetDepartureTime() + o.GetDuration());

        //printf(" TIME =  %d \n", time);
        //Se tem tempo de ligar direto (custo 0 - arco do tipo 1)
        if (diff >= 0) {
            return 0;
        } else {

            return MAX_COST;
        }
    }

    return MAX_COST;
}

int ARPSolver::costOfArc2(vector<Flight> *v, int orig, int dest, int maxDelay) {
    Flight &o = (*v)[orig];
    Flight &d = (*v)[dest];

    if (o.GetArrivalCity() == d.GetDepartureCity()) {
        int diff = (d.GetDepartureTime()) - (o.GetDepartureTime() + o.GetDuration());

        //printf(" TIME =  %d \n", time);
        //Se tem tempo de ligar direto (custo 0 - arco do tipo 1)
        if (diff >= 0) {
            return MAX_COST;
        } else {

            diff += maxDelay;
            //Se tem tempo de ligar com o delay (custo 0 - arco do tipo 1)
            if (diff >= 0) {
                return maxDelay - diff;
            } else { //Se nao tem tempo (CUSTO MAXIMO)
                return MAX_COST;
            }
        }
    }

    return MAX_COST;
}

int ARPSolver::costOfArc3(vector<Flight> *v, map< int, map< int, int > > *distanceTimes, int orig, int dest) {
    Flight &o = (*v)[orig];
    Flight &d = (*v)[dest];

    if (o.GetArrivalCity() != d.GetDepartureCity()) {
        int diff = (d.GetDepartureTime()) - (o.GetDepartureTime() + o.GetDuration());
        int origIndex = o.GetArrivalCity();
        int destIndex = d.GetDepartureCity();

        if (destIndex < origIndex) {
            int temp = origIndex;
            origIndex = destIndex;
            destIndex = temp;
        }

        int flightTime = (*distanceTimes)[origIndex][destIndex];
        diff -= flightTime;

        if (diff >= 0) {
            return flightTime;
        }
        //FAZ O REPOSICIONAMENTO E OBTEM O CUSTO DELES.
    }

    return MAX_COST;
}

int ARPSolver::costOfArc4(vector<Flight> *v, map< int, map< int, int > > *distanceTimes, int orig, int dest, int maxDelay) {
    Flight &o = (*v)[orig];
    Flight &d = (*v)[dest];

    if (o.GetArrivalCity() != d.GetDepartureCity()) {
        int diff = (d.GetDepartureTime()) - (o.GetDepartureTime() + o.GetDuration());
        int origIndex = o.GetArrivalCity();
        int destIndex = d.GetDepartureCity();

        if (destIndex < origIndex) {
            int temp = origIndex;
            origIndex = destIndex;
            destIndex = temp;
        }

        int flightTime = (*distanceTimes)[origIndex][destIndex];
        diff -= flightTime;

        if (diff >= 0) {
            return MAX_COST;
        } else {

            //return MAX_COST; // Essa versao nao permite reposicionamento.

            diff += maxDelay;

            if (diff >= 0) {
                return flightTime + (maxDelay - diff);
            }
        }
    }

    return MAX_COST;
}

void ARPSolver::showVariables(IloCplex &model, IloIntVarArray vars[], IloIntVar delay[], int n) {
    //    printf("\nVariaveis X - %d\n", n);
    //    for(int i = 0 ; i < n; i++){
    //        int qtde = 0;
    //        for(int j =0; j < n; j++){
    //
    //            IloInt v = model.getValue(vars[i][j]);
    //            if(v == 1) qtde++;
    //            printf("%-4d  ", (int)v);
    //        }
    //
    //        if(qtde != 1){
    ////            printf("\n\n-----> (%d)", i);
    ////            exit(1);
    //        }
    //
    //        printf("\n");
    //    }

    printf("\nVariaveis Delay \n");
    for (int i = 0; i < n - 2; i++) {
        IloInt v = model.getValue(delay[i]);
        if (v != 0)
            printf("(%d) %-4d  ", i, (int) v);
    }
    printf("\n");
}

void ARPSolver::showCosts(IloNumArray cost[], int n) {
    printf("\nCustos\n");
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            printf("%-4d  ", (int) cost[i][j]);
        }
        printf("\n");
    }
}



void ARPSolver::finalizeTrail(vector<Flight> *flight, vector<Flight> *trail, IloCplex &cplex, IloBoolVarArray vars[], IloIntArray cost[], IloIntVar delay[], int n) {
    while (true) {
        Flight &previousFlight = (*trail)[(*trail).size() - 1];
        int previousIndex = previousFlight.GetIlogIndex();

        int qtde = 0;
        for (int i = 0; i < n; i++) {

            IloInt v = cplex.getValue(vars[previousIndex][i]);

            if (v == 1) {
                qtde++;

                if (i == (n - 1)) return;

                int fcost = 0;
                if (EXTENDED && (i < n - 2)) {
                    fcost = cplex.getValue(delay[i]);
                } else {
                    fcost = cost[previousIndex][i];
                }

                (*flight)[i].SetCost(fcost);
                trail->push_back((*flight)[i]);
                break;
            }
        }

        if (qtde == 0) {
            cout << "Saiu aqui, o voo " << previousFlight.GetIlogIndex() << endl;
            exit(1);
        }
    }

    return;
}

void ARPSolver::finalizeTrail2(vector<Flight> *flight, vector<Flight> *trail, IloCplex &cplex, IloBoolVarArray** x, IloIntArray** cost, int n) {
    while (true) {
        Flight &previousFlight = (*trail)[(*trail).size() - 1];
        int previousIndex = previousFlight.GetIlogIndex();

        int qtde = 0;
        for (int coluna = 0; coluna < n; coluna++) {

            for(int arc = 0; arc < 4; arc++){
                IloInt v = cplex.getValue(x[previousIndex][coluna][arc]);

                if (v == 1) {
                    qtde++;

                    if (coluna == (n - 1)) return;

                    int fcost = cost[previousIndex][coluna][arc];

                    (*flight)[coluna].SetCost(fcost);
                    trail->push_back((*flight)[coluna]);
                    break;
                }
            }
        }

        if (qtde == 0) {
            cout << "Saiu aqui, o voo " << previousFlight.GetIlogIndex() << endl;
            exit(1);
        }
    }

    return;
}

vector< vector<Flight> > ARPSolver::assembleResult(vector<Flight> *flight, Instance *instance, IloCplex &cplex, IloBoolVarArray x[], IloIntArray cost[], IloIntVar delay[], int n) {

    int sourceIndex = (n - 2);

    vector< vector<Flight> > result;

    for (int i = 0; i < n - 2; i++) {

        if (ISDEBUG) {
            printf("Construção do trilho %d\n", i);
        }
        IloInt v = cplex.getValue(x[sourceIndex][i]);

        if (v == 1) {
            vector<Flight> track;
            track.push_back((*flight)[i]);
            if (ISDEBUG) {
                cout << " Finalizando trilho" << endl;
            }
            finalizeTrail(flight, &track, cplex, x, cost, delay, n);
            int finalCost = ARPUtil::configureTrack(track, instance);
            result.push_back(track);
            if (ISDEBUG)
                printf("Quantidade = %d\n", (int) track.size());
        }
    }



    return result;
}

vector< vector<Flight> > ARPSolver::assembleResult2(vector<Flight> *flight, Instance *instance, IloCplex &cplex, IloBoolVarArray** x, IloIntArray** cost, int n) {

    int sourceIndex = (n - 2);

    vector< vector<Flight> > result;

    for (int coluna = 0; coluna < n - 2; coluna++) {
        for(int arc = 0; arc < 4; arc++){
            if (ISDEBUG) {
                printf("Construção do trilho %d\n", coluna);
            }

            int v = (int)cplex.getValue(x[sourceIndex][coluna][arc]);

            if (v == 1) {
                printf("Um\n");
                vector<Flight> track;
                track.push_back((*flight)[coluna]);
                if (ISDEBUG) {
                    cout << " Finalizando trilho" << endl;
                }
                finalizeTrail2(flight, &track, cplex, x, cost, n);
                int finalCost = ARPUtil::configureTrack(track, instance);
                result.push_back(track);
                if (ISDEBUG)
                    printf("Quantidade = %d\n", (int) track.size());
            }
        }
    }



    return result;
}

vector< vector<Flight> > ARPSolver::solver(vector<Flight> *v, Instance *instance) {
    if(ISDEBUG)
        cout << "Solver With " << v->size() << " Flights " << endl;

    int maxDelay = instance->getMaxDelay();


    for (int i = 0; i < v->size(); i++) {
        (*v)[i].SetIlogIndex(i);
    }

    if (v->empty()) {
        cout << "Lista vazia" << endl;
    }

    IloEnv env;

    try {
        IloModel model(env);


        //size + 1 e size + 2 sao os nos de origem e o de fim.
        IloInt n = v->size() + 2;
        IloBoolVarArray x[n];
        IloIntArray cost[n];
        IloIntVar delay[n - 2];
        IloIntExpr objExpr = IloIntExpr(env);

        IloIntExpr rowRestric[n]; //remove as linhas dos nodes source e sink
        IloIntExpr colRestric[n]; //remove-se as colunas dos nodes sourc e sink

        cout << " CRIANDO ARRAY DE CUSTOS, VARIAVEIS X, E INICIALIZANDO A FUNCAO OBJETIVO " << endl;

        //(n-2) = NODE SOURCE
        //(n-1) = NODE SINK
        for (int linha = 0; linha < n; linha++) { //Cada Linha

            x[linha] = IloBoolVarArray(env, n);
            cost[linha] = IloIntArray(env, n);

            for (int coluna = 0; coluna < n; coluna++) { //Cada coluna
                model.add(x[linha][coluna]);

                if(ISDEBUG){
                    char name[20];
                    sprintf(name, "x[%d][%d]", linha, coluna);
                    x[linha][coluna].setName(name);
                }


                //Se for na primeira execucao, inicializa as restricoes.
                if ((linha == 0)) {
                    //Essas restrições não envolvem o node source e o node sink.
                    rowRestric[coluna] = IloIntExpr(env);
                    colRestric[coluna] = IloIntExpr(env);

                    if (coluna < (n - 2) && EXTENDED) {

                        if(ISDEBUG){
                            char name[20];
                            sprintf(name, "delay[%d]", coluna);
                            delay[coluna].setName(name);
                        }
                        
                        delay[coluna] = IloIntVar(env, -maxDelay, maxDelay);
                        model.add(delay[coluna]); // Um delay por linha ( cada linha representa a quem o voo j está ligado )
                    }
                }

                // A coluna dos nos ficticios nao entram nas restricoes.
                if (linha < n - 2) {
                    rowRestric[linha] += x[linha][coluna];
                }

                if (coluna < n - 2) {
                    colRestric[coluna] += x[linha][coluna];
                }


                //Ninguem pode chegar nele mesmo
                //O node sink nao pode chegar em ninguem
                if ((linha == coluna) || (linha == (n - 1))) {
                    cost[linha][coluna] = MAX_COST;
                }                    /*
                     * Da origem para qualquer canto ( custo 1000 )
                     */
                else if (linha == (n - 2)) {
                    cost[linha][coluna] = 1000;
                    if (coluna == (n - 1)) cost[linha][coluna] = MAX_COST;
                }/*
                     * Para chegar no destino (custo 0)
                     */
                else if (coluna == (n - 1)) {
                    cost[linha][coluna] = 0;
                }//Ninguem pode chegar na origem.
                else if (coluna == (n - 2)) {
                    cost[linha][coluna] = MAX_COST;
                }                    //Nesse caso e preciso calcular o custo
                else {

                    cost[linha][coluna] = costOfArc(v, instance->getTimes(), linha, coluna, maxDelay);
                }


                if (cost[linha][coluna] != 0) {
                    objExpr += x[linha][coluna]*(cost[linha][coluna]);
                }

            }

        }

        if(ISDEBUG)
            printf("CRIACAO FINALIZADA\n");

        for (int i = 0; i < n - 2; i++) {

            model.add(1 <= rowRestric[i] <= 1);
            model.add(1 <= colRestric[i] <= 1);

            if (EXTENDED) {
                objExpr += IloAbs(delay[i]);
            }
            rowRestric[i].end();
            colRestric[i].end();
        }

        if(EXTENDED){
            for (int linha = 0; linha < n - 2; linha++) {
                Flight &fo = (*v)[linha];
                for (int coluna = 0; coluna < n - 2; coluna++) {
                    if (linha != coluna) {
                        Flight &fd = (*v)[coluna];
                        int endFirstFlight = fo.GetDepartureTime() + fo.GetDuration();
                        int beginNextFlight = fd.GetDepartureTime();
                        model.add((x[linha][coluna] * endFirstFlight) + delay[linha] - ((1 - x[linha][coluna]) * MAX_COST) <= (beginNextFlight + delay[coluna]));
                    }
                }
            }
        }


        model.add(IloMinimize(env, objExpr));
        IloCplex cplex(model);
        cplex.setOut(env.getNullStream());

//        cplex.setParam(IloCplex::TiLim, 5);

        if(ISDEBUG){
            char saida[30] = "modelo.lp\0";
            cplex.exportModel(saida);
        }

        // Optimize the problem and obtain solution.
        if(ISDEBUG)
            cout << "[ARPSolver.solve()] Iniciando o solver" << endl;
        if (!cplex.solve()) {
            env.error() << "Failed to optimize LP" << endl;
            throw (-1);
        }
        if(ISDEBUG)
            cout << "[ARPSolver.solve()] Saiu com sucesso do solver." << endl;
        //env.out() << "Solution status = " << cplex.getStatus() << endl;

        if(ISDEBUG)
            cout << "[ARPSolver.solve()] Iniciando montagem da malha" << endl;
        vector< vector<Flight> > result = assembleResult(v, instance, cplex, x, cost, delay, n);
        if(ISDEBUG)
            cout << "[ARPSolver.solve()] Montagem da malha finalizada" << endl;

        if(ISDEBUG)
            env.out() << "Solution value  = " << cplex.getObjValue() << endl;
        env.end();

        return result;

    } catch (IloException& e) {

        cerr << "Concert exception caught: " << e << endl;
    } catch (...) {
        cerr << "Unknown exception caught" << endl;
    }

    env.end();

    cout << "[ARPSolver.solve()] Saiu com erro" << endl;

    return vector< vector<Flight> >();
}

vector< vector<Flight> > ARPSolver::solver2(vector<Flight> *v, Instance *instance) {

    if(ISDEBUG2)
        cout << "Solver With " << v->size() << " Flights " << endl;

    int maxDelay = instance->getMaxDelay();


    for (int i = 0; i < v->size(); i++) {
        (*v)[i].SetIlogIndex(i);
    }

    if (v->empty()) {
        cout << "Lista vazia" << endl;
    }

    IloEnv env;

    try {
        IloModel model(env);


        //size + 1 e size + 2 sao os nos de origem e o de fim.
        IloInt n = v->size() + 2;
        IloBoolVarArray** x;
        IloIntArray** cost;
        IloIntExpr objExpr = IloIntExpr(env);

        IloIntExpr rowRestric[n]; //remove as linhas dos nodes source e sink
        IloIntExpr colRestric[n]; //remove-se as colunas dos nodes sourc e sink

        if(ISDEBUG2)
            cout << " CRIANDO ARRAY DE CUSTOS, VARIAVEIS X, E INICIALIZANDO A FUNCAO OBJETIVO " << endl;


        //(n-2) = NODE SOURCE
        //(n-1) = NODE SINK
        x = new IloBoolVarArray*[n];
        cost = new IloIntArray*[n];
        for (int linha = 0; linha < n; linha++) { //Cada Linha

            x[linha] = new IloBoolVarArray[n];
            cost[linha] = new IloIntArray[n];

            for (int coluna = 0; coluna < n; coluna++) { //Cada coluna

                x[linha][coluna] = IloBoolVarArray(env, 4);
                cost[linha][coluna] = IloIntArray(env, 4);

                //Se for na primeira execucao, inicializa as restricoes.
                if ((linha == 0)) {
                    rowRestric[coluna] = IloIntExpr(env);
                    colRestric[coluna] = IloIntExpr(env);
                }


                for(int arc = 0; arc < 4; arc++){
                    model.add(0<=x[linha][coluna][arc]<=1);
                    if(ISDEBUG2){
                        char name[20];
                        sprintf(name, "x[%d][%d][%d]", linha, coluna, arc);
                        x[linha][coluna][arc].setName(name);
                    }

                    // A coluna dos nos ficticios nao entram nas restricoes.
                    if (linha < n - 2) {
                        rowRestric[linha] += x[linha][coluna][arc];
                    }

                    if (coluna < n - 2) {
                        colRestric[coluna] += x[linha][coluna][arc];
                    }

                    //Ninguem pode chegar nele mesmo
                    //O node sink nao pode chegar em ninguem
                    if ((linha == coluna) || (linha == (n - 1))) {
                        cost[linha][coluna][arc] = MAX_COST;
                    }        
                    
                    //Da origem para qualquer canto ( custo 1000 )
                    else if (linha == (n - 2)) {
                        cost[linha][coluna][arc] = 1000;

                        if (coluna == (n - 1)){
                            cost[linha][coluna][arc] = MAX_COST;
                        }
                    }
                    // Para chegar no destino (custo 0)
                    else if (coluna == (n - 1)) {
                        cost[linha][coluna][arc] = 0;
                    }//Ninguem pode chegar na origem.
                    else if (coluna == (n - 2)) {
                        cost[linha][coluna][arc] = MAX_COST;
                    }//Nesse caso e preciso calcular o custo
                    else {

                        int tempcost = 0;
                        switch (arc) {
                            case 0: tempcost = costOfArc1(v, linha, coluna);
                                break;
                            case 1: tempcost = costOfArc2(v, linha, coluna, maxDelay);
                                break;
                            case 2: tempcost = costOfArc3(v, instance->getTimes(), linha, coluna);
                                break;
                            case 3: tempcost = costOfArc4(v, instance->getTimes(), linha, coluna, maxDelay);
                                break;

                        }
                        cost[linha][coluna][arc] = tempcost;
                    }

                    //if (cost[linha][coluna][arc] != 0 /*|| cost[linha][coluna] >= MAX_COST*/) {
                    objExpr += x[linha][coluna][arc]*(cost[linha][coluna][arc]);
                    // }
                }
            }

        }


        if(ISDEBUG2)
            printf("CRIACAO FINALIZADA\n");

        for (int i = 0; i < n - 2; i++) {

            model.add(1 <= rowRestric[i] <= 1);
            model.add(1 <= colRestric[i] <= 1);

            rowRestric[i].end();
            colRestric[i].end();
        }

        model.add(IloMinimize(env, objExpr));
        IloCplex cplex(model);
        cplex.setOut(env.getNullStream());

        //        cplex.setParam(IloCplex::TiLim, 5);

        if(ISDEBUG2){
            char saida[30] = "modelo.lp\0";
            cplex.exportModel(saida);
        }


        // Optimize the problem and obtain solution.
        if(ISDEBUG2)
            cout << "[ARPSolver.solve()] Iniciando o solver" << endl;
        if (!cplex.solve()) {
            env.error() << "Failed to optimize LP" << endl;
            throw (-1);
        }

        if(ISDEBUG2){
            cout << "[ARPSolver.solve()] Saiu com sucesso do solver." << endl;
            env.out() << "Solution status = " << cplex.getStatus() << endl;
        }


        if(ISDEBUG2)
            cout << "[ARPSolver.solve()] Iniciando montagem da malha" << endl;

        vector< vector<Flight> > result = assembleResult2(v, instance, cplex, x, cost, n);

        if(ISDEBUG2){
            cout << "[ARPSolver.solve()] Montagem da malha finalizada" << endl;
            env.out() << "Solution value  = " << cplex.getObjValue() << endl;
        }
        
        env.end();

        return result;

    } catch (IloException& e) {

        cerr << "Concert exception caught: " << e << endl;
    } catch (...) {
        cerr << "Unknown exception caught" << endl;
    }

    env.end();
    cout << "[ARPSolver.solve()] Saiu com erro" << endl;

    return vector< vector<Flight> >();
}

void ARPSolver::readInput(char *file) {
    ifstream ifs(file, ifstream::in);

    Instance instance = Instance::read(ifs);

    ifs.close();


    vector<Flight> flights;

    for (int i = 0; i < (*instance.getFlights()).size(); i++) {
        Flight *f = (*instance.getFlights())[i];
        flights.push_back(*f);
    }


    vector< vector <Flight> > result = solver2(&flights, &instance);

    ofstream ofs("saida.txt", ofstream::out);
    ARPUtil::writeSolution(&result, ofs);
    ofs.flush();
    ARPUtil::showSolution(result);
}

void ARPSolver::test() {
    vector<Flight> f;

    //Trilho 1
    f.push_back(Flight(0, 0, 50, 'a', 'b'));
    f.push_back(Flight(1, 90, 50, 'b', 'd'));

    //Trilho 2
    f.push_back(Flight(2, 45, 65, 'b', 'c'));

    //Trilho 3
    f.push_back(Flight(3, 0, 80, 'c', 'b'));

    //vector< vector<Flight> > r = solver(&f, 10);
    //ARPUtil::showSolution(r);
}

void ARPSolver::test2() {
    vector<Flight> v;

    v.push_back(Flight(0, 10, 30, 0, 1));
    v.push_back(Flight(1, 50, 40, 1, 3));


    //vector< vector <Flight> > result = ARPSolver::solver(&v, 0);

    //ARPUtil::showSolution(result);
}


