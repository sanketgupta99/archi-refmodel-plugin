/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.refmodel.model;

import java.util.List;



/**
 * Interface for All Template Groups
 * 
 * @author Phillip Beauvoir
 */
public interface IRefModelGroup {
    
    /**
     * @return The Name of the group
     */
    String getName();
    
    /**
     * Set the Name of the Group
     */
    void setName(String name);
    
    /**
     * @return The templates in the group
     */
    List<IRefModel> getTemplates();
    
    /**
     * @return A copy of the templates in the group, sorted
     */
    List<IRefModel> getSortedTemplates();

    /**
     * Add a template
     * @param template
     */
    void addTemplate(IRefModel template);
    
    /**
     * Delete a template
     * @param template
     * @return
     */
    boolean removeTemplate(IRefModel template);
}
