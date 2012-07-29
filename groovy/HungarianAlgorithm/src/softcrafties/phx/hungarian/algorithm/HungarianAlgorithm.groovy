package softcrafties.phx.hungarian.algorithm

import softcrafties.phx.hungarian.abstractions.Allocation;
import softcrafties.phx.hungarian.abstractions.AllocationFactory

class HungarianAlgorithm {

	public static def calculate(Collection<Allocation> costs, AllocationFactory fac){
		def allocations = new Allocations(costs)
		step4(step3(step2(step1(allocations, fac))))
	}
	
	private static def step1(Allocations costs, AllocationFactory fac){
		def zeroed = []
		costs.each { 
			def minRes = costs.findMinimumAllocationFor(it.key)
			it.value.each{ Allocation al ->
				zeroed += ( al == minRes ) ? fac.create(0, al.resource, al.task) : al 
			}
		}
		new Allocations(zeroed)
	}
	
	private static def step2(Allocations costs){
		Assigner ass = new Assigner(costs)
		if(ass.canFullyAssign()){
			return ass.assignments
		}
		println "costs is ${costs}"
	}
	
	private static def step3(Allocations costs){
		[]
	}
	
	private static def step4(Allocations costs){
		[]
	}
}
