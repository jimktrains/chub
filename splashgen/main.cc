#include <cstdlib>
#include "message.pb.h"
#include <fstream> 
#include <iostream>
#include <string>

#include <boost/uuid/uuid.hpp>
#include <boost/uuid/uuid_io.hpp>

struct InvalidUUIDBytes {};

std::unique_ptr<boost::uuids::uuid> bytes2uuid(std::string bytes) {
    if (bytes.length() != 16) throw new InvalidUUIDBytes;
    
    std::unique_ptr<boost::uuids::uuid> u = std::make_unique<boost::uuids::uuid>();
    
    memcpy(u.get(), bytes.c_str(), 16);
    
    return u;
}

int main(int argc, char** argv) {
    std::ifstream ifs;
    ifs.open ("/Users/jameskeener/chub/d8dc1175-b1cf-42ff-8e25-1224ad7f50d9.post.pb", std::ifstream::in);

    
    SignedMessage sm = SignedMessage();
    sm.ParseFromIstream(&ifs);
    
    std::cout << boost::uuids::to_string(*bytes2uuid(sm.id())) << "\t" << sm.post().title() << std::endl;
    return 0;
}