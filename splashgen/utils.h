#pragma once

#include <cstring>

#include <boost/uuid/uuid.hpp>
#include <boost/uuid/uuid_io.hpp>

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

            const unsigned char* PublicKeyToBytes(PublicKey pk) {
                if (pk.key().length() != 47) throw new PublicKeyNotCorrectLength;
                if (pk.encodingtype() != KeyEncodingType::x509) throw new PublicKeyNotX509;
                if (std::memcmp(ASN1_PREFIX, pk.key().c_str(), ASN1_PREFIX_LENGTH) != 0) throw new PublicKeyx509PrefixInvalid;

                return reinterpret_cast<const unsigned char*> (pk.key().c_str() + ASN1_PREFIX_LENGTH);
            }

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
        }
    }
}