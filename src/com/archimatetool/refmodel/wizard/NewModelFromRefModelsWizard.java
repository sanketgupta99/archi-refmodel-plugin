package com.archimatetool.refmodel.wizard;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.refmodel.model.IRefModel;
import com.archimatetool.templates.model.ITemplate;
import com.archimatetool.templates.model.ITemplateGroup;
import com.archimatetool.templates.model.TemplateManager;


public class NewModelFromRefModelsWizard extends Wizard {

	private NewModelFromRefModelsWizardPage fMainPage;
	private IRefModel iRefModel;
    
    public NewModelFromRefModelsWizard() {
        setWindowTitle("Ref Model");
    }
    
    @Override
    public void addPages() {
        fMainPage = new NewModelFromRefModelsWizardPage(new TemplateManager() {
			
			@Override
			protected ITemplateGroup loadInbuiltTemplates() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected boolean isValidTemplateFile(File file) throws IOException {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public File getUserTemplatesManifestFile() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getTemplateFileExtension() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Image getMainImage() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected ITemplate createTemplate(String type) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ITemplate createTemplate(File file) throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
		});
        addPage(fMainPage);
    }
    
    @Override
    public void dispose() {
        super.dispose();
    }
    
    @Override
    public boolean performFinish() {
        // Get template
        iRefModel = fMainPage.getSelectedTemplate();
        return iRefModel != null;
    }
    
    public File getTempModelFile() throws IOException {
        if(iRefModel == null) {
            return null;
        }
        
        File zipFile = iRefModel.getFile();
        
        File tmpFile = File.createTempFile("~architemplate", null); //$NON-NLS-1$
        tmpFile.deleteOnExit();
        
        return ZipUtils.extractZipEntry(zipFile, TemplateManager.ZIP_ENTRY_MODEL, tmpFile);
    }

}
