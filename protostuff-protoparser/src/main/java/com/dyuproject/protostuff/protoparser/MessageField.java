//========================================================================
//Copyright 2007-2009 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.protoparser;


/**
 * TODO
 *
 * @author David Yu
 * @created Dec 19, 2009
 */
public class MessageField extends Field<Message>
{
    
    java.lang.String javaType;
    Message owner;
    Message message;
    
    public MessageField()
    {
        super(false);
    }

    public MessageField(Message message)
    {
        this();
        this.message = message;
    }
    
    public Message getOwner()
    {
        return owner;
    }
    
    public Message getMessage()
    {
        return message;
    }

    public java.lang.String getJavaType()
    {
        if(javaType!=null)
            return javaType;
        
        StringBuilder buffer = new StringBuilder();
        Message.computeName(message, owner, buffer);

        return javaType=buffer.toString();
    }
    
    public java.lang.String getDefaultValueAsString()
    {
        return "null";
    }

}
