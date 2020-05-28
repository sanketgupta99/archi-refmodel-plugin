package com.archimatetool.refmodel.wizard;

import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.refmodel.RefModelEditorPlugin;
import com.archimatetool.refmodel.model.IRefModel;
import com.archimatetool.refmodel.model.IRefModelGroup;
import com.archimatetool.refmodel.model.RefModel;
import com.archimatetool.refmodel.model.RefModelGroup;
import com.archimatetool.refmodel.model.RefModelManager;
import com.archimatetool.templates.model.ITemplate;
import com.archimatetool.templates.model.ITemplateGroup;
import com.archimatetool.templates.model.TemplateManager;
import com.archimatetool.templates.wizard.Messages;
import com.archimatetool.templates.wizard.TemplateGroupsTableViewer;

import java.io.File;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.nebula.widgets.gallery.NoGroupRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.ui.PlatformUI;

public class NewModelFromRefModelsWizardPage extends WizardPage {

	protected NewModelFromRefModelsWizardPage(TemplateManager templateManager) {
		super("New Page");
		// TODO Auto-generated constructor stub
	}

	protected void init() {
        setTitle("ll");
        setDescription("ll");
    }
    
    protected String getHelpID() {
        return "ll";
    }
    

    
    protected Gallery fGallery;
    protected GalleryItem fGalleryRoot;
    protected StyledText fDescriptionText;
    
    protected TableViewer fInbuiltTableViewer;
    protected TemplateManager fTemplateManager;
    protected TableViewer fLastViewerFocus;
    
    protected IRefModel fSelectedModel;
    /*
     * Track and de-select Viewers so that only one has focus at a time
     */
    
    protected int DEFAULT_GALLERY_ITEM_SIZE = 120;
    
    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(container, getHelpID());

        GridData gd;
        
        SashForm sash1 = new SashForm(container, SWT.VERTICAL);
        //this is the middle part where each model is listed
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.widthHint = 800;
        gd.heightHint = 500;
        sash1.setLayoutData(gd);
        
        Composite tableComposite = new Composite(sash1, SWT.BORDER);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        //tableComposite.setLayout(layout); //Refmodel object
        
        RefModelManager refModelManager = new RefModelManager();
        
        fInbuiltTableViewer = createGroupsTableViewer(tableComposite, Messages.NewModelFromTemplateWizardPage_2, gd);
        fInbuiltTableViewer.setInput(new Object[] { refModelManager.loadInbuiltRefModels()});
        
        SashForm sash2 = new SashForm(sash1, SWT.VERTICAL);
        
        Composite galleryComposite = new Composite(sash2, SWT.NULL);
        layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = 0;
        galleryComposite.setLayout(layout); //refmodel models
        
        fGallery = new Gallery(galleryComposite, SWT.V_SCROLL | SWT.BORDER);
        fGallery.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Renderers
        final NoGroupRenderer groupRenderer = new NoGroupRenderer();
        groupRenderer.setItemSize(DEFAULT_GALLERY_ITEM_SIZE, DEFAULT_GALLERY_ITEM_SIZE);
        groupRenderer.setAutoMargin(true);
        groupRenderer.setMinMargin(10);
        fGallery.setGroupRenderer(groupRenderer);
        
        final DefaultGalleryItemRenderer itemRenderer = new DefaultGalleryItemRenderer();
        itemRenderer.setDropShadows(true);
        itemRenderer.setDropShadowsSize(7);
        itemRenderer.setShowRoundedSelectionCorners(false);
        fGallery.setItemRenderer(itemRenderer);
        
        // Root Group
        fGalleryRoot = new GalleryItem(fGallery, SWT.NONE);
        
