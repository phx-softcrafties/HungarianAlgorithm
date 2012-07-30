package softcrafties.phx.hungarian.algorithm

import softcrafties.phx.hungarian.abstractions.Allocation
import softcrafties.phx.hungarian.abstractions.Resource
import softcrafties.phx.hungarian.abstractions.Task

class Assigner {
	public static boolean canFullyAssign(Allocations alloc) {  
		assign(alloc, new LinkedHashSet())
	}
	
	public static Set<Allocation> assign(Allocations alloc, Set<Allocation> seed){
		def workers = alloc.resources
		Resource firstWorker = workers[0]
		List<Allocation> allocations = alloc.findNoCostAllocationsFor(firstWorker)
		def result= allocations.find{ checkRemainingAssignments(alloc, it, seed) }
		if(result){
			seed.add(result)
			println "the result is ${result} the seed is ${seed}"
		}
		seed
	}
	
	public static boolean checkRemainingAssignments(Allocations alloc, Allocation candidate, Set<Allocation> seed){
		boolean canAssign = true
		if(alloc.dimensions > 1){
			def reduced = extractUnassigned(alloc, candidate.resource, candidate.task)
			canAssign = assign(reduced, seed)
		}
		canAssign
	}
	
	public static Allocations extractUnassigned(Allocations alls, Resource res, Task tsk){
		def others = alls.findAll{ Allocation al ->
			al.resource != res && al.task != tsk
		}
		new Allocations(others)
	}
}
