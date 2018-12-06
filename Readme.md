
Context :

* the API is fixed, we cannot change it
* the database structure is fixed, we cannot change it
* how to clean the code ?

Smells :
* CallController : large class (bloaters-large class), has several reasons to change (change preventer-divergent change), duplicate code (dispensable)
* Test smell on callController : exception code is not covered by tests
* CallService : middle-man (couplers),but also has several reasons to change...

