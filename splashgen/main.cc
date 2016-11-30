
#include <fstream> 
#include <iostream>

#include "ed25519/src/ed25519.h"

#include "message.pb.h"

#include "utils.h"
#include "exceptions.h"

using namespace org::metamesh::chub;

int main(int argc, char** argv) {

    std::ifstream pubkeyfile;
    pubkeyfile.open("/Users/jameskeener/chub/jim@jimkeener.com-266hHqDrb.k0P3wLWqCJ02rZTHqBEGEct4fCxxH5xSyFV0TPq9Jb5T2gu6H8oH2.eFqYEg16wd14YH9fvYH3VQ--.pub.pb", std::ifstream::in);
    PublicKey pk;
    pk.ParseFromIstream(&pubkeyfile);

    std::ifstream post_file;
    post_file.open("/Users/jameskeener/chub/3d256bfd-5737-47f8-874b-6dc5f687b573.post.pb", std::ifstream::in);
    SignedMessage sm;
    sm.ParseFromIstream(&post_file);

    std::cout << *bytes2uuid2string(sm.id()) << "\t" << sm.post().title() << std::endl;
    std::string serpost = sm.post().SerializeAsString();

    std::string sig = sm.message_signature().signature();
    std::cout << "Sig length: " << sig.length() << std::endl;
    std::cout << "Key length: " << (pk.key().length() - ASN1_PREFIX_LENGTH) << std::endl;

    std::cout << ed25519_verify(
            reinterpret_cast<const unsigned char*> (sig.c_str()),
            reinterpret_cast<const unsigned char*> (serpost.c_str()),
            serpost.length(),
            PublicKeyToBytes(pk)
            ) << std::endl;


    return 0;
}