package com.archimatetool.refmodel;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

// TODO Create RefModel's own Message class and message.properties file and Images.
import com.archimatetool.canvas.ICanvasImages;
import com.archimatetool.canvas.Messages;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.ui.services.UIRequestManager;
import com.archimatetool.editor.views.tree.TreeEditElementRequest;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.ModelVersion;
import com.archimatetool.model.util.UUIDFactory;
import com.archimatetool.refmodel.wizard.NewModelFromRefModelsWizard;

public class NewModelFromRefModels extends ExtensionContributionFactory {

	@Override
	public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {
		// TODO Auto-generated method stub
		IContributionItem item = new ActionContributionItem(new NewModelWithRefAction());
        additions.addContributionItem(item, visibleExpression);
	}
	
	// TODO Do we really want this feature? It's the same as create new model, then add new canvas from template
    private final static boolean ENABLED = true;
    
    public NewModelFromRefModels() {
    }

    /**
     * Action to create new Model with a Reference Model
     */
    private class NewModelWithRefAction extends Action {
        @Override
        public String getText() {
            return "Model From Reference Models";
        }
        
        @Override
        public void run() {
            // Open a Wizard to list out available reference models
        	NewModelFromRefModelsWizard wizard = new NewModelFromRefModelsWizard();
            WizardDialog dialog = new ExtendedWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                                  wizard,
                                  "NewModelFromReferenceModel"); //$NON-NLS-1$
            
            if(dialog.open() == Window.OK) {
            	try {
                    File file = wizard.getTempModelFile();
                    if(file != null) {
                        IArchimateModel model = IEditorModelManager.INSTANCE.openModel(file);
                        if(model != null) {
                            // Name
                            model.setName(Messages.NewModelWithCanvasExtensionContributionFactory_1); // TODO Change message
                            
                            // Set latest model version (need to do this in case we immediately save as Template)
                            model.setVersion(ModelVersion.VERSION);
                            
                            // Set file to null
                            model.setFile(null);
                            
                            // New IDs
                            UUIDFactory.generateNewIDs(model);
                            
                            // Open new model
                            EditorManager.openDiagramEditor(model.getDefaultDiagramModel(), false);
                            
                            // Edit in-place in Tree
                            UIRequestManager.INSTANCE.fireRequestAsync(new TreeEditElementRequest(this, model));
                        }
                        
                        file.delete();
                    }
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                    MessageDialog.openError(Display.getCurrent().getActiveShell(),
                            Messages.NewModelWithCanvasExtensionContributionFactory_2, ex.getMessage());
                }
            }
        }
        
        @Override
        public String getId() {
            return "newModelFromReferenceModel"; //$NON-NLS-1$
        };
        
        @Override
        public ImageDescriptor getImageDescriptor() {
            return ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_NEWFILE); // TODO image
        }
    };
    
    private Expression visibleExpression = new Expression() {
        @Override
        public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
            boolean isVisible = ENABLED;
            return isVisible ? EvaluationResult.TRUE : EvaluationResult.FALSE;
        }
    };
}
