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

lexer grammar ProtoLexer;

options {

   language=Java;  // Default

   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and 
   // variables will be placed.
   //
   superClass = AbstractLexer;
}

// What package should the generated source exist in?
//
@header {
    package com.dyuproject.protostuff.parser;
}
    
ASSIGN
    :   '='
    ;
    
LEFTCURLY
    :   '{'
    ;
    
RIGHTCURLY
    :   '}'
    ;
    
LEFTPAREN
    :   '('
    ;
    
RIGHTPAREN
    :   ')'
    ;
    
LEFTSQUARE
    :   '['
    ;
    
RIGHTSQUARE
    :   ']'
    ;

SEMICOLON
    :   ';'
    ;
    
COMMA
    :   ','
    ;
    
PLUS
    :   '+'
    ;
    
MINUS
    :   '-'
    ;
    
TO
    :   'to'
    ;

TRUE
    :   'true'
    ;
    
FALSE
    :   'false'
    ;
    
PKG 
    :   'package'
    ;

SYNTAX
    :   'syntax'
    ;
    
IMPORT
    :   'import'
    ;
    
OPTION
    :   'option'
    ;
    
MESSAGE
    :   'message'
    ;

SERVICE
    :   'service'
    ;
    
ENUM
    :   'enum'
    ;

REQUIRED
    :   'required'
    ;
    
OPTIONAL
    :   'optional'
    ;

REPEATED
    :   'repeated'
    ;
    
EXTENSIONS
    :   'extensions'
    ;
    
EXTEND
    :   'extend'
    ;
    
GROUP
    :   'group'
    ;
    
RPC
    :   'rpc'
    ;
    
RETURNS
    :   'returns'
    ;
    
INT32
    :   'int32'
    ;
    
INT64
    :   'int64'
    ;
    
UINT32
    :   'uint32'
    ;
    
UINT64
    :   'uint64'
    ;
    
SINT32
    :   'sint32'
    ;
    
SINT64
    :   'sint64'
    ;
    
FIXED32
    :   'fixed32'
    ;
    
FIXED64
    :   'fixed64'
    ;
    
SFIXED32
    :   'sfixed32'
    ;
    
SFIXED64
    :   'sfixed64'
    ;
    
FLOAT
    :   'float'
    ;
    
DOUBLE
    :   'double'
    ;
    
BOOL
    :   'bool'
    ;
    
STRING
    :   'string'
    ;
    
BYTES
    :   'bytes'
    ;
    
DEFAULT
    :   'default'
    ;

FULL_ID
    : ID ('.' ID)+
    ;

ID  
    :   ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'0'..'9'|'_')*
    ;
    
EXP
    :   NUMINT ('e'|'E') NUMINT
    ;
    
NUMDOUBLE
    :   (NUMFLOAT|NUMINT)
        'e' '0'..'9'+
    ;

NUMFLOAT
    :   NUMINT '.' '0'..'9'+ 'f'?
    ;
 
NUMINT
    :   '0' | MINUS? '1'..'9' '0'..'9'*
    ;
    
HEX
    :   MINUS? '0' ('x'|'X') HEX_DIGIT+
    ;
    
OCTAL
    :   '0' ('0'..'7')+
    ;
    
COMMENT
    :   '//' ~('\n'|'\r')* '\r'? '\n' {skip();}
    |   '/*' ( options {greedy=false;} : . )* '*/' {skip();}
    ;

WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {skip();}
    ;
    
STRING_LITERAL
    :   '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

fragment HEX_DIGIT
    :   ('0'..'9'|'a'..'f'|'A'..'F')
    ;

fragment ESC_SEQ
    :   '\\' ('a'|'v'|'b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   '\\' ('x'|'X') HEX_DIGIT HEX_DIGIT
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
