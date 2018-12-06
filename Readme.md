
# Smells driven refactoring

This application provides calls management feature : 
- create and store a call
- add events on a call
- get a call and calculated informations depending on events
 
What to do for this kata ?

* identify some code smells
* refactor the code in order to suppress the code smells
* do it again if needed
 
Constraints :

* the API is fixed, we cannot change it
* the database structure is fixed, we cannot change it

# Tips and solution

Smells :

* CallController : large class (bloaters-large class), has several reasons to change (change preventer-divergent change), duplicate code (dispensable)
* Test smell on callController : exception code is not covered by tests
* CallService : middle-man (couplers),but also has several reasons to change...
* Call : too many parameters on constructors, too many fields
* CallRepository : implicit behaviour
* CallDao : large class, many reasons to change. Test smell : code is not covered by unit tests

First pass on smells :

* CallController : large class, large methods
* Too many param in Call +  anemic model
* Large class in CallDao
* Tests smells : some rules are not covered by unit tests => not in the godd place

First steps on CallController : extract and move to existing object
=> reduce the size of CallController
* extract methods with business logic, and then move it to CallService
* extract method with geozone logic, and then move it to Call

Second step on CallService : move to new object

=> reduce the size, with the new methods, this one is now too big
* create a new field and a new CallDaoMapper class, inject it in CallService
* move mapping methods

+ reorder methods 