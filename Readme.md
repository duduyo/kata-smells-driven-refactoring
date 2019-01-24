
# Smells driven refactoring kata

## Objective

This kata shows how code smells, refactorings and clean architecture are linked together.
It illustrates :
- how to make a diagnosis on your code based on "code smells", how to use this diagnosis to organize your refactoring steps
- how to make each refactoring without breaking the tests, with baby steps
- how your IDE can (greatly !)  help
- how to transform the global architecture of your system to a "clean architecture"


## Try it

Where to start :

* identify some code smells
* refactor the code in order to suppress the code smells
* do it again if needed

The codebase is a small application, exposing a call management API
- POST /calls : create and store a call
- POST /calls/{callId}/events : add an event on a call (CREATED, RINGING, CONNECTED, TERMINATED)
- GET /calls/{callId} : get a call, including calculated data such a geozone & dates
 
Constraints :
* the API spec is fixed, we cannot change it
* the database structure is fixed, we cannot change it

## Tips and solution

See commit order on the "solution" branch. The solution of the step 0, is in the commit called "1-xxx", etc....

0- Run tests with coverage : some code is not covered by tests. Complete the CallST class to have 100% coverage

1- Look for smells in the code

* CallController : big class, big methods
* CallService : big class + middle-man between CallController and CallRepository
* Call : many parameters on constructors, many fields, anemic model
* CallRepository : implicit behaviour
* CallDao : big class, code duplication

2- Refactor CallController :  move code to other classes

* how = extract methods, and then move methods from CallController to CallService or to Call
*  IDE refactoring  tips = use "extract method" and "move" feature

3- Refactor CallService : move code to a new class

* how = create a new field for the new class, inject it in the CallService constructor, and then move methods to this new class. Take care of the dependency between methods while moving
* IDE refactoring tips = use "add to constructor" on the field, use "move" on the methods

4- Refactor CallDao : move code to a class that is not accessible in the scope


* how = as there is no relationship between CallDao and Call class (where we'd like to move the code), we have to do it manually.
Do parallel change : copy/paste the method to the new location, rename the new method with "New" suffix, make it compile. Step by step, replace in the code the calls to the old method by calls to the new one. At the end, delete the old method 
At the end, delete the old method

* IDE refactoring tips = we cannot use the "move" feature, so we copy/paste. But then we can use "rename", "inline", etc... to switch to the new code

5- Refactor Call class : introduce a Participant object to reduce fields and parameters number

* how = on the constructor, extract an object from the 3 participant parameters. This creates the Participant class. Do it on the second constructor. Create the participant field, and use it in the code. Delete participantXxx fields when finished
* IDE refactoring tips : Use "extract parameter object" on the 2 constructors

6- Refactor CallService : extract storage feature to a new class CallRepositoryPort

* how : extract code to be moved in private methods and then move it to a new class
* IDE refactoring tips : Do it as in step 2, or try the "Extract Delegate" refactoring action
 
7- Refactor the application to a clean architecture :

* how = extract interface for CallRepositoryPort, with an implementing class called CallRepositoryAdapter. And the arrange packages  and classes so that the service package does not depend on any other package
* IDE refactoring tips : Use "Extract interface" feature
* IDE dependency analysis tips : use "Analyse dependcy matrix" on the project to check dependencies between packages
