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

package io.protostuff;

import java.io.IOException;

/**
 * Transfers data from an {@link Input} to an {@link Output}.
 * <p>
 * It is recommended to use pipe only to stream data coming from server-side services (e.g from your datastore/etc).
 * <p>
 * Incoming data from the interwebs should not be piped due to validation/security purposes.
 * 
 * @author David Yu
 * @created Oct 6, 2010
 */
public abstract class Pipe
{

    protected Input input;
    protected Output output;

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
     * <p>
     * If {@code cleanupOnly} is true, the io processing ended prematurely hence the underlying pipe should
     * cleanup/close all resources that need to be.
     */
    protected abstract void end(Pipe.Schema<?> pipeSchema, Input input,
            boolean cleanupOnly) throws IOException;

    /**
     * Schema for transferring data from a source ({@link Input}) to a different sink ({@link Output}).
     */
    public static abstract class Schema<T> implements io.protostuff.Schema<Pipe>
    {

        public final io.protostuff.Schema<T> wrappedSchema;

        public Schema(io.protostuff.Schema<T> wrappedSchema)
        {
            this.wrappedSchema = wrappedSchema;
        }

        @Override
        public String getFieldName(int number)
        {
            return wrappedSchema.getFieldName(number);
        }

        @Override
        public int getFieldNumber(String name)
        {
            return wrappedSchema.getFieldNumber(name);
        }

        /**
         * Always returns true since we're just transferring data.
         */
        @Override
        public boolean isInitialized(Pipe message)
        {
            return true;
        }

        @Override
        public String messageFullName()
        {
            return wrappedSchema.messageFullName();
        }

        @Override
        public String messageName()
        {
            return wrappedSchema.messageName();
        }

        @Override
        public Pipe newMessage()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<Pipe> typeClass()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public final void writeTo(final Output output, final Pipe pipe) throws IOException
        {
            if (pipe.output == null)
            {
                pipe.output = output;

                // begin message pipe
                final Input input = pipe.begin(this);

                if (input == null)
                {
                    // empty message pipe.
                    pipe.output = null;
                    pipe.end(this, input, true);
                    return;
                }

                pipe.input = input;

                boolean transferComplete = false;
                try
                {
                    transfer(pipe, input, output);
                    transferComplete = true;
                }
                finally
                {
                    pipe.end(this, input, !transferComplete);
                    // pipe.input = null;
                    // pipe.output = null;
                }

                return;
            }

            // nested message.
            pipe.input.mergeObject(pipe, this);
        }

        @Override
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

    /**
     * This should not be called directly by applications.
     */
    public static <T> void transferDirect(Pipe.Schema<T> pipeSchema, Pipe pipe,
            Input input, Output output) throws IOException
    {
        pipeSchema.transfer(pipe, input, output);
    }

}
