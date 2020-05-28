package com.archimatetool.refmodel.model;

import org.eclipse.swt.graphics.Image;

import com.archimatetool.canvas.ICanvasImages;

public class RefModel extends AbstractRefModel{
	
	public static final String XML_CANVAS_TEMPLATE_ATTRIBUTE_TYPE_MODEL = "RefModel"; //$NON-NLS-1$
    
    public RefModel() {
    }
    
    public RefModel(String id) {
        super(id);
    }

    @Override
    public String getType() {
        return XML_CANVAS_TEMPLATE_ATTRIBUTE_TYPE_MODEL;
    }

    @Override
    public Image getImage() {
        return ICanvasImages.ImageFactory.getImage(ICanvasImages.ICON_CANVAS_MODEL);
    }

}
