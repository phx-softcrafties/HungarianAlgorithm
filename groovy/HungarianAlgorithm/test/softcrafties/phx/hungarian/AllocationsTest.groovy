package softcrafties.phx.hungarian;

import static org.junit.Assert.*;
import org.junit.Test
import softcrafties.phx.hungarian.abstractions.Task
import softcrafties.phx.hungarian.algorithm.Allocations

class AllocationsTest {
	private Allocations toTest
	private CostFactory fac = new CostFactory()
	
	private def createNonSquare(){
		[
			fac.create(1, new Worker(name:"dumb"), new Job(name:"silly"))
		]
	}
	
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
	
	@Test(expected=UnsupportedOperationException.class)
	public void testNonSquare(){
		toTest = new Allocations(createNonSquare()) 
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
	public void testFindMinimumAllocationForResource(){
		Worker dumb = new Worker(name:"dumb")
		toTest = new Allocations(createValid())
		def expected = fac.create(1, new Worker(name:"dumb"),  new Job(name:"silly"))
		assertEquals expected, toTest.findMinimumAllocationFor(dumb)
	}
	
	@Test
	public void testFindMinimumAllocationForTask(){
		Task silly = new Job(name:"silly")
		toTest = new Allocations(createValid())
		def expected = fac.create(1, new Worker(name:"dumb"),  new Job(name:"silly"))
		assertEquals expected, toTest.findMinimumAllocationFor(silly)
	}
}