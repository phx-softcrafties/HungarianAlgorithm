package softcrafties.phx.hungarian;

import static org.junit.Assert.*;
import org.junit.Test;

import softcrafties.phx.hungarian.abstractions.Allocation;
import softcrafties.phx.hungarian.abstractions.Resource;
import softcrafties.phx.hungarian.abstractions.Task;
import softcrafties.phx.hungarian.algorithm.HungarianAlgorithm;

class HungarianAlgorithmTest {

	@Test
	public void testCreateWorker(){
		Worker steve = new Worker(name: "Steve")
		assertEquals "Steve", steve.name
	}
	
	@Test
	public void testCreateJob(){
		Job bathroom = new Job(name: "Clean bathroom")
		assertEquals "Clean bathroom", bathroom.name
	}
	
	@Test
	public void testCreateCost(){
		Worker steve = new Worker(name: "Steve")
		Job bathroom = new Job(name: "Clean bathroom")
		Cost cost = new Cost(price:5, job:bathroom, worker:steve)
		assertEquals 5, cost.price, 0.001
	}
	
	@Test
	public void testGeneralization(){
		Resource steve = new Worker(name: "Steve")
		Task bathroom = new Job(name: "Clean bathroom")
		Allocation cost = new Cost(price:5, job:bathroom, worker:steve)
		assertEquals steve, cost.resource
		assertEquals bathroom, cost.task
		assertEquals 5, cost.cost, 0.001
	}
	
	@Test
	public void testComputeHungarian(){
		def matrix = createFromWikipedia()
		def results = HungarianAlgorithm.calculate(matrix.costs, new CostFactory())
		assertEquals 3, results.size
		def expected = [ 
							new Cost(cost:1, resource:matrix.jim, task:matrix.bathroom),
							new Cost(cost:3, resource:matrix.steve, task: matrix.floors),
							new Cost(cost:2, resource:matrix.alan, task:matrix.windows)
					   ]
		assertArrayEquals(expected, results.toArray())  
	}
	
	private def createFromWikipedia(){
		def result = [:]
		result.jim = new Worker(name:"Jim")
		result.steve = new Worker(name:"Steve")
		result.alan =new Worker(name:"Alan")
		result.bathroom = new Job(name:"Clean bathroom")
		result.floors = new Job(name:"Sweep floors")
		result.windows = new Job(name:"Wash Windows")
		def fac = new CostFactory()
		result.costs = [
						   fac.create(1, result.jim,   result.bathroom),
						   fac.create(2, result.jim,   result.floors),
						   fac.create(3, result.jim,   result.windows),
						   fac.create(3, result.steve, result.bathroom),
						   fac.create(3, result.steve, result.floors),
						   fac.create(3, result.steve, result.windows),
						   fac.create(3, result.alan,  result.bathroom),
						   fac.create(3, result.alan,  result.floors),
						   fac.create(2, result.alan,  result.windows),
						]
		result
	}
}