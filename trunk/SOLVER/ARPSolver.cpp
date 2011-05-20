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

#define MAX_COST 9999

#define EXTENDED false
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
        diff -= flightTime;
        
        if(diff >= 0){
            return flightTime;
        }
        else{

            //return MAX_COST; // Essa versao nao permite reposicionamento.
            
            diff += maxDelay;

            if(diff >= 0){
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
    for(int i = 0 ; i < n - 2; i++){
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

                int fcost = 0;
                if(EXTENDED && (i < n - 2)){
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

        if(qtde == 0){
            //cout << "Saiu aqui... " << endl;
           exit(1);
        }
    }
    
    return;
}

vector< vector<Flight> > ARPSolver::assembleResult(vector<Flight> *flight, Instance *instance, IloCplex &cplex, IloBoolVarArray x[], IloIntArray cost[], IloIntVar delay[], int n) {

    int sourceIndex = (n - 2);

    vector< vector<Flight> > result;

    for (int i = 0; i < n - 2; i++) {

        //if (ISDEBUG)
        printf("Construção do trilho %d\n", i);
        IloInt v = cplex.getValue(x[sourceIndex][i]);
        cout << " Passo 2" << endl;
        if (v == 1) {
            vector<Flight> track;
            track.push_back((*flight)[i]);
            cout << " Finalizando trilho" << endl;
            finalizeTrail(flight, &track, cplex, x, cost, delay, n);
            int initialCost = ARPUtil::calculeCost(track, instance);
            int finalCost = ARPUtil::configureTrack(track, instance);

            if(initialCost != finalCost){
                printf("Erro na geração do trilho (%d) {Custo Inicial = %d | Custo Final = %d}\n", (int)result.size(), initialCost, finalCost);
              //  exit(1);
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
        IloIntVar delay[n - 2];
        IloIntExpr objExpr = IloIntExpr(env);
       // IloRangeArray restrictions = IloRangeArray(env, (2 *(n - 2)));
        

        IloIntExpr rowRestric[n]; //remove as linhas dos nodes source e sink
        IloIntExpr colRestric[n]; //remove-se as colunas dos nodes sourc e sink

        //IloExprArray connectionFeasibility[n];
       // IloExpr flightRestriction[n - 2];


        cout << " CRIANDO ARRAY DE CUSTOS, VARIAVEIS X, E INICIALIZANDO A FUNCAO OBJETIVO " << endl;
        //(n-2) = NODE SOURCE
        //(n-1) = NODE SINK
        for (int linha = 0; linha < n; linha++) { //Cada Linha

            
            
            x[linha] = IloBoolVarArray(env, n);
            cost[linha] = IloIntArray(env, n);
          //  connectionFeasibility[i] = IloExprArray(env, n);


            for (int coluna = 0; coluna < n; coluna++) { //Cada coluna
                model.add(x[linha][coluna]);
                char name[20];
                sprintf(name, "x[%d][%d]", linha, coluna);

                x[linha][coluna].setName(name);


                //Se for na primeira execucao, inicializa as restricoes.
                if ((linha == 0) ) {
                    //Essas restrições não envolvem o node source e o node sink.
                    rowRestric[coluna] = IloIntExpr(env);
                    colRestric[coluna] = IloIntExpr(env);

                    if(coluna < (n-2) && EXTENDED){
                        delay[coluna] = IloIntVar(env);
                        model.add(delay[coluna]); // Um delay por linha ( cada linha representa a quem o voo j está ligado )
                    }
                }

                //cout << "Teste 1 " << linha << " " << coluna << endl;
                // A coluna dos nos ficticios nao entram nas restricoes.
                //if (linha < n - 2) {
                    rowRestric[linha] += x[linha][coluna];
                //}

//                if (coluna < n - 2) {
                    colRestric[coluna] += x[linha][coluna];
//                }

                if (linha < n - 2 && coluna < n - 2) {
                    Flight &fo = (*v)[linha];
                    Flight &fd = (*v)[coluna];

                    if (EXTENDED) {
                        model.add(((x[linha][coluna]) * fo.GetDepartureTime() + (x[linha][coluna]) * fo.GetDuration() - (1 - x[linha][coluna]) * MAX_COST + delay[linha]) <= (fd.GetDepartureTime() + delay[coluna]));
                    }
                }




                if((linha == coluna) || (linha == (n-1))){
                    cost[linha][coluna] = MAX_COST;
                }

                /*
                 * Da origem para qualquer canto ( custo 1000 )
                 */
                else if (linha == (n - 2)) {
                    cost[linha][coluna] = 1000;
                    if(coluna == (n-1)) cost[linha][coluna] = MAX_COST;
                }
                /*
                     * Se alguem chegar no destino (custo 0)
                     */
                else if (coluna == (n - 1)) {
                    cost[linha][coluna] = 0;
                }
                /*
                     * Ninguem pode chegar nele mesmo
                     * Ninguem pode chegar no destino
                     * Ninguem pode sair da origem
                     *
                     */
                else if (coluna == (n - 2)) {
                    cost[linha][coluna] = MAX_COST;
                }/*
             * Nesse caso e preciso calcular o custo
             */
                else {

                    cost[linha][coluna] = costOfArc(v, instance->getTimes(), linha, coluna, maxDelay);
                }

                //printf("ANTES(%d,%d) = %d\n", i, j, (int) cost[i][j]);

                //if (cost[linha][coluna] != 0 /*|| cost[linha][coluna] >= MAX_COST*/) {
                    objExpr += x[linha][coluna]*(cost[linha][coluna]);
                //}

                if (cost[linha][coluna] >= MAX_COST) {
                   // model.add(1 <= x[linha][coluna] <= 1);
                }

            }


            if (linha < n - 2) {
                if (EXTENDED) {
                    objExpr += delay[linha];
                }
            }

            //#########################################
            //Cria a funcao de minimizacao.
            //printf("ANTES\n");

            //objExpr += IloScalProd(x[i], cost[i]);
            //printf("DEPOIS\n");
            //#########################################
        }

        printf("CRIACAO FINALIZADA\n");

        //model.add(x[n-1] = x[n-2]);

        for (int i = 0; i < n - 2; i++) {
//            char rowname[20], colname[20];
//
//            sprintf(rowname, "row[%d]", i);
//            sprintf(colname, "col[%d]", i);

            
            model.add(1 <= rowRestric[i] <= 1);
            model.add(1 <= colRestric[i] <= 1);

            //            rowRestric[i].end();
            //            colRestric[i].end();
        }

        //    printf("Adicao de restricoes finalizada");


        model.add(IloMinimize(env, objExpr));
        IloCplex cplex(model);
        cplex.setOut(env.getNullStream());
        //cplex.setParam(IloCplex::TiLim, 5);
        char saida[30] = "modelo.lp\0";
        cplex.exportModel(saida);

        // Optimize the problem and obtain solution.
        cout << "[ARPSolver.solve()] Iniciando o solver" << endl;
        if (!cplex.solve()) {
            env.error() << "Failed to optimize LP" << endl;
            throw (-1);
        }
        cout << "[ARPSolver.solve()] Saiu com sucesso do solver."  << endl;
        //env.out() << "Solution status = " << cplex.getStatus() << endl;

        //  showVariables(cplex, x, delay, n);
        //cout << " SAIDA 6 " << endl;
        //showCosts(cost, n);


        cout << "[ARPSolver.solve()] Iniciando montagem da malha" << endl;
        vector< vector<Flight> > result = assembleResult(v, instance, cplex, x, cost, delay, n);
        cout << "[ARPSolver.solve()] Montagem da malha finalizada" << endl;

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

void ARPSolver::readInput(char *file) {
    ifstream ifs(file, ifstream::in);

    Instance instance = Instance::read(ifs);

    ifs.close();


    vector<Flight> flights;

    for (int i = 0; i < (*instance.getFlights()).size(); i++) {
        Flight *f = (*instance.getFlights())[i];
        flights.push_back(*f);
    }


    vector< vector <Flight> > result = solver(&flights, &instance, instance.getMaxDelay());

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


