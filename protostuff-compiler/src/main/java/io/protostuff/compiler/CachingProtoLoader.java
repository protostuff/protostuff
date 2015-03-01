//================================================================================
//Copyright (c) 2011, David Yu
//All rights reserved.
//--------------------------------------------------------------------------------
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
// 1. Redistributions of source code must retain the above copyright notice,
//    this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
// 3. Neither the name of protostuff nor the names of its contributors may be used
//    to endorse or promote products derived from this software without
//    specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.
//================================================================================

package io.protostuff.compiler;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.protostuff.parser.DefaultProtoLoader;
import io.protostuff.parser.Proto;

/**
 * A proto loader that caches the protos for re-use.
 * 
 * @author David Yu
 * @created Dec 4, 2011
 */
public class CachingProtoLoader extends DefaultProtoLoader
{
    public final Map<String, Proto> loadedProtos;

    public CachingProtoLoader()
    {
        this(new HashMap<String, Proto>());
    }

    public CachingProtoLoader(Map<String, Proto> loadedProtos)
    {
        this.loadedProtos = loadedProtos;
    }

    public Collection<Proto> getCachedProtos()
    {
        return loadedProtos.values();
    }

    @Override
    public Proto loadFrom(File file, Proto importer) throws Exception
    {
        String key = file.getCanonicalPath();
        Proto proto = loadedProtos.get(key);
        if (proto == null)
            loadedProtos.put(key, proto = super.loadFrom(file, null));

        return proto;
    }

    @Override
    public Proto loadFrom(URL resource, Proto importer) throws Exception
    {
        String key = resource.toExternalForm();
        Proto proto = loadedProtos.get(key);
        if (proto == null)
            loadedProtos.put(key, proto = super.loadFrom(resource, null));

        return proto;
    }
}