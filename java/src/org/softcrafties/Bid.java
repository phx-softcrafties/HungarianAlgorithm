package org.softcrafties;


public class Bid {

    private Resource resource;
    private Task task;
    private double cost;

    public Bid(Resource resource, Task task, double cost) {
        this.resource = resource;
        this.task = task;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return resource.toString() + " will do "
                + task.toString() + " for " 
                + cost;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Bid bid = (Bid) obj;
        return resource == bid.resource
                && task == bid.task
                && cost == bid.cost;
    }
   
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((resource == null) ? 0 : resource.hashCode());
        result = prime * result
                + ((task == null) ? 0 : task.hashCode());
        result = prime * result + new Double(cost).hashCode();
        return result;
    }

    public Resource getResource() {
        return resource;
    }

    public Task getTask() {
        return task;
    }
    

    public Double getCost() {
        return cost;
    }

    public void setCost(double value) {
        cost = value;
    }

}
