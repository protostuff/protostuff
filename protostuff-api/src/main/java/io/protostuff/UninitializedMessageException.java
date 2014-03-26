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
 * Thrown when attempting to build a protocol message that is missing required fields. This is a
 * {@code RuntimeException} because it normally represents a programming error: it happens when some code which
 * constructs a message fails to set all the fields.
 * 
 * @author kenton@google.com Kenton Varda
 * @author David Yu
 */
public final class UninitializedMessageException extends RuntimeException
{
    private static final long serialVersionUID = -7466929953374883507L;

    public final Object targetMessage;
    public final Schema<?> targetSchema;

    public UninitializedMessageException(Message<?> targetMessage)
    {
        this(targetMessage, targetMessage.cachedSchema());
    }

    public UninitializedMessageException(Object targetMessage, Schema<?> targetSchema)
    {
        this.targetMessage = targetMessage;
        this.targetSchema = targetSchema;
    }

    public UninitializedMessageException(String msg, Message<?> targetMessage)
    {
        this(msg, targetMessage, targetMessage.cachedSchema());
    }

    public UninitializedMessageException(String msg, Object targetMessage,
            Schema<?> targetSchema)
    {
        super(msg);
        this.targetMessage = targetMessage;
        this.targetSchema = targetSchema;
    }

    @SuppressWarnings("unchecked")
    public <T> T getTargetMessage()
    {
        return (T) targetMessage;
    }

    @SuppressWarnings("unchecked")
    public <T> Schema<T> getTargetSchema()
    {
        return (Schema<T>) targetSchema;
    }

    /*
     * @public UninitializedMessageException(final MessageLite message) {
     * super("Message was missing required fields.  (Lite runtime could not " +
     * "determine which fields were missing)."); missingFields = null; }
     * 
     * public UninitializedMessageException(final List<String> missingFields) { super(buildDescription(missingFields));
     * this.missingFields = missingFields; }
     * 
     * private final List<String> missingFields;
     * 
     * /** Get a list of human-readable names of required fields missing from this message. Each name is a full path to
     * a field, e.g. "foo.bar[5].baz". Returns null if the lite runtime was used, since it lacks the ability to find
     * missing fields.
     * 
     * @ public List<String> getMissingFields() { return Collections.unmodifiableList(missingFields); }
     * 
     * /** Converts this exception to an {@link InvalidProtocolBufferException}. When a parsed message is missing
     * required fields, this should be thrown instead of {@code UninitializedMessageException}.
     * 
     * @ public InvalidProtocolBufferException asInvalidProtocolBufferException() { return new
     * InvalidProtocolBufferException(getMessage()); }
     * 
     * /** Construct the description string for this exception. *@ private static String buildDescription(final
     * List<String> missingFields) { final StringBuilder description = new
     * StringBuilder("Message missing required fields: "); boolean first = true; for (final String field :
     * missingFields) { if (first) { first = false; } else { description.append(", "); } description.append(field); }
     * return description.toString(); }
     */
}
