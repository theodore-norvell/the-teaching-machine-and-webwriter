//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package tm.clc.ast;

import tm.interfaces.SourceCoords;
import tm.languageInterface.ExpressionInterface;

public abstract class ExpressionNode extends Node
    implements ExpressionInterface, TypedNodeInterface {

    private TypeNode type ;
    private boolean has_constant_value = false ;
    private long constant_value ;
    private boolean has_floating_value = false ;
    private double floating_value;
    private boolean uninteresting = false ;


    public ExpressionNode(String name, NodeList children) {
        super(name, children) ; }

    public ExpressionNode(String name) {
        super(name) ; }

    public ExpressionNode(String name, Node a ) {
        super(name, a) ; }

    public ExpressionNode(String name, Node a, Node b ) {
        super(name, a, b) ; }

    public ExpressionNode(String name, Node a, Node b, Node c ) {
        super(name, a, b, c) ; }

    public ExpressionNode(String name, Node a, Node b, Node c, Node d ) {
        super(name, a, b, c, d) ; }

    public ExpressionNode(String name, Node a, NodeList args ) {
        super(name, a, args) ; }

    protected void set_type( TypeNode type ) { this.type = type ; }

    public TypeNode get_type() { return type ; }

    public boolean is_integral_constant() { return has_constant_value ; }

    public long get_integral_constant_value() { return constant_value ; }

    public void set_integral_constant_value(long val) {
        has_constant_value = true ;
        constant_value = val ; }

    public boolean is_floating_constant() { return has_floating_value ; }

    public double get_floating_constant_value() { return floating_value ; }

    public void set_floating_constant_value(double val) {
        has_floating_value = true ;
        floating_value = val ; }

    public ExpressionNode child_exp( int i ) {
        return (ExpressionNode) child( i ) ; }


    public void setUninteresting( boolean ui ) { uninteresting = ui ; }

    public boolean isUninteresting() { return uninteresting ; }

    public boolean isStopWorthy() { return true ; }

    public SourceCoords getCoords() { return null ; }

}