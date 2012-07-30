package softcrafties.phx.hungarian.algorithm

import softcrafties.phx.hungarian.abstractions.Allocation;
import softcrafties.phx.hungarian.abstractions.AllocationFactory

class HungarianAlgorithm {
	public static def calculate(Collection<Allocation> costs, AllocationFactory fac){
		def original = new Allocations(costs)
		def allocations = new Allocations(costs)
		def assignments = [].toSet()
		if(!Assigner.assign(allocations, assignments))
		{
			allocations = step1(allocations, fac)
		}
		Assigner.assign(allocations, assignments)
		def result = mapToOriginal(original, assignments)
		result
	}
	
	public static Collection<Allocation> mapToOriginal(Allocations original, Set<Allocation> assignments){
		def answer =[].toSet()
		assignments.each{
			def found = original.find{ Allocation al ->
				al.resource == it.resource && al.task == it.task 
			}
			answer.add(found)
		}
		answer
	}
	
	public static def step1(Allocations costs, AllocationFactory fac){
		def zeroed = subtractMinimumFromRows(costs, fac)
		if(!Assigner.assign(zeroed, [].toSet())){
			zeroed = step2(zeroed)
		}
		zeroed
	}
	
	static Allocations subtractMinimumFromRows(Allocations costs, AllocationFactory fac){
		def minimized = []
		costs.matrix.each { it ->
			def minRes = costs.findMinimumAllocationFor(it.key)
			it.value.each{ Allocation al ->
				minimized += fac.create(al.cost - minRes.cost, al.resource, al.task)
			}
		}
		minimized
	}
	
	public static Allocations step2(Allocations costs){
		costs
	}
	
	public static Allocations step3(Allocations costs){
		costs
	}
	
	public static Allocations step4(Allocations costs){
		costs
	}
}
