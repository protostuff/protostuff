//========================================================================
//Copyright 2007-2008 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLite.Builder;

/**
 * @author David Yu
 * @created Sep 30, 2009
 */

public interface ProtobufConvertor<T extends MessageLite, B extends Builder>
{
    
    public void generateTo(JsonGenerator generator, T message) throws IOException;
    
    public B parseFrom(JsonParser parser) throws IOException;
    
    
    public interface ProtobufField<T extends MessageLite, B extends Builder>
    {
        public void readFrom(JsonParser parser, B builder) throws IOException;
        
        public void writeTo(JsonGenerator generator, T message, String fieldName) throws IOException;
        
    }
    

}
