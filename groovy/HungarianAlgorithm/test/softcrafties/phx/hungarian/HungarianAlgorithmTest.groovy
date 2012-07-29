package softcrafties.phx.hungarian;

import static org.junit.Assert.*;
import org.junit.Test;

import softcrafties.phx.hungarian.abstractions.Allocation;
import softcrafties.phx.hungarian.abstractions.Resource;
import softcrafties.phx.hungarian.abstractions.Task;
import softcrafties.phx.hungarian.algorithm.Allocations
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

	@Test
	public void testStep1(){
		def matrix = createFromWikipedia()
		def costs = new Allocations(matrix.costs)
		def fac = new CostFactory()
		def results = HungarianAlgorithm.step1(costs, fac)
		def expected = [:]
		expected[matrix.jim] = [
			fac.create(0, matrix.jim, matrix.bathroom),
			fac.create(1, matrix.jim, matrix.floors),
			fac.create(2, matrix.jim, matrix.windows) ]
		expected[matrix.steve] = [ 
			fac.create(0, matrix.steve, matrix.bathroom),
			fac.create(0, matrix.steve, matrix.floors),
			fac.create(0, matrix.steve, matrix.windows) ]
		expected[matrix.alan] = [ 
			fac.create(1, matrix.alan,  matrix.bathroom),
			fac.create(1, matrix.alan,  matrix.floors),
			fac.create(0, matrix.alan,  matrix.windows) ]
		assertEquals expected, results.getMatrix()
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