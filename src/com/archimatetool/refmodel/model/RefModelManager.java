package com.archimatetool.refmodel.model;

import java.io.File;

import org.eclipse.swt.graphics.Image;

import com.archimatetool.canvas.ICanvasImages;
import com.archimatetool.refmodel.RefModelEditorPlugin;

public class RefModelManager implements IRefModelXMLTags{
	
	public static final String ZIP_ENTRY_MANIFEST = "manifest.xml"; //$NON-NLS-1$
    public static final String ZIP_ENTRY_MODEL = "model.archimate"; //$NON-NLS-1$
    public static final String ZIP_ENTRY_THUMBNAILS = "Thumbnails/"; //$NON-NLS-1$
    
    public static final int THUMBNAIL_WIDTH = 512;
    public static final int THUMBNAIL_HEIGHT = 512;
    
    public Image getMainImage() {
        return ICanvasImages.ImageFactory.getImage(ICanvasImages.ICON_CANVAS_MODEL);
    }
    
    public IRefModelGroup loadInbuiltRefModels() {
    	IRefModelGroup group = new RefModelGroup("Sanket1");
    	RefModelEditorPlugin refPlugin = new RefModelEditorPlugin();
        File folder = refPlugin.getTemplatesFolder();
        if(folder.exists()) {
            for(File file : folder.listFiles()) {
                if(file.getName().toLowerCase().endsWith(".zip")) {
                    IRefModel refModel = new RefModel();
                    refModel.setFile(file);
                    group.addTemplate(refModel);
                }
            }
        }
        return group;
    }
    
    /*public void dispose() {
        disposeInbuiltTemplates();
    }
    
    protected void disposeInbuiltTemplates() {
        if(fInbuiltTemplateGroup != null) {
            for(ITemplate template : fInbuiltTemplateGroup.getTemplates()) {
                template.dispose();
            }
        }
    }*/

}
