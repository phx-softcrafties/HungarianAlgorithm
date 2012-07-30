package softcrafties.phx.hungarian;

import static org.junit.Assert.*;
import org.junit.Test
import softcrafties.phx.hungarian.abstractions.Task
import softcrafties.phx.hungarian.algorithm.Allocations

class AllocationsTest {
	private Allocations toTest
	private CostFactory fac = new CostFactory()
	
	private def createUnequal(){
		[
			fac.create(1, new Worker(name:"dumb"),  new Job(name:"silly")),
			fac.create(1, new Worker(name:"dumb"),  new Job(name:"billy"))
		]
	}
	
	private def createValid(){
		[
			fac.create(1, new Worker(name:"dumb"),  new Job(name:"silly")),
			fac.create(2, new Worker(name:"dumb"),  new Job(name:"billy")),
			fac.create(4, new Worker(name:"nilly"), new Job(name:"silly")),
			fac.create(2, new Worker(name:"nilly"), new Job(name:"billy"))
		]
	}
	
	private def createValidWithFree(){
		[
			fac.create(0, new Worker(name:"dumb"),  new Job(name:"silly")),
			fac.create(2, new Worker(name:"dumb"),  new Job(name:"billy")),
			fac.create(0, new Worker(name:"nilly"), new Job(name:"silly")),
			fac.create(0, new Worker(name:"nilly"), new Job(name:"billy"))
		]
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testNonEqualWorkersAndTasks(){
		toTest = new Allocations(createUnequal())
	}
	
	@Test
	public void testValid(){
		toTest = new Allocations(createValid())
	}
	
	@Test 
	public void testGetResources(){
		toTest = new Allocations(createValid())
		def expected = [ new Worker(name:"dumb"),  new Worker(name:"nilly") ]
		assertEquals expected, toTest.resources
	}
	
	@Test
	public void testGetTasks(){
		toTest = new Allocations(createValid())
		def expected = [ new Job(name:"silly"),  new Job(name:"billy") ]
		assertEquals expected, toTest.tasks
	}
	
	@Test
	public void testFindAllAllocationsForResource(){
		Worker dumb = new Worker(name:"dumb")
		toTest = new Allocations(createValid())
		def expected = [ fac.create(1, new Worker(name:"dumb"),  new Job(name:"silly")),
						 fac.create(2, new Worker(name:"dumb"),  new Job(name:"billy")) ]
		assertEquals expected, toTest.findAllAllocationsFor(dumb)
	}
	
	@Test
	public void testFindMinimumAllocationForResource(){
		Worker dumb = new Worker(name:"dumb")
		toTest = new Allocations(createValid())
		def expected = fac.create(1, new Worker(name:"dumb"),  new Job(name:"silly"))
		assertEquals expected, toTest.findMinimumAllocationFor(dumb)
	}
	
	@Test
	public void testNoCostAllocationForResource(){
		Worker dumb = new Worker(name:"dumb")
		toTest = new Allocations(createValidWithFree())
		def expected = [ fac.create(0, dumb,  new Job(name:"silly")) ]
		assertEquals expected, toTest.findNoCostAllocationsFor(dumb)
		Worker nilly = new Worker(name:"nilly")
		expected = [ fac.create(0, nilly,  new Job(name:"silly")), 
					 fac.create(0, nilly,  new Job(name:"billy")) ]
		assertEquals expected, toTest.findNoCostAllocationsFor(nilly)
	}
	
	@Test
	public void testFindAllAllocationsForTask(){
		Task silly = new Job(name:"silly")
		toTest = new Allocations(createValid())
		def expected = [ fac.create(1, new Worker(name:"dumb"),  new Job(name:"silly")),
						 fac.create(4, new Worker(name:"nilly"),  new Job(name:"silly")) ]
		assertEquals expected, toTest.findAllAllocationsFor(silly)
	}
	
	@Test
	public void testFindMinimumAllocationForTask(){
		Task silly = new Job(name:"silly")
		toTest = new Allocations(createValid())
		def expected = fac.create(1, new Worker(name:"dumb"),  new Job(name:"silly"))
		assertEquals expected, toTest.findMinimumAllocationFor(silly)
	}
	
	@Test
	public void testNoCostAllocationForTask(){
		Worker dumb = new Worker(name:"dumb")
		Worker nilly = new Worker(name:"nilly")
		Task silly = new Job(name:"silly")
		toTest = new Allocations(createValidWithFree())
		def expected = [ fac.create(0, dumb,  silly), fac.create(0, nilly,  silly) ]
		assertEquals expected, toTest.findNoCostAllocationsFor(silly)
		Task billy = new Job(name:"billy")
		expected = [ fac.create(0, nilly,  billy) ]
		assertEquals expected, toTest.findNoCostAllocationsFor(billy)
	}
	
	@Test
	public void testCanGetMatrixDimensions(){
		toTest = new Allocations(createValid())
		assertEquals 2, toTest.dimensions
	}
}