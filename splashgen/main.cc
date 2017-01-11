
#include <fstream> 
#include <iostream>
#include <dirent.h>

//#include <plustache_types.hpp>
//#include <template.hpp>
//using PlustacheTypes::ObjectType;
//using Plustache::template_t;

#include "ed25519/src/ed25519.h"

#include "message.pb.h"

#include "utils.h"
#include "exceptions.h"


using namespace org::metamesh::chub;

//void recurse(std::string start_path, auto callback) {
//    DIR *dir;
//    struct dirent *ent;
//    if ((dir = opendir(start_path)) != NULL) {
//        /* print all the files and directories within directory */
//        while ((ent = readdir(dir)) != NULL) {
//            
//            callback(ent->d_name);
//        }
//        closedir(dir);
//    } else {
//        /* could not open directory */
//        perror("");
//        return EXIT_FAILURE;
//    }
//}

/*
 * Grab messages from server
 *  /chub/YYYY/MM/DD/<uuid>.<msg>.pb
 * Authenticate message against admin
 * 
 * 
 */
int main(int argc, char** argv) {
    std::string test_pubkey("/Users/jameskeener/chub/jim@jimkeener.com-266hHqDrb.k0P3wLWqCJ02rZTHqBEGEct4fCxxH5xSyFV0TPq9Jb5T2gu6H8oH2.eFqYEg16wd14YH9fvYH3VQ--.pub.pb");
    auto pk = loadProtoBufFromFile<PublicKey>(test_pubkey);

    std::string test_post("/Users/jameskeener/chub/3d256bfd-5737-47f8-874b-6dc5f687b573.post.pb");
    auto sm = loadProtoBufFromFile<SignedMessage>(test_post);

    std::cout << bytes2uuid2string(sm.id()) << "\t" << sm.post().title() << std::endl;

    std::cout << verify(sm, pk) << std::endl;

    return 0;
}
