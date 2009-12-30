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

import org.antlr.runtime.CharStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.RecognizerSharedState;

/**
 * Base lexer.
 *
 * @author David Yu
 * @created Dec 16, 2009
 */
public abstract class AbstractLexer extends Lexer
{
    
    /**
     * Default constructor for the lexer, when you do not yet know what the
     * character stream to be provided is.
     */
    public AbstractLexer()
    {
    }

    /**
     * Create a new instance of the lexer using the given character stream as
     * the input to lex into tokens.
     * 
     * @param input
     *            A valid character stream that contains the ruleSrc code you
     *            wish to compile (or lex at least)
     */
    public AbstractLexer(CharStream input)
    {
        this(input,new RecognizerSharedState());
    }

    /**
     * Internal constructor for ANTLR - do not use.
     * 
     * @param input
     *            The character stream we are going to lex
     * @param state
     *            The shared state object, shared between all lexer comonents
     */
    public AbstractLexer(CharStream input, RecognizerSharedState state)
    {
        super(input,state);
    }

}
