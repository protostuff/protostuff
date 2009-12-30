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

}
