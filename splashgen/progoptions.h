#pragma once

#include <boost/program_options.hpp>
namespace po = boost::program_options;

namespace org {
    namespace metamesh {
        namespace chub {

            class ProgOptions {
            public:

                ProgOptions(po::variables_map vm) 
                    : msg_dir(vm["msg_dir"].as<std::string>()), 
                      work_dir(vm["work_dir"].as<std::string>()) {
                }
                std::string msg_dir;
                std::string work_dir;
            };

            ProgOptions parse_options(int argc, char** argv) {
                // Declare the supported options.
                po::options_description desc("Allowed options");
                desc.add_options()
                        ("help,h", "produce help message")
                        ("msg_dir,m", po::value<std::string>()->required(), "directory where the messages are stored")
                        ("work_dir,w", po::value<std::string>()->required(), "directory where working files are stored")
                        ;

                po::variables_map vm;
                po::store(po::command_line_parser(argc, argv)
                        .options(desc)
                        .run(),
                        vm);
                try {
                    po::notify(vm);
                } catch (boost::program_options::required_option &req) {
                    std::cout << "Error: " << req.what() << std::endl;
                    std::cout << desc << std::endl;
                    std::exit(1);
                }
                if (vm.count("help")) {
                    std::cout << desc << std::endl;
                    std::exit(0);
                }
                return vm;
            }

        }
    }
}
