package softcrafties.phx.hungarian;

import static org.junit.Assert.*;
import org.junit.Test;
import softcrafties.phx.hungarian.algorithm.Allocations
import softcrafties.phx.hungarian.algorithm.Assigner

class AssignerTests {
	Allocations toAssign
	
	@Test
	public void testCanFullyAssignSuccessfully(){
		def fac = new CostFactory()
		def allocs = [ 
						fac.create(0, new Worker(name:"Jim"), new Job(name:"Running")),
						fac.create(1, new Worker(name:"Jim"), new Job(name:"Eating")),
						fac.create(1, new Worker(name:"Bob"), new Job(name:"Running")),
						fac.create(0, new Worker(name:"Bob"), new Job(name:"Eating")),
					 ]
		toAssign = new Allocations(allocs)
		Assigner ass = new Assigner(toAssign)
		assertTrue ass.canFullyAssign()		
	}
	
	@Test
	public void testCannotFullyAssignSuccessfullyWhenTwoResourcesHaveEqualCost(){
		def fac = new CostFactory()
		def allocs = [
						fac.create(0, new Worker(name:"Jim"), new Job(name:"Running")),
						fac.create(1, new Worker(name:"Jim"), new Job(name:"Eating")),
						fac.create(0, new Worker(name:"Bob"), new Job(name:"Running")),
						fac.create(1, new Worker(name:"Bob"), new Job(name:"Eating")),
					 ]
		toAssign = new Allocations(allocs)
		Assigner ass = new Assigner(toAssign)
		assertFalse ass.canFullyAssign()
	}
}
