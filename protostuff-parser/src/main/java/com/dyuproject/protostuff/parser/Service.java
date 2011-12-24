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
public class Service extends AnnotationContainer implements HasName
{
    
    final String name;
    final Proto proto;
    final LinkedHashMap<String,RpcMethod> rpcMethods = new LinkedHashMap<String,RpcMethod>();
    
    public Service(String name, Proto proto)
    {
        this.name = name;
        this.proto = proto;
        if(proto.services.put(name, this) != null)
            throw new IllegalStateException("Duplicate service: " + name);
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
    
    public class RpcMethod extends AnnotationContainer implements HasName, HasOptions
    {
        
        final LinkedHashMap<String,Object> standardOptions = new LinkedHashMap<String,Object>();
        final LinkedHashMap<String,Object> extraOptions = new LinkedHashMap<String,Object>();
        
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
            
            if(rpcMethods.put(name, this) != null)
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
        
        public boolean isVoidArgType()
        {
            return argType == null;
        }
        
        public boolean isVoidReturnType()
        {
            return returnType == null;
        }
        
        public String getJavaArgType()
        {
            if(argType == null)
                return "null";
            
            return getProto().getJavaPackageName().equals(argType.getProto().getJavaPackageName()) ? 
                    argType.getRelativeName() : argType.getJavaFullName();
        }
        
        public String getJavaReturnType()
        {
            if(returnType == null)
                return "null";
            
            return getProto().getJavaPackageName().equals(returnType.getProto().getJavaPackageName()) ? 
                    returnType.getRelativeName() : returnType.getJavaFullName();
        }
        
        public LinkedHashMap<String,Object> getStandardOptions()
        {
            return standardOptions;
        }
        
        public void putStandardOption(String key, Object value)
        {
            putExtraOption(key, value);
            standardOptions.put(key, value);
        }
        
        public Object getStandardOption(String name)
        {
            return standardOptions.get(name);
        }
        
        public LinkedHashMap<String,Object> getExtraOptions()
        {
            return extraOptions;
        }
        
        public void putExtraOption(String key, Object value)
        {
            if(extraOptions.put(key, value) != null)
                throw new IllegalStateException("Duplicate rpc option: " + key);
            
            System.err.println(key);
        }
        
        public Object getExtraOption(String name)
        {
            return extraOptions.get(name);
        }
        
        public LinkedHashMap<String,Object> getOptions()
        {
            return extraOptions;
        }
        
        void resolveReferences()
        {
            String fullArgName = (argPackage != null ? argPackage + '.' + argName : argName);
            if(!"void".equals(fullArgName))
            {
                Message argType = proto.findMessageReference(fullArgName, proto.getPackageName());
                if(argType == null)
                {
                    throw new IllegalStateException("The message " + fullArgName + " is not defined.");
                }
                this.argType = argType;
            }
            
            String fullReturnName = (retPackage != null ? retPackage + '.' + retName : retName);
            if(!"void".equals(fullReturnName))
            {
                Message returnType = proto.findMessageReference(fullReturnName, proto.getPackageName());
                if(returnType == null)
                {
                    throw new IllegalStateException("The message " + fullReturnName + " is not defined.");
                }
                this.returnType = returnType;
            }
            
            if(!standardOptions.isEmpty())
                proto.references.add(new ConfiguredReference(standardOptions, extraOptions, proto.getPackageName()));
        }
        
    }

}
