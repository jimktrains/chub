CXX = clang
CXXFLAG = -std=c++14 -Wall -Wextra -Wpedantic -Wsign-conversion \
				  -Wold-style-cast -Wsign-promo -Wsign-promo -Wswitch-enum \
					-Weffc++ -Wshadow -Wno-missing-braces -O3 \
					-I /usr/local/include -lstdc++ -L /usr/local/lib -lprotobuf

%.o: %.cc
	${CXX} ${CXXFLAG} -c $?

build: main.o message.pb.o
	${CXX} ${CXXFLAG} -o splashgen $?

clean:
	rm -f *.o splashgen *.s
