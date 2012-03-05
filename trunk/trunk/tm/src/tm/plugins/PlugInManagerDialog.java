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

/*
 * Created on 7-Jul-2006 by Theodore S. Norvell. 
 */
package tm.plugins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import tm.interfaces.ImageSourceInterface;
import tm.utilities.Assert;

/** The PlugInManagerDialog gives a user interface to the
 * PlugInManager. Allowing the user to inspect and modify
 * the set of entries.
 * <p>
 * The dialog also reports on potential problems with the set
 * of entries.
 * 
 * @author theo
 * @see PlugInManager
 */
public class PlugInManagerDialog extends JDialog {

    // The models
    private PlugInManager model ;
    /** treeModel is organized into 4 levels.
     * <ul> <li> The root: The user Object is a constant String
     *      <li> Level 1: The user Object is a String jack name
     *      <li> Level 2: The user Object is a PlugInRegistration
     *      <li> Level 3: The user Object is a Requirement. Note that level 3
     *              nodes are only present when the level 2 PlugInRegistration
     *              objects name a class that can be instantiated.
     * </ul>
     * 
     */
    private DefaultTreeModel treeModel ;
    private DefaultListModel problemListModel ;
    
    // The actions
    private Action addAction ;
    private Action removeAction ;
    private Action changeAction ;
    
    // The GUI containers
    private JPanel mainPanel ;
    private JPanel topPanel ;
    private JScrollPane treePane ;
    private JScrollPane problemPane ;
    private JPanel buttonPanel ;
    private AddChangeRemoveDialog addChangeRemoveDialog ;
    
    // The GUI components
    private JTree theTree ;
    private JList problemList ;
    private JButton addButton ;
    private JButton removeButton ;
    private JButton changeButton ;

