CXX = clang
CXXFLAG = -std=c++14 -Wall -Wextra -Wpedantic -Wsign-conversion \
				  -Wold-style-cast -Wsign-promo -Wsign-promo -Wswitch-enum \
					-Weffc++ -Wshadow -Wno-missing-braces -O3 \
					-I /usr/local/include -lstdc++

%.o: %.cc
	${CXX} ${CXXFLAG} -c $?

test: main.o message.pb.o
	${CXX} ${CXXFLAG} -o test $?
	./test

clean:
	rm -f *.o test *.s
