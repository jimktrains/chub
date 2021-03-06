#pragma once

#include <cstring>
#include <boost/optional.hpp>
using boost::optional;

#include <boost/uuid/uuid.hpp>
#include <boost/uuid/uuid_io.hpp>
#include <boost/archive/iterators/base64_from_binary.hpp>
#include <boost/archive/iterators/insert_linebreaks.hpp>
#include <boost/archive/iterators/transform_width.hpp>
#include <boost/archive/iterators/ostream_iterator.hpp>
using namespace boost::archive::iterators;

#include "ed25519/src/ed25519.h"

#include "message.pb.h"
#include "exceptions.h"


namespace org {
    namespace metamesh {
        namespace chub {
            const int ASN1_PREFIX_LENGTH = 15;

            const unsigned char ASN1_PREFIX[ASN1_PREFIX_LENGTH] = {
                0x30, 0x2D, 0x30,
                0x08, 0x06, 0x03,
                0x2B, 0x65, 0x64,
                0x0A, 0x01, 0x01,
                0x03, 0x21, 0x00
            };

            const unsigned char* PublicKeyToBytes(const PublicKey pk) {
                if (pk.key().length() != 47) throw new PublicKeyNotCorrectLength;
                if (pk.encodingtype() != KeyEncodingType::x509) throw new PublicKeyNotX509;
                if (std::memcmp(ASN1_PREFIX, pk.key().c_str(), ASN1_PREFIX_LENGTH) != 0) throw new PublicKeyx509PrefixInvalid;

                return reinterpret_cast<const unsigned char*> (pk.key().c_str() + ASN1_PREFIX_LENGTH);
            }

            // These are so small that copying them isn't much more terrible
            // than copying the pointer

            boost::uuids::uuid bytes2uuid(const std::string bytes) {
                if (bytes.length() != 16) throw new InvalidUUIDBytes;

                boost::uuids::uuid u;

                memcpy(&u, bytes.c_str(), 16);

                return u;
            }

            std::string bytes2uuid2string(const std::string bytes) {
                std::string s = boost::uuids::to_string(bytes2uuid(bytes));
                return s;
            }

            bool verify(const SignedMessage &sm, const PublicKey &pk) {
                std::string serpost = sm.post().SerializeAsString();

                std::string sig = sm.message_signature().signature();
                return ed25519_verify(
                        reinterpret_cast<const unsigned char*> (sig.c_str()),
                        reinterpret_cast<const unsigned char*> (serpost.c_str()),
                        serpost.length(),
                        PublicKeyToBytes(pk)
                        );
            }

            template<typename T>
            optional<T> loadProtoBufFromFile(const std::string &filename) {
                std::ifstream pubkeyfile;
                pubkeyfile.open(filename, std::ifstream::in);
                T pk;
                auto res = pk.ParseFromIstream(&pubkeyfile);
                if (res) {
                    return pk;
                }
                return
                {
                };
            }

            auto postToObjectType(Post p) {
              std::map<std::string, std::string> data = { 
                { "id", bytes2uuid2string(p.id()) },
                { "title"  , p.title() },
                { "description"   , p.description()     },
                { "test", "test"},
                { "image", ""}
              };
              if (p.has_image()) {
                  std::stringstream os;
                  typedef
                  insert_linebreaks< // insert line breaks every 72 characters
                          base64_from_binary< // convert binary values to base64 characters
                          transform_width< // retrieve 6 bit integers from a sequence of 8 bit bytes
                          const char *,
                          6,
                          8
                          >
                          >
                          , INT_MAX
                          >
                          base64_text; // compose all the above operations in to a new iterator
                  std::string s = p.image().image();
                  std::copy(
                          base64_text(s.c_str()),
                          base64_text(s.c_str() + s.size()),
                          ostream_iterator<char>(os)
                  );
                  data["image"] = std::string("data:image/jpeg;base64,") + os.str();
            }
            return data;
          }
        }
    }
}
