# Project 1 Part 2: NFA to DFA
 
* Author: Anne Brinegar, Megan Pierce
* Class: CS361 Section 2
* Semester: Spring 2018

## Overview

This Java application creates a model of a nondeterministic finite automaton 
(NFA), which is then changed to an equivalent DFA. Strings can then be processed 
under the DFA to determine if they meet the requirements of the language or not.

## Compiling and Using

To compile, execute the following command in the main project directory:
```
$ javac fa/nfa/NFADriver.java
```

Run the compiled class with the command:
```
$ java fa.nfa.NFADriver ./tests/<testTextFile>
```


## Discussion

We started this project with a pretty good idea of how we were to execute it. We 
drew up examples and talked through how we would access everything we needed to 
build an NFA, and change it into an equivalent DFA. Our coding was smooth to 
begin with, until we got to the epsilon function. We decided to make a helper 
function, so that we could recursively pass in the current state, as well as the 
set being build of other sets that we could transition to. It took us a while to 
wrap our head around exactly how this method was to work correctly, but after 
some time we were able to continually step through the states that were part of 
the closure, until the closure ended.

We thought that getting past this method would make the rest of the program go 
smoothly, but we underestimated the complexity of the getDFA method. First, when 
we were building the transition table for the NFA, we struggled to deal with the 
empty transitions potentially coming before and/or after a normal transition. 
Once we implemented a queue and explored the closure of each state that we 
reached, we were able to correctly fill in the table.

Once we were able to map out the NFA table, we moved on to creating new states 
from the transitions, and mapping the new DFA transitions. The way we were 
initially implementing this included extra DFA transitions, instead of using a 
breadth first or depth first search-like algorithm to only find the applicable 
DFA states. Once got all of our many loops correct and created a recursive function to find all of the DFA transitions, our program was almost working.

Our last struggle had to do with incorrectly passing in arguments to our makeDFA 
function. One of the arguments was a set of strings, and we were mistakenly 
passing in an empty set, which resulted in a null pointer exception in our 
makeDFA function. The stack trace didn't point to our NFA class at all, which 
made it really tricky to find where the error was coming from. Once we realized 
that we needed to initialize the string set with our start string, our program 
ran correctly and passed all of our tests.

Overall, this was one of the most complex programs we have worked on. We 
continually underestimated the complexity of what needed to be done, and 
struggled to wrap our heads around everything that was happening as our program 
grew larger and more convoluted. Although time consuming and frustrating at 
times, it was a good experience to think about how these machines work and 
transform, and be able to correctly represent them in our code.
