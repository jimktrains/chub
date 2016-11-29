CXX = clang
CXXFLAG = -std=c++14 -Wall -Wextra -Wpedantic -Wsign-conversion \
	  -Wold-style-cast -Wsign-promo -Wsign-promo -Wswitch-enum \
	  -Weffc++ -Wshadow -Wno-missing-braces -g \
	  -I /usr/local/include -lstdc++ -L /usr/local/lib \
	  -L /usr/local/Cellar/boost/1.62.0/lib \
	  -lprotobuf

%.o: %.cc
	${CXX} ${CXXFLAG} -c $?

build: main.o message.pb.o
	${CXX} ${CXXFLAG} -o splashgen $?

clean:
	rm -f *.o splashgen *.s
