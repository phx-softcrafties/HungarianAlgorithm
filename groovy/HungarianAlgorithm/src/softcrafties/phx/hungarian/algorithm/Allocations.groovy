package softcrafties.phx.hungarian.algorithm

import softcrafties.phx.hungarian.abstractions.Allocation
import softcrafties.phx.hungarian.abstractions.Resource
import softcrafties.phx.hungarian.abstractions.Task

class Allocations implements Iterable<Allocation> {
	private Set<Allocation> allocations
	
	public Allocations(Collection<Allocation> alloc){
		validate(alloc)
		initialize(alloc)
	}
	
	private void validate(Collection<Allocation> alloc){
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
	
	public Map<Resource, List<Allocations>> getMatrix(){
		def result = [:]
		getResources().each {
			result[it] = findAllAllocationsFor(it)
		}
		result
	}
	
	public Set<Resource> getResources(){
		allocations.collect { it.resource }.unique()
	}
	
	public Set<Task> getTasks(){
		allocations.collect { it.task }.unique()
	}
	
	public List<Allocations> findAllAllocationsFor(Resource res){
		allocations.findAll{ it.resource == res }.toList()
	}
	
	public Allocation findMinimumAllocationFor(Resource res){
		def allocs = findAllAllocationsFor(res)
		allocs.min{ it.cost } 
	}
	
	public List<Allocations> findAllAllocationsFor(Task tsk){
		allocations.findAll{ it.task == tsk }.toList()
	}
	
	public Allocation findMinimumAllocationFor(Task tsk){
		def allocs = findAllAllocationsFor(tsk)
		allocs.min{ it.cost }
	}
	
	public Iterator<Allocation> iterator(){
		return getMatrix().iterator()
	}
}
