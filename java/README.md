This is an implementation of the HungarianAlgorithm in Java, done
as an exercise in TDD  CleanCode techniques.

The algorithm is based on the article
http://onlinelibrary.wiley.com/doi/10.1002/nav.20056/pdf

Features:
* No matrices, so we'll suited for large, sparse problems.
* Object oriented: Resources, Tasks, and Bids.
* Unit test coverage.
* Clean code style is supposed to be readable.
Note: since the algorithm is fairly mathematical (operations on a bipartite
graph), the source may take some more refactoring before it is clear.

To Do:
* Needs benchmarking, especially against established FORTRAN 77 routines.
* Some links of nodes to bids (edges) on the bipartite tree may help 
efficiency.
* Not fully tested yet, but works on a couple of example cases and passes all 
unit tests.