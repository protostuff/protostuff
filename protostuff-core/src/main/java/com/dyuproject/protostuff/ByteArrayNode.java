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

package com.dyuproject.protostuff;


/**
 * A node that wraps a byte array and has a reference to the next node.
 * 
 * This is used as a single-direction linked list of byte[] wrappers.
 *
 * @author David Yu
 * @created Nov 12, 2009
 */
class ByteArrayNode
{
    
    final byte[] bytes;
    ByteArrayNode next;
    
    ByteArrayNode(byte[] bytes)
    {
        this.bytes = bytes;
    }
    
    ByteArrayNode(byte[] bytes, ByteArrayNode node)
    {
        this.bytes = bytes;
        node.next = this;
    }

}
