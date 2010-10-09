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

import java.io.IOException;

/**
 * Transfers data from an {@link Input} to an {@link Output}.
 * 
 * It is recommended to use pipe only to stream data coming from server-side 
 * services (e.g from your datastore/etc).
 * 
 * Incoming data from the interwebs should not be piped due to 
 * validation/security purposes.
 *
 * @author David Yu
 * @created Oct 6, 2010
 */
public abstract class Pipe
{
    
    Input input;
    Output output;
    
    /**
     * Resets this pipe for re-use.
     */
    protected Pipe reset()
    {
        output = null;
        input = null;
        return this;
    }
    
    /**
     * Begin preliminary input processing.
     */
    protected abstract Input begin(Pipe.Schema<?> pipeSchema) throws IOException;
    
    /**
     * End input processing.
     */
    protected abstract void end(Pipe.Schema<?> pipeSchema, Input input) throws IOException;
    
    /**
     * Schema for transferring data from a source ({@link Input}) to a 
     * different sink ({@link Output}).
     */
    public static abstract class Schema<T> implements com.dyuproject.protostuff.Schema<Pipe>
    {
        
        public final com.dyuproject.protostuff.Schema<T> wrappedSchema;
        
        public Schema(com.dyuproject.protostuff.Schema<T> wrappedSchema)
        {
            this.wrappedSchema = wrappedSchema;
        }

        public String getFieldName(int number)
        {
            return wrappedSchema.getFieldName(number);
        }

        public int getFieldNumber(String name)
        {
            return wrappedSchema.getFieldNumber(name);
        }

        public boolean isInitialized(Pipe message)
        {
            return true;
        }

        public String messageFullName()
        {
            return wrappedSchema.messageFullName();
        }

        public String messageName()
        {
            return wrappedSchema.messageName();
        }

        public Pipe newMessage()
        {
            throw new UnsupportedOperationException();
        }

        public Class<Pipe> typeClass()
        {
            throw new UnsupportedOperationException();
        }

        public final void writeTo(final Output output, final Pipe pipe) throws IOException
        {
            if(pipe.output == null)
            {
                // begin message pipe
                final Input input = pipe.begin(this);
                
                if(input == null)
                {
                    // empty message pipe.
                    return;
                }
                
                pipe.input = input;
                pipe.output = output;

                transfer(pipe, input, output);
                
                pipe.end(this, input);
                
                //pipe.input = null;
                //pipe.output = null;
                
                return;
            }

            // nested message.
            pipe.input.mergeObject(pipe, this);
        }

        public final void mergeFrom(final Input input, final Pipe pipe) throws IOException
        {
            transfer(pipe, input, pipe.output);
        }
        
        /**
         * Transfer data from the {@link Input} to the {@link Output}.
         */
        protected abstract void transfer(Pipe pipe, Input input, Output output) 
        throws IOException;

    }

}
