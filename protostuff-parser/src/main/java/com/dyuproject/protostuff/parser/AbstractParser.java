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

import java.io.InputStream;
import java.nio.ByteBuffer;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Parser;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.TokenStream;

/**
 * Base parser
 * 
 * @author David Yu
 * @created Dec 16, 2009
 */
public abstract class AbstractParser extends Parser
{

    protected AbstractParser(TokenStream input)
    {
        super(input);
    }

    /**
     * Create a new parser instance, pre-supplying the input token stream and
     * the shared state.
     * 
     * This is only used when a grammar is imported into another grammar, but we
     * must supply this constructor to satisfy the super class contract.
     * 
     * @param input
     *            The stream of tokesn that will be pulled from the lexer
     * @param state
     *            The shared state object created by an interconnectd grammar
     */
    protected AbstractParser(TokenStream input, RecognizerSharedState state)
    {
        super(input,state);
    }

    /**
     * Creates the error/warning message that we need to show users/IDEs when
     * ANTLR has found a parsing error, has recovered from it and is now telling
     * us that a parsing exception occurred.
     * 
     * @param tokenNames
     *            token names as known by ANTLR (which we ignore)
     * @param e
     *            The exception that was thrown
     */
    public void displayRecognitionError(String[] tokenNames, RecognitionException e)
    {

        // This is just a place holder that shows how to override this method
        //
        super.displayRecognitionError(tokenNames, e);
    }
    
    public static void load(InputStream in, Proto proto) throws Exception
    {
        // Create an input character stream from standard in
        ANTLRInputStream input = new ANTLRInputStream(in);
        // Create an ExprLexer that feeds from that stream
        ProtoLexer lexer = new ProtoLexer(input);
        // Create a stream of tokens fed by the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // Create a parser that feeds off the token stream
        ProtoParser parser = new ProtoParser(tokens);
        // Begin parsing at rule prog
        parser.parse(proto);
    }
    
    static String getStringFromStringLiteral(String literal)
    {
        return getString(literal.substring(1, literal.length()-1));
    }
    
    static String getString(String value)
    {
        return TextFormat.unescapeText(value);
    }
    
    static byte[] getBytesFromStringLiteral(String literal)
    {
        return getBytes(literal.substring(1, literal.length()-1));
    }
    
    static byte[] getBytes(String value)
    {
        ByteBuffer buffer = TextFormat.unescapeBytes(value);
        byte[] buf = new byte[buffer.limit()];
        buffer.get(buf);
        return buf;
    }
    
    static byte[] getBytesFromHexString(String value)
    {
        int start = value.startsWith("0x") ? 2 : 0;
        int len = value.length()-start;
        if(len%2!=0)
            throw new IllegalArgumentException("malformed hex string: " + value);
        
        byte[] out = new byte[len/2];
        for(int i=0; i<out.length;)
        {
            int left = decimalFromHex(value.charAt(start++));
            int right = decimalFromHex(value.charAt(start++));
            out[i++] = (byte)((right & 0x0F) | (left << 4 & 0xF0));
        }
        return out;
    }
    
    static int decimalFromHex(char c)
    {
        switch(c)
        {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'a':
            case 'A':
                return 10;
            case 'b':
            case 'B':
                return 11;
            case 'c':
            case 'C':
                return 12;
            case 'd':
            case 'D':
                return 13;
            case 'e':
            case 'E':
                return 14;
            case 'f':
            case 'F':
                return 15;
            default:
                throw new IllegalArgumentException("Not a hex character: " + c);
        }
    }
}
