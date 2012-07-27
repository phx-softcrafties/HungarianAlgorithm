package softcrafties.phx.hungarian.algorithm

import softcrafties.phx.hungarian.abstractions.Allocation
import softcrafties.phx.hungarian.abstractions.Resource
import softcrafties.phx.hungarian.abstractions.Task

class Allocations {
	private Set<Allocation> allocations
	
	public Allocations(Collection<Allocation> alloc){
		validate(alloc)
		initialize(alloc)
	}
	
	private void validate(Collection<Allocation> alloc){
		if(alloc.size() % 2 != 0){
			throw new UnsupportedOperationException("This matrix is not square")
		}
		int workers = countEm(alloc, "resource")
		int tasks = countEm(alloc, "task")
		if( workers != tasks ){
			throw new UnsupportedOperationException("This matrix should have an equal number of workers and tasks")
		}
	}
	
	private int countEm(Collection<Allocation> alloc, String toCount){
		def elems = []
		alloc.each{
			def interested = (toCount.equals("resource")) ? it.resource : it.task
			if(!elems.contains(interested)){
				elems.add(interested)
			}
		}
		elems.size()
	}
	
	private void initialize(Collection<Allocation> alloc){
		allocations = new LinkedHashSet<Allocation>(alloc)
		alloc.each{
			allocations.add(it)
		}
	}
	
	public Allocation findMinimumAllocationFor(Resource res){
		def allocs = allocations.findAll{ it.resource == res }
		allocs.min{ it.cost } 
	}
	
	public Allocation findMinimumAllocationFor(Task tsk){
		def allocs = allocations.findAll{ it.task == tsk }
		allocs.min{ it.cost }
	}
}