        // Slider
        final Scale scale = new Scale(galleryComposite, SWT.HORIZONTAL);
        gd = new GridData(SWT.END, SWT.NONE, false, false);
        gd.widthHint = 120;
        if(PlatformUtils.isMac()) { // Mac clips height of slider
            gd.heightHint = 20;
        }
        scale.setLayoutData(gd);
        scale.setMaximum(480);
        scale.setMinimum(64);
        scale.setIncrement(8);
        scale.setPageIncrement(64);
        scale.setSelection(DEFAULT_GALLERY_ITEM_SIZE);
        scale.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int inc = scale.getSelection();
                itemRenderer.setDropShadows(inc >= DEFAULT_GALLERY_ITEM_SIZE);
                groupRenderer.setItemSize(inc, inc);
            }
        });
        
        // Description
        fDescriptionText = new StyledText(sash2, SWT.V_SCROLL | SWT.READ_ONLY | SWT.WRAP | SWT.BORDER);
        
        fGallery.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(e.item instanceof GalleryItem) {
                	IRefModel refModel = (IRefModel)((GalleryItem)e.item).getData();
                    updateWizard(refModel);
                }
                else {
                    updateWizard(null);
                }
             }
        });
        
        selectFirstTableItem();
        sash1.setWeights(new int[] { 30, 70 });
        sash2.setWeights(new int[] { 70, 30 });
        setPageComplete(true); // Yes it's OK
        
    }
    
    protected RefModelGroupsTableViewer createGroupsTableViewer(Composite parent, String labelText, GridData gd) {
        CLabel label = new CLabel(parent, SWT.NULL);
        label.setText(labelText);
        //label.setImage(fTemplateManager.getMainImage());
        
        Composite tableComp = new Composite(parent, SWT.NULL);
        tableComp.setLayout(new TableColumnLayout());
        tableComp.setLayoutData(gd);
        final RefModelGroupsTableViewer tableViewer = new RefModelGroupsTableViewer(tableComp, SWT.NULL);
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                Object o = ((IStructuredSelection)event.getSelection()).getFirstElement();
                handleTableItemSelected(o);
                deFocusTable(tableViewer);
            }
        });
        
        return tableViewer;
    }
    
    private void selectFirstTableItem() {
        TableViewer tableViewer;
        tableViewer = fInbuiltTableViewer;
        Object o = tableViewer.getElementAt(0);
        if(o != null) {
            tableViewer.setSelection(new StructuredSelection(o));
            tableViewer.getControl().setFocus();
        }
    }
    
    protected void handleTableItemSelected(Object item) {
        // Template Group
        if(item instanceof IRefModelGroup) {
            clearGallery();
            updateGallery((IRefModelGroup)item);
        }
    }
    
    protected void clearGallery() {
        if(fGalleryRoot != null && !fGallery.isDisposed() && fGallery.getItemCount() > 0) {
            while(fGalleryRoot.getItemCount() > 0) {
                GalleryItem item = fGalleryRoot.getItem(0);
                fGalleryRoot.remove(item);
            }
        }
    }
    
    /**
     * Update the Gallery
     * @param item2
     */
    protected void updateGallery(IRefModelGroup item2) {
        for(IRefModel refModel : item2.getSortedTemplates()) {
            GalleryItem item = new GalleryItem(fGalleryRoot, SWT.NONE);
            item.setText(StringUtils.safeString(refModel.getName()));
            item.setImage(refModel.getKeyThumbnail());
            item.setData(refModel);
        }
        
        if(fGalleryRoot.getItem(0) != null) {
            fGallery.setSelection(new GalleryItem[] { fGalleryRoot.getItem(0) });
            updateWizard((IRefModel)fGalleryRoot.getItem(0).getData());
        }
        else {
            updateWizard(null);
        }
    }
    
    private void deFocusTable(TableViewer viewer) {
        if(fLastViewerFocus != null && !fLastViewerFocus.getControl().isDisposed() && fLastViewerFocus != viewer) {
                fLastViewerFocus.getTable().deselectAll();
        }
        fLastViewerFocus = viewer;
    }
    
    protected void updateWizard(IRefModel refModel) {
        fSelectedModel = refModel;

        if(refModel == null) {
            fDescriptionText.setText(""); //$NON-NLS-1$
            setPageComplete(false);
        }
        else {
            // Update description
            String text = StringUtils.safeString(refModel.getDescription());
            String desc = StringUtils.safeString(refModel.getName()) + ":"; //$NON-NLS-1$
            fDescriptionText.setText(desc + "   " + text); //$NON-NLS-1$
            StyleRange style = new StyleRange();
            style.start = 0;
            style.length = desc.length();
            style.fontStyle = SWT.BOLD;
            fDescriptionText.setStyleRange(style);
            setPageComplete(true);
        }
    }
    
    public IRefModel getSelectedTemplate() {
        return fSelectedModel;
    }
}
