//========================================================================
//Copyright 2012 David Yu
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

package com.dyuproject.protostuff.me;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Delimiter tests for the graph format
 * 
 * @author David Yu
 * @created Aug 29, 2012
 */
public class GraphDelimiterTest extends DelimiterTest
{
    
    protected int writeDelimitedTo(OutputStream out, Object message, Schema schema,
            LinkedBuffer buffer) throws IOException
    {
        return GraphIOUtil.writeDelimitedTo(out, message, schema, buffer);
    }
    
    protected void mergeDelimitedFrom(InputStream in, Object message, Schema schema,
            LinkedBuffer buffer) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, message, schema, buffer);
    }
    
    protected int optWriteDelimitedTo(OutputStream out, Object message, Schema schema,
            LinkedBuffer buffer) throws IOException
    {
        return GraphIOUtil.optWriteDelimitedTo(out, message, schema, buffer);
    }
    
    protected boolean optMergeDelimitedFrom(InputStream in, Object message, Schema schema,
            LinkedBuffer buffer) throws IOException
    {
        return GraphIOUtil.optMergeDelimitedFrom(in, message, schema, buffer);
    }

}
