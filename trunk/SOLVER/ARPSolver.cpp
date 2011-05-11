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

#define EXTENDED true
#define ISDEBUG false

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

//    for(int i = 0; i < v->size(); i++){
//        printf("@@@ %d - %d", i, (*v)[i].GetDepartureTime());
//    }
    //exit(1);
    //Se as cidades forem iguais.

    //printf(">>>>>>>> %d(%d) - %d(%d) (%c - %c)\n", orig, o.GetDepartureTime(), dest, d.GetDepartureTime(), o.GetArrivalCity(), d.GetDepartureCity());
    //printf("Teste 2 %d %d\n", o.GetDepartureTime(), d.GetDepartureTime());

    if (o.GetArrivalCity() == d.GetDepartureCity()) {
        int diff = (d.GetDepartureTime()) - (o.GetDepartureTime() + o.GetDuration());

        //printf(" TIME =  %d \n", time);
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

        if(destIndex < origIndex){
            int temp = origIndex;
            origIndex = destIndex;
            destIndex = temp;
        }

        int flightTime = (*distanceTimes)[origIndex][destIndex];
        if(diff >= flightTime){
            return flightTime;
        }
        else{
            diff += maxDelay;

            if(diff >= flightTime){
                if(EXTENDED) return flightTime;
                else return flightTime + (maxDelay - diff);
            }
        }
        //FAZ O REPOSICIONAMENTO E OBTEM O CUSTO DELES.
        return MAX_COST; // Essa versao nao permite reposicionamento.
    }

    return 0;
}



