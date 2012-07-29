package softcrafties.phx.hungarian.algorithm

import softcrafties.phx.hungarian.abstractions.Allocation;
import softcrafties.phx.hungarian.abstractions.AllocationFactory

class HungarianAlgorithm {

	public static def calculate(Collection<Allocation> costs, AllocationFactory fac){
		def allocations = new Allocations(costs)
		step4(step3(step2(step1(allocations, fac))))
	}
	
	public static def step1(Allocations costs, AllocationFactory fac){
		def zeroed = subtractMinimumFromRows(costs, fac)
		// assign(new Allocations(zeroed))
		new Allocations(zeroed)
	}
	
	static def subtractMinimumFromRows(Allocations costs, AllocationFactory fac){
		def minimized = []
		costs.matrix.each { it ->
			def minRes = costs.findMinimumAllocationFor(it.key)
			it.value.each{ Allocation al ->
				minimized += fac.create(al.cost - minRes.cost, al.resource, al.task)
			}
		}
		minimized
	}
	
	static def assign(Allocation costs){
		Assigner ass = new Assigner(costs)
		if(ass.canFullyAssign()){
			return ass.assignments
		}
		println "costs is ${costs}"
	}
	
	public static def step2(Allocations costs){
		
	}
	
	public static def step3(Allocations costs){
		[]
	}
	
	public static def step4(Allocations costs){
		[]
	}
}
