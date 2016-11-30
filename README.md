# CHub

*Note*: This application is still in development.

CHub is a Community Hub. It allows trusted "Updaters" to post updates and
messages to router splash pages. CHub allows any router to listen to
multiple "Updaters" as well as the "System".  The "System" is useful for
network-wide updates, but also allows "Updaters" to be revoked and messages
to be removed when an "Updater" has been compromised.

## Parts

Four pieces of the software that needs to be created for CHub:

### SignMessage

End-User, _i.e._ the people making posts, will use this application
to generate their inital key and all posts.

This is configured to listen to different channels, as decided by the
router owner.

### Server

Acts as a central repository for all messages. (May be a webdav server?)


### splashgen

Generates the splash page on the router. 

### AdminTools

Tools to sign end user public keys, change admin keys, and revoke 
certificates and posts (subject to quorum). 

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
