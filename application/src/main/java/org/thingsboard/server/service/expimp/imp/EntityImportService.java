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
package org.thingsboard.server.service.expimp.imp;

import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.export.EntityExportData;
import org.thingsboard.server.common.data.export.ExportableEntity;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.id.TenantId;

public interface EntityImportService<I extends EntityId, E extends ExportableEntity<I>, D extends EntityExportData<E>> {

    /*
     * TODO [viacheslav]: should be as options:
     *  to update related entities e.g. firmware or device profile if entity already exists
     *  to delete current relations when importing new
     *  to ignore when cannot find linked entity by external id
     * */
    EntityImportResult<E> importEntity(TenantId tenantId, D exportData, EntityImportSettings importSettings);

    EntityType getEntityType();

}
