/*
 * DBeaver - Universal Database Manager
 * Copyright (C) 2010-2015 Serge Rieder (serge@jkiss.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (version 2)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.jkiss.dbeaver.model.data;

import org.jkiss.code.NotNull;
import org.jkiss.code.Nullable;
import org.jkiss.dbeaver.model.DBPDataKind;
import org.jkiss.dbeaver.model.DBPImage;
import org.jkiss.dbeaver.model.DBPImageProvider;
import org.jkiss.dbeaver.model.DBUtils;
import org.jkiss.dbeaver.model.exec.DBCAttributeMetaData;
import org.jkiss.dbeaver.model.exec.DBCEntityMetaData;
import org.jkiss.dbeaver.model.exec.DBCException;
import org.jkiss.dbeaver.model.struct.DBSEntityAttribute;
import org.jkiss.dbeaver.model.struct.DBSEntityReferrer;

import java.util.List;

/**
 * Collection element binding info
 */
public class DBDAttributeBindingElement extends DBDAttributeBinding implements DBCAttributeMetaData, DBPImageProvider {
    @NotNull
    private final DBDCollection collection;
    private final int index;

    public DBDAttributeBindingElement(
        @NotNull DBDAttributeBinding parent,
        @NotNull DBDCollection collection,
        int index)
    {
        super(parent.getDataSource(), parent, collection.getComponentValueHandler());
        this.collection = collection;
        this.index = index;
    }

    /**
     * Attribute index in result set
     * @return attribute index (zero based)
     */
    @Override
    public int getOrdinalPosition()
    {
        return 0;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public boolean isAutoGenerated() {
        return false;
    }

    @Override
    public boolean isPseudoAttribute() {
        return false;
    }

    @Nullable
    @Override
    public DBDCollection getSource() {
        return collection;
    }

    /**
     * Attribute label
     */
    @NotNull
    public String getLabel()
    {
        return getName();
    }

    @Nullable
    @Override
    public String getEntityName() {
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Nullable
    @Override
    public DBDPseudoAttribute getPseudoAttribute() {
        return null;
    }

    @Nullable
    @Override
    public DBCEntityMetaData getEntityMetaData() {
        assert parent != null;
        return parent.getMetaAttribute().getEntityMetaData();
    }

    /**
     * Attribute name
     */
    @NotNull
    public String getName()
    {
        return String.valueOf(index + 1);
    }

    /**
     * Meta attribute (obtained from result set)
     */
    @NotNull
    public DBCAttributeMetaData getMetaAttribute() {
        return this;
    }

    /**
     * Entity attribute
     */
    @Nullable
    public DBSEntityAttribute getEntityAttribute()
    {
        return null;
    }

    /**
     * Row identifier (may be null)
     */
    @Nullable
    public DBDRowIdentifier getRowIdentifier() {
        assert parent != null;
        return parent.getRowIdentifier();
    }

    @Nullable
    @Override
    public List<DBSEntityReferrer> getReferrers() {
        return null;
    }

    @Nullable
    @Override
    public Object extractNestedValue(@NotNull Object ownerValue) throws DBCException {
        if (collection.isNull()) {
            // Can happen if values was released
            return null;
        }
        return collection.getItem(index);
    }

/*
    @Override
    public boolean hasNestedBindings() {
        assert parent != null;
        return parent.hasNestedBindings();
    }

    @Nullable
    @Override
    public List<DBDAttributeBinding> getNestedBindings() {
        assert parent != null;
        return parent.getNestedBindings();
    }
*/

    @Override
    public String getTypeName() {
        return collection.getComponentType().getTypeName();
    }

    @Override
    public int getTypeID() {
        return collection.getComponentType().getTypeID();
    }

    @Override
    public DBPDataKind getDataKind() {
        return collection.getComponentType().getDataKind();
    }

    @Override
    public int getScale() {
        return collection.getComponentType().getScale();
    }

    @Override
    public int getPrecision() {
        return collection.getComponentType().getPrecision();
    }

    @Override
    public long getMaxLength() {
        return collection.getComponentType().getMaxLength();
    }

    @Nullable
    @Override
    public DBPImage getObjectImage() {
        if (collection.getComponentType() instanceof DBPImageProvider) {
            return ((DBPImageProvider) collection.getComponentType()).getObjectImage();
        }
        return DBUtils.getDataIcon(collection.getComponentType());
    }
}