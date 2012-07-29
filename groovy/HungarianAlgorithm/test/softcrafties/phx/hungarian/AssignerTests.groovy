package softcrafties.phx.hungarian;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Ignore;
import softcrafties.phx.hungarian.abstractions.AllocationFactory
import softcrafties.phx.hungarian.abstractions.Resource
import softcrafties.phx.hungarian.abstractions.Task
import softcrafties.phx.hungarian.algorithm.Allocations
import softcrafties.phx.hungarian.algorithm.Assigner

import org.junit.Before;

class AssignerTests {
	Allocations toAssign
	AllocationFactory fac
	Collection<Allocations> allocs
	Assigner ass
	Resource jim
	Resource bob
	Task running
	Task eating
	
	@Before
	public void setup(){
		allocs = []
		fac = new CostFactory()
		ass = new Assigner()
		setupResources()
		setupTasks()
	}
	
	public void setupResources(){
		jim = new Worker(name:"Jim")
		bob = new Worker(name:"Bob")
	}
	
	public void setupTasks(){
		running = new Job(name:"Running")
		eating = new Job(name:"Eating")
	}
	
	public def appendAllocation(double cost, Resource worker, Task job){
		allocs += [ fac.create(cost, worker, job) ]
	}

	@Test
	public void testCanFullyAssignSuccessfullyDumb(){
		appendAllocation(0, jim, running)
		toAssign = new Allocations(allocs)
		assertTrue ass.canFullyAssign(toAssign)
	}
	
	@Test
	public void testCannotFullyAssignSuccessfullyDumb(){
		appendAllocation(5, jim, running)
		toAssign = new Allocations(allocs)
		assertFalse ass.canFullyAssign(toAssign)
	}
	
	@Test
	public void testCanFullyAssignSuccessfullyEasy(){
		appendAllocation(0, jim, running)
		appendAllocation(1, jim, eating)
		appendAllocation(1, bob, running)
		appendAllocation(0, bob, eating)
		toAssign = new Allocations(allocs)
		assertTrue ass.canFullyAssign(toAssign)		
	}
	
	
	@Test
	public void testCannotFullyAssignSuccessfullyWhenTwoResourcesHaveEqualCost(){
		appendAllocation(0, jim, running)
		appendAllocation(1, jim, eating)
		appendAllocation(0, bob, running)
		appendAllocation(1, bob, eating)
		toAssign = new Allocations(allocs)
		assertFalse ass.canFullyAssign(toAssign)
	}
	
	@Test
	public void testCanFullyAssignSuccessfullyWhenSwapped(){
		appendAllocation(1, jim, running)
		appendAllocation(0, jim, eating)
		appendAllocation(0, bob, running)
		appendAllocation(1, bob, eating)
		toAssign = new Allocations(allocs)
		assertTrue ass.canFullyAssign(toAssign)
	}
	
	@Test
	public void testCanFullyAssignSuccessfullyWhenInvertedZeros(){
		appendAllocation(0, jim, running)
		appendAllocation(0, jim, eating)
		appendAllocation(0, bob, running)
		appendAllocation(1, bob, eating)
		toAssign = new Allocations(allocs)
		assertTrue ass.canFullyAssign(toAssign)
	}
	
	@Test
	public void testRemoveWorkerAndTask() {
		appendAllocation(0, jim, running)
		appendAllocation(0, jim, eating)
		appendAllocation(0, bob, running)
		appendAllocation(0, bob, eating)
		toAssign = new Allocations(allocs)
		def result = ass.assign(toAssign, jim, running)
		def expected = new Allocations([fac.create(0, bob, eating)])
		assertEquals expected, result	
	}
}
