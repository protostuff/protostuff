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
 * The base io exception for all formats (protobuf/json/xml).
 * 
 * @author David Yu
 * @created May 25, 2010
 */
public class ProtostuffException extends IOException
{

    private static final long serialVersionUID = 3969366848110070516L;

    public ProtostuffException()
    {
        super();
    }

    public ProtostuffException(String message)
    {
        super(message);
    }

    public ProtostuffException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ProtostuffException(Throwable cause)
    {
        super(cause);
    }

}
