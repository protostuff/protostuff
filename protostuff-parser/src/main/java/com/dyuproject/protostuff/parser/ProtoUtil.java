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

package com.dyuproject.protostuff.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;

/**
 * TODO
 *
 * @author David Yu
 * @created Dec 24, 2009
 */
public class ProtoUtil
{
    
    public static void loadFrom(InputStream in, Proto target) throws Exception
    {
        // Create an input character stream from standard in
        ANTLRInputStream input = new ANTLRInputStream(in);
        // Create an ExprLexer that feeds from that stream
        ProtoLexer lexer = new ProtoLexer(input);
        // Create a stream of tokens fed by the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // Create a parser that feeds off the token stream
        ProtoParser parser = new ProtoParser(tokens);
        // Begin parsing at rule parse
        parser.parse(target);
    }
    
    public static Proto parseProto(File file)
    {
        Proto proto = new Proto(file);
        try
        {
            loadFrom(file, proto);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return proto;
    }
    
    public static void loadFrom(File file, Proto target) throws Exception
    {
        FileInputStream in = new FileInputStream(file);
        try
        {
            loadFrom(in, target);
        }
        finally
        {
            in.close();
        }
    }
    
    public static void loadFrom(URL resource, Proto target) throws Exception
    {
        InputStream in = resource.openStream();
        try
        {
            loadFrom(in, target);
        }
        finally
        {
            in.close();
        }
    }
    
    public static StringBuilder toCamelCase(String name)
    {
        StringBuilder buffer = new StringBuilder();
        int toUpper = 0;
        char c;
        for(int i=0, len=name.length(); i<len;)
        {
            c= name.charAt(i++);
            if(c=='_')
            {
                if(i==len)
                    break;
                if(buffer.length()!=0)
                    toUpper++;
                continue;
            }
            else if(toUpper!=0)
            {
                if(c>96 && c<123)
                {
                    buffer.append((char)(c-32));
                    toUpper = 0;
                }
                else if(c>64 && c<91)
                {
                    buffer.append(c);
                    toUpper = 0;
                }
                else
                {
                    while(toUpper>0)
                    {
                        buffer.append('_');
                        toUpper--;
                    }
                    buffer.append(c);
                }
            }
            else
            {
                if(buffer.length()==0 && c>64 && c<91)
                    buffer.append((char)(c+32));
                else
                    buffer.append(c);
            }
        }
        return buffer;
    }
    
    public static StringBuilder toPascalCase(String name)
    {
        StringBuilder buffer = toCamelCase(name);
        char c = buffer.charAt(0);
        if(c>96 && c<123)
            buffer.setCharAt(0, (char)(c-32));
        
        return buffer;
    }
    
    public static StringBuilder toUnderscoreCase(String name)
    {
        StringBuilder buffer = new StringBuilder();
        boolean toLower = false, appendUnderscore=false;
        for(int i=0, len=name.length(); i<len;)
        {
            char c = name.charAt(i++);
            if(c=='_')
            {
                if(i==len)
                    break;
                if(buffer.length()!=0)
                    appendUnderscore = true;
                
                continue;
            }
            
            if(appendUnderscore)
                buffer.append('_');
            
            if(c>96 && c<123)
            {
                buffer.append(c);
                toLower = true;
            }
            else if(c>64 && c<91)
            {
                if(toLower)
                {
                    // avoid duplicate underscore
                    if(!appendUnderscore)
                        buffer.append('_');
                    toLower = false;
                }
                buffer.append((char)(c+32));
            }
            else
            {
                buffer.append(c);
                toLower = false;
            }
            appendUnderscore = false;
        }
        return buffer;
    }
    
    public static void main(String[] args)
    {
        String[] gg = {"foo_bar_baz", "fooBarBaz", "FooBarBaz", "foo_bar_baz", "____Foo____Bar___Baz____"};
        for(String g : gg)
        {
            System.err.println(toCamelCase(g));
            System.err.println(toPascalCase(g));
            System.err.println(toUnderscoreCase(g));
            System.err.println(toUnderscoreCase(g).toString().toUpperCase());
        }
    }

}
