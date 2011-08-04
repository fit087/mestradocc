CPLEXDIR      = /Users/alexander/Applications/IBM/ILOG/CPLEX_Studio_Academic123/cplex/
CONCERTDIR    = /Users/alexander/Applications/IBM/ILOG/CPLEX_Studio_Academic123/concert/

all:
	g++ -wall -c -m64 -O -fPIC -fexceptions -DNDEBUG -DIL_STD -I$(CPLEXDIR)include -I$(CONCERTDIR)include  Main.cpp Flight.cpp ARPSolver.cpp GRASP.cpp Instance.cpp util/MathUtil.cpp util/ARPUtil.cpp Parameters.cpp
	
	g++ -m64 -O -fPIC -fexceptions -DNDEBUG -DIL_STD -I$(CPLEXDIR)include -I$(CONCERTDIR)include  Main.o Flight.o ARPSolver.o GRASP.o Parameters.o Instance.o MathUtil.o ARPUtil.o -o main -L$(CPLEXDIR)lib/x86-64_darwin9_gcc4.0/static_pic -lilocplex -lcplex -L$(CONCERTDIR)lib/x86-64_darwin9_gcc4.0/static_pic -lconcert -m64 -lm -lpthread -framework CoreFoundation -framework IOKit


clean:
	rm -r *.o
	rm -r main

