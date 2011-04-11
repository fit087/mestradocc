all:
	g++ -c -m64 -O -fPIC -fexceptions -DNDEBUG -DIL_STD -I/usr/ilog/cplex112/include -I/usr/ilog/concert27/include  Main.cpp Flight.cpp ARPSolver.cpp
	
	g++ -m64 -O -fPIC -fexceptions -DNDEBUG -DIL_STD -I/usr/ilog/cplex112/include -I/usr/ilog/concert27/include  Main.o Flight.o ARPSolver.o -o main -L/usr/ilog/cplex112/lib/x86-64_debian4.0_4.1/static_pic -lilocplex -lcplex -L/usr/ilog/concert27/lib/x86-64_debian4.0_4.1/static_pic -lconcert -lpthread

clean:
	rm -r *.o
	rm -r main
