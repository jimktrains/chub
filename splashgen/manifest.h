#pragma once

#include <cstring>

#include <boost/uuid/uuid.hpp>
#include <boost/uuid/uuid_io.hpp>
#include <boost/tokenizer.hpp>

#include "ed25519/src/ed25519.h"

#include "message.pb.h"
#include "exceptions.h"

namespace org {
    namespace metamesh {
        namespace chub {

            class ManifestItem {
            public:

                ManifestItem(const std::string & _filename, const bool _valid)
                : filename(_filename), valid(_valid) {
                }



                std::string filename;
                bool valid;
            };

            bool operator<(const ManifestItem &mm, const ManifestItem &mi) {
                return mm.filename < mi.filename;
            }

            class Manifest : public std::vector<ManifestItem> {
            public:

                static Manifest fromStream(std::ifstream &manifest_file);

                static Manifest fromDirectory(fs::directory_iterator dir);

                static Manifest fromDirectory(std::string dir);

                static Manifest diff(Manifest &new_manifest, Manifest & old_manifest);
            };

            std::ofstream & operator<<(std::ofstream &out, const Manifest & manifest) {
                for (auto m : manifest) {
                    out << m.filename;
                }
                return out;
            }

            std::ifstream & operator>>(std::ifstream &in, Manifest & manifest) {
                std::string t;
                boost::char_separator<char> sep(" ");
                while (in >> t) {
                    boost::tokenizer<boost::char_separator<char>> tok(t, sep);
                    std::string fn;
                    bool valid;
                    int i = 0;
                    for(auto & s : tok) {
                        if (i == 0) {
                            fn = s;
                        } else {
                            valid = (s == "valid");
                        }
                        i++;
                    }
                    manifest.push_back(ManifestItem(fn, valid));
                }
                return in;
            }

            Manifest Manifest::fromStream(std::ifstream &manifest_file) {
                Manifest m;
                manifest_file >> m;
                return m;
            }

            Manifest Manifest::fromDirectory(fs::directory_iterator dir) {
                Manifest m;
                for (auto n : dir) {
                    auto path = n.path();
                    if (fs::is_regular_file(path)) {
                        m.push_back(ManifestItem(path.c_str(), false));
                    }
                }
                return m;
            }

            Manifest Manifest::fromDirectory(std::string dir) {
                return fromDirectory(fs::directory_iterator(dir));
            }

            Manifest Manifest::diff(Manifest &new_manifest, Manifest & old_manifest) {
                std::sort(old_manifest.begin(), old_manifest.end());
                std::sort(new_manifest.begin(), new_manifest.end());

                Manifest diff;
                std::set_difference(new_manifest.begin(), new_manifest.end(), old_manifest.begin(), old_manifest.end(), std::back_inserter(diff));

                return diff;
            }
        }
    }
}
