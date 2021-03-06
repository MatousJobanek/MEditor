/*
 * Metadata Editor
 * @author Jiri Kremser
 * 
 * 
 * 
 * Metadata Editor - Rich internet application for editing metadata.
 * Copyright (C) 2011  Jiri Kremser (kremser@mzk.cz)
 * Moravian Library in Brno
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * 
 */

package cz.mzk.editor.client.gwtrpcds;

import javax.inject.Inject;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

import cz.mzk.editor.client.DublinCoreConstants;
import cz.mzk.editor.client.LangConstants;
import cz.mzk.editor.client.util.Constants;

// TODO: Auto-generated Javadoc
/**
 * The Class InputTreeGwtRPCDS.
 */
public class Z3950ResultDS
        extends DataSource {

    /**
     * Instantiates a new input tree gwt rpcds.
     * 
     * @param dispatcher
     *        the dispatcher
     */
    @Inject
    public Z3950ResultDS(LangConstants lang) {
        DataSourceField field;
        field = new DataSourceIntegerField(Constants.ATTR_GENERIC_ID, "id");
        field.setPrimaryKey(true);
        field.setRequired(true);
        field.setHidden(true);
        field = new DataSourceTextField(DublinCoreConstants.DC_TITLE, lang.dcTitle());
        field.setAttribute("width", "50%");
        field.setRequired(true);
        field.setMultiple(true);
        addField(field);
        field = new DataSourceTextField(DublinCoreConstants.DC_IDENTIFIER, lang.dcIdentifier());
        field.setMultiple(true);
        field.setPrimaryKey(true);
        addField(field);
        field = new DataSourceTextField(DublinCoreConstants.DC_PUBLISHER, lang.dcPublisher());
        field.setMultiple(true);
        addField(field);
        field = new DataSourceTextField(DublinCoreConstants.DC_DATE, lang.dcDate());
        field.setMultiple(true);
        addField(field);
        field = new DataSourceTextField(DublinCoreConstants.DC_DESCRIPTION, lang.dcDescription());
        field.setMultiple(true);
        addField(field);
        field = new DataSourceTextField(DublinCoreConstants.DC_LANGUAGE, lang.dcLanguage());
        field.setMultiple(true);
        addField(field);
        field = new DataSourceTextField(DublinCoreConstants.DC_SUBJECT, lang.dcSubject());
        field.setMultiple(true);
        addField(field);
        field = new DataSourceTextField(DublinCoreConstants.DC_RELATION, lang.dcRelation());
        field.setMultiple(true);
        addField(field);

    }
}
