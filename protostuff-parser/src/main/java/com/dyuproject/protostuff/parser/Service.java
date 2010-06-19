//========================================================================
//Copyright 2007-2010 David Yu dyuproject@gmail.com
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

package com.dyuproject.protostuff.parser;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Represents a service defined in the proto (for generating rpc services).
 *
 * @author David Yu
 * @created Jun 18, 2010
 */
public class Service implements HasName
{
    
    final String name;
    final Proto proto;
    final LinkedHashMap<String,RpcMethod> rpcMethods = new LinkedHashMap<String,RpcMethod>();
    
    public Service(String name, Proto proto)
    {
        this.name = name;
        this.proto = proto;
        proto.services.put(name, this);
    }

    public String getName()
    {
        return name;
    }
    
    public Proto getProto()
    {
        return proto;
    }
    
    public Collection<RpcMethod> getRpcMethods()
    {
        return rpcMethods.values();
    }
    
    public RpcMethod getRpcMethod(String name)
    {
        return rpcMethods.get(name);
    }
    
    RpcMethod addRpcMethod(String name, String argName, String argPackage, 
            String retName, String retPackage)
    {
        return new RpcMethod(name, argName, argPackage, retName, retPackage);
    }
    
    void resolveReferences()
    {
        for(RpcMethod rm : rpcMethods.values())
            rm.resolveReferences();
    }
    
    public class RpcMethod implements HasName
    {
        
        final int index;
        final String name, argName, argPackage, retName, retPackage;
        Message argType, returnType;
        
        RpcMethod(String name, String argName, String argPackage, String retName, 
                String retPackage)
        {
            index = rpcMethods.size();
            this.name = name;
            this.argName = argName;
            this.argPackage = argPackage;
            this.retName = retName;
            this.retPackage = retPackage;
            
            rpcMethods.put(name, this);
            if(index+1 != rpcMethods.size())
            {
                throw new IllegalStateException("Duplicate rpc method: " + name + 
                        " from service " + Service.this.name);
            }
        }

        public String getName()
        {
            return name;
        }
        
        public int getIndex()
        {
            return index;
        }
        
        public Service getOwner()
        {
            return Service.this;
        }
        
        public Message getArgType()
        {
            return argType;
        }
        
        public Message getReturnType()
        {
            return returnType;
        }
        
        void resolveReferences()
        {
            argType = Message.findMessage(argName, argPackage, proto);
            returnType = Message.findMessage(retName, retPackage, proto);
            if(argType == null)
            {
                String type = argPackage == null ? argName : argPackage + "." + argName;
                throw new IllegalStateException("The message " + type + " is not defined.");
            }
            if(returnType == null)
            {
                String type = retPackage == null ? retName : retPackage + "." + retName;
                throw new IllegalStateException("The message " + type + " is not defined.");
            }
        }
        
    }

}
