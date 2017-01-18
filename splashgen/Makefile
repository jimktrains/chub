CXX = clang
CXXFLAG = -std=c++1z -Wall -Wextra -Wpedantic -Wsign-conversion \
	  -Wold-style-cast -Wsign-promo -Wsign-promo -Wswitch-enum \
	  -Weffc++ -Wshadow -Wno-missing-braces -g \
	  -I /usr/local/include \
	  -L /usr/local/lib \
	  -I /usr/local/Cellar/plustache/0.3.0/include \
	  -I /usr/local/Cellar/plustache/0.3.0/ \
	  -L /usr/local/Cellar/plustache/0.3.0/lib \
	  -lprotobuf -lstdc++ -lplustache -lboost_regex -lboost_filesystem \
	  -lboost_system -lboost_program_options

CC = clang
CFLAG = -std=c11 -Wall -Wextra -Wpedantic -Wsign-conversion \
	  -Wold-style-cast -Wsign-promo -Wsign-promo -Wswitch-enum \
	  -Wshadow -Wno-missing-braces -g

build: splashgen.exe

splashgen.exe: main.o message.pb.o ed25519.a
	${CXX} ${CXXFLAG} -o splashgen.exe $?

%.o: %.c
	${CC} ${CFLAGS} -c -o $@ $?

%.o: %.cc
	${CXX} ${CXXFLAG} -c -o $@ $?

# Yeah, I should commit/offer make file to their project
ed25519.a: ed25519/src/add_scalar.o ed25519/src/fe.o ed25519/src/ge.o ed25519/src/key_exchange.o ed25519/src/keypair.o ed25519/src/sc.o ed25519/src/seed.o ed25519/src/sha512.o ed25519/src/sign.o ed25519/src/verify.o
	ar cr $@ $?

clean:
	rm -f *.o splashgen.exe *.s ed25519/src/*.o *.a