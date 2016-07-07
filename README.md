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

A Java program that allows the creation of CRL and MRL messages that
revoke user access and mass delete specific messages from all connected
devices on a Chub subcription. These messages will be signed through the
use of (Shamir's Secret Sharing) requiring multiple board
members/advisors to agree and meet to approve this action. The use of
this program will require a transparency guarantee or mandate or ruleset
or whatever. Remember that upon use of 1984, there is a "trail" or
record of denied users and messages via the revocation list on each
router (as long as they are subscribed to the System Subscription).
There is also a mandatory field describing what was deleted, why, and
when.

## Notes 

The CHub server and client understand System, and only System, broadcasts
for:

* CRLs so that we can revoke naughty users. 
* MRLs (Message Revocation Lists) so that we can revoke naughty messages

