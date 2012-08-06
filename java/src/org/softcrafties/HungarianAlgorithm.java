package org.softcrafties;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HungarianAlgorithm {

    private Set<Resource> resources;
    private Set<Resource> freeResources;
    private Set<Resource> resourcesToVisit;
    private Set<Resource> reachableResources;
    private Map<Resource,Double> resourcePotential;
    private Set<Task> tasks;
    private Set<Task> freeTasks;
    private Set<Task> reachableTasks;
    private Set<Task> tasksToVisit;
    private Map<Task,Double> taskPotential;
    private Set<Bid> looseBids;
    private Set<Bid> matchedBids;
    private Set<Bid> tightBids;
    private Set<Bid> allBids;
    private double totalCost;

    public HungarianAlgorithm(Set<Resource> resources, 
            Set<Task> tasks, Set<Bid> bids) {
        initializeResources(resources);
        initializeTasks(tasks);
        initializeBids(bids);
    }

    public void solve() {
        totalCost = 0.0;
        int expectedMatches = Math.min(resources.size(), tasks.size());
        while (matchedBids.size() < expectedMatches) {
            visitFromFreeTasks();
            Resource looseEnd = findExtendablePath();
            if (looseEnd == null) {
                updatePotential();
            } else {
                alternatePath(looseEnd);
            }
        }
    }

    public void visitFromFreeTasks() {
        findTightBids();
        reachableResources.clear();
        reachableTasks.clear();
        reachableTasks.addAll(freeTasks);
        tasksToVisit.addAll(freeTasks);
        visitTasks();
    }

    private void visitTasks() {
        for (Bid bid : tightBids) {
            if (tasksToVisit.contains(bid.getTask())) {
                Resource resource = bid.getResource();
                if (!reachableResources.contains(resource)) {
                    reachableResources.add(resource);
                    resourcesToVisit.add(resource);
                }
            }
        }
        tasksToVisit.clear();
        if (!resourcesToVisit.isEmpty()) {
            visitResources();
        }
    }

    private void visitResources() {
        for (Bid bid : matchedBids) {
            if (resourcesToVisit.contains(bid.getResource())) {
                Task task = bid.getTask();
                if (!reachableTasks.contains(task)) {
                    reachableTasks.add(task);
                    tasksToVisit.add(task);
                }
            }
        }
        resourcesToVisit.clear();
        if (!tasksToVisit.isEmpty()) {
            visitTasks();
        }
    }

    public Resource findExtendablePath() {
        Resource endpoint = null;
        HashSet<Resource> intersection = new HashSet<Resource>(reachableResources);
        intersection.retainAll(freeResources);
        if (!intersection.isEmpty()) {
            endpoint = intersection.iterator().next();
        }
        return endpoint;
    }

    public void alternatePath(Resource start) {
        freeResources.remove(start);
        Bid tightBid = findTightBidFromResource(start);
        Task task = tightBid.getTask();
        Bid matchedBid = findMatchedBidFromTask(task);
        tightBids.remove(tightBid);
        matchedBids.add(tightBid);
        while (matchedBid != null) {
            Resource resource = matchedBid.getResource();
            tightBid = findTightBidFromResource(resource);
            matchedBids.remove(matchedBid);
            tightBids.add(matchedBid);
            task = tightBid.getTask();
            matchedBid = findMatchedBidFromTask(task);
            tightBids.remove(tightBid);
            matchedBids.add(tightBid);
        }
        freeTasks.remove(task);
    }

    public void updatePotential() {
        double delta = allBids.iterator().next().getCost();
        for (Bid bid : allBids) {
            Resource resource = bid.getResource();
            Task task = bid.getTask();
            if (!reachableResources.contains(resource)
                    && reachableTasks.contains(task)) {
                double netCost = bid.getCost() 
                        - resourcePotential.get(resource) 
                        - taskPotential.get(task);
                delta = (netCost < delta) ? netCost : delta;
            }
        }
        for (Task task : reachableTasks) {
            increaseTaskPotential(task, delta);
        }
        for (Resource resource : reachableResources) {
            increaseResourcePotential(resource, -delta);
        }
        findTightBids();
    }

    public void increaseTaskPotential(Task task, double delta) {
        totalCost += delta;
        double value = taskPotential.get(task) + delta;
        taskPotential.put(task, value);
    }

    public void increaseResourcePotential(Resource resource, double delta) {
        totalCost += delta;
        double value = resourcePotential.get(resource) + delta;
        resourcePotential.put(resource, value);
    }

    public void findTightBids() {
        Iterator<Bid> iterator = looseBids.iterator();
        while (iterator.hasNext()) {
            Bid bid = iterator.next();
            if (bidIsTight(bid)) {
                tightBids.add(bid);
                iterator.remove();
            }
        }
    }

    public boolean bidIsTight(Bid bid) {
        double difference = bid.getCost()
                - resourcePotential.get(bid.getResource())
                - taskPotential.get(bid.getTask());
        return Math.abs(difference) < 1e-14;
    }

    private Bid findTightBidFromResource(Resource resource) {
        Bid found = null;
        Iterator<Bid> iterator = tightBids.iterator();
        while (found==null && iterator.hasNext()) {
            Bid bid = iterator.next();
            if (bid.getResource() == resource) {
                found = bid;
            }
        }
        return found;
    }

    private Bid findMatchedBidFromTask(Task task) {
        Bid found = null;
        Iterator<Bid> iterator = matchedBids.iterator();
        while (found==null && iterator.hasNext()) {
            Bid bid = iterator.next();
            if (bid.getTask() == task) {
                found = bid;
            }
        }
        return found;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public Set<Resource> getUnassignedResources() {
        return freeResources;
    }

    public Set<Task> getUnassignedTasks() {
        return freeTasks;
    }

    public Set<Task> getReachableTasks() {
        return reachableTasks;
    }

    public Set<Resource> getReachableResources() {
        return reachableResources;
    }

    public Set<Bid> getMatchedBids() {
        return matchedBids;
    }

    public Map<Task, Double> getTaskPotential() {
        return taskPotential;
    }

    public void initializeResources(Set<Resource> resources) {
        this.resources = resources;
        freeResources = new HashSet<Resource>(resources);
        reachableResources = new HashSet<Resource>(resources.size());
        resourcesToVisit = new HashSet<Resource>(resources.size());
        resourcePotential = new HashMap<Resource, Double>(resources.size());
        for (Resource resource : resources) {
            resourcePotential.put(resource, 0.0);
        }
    }

    public void initializeTasks(Set<Task> tasks) {
        this.tasks = tasks;
        freeTasks = new HashSet<Task>(tasks);
        reachableTasks = new HashSet<Task>(tasks.size());
        tasksToVisit = new HashSet<Task>(tasks.size());
        taskPotential = new HashMap<Task, Double>(tasks.size());
        for (Task task : tasks) {
            taskPotential.put(task, 0.0);
        }
    }

    public void initializeBids(Set<Bid> bids) {
        allBids = new HashSet<Bid>(bids);
        looseBids = new HashSet<Bid>(bids);
        matchedBids = new HashSet<Bid>(bids.size());
        tightBids = new HashSet<Bid>(bids.size());
        findTightBids();
    }

    public void match(Bid bid) {
        if (matchedBids.contains(bid)) {
            return;
        }
        if (!looseBids.contains(bid) && !tightBids.contains(bid)) {
            throw new AssertionError("Unknown key: " + bid);
        }
        if (!tightBids.contains(bid)) {
            
            String message = "Cannot match loose bid: " + bid;
            message += "\n  (resource potential = " +   
                    resourcePotential.get(bid.getResource());
            message += ", task potential = " +
                    taskPotential.get(bid.getTask()) + ")";
            throw new AssertionError(message);
        }
        Resource resource = bid.getResource();
        if (!freeResources.contains(resource)) {
            throw new AssertionError("Resource already matched: " + resource);
        }
        Task task = bid.getTask();
        if (!freeTasks.contains(task)) {
            throw new AssertionError("Task already matched: " + task);
        }
        matchedBids.add(bid);
        looseBids.remove(bid);
        tightBids.remove(bid);
        freeResources.remove(resource);
        freeTasks.remove(task);
    }

    public void free(Bid bid) {
        if (looseBids.contains(bid)) {
            return;
        }
        if (!matchedBids.contains(bid)) {
            throw new AssertionError("Unknown key: " + bid);
        }
        Resource resource = bid.getResource();
        Task task = bid.getTask();
        tightBids.add(bid);
        matchedBids.remove(bid);
        freeResources.add(resource);
        freeTasks.add(task);
    }
}
