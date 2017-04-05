
#include <fstream> 
#include <iostream>
#include <algorithm>

#include <boost/optional.hpp>
using boost::optional;

#include <boost/filesystem.hpp>
namespace fs = boost::filesystem;

#include <boost/boostache/boostache.hpp>
#include <boost/boostache/frontend/stache/grammar_def.hpp> // need to work out header only syntax
#include <boost/boostache/stache.hpp>
#include <boost/boostache/model/helper.hpp>
#include <iostream>
#include <sstream>

namespace boostache = boost::boostache;


#include "ed25519/src/ed25519.h"

#include "message.pb.h"

#include "utils.h"
#include "exceptions.h"
#include "progoptions.h"
#include "manifest.h"

#include <webdav/client.hpp>

#include <boost/property_tree/ptree.hpp>
#include <boost/property_tree/ini_parser.hpp>

using namespace org::metamesh::chub;

/*
 * Grab messages from server
 *  /chub/YYYY/MM/DD/<uuid>.<msg>.pb
 * Authenticate message against admin
 * 
 * 
 */
int main(int argc, char** argv) {
    auto vm = parse_options(argc, argv);

//    std::string test_pubkey("/Users/jameskeener/chub/jim@jimkeener.com-266hHqDrb.k0P3wLWqCJ02rZTHqBEGEct4fCxxH5xSyFV0TPq9Jb5T2gu6H8oH2.eFqYEg16wd14YH9fvYH3VQ--.pub.pb");
//    std::string test_pubkey("/Users/jameskeener/chub/jim@jimkeener.com-9sAuChbPF75B813jUCX4sKP8JwZtlEmQkiYZm2tSy_W_fP0WJ0dyxjF38uxaeobSvB7Op3svNykrPcMj42ne4g--.pub.pb");
//    auto pk = *loadProtoBufFromFile<PublicKey>(test_pubkey);
    //    std::string test_post("/Users/jameskeener/chub/3d256bfd-5737-47f8-874b-6dc5f687b573.post.pb");
    //    auto sm = loadProtoBufFromFile<SignedMessage>(test_post);
    //    std::cout << bytes2uuid2string(sm.id()) << '\t' << sm.post().title() << '\t';
    //    std::cout << (verify(sm, pk) ? "" : "in") << "valid-message" << std::endl;

		std::ifstream s("config.ini");
    if(!s)
    {
        std::cerr<<"error"<<std::endl;
        return 1;
    }

    boost::property_tree::ptree pt;
    boost::property_tree::ini_parser::read_ini("config.ini", pt);

    std::map<std::string, std::string> options = {
          {"webdav_hostname", "http://localhost"},
          {"webdav_username", "test"},
          {"webdav_password", "test"},
          {"webdav_root", "/webdav"}
    };

    std::shared_ptr<WebDAV::Client> client(WebDAV::Client::Init(options));
    auto check_connection = client->check("/posts");
		if (!check_connection) {
			return 2;
		}
    std::cout << "SDSDSDSD" << std::endl;
    for(auto filename : client->list("/posts/")) {
      auto path = vm.msg_dir + "/" + filename;
      std::cout << "Checking " << filename << std::endl;
      if (!fs::is_regular_file(path)) {
        std::cout << "Downloading to " << path << std::endl;
        client->download("/posts/" + filename, path);
      }
    }

    std::ifstream manifest(vm.work_dir + "/manifest");
    auto old_manifest = Manifest::fromStream(manifest);
    auto new_manifest = Manifest::fromDirectory(vm.msg_dir);
    auto diff = Manifest::diff(new_manifest, old_manifest);
    
    std::vector<SignedMessage> sms;

    //std::cout << "Difference:" << std::endl;
    //for (auto d : diff) {
    //    std::cout << d.filename << std::endl;
    //}

    std::vector<optional < SignedMessage>> osms;
    std::transform(diff.begin(), diff.end(), std::back_inserter(osms), [](const auto & mi) {
        return loadProtoBufFromFile<SignedMessage>(mi.filename);
    });

    /* remove the files that didn't parse correctly 
     */
    osms.erase(std::remove_if(osms.begin(), osms.end(), [](const auto & sm) {
        return !sm;
    }), osms.end());


    /* unwraps the optional so that the rest of the application doesn't 
     * need to worry about it
     */
    std::transform(osms.begin(), osms.end(), std::back_inserter(sms), [](auto & sm) {
        return *sm;
    });

//    /* remove the files that aren't correctly signed
//     */
//    sms.erase(std::remove_if(sms.begin(), sms.end(), [& pk](auto & sm) {
//        return verify(sm, pk);
//    }), osms.end());


    std::vector<optional<SignedMessage>> old_osms;
    /* Go through the old manifest and load valid files
     */
    std::transform(old_manifest.begin(), old_manifest.end(), std::back_inserter(old_osms), [](const auto & mi) {
        if (!mi.valid) {
            return optional<SignedMessage>();
        }
        return loadProtoBufFromFile<SignedMessage>(mi.filename);
    });

    /* remove the files that didn't parse correctly 
     */
    old_osms.erase(std::remove_if(old_osms.begin(), old_osms.end(), [](const auto & sm) {
        return !sm;
    }), old_osms.end());


    /* unwraps the optional so that the rest of the application doesn't 
     * need to worry about it
     */
    std::transform(old_osms.begin(), old_osms.end(), std::back_inserter(sms), [](auto & sm) {
        return *sm;
    });

    for (auto sm : sms) {
        //std::cout << bytes2uuid2string(sm.id()) << '\t' << sm.post().title() << '\t' << std::endl;
                //<< (verify(sm, pk) ? "" : "in") << "valid-message" << std::endl;

        if (sm.has_post()) {

        } else {

        }
    }



    std::vector<std::map<std::string, std::string>> c;
    std::transform(sms.begin(), sms.end(), std::back_inserter(c), [](const auto & sm) {
        return postToObjectType(sm.post()); 
    });

    std::ifstream template_file("splash.mustache");
    std::string tplate((std::istreambuf_iterator<char>(template_file)), std::istreambuf_iterator<char>());

    auto iter = tplate.begin();
    auto templ = boostache::load_template<boostache::format::stache>(iter, tplate.end());
    
    std::stringstream stream;
    boostache::generate(stream, templ, c);

    std::cout << stream.str() << std::endl;
    return 0;
}
