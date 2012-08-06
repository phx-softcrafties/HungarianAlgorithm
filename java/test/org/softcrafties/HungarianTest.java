package org.softcrafties;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class HungarianTest {

    private SituationFactory factory;

    @Before
    public void before() {
        factory = new SituationFactory();
    }
    
    @Test
    public void testFindUnassignedResources() {
        HungarianAlgorithm hungarian = factory.createWithOneMatch();
        Set<Resource> freeResources = hungarian.getUnassignedResources();
        Set<Resource> expect = new HashSet<Resource>();
        expect.add(factory.alan);
        expect.add(factory.steve);
        assertEquals(expect, freeResources);
    }

    @Test
    public void testFindUnassignedTasks() {
        HungarianAlgorithm hungarian = factory.createWithOneMatch();
        Set<Task> freeTasks = hungarian.getUnassignedTasks();
        Set<Task> expect = new HashSet<Task>();
        expect.add(factory.floors);
        expect.add(factory.windows);
        assertEquals(expect, freeTasks);
    }

    @Test
    public void testReachableTasksWithOneTightBid() {
        HungarianAlgorithm hungarian = factory.createWithOneTightBid();
        hungarian.visitFromFreeTasks();
        Set<Task> reachableTasks = hungarian.getReachableTasks();
        Set<Task> expect = new HashSet<Task>();
        expect.add(factory.bathroom);
        expect.add(factory.floors);
        expect.add(factory.windows);
        assertEquals(expect, reachableTasks);
    }
    
    @Test
    public void testReachableResourcesWithOneTightBid() {
        HungarianAlgorithm hungarian = factory.createWithOneTightBid();
        hungarian.visitFromFreeTasks();
        Set<Resource> reachableResources = hungarian.getReachableResources();
        Set<Resource> expect = new HashSet<Resource>();
        expect.add(factory.jim);
        assertEquals(expect, reachableResources);
    }
    
    @Test
    public void testReachableTasksWithOneMatch() {
        HungarianAlgorithm hungarian = factory.createWithOneMatch();
        hungarian.visitFromFreeTasks();
        Set<Task> reachableTasks = hungarian.getReachableTasks();
        Set<Task> expect = new HashSet<Task>();
        expect.add(factory.floors);
        expect.add(factory.windows);
        assertEquals(expect, reachableTasks);
    }
    
    @Test
    public void testReachableResourcesWithOneMatch() {
        HungarianAlgorithm hungarian = factory.createWithOneMatch();
        hungarian.visitFromFreeTasks();
        Set<Resource> reachableResources = hungarian.getReachableResources();
        Set<Resource> expect = new HashSet<Resource>();
        assertEquals(expect, reachableResources);
    }
    
    @Test
    public void testForAlternatingPathWithOneTightBid() {
        HungarianAlgorithm hungarian = factory.createWithOneTightBid();
        hungarian.visitFromFreeTasks();
        Resource actual = hungarian.findExtendablePath();
        assertEquals(factory.jim, actual);
    }
    
    @Test
    public void testAlternatePath() {
        HungarianAlgorithm hungarian = factory.createWithOneTightBid();
        hungarian.alternatePath(factory.jim);
        Set<Bid> matchedBids = hungarian.getMatchedBids();
        Set<Bid> expect = new HashSet<Bid>();
        expect.add(factory.jimBathroomBid);
        assertEquals(expect, matchedBids);
    }
    
    @Test
    public void testTaskPotentialAfterUpdateFromIntialState() {
        HungarianAlgorithm hungarian = factory.createInitialState();
        hungarian.visitFromFreeTasks();
        hungarian.updatePotential();
        Map<Task, Double> taskPotential = hungarian.getTaskPotential();
        Map<Task, Double> expect = new HashMap<Task, Double>();
        expect.put(factory.bathroom, 1.0);
        expect.put(factory.floors, 1.0);
        expect.put(factory.windows, 1.0);
        assertEquals(expect, taskPotential);
    }

    @Test
    public void testMinimumCostFromInitialState() {
        HungarianAlgorithm hungarian = factory.createInitialState();
        hungarian.solve();
        double cost = hungarian.getTotalCost();
        assertEquals(6.0, cost, 1e-14);
    }
    
    @Test
    public void testAssignmentFromInitialState() {
        HungarianAlgorithm hungarian = factory.createInitialState();
        hungarian.solve();
        Set<Bid> solution = hungarian.getMatchedBids();
        Set<Bid> expect = new HashSet<Bid>();
        expect.add(factory.jimBathroomBid);
        expect.add(factory.steveFloorsBid);
        expect.add(factory.alanWindowsBid); 
        assertEquals(expect, solution);
    }

    @Test
    public void testMinimumCostFromToughProblem() {
        HungarianAlgorithm hungarian = factory.createToughProblem();
        hungarian.solve();
        double cost = hungarian.getTotalCost();
        assertEquals(10.0, cost, 1e-14);
    }

    @Test
    public void testAssignmentFromToughProblem() {
        HungarianAlgorithm hungarian = factory.createToughProblem();
        hungarian.solve();
        Set<Bid> solution = hungarian.getMatchedBids();
        Set<Bid> expect = new HashSet<Bid>();
        expect.add(factory.jimFloorsBid);
        expect.add(factory.steveBathroomBid);
        expect.add(factory.alanWindowsBid); 
        assertEquals(expect, solution);
    }

}
