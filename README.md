## openid-connect-server

This is a modified version of the MITREid Connect server that connects to a backend LDAP 
system for its user information and authentication, originally derived from the main Github repository
at: https://github.com/mitreid-connect/ldap-openid-connect-server.


### To use this application

The build process uses Maven, and like the original, is implemented as an overlay.

To build:

    mvn package
    
To run in development mode:

    mvn jetty:run


### Notable changes

 * Runs under Maven Jetty for easy testing
 * Switched to use MySQL (for now) as a persistence layer rather than in-memory HSQLDB
 * Architectural changes to improve Active Directory


### Active Directory

Like the original application, this uses Spring Security for the back-end authentication, and 
making that work with Active Directory requires a little architectural work. This is because
normal deployments of Active Directory discourage service accounts for retrieving user 
details (and this is not a bad thing, I'd suggest). So although all the user details are
available at authentication time, they can't be looked up again after authentication without
a service account. 

For this reason, the logic to retrieve user details is entirely different to that of the 
original implementation. 
