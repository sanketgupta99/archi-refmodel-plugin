/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.refmodel.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.archimatetool.editor.utils.StringUtils;




/**
 * Template Group
 * 
 * @author Phillip Beauvoir
 */
public class RefModelGroup implements IRefModelGroup {
    
    private List<IRefModel> fTemplates = new ArrayList<IRefModel>();
    private String fName;
    
    public RefModelGroup() {
    }

    public RefModelGroup(String name) {
        fName = name;
    }

    @Override
    public String getName() {
        return fName;
    }

    @Override
    public void setName(String name) {
        fName = name;
    }

    @Override
    public List<IRefModel> getTemplates() {
        return fTemplates;
    }

    @Override
    public void addTemplate(IRefModel template) {
        fTemplates.add(template);
    }

    @Override
    public boolean removeTemplate(IRefModel template) {
        return fTemplates.remove(template);
    }

    @Override
    public List<IRefModel> getSortedTemplates() {
        List<IRefModel> list = new ArrayList<IRefModel>(getTemplates());
        
        Collections.sort(list, new Comparator<IRefModel>() {
            @Override
            public int compare(IRefModel t1, IRefModel t2) {
                String name1 = StringUtils.safeString(t1.getName()).toLowerCase().trim();
                String name2 = StringUtils.safeString(t2.getName()).toLowerCase().trim();
                return name1.compareTo(name2);
            }
        });
        
        return list;
    }
}
