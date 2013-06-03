package tmeclipseplugin_2.actions;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI ;

import tmeclipseplugin_2.TMAdapter;

public class SendToTMObjectActionDelegate implements IObjectActionDelegate {

	//private IWorkbenchPart targetPart;
	private ISelection selection;

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		//this.targetPart = targetPart ;
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		Shell shell = new Shell();

		String message = null ;
        IResource resource = null ;
		if( selection != null && selection instanceof IStructuredSelection ) {
			IStructuredSelection iss = (IStructuredSelection) selection ;
			if( iss.size() == 1 ) {
				Object obj = iss.getFirstElement() ;
				if( obj instanceof IResource ) {
					resource = (IResource) obj ; }
				else {
                    if( obj instanceof IAdaptable ) {
                        resource = (IResource) ((IAdaptable)obj).getAdapter( IResource.class ) ;
                    }
					if( resource == null ) 
                        message = "Selection is not a file" ; } }
			else if( iss.size() == 0 ){
				message = "Selection is empty" ; }
			else {
				message = "Selection is multiple" ; } }
		else {
			message = "No selection or selection is unsuitable" ; }
        
        // assert resource != null || message != null ;
        
        // Send it to the Teaching Machine
        if( resource != null ) {
        	boolean cancelled = ! PlatformUI.getWorkbench().saveAllEditors(true) ; 
            TMAdapter.load( resource ) ; }
        
		if (message != null) {
            MessageDialog.openInformation(
                    shell, 
                    "TmEclipsePlugIn Plug-in",
                    message) ; }
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection ;
	}
}