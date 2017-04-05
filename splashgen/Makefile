CXX = clang
CXXFLAG = -std=c++14 -Wall -Wextra -Wpedantic -Wsign-conversion \
	  -Wold-style-cast -Wsign-promo -Wsign-promo -Wswitch-enum \
	  -Weffc++ -Wshadow -Wno-missing-braces -g \
		-Wno-unused-parameter \
		-I /usr/local/include \
	  -I /home/jim/projects/boostache/include
LDFLAG = -lprotobuf -lstdc++ -lboost_regex -lboost_filesystem \
	  -lboost_system -lboost_program_options -lpthread -lcurl -lpugixml

LIBS = ed25519/ed25519.a \
			 webdav-client-cpp/build/libwdc.a

build: splashgen.exe

splashgen.exe: main.o message.pb.o $(LIBS)
	${CXX} ${LDFLAG} -o $@ $^

main.o: main.cc message.pb.h utils.h manifest.h
	${CXX} ${CXXFLAG} -c -o main.o main.cc

%.o: %.cc
	${CXX} ${CXXFLAG} -c -o $@ $?

clean:
	rm -f *.o *.exe
