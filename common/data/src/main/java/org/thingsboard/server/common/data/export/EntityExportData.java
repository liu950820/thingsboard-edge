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
package org.thingsboard.server.common.data.export;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.export.impl.AssetExportData;
import org.thingsboard.server.common.data.export.impl.CustomerExportData;
import org.thingsboard.server.common.data.export.impl.DashboardExportData;
import org.thingsboard.server.common.data.export.impl.DeviceExportData;
import org.thingsboard.server.common.data.export.impl.DeviceProfileExportData;
import org.thingsboard.server.common.data.export.impl.RuleChainExportData;
import org.thingsboard.server.common.data.id.EntityId;
import org.thingsboard.server.common.data.relation.EntityRelation;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(property = "entityType", use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @Type(name = "DEVICE", value = DeviceExportData.class),
        @Type(name = "DEVICE_PROFILE", value = DeviceProfileExportData.class),
        @Type(name = "ASSET", value = AssetExportData.class),
        @Type(name = "RULE_CHAIN", value = RuleChainExportData.class),
        @Type(name = "DASHBOARD", value = DashboardExportData.class),
        @Type(name = "CUSTOMER", value = CustomerExportData.class)
})
@Data
public abstract class EntityExportData<E extends ExportableEntity<? extends EntityId>> {

    private E mainEntity;
    private List<EntityRelation> inboundRelations;

    @JsonIgnore
    public abstract EntityType getEntityType();

}
