package softcrafties.phx.hungarian.abstractions;

public abstract class AllocationFactory {
	public abstract Allocation create(double cost, Resource resource, Task task);
}
