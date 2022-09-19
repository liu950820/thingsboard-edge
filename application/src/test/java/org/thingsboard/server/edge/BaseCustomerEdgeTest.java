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
package org.thingsboard.server.edge;

import com.google.protobuf.AbstractMessage;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.thingsboard.server.common.data.Customer;
import org.thingsboard.server.gen.edge.v1.CustomerUpdateMsg;
import org.thingsboard.server.gen.edge.v1.UpdateMsgType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

abstract public class BaseCustomerEdgeTest extends AbstractEdgeTest {

    @Test
    @Ignore
    public void testCreateUpdateDeleteCustomer() throws Exception {
        // create customer
        edgeImitator.expectMessageAmount(1);
        Customer customer = new Customer();
        customer.setTitle("Edge Customer");
        Customer savedCustomer = doPost("/api/customer", customer, Customer.class);
        Assert.assertTrue(edgeImitator.waitForMessages());
        AbstractMessage latestMessage = edgeImitator.getLatestMessage();
        Assert.assertTrue(latestMessage instanceof CustomerUpdateMsg);
        CustomerUpdateMsg customerUpdateMsg = (CustomerUpdateMsg) latestMessage;
        Assert.assertEquals(UpdateMsgType.ENTITY_CREATED_RPC_MESSAGE, customerUpdateMsg.getMsgType());
        Assert.assertEquals(savedCustomer.getUuidId().getMostSignificantBits(), customerUpdateMsg.getIdMSB());
        Assert.assertEquals(savedCustomer.getUuidId().getLeastSignificantBits(), customerUpdateMsg.getIdLSB());
        Assert.assertEquals(savedCustomer.getTitle(), customerUpdateMsg.getTitle());
        testAutoGeneratedCodeByProtobuf(customerUpdateMsg);

        // update customer
        edgeImitator.expectMessageAmount(1);
        savedCustomer.setTitle("Edge Customer Updated");
        savedCustomer = doPost("/api/customer", savedCustomer, Customer.class);
        Assert.assertTrue(edgeImitator.waitForMessages());
        latestMessage = edgeImitator.getLatestMessage();
        Assert.assertTrue(latestMessage instanceof CustomerUpdateMsg);
        customerUpdateMsg = (CustomerUpdateMsg) latestMessage;
        Assert.assertEquals(UpdateMsgType.ENTITY_UPDATED_RPC_MESSAGE, customerUpdateMsg.getMsgType());
        Assert.assertEquals(savedCustomer.getUuidId().getMostSignificantBits(), customerUpdateMsg.getIdMSB());
        Assert.assertEquals(savedCustomer.getUuidId().getLeastSignificantBits(), customerUpdateMsg.getIdLSB());
        Assert.assertEquals(savedCustomer.getTitle(), customerUpdateMsg.getTitle());

        // delete customer
        edgeImitator.expectMessageAmount(1);
        doDelete("/api/customer/" + savedCustomer.getUuidId())
                .andExpect(status().isOk());
        Assert.assertTrue(edgeImitator.waitForMessages());
        latestMessage = edgeImitator.getLatestMessage();
        Assert.assertTrue(latestMessage instanceof CustomerUpdateMsg);
        customerUpdateMsg = (CustomerUpdateMsg) latestMessage;
        Assert.assertEquals(UpdateMsgType.ENTITY_DELETED_RPC_MESSAGE, customerUpdateMsg.getMsgType());
        Assert.assertEquals(customerUpdateMsg.getIdMSB(), savedCustomer.getUuidId().getMostSignificantBits());
        Assert.assertEquals(customerUpdateMsg.getIdLSB(), savedCustomer.getUuidId().getLeastSignificantBits());
    }

}
