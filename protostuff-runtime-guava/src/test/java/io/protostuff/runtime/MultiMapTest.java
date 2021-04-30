//========================================================================
//Copyright 2020 Mr14huashao
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package io.protostuff.runtime;

import com.google.common.collect.LinkedListMultimap;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

/**
 * Test  MultiMap
 * 
 * @author Mr14huashao
 * @created Oct 9, 2020
 */
public  class MultiMapTest
{

    @Test
    public void testListMultiMapWrapper() {
        LinkedBuffer buffer = LinkedBuffer.allocate(256);
        LinkedListMultimap<String, String> listMultimap = LinkedListMultimap.create();
        HashMap hashMap = new HashMap();
        hashMap.put("xy","AC");
        hashMap.put("xy","CD");
        listMultimap.put("xy","xy1");
        listMultimap.put("xy","ab2");

        ListMultiMapWrapper listMultiMapWrapper = new ListMultiMapWrapper(listMultimap,hashMap);
        Schema<ListMultiMapWrapper> lSchema = RuntimeSchema.getSchema(ListMultiMapWrapper.class);
        byte[] bytes = ProtostuffIOUtil.toByteArray(listMultiMapWrapper,lSchema,buffer);

        ListMultiMapWrapper resultMMW = new ListMultiMapWrapper();
        ProtostuffIOUtil.mergeFrom(bytes,resultMMW,lSchema);

        Assert.assertEquals(resultMMW.multimap, listMultiMapWrapper.multimap);
    }

    public class ListMultiMapWrapper {
        private LinkedListMultimap multimap;
        private HashMap hashMap;

        public ListMultiMapWrapper(LinkedListMultimap multimap, HashMap hashmap) {
            this.multimap = multimap;
            this.hashMap = hashmap;
        }

        public ListMultiMapWrapper() {

        }
    }
    

}
