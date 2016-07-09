# CHub

CHub is a Community Hub. It allows trusted "Updaters" to post updates and
messages to router splash pages. CHub allows any router to listen to
multiple "Updaters" as well as the "System".  The "System" is useful for
network-wide updates, but also allows "Updaters" to be revoked and messages
to be removed when an "Updater" has been compromised.

## Parts

Four pieces of the software that needs to be created for CHub:

### Client

Finds CHub server, gets the content for the HTML frame for possibly
multiple "subscribed" areas, and updates its HTML Splash page. 

### Server

Accepts updates from the "CHupdater" CHub Update Client, and accepts
connections from CHub clients and passes frame data to them. 


### Updater

A Java program that allows updates to be sent to the CHub Server. This
program will have all the (optional) fields available in a form as well
as an image resizer for 30(ish)kb.

### 1984

-------

#### Initial Description

A Java program that allows the creation of CRL and MRL messages that
revoke user access and mass delete specific messages from all connected
devices on a Chub subscription. These messages will be signed through the
use of (Shamir's Secret Sharing) requiring multiple board
members/advisors to agree and meet to approve this action. The use of
this program will require a transparency guarantee or mandate or ruleset
or whatever. Remember that upon use of 1984, there is a "trail" or
record of denied users and messages via the revocation list on each
router (as long as they are subscribed to the System Subscription).
There is also a mandatory field describing what was deleted, why, and
when.

-------

#### Working idea

Each board member will have their own public-private key pair.

All public keys will be distributed to all routers.

Routers will decide the proper number of certs needed to accept the command.
The default is $floor( (# certs)/2 ) + 1$.

Commands

* Revoke publisher cert
* Add publisher cert
* Remove message
* Revoke system cert
* Add system cert

Message schema (json format):

    [
      {
        "cmd": "string",
        "oid": "msg id or cert fingerprint",
        "desc": "description",
        "ts": unix-timestamp
      }
    ]

URLs

Must use Last-Updated/If-Modified-Since headers

Any system cert being added must be signed by that cert as well as a quorum
of currently valid system certs.

* /cmd - lists all commands
* /cmd/[unix timestamp].[uuid] - a single command sequence
* /cmd/[unix timestamp].[uuid].[signing fingerprint] - PEM encoded signature
* /crts - lists all certs
* /crts/[fingerprint] - PEM encoded cert (self-signed)

## Notes 

The CHub server and client understand System, and only System, broadcasts
for:

* CRLs so that we can revoke naughty users. 
* MRLs (Message Revocation Lists) so that we can revoke naughty messages

