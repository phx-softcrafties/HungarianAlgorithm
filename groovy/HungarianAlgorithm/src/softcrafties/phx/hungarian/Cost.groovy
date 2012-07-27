package softcrafties.phx.hungarian

import softcrafties.phx.hungarian.abstractions.Allocation;
import softcrafties.phx.hungarian.abstractions.Resource;
import softcrafties.phx.hungarian.abstractions.Task;

class Cost implements Allocation {
	double price
	Worker worker
	Job job
	@Override
	public double getCost() {
		return price;
	}
	@Override
	public Resource getResource() {
		return worker;
	}
	@Override
	public Task getTask() {
		return job;
	}
	
	@Override
	public boolean equals(Object another){
		if(!(another instanceof Cost)){
			return false
		}
		def o = (Cost) another
		return o.cost == cost && o.worker.equals(worker) && o.job.equals(job)
	}
	
	@Override
	public int hashCode(){
		return price.hashCode() + worker.hashCode() + job.hashCode()
	}
}
