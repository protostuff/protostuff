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

// Protocol Buffers - Google's data interchange format
// Copyright 2008 Google Inc.  All rights reserved.
// http://code.google.com/p/protobuf/
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are
// met:
//
//     * Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above
// copyright notice, this list of conditions and the following disclaimer
// in the documentation and/or other materials provided with the
// distribution.
//     * Neither the name of Google Inc. nor the names of its
// contributors may be used to endorse or promote products derived from
// this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package io.protostuff;

/**
 * Thrown when a protocol message being parsed is invalid in some way, e.g. it contains a malformed varint or a negative
 * byte length.
 * 
 * @author kenton@google.com Kenton Varda
 * @author David Yu
 */
public class ProtobufException extends ProtostuffException
{
    private static final long serialVersionUID = 1616151763072450476L;

    private static final String ERR_TRUNCATED_MESSAGE =
            "While parsing a protocol message, the input ended unexpectedly " +
                    "in the middle of a field.  This could mean either than the " +
                    "input has been truncated or that an embedded message " +
                    "misreported its own length.";

    public ProtobufException(final String description)
    {
        super(description);
    }

    public ProtobufException(final String description, final Throwable cause)
    {
        super(description, cause);
    }

    static ProtobufException truncatedMessage(final Throwable cause)
    {
        return new ProtobufException(ERR_TRUNCATED_MESSAGE, cause);
    }

    static ProtobufException truncatedMessage()
    {
        return new ProtobufException(ERR_TRUNCATED_MESSAGE);
    }

    static ProtobufException misreportedSize()
    {
        return new ProtobufException(
                "CodedInput encountered an embedded string or bytes " +
                        "that misreported its size.");
    }

    static ProtobufException negativeSize()
    {
        return new ProtobufException(
                "CodedInput encountered an embedded string or message " +
                        "which claimed to have negative size.");
    }

    static ProtobufException malformedVarint()
    {
        return new ProtobufException(
                "CodedInput encountered a malformed varint.");
    }

    static ProtobufException invalidTag()
    {
        return new ProtobufException(
                "Protocol message contained an invalid tag (zero).");
    }

    static ProtobufException invalidEndTag()
    {
        return new ProtobufException(
                "Protocol message end-group tag did not match expected tag.");
    }

    static ProtobufException invalidWireType()
    {
        return new ProtobufException(
                "Protocol message tag had invalid wire type.");
    }

    static ProtobufException recursionLimitExceeded()
    {
        return new ProtobufException(
                "Protocol message had too many levels of nesting.  May be malicious.  " +
                        "Use CodedInput.setRecursionLimit() to increase the depth limit.");
    }

    static ProtobufException sizeLimitExceeded()
    {
        return new ProtobufException(
                "Protocol message was too large.  May be malicious.  " +
                        "Use CodedInput.setSizeLimit() to increase the size limit.");
    }
}
