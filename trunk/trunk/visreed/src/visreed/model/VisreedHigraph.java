/**
 * VisreedHigraph.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package visreed.model;

import visreed.model.payload.VisreedPayload;
import visreed.pattern.IObservable;
import higraph.model.abstractClasses.AbstractHigraph;

/**
 * @author Xiaoyu Guo
 */
public interface VisreedHigraph
extends
AbstractHigraph <VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> , 
IObservable <VisreedHigraph> {
}
