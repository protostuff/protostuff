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

package com.dyuproject.protostuff;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

/**
 * Resolves the target {@link XMLInputFactory} and {@link XMLOutputFactory} to use.
 *
 * @author David Yu
 * @created May 24, 2010
 */
public final class XmlIOFactoryUtil
{
    
    private XmlIOFactoryUtil() {}
    
    static final String CONFIGURED_INPUT_FACTORY = 
        System.getProperty("javax.xml.stream.XMLInputFactory");
    
    static final String CONFIGURED_OUTPUT_FACTORY = 
        System.getProperty("javax.xml.stream.XMLOutputFactory");
    
    static final boolean CHECK_PARENT = Boolean.getBoolean("protostuff.loader.check_parent");
    
    // check the classpath for these input factory impls
    // sorted according to speed
    private static final String[] INPUT_FACTORY_IMPLS = new String[]{
        "com.fasterxml.aalto.stax.InputFactoryImpl",
        "com.ctc.wstx.stax.WstxInputFactory",
        "com.sun.xml.fastinfoset.stax.factory.StAXInputFactory",
        "com.sun.xml.internal.stream.XMLInputFactoryImpl"
    };
    
    // check the classpath for these output factory impls
    // sorted according to speed
    private static final String[] OUTPUT_FACTORY_IMPLS = new String[]{
        "com.fasterxml.aalto.stax.OutputFactoryImpl",
        "com.ctc.wstx.stax.WstxOutputFactory",
        "com.sun.xml.fastinfoset.stax.factory.StAXOutputFactory",
        "com.sun.xml.internal.stream.XMLOutputFactoryImpl"
    };
    
    /**
     * The default {@link XMLInputFactory} impl.
     */
    public static final XMLInputFactory DEFAULT_INPUT_FACTORY = newXMLInputFactory();
    
    /**
     * The default {@link XMLOutputFactory} impl.
     */
    public static final XMLOutputFactory DEFAULT_OUTPUT_FACTORY = newXMLOutputFactory();
    
    static XMLInputFactory newXMLInputFactory()
    {
        if(CONFIGURED_INPUT_FACTORY != null)
        {
            Class<?> c = loadClass(CONFIGURED_INPUT_FACTORY, XmlIOFactoryUtil.class, CHECK_PARENT);
            if(c == null)
                throw new IllegalStateException("Could not load class: " + CONFIGURED_INPUT_FACTORY);
            
            try
            {
                return (XMLInputFactory)c.newInstance();
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        for(String s : INPUT_FACTORY_IMPLS)
        {
            Class<?> c = loadClass(s, XmlIOFactoryUtil.class, CHECK_PARENT);
            if(c != null)
            {
                try
                {
                    return (XMLInputFactory)c.newInstance();
                }
                catch (Exception e)
                {
                    // print stacktrace?
                    // e.printStackTrace();
                    continue;
                }
            }
        }
        
        throw new IllegalStateException("Cannot find impl for javax.xml.stream.XMLInputFactory");
    }
    
    static XMLOutputFactory newXMLOutputFactory()
    {
        if(CONFIGURED_OUTPUT_FACTORY != null)
        {
            Class<?> c = loadClass(CONFIGURED_OUTPUT_FACTORY, XmlIOFactoryUtil.class, CHECK_PARENT);
            if(c == null)
                throw new IllegalStateException("Could not load class: " + CONFIGURED_OUTPUT_FACTORY);
            
            try
            {
                return (XMLOutputFactory)c.newInstance();
            }
            catch(Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        
        
        for(String s : OUTPUT_FACTORY_IMPLS)
        {
            Class<?> c = loadClass(s, XmlIOFactoryUtil.class, CHECK_PARENT);
            if(c != null)
            {
                try
                {
                    return (XMLOutputFactory)c.newInstance();
                }
                catch (Exception e)
                {
                    // print stacktrace?
                    // e.printStackTrace();
                    continue;
                }
            }
        }
        
        throw new IllegalStateException("Cannot find impl for javax.xml.stream.XMLOutputFactory");
    }
    
    /**
     * Loads a class from the classloader; 
     * If not found, the classloader of the {@code context} class specified will be used.
     * If the flag {@code checkParent} is true, the classloader's parent is included in 
     * the lookup.
     */
    static Class<?> loadClass(String className, Class<?> context, boolean checkParent)
    {
        Class<?> clazz = null;
        try
        {
            clazz = Thread.currentThread().getContextClassLoader().loadClass(className);  
        }
        catch(ClassNotFoundException e)
        {
            if(context != null)
            {
                ClassLoader loader = context.getClassLoader();
                while(loader != null)
                {
                    try
                    {                    
                        clazz = loader.loadClass(className);
                        return clazz;
                    }
                    catch(ClassNotFoundException e1)
                    {
                        loader = checkParent ? loader.getParent() : null;
                    }
                }
            }
        }
        return clazz;
    }

}
