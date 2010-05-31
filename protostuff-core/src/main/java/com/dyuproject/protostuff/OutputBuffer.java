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


/**
 * A buffer that wraps a byte array and has a reference to the next node.
 * 
 * @author David Yu
 * @created May 18, 2010
 */
final class OutputBuffer
{

    final byte[] buffer;
    
    final int start;
    
    int offset;

    OutputBuffer next;
    
    // view
    OutputBuffer(OutputBuffer viewSource)
    {
        buffer = viewSource.buffer;
        offset = start = viewSource.offset;
    }
    
    OutputBuffer(byte[] buffer)
    {
        this.buffer = buffer;
        offset = start = 0;
    }
    
    // links this node to the given output buffer.
    OutputBuffer(byte[] buffer, OutputBuffer ob)
    {
        this(buffer);
        ob.next = this;
    }
    


}
