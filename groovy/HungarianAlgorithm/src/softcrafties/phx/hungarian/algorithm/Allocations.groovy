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
	
	public List<Resource> getResources(){
		allocations.collect { it.resource }.unique()
	}
	
	public List<Task> getTasks(){
		allocations.collect { it.task }.unique()
	}
	
	public List<Allocations> findAllAllocationsFor(Resource res){
		allocations.findAll{ it.resource == res }.toList()
	}
	
	public List<Allocations> findNoCostAllocationsFor(Resource res){
		findAllAllocationsFor(res).findAll{ it.cost == 0 }.unique()
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
	
	public List<Allocations> findNoCostAllocationsFor(Task tsk){
		findAllAllocationsFor(tsk).findAll{ it.cost == 0 }.unique()
	}
	
	public Iterator<Allocation> iterator(){
		return allocations.iterator()
	}
	
	public int getDimensions(){ getResources().size() }
	
	@Override
	public boolean equals(Object another){
		if(!(another instanceof Allocations)){
			return false
		}
		def o = (Allocations) another
		allocations.equals(o.allocations)
	}
	
	@Override
	public int hashCode(){
		allocations.hashCode()
	}
	
	@Override
	public String toString() { "${allocations}" }
}
