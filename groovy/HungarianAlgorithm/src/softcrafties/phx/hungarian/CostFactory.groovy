package softcrafties.phx.hungarian

import softcrafties.phx.hungarian.abstractions.Allocation;
import softcrafties.phx.hungarian.abstractions.AllocationFactory
import softcrafties.phx.hungarian.abstractions.Resource;
import softcrafties.phx.hungarian.abstractions.Task;

class CostFactory extends AllocationFactory {

	@Override
	public Allocation create(double cost, Resource resource, Task task) {
		new Cost(price:cost, worker:resource, job:task)
	}
}
