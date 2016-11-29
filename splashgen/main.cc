#include <cstdlib>
#include "message.pb.h"
#include <fstream> 
#include <iostream>
#include <string>

#include <boost/uuid/uuid.hpp>
#include <boost/uuid/uuid_io.hpp>

#include "cryptopp/eccrypto.h"
using CryptoPP::ECDSA;
using CryptoPP::ECP;
using CryptoPP::DL_GroupParameters_EC;

#include "cryptopp/sha.h"
using CryptoPP::SHA512;

#include "cryptopp/filters.h"
using CryptoPP::StringSource;
using CryptoPP::StringSink;

struct InvalidUUIDBytes {
};

std::unique_ptr<boost::uuids::uuid> bytes2uuid(std::string bytes) {
    if (bytes.length() != 16) throw new InvalidUUIDBytes;

    std::unique_ptr<boost::uuids::uuid> u = std::make_unique<boost::uuids::uuid>();

    memcpy(u.get(), bytes.c_str(), 16);

    return u;
}

std::unique_ptr<std::string> bytes2uuid2string(std::string bytes) {
    std::unique_ptr<std::string> s = std::make_unique<std::string>(boost::uuids::to_string(*bytes2uuid(bytes)));
    return s;
}

int main(int argc, char** argv) {

    std::ifstream pubkeyfile;
    pubkeyfile.open("/Users/jameskeener/chub/jim@jimkeener.com-T4ETTalcZfQ9UW7wRGzq3xnz3WtufcF7iG1yXf3ihTQV14YAgozYQTHnIvh22LzkLJ5GR3smx2ndFr2aDK591Q--.pub.pb", std::ifstream::in);
    PublicKey pk;
    pk.ParseFromIstream(&pubkeyfile);


    ECDSA<ECP, SHA512>::PublicKey key;
//    std::string k;
    StringSource src(pk.key(), true);
    key.Load(src);

    ECDSA<ECP, SHA512>::Verifier verifier(key);

    std::ifstream post_file;
    post_file.open("/Users/jameskeener/chub/d8dc1175-b1cf-42ff-8e25-1224ad7f50d9.post.pb", std::ifstream::in);

    SignedMessage sm;
    sm.ParseFromIstream(&post_file);

    std::cout << *bytes2uuid2string(sm.id()) << "\t" << sm.post().title() << std::endl;
    std::string serpost = sm.post().SerializeAsString();
    
    std::string sig = sm.message_signature().signature();
    std::cout << verifier.VerifyMessage(reinterpret_cast<const byte *>(serpost.c_str()), serpost.length(), reinterpret_cast<const byte *>(sig.c_str()), sig.length()) << std::endl;
    return 0;
}