    /** Create a dialog for managing plug-ins.
     * 
     * @param model
     * @throws HeadlessException
     */
    public PlugInManagerDialog(PlugInManager model, ImageSourceInterface imageSource) throws HeadlessException {
        super();
        this.setTitle("Manage Plug-ins") ;
        this.model = model ;
        
        problemListModel = new DefaultListModel() ;
        problemList = new JList( problemListModel ) ;
        
        // Build the tree.
        DefaultMutableTreeNode root = new DefaultMutableTreeNode() ;
        treeModel = new DefaultTreeModel( root ) ;
        theTree = new JTree( treeModel ) ;
        rebuildTree() ;
        theTree.setRootVisible( true ) ;
        theTree.setShowsRootHandles( true ) ;
        theTree.expandPath( new TreePath( new Object[] { root } ) ) ;
        theTree.setCellRenderer( new PlugInCellRenderer(imageSource) ) ;
        theTree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION ) ;
        theTree.getSelectionModel().addTreeSelectionListener( new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) { selectionChanged() ; }} ) ;
        
        // Create the actions
        addAction = new AddAction() ;
        addAction.setEnabled( true ) ;
        changeAction = new ChangeAction() ;
        changeAction.setEnabled( false ) ;
        removeAction = new RemoveAction() ;
        removeAction.setEnabled( false ) ;
        
        // Create the buttons
        addButton = new JButton( addAction ) ;
        addButton.setToolTipText( "Add a new plug-in registration.") ;
        changeButton = new JButton( changeAction ) ;
        addButton.setToolTipText( "Change an existing plug-in registration.") ;
        removeButton = new JButton( removeAction ) ;
        addButton.setToolTipText( "Remove a plug-in registration.") ;
        
        // Build the GUI 
        Border border = BorderFactory.createEtchedBorder() ;
        addChangeRemoveDialog = new AddChangeRemoveDialog() ;
        addChangeRemoveDialog.setBorder( border ) ;
        treePane = new JScrollPane( theTree ) ;
        problemPane = new JScrollPane( problemList ) ;
        
        buttonPanel = new JPanel( ) ;
        buttonPanel.setLayout( new BoxLayout( buttonPanel, BoxLayout.PAGE_AXIS ) ) ;
        buttonPanel.add( Box.createRigidArea( new Dimension(20, 20) ) ) ;
        buttonPanel.add( addButton ) ;
        buttonPanel.add( Box.createRigidArea( new Dimension(20, 20) ) ) ;
        buttonPanel.add( removeButton ) ;
        buttonPanel.add( Box.createRigidArea( new Dimension(20, 20) ) ) ;
        buttonPanel.add( changeButton ) ;
        buttonPanel.add( Box.createVerticalGlue() ) ;
        buttonPanel.setBorder( border ) ;
        
        topPanel = new JPanel() ;
        topPanel.setLayout( new BoxLayout( topPanel, BoxLayout.LINE_AXIS ) ) ;
        topPanel.add( treePane ) ;
        topPanel.add(buttonPanel) ;
        
        mainPanel = new JPanel() ;
        mainPanel.setLayout( new BoxLayout( mainPanel, BoxLayout.PAGE_AXIS ) ) ;
        mainPanel.add( topPanel ) ;
        mainPanel.add( problemPane ) ;
        mainPanel.setBorder( border ) ;
        
        add( mainPanel, BorderLayout.CENTER ) ;
        
        treePane.setPreferredSize( new Dimension( 300, 400 ) ) ;
        setBounds(0, 0, 700, 600 ) ;
    }
    
    private String getSelectedJackName() {
        TreeSelectionModel selectionModel = theTree.getSelectionModel() ;
        int selectionCount = selectionModel.getSelectionCount() ;
        if( selectionCount == 0 ) {
            return null ; }
        else  {
            TreePath path = selectionModel.getSelectionPath() ;
            int pathLength = path.getPathCount() ;
            if( pathLength == 3 || pathLength == 2 ) {
                DefaultMutableTreeNode nd = (DefaultMutableTreeNode) path.getPathComponent( 1 ) ;         
                return (String) nd.getUserObject() ; }
            else if( pathLength == 4 ) {
                DefaultMutableTreeNode nd = (DefaultMutableTreeNode) path.getPathComponent( 3 ) ;
                return ((Requirement)nd.getUserObject()).getJackName() ; }
            else
                return null ; }
    }
    
    private PlugInRegistration getSelectedRegistration() {
        TreeSelectionModel selectionModel = theTree.getSelectionModel() ;
        int selectionCount = selectionModel.getSelectionCount() ;
        if( selectionCount == 0 ) {
            return null ; }
        else {
            TreePath path = selectionModel.getSelectionPath() ;
            int pathLength = path.getPathCount() ;
            if( pathLength == 3 ) {
                DefaultMutableTreeNode nd = (DefaultMutableTreeNode) path.getPathComponent( 2 ) ;         
                return (PlugInRegistration) nd.getUserObject() ; }
            else {
                return null ; } }
    }
    
    /** When the selection on the tree changes the set of enabled buttons
     * will change.
     */
    private void selectionChanged() {
        PlugInRegistration registration = getSelectedRegistration() ;
        if( registration == null ) {
            addAction.setEnabled( true ) ;
            removeAction.setEnabled( false ) ;
            changeAction.setEnabled( false ) ; }
        else {
            addAction.setEnabled( true ) ;
            removeAction.setEnabled( true ) ;
            changeAction.setEnabled( true ) ; }    }

    /** Return the tree (for testing purposes) */
    JTree theTree() { return theTree ; }
    
    /** Rebuild the tree.
     * <p>When the model changes, the tree should be rebuilt
     * and the model should be inspected for problems.
     */
    void rebuildTree() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot() ;
        root.setUserObject( "Jacks and plugins:") ;
        
        //0. We need a list of all jackNames and a mapping from names to registrations
        SortedSet <String> jackNames= new TreeSet<String>() ;
        Map<String, List<PlugInRegistration> > map = new HashMap<String, List<PlugInRegistration> >() ;
        for( PlugInRegistration r : model ) {
            String jackName = r.getJackName() ;
            if( ! jackNames.contains( jackName ) ) {
                map.put( jackName, new ArrayList<PlugInRegistration>() ) ;
                jackNames.add( jackName ) ; }
            map.get( jackName ).add( r ) ;
            
            if( r.isActive() ) {
                try {
                    PlugInFactory f = r.createFactoryObject() ;
                    Requirement[] requirements = f.getRequirements() ;
                    if( requirements == null ) requirements = new Requirement[0] ;
                    for( Requirement req : requirements ) {
                        String jackName1 = req.getJackName() ;
                        if( ! jackNames.contains( jackName1 ) ) {
                            map.put( jackName1, new ArrayList<PlugInRegistration>() ) ;
                            jackNames.add( jackName1 ) ; } } }
                catch( PlugInNotFound ex ) {} }
        }
        String[] jackNameArray = jackNames.toArray( new String[0] ) ;
        
        //1. Loop through the children of the root and check that they match the jackNames
        int childIndex = 0 ;
        
        while( true ) {
            boolean treeDone = childIndex == root.getChildCount() ;
            boolean arrayDone = childIndex == jackNameArray.length ;
            
            if( treeDone && arrayDone ) break ;
            
            DefaultMutableTreeNode nd = treeDone ? null : (DefaultMutableTreeNode) root.getChildAt( childIndex ) ;
            String childLabel = treeDone ? null : (String) nd.getUserObject() ;
            String jackName = arrayDone ? null : jackNameArray[childIndex] ;
            
            if( treeDone
             || !arrayDone && childLabel.compareTo( jackName ) > 0 ) {
                // Must insert a new node in the tree
                addFirstLevelChild( childIndex, jackName, map.get( jackName ) ) ;
                childIndex += 1 ; }
            else if( arrayDone
             || !treeDone && childLabel.compareTo( jackName ) < 0 ) {
                // Must delete a node
                treeModel.removeNodeFromParent(nd) ; }
            else {
                Assert.check( !arrayDone && !treeDone && childLabel.compareTo( jackName ) == 0 ) ;
                rebuildFirstLevelNode( nd, map.get( jackName ) ) ;
                childIndex += 1 ; } }
        
        //2. Clear the problem list
        problemListModel.clear() ;
        
        //3. Now check for problems.
        for( PlugInRegistration r : model ) {
            if( ! r.isActive() ) continue ;
            PlugInFactory factory ;
            try {
                factory = r.createFactoryObject() ;
                // Now check all the Jacks required by this factory object.
                Requirement[] requirements = factory.getRequirements() ;
                if( requirements == null ) requirements = new Requirement[0] ;
                for( Requirement req : requirements ) {
                     String jackName = req.getJackName() ;
                     Class<? extends PlugInFactory> interfaceRequired = req.getInterfaceRequired() ;
                     boolean isMultple = req.isMultiple() ;
                     boolean isMandatory = req.isMandatory() ;
                     List<PlugInRegistration> lpr = map.get( jackName ) ;
                     int activeCount = 0 ;
                     for( PlugInRegistration pir : lpr ) { if( pir.isActive() ) activeCount += 1 ; }
                     if( isMandatory && activeCount < 1 ) {
                         problemListModel.addElement( "Factory class '"
                                 + r.getClassName() 
                                 + "' requires at least 1 active plug-in in jack '"
                                 + jackName + "'." ) ; }
                     if( !isMultple && activeCount > 1 ) {
                         problemListModel.addElement( "Factory class '"
                                 + r.getClassName() 
                                 + "' requires less than 2 active plug-ins in jack '"
                                 + jackName + "'." ) ; }
                     // Check that each active plug-in implements the right type.
                     for( PlugInRegistration pirForJack : lpr ) {
                         if( ! pirForJack.isActive() ) continue ;
                         try {
                             PlugInFactory f = pirForJack.createFactoryObject() ;
                             if( ! interfaceRequired.isAssignableFrom( f.getClass()) ) {
                                 problemListModel.addElement( "Factory class '"
                                         + r.getClassName() 
                                         + "' requires that '"
                                         + f.getClass().getName()
                                         + "' in jack '"
                                         + jackName
                                         + "' be assignable to '"
                                         + interfaceRequired.getName() +"'." ) ; }
                         }
                         catch( PlugInNotFound ex ) {/*Dealt with elsewhere*/}
                     }
                }
            }
            catch( PlugInNotFound ex ) {
                String msg = ex.getMessage() ;
                if( msg == null ) msg = "Problem building factory '"+r.getClassName()+"'." ;
                problemListModel.addElement( msg ) ; }
        }
        
        if( problemListModel.size() == 0 )
            problemListModel.add(0, "No problems found.") ;
        else
            problemListModel.add(0, "Problems:" ) ;
    }

    private void addFirstLevelChild(int childIndex, String jackName, List<PlugInRegistration> registrations) {
       
        
        // 1 Add a node under the root.
        DefaultMutableTreeNode nd = new DefaultMutableTreeNode( jackName ) ;
        MutableTreeNode root = (MutableTreeNode) treeModel.getRoot() ;
        treeModel.insertNodeInto(nd, root, childIndex) ;
        
        // 2 Ensure that its children are right
        rebuildFirstLevelNode( nd, registrations ) ;
    }

    private void rebuildFirstLevelNode(MutableTreeNode nd, List<PlugInRegistration> registrations) {
        // 0 Sort the list
        Collections.sort( registrations ) ;
        
        int childIndex = 0 ;
        while( true ) {
            boolean treeDone = childIndex == nd.getChildCount() ;
            boolean arrayDone = childIndex == registrations.size() ;
            
            if( treeDone && arrayDone ) break ;
            
            PlugInRegistration r = arrayDone ? null : registrations.get(childIndex) ;
            DefaultMutableTreeNode child = treeDone ? null : (DefaultMutableTreeNode) nd.getChildAt( childIndex ) ;
            PlugInRegistration childRegistration = treeDone ? null : (PlugInRegistration) child.getUserObject() ;
            
            if( treeDone || !arrayDone && childRegistration.compareTo( r ) > 0 ) {
                addSecondLevelNode(nd, childIndex, r ) ;
                childIndex += 1 ; }
            else if( arrayDone || !treeDone && childRegistration.compareTo( r ) < 0 ) {
                treeModel.removeNodeFromParent( child ) ; }
            else {
                Assert.check( !treeDone && !arrayDone && childRegistration.compareTo( r ) == 0 );
                rebuildSecondLevelNode( child ) ;
                childIndex += 1 ; } }
    }

    private void addSecondLevelNode(MutableTreeNode nd, int i, PlugInRegistration r) {
        // 0 Insert a new second level node at position i.
        DefaultMutableTreeNode child = new DefaultMutableTreeNode( r ) ;
        treeModel.insertNodeInto( child, nd, i ) ;
        
        // Add children to it
        rebuildSecondLevelNode( child ) ;
    }

    private void rebuildSecondLevelNode(DefaultMutableTreeNode child) {
        PlugInRegistration r = (PlugInRegistration) child.getUserObject() ;
        
        while( child.getChildCount() != 0 ) {
            DefaultMutableTreeNode grandChild = (DefaultMutableTreeNode) child.getChildAt(0) ;
            treeModel.removeNodeFromParent( grandChild ) ; }
        
        PlugInFactory pif = null ;
        if( r.isActive() ) {
            try {
                pif = r.createFactoryObject() ; }
            catch( Throwable th ) { } }
        
        if( pif != null ) {
            Requirement[] requirements = pif.getRequirements() ;
            if( requirements == null ) requirements = new Requirement[0] ;
            int i = 0 ;
            for( Requirement requirement : requirements ) {
                DefaultMutableTreeNode grandChild = new DefaultMutableTreeNode(requirement ) ;
                treeModel.insertNodeInto( grandChild, child, i ) ;
                i += 1 ; }
        }
    }
    
    private void showAddChangeRemoveDialog() {
        topPanel.remove( buttonPanel ) ;
        topPanel.add( addChangeRemoveDialog ) ;
        mainPanel.validate() ;
    }
    
    private void hideAddChangeRemoveDialog() {
        topPanel.remove( addChangeRemoveDialog ) ;
        topPanel.add( buttonPanel ) ;
        mainPanel.validate() ;
    }
    
    private void showAddDialog() {
        addChangeRemoveDialog.configureForAdd(getSelectedJackName()) ;
        showAddChangeRemoveDialog() ;
    }
    
    private void showChangeDialog() {
        PlugInRegistration reg = getSelectedRegistration() ;
        if( reg == null ) return ;
        addChangeRemoveDialog.configureForChange(reg) ;
        showAddChangeRemoveDialog() ;
    }
    
    private void showRemoveDialog() {
        PlugInRegistration reg = getSelectedRegistration() ;
        if( reg == null ) return ;
        addChangeRemoveDialog.configureForRemove(reg) ;
        showAddChangeRemoveDialog() ;
    }
    
    private void completeAddRegistration(String jackName, String className, String parameter, boolean isActive ) {
        PlugInRegistration reg = new PlugInRegistration(jackName, className, parameter, isActive ) ;
        model.registerPlugIn( reg ) ;
        rebuildTree() ;
        hideAddChangeRemoveDialog() ;
    }
    
    private void completeChangeRegistration(PlugInRegistration reg, String jackName, String className, String parameter, boolean isActive ) {
        model.deRegisterPlugIn( reg ) ;
        completeAddRegistration( jackName, className, parameter, isActive ) ;
    }
    
    private void completeRemoveRegistration(PlugInRegistration reg ) {
        model.deRegisterPlugIn( reg ) ;
        rebuildTree() ;
        hideAddChangeRemoveDialog() ;
    }
    
    private class AddAction extends AbstractAction {

        AddAction() {  super("Add") ;  }
        
        public void actionPerformed(ActionEvent e) {
            showAddDialog() ;
        }
    }
    
    private class RemoveAction extends AbstractAction {

        RemoveAction() { super("Remove" ) ; }
        
        public void actionPerformed(ActionEvent e) {
            showRemoveDialog() ;
        }
    }
    
    private class ChangeAction extends AbstractAction {

        ChangeAction() { super("Change" ) ; }

        public void actionPerformed(ActionEvent e) {
            showChangeDialog() ;
        }
    }
    
    private class AddChangeRemoveDialog extends JPanel {
        
        JTextField jackNameField = new JTextField(15) ;
        JTextField classNameField = new JTextField(15) ;
        JTextField parameterField = new JTextField(15) ;
        JCheckBox isActiveCheckBox = new JCheckBox() ;
        
        JButton actionButton = new JButton() ;
        JButton cancelButton = new JButton() ;
        
        AddChangeRemoveDialog() {
            JLabel jackNameLabel = new JLabel( "Jack name:" ) ;
            JLabel classNameLabel = new JLabel( "Name of plug-in factory class:" ) ;
            JLabel parameterLabel = new JLabel( "Optional string parameter:" ) ;
            JLabel isActiveLabel = new JLabel( "Registration is active:" ) ;
            
            GridBagLayout gbl = new GridBagLayout() ;
            GridBagConstraints gblc = new GridBagConstraints();
            JPanel fieldPanel = new JPanel( gbl ) ;
            gblc.fill = GridBagConstraints.NONE ;
            gblc.insets = new Insets(5, 5, 5, 5) ;
            
            gblc.anchor = GridBagConstraints.EAST ;
            gblc.gridx = 0 ; gblc.gridy = 0 ;
            fieldPanel.add( jackNameLabel, gblc ) ;
            gblc.gridx = 0 ; gblc.gridy = 1 ;
            fieldPanel.add( classNameLabel, gblc ) ;
            gblc.gridx = 0 ; gblc.gridy = 2 ;
            fieldPanel.add( parameterLabel, gblc ) ;
            gblc.gridx = 0 ; gblc.gridy = 3 ;
            fieldPanel.add( isActiveLabel, gblc ) ;
            
            gblc.anchor = GridBagConstraints.WEST ;
            gblc.gridx = 1 ; gblc.gridy = 0 ;
            fieldPanel.add( jackNameField, gblc ) ;
            gblc.gridx = 1 ; gblc.gridy = 1 ;
            fieldPanel.add( classNameField, gblc ) ;
            gblc.gridx = 1 ; gblc.gridy = 2 ;
            fieldPanel.add( parameterField, gblc ) ;
            gblc.gridx = 1 ; gblc.gridy = 3 ;
            fieldPanel.add( isActiveCheckBox, gblc ) ;
            
            JPanel buttonPanel = new JPanel() ;
            buttonPanel.setLayout( new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS ) ) ;
            buttonPanel.add( actionButton ) ;
            buttonPanel.add( Box.createRigidArea( new Dimension(20, 1) ) ) ;
            buttonPanel.add( cancelButton ) ;
            buttonPanel.add( Box.createRigidArea( new Dimension(20, 1) ) ) ;
            
            this.setLayout( new BoxLayout( this, BoxLayout.PAGE_AXIS ) ) ;
            //this.add( Box.createVerticalGlue() ) ;
            this.add( fieldPanel ) ;
            this.add( buttonPanel ) ;
            //this.add( Box.createVerticalGlue() ) ;
            
            cancelButton.setAction( new AbstractAction("Cancel") {
                public void actionPerformed(ActionEvent e) {
                    hideAddChangeRemoveDialog() ; } } ) ;
        }
        
        void configureForAdd(String optJackName ) {
            actionButton.setAction( new AbstractAction("Confirm Adding This Registation") {
                public void actionPerformed(ActionEvent e) {
                    String jackName = jackNameField.getText() ;
                    String className = classNameField.getText() ;
                    String parameter = parameterField.getText() ;
                    boolean isActive = isActiveCheckBox.isSelected() ;
                    completeAddRegistration(jackName, className, parameter, isActive ) ; } } ) ;
            jackNameField.setText(optJackName==null ? "" : optJackName ) ;
            jackNameField.setEditable( true ) ;
            classNameField.setText("") ;
            classNameField.setEditable( true ) ;
            parameterField.setText("") ;
            parameterField.setEditable( true ) ;
            isActiveCheckBox.setSelected( true ) ;
            isActiveCheckBox.setEnabled( true ) ;
            validate() ;
        }
        
        void configureForChange( final PlugInRegistration registration ) {
            actionButton.setAction( new AbstractAction("Confirm Changes To Registation") {
                public void actionPerformed(ActionEvent e) {
                    String jackName = jackNameField.getText() ;
                    String className = classNameField.getText() ;
                    String parameter = parameterField.getText() ;
                    boolean isActive = isActiveCheckBox.isSelected() ;
                    completeChangeRegistration(registration, jackName, className, parameter, isActive ) ; } } ) ;
            jackNameField.setText( registration.getJackName() ) ;
            jackNameField.setEditable( true ) ;
            classNameField.setText( registration.getClassName() ) ;
            classNameField.setEditable( true ) ;
            parameterField.setText( registration.getParameter() ) ;
            parameterField.setEditable( true ) ;
            isActiveCheckBox.setSelected( registration.isActive() ) ;
            isActiveCheckBox.setEnabled( true ) ;
            validate() ;
        }
        
        void configureForRemove( final PlugInRegistration registration ) {
            actionButton.setAction( new AbstractAction("Confirm Removal Of Registation") {
                public void actionPerformed(ActionEvent e) {
                    completeRemoveRegistration(registration ) ; } } ) ;
            jackNameField.setText( registration.getJackName() ) ;
            jackNameField.setEditable( false ) ;
            classNameField.setText( registration.getClassName() ) ;
            classNameField.setEditable( false ) ;
            parameterField.setText( registration.getParameter() ) ;
            parameterField.setEditable( false ) ;
            isActiveCheckBox.setSelected( registration.isActive() ) ;
            isActiveCheckBox.setEnabled( false ) ;
            validate() ;
        }

    }

    class PlugInCellRenderer implements TreeCellRenderer {
        
        Icon plugIcon ;
        Icon unpluggedIcon ;
        Icon outletIcon  ;
        
        PlugInCellRenderer( ImageSourceInterface imageSource ) {     
            Image plugImage = imageSource.fetchImage("plugins/plug.gif") ;
            Image unpluggedImage = imageSource.fetchImage("plugins/unplugged.gif") ;
            Image outletImage = imageSource.fetchImage("plugins/outlet.gif") ;
            plugIcon = plugImage==null ? null : new ImageIcon( plugImage ) ;
            unpluggedIcon = unpluggedImage==null ? null : new ImageIcon( unpluggedImage ) ;
            outletIcon = outletImage==null ? null : new ImageIcon( outletImage ) ;
        }
        
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            String str = null ;
            Icon icon = null ;
            DefaultMutableTreeNode nd = (DefaultMutableTreeNode) value ;
            Object userObject = nd.getUserObject() ;
            int level = nd.getLevel() ;
            boolean active = true ;
            switch( level ) {
            case 0 : {
                str = "Jacks:" ;
            } break ;
            case 1 : {
                str = userObject.toString() ;
                // Report the number of active registrations.
                int activeCount = 0 ;
                for( PlugInRegistration pir : PlugInManagerDialog.this.model ) {
                    if( pir.getJackName().equals( userObject ) )
                        activeCount += pir.isActive() ? 1 : 0 ; }
                str += " ("+activeCount+" active " + (activeCount==1? "factory" :"factories") +")" ;
                icon = outletIcon ;
            } break ;
            case 2 :{
                Assert.check( userObject instanceof PlugInRegistration ) ;
                PlugInRegistration reg = (PlugInRegistration) userObject ;
                str =  reg.getClassName() + "(\"" + reg.getParameter() + "\")" ;
                if( reg.isActive() ) {
                    icon = plugIcon ; }
                else {
                    icon = unpluggedIcon ;
                    active = false ; }
                if( expanded ) str += " Requirements:" ;
            } break ;
            case 3 : {
                Assert.check(  userObject instanceof Requirement ) ;
                Requirement req = (Requirement) userObject ;
                icon = outletIcon ;
                str = req.getJackName() +". ";
                if( !req.isMultiple() && !req.isMandatory() )
                    str += "0 or 1" ;
                else if( !req.isMultiple() && req.isMandatory() )
                    str += "Exactly 1" ;
                else if( req.isMultiple() && !req.isMandatory() )
                    str += "Any number" ;
                else if( req.isMultiple() && req.isMandatory() )
                    str += "At least 1" ;
                str += ", implementing " + req.getInterfaceRequired().getName() ;
            } break ;
            default : Assert.check( false ) ;
            }
            
            JLabel result = new JLabel( str ) ;
            result.setOpaque( true ) ;
            if( icon != null ) result.setIcon( icon ) ;
            
            //Font font = result.getFont() ;
            // result.setFont( font.deriveFont( Font.PLAIN ) ) ;
            if( selected ) {
                result.setForeground( active ? Color.BLACK : Color.LIGHT_GRAY ) ;
                result.setBackground( Color.YELLOW ) ; }
            else {
                result.setForeground( active ? Color.BLACK : Color.LIGHT_GRAY ) ;
                result.setBackground( Color.WHITE ) ; }
            return result ;
        }
    }
}