void ARPSolver::showVariables(IloCplex &model, IloIntVarArray vars[], IloIntVar delay[], int n){
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
    for(int i = 0 ; i < n; i++){
        IloInt v = model.getValue(delay[i]);
        if(v != 0)
        printf("(%d) %-4d  ", i,(int)v);
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

void ARPSolver::finalizeTrail(vector<Flight> *flight, vector<Flight> *trail, IloCplex &cplex, IloBoolVarArray vars[], IloIntArray cost[], IloIntVar delay[], int n){
    while(true){
        Flight &f = (*trail)[(*trail).size() - 1];
        int index = f.GetIlogIndex();

        int qtde = 0;
        for(int i = 0; i < n; i++){

            if (ISDEBUG)
                cout << " N " << i << " " << index << " " << n << " " << endl;

            IloInt v = cplex.getValue(vars[index][i]);

            if (v == 1) {
                qtde++;

                if(i == (n-1)) return;

                int fcost;
                if(EXTENDED){
                    fcost = cplex.getValue(delay[i]);
                }
                else{
                    fcost = cost[index][i];
                }
                
                (*flight)[i].SetDelay(fcost);
                trail->push_back((*flight)[i]);
                continue;
            }
        }

        //if(qtde == 0) return;
    }
    
    return;
}

vector< vector<Flight> > ARPSolver::assembleResult(vector<Flight> *flight, Instance *instance, IloCplex &cplex, IloBoolVarArray vars[], IloIntArray cost[], IloIntVar delay[], int n) {

    int sourceIndex = (n - 2);

    vector< vector<Flight> > result;

    for (int i = 0; i < n - 2; i++) {

        if (ISDEBUG)
            printf("Construção do trilho %d\n", i);
        IloInt v = cplex.getValue(vars[sourceIndex][i]);
        if (v == 1) {
            vector<Flight> track;
            track.push_back((*flight)[i]);
            finalizeTrail(flight, &track, cplex, vars, cost, delay, n);
            int initialCost = ARPUtil::calculeCost(track, instance);
            int finalCost = ARPUtil::configureTrack(track, instance);

            if(initialCost != finalCost){
                printf("Erro na geração do trilho (%d) {Custo Inicial = %d | Custo Final = %d}\n", (int)result.size(), initialCost, finalCost);
                exit(1);
            }
            
            result.push_back(track);
            if (ISDEBUG)
                printf("Quantidade = %d\n", (int)track.size());
        }
    }



    return result;
}

vector< vector<Flight> > ARPSolver::solver(vector<Flight> *v, Instance *instance, int maxDelay){

    cout << "Solver With " << v->size() << " Flights " << endl;
    
    for(int i = 0; i < v->size(); i++){
        (*v)[i].SetIlogIndex(i);
    }

    //ARPSolver::adjustTime();

    if (v->empty()) {
        cout << "Lista vazia" << endl;
        //   return NULL;
    }

    //cout << " INICIANDO SOLVER " << endl;

    IloEnv env;

    try {
        IloModel model(env);


        //size + 1 e size + 2 sao os nos de origem e o de fim.
        IloInt n = v->size() + 2;
        IloBoolVarArray x[n];
        IloIntArray cost[n];
        IloIntVar delay[n];
        IloExpr objExpr = IloExpr(env);
       // IloRangeArray restrictions = IloRangeArray(env, (2 *(n - 2)));
        

        IloExpr rowRestric[n - 2];
        IloExpr colRestric[n - 2];

        //IloExprArray connectionFeasibility[n];
       // IloExpr flightRestriction[n - 2];


        cout << " CRIANDO ARRAY DE CUSTOS, VARIAVEIS X, E INICIALIZANDO A FUNCAO OBJETIVO " << endl;
        
        for (int i = 0; i < n; i++) { //Cada Linha

            
            
            x[i] = IloBoolVarArray(env, n);
            cost[i] = IloIntArray(env, n);
          //  connectionFeasibility[i] = IloExprArray(env, n);


            for (int j = 0; j < n; j++) { //Cada coluna
                model.add(x[i][j]);


                //Se for na primeira execucao, inicializa as restricoes.
                if (i == 0) {
                    rowRestric[j] = IloExpr(env);
                    colRestric[j] = IloExpr(env);
                    delay[j] = IloIntVar(env);
                    model.add(delay[j]);
                  //  flightRestriction[i] = IloExpr(env);
                    //flightRestriction[i] =
                   //         (x[i][j])*
                   //         (((*v)[i].GetDepartureTime() +  ((*v)[i].GetDuration()) + delay[i]) - (((*v)[j].GetDepartureTime() + delay[j])))
                   //         >= 0 ;
                }

                // A linha e a coluna dos nos ficticios nao entram nas restricoes.
                if ((i < (n - 2)) || (j < (n - 2))) {
                    
                    rowRestric[j] += x[i][j];
                    colRestric[i] += x[i][j];
                    Flight &fo = (*v)[i];
                    Flight &fd = (*v)[j];

                    if ((i < (n - 2)) && (j < (n - 2))) {
                        if(EXTENDED ){
                          model.add(((x[i][j])*fo.GetDepartureTime() + (x[i][j])*fo.GetDuration() - (1 - x[i][j])*MAX_COST  + delay[i])   <= (fd.GetDepartureTime() + delay[j]));
                        }
                          //((fo.GetDepartureTime() + fo.GetDuration() + delay[i]) - (fd.GetDepartureTime() + delay[j])) <= 0);
                    }
                            
                }


                if((i == j) || (i == (n-1))){
                    cost[i][j] = MAX_COST;
                }

                /*
                 * Da origem para qualquer canto ( custo 1000 )
                 */
                else if (i == (n - 2)) {
                    cost[i][j] = 1000;
                    if(j == (n-1)) cost[i][j] = MAX_COST;
                }
                /*
                     * Se alguem chegar no destino (custo 0)
                     */
                else if (j == (n - 1)) {
                    cost[i][j] = 0;
                }
                /*
                     * Ninguem pode chegar nele mesmo
                     * Ninguem pode chegar no destino
                     * Ninguem pode sair da origem
                     *
                     */
                else if (j == (n - 2)) {
                    cost[i][j] = MAX_COST;
                }/*
             * Nesse caso e preciso calcular o custo
             */
                else {

                    cost[i][j] = costOfArc(v, instance->getTimes() ,i, j, maxDelay);
                }

                //printf("ANTES(%d,%d) = %d\n", i, j, (int) cost[i][j]);

                objExpr += x[i][j]*(cost[i][j]);
            }

            if(EXTENDED){
                objExpr += delay[i];
            }

            //#########################################
            //Cria a funcao de minimizacao.
            //printf("ANTES\n");

            //objExpr += IloScalProd(x[i], cost[i]);
            //printf("DEPOIS\n");
            //#########################################
        }

        printf("CRIACAO FINALIZADA\n");

        for (int i = 0; i < n - 2; i++) {
            model.add(1 <= rowRestric[i] <= 1);
            model.add(1 <= colRestric[i] <= 1);

//            rowRestric[i].end();
//            colRestric[i].end();
        }

        //    printf("Adicao de restricoes finalizada");



        model.add(IloMinimize(env, objExpr));

        IloCplex cplex(model);
        cplex.setOut(env.getNullStream());
        // cplex.extract(model);

        // Optimize the problem and obtain solution.
        if (!cplex.solve()) {
            env.error() << "Failed to optimize LP" << endl;
            throw (-1);
        }

        //env.out() << "Solution status = " << cplex.getStatus() << endl;
        //env.out() << "Solution value  = " << cplex.getObjValue() << endl;

        showVariables(cplex, x, delay, n);
        //showCosts(cost, n);

        cout << "Teste solver(list<Flight>) Finalizado" << endl;

        return assembleResult(v, instance, cplex, x, cost, delay, n);

    } catch (IloException& e) {
        cerr << "Concert exception caught: " << e << endl;
    } catch (...) {
        cerr << "Unknown exception caught" << endl;
    }
    env.end();

    cout << "Teste solver(list<Flight>) Finalizado" << endl;

    return vector< vector<Flight> >();
}

void ARPSolver::readInput(char *file) {
    ifstream ifs(file, ifstream::in);

    Instance instance = Instance::read(ifs);

    ifs.close();


    vector<Flight> flights;

    for(int i = 0; i < (*instance.getFlights()).size(); i++){
        Flight *f = (*instance.getFlights())[i];
        flights.push_back(*f);
    }


    vector< vector <Flight> > result = solver(&flights, &instance, instance.getMaxDelay());

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
