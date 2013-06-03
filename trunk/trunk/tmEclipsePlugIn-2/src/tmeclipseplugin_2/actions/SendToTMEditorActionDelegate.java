/*
 * Created on Mar 20, 2005
 *
 * */
package tmeclipseplugin_2.actions;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI ;


import tmeclipseplugin_2.TMAdapter;


/** Action delegate for sending a resource being edited to the Teaching Machine.
 * @author theo
 */
public class SendToTMEditorActionDelegate implements IEditorActionDelegate {

    private IEditorPart targetEditor;
    
    /**
     * @see org.eclipse.ui.IEditorActionDelegate#setActiveEditor(org.eclipse.jface.action.IAction, org.eclipse.ui.IEditorPart)
     */
    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        this.targetEditor = targetEditor ;
        //System.out.println("SendToTMEditorActionDelegate setActiveEditor" ) ;
    }

    /**
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
        //System.out.println("SendToTMEditorActionDelegate run" ) ;
        String message = null ;
        if( targetEditor == null ) {
            message = "No editor" ; }
        else {
            IEditorInput ei = targetEditor.getEditorInput() ;
            if( ei == null  ) {
                message = "Editor has no input" ; }
            else {
                IResource resource = (IResource) ei.getAdapter( IResource.class ) ;
                if( resource == null ) {
                    message = "Input does not adapt to resource" ; }
                else {
                	boolean cancelled = ! PlatformUI.getWorkbench().saveAllEditors(true) ; 
                    TMAdapter.load( resource ) ; } } }
        if( message != null ) {
            Shell shell = new Shell() ;
            MessageDialog.openError(shell,
                    "TmEclipsePlugIn Plug-in",
                    message ) ; }
    }

    /**
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        //System.out.println("SendToTMEditorActionDelegate selectionChanged" ) ;
    }

}
