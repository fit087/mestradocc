CPLEXDIR      = /Users/alexanderdealmeidapinto/ILOG/CPLEX_Studio_AcademicResearch122/cplex/
CONCERTDIR    = /Users/alexanderdealmeidapinto/ILOG/CPLEX_Studio_AcademicResearch122/concert/

all:
	g++ -c -m64 -O -fPIC -fexceptions -DNDEBUG -DIL_STD -I$(CPLEXDIR)include -I$(CONCERTDIR)include  Main.cpp Flight.cpp ARPSolver.cpp GRASP.cpp Instance.cpp util/MathUtil.cpp Parameters.cpp
	
	g++ -m64 -O -fPIC -fexceptions -DNDEBUG -DIL_STD -I$(CPLEXDIR)include -I$(CONCERTDIR)include  Main.o Flight.o ARPSolver.o GRASP.o Parameters.o Instance.o MathUtil.o -o main -L$(CPLEXDIR)lib/x86-64_darwin9_gcc4.0/static_pic -lilocplex -lcplex -L$(CONCERTDIR)lib/x86-64_darwin9_gcc4.0/static_pic -lconcert -m64 -lm -lpthread -framework CoreFoundation -framework IOKit


clean:
	rm -r *.o
	rm -r main
