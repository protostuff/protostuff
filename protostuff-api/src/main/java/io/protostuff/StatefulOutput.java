//========================================================================
//Copyright 2007-2011 David Yu dyuproject@gmail.com
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

/**
 * An output that keeps the state of the schema being used.
 * 
 * @author David Yu
 * @created Jan 24, 2011
 */
public interface StatefulOutput extends Output
{

    /**
     * Updates the schema if {@code lastSchema} was indeed the last schema used.
     */
    public void updateLast(Schema<?> schema, Schema<?> lastSchema);

}
