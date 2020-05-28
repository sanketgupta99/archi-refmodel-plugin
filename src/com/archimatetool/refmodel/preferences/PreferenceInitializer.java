/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.refmodel.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;




/**
 * Class used to initialize default preference values
 * 
 * @author Phillip Beauvoir
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer
implements IPreferenceConstants {

    @Override
    public void initializeDefaultPreferences() {
		/*IPreferenceStore store = CanvasEditorPlugin.INSTANCE.getPreferenceStore();
        
		store.setDefault(CANVAS_EDITOR_ENABLED, true);*/
    }
}
