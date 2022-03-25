/**
 * Copyright © 2016-2022 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.server.service.expimp.exp.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.thingsboard.server.common.data.export.EntityExportData;
import org.thingsboard.server.common.data.export.ExportableEntity;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.TenantId;
import org.thingsboard.server.common.data.relation.EntityRelation;
import org.thingsboard.server.common.data.relation.RelationTypeGroup;
import org.thingsboard.server.dao.relation.RelationService;
import org.thingsboard.server.service.expimp.ExportableEntitiesService;
import org.thingsboard.server.service.expimp.exp.EntityExportService;
import org.thingsboard.server.service.expimp.exp.EntityExportSettings;

import java.util.List;

public abstract class AbstractEntityExportService<I extends EntityId, E extends ExportableEntity<I>, D extends EntityExportData<E>> implements EntityExportService<I, E, D> {

    @Autowired @Lazy
    private ExportableEntitiesService exportableEntitiesService;
    @Autowired
    private RelationService relationService;


    @Override
    public final D getExportData(TenantId tenantId, I entityId, EntityExportSettings exportSettings) {
        D exportData = newExportData();

        E mainEntity = exportableEntitiesService.findEntityById(tenantId, entityId);
        exportData.setMainEntity(mainEntity);
        setRelatedEntities(tenantId, mainEntity, exportData);
        if (exportSettings.isExportInboundRelations()) {
            exportData.setInboundRelations(getInboundRelations(tenantId, entityId));
        }

        return exportData;
    }

    protected List<EntityRelation> getInboundRelations(TenantId tenantId, I entityId) {
        return relationService.findByTo(tenantId, entityId, RelationTypeGroup.COMMON); // FIXME [viacheslav]: RelationTypeGroup
    }

    protected void setRelatedEntities(TenantId tenantId, E mainEntity, D exportData) {}

    protected abstract D newExportData();

}
