CXX = clang
CXXFLAG = -std=c++14 -Wall -Wextra -Wpedantic -Wsign-conversion \
	  -Wold-style-cast -Wsign-promo -Wsign-promo -Wswitch-enum \
	  -Weffc++ -Wshadow -Wno-missing-braces -g \
	  -I /usr/local/include -lstdc++ \
	  -I /usr/local/Cellar/cryptopp/5.6.5/include \
	  -L /usr/local/Cellar/cryptopp/5.6.5/lib -lcryptopp \
	  -L /usr/local/lib \
	  -L /usr/local/Cellar/boost/1.62.0/lib \
	  -lprotobuf \
#	  -I ../../libeddsa/lib \
	  -L ../../libeddsa/build/lib -leddsa-static

CC = clang
CFLAG = -std=c11 -Wall -Wextra -Wpedantic -Wsign-conversion \
	  -Wold-style-cast -Wsign-promo -Wsign-promo -Wswitch-enum \
	  -Weffc++ -Wshadow -Wno-missing-braces -g

%.o: %.c
	${CC} ${CFLAGS} -c -o $@ $?

%.o: %.cc
	${CXX} ${CXXFLAG} -c -o $@ $?

# Yeah, I should commit/offer make file to their project
ed25519.a: ed25519/src/add_scalar.o ed25519/src/fe.o ed25519/src/ge.o ed25519/src/key_exchange.o ed25519/src/keypair.o ed25519/src/sc.o ed25519/src/seed.o ed25519/src/sha512.o ed25519/src/sign.o ed25519/src/verify.o
	ar cr $@ $?
	
build: main.o message.pb.o ed25519.a #../../libeddsa/build/lib/libeddsa-static.a
	${CXX} ${CXXFLAG} -o splashgen $?

clean:
	rm -f *.o splashgen *.s ed25519/src/*.o
