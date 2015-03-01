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

package io.protostuff.parser;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Represents a service defined in the proto (for generating rpc services).
 * 
 * @author David Yu
 * @created Jun 18, 2010
 */
public class Service extends AnnotationContainer implements HasName, HasOptions
{

    final String name;
    final Message parentMessage;
    final Proto proto;

    final LinkedHashMap<String, RpcMethod> rpcMethods = new LinkedHashMap<>();

    final LinkedHashMap<String, Object> standardOptions = new LinkedHashMap<>();
    final LinkedHashMap<String, Object> extraOptions = new LinkedHashMap<>();

    public Service(String name, Message parentMessage, Proto proto)
    {
        this.name = name;
        this.parentMessage = parentMessage;

        if (parentMessage != null)
        {
            this.proto = parentMessage.getProto();
            parentMessage.addNestedService(this);
        }
        else
        {
            this.proto = proto;
            proto.addService(this);
        }
    }

    @Override
    public String getName()
    {
        return name;
    }

    public String getFullName()
    {
        StringBuilder buffer = new StringBuilder();
        if (isNested())
            buffer.append(parentMessage.getFullName()).append('.').append(name);
        else
            buffer.append(getProto().getPackageName()).append('.').append(name);
        return buffer.toString();
    }

    public String getJavaFullName()
    {
        StringBuilder buffer = new StringBuilder();
        if (isNested())
            buffer.append(parentMessage.getJavaFullName()).append('.').append(name);
        else
            buffer.append(getProto().getJavaPackageName()).append('.').append(name);
        return buffer.toString();
    }

    public String getRelativeName()
    {
        return isNested() ? parentMessage.getRelativeName() + "." + name : name;
    }

    /* ================================================== */

    public Message getParentMessage()
    {
        return parentMessage;
    }

    public boolean isNested()
    {
        return parentMessage != null;
    }

    @Override
    public Proto getProto()
    {
        return proto;
    }

    /* ================================================== */

    public Collection<RpcMethod> getRpcMethods()
    {
        return rpcMethods.values();
    }

    public LinkedHashMap<String, RpcMethod> getRpcMethodMap()
    {
        return rpcMethods;
    }

    public RpcMethod getRpcMethod(String name)
    {
        return rpcMethods.get(name);
    }

    RpcMethod addRpcMethod(String name, String argName, String argPackage,
            String retName, String retPackage)
    {
        return new RpcMethod(name, this, argName, argPackage, retName, retPackage);
    }

    public LinkedHashMap<String, Object> getStandardOptions()
    {
        return standardOptions;
    }

    @Override
    public void putStandardOption(String key, Object value)
    {
        putExtraOption(key, value);
        standardOptions.put(key, value);
    }

    public Object getStandardOption(String name)
    {
        return standardOptions.get(name);
    }

    public LinkedHashMap<String, Object> getExtraOptions()
    {
        return extraOptions;
    }

    @Override
    public void putExtraOption(String key, Object value)
    {
        if (extraOptions.put(key, value) != null)
            throw err("Duplicate service option: " + key, getProto());
    }

    public Object getExtraOption(String name)
    {
        return extraOptions.get(name);
    }

    public LinkedHashMap<String, Object> getO()
    {
        return getOptions();
    }

    @Override
    public LinkedHashMap<String, Object> getOptions()
    {
        return extraOptions;
    }

    void resolveReferences()
    {
        for (RpcMethod rm : rpcMethods.values())
            rm.resolveReferences();

        if (!standardOptions.isEmpty())
            proto.references.add(new ConfiguredReference(standardOptions, extraOptions, proto.getPackageName()));
    }

    public static class RpcMethod extends AnnotationContainer implements HasName, HasOptions
    {

        final LinkedHashMap<String, Object> standardOptions = new LinkedHashMap<>();
        final LinkedHashMap<String, Object> extraOptions = new LinkedHashMap<>();

        final String name;
        final Service service;
        final int index;

        final String argName, argPackage, retName, retPackage;

        Message argType, returnType;

        RpcMethod(String name, Service service,
                String argName, String argPackage,
                String retName, String retPackage)
        {
            this.name = name;
            this.service = service;
            index = service.rpcMethods.size();

            this.argName = argName;
            this.argPackage = argPackage;

            this.retName = retName;
            this.retPackage = retPackage;

            if (service.rpcMethods.put(name, this) != null)
            {
                throw err("Duplicate rpc method: " + name +
                        " from service " + service.name, service.getProto());
            }
        }

        @Override
        public String getName()
        {
            return name;
        }

        public int getIndex()
        {
            return index;
        }

        @Override
        public Proto getProto()
        {
            return service.getProto();
        }

        public Service getService()
        {
            return service;
        }

        public Service getOwner()
        {
            return service;
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
            if (argType == null)
                return "null";

            return getProto().getJavaPackageName().equals(argType.getProto().getJavaPackageName()) ?
                    argType.getRelativeName() : argType.getJavaFullName();
        }

        public String getJavaReturnType()
        {
            if (returnType == null)
                return "null";

            return getProto().getJavaPackageName().equals(returnType.getProto().getJavaPackageName()) ?
                    returnType.getRelativeName() : returnType.getJavaFullName();
        }

        public LinkedHashMap<String, Object> getStandardOptions()
        {
            return standardOptions;
        }

        @Override
        public void putStandardOption(String key, Object value)
        {
            putExtraOption(key, value);
            standardOptions.put(key, value);
        }

        public Object getStandardOption(String name)
        {
            return standardOptions.get(name);
        }

        public LinkedHashMap<String, Object> getExtraOptions()
        {
            return extraOptions;
        }

        @Override
        public void putExtraOption(String key, Object value)
        {
            if (extraOptions.put(key, value) != null)
                throw err("Duplicate rpc option: " + key, getProto());
        }

        public Object getExtraOption(String name)
        {
            return extraOptions.get(name);
        }

        public LinkedHashMap<String, Object> getO()
        {
            return getOptions();
        }

        @Override
        public LinkedHashMap<String, Object> getOptions()
        {
            return extraOptions;
        }

        void resolveReferences()
        {
            final Proto proto = getProto();

            String enclosingNs = service.isNested() ?
                    service.parentMessage.getFullName() : proto.getPackageName();

            String fullArgName = (argPackage != null ? argPackage + '.' + argName : argName);
            if (!"void".equals(fullArgName))
            {
                Message argType = proto.findMessageReference(fullArgName, enclosingNs);
                if (argType == null)
                {
                    throw err("The message " + fullArgName + " is not defined",
                            proto);
                }

                this.argType = argType;
            }

            String fullReturnName = (retPackage != null ? retPackage + '.' + retName : retName);
            if (!"void".equals(fullReturnName))
            {
                Message returnType = proto.findMessageReference(fullReturnName, enclosingNs);
                if (returnType == null)
                {
                    throw err("The message " + fullReturnName + " is not defined",
                            proto);
                }

                this.returnType = returnType;
            }

            if (!standardOptions.isEmpty())
                proto.references.add(new ConfiguredReference(standardOptions, extraOptions, proto.getPackageName()));
        }

    }

}
