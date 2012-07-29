package softcrafties.phx.hungarian.algorithm

import softcrafties.phx.hungarian.abstractions.Allocation
import softcrafties.phx.hungarian.abstractions.Resource
import softcrafties.phx.hungarian.abstractions.Task

class Assigner {
	public boolean canFullyAssign(Allocations alloc) {  
		def workers = alloc.resources
		Resource firstWorker = workers[0]
		List<Allocation> allocations = alloc.findNoCostAllocationsFor(firstWorker)
		allocations.any{ checkRemainingAssignments(alloc, it) }
	}
	
	public boolean checkRemainingAssignments(Allocations alloc, Allocation candidate){
		boolean canAssign = true
		if(alloc.dimensions > 1){
			def reduced = assign(alloc, candidate.resource, candidate.task)
			canAssign = canFullyAssign(reduced)
		}
		canAssign
	}
	
	public static Allocations assign(Allocations alls, Resource res, Task tsk){
		def others = alls.findAll{ Allocation al ->
			al.resource != res && al.task != tsk
		}
		new Allocations(others)
	}
	public Collection<Allocation> getAssignments() { [] }
}
