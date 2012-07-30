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
	Resource steve
	Resource alan
	Task bathroom
	Task floors
	Task windows
	
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
		steve = new Worker(name:"Steve")
		alan = new Worker(name:"Alan")
	}
	
	public void setupTasks(){
		bathroom = new Job(name:"Clean Bathrooms")
		floors = new Job(name:"Sweep floors")
		windows = new Job(name:"Wash windows")
	}
	
	public def appendAllocation(double cost, Resource worker, Task job){
		allocs += [ fac.create(cost, worker, job) ]
	}

	@Test
	public void testCanFullyAssignSuccessfullyDumb(){
		appendAllocation(0, jim, bathroom)
		toAssign = new Allocations(allocs)
		assertTrue ass.canFullyAssign(toAssign)
	}
	
	@Test
	public void testCannotFullyAssignSuccessfullyDumb(){
		appendAllocation(5, jim, bathroom)
		toAssign = new Allocations(allocs)
		assertFalse ass.canFullyAssign(toAssign)
	}
	
	@Test
	public void testCanFullyAssignSuccessfullyEasy(){
		appendAllocation(0, jim, bathroom)
		appendAllocation(1, jim, floors)
		appendAllocation(1, steve, bathroom)
		appendAllocation(0, steve, floors)
		toAssign = new Allocations(allocs)
		assertTrue ass.canFullyAssign(toAssign)		
	}
	
	
	@Test
	public void testCannotFullyAssignSuccessfullyWhenTwoResourcesHaveEqualCost(){
		appendAllocation(0, jim, bathroom)
		appendAllocation(1, jim, floors)
		appendAllocation(0, steve, bathroom)
		appendAllocation(1, steve, floors)
		toAssign = new Allocations(allocs)
		assertFalse ass.canFullyAssign(toAssign)
	}
	
	@Test
	public void testCanFullyAssignSuccessfullyWhenSwapped(){
		appendAllocation(1, jim, bathroom)
		appendAllocation(0, jim, floors)
		appendAllocation(0, steve, bathroom)
		appendAllocation(1, steve, floors)
		toAssign = new Allocations(allocs)
		assertTrue ass.canFullyAssign(toAssign)
	}
	
	@Test
	public void testCanFullyAssignSuccessfullyWhenInvertedZeros(){
		appendAllocation(0, jim, bathroom)
		appendAllocation(0, jim, floors)
		appendAllocation(0, steve, bathroom)
		appendAllocation(1, steve, floors)
		toAssign = new Allocations(allocs)
		assertTrue ass.canFullyAssign(toAssign)
	}
	
	@Test
	public void testRemoveWorkerAndTask() {
		appendAllocation(0, jim, bathroom)
		appendAllocation(0, jim, floors)
		appendAllocation(0, steve, bathroom)
		appendAllocation(0, steve, floors)
		toAssign = new Allocations(allocs)
		def result = ass.extractUnassigned(toAssign, jim, bathroom)
		def expected = new Allocations([fac.create(0, steve, floors)])
		assertEquals expected, result	
	}
	
	@Test
	public void testAssignWikipediaReduced(){
		appendAllocation(0, jim, bathroom)
		appendAllocation(1, jim, floors)
		appendAllocation(2, jim, windows)
		appendAllocation(0, steve, bathroom)
		appendAllocation(0, steve, floors)
		appendAllocation(0, steve, windows)
		appendAllocation(1, alan, bathroom)
		appendAllocation(1, alan, floors)
		appendAllocation(0, alan, windows)
		toAssign = new Allocations(allocs)
		def seed = []
		def result = ass.assign(toAssign, seed.toSet())
		def expected = [
							fac.create(0, jim, bathroom),
							fac.create(0, steve, floors),
							fac.create(0, alan, windows)
					   ].toSet()
		assertEquals expected, result
	}
}
