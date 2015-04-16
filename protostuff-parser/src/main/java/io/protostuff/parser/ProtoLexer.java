// $ANTLR 3.2 Sep 23, 2009 14:05:07 io/protostuff/parser/ProtoLexer.g 2015-04-16 09:46:11

    package io.protostuff.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class ProtoLexer extends AbstractLexer {
    public static final int OPTION=22;
    public static final int DOC=61;
    public static final int FULL_ID=53;
    public static final int OCTAL_ESC=67;
    public static final int PKG=19;
    public static final int NUMFLOAT=56;
    public static final int MAX=50;
    public static final int FLOAT=44;
    public static final int FIXED64=41;
    public static final int ID=52;
    public static final int EOF=-1;
    public static final int AT=5;
    public static final int LEFTPAREN=8;
    public static final int SYNTAX=20;
    public static final int EXTEND=30;
    public static final int IMPORT=21;
    public static final int STRING_LITERAL=65;
    public static final int ESC_SEQ=64;
    public static final int EXTENSIONS=29;
    public static final int SINT64=39;
    public static final int LEFTCURLY=6;
    public static final int EXP=55;
    public static final int HEX=59;
    public static final int COMMA=13;
    public static final int FIXED32=40;
    public static final int RIGHTCURLY=7;
    public static final int SFIXED32=42;
    public static final int DOUBLE=45;
    public static final int MESSAGE=23;
    public static final int PLUS=14;
    public static final int VOID=51;
    public static final int NUMDOUBLE=57;
    public static final int COMMENT=62;
    public static final int NUMINT=54;
    public static final int SINT32=38;
    public static final int RETURNS=33;
    public static final int TO=16;
    public static final int INT64=35;
    public static final int RIGHTSQUARE=11;
    public static final int UNICODE_ESC=66;
    public static final int DEFAULT=49;
    public static final int BOOL=46;
    public static final int REPEATED=28;
    public static final int HEX_DIGIT=58;
    public static final int SEMICOLON=12;
    public static final int MINUS=15;
    public static final int REQUIRED=26;
    public static final int TRUE=17;
    public static final int UINT64=37;
    public static final int OPTIONAL=27;
    public static final int INT32=34;
    public static final int GROUP=31;
    public static final int WS=63;
    public static final int ENUM=25;
    public static final int SERVICE=24;
    public static final int LEFTSQUARE=10;
    public static final int RIGHTPAREN=9;
    public static final int SFIXED64=43;
    public static final int BYTES=48;
    public static final int ASSIGN=4;
    public static final int OCTAL=60;
    public static final int RPC=32;
    public static final int UINT32=36;
    public static final int FALSE=18;
    public static final int STRING=47;

    // delegates
    // delegators

    public ProtoLexer() {;} 
    public ProtoLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public ProtoLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "io/protostuff/parser/ProtoLexer.g"; }

    // $ANTLR start "ASSIGN"
    public final void mASSIGN() throws RecognitionException {
        try {
            int _type = ASSIGN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:35:5: ( '=' )
            // io/protostuff/parser/ProtoLexer.g:35:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ASSIGN"

    // $ANTLR start "AT"
    public final void mAT() throws RecognitionException {
        try {
            int _type = AT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:39:5: ( '@' )
            // io/protostuff/parser/ProtoLexer.g:39:9: '@'
            {
            match('@'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AT"

    // $ANTLR start "LEFTCURLY"
    public final void mLEFTCURLY() throws RecognitionException {
        try {
            int _type = LEFTCURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:43:5: ( '{' )
            // io/protostuff/parser/ProtoLexer.g:43:9: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEFTCURLY"

    // $ANTLR start "RIGHTCURLY"
    public final void mRIGHTCURLY() throws RecognitionException {
        try {
            int _type = RIGHTCURLY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:47:5: ( '}' )
            // io/protostuff/parser/ProtoLexer.g:47:9: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RIGHTCURLY"

    // $ANTLR start "LEFTPAREN"
    public final void mLEFTPAREN() throws RecognitionException {
        try {
            int _type = LEFTPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:51:5: ( '(' )
            // io/protostuff/parser/ProtoLexer.g:51:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEFTPAREN"

    // $ANTLR start "RIGHTPAREN"
    public final void mRIGHTPAREN() throws RecognitionException {
        try {
            int _type = RIGHTPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:55:5: ( ')' )
            // io/protostuff/parser/ProtoLexer.g:55:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RIGHTPAREN"

    // $ANTLR start "LEFTSQUARE"
    public final void mLEFTSQUARE() throws RecognitionException {
        try {
            int _type = LEFTSQUARE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:59:5: ( '[' )
            // io/protostuff/parser/ProtoLexer.g:59:9: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LEFTSQUARE"

    // $ANTLR start "RIGHTSQUARE"
    public final void mRIGHTSQUARE() throws RecognitionException {
        try {
            int _type = RIGHTSQUARE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:63:5: ( ']' )
            // io/protostuff/parser/ProtoLexer.g:63:9: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RIGHTSQUARE"

    // $ANTLR start "SEMICOLON"
    public final void mSEMICOLON() throws RecognitionException {
        try {
            int _type = SEMICOLON;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:67:5: ( ';' )
            // io/protostuff/parser/ProtoLexer.g:67:9: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEMICOLON"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:71:5: ( ',' )
            // io/protostuff/parser/ProtoLexer.g:71:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "PLUS"
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:75:5: ( '+' )
            // io/protostuff/parser/ProtoLexer.g:75:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PLUS"

    // $ANTLR start "MINUS"
    public final void mMINUS() throws RecognitionException {
        try {
            int _type = MINUS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:79:5: ( '-' )
            // io/protostuff/parser/ProtoLexer.g:79:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MINUS"

    // $ANTLR start "TO"
    public final void mTO() throws RecognitionException {
        try {
            int _type = TO;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:83:5: ( 'to' )
            // io/protostuff/parser/ProtoLexer.g:83:9: 'to'
            {
            match("to"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TO"

    // $ANTLR start "TRUE"
    public final void mTRUE() throws RecognitionException {
        try {
            int _type = TRUE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:87:5: ( 'true' )
            // io/protostuff/parser/ProtoLexer.g:87:9: 'true'
            {
            match("true"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TRUE"

    // $ANTLR start "FALSE"
    public final void mFALSE() throws RecognitionException {
        try {
            int _type = FALSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:91:5: ( 'false' )
            // io/protostuff/parser/ProtoLexer.g:91:9: 'false'
            {
            match("false"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FALSE"

    // $ANTLR start "PKG"
    public final void mPKG() throws RecognitionException {
        try {
            int _type = PKG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:95:5: ( 'package' )
            // io/protostuff/parser/ProtoLexer.g:95:9: 'package'
            {
            match("package"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PKG"

    // $ANTLR start "SYNTAX"
    public final void mSYNTAX() throws RecognitionException {
        try {
            int _type = SYNTAX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:99:5: ( 'syntax' )
            // io/protostuff/parser/ProtoLexer.g:99:9: 'syntax'
            {
            match("syntax"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SYNTAX"

    // $ANTLR start "IMPORT"
    public final void mIMPORT() throws RecognitionException {
        try {
            int _type = IMPORT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:103:5: ( 'import' )
            // io/protostuff/parser/ProtoLexer.g:103:9: 'import'
            {
            match("import"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IMPORT"

    // $ANTLR start "OPTION"
    public final void mOPTION() throws RecognitionException {
        try {
            int _type = OPTION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:107:5: ( 'option' )
            // io/protostuff/parser/ProtoLexer.g:107:9: 'option'
            {
            match("option"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OPTION"

    // $ANTLR start "MESSAGE"
    public final void mMESSAGE() throws RecognitionException {
        try {
            int _type = MESSAGE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:111:5: ( 'message' )
            // io/protostuff/parser/ProtoLexer.g:111:9: 'message'
            {
            match("message"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MESSAGE"

    // $ANTLR start "SERVICE"
    public final void mSERVICE() throws RecognitionException {
        try {
            int _type = SERVICE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:115:5: ( 'service' )
            // io/protostuff/parser/ProtoLexer.g:115:9: 'service'
            {
            match("service"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SERVICE"

    // $ANTLR start "ENUM"
    public final void mENUM() throws RecognitionException {
        try {
            int _type = ENUM;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:119:5: ( 'enum' )
            // io/protostuff/parser/ProtoLexer.g:119:9: 'enum'
            {
            match("enum"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ENUM"

    // $ANTLR start "REQUIRED"
    public final void mREQUIRED() throws RecognitionException {
        try {
            int _type = REQUIRED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:123:5: ( 'required' )
            // io/protostuff/parser/ProtoLexer.g:123:9: 'required'
            {
            match("required"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "REQUIRED"

    // $ANTLR start "OPTIONAL"
    public final void mOPTIONAL() throws RecognitionException {
        try {
            int _type = OPTIONAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:127:5: ( 'optional' )
            // io/protostuff/parser/ProtoLexer.g:127:9: 'optional'
            {
            match("optional"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OPTIONAL"

    // $ANTLR start "REPEATED"
    public final void mREPEATED() throws RecognitionException {
        try {
            int _type = REPEATED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:131:5: ( 'repeated' )
            // io/protostuff/parser/ProtoLexer.g:131:9: 'repeated'
            {
            match("repeated"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "REPEATED"

    // $ANTLR start "EXTENSIONS"
    public final void mEXTENSIONS() throws RecognitionException {
        try {
            int _type = EXTENSIONS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:135:5: ( 'extensions' )
            // io/protostuff/parser/ProtoLexer.g:135:9: 'extensions'
            {
            match("extensions"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EXTENSIONS"

    // $ANTLR start "EXTEND"
    public final void mEXTEND() throws RecognitionException {
        try {
            int _type = EXTEND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:139:5: ( 'extend' )
            // io/protostuff/parser/ProtoLexer.g:139:9: 'extend'
            {
            match("extend"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EXTEND"

    // $ANTLR start "GROUP"
    public final void mGROUP() throws RecognitionException {
        try {
            int _type = GROUP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:143:5: ( 'group' )
            // io/protostuff/parser/ProtoLexer.g:143:9: 'group'
            {
            match("group"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "GROUP"

    // $ANTLR start "RPC"
    public final void mRPC() throws RecognitionException {
        try {
            int _type = RPC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:147:5: ( 'rpc' )
            // io/protostuff/parser/ProtoLexer.g:147:9: 'rpc'
            {
            match("rpc"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RPC"

    // $ANTLR start "RETURNS"
    public final void mRETURNS() throws RecognitionException {
        try {
            int _type = RETURNS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:151:5: ( 'returns' )
            // io/protostuff/parser/ProtoLexer.g:151:9: 'returns'
            {
            match("returns"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RETURNS"

    // $ANTLR start "INT32"
    public final void mINT32() throws RecognitionException {
        try {
            int _type = INT32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:155:5: ( 'int32' )
            // io/protostuff/parser/ProtoLexer.g:155:9: 'int32'
            {
            match("int32"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT32"

    // $ANTLR start "INT64"
    public final void mINT64() throws RecognitionException {
        try {
            int _type = INT64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:159:5: ( 'int64' )
            // io/protostuff/parser/ProtoLexer.g:159:9: 'int64'
            {
            match("int64"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "INT64"

    // $ANTLR start "UINT32"
    public final void mUINT32() throws RecognitionException {
        try {
            int _type = UINT32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:163:5: ( 'uint32' )
            // io/protostuff/parser/ProtoLexer.g:163:9: 'uint32'
            {
            match("uint32"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UINT32"

    // $ANTLR start "UINT64"
    public final void mUINT64() throws RecognitionException {
        try {
            int _type = UINT64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:167:5: ( 'uint64' )
            // io/protostuff/parser/ProtoLexer.g:167:9: 'uint64'
            {
            match("uint64"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UINT64"

    // $ANTLR start "SINT32"
    public final void mSINT32() throws RecognitionException {
        try {
            int _type = SINT32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:171:5: ( 'sint32' )
            // io/protostuff/parser/ProtoLexer.g:171:9: 'sint32'
            {
            match("sint32"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SINT32"

    // $ANTLR start "SINT64"
    public final void mSINT64() throws RecognitionException {
        try {
            int _type = SINT64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:175:5: ( 'sint64' )
            // io/protostuff/parser/ProtoLexer.g:175:9: 'sint64'
            {
            match("sint64"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SINT64"

    // $ANTLR start "FIXED32"
    public final void mFIXED32() throws RecognitionException {
        try {
            int _type = FIXED32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:179:5: ( 'fixed32' )
            // io/protostuff/parser/ProtoLexer.g:179:9: 'fixed32'
            {
            match("fixed32"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FIXED32"

    // $ANTLR start "FIXED64"
    public final void mFIXED64() throws RecognitionException {
        try {
            int _type = FIXED64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:183:5: ( 'fixed64' )
            // io/protostuff/parser/ProtoLexer.g:183:9: 'fixed64'
            {
            match("fixed64"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FIXED64"

    // $ANTLR start "SFIXED32"
    public final void mSFIXED32() throws RecognitionException {
        try {
            int _type = SFIXED32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:187:5: ( 'sfixed32' )
            // io/protostuff/parser/ProtoLexer.g:187:9: 'sfixed32'
            {
            match("sfixed32"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SFIXED32"

    // $ANTLR start "SFIXED64"
    public final void mSFIXED64() throws RecognitionException {
        try {
            int _type = SFIXED64;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:191:5: ( 'sfixed64' )
            // io/protostuff/parser/ProtoLexer.g:191:9: 'sfixed64'
            {
            match("sfixed64"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SFIXED64"

    // $ANTLR start "FLOAT"
    public final void mFLOAT() throws RecognitionException {
        try {
            int _type = FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:195:5: ( 'float' )
            // io/protostuff/parser/ProtoLexer.g:195:9: 'float'
            {
            match("float"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "DOUBLE"
    public final void mDOUBLE() throws RecognitionException {
        try {
            int _type = DOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:199:5: ( 'double' )
            // io/protostuff/parser/ProtoLexer.g:199:9: 'double'
            {
            match("double"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUBLE"

    // $ANTLR start "BOOL"
    public final void mBOOL() throws RecognitionException {
        try {
            int _type = BOOL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:203:5: ( 'bool' )
            // io/protostuff/parser/ProtoLexer.g:203:9: 'bool'
            {
            match("bool"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BOOL"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:207:5: ( 'string' )
            // io/protostuff/parser/ProtoLexer.g:207:9: 'string'
            {
            match("string"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "BYTES"
    public final void mBYTES() throws RecognitionException {
        try {
            int _type = BYTES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:211:5: ( 'bytes' )
            // io/protostuff/parser/ProtoLexer.g:211:9: 'bytes'
            {
            match("bytes"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BYTES"

    // $ANTLR start "DEFAULT"
    public final void mDEFAULT() throws RecognitionException {
        try {
            int _type = DEFAULT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:215:5: ( 'default' )
            // io/protostuff/parser/ProtoLexer.g:215:9: 'default'
            {
            match("default"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DEFAULT"

    // $ANTLR start "MAX"
    public final void mMAX() throws RecognitionException {
        try {
            int _type = MAX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:219:5: ( 'max' )
            // io/protostuff/parser/ProtoLexer.g:219:9: 'max'
            {
            match("max"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MAX"

    // $ANTLR start "VOID"
    public final void mVOID() throws RecognitionException {
        try {
            int _type = VOID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:223:5: ( 'void' )
            // io/protostuff/parser/ProtoLexer.g:223:9: 'void'
            {
            match("void"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "VOID"

    // $ANTLR start "FULL_ID"
    public final void mFULL_ID() throws RecognitionException {
        try {
            int _type = FULL_ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:227:5: ( ID ( '.' ID )+ )
            // io/protostuff/parser/ProtoLexer.g:227:7: ID ( '.' ID )+
            {
            mID(); 
            // io/protostuff/parser/ProtoLexer.g:227:10: ( '.' ID )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                switch ( input.LA(1) ) {
                case '.':
                    {
                    alt1=1;
                    }
                    break;

                }

                switch (alt1) {
            	case 1 :
            	    // io/protostuff/parser/ProtoLexer.g:227:11: '.' ID
            	    {
            	    match('.'); 
            	    mID(); 

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FULL_ID"

    // $ANTLR start "ID"
    public final void mID() throws RecognitionException {
        try {
            int _type = ID;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:231:5: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )* )
            // io/protostuff/parser/ProtoLexer.g:231:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // io/protostuff/parser/ProtoLexer.g:231:33: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '_' )*
            loop2:
            do {
                int alt2=2;
                switch ( input.LA(1) ) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case '_':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                    {
                    alt2=1;
                    }
                    break;

                }

                switch (alt2) {
            	case 1 :
            	    // io/protostuff/parser/ProtoLexer.g:
            	    {
            	    if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ID"

    // $ANTLR start "EXP"
    public final void mEXP() throws RecognitionException {
        try {
            int _type = EXP;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:235:5: ( NUMINT ( 'e' | 'E' ) NUMINT )
            // io/protostuff/parser/ProtoLexer.g:235:9: NUMINT ( 'e' | 'E' ) NUMINT
            {
            mNUMINT(); 
            if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            mNUMINT(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EXP"

    // $ANTLR start "NUMDOUBLE"
    public final void mNUMDOUBLE() throws RecognitionException {
        try {
            int _type = NUMDOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:239:5: ( ( NUMFLOAT | NUMINT ) 'e' ( '0' .. '9' )+ )
            // io/protostuff/parser/ProtoLexer.g:239:9: ( NUMFLOAT | NUMINT ) 'e' ( '0' .. '9' )+
            {
            // io/protostuff/parser/ProtoLexer.g:239:9: ( NUMFLOAT | NUMINT )
            int alt3=2;
            alt3 = dfa3.predict(input);
            switch (alt3) {
                case 1 :
                    // io/protostuff/parser/ProtoLexer.g:239:10: NUMFLOAT
                    {
                    mNUMFLOAT(); 

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoLexer.g:239:19: NUMINT
                    {
                    mNUMINT(); 

                    }
                    break;

            }

            match('e'); 
            // io/protostuff/parser/ProtoLexer.g:240:13: ( '0' .. '9' )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                switch ( input.LA(1) ) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    {
                    alt4=1;
                    }
                    break;

                }

                switch (alt4) {
            	case 1 :
            	    // io/protostuff/parser/ProtoLexer.g:240:13: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMDOUBLE"

    // $ANTLR start "NUMFLOAT"
    public final void mNUMFLOAT() throws RecognitionException {
        try {
            int _type = NUMFLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:244:5: ( NUMINT '.' ( '0' .. '9' )+ ( 'f' )? )
            // io/protostuff/parser/ProtoLexer.g:244:9: NUMINT '.' ( '0' .. '9' )+ ( 'f' )?
            {
            mNUMINT(); 
            match('.'); 
            // io/protostuff/parser/ProtoLexer.g:244:20: ( '0' .. '9' )+
            int cnt5=0;
            loop5:
            do {
                int alt5=2;
                switch ( input.LA(1) ) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    {
                    alt5=1;
                    }
                    break;

                }

                switch (alt5) {
            	case 1 :
            	    // io/protostuff/parser/ProtoLexer.g:244:20: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt5 >= 1 ) break loop5;
                        EarlyExitException eee =
                            new EarlyExitException(5, input);
                        throw eee;
                }
                cnt5++;
            } while (true);

            // io/protostuff/parser/ProtoLexer.g:244:30: ( 'f' )?
            int alt6=2;
            switch ( input.LA(1) ) {
                case 'f':
                    {
                    alt6=1;
                    }
                    break;
            }

            switch (alt6) {
                case 1 :
                    // io/protostuff/parser/ProtoLexer.g:244:30: 'f'
                    {
                    match('f'); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMFLOAT"

    // $ANTLR start "NUMINT"
    public final void mNUMINT() throws RecognitionException {
        try {
            int _type = NUMINT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:248:5: ( '0' | ( MINUS )? '1' .. '9' ( '0' .. '9' )* )
            int alt9=2;
            switch ( input.LA(1) ) {
            case '0':
                {
                alt9=1;
                }
                break;
            case '-':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                {
                alt9=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }

            switch (alt9) {
                case 1 :
                    // io/protostuff/parser/ProtoLexer.g:248:9: '0'
                    {
                    match('0'); 

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoLexer.g:248:15: ( MINUS )? '1' .. '9' ( '0' .. '9' )*
                    {
                    // io/protostuff/parser/ProtoLexer.g:248:15: ( MINUS )?
                    int alt7=2;
                    switch ( input.LA(1) ) {
                        case '-':
                            {
                            alt7=1;
                            }
                            break;
                    }

                    switch (alt7) {
                        case 1 :
                            // io/protostuff/parser/ProtoLexer.g:248:15: MINUS
                            {
                            mMINUS(); 

                            }
                            break;

                    }

                    matchRange('1','9'); 
                    // io/protostuff/parser/ProtoLexer.g:248:31: ( '0' .. '9' )*
                    loop8:
                    do {
                        int alt8=2;
                        switch ( input.LA(1) ) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            {
                            alt8=1;
                            }
                            break;

                        }

                        switch (alt8) {
                    	case 1 :
                    	    // io/protostuff/parser/ProtoLexer.g:248:31: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    break loop8;
                        }
                    } while (true);


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMINT"

    // $ANTLR start "HEX"
    public final void mHEX() throws RecognitionException {
        try {
            int _type = HEX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:252:5: ( ( MINUS )? '0' ( 'x' | 'X' ) ( HEX_DIGIT )+ )
            // io/protostuff/parser/ProtoLexer.g:252:9: ( MINUS )? '0' ( 'x' | 'X' ) ( HEX_DIGIT )+
            {
            // io/protostuff/parser/ProtoLexer.g:252:9: ( MINUS )?
            int alt10=2;
            switch ( input.LA(1) ) {
                case '-':
                    {
                    alt10=1;
                    }
                    break;
            }

            switch (alt10) {
                case 1 :
                    // io/protostuff/parser/ProtoLexer.g:252:9: MINUS
                    {
                    mMINUS(); 

                    }
                    break;

            }

            match('0'); 
            if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            // io/protostuff/parser/ProtoLexer.g:252:30: ( HEX_DIGIT )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                switch ( input.LA(1) ) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                    {
                    alt11=1;
                    }
                    break;

                }

                switch (alt11) {
            	case 1 :
            	    // io/protostuff/parser/ProtoLexer.g:252:30: HEX_DIGIT
            	    {
            	    mHEX_DIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HEX"

    // $ANTLR start "OCTAL"
    public final void mOCTAL() throws RecognitionException {
        try {
            int _type = OCTAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:256:5: ( '0' ( '0' .. '7' )+ )
            // io/protostuff/parser/ProtoLexer.g:256:9: '0' ( '0' .. '7' )+
            {
            match('0'); 
            // io/protostuff/parser/ProtoLexer.g:256:13: ( '0' .. '7' )+
            int cnt12=0;
            loop12:
            do {
                int alt12=2;
                switch ( input.LA(1) ) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    {
                    alt12=1;
                    }
                    break;

                }

                switch (alt12) {
            	case 1 :
            	    // io/protostuff/parser/ProtoLexer.g:256:14: '0' .. '7'
            	    {
            	    matchRange('0','7'); 

            	    }
            	    break;

            	default :
            	    if ( cnt12 >= 1 ) break loop12;
                        EarlyExitException eee =
                            new EarlyExitException(12, input);
                        throw eee;
                }
                cnt12++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OCTAL"

    // $ANTLR start "DOC"
    public final void mDOC() throws RecognitionException {
        try {
            int _type = DOC;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:260:5: ( '///' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
            // io/protostuff/parser/ProtoLexer.g:260:9: '///' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
            {
            match("///"); 

            // io/protostuff/parser/ProtoLexer.g:260:15: (~ ( '\\n' | '\\r' ) )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>='\u0000' && LA13_0<='\t')||(LA13_0>='\u000B' && LA13_0<='\f')||(LA13_0>='\u000E' && LA13_0<='\uFFFF')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // io/protostuff/parser/ProtoLexer.g:260:15: ~ ( '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

            // io/protostuff/parser/ProtoLexer.g:260:29: ( '\\r' )?
            int alt14=2;
            switch ( input.LA(1) ) {
                case '\r':
                    {
                    alt14=1;
                    }
                    break;
            }

            switch (alt14) {
                case 1 :
                    // io/protostuff/parser/ProtoLexer.g:260:29: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }

            match('\n'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOC"

    // $ANTLR start "COMMENT"
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:264:5: ( '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' | '/*' ( options {greedy=false; } : . )* '*/' )
            int alt18=2;
            switch ( input.LA(1) ) {
            case '/':
                {
                switch ( input.LA(2) ) {
                case '/':
                    {
                    alt18=1;
                    }
                    break;
                case '*':
                    {
                    alt18=2;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 18, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // io/protostuff/parser/ProtoLexer.g:264:9: '//' (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
                    {
                    match("//"); 

                    // io/protostuff/parser/ProtoLexer.g:264:14: (~ ( '\\n' | '\\r' ) )*
                    loop15:
                    do {
                        int alt15=2;
                        int LA15_0 = input.LA(1);

                        if ( ((LA15_0>='\u0000' && LA15_0<='\t')||(LA15_0>='\u000B' && LA15_0<='\f')||(LA15_0>='\u000E' && LA15_0<='\uFFFF')) ) {
                            alt15=1;
                        }


                        switch (alt15) {
                    	case 1 :
                    	    // io/protostuff/parser/ProtoLexer.g:264:14: ~ ( '\\n' | '\\r' )
                    	    {
                    	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFF') ) {
                    	        input.consume();

                    	    }
                    	    else {
                    	        MismatchedSetException mse = new MismatchedSetException(null,input);
                    	        recover(mse);
                    	        throw mse;}


                    	    }
                    	    break;

                    	default :
                    	    break loop15;
                        }
                    } while (true);

                    // io/protostuff/parser/ProtoLexer.g:264:28: ( '\\r' )?
                    int alt16=2;
                    switch ( input.LA(1) ) {
                        case '\r':
                            {
                            alt16=1;
                            }
                            break;
                    }

                    switch (alt16) {
                        case 1 :
                            // io/protostuff/parser/ProtoLexer.g:264:28: '\\r'
                            {
                            match('\r'); 

                            }
                            break;

                    }

                    match('\n'); 
                    skip();

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoLexer.g:265:9: '/*' ( options {greedy=false; } : . )* '*/'
                    {
                    match("/*"); 

                    // io/protostuff/parser/ProtoLexer.g:265:14: ( options {greedy=false; } : . )*
                    loop17:
                    do {
                        int alt17=2;
                        int LA17_0 = input.LA(1);

                        if ( (LA17_0=='*') ) {
                            int LA17_1 = input.LA(2);

                            if ( (LA17_1=='/') ) {
                                alt17=2;
                            }
                            else if ( ((LA17_1>='\u0000' && LA17_1<='.')||(LA17_1>='0' && LA17_1<='\uFFFF')) ) {
                                alt17=1;
                            }


                        }
                        else if ( ((LA17_0>='\u0000' && LA17_0<=')')||(LA17_0>='+' && LA17_0<='\uFFFF')) ) {
                            alt17=1;
                        }


                        switch (alt17) {
                    	case 1 :
                    	    // io/protostuff/parser/ProtoLexer.g:265:42: .
                    	    {
                    	    matchAny(); 

                    	    }
                    	    break;

                    	default :
                    	    break loop17;
                        }
                    } while (true);

                    match("*/"); 

                    skip();

                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMENT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:268:5: ( ( ' ' | '\\t' | '\\r' | '\\n' ) )
            // io/protostuff/parser/ProtoLexer.g:268:9: ( ' ' | '\\t' | '\\r' | '\\n' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\n')||input.LA(1)=='\r'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            skip();

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "STRING_LITERAL"
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // io/protostuff/parser/ProtoLexer.g:276:5: ( '\"' ( ESC_SEQ | ~ ( '\\\\' | '\"' ) )* '\"' )
            // io/protostuff/parser/ProtoLexer.g:276:9: '\"' ( ESC_SEQ | ~ ( '\\\\' | '\"' ) )* '\"'
            {
            match('\"'); 
            // io/protostuff/parser/ProtoLexer.g:276:13: ( ESC_SEQ | ~ ( '\\\\' | '\"' ) )*
            loop19:
            do {
                int alt19=3;
                int LA19_0 = input.LA(1);

                if ( (LA19_0=='\\') ) {
                    alt19=1;
                }
                else if ( ((LA19_0>='\u0000' && LA19_0<='!')||(LA19_0>='#' && LA19_0<='[')||(LA19_0>=']' && LA19_0<='\uFFFF')) ) {
                    alt19=2;
                }


                switch (alt19) {
            	case 1 :
            	    // io/protostuff/parser/ProtoLexer.g:276:15: ESC_SEQ
            	    {
            	    mESC_SEQ(); 

            	    }
            	    break;
            	case 2 :
            	    // io/protostuff/parser/ProtoLexer.g:276:25: ~ ( '\\\\' | '\"' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='[')||(input.LA(1)>=']' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING_LITERAL"

    // $ANTLR start "HEX_DIGIT"
    public final void mHEX_DIGIT() throws RecognitionException {
        try {
            // io/protostuff/parser/ProtoLexer.g:280:5: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // io/protostuff/parser/ProtoLexer.g:280:9: ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' )
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='F')||(input.LA(1)>='a' && input.LA(1)<='f') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "HEX_DIGIT"

    // $ANTLR start "ESC_SEQ"
    public final void mESC_SEQ() throws RecognitionException {
        try {
            // io/protostuff/parser/ProtoLexer.g:284:5: ( '\\\\' ( 'a' | 'v' | 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | '\\\\' ( 'x' | 'X' ) HEX_DIGIT HEX_DIGIT | UNICODE_ESC | OCTAL_ESC )
            int alt20=4;
            switch ( input.LA(1) ) {
            case '\\':
                {
                switch ( input.LA(2) ) {
                case '\"':
                case '\'':
                case '\\':
                case 'a':
                case 'b':
                case 'f':
                case 'n':
                case 'r':
                case 't':
                case 'v':
                    {
                    alt20=1;
                    }
                    break;
                case 'X':
                case 'x':
                    {
                    alt20=2;
                    }
                    break;
                case 'u':
                    {
                    alt20=3;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                    {
                    alt20=4;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 20, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }

            switch (alt20) {
                case 1 :
                    // io/protostuff/parser/ProtoLexer.g:284:9: '\\\\' ( 'a' | 'v' | 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
                    {
                    match('\\'); 
                    if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||(input.LA(1)>='a' && input.LA(1)<='b')||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t'||input.LA(1)=='v' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}


                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoLexer.g:285:9: '\\\\' ( 'x' | 'X' ) HEX_DIGIT HEX_DIGIT
                    {
                    match('\\'); 
                    if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                        input.consume();

                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        recover(mse);
                        throw mse;}

                    mHEX_DIGIT(); 
                    mHEX_DIGIT(); 

                    }
                    break;
                case 3 :
                    // io/protostuff/parser/ProtoLexer.g:286:9: UNICODE_ESC
                    {
                    mUNICODE_ESC(); 

                    }
                    break;
                case 4 :
                    // io/protostuff/parser/ProtoLexer.g:287:9: OCTAL_ESC
                    {
                    mOCTAL_ESC(); 

                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end "ESC_SEQ"

    // $ANTLR start "OCTAL_ESC"
    public final void mOCTAL_ESC() throws RecognitionException {
        try {
            // io/protostuff/parser/ProtoLexer.g:291:5: ( '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) ( '0' .. '7' ) | '\\\\' ( '0' .. '7' ) )
            int alt21=3;
            switch ( input.LA(1) ) {
            case '\\':
                {
                switch ( input.LA(2) ) {
                case '0':
                case '1':
                case '2':
                case '3':
                    {
                    switch ( input.LA(3) ) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                        {
                        switch ( input.LA(4) ) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                            {
                            alt21=1;
                            }
                            break;
                        default:
                            alt21=2;}

                        }
                        break;
                    default:
                        alt21=3;}

                    }
                    break;
                case '4':
                case '5':
                case '6':
                case '7':
                    {
                    switch ( input.LA(3) ) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                        {
                        alt21=2;
                        }
                        break;
                    default:
                        alt21=3;}

                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 21, 1, input);

                    throw nvae;
                }

                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // io/protostuff/parser/ProtoLexer.g:291:9: '\\\\' ( '0' .. '3' ) ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // io/protostuff/parser/ProtoLexer.g:291:14: ( '0' .. '3' )
                    // io/protostuff/parser/ProtoLexer.g:291:15: '0' .. '3'
                    {
                    matchRange('0','3'); 

                    }

                    // io/protostuff/parser/ProtoLexer.g:291:25: ( '0' .. '7' )
                    // io/protostuff/parser/ProtoLexer.g:291:26: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // io/protostuff/parser/ProtoLexer.g:291:36: ( '0' .. '7' )
                    // io/protostuff/parser/ProtoLexer.g:291:37: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoLexer.g:292:9: '\\\\' ( '0' .. '7' ) ( '0' .. '7' )
                    {
                    match('\\'); 
                    // io/protostuff/parser/ProtoLexer.g:292:14: ( '0' .. '7' )
                    // io/protostuff/parser/ProtoLexer.g:292:15: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }

                    // io/protostuff/parser/ProtoLexer.g:292:25: ( '0' .. '7' )
                    // io/protostuff/parser/ProtoLexer.g:292:26: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;
                case 3 :
                    // io/protostuff/parser/ProtoLexer.g:293:9: '\\\\' ( '0' .. '7' )
                    {
                    match('\\'); 
                    // io/protostuff/parser/ProtoLexer.g:293:14: ( '0' .. '7' )
                    // io/protostuff/parser/ProtoLexer.g:293:15: '0' .. '7'
                    {
                    matchRange('0','7'); 

                    }


                    }
                    break;

            }
        }
        finally {
        }
    }
    // $ANTLR end "OCTAL_ESC"

    // $ANTLR start "UNICODE_ESC"
    public final void mUNICODE_ESC() throws RecognitionException {
        try {
            // io/protostuff/parser/ProtoLexer.g:297:5: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
            // io/protostuff/parser/ProtoLexer.g:297:9: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
            {
            match('\\'); 
            match('u'); 
            mHEX_DIGIT(); 
            mHEX_DIGIT(); 
            mHEX_DIGIT(); 
            mHEX_DIGIT(); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "UNICODE_ESC"

    public void mTokens() throws RecognitionException {
        // io/protostuff/parser/ProtoLexer.g:1:8: ( ASSIGN | AT | LEFTCURLY | RIGHTCURLY | LEFTPAREN | RIGHTPAREN | LEFTSQUARE | RIGHTSQUARE | SEMICOLON | COMMA | PLUS | MINUS | TO | TRUE | FALSE | PKG | SYNTAX | IMPORT | OPTION | MESSAGE | SERVICE | ENUM | REQUIRED | OPTIONAL | REPEATED | EXTENSIONS | EXTEND | GROUP | RPC | RETURNS | INT32 | INT64 | UINT32 | UINT64 | SINT32 | SINT64 | FIXED32 | FIXED64 | SFIXED32 | SFIXED64 | FLOAT | DOUBLE | BOOL | STRING | BYTES | DEFAULT | MAX | VOID | FULL_ID | ID | EXP | NUMDOUBLE | NUMFLOAT | NUMINT | HEX | OCTAL | DOC | COMMENT | WS | STRING_LITERAL )
        int alt22=60;
        alt22 = dfa22.predict(input);
        switch (alt22) {
            case 1 :
                // io/protostuff/parser/ProtoLexer.g:1:10: ASSIGN
                {
                mASSIGN(); 

                }
                break;
            case 2 :
                // io/protostuff/parser/ProtoLexer.g:1:17: AT
                {
                mAT(); 

                }
                break;
            case 3 :
                // io/protostuff/parser/ProtoLexer.g:1:20: LEFTCURLY
                {
                mLEFTCURLY(); 

                }
                break;
            case 4 :
                // io/protostuff/parser/ProtoLexer.g:1:30: RIGHTCURLY
                {
                mRIGHTCURLY(); 

                }
                break;
            case 5 :
                // io/protostuff/parser/ProtoLexer.g:1:41: LEFTPAREN
                {
                mLEFTPAREN(); 

                }
                break;
            case 6 :
                // io/protostuff/parser/ProtoLexer.g:1:51: RIGHTPAREN
                {
                mRIGHTPAREN(); 

                }
                break;
            case 7 :
                // io/protostuff/parser/ProtoLexer.g:1:62: LEFTSQUARE
                {
                mLEFTSQUARE(); 

                }
                break;
            case 8 :
                // io/protostuff/parser/ProtoLexer.g:1:73: RIGHTSQUARE
                {
                mRIGHTSQUARE(); 

                }
                break;
            case 9 :
                // io/protostuff/parser/ProtoLexer.g:1:85: SEMICOLON
                {
                mSEMICOLON(); 

                }
                break;
            case 10 :
                // io/protostuff/parser/ProtoLexer.g:1:95: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 11 :
                // io/protostuff/parser/ProtoLexer.g:1:101: PLUS
                {
                mPLUS(); 

                }
                break;
            case 12 :
                // io/protostuff/parser/ProtoLexer.g:1:106: MINUS
                {
                mMINUS(); 

                }
                break;
            case 13 :
                // io/protostuff/parser/ProtoLexer.g:1:112: TO
                {
                mTO(); 

                }
                break;
            case 14 :
                // io/protostuff/parser/ProtoLexer.g:1:115: TRUE
                {
                mTRUE(); 

                }
                break;
            case 15 :
                // io/protostuff/parser/ProtoLexer.g:1:120: FALSE
                {
                mFALSE(); 

                }
                break;
            case 16 :
                // io/protostuff/parser/ProtoLexer.g:1:126: PKG
                {
                mPKG(); 

                }
                break;
            case 17 :
                // io/protostuff/parser/ProtoLexer.g:1:130: SYNTAX
                {
                mSYNTAX(); 

                }
                break;
            case 18 :
                // io/protostuff/parser/ProtoLexer.g:1:137: IMPORT
                {
                mIMPORT(); 

                }
                break;
            case 19 :
                // io/protostuff/parser/ProtoLexer.g:1:144: OPTION
                {
                mOPTION(); 

                }
                break;
            case 20 :
                // io/protostuff/parser/ProtoLexer.g:1:151: MESSAGE
                {
                mMESSAGE(); 

                }
                break;
            case 21 :
                // io/protostuff/parser/ProtoLexer.g:1:159: SERVICE
                {
                mSERVICE(); 

                }
                break;
            case 22 :
                // io/protostuff/parser/ProtoLexer.g:1:167: ENUM
                {
                mENUM(); 

                }
                break;
            case 23 :
                // io/protostuff/parser/ProtoLexer.g:1:172: REQUIRED
                {
                mREQUIRED(); 

                }
                break;
            case 24 :
                // io/protostuff/parser/ProtoLexer.g:1:181: OPTIONAL
                {
                mOPTIONAL(); 

                }
                break;
            case 25 :
                // io/protostuff/parser/ProtoLexer.g:1:190: REPEATED
                {
                mREPEATED(); 

                }
                break;
            case 26 :
                // io/protostuff/parser/ProtoLexer.g:1:199: EXTENSIONS
                {
                mEXTENSIONS(); 

                }
                break;
            case 27 :
                // io/protostuff/parser/ProtoLexer.g:1:210: EXTEND
                {
                mEXTEND(); 

                }
                break;
            case 28 :
                // io/protostuff/parser/ProtoLexer.g:1:217: GROUP
                {
                mGROUP(); 

                }
                break;
            case 29 :
                // io/protostuff/parser/ProtoLexer.g:1:223: RPC
                {
                mRPC(); 

                }
                break;
            case 30 :
                // io/protostuff/parser/ProtoLexer.g:1:227: RETURNS
                {
                mRETURNS(); 

                }
                break;
            case 31 :
                // io/protostuff/parser/ProtoLexer.g:1:235: INT32
                {
                mINT32(); 

                }
                break;
            case 32 :
                // io/protostuff/parser/ProtoLexer.g:1:241: INT64
                {
                mINT64(); 

                }
                break;
            case 33 :
                // io/protostuff/parser/ProtoLexer.g:1:247: UINT32
                {
                mUINT32(); 

                }
                break;
            case 34 :
                // io/protostuff/parser/ProtoLexer.g:1:254: UINT64
                {
                mUINT64(); 

                }
                break;
            case 35 :
                // io/protostuff/parser/ProtoLexer.g:1:261: SINT32
                {
                mSINT32(); 

                }
                break;
            case 36 :
                // io/protostuff/parser/ProtoLexer.g:1:268: SINT64
                {
                mSINT64(); 

                }
                break;
            case 37 :
                // io/protostuff/parser/ProtoLexer.g:1:275: FIXED32
                {
                mFIXED32(); 

                }
                break;
            case 38 :
                // io/protostuff/parser/ProtoLexer.g:1:283: FIXED64
                {
                mFIXED64(); 

                }
                break;
            case 39 :
                // io/protostuff/parser/ProtoLexer.g:1:291: SFIXED32
                {
                mSFIXED32(); 

                }
                break;
            case 40 :
                // io/protostuff/parser/ProtoLexer.g:1:300: SFIXED64
                {
                mSFIXED64(); 

                }
                break;
            case 41 :
                // io/protostuff/parser/ProtoLexer.g:1:309: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 42 :
                // io/protostuff/parser/ProtoLexer.g:1:315: DOUBLE
                {
                mDOUBLE(); 

                }
                break;
            case 43 :
                // io/protostuff/parser/ProtoLexer.g:1:322: BOOL
                {
                mBOOL(); 

                }
                break;
            case 44 :
                // io/protostuff/parser/ProtoLexer.g:1:327: STRING
                {
                mSTRING(); 

                }
                break;
            case 45 :
                // io/protostuff/parser/ProtoLexer.g:1:334: BYTES
                {
                mBYTES(); 

                }
                break;
            case 46 :
                // io/protostuff/parser/ProtoLexer.g:1:340: DEFAULT
                {
                mDEFAULT(); 

                }
                break;
            case 47 :
                // io/protostuff/parser/ProtoLexer.g:1:348: MAX
                {
                mMAX(); 

                }
                break;
            case 48 :
                // io/protostuff/parser/ProtoLexer.g:1:352: VOID
                {
                mVOID(); 

                }
                break;
            case 49 :
                // io/protostuff/parser/ProtoLexer.g:1:357: FULL_ID
                {
                mFULL_ID(); 

                }
                break;
            case 50 :
                // io/protostuff/parser/ProtoLexer.g:1:365: ID
                {
                mID(); 

                }
                break;
            case 51 :
                // io/protostuff/parser/ProtoLexer.g:1:368: EXP
                {
                mEXP(); 

                }
                break;
            case 52 :
                // io/protostuff/parser/ProtoLexer.g:1:372: NUMDOUBLE
                {
                mNUMDOUBLE(); 

                }
                break;
            case 53 :
                // io/protostuff/parser/ProtoLexer.g:1:382: NUMFLOAT
                {
                mNUMFLOAT(); 

                }
                break;
            case 54 :
                // io/protostuff/parser/ProtoLexer.g:1:391: NUMINT
                {
                mNUMINT(); 

                }
                break;
            case 55 :
                // io/protostuff/parser/ProtoLexer.g:1:398: HEX
                {
                mHEX(); 

                }
                break;
            case 56 :
                // io/protostuff/parser/ProtoLexer.g:1:402: OCTAL
                {
                mOCTAL(); 

                }
                break;
            case 57 :
                // io/protostuff/parser/ProtoLexer.g:1:408: DOC
                {
                mDOC(); 

                }
                break;
            case 58 :
                // io/protostuff/parser/ProtoLexer.g:1:412: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 59 :
                // io/protostuff/parser/ProtoLexer.g:1:420: WS
                {
                mWS(); 

                }
                break;
            case 60 :
                // io/protostuff/parser/ProtoLexer.g:1:423: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;

        }

    }


    protected DFA3 dfa3 = new DFA3(this);
    protected DFA22 dfa22 = new DFA22(this);
    static final String DFA3_eotS =
        "\7\uffff";
    static final String DFA3_eofS =
        "\7\uffff";
    static final String DFA3_minS =
        "\1\55\1\56\1\61\1\56\2\uffff\1\56";
    static final String DFA3_maxS =
        "\1\71\1\145\1\71\1\145\2\uffff\1\145";
    static final String DFA3_acceptS =
        "\4\uffff\1\1\1\2\1\uffff";
    static final String DFA3_specialS =
        "\7\uffff}>";
    static final String[] DFA3_transitionS = {
            "\1\2\2\uffff\1\1\11\3",
            "\1\4\66\uffff\1\5",
            "\11\3",
            "\1\4\1\uffff\12\6\53\uffff\1\5",
            "",
            "",
            "\1\4\1\uffff\12\6\53\uffff\1\5"
    };

    static final short[] DFA3_eot = DFA.unpackEncodedString(DFA3_eotS);
    static final short[] DFA3_eof = DFA.unpackEncodedString(DFA3_eofS);
    static final char[] DFA3_min = DFA.unpackEncodedStringToUnsignedChars(DFA3_minS);
    static final char[] DFA3_max = DFA.unpackEncodedStringToUnsignedChars(DFA3_maxS);
    static final short[] DFA3_accept = DFA.unpackEncodedString(DFA3_acceptS);
    static final short[] DFA3_special = DFA.unpackEncodedString(DFA3_specialS);
    static final short[][] DFA3_transition;

    static {
        int numStates = DFA3_transitionS.length;
        DFA3_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA3_transition[i] = DFA.unpackEncodedString(DFA3_transitionS[i]);
        }
    }

    class DFA3 extends DFA {

        public DFA3(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 3;
            this.eot = DFA3_eot;
            this.eof = DFA3_eof;
            this.min = DFA3_min;
            this.max = DFA3_max;
            this.accept = DFA3_accept;
            this.special = DFA3_special;
            this.transition = DFA3_transition;
        }
        public String getDescription() {
            return "239:9: ( NUMFLOAT | NUMINT )";
        }
    }
    static final String DFA22_eotS =
        "\14\uffff\1\41\17\46\2\101\5\uffff\1\111\2\46\2\uffff\31\46\5\uffff"+
        "\1\101\3\uffff\16\46\1\171\5\46\1\177\7\46\1\u0088\2\104\1\uffff"+
        "\1\u008e\16\46\1\uffff\1\u009e\4\46\1\uffff\4\46\1\u00a8\1\46\1"+
        "\u00aa\1\u0088\2\uffff\1\104\4\uffff\1\u00ac\1\46\1\u00af\10\46"+
        "\1\u00b8\1\u00b9\2\46\1\uffff\4\46\1\u00c1\4\46\1\uffff\1\u00c6"+
        "\3\uffff\2\46\1\uffff\1\46\1\u00ca\1\46\1\u00cc\1\u00cd\1\46\1\u00d0"+
        "\1\u00d1\2\uffff\1\u00d3\2\46\1\u00d6\3\46\1\uffff\1\u00da\1\u00db"+
        "\1\u00dc\1\46\1\uffff\1\u00de\1\u00df\1\u00e0\1\uffff\1\u00e1\2"+
        "\uffff\2\46\2\uffff\1\46\1\uffff\1\u00e5\1\46\1\uffff\2\46\1\u00e9"+
        "\3\uffff\1\u00ea\4\uffff\1\u00eb\1\u00ec\1\u00ed\1\uffff\1\46\1"+
        "\u00ef\1\u00f0\5\uffff\1\46\2\uffff\1\u00f2\1\uffff";
    static final String DFA22_eofS =
        "\u00f3\uffff";
    static final String DFA22_minS =
        "\1\11\13\uffff\1\60\21\56\1\52\4\uffff\3\56\2\uffff\31\56\1\uffff"+
        "\1\60\1\55\2\uffff\1\56\1\0\2\uffff\34\56\3\60\1\0\17\56\1\uffff"+
        "\5\56\1\uffff\7\56\1\145\2\uffff\1\60\1\0\1\12\2\uffff\17\56\1\uffff"+
        "\11\56\1\uffff\1\56\3\uffff\2\56\1\uffff\10\56\2\uffff\7\56\1\uffff"+
        "\4\56\1\uffff\3\56\1\uffff\1\56\2\uffff\2\56\2\uffff\1\56\1\uffff"+
        "\2\56\1\uffff\3\56\3\uffff\1\56\4\uffff\3\56\1\uffff\3\56\5\uffff"+
        "\1\56\2\uffff\1\56\1\uffff";
    static final String DFA22_maxS =
        "\1\175\13\uffff\1\71\17\172\1\170\1\145\1\57\4\uffff\3\172\2\uffff"+
        "\31\172\1\uffff\2\71\2\uffff\1\145\1\uffff\2\uffff\34\172\1\146"+
        "\2\71\1\uffff\17\172\1\uffff\5\172\1\uffff\7\172\1\145\2\uffff\1"+
        "\71\1\uffff\1\12\2\uffff\17\172\1\uffff\11\172\1\uffff\1\172\3\uffff"+
        "\2\172\1\uffff\10\172\2\uffff\7\172\1\uffff\4\172\1\uffff\3\172"+
        "\1\uffff\1\172\2\uffff\2\172\2\uffff\1\172\1\uffff\2\172\1\uffff"+
        "\3\172\3\uffff\1\172\4\uffff\3\172\1\uffff\3\172\5\uffff\1\172\2"+
        "\uffff\1\172\1\uffff";
    static final String DFA22_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\23\uffff"+
        "\1\73\1\74\1\14\1\67\3\uffff\1\62\1\61\31\uffff\1\66\2\uffff\1\63"+
        "\1\70\2\uffff\1\72\1\15\57\uffff\1\57\5\uffff\1\35\10\uffff\1\65"+
        "\1\64\3\uffff\1\71\1\16\17\uffff\1\26\11\uffff\1\53\1\uffff\1\60"+
        "\1\71\1\17\2\uffff\1\51\10\uffff\1\37\1\40\7\uffff\1\34\4\uffff"+
        "\1\55\3\uffff\1\21\1\uffff\1\43\1\44\2\uffff\1\54\1\22\1\uffff\1"+
        "\23\2\uffff\1\33\3\uffff\1\41\1\42\1\52\1\uffff\1\45\1\46\1\20\1"+
        "\25\3\uffff\1\24\3\uffff\1\36\1\56\1\47\1\50\1\30\1\uffff\1\27\1"+
        "\31\1\uffff\1\32";
    static final String DFA22_specialS =
        "\107\uffff\1\0\41\uffff\1\1\41\uffff\1\2\147\uffff}>";
    static final String[] DFA22_transitionS = {
            "\2\37\2\uffff\1\37\22\uffff\1\37\1\uffff\1\40\5\uffff\1\5\1"+
            "\6\1\uffff\1\13\1\12\1\14\1\uffff\1\36\1\34\11\35\1\uffff\1"+
            "\11\1\uffff\1\1\2\uffff\1\2\32\33\1\7\1\uffff\1\10\1\uffff\1"+
            "\33\1\uffff\1\33\1\31\1\33\1\30\1\24\1\16\1\26\1\33\1\21\3\33"+
            "\1\23\1\33\1\22\1\17\1\33\1\25\1\20\1\15\1\27\1\32\4\33\1\3"+
            "\1\uffff\1\4",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\42\11\35",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\16\45"+
            "\1\43\2\45\1\44\10\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\1\50"+
            "\7\45\1\51\2\45\1\52\16\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\1\53"+
            "\31\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\55\1\57\2\45\1\56\12\45\1\60\4\45\1\54\1\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\14\45"+
            "\1\61\1\62\14\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\17\45"+
            "\1\63\12\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\1\65"+
            "\3\45\1\64\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\15\45"+
            "\1\66\11\45\1\67\2\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\70\12\45\1\71\12\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\21\45"+
            "\1\72\10\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\10\45"+
            "\1\73\21\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\75\11\45\1\74\13\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\16\45"+
            "\1\76\11\45\1\77\1\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\16\45"+
            "\1\100\13\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\102\1\uffff\10\105\15\uffff\1\104\22\uffff\1\42\14\uffff"+
            "\1\103\22\uffff\1\42",
            "\1\102\1\uffff\12\106\13\uffff\1\104\37\uffff\1\103",
            "\1\110\4\uffff\1\107",
            "",
            "",
            "",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\24\45"+
            "\1\112\5\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\13\45"+
            "\1\113\16\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\27\45"+
            "\1\114\2\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\16\45"+
            "\1\115\13\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\2\45"+
            "\1\116\27\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\15\45"+
            "\1\117\14\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\21\45"+
            "\1\120\10\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\15\45"+
            "\1\121\14\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\10\45"+
            "\1\122\21\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\21\45"+
            "\1\123\10\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\17\45"+
            "\1\124\12\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\23\45"+
            "\1\125\6\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\23\45"+
            "\1\126\6\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\22\45"+
            "\1\127\7\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\27\45"+
            "\1\130\2\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\24\45"+
            "\1\131\5\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\23\45"+
            "\1\132\6\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\17\45"+
            "\1\134\1\133\2\45\1\135\6\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\2\45"+
            "\1\136\27\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\16\45"+
            "\1\137\13\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\15\45"+
            "\1\140\14\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\24\45"+
            "\1\141\5\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\5\45"+
            "\1\142\24\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\16\45"+
            "\1\143\13\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\23\45"+
            "\1\144\6\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\10\45"+
            "\1\145\21\45",
            "",
            "\12\146",
            "\1\104\2\uffff\1\147\11\150",
            "",
            "",
            "\1\102\1\uffff\12\106\13\uffff\1\104\37\uffff\1\103",
            "\57\110\1\151\uffd0\110",
            "",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\152\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\22\45"+
            "\1\153\7\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\154\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\1\155"+
            "\31\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\12\45"+
            "\1\156\17\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\23\45"+
            "\1\157\6\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\25\45"+
            "\1\160\4\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\23\45"+
            "\1\161\6\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\27\45"+
            "\1\162\2\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\10\45"+
            "\1\163\21\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\16\45"+
            "\1\164\13\45",
            "\1\47\1\uffff\3\45\1\165\2\45\1\166\3\45\7\uffff\32\45\4\uffff"+
            "\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\10\45"+
            "\1\167\21\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\22\45"+
            "\1\170\7\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\14\45"+
            "\1\172\15\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\173\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\24\45"+
            "\1\174\5\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\175\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\24\45"+
            "\1\176\5\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\24\45"+
            "\1\u0080\5\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\23\45"+
            "\1\u0081\6\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\1\45"+
            "\1\u0082\30\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\1\u0083"+
            "\31\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\13\45"+
            "\1\u0084\16\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\u0085\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\3\45"+
            "\1\u0086\26\45",
            "\12\146\53\uffff\1\u0089\1\u0087",
            "\12\u0089",
            "\12\u008a",
            "\12\u008b\1\u008d\2\u008b\1\u008c\ufff2\u008b",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\u008f\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\3\45"+
            "\1\u0090\26\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\23\45"+
            "\1\u0091\6\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\1\u0092"+
            "\31\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\1\u0093"+
            "\31\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\10\45"+
            "\1\u0094\21\45",
            "\1\47\1\uffff\3\45\1\u0095\2\45\1\u0096\3\45\7\uffff\32\45"+
            "\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\u0097\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\15\45"+
            "\1\u0098\14\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\21\45"+
            "\1\u0099\10\45",
            "\1\47\1\uffff\2\45\1\u009a\7\45\7\uffff\32\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "\1\47\1\uffff\4\45\1\u009b\5\45\7\uffff\32\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\16\45"+
            "\1\u009c\13\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\1\u009d"+
            "\31\45",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\15\45"+
            "\1\u009f\14\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\10\45"+
            "\1\u00a0\21\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\1\u00a1"+
            "\31\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\21\45"+
            "\1\u00a2\10\45",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\17\45"+
            "\1\u00a3\12\45",
            "\1\47\1\uffff\3\45\1\u00a4\2\45\1\u00a5\3\45\7\uffff\32\45"+
            "\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\13\45"+
            "\1\u00a6\16\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\24\45"+
            "\1\u00a7\5\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\22\45"+
            "\1\u00a9\7\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u0089",
            "",
            "",
            "\12\u008a",
            "\12\u008b\1\u008d\2\u008b\1\u008c\ufff2\u008b",
            "\1\u008d",
            "",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\3\45\1\u00ad\2\45\1\u00ae\3\45\7\uffff\32\45"+
            "\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\6\45"+
            "\1\u00b0\23\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\27\45"+
            "\1\u00b1\2\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\2\45"+
            "\1\u00b2\27\45",
            "\1\47\1\uffff\2\45\1\u00b3\7\45\7\uffff\32\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "\1\47\1\uffff\4\45\1\u00b4\5\45\7\uffff\32\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\3\45"+
            "\1\u00b5\26\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\6\45"+
            "\1\u00b6\23\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\23\45"+
            "\1\u00b7\6\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\15\45"+
            "\1\u00ba\14\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\6\45"+
            "\1\u00bb\23\45",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\3\45"+
            "\1\u00bd\16\45\1\u00bc\7\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\21\45"+
            "\1\u00be\10\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\23\45"+
            "\1\u00bf\6\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\15\45"+
            "\1\u00c0\14\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\2\45\1\u00c2\7\45\7\uffff\32\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "\1\47\1\uffff\4\45\1\u00c3\5\45\7\uffff\32\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\u00c4\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\13\45"+
            "\1\u00c5\16\45",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "",
            "",
            "\1\47\1\uffff\2\45\1\u00c7\7\45\7\uffff\32\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "\1\47\1\uffff\4\45\1\u00c8\5\45\7\uffff\32\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\u00c9\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\u00cb\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\3\45\1\u00ce\2\45\1\u00cf\3\45\7\uffff\32\45"+
            "\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\1\u00d2"+
            "\31\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\u00d4\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\10\45"+
            "\1\u00d5\21\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\u00d7\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45"+
            "\1\u00d8\25\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\22\45"+
            "\1\u00d9\7\45",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\23\45"+
            "\1\u00dd\6\45",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "",
            "\1\47\1\uffff\2\45\1\u00e2\7\45\7\uffff\32\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "\1\47\1\uffff\4\45\1\u00e3\5\45\7\uffff\32\45\4\uffff\1\45"+
            "\1\uffff\32\45",
            "",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\13\45"+
            "\1\u00e4\16\45",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\16\45"+
            "\1\u00e6\13\45",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\3\45"+
            "\1\u00e7\26\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\3\45"+
            "\1\u00e8\26\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "",
            "",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\15\45"+
            "\1\u00ee\14\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "",
            "",
            "",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\22\45"+
            "\1\u00f1\7\45",
            "",
            "",
            "\1\47\1\uffff\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            ""
    };

    static final short[] DFA22_eot = DFA.unpackEncodedString(DFA22_eotS);
    static final short[] DFA22_eof = DFA.unpackEncodedString(DFA22_eofS);
    static final char[] DFA22_min = DFA.unpackEncodedStringToUnsignedChars(DFA22_minS);
    static final char[] DFA22_max = DFA.unpackEncodedStringToUnsignedChars(DFA22_maxS);
    static final short[] DFA22_accept = DFA.unpackEncodedString(DFA22_acceptS);
    static final short[] DFA22_special = DFA.unpackEncodedString(DFA22_specialS);
    static final short[][] DFA22_transition;

    static {
        int numStates = DFA22_transitionS.length;
        DFA22_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA22_transition[i] = DFA.unpackEncodedString(DFA22_transitionS[i]);
        }
    }

    class DFA22 extends DFA {

        public DFA22(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 22;
            this.eot = DFA22_eot;
            this.eof = DFA22_eof;
            this.min = DFA22_min;
            this.max = DFA22_max;
            this.accept = DFA22_accept;
            this.special = DFA22_special;
            this.transition = DFA22_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( ASSIGN | AT | LEFTCURLY | RIGHTCURLY | LEFTPAREN | RIGHTPAREN | LEFTSQUARE | RIGHTSQUARE | SEMICOLON | COMMA | PLUS | MINUS | TO | TRUE | FALSE | PKG | SYNTAX | IMPORT | OPTION | MESSAGE | SERVICE | ENUM | REQUIRED | OPTIONAL | REPEATED | EXTENSIONS | EXTEND | GROUP | RPC | RETURNS | INT32 | INT64 | UINT32 | UINT64 | SINT32 | SINT64 | FIXED32 | FIXED64 | SFIXED32 | SFIXED64 | FLOAT | DOUBLE | BOOL | STRING | BYTES | DEFAULT | MAX | VOID | FULL_ID | ID | EXP | NUMDOUBLE | NUMFLOAT | NUMINT | HEX | OCTAL | DOC | COMMENT | WS | STRING_LITERAL );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA22_71 = input.LA(1);

                        s = -1;
                        if ( (LA22_71=='/') ) {s = 105;}

                        else if ( ((LA22_71>='\u0000' && LA22_71<='.')||(LA22_71>='0' && LA22_71<='\uFFFF')) ) {s = 72;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA22_105 = input.LA(1);

                        s = -1;
                        if ( ((LA22_105>='\u0000' && LA22_105<='\t')||(LA22_105>='\u000B' && LA22_105<='\f')||(LA22_105>='\u000E' && LA22_105<='\uFFFF')) ) {s = 139;}

                        else if ( (LA22_105=='\r') ) {s = 140;}

                        else if ( (LA22_105=='\n') ) {s = 141;}

                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA22_139 = input.LA(1);

                        s = -1;
                        if ( (LA22_139=='\r') ) {s = 140;}

                        else if ( (LA22_139=='\n') ) {s = 141;}

                        else if ( ((LA22_139>='\u0000' && LA22_139<='\t')||(LA22_139>='\u000B' && LA22_139<='\f')||(LA22_139>='\u000E' && LA22_139<='\uFFFF')) ) {s = 139;}

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 22, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}