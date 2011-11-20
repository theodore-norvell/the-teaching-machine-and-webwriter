/**
 * VisreedHigraph.java
 * 
 * @date: 2011-5-25
 * @author: Xiaoyu Guo (MUN# 200982155)
 * This file was prepared by Xiaoyu Guo. It was completed by me alone.
 */
package visreed.model;

import higraph.model.abstractTaggedClasses.AbstractTaggedHigraph;
import visreed.pattern.IObservable;

/**
 * @author Xiaoyu Guo
 */
public interface VisreedHigraph
extends
AbstractTaggedHigraph <VisreedTag, VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> , 
IObservable <VisreedHigraph> {
}
