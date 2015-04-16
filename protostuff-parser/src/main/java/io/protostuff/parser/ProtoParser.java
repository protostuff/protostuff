// $ANTLR 3.2 Sep 23, 2009 14:05:07 io/protostuff/parser/ProtoParser.g 2015-04-16 10:22:29

    package io.protostuff.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;

public class ProtoParser extends AbstractParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ASSIGN", "AT", "LEFTCURLY", "RIGHTCURLY", "LEFTPAREN", "RIGHTPAREN", "LEFTSQUARE", "RIGHTSQUARE", "SEMICOLON", "COMMA", "PLUS", "MINUS", "TO", "TRUE", "FALSE", "PKG", "SYNTAX", "IMPORT", "OPTION", "MESSAGE", "SERVICE", "ENUM", "REQUIRED", "OPTIONAL", "REPEATED", "EXTENSIONS", "EXTEND", "GROUP", "RPC", "RETURNS", "INT32", "INT64", "UINT32", "UINT64", "SINT32", "SINT64", "FIXED32", "FIXED64", "SFIXED32", "SFIXED64", "FLOAT", "DOUBLE", "BOOL", "STRING", "BYTES", "DEFAULT", "MAX", "VOID", "ID", "FULL_ID", "NUMINT", "EXP", "NUMFLOAT", "NUMDOUBLE", "HEX_DIGIT", "HEX", "OCTAL", "DOC", "COMMENT", "WS", "ESC_SEQ", "STRING_LITERAL", "UNICODE_ESC", "OCTAL_ESC"
    };
    public static final int OPTION=22;
    public static final int DOC=61;
    public static final int FULL_ID=53;
    public static final int PKG=19;
    public static final int OCTAL_ESC=67;
    public static final int NUMFLOAT=56;
    public static final int MAX=50;
    public static final int FLOAT=44;
    public static final int FIXED64=41;
    public static final int ID=52;
    public static final int EOF=-1;
    public static final int AT=5;
    public static final int SYNTAX=20;
    public static final int LEFTPAREN=8;
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
    public static final int REQUIRED=26;
    public static final int MINUS=15;
    public static final int TRUE=17;
    public static final int UINT64=37;
    public static final int OPTIONAL=27;
    public static final int INT32=34;
    public static final int GROUP=31;
    public static final int WS=63;
    public static final int ENUM=25;
    public static final int SERVICE=24;
    public static final int RIGHTPAREN=9;
    public static final int LEFTSQUARE=10;
    public static final int SFIXED64=43;
    public static final int BYTES=48;
    public static final int ASSIGN=4;
    public static final int RPC=32;
    public static final int OCTAL=60;
    public static final int UINT32=36;
    public static final int FALSE=18;
    public static final int STRING=47;

    // delegates
    // delegators


        public ProtoParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public ProtoParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return ProtoParser.tokenNames; }
    public String getGrammarFileName() { return "io/protostuff/parser/ProtoParser.g"; }


    public static class parse_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "parse"
    // io/protostuff/parser/ProtoParser.g:52:1: parse[Proto proto] : ( statement[proto] )+ EOF ;
    public final ProtoParser.parse_return parse(Proto proto) throws RecognitionException {
        ProtoParser.parse_return retval = new ProtoParser.parse_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token EOF2=null;
        ProtoParser.statement_return statement1 = null;


        Object EOF2_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:53:5: ( ( statement[proto] )+ EOF )
            // io/protostuff/parser/ProtoParser.g:53:9: ( statement[proto] )+ EOF
            {
            root_0 = (Object)adaptor.nil();

            // io/protostuff/parser/ProtoParser.g:53:9: ( statement[proto] )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                switch ( input.LA(1) ) {
                case AT:
                case PKG:
                case SYNTAX:
                case IMPORT:
                case OPTION:
                case MESSAGE:
                case SERVICE:
                case ENUM:
                case EXTEND:
                case DOC:
                    {
                    alt1=1;
                    }
                    break;

                }

                switch (alt1) {
            	case 1 :
            	    // io/protostuff/parser/ProtoParser.g:53:10: statement[proto]
            	    {
            	    pushFollow(FOLLOW_statement_in_parse178);
            	    statement1=statement(proto);

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, statement1.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);

            EOF2=(Token)match(input,EOF,FOLLOW_EOF_in_parse183); if (state.failed) return retval;
            if ( state.backtracking==0 ) {

                          if(!proto.annotations.isEmpty())
                              throw new IllegalStateException("Misplaced annotations: " + proto.annotations);
                          if(!proto.docs.isEmpty())
                              throw new IllegalStateException("Misplaced docs: " + proto.docs);
                          
                          proto.postParse();
                      
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "parse"

    public static class statement_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "statement"
    // io/protostuff/parser/ProtoParser.g:63:1: statement[Proto proto] : ( header_syntax[proto] | header_package[proto] | header_import[proto] | message_block[proto, null] | enum_block[proto, null] | extend_block[proto, null] | service_block[proto, null] | annotation_entry[proto] | doc_entry[proto] | option_entry[proto, proto] );
    public final ProtoParser.statement_return statement(Proto proto) throws RecognitionException {
        ProtoParser.statement_return retval = new ProtoParser.statement_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ProtoParser.header_syntax_return header_syntax3 = null;

        ProtoParser.header_package_return header_package4 = null;

        ProtoParser.header_import_return header_import5 = null;

        ProtoParser.message_block_return message_block6 = null;

        ProtoParser.enum_block_return enum_block7 = null;

        ProtoParser.extend_block_return extend_block8 = null;

        ProtoParser.service_block_return service_block9 = null;

        ProtoParser.annotation_entry_return annotation_entry10 = null;

        ProtoParser.doc_entry_return doc_entry11 = null;

        ProtoParser.option_entry_return option_entry12 = null;



        try {
            // io/protostuff/parser/ProtoParser.g:64:5: ( header_syntax[proto] | header_package[proto] | header_import[proto] | message_block[proto, null] | enum_block[proto, null] | extend_block[proto, null] | service_block[proto, null] | annotation_entry[proto] | doc_entry[proto] | option_entry[proto, proto] )
            int alt2=10;
            switch ( input.LA(1) ) {
            case SYNTAX:
                {
                alt2=1;
                }
                break;
            case PKG:
                {
                alt2=2;
                }
                break;
            case IMPORT:
                {
                alt2=3;
                }
                break;
            case MESSAGE:
                {
                alt2=4;
                }
                break;
            case ENUM:
                {
                alt2=5;
                }
                break;
            case EXTEND:
                {
                alt2=6;
                }
                break;
            case SERVICE:
                {
                alt2=7;
                }
                break;
            case AT:
                {
                alt2=8;
                }
                break;
            case DOC:
                {
                alt2=9;
                }
                break;
            case OPTION:
                {
                alt2=10;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }

            switch (alt2) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:64:9: header_syntax[proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_header_syntax_in_statement211);
                    header_syntax3=header_syntax(proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, header_syntax3.getTree());

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:65:9: header_package[proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_header_package_in_statement222);
                    header_package4=header_package(proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, header_package4.getTree());

                    }
                    break;
                case 3 :
                    // io/protostuff/parser/ProtoParser.g:66:9: header_import[proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_header_import_in_statement233);
                    header_import5=header_import(proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, header_import5.getTree());

                    }
                    break;
                case 4 :
                    // io/protostuff/parser/ProtoParser.g:67:9: message_block[proto, null]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_message_block_in_statement244);
                    message_block6=message_block(proto, null);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, message_block6.getTree());

                    }
                    break;
                case 5 :
                    // io/protostuff/parser/ProtoParser.g:68:9: enum_block[proto, null]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_block_in_statement255);
                    enum_block7=enum_block(proto, null);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_block7.getTree());

                    }
                    break;
                case 6 :
                    // io/protostuff/parser/ProtoParser.g:69:9: extend_block[proto, null]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_extend_block_in_statement266);
                    extend_block8=extend_block(proto, null);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, extend_block8.getTree());

                    }
                    break;
                case 7 :
                    // io/protostuff/parser/ProtoParser.g:70:9: service_block[proto, null]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_service_block_in_statement277);
                    service_block9=service_block(proto, null);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, service_block9.getTree());

                    }
                    break;
                case 8 :
                    // io/protostuff/parser/ProtoParser.g:71:9: annotation_entry[proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_annotation_entry_in_statement288);
                    annotation_entry10=annotation_entry(proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotation_entry10.getTree());

                    }
                    break;
                case 9 :
                    // io/protostuff/parser/ProtoParser.g:72:9: doc_entry[proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_doc_entry_in_statement299);
                    doc_entry11=doc_entry(proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, doc_entry11.getTree());

                    }
                    break;
                case 10 :
                    // io/protostuff/parser/ProtoParser.g:73:9: option_entry[proto, proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_option_entry_in_statement310);
                    option_entry12=option_entry(proto, proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, option_entry12.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "statement"

    public static class var_reserved_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "var_reserved"
    // io/protostuff/parser/ProtoParser.g:77:1: var_reserved : ( TO | PKG | SYNTAX | IMPORT | OPTION | MESSAGE | SERVICE | ENUM | REQUIRED | OPTIONAL | REPEATED | EXTENSIONS | EXTEND | GROUP | RPC | RETURNS | INT32 | INT64 | UINT32 | UINT64 | SINT32 | SINT64 | FIXED32 | FIXED64 | SFIXED32 | SFIXED64 | FLOAT | DOUBLE | BOOL | STRING | BYTES | DEFAULT | MAX | VOID );
    public final ProtoParser.var_reserved_return var_reserved() throws RecognitionException {
        ProtoParser.var_reserved_return retval = new ProtoParser.var_reserved_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set13=null;

        Object set13_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:78:5: ( TO | PKG | SYNTAX | IMPORT | OPTION | MESSAGE | SERVICE | ENUM | REQUIRED | OPTIONAL | REPEATED | EXTENSIONS | EXTEND | GROUP | RPC | RETURNS | INT32 | INT64 | UINT32 | UINT64 | SINT32 | SINT64 | FIXED32 | FIXED64 | SFIXED32 | SFIXED64 | FLOAT | DOUBLE | BOOL | STRING | BYTES | DEFAULT | MAX | VOID )
            // io/protostuff/parser/ProtoParser.g:
            {
            root_0 = (Object)adaptor.nil();

            set13=(Token)input.LT(1);
            if ( input.LA(1)==TO||(input.LA(1)>=PKG && input.LA(1)<=VOID) ) {
                input.consume();
                if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set13));
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "var_reserved"

    public static class var_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "var"
    // io/protostuff/parser/ProtoParser.g:85:1: var : ( ID | var_reserved );
    public final ProtoParser.var_return var() throws RecognitionException {
        ProtoParser.var_return retval = new ProtoParser.var_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID14=null;
        ProtoParser.var_reserved_return var_reserved15 = null;


        Object ID14_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:86:5: ( ID | var_reserved )
            int alt3=2;
            switch ( input.LA(1) ) {
            case ID:
                {
                alt3=1;
                }
                break;
            case TO:
            case PKG:
            case SYNTAX:
            case IMPORT:
            case OPTION:
            case MESSAGE:
            case SERVICE:
            case ENUM:
            case REQUIRED:
            case OPTIONAL:
            case REPEATED:
            case EXTENSIONS:
            case EXTEND:
            case GROUP:
            case RPC:
            case RETURNS:
            case INT32:
            case INT64:
            case UINT32:
            case UINT64:
            case SINT32:
            case SINT64:
            case FIXED32:
            case FIXED64:
            case SFIXED32:
            case SFIXED64:
            case FLOAT:
            case DOUBLE:
            case BOOL:
            case STRING:
            case BYTES:
            case DEFAULT:
            case MAX:
            case VOID:
                {
                alt3=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }

            switch (alt3) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:86:9: ID
                    {
                    root_0 = (Object)adaptor.nil();

                    ID14=(Token)match(input,ID,FOLLOW_ID_in_var520); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ID14_tree = (Object)adaptor.create(ID14);
                    adaptor.addChild(root_0, ID14_tree);
                    }

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:86:14: var_reserved
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_var_reserved_in_var524);
                    var_reserved15=var_reserved();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, var_reserved15.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "var"

    public static class var_full_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "var_full"
    // io/protostuff/parser/ProtoParser.g:89:1: var_full : ( FULL_ID | var );
    public final ProtoParser.var_full_return var_full() throws RecognitionException {
        ProtoParser.var_full_return retval = new ProtoParser.var_full_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token FULL_ID16=null;
        ProtoParser.var_return var17 = null;


        Object FULL_ID16_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:90:5: ( FULL_ID | var )
            int alt4=2;
            switch ( input.LA(1) ) {
            case FULL_ID:
                {
                alt4=1;
                }
                break;
            case TO:
            case PKG:
            case SYNTAX:
            case IMPORT:
            case OPTION:
            case MESSAGE:
            case SERVICE:
            case ENUM:
            case REQUIRED:
            case OPTIONAL:
            case REPEATED:
            case EXTENSIONS:
            case EXTEND:
            case GROUP:
            case RPC:
            case RETURNS:
            case INT32:
            case INT64:
            case UINT32:
            case UINT64:
            case SINT32:
            case SINT64:
            case FIXED32:
            case FIXED64:
            case SFIXED32:
            case SFIXED64:
            case FLOAT:
            case DOUBLE:
            case BOOL:
            case STRING:
            case BYTES:
            case DEFAULT:
            case MAX:
            case VOID:
            case ID:
                {
                alt4=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }

            switch (alt4) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:90:9: FULL_ID
                    {
                    root_0 = (Object)adaptor.nil();

                    FULL_ID16=(Token)match(input,FULL_ID,FOLLOW_FULL_ID_in_var_full543); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FULL_ID16_tree = (Object)adaptor.create(FULL_ID16);
                    adaptor.addChild(root_0, FULL_ID16_tree);
                    }

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:90:19: var
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_var_in_var_full547);
                    var17=var();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, var17.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "var_full"

    public static class annotation_entry_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "annotation_entry"
    // io/protostuff/parser/ProtoParser.g:93:1: annotation_entry[Proto proto] : AT var ( LEFTPAREN annotation_keyval[proto, annotation] ( COMMA annotation_keyval[proto, annotation] )* RIGHTPAREN )? ;
    public final ProtoParser.annotation_entry_return annotation_entry(Proto proto) throws RecognitionException {
        ProtoParser.annotation_entry_return retval = new ProtoParser.annotation_entry_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token AT18=null;
        Token LEFTPAREN20=null;
        Token COMMA22=null;
        Token RIGHTPAREN24=null;
        ProtoParser.var_return var19 = null;

        ProtoParser.annotation_keyval_return annotation_keyval21 = null;

        ProtoParser.annotation_keyval_return annotation_keyval23 = null;


        Object AT18_tree=null;
        Object LEFTPAREN20_tree=null;
        Object COMMA22_tree=null;
        Object RIGHTPAREN24_tree=null;


            Annotation annotation = null;

        try {
            // io/protostuff/parser/ProtoParser.g:97:5: ( AT var ( LEFTPAREN annotation_keyval[proto, annotation] ( COMMA annotation_keyval[proto, annotation] )* RIGHTPAREN )? )
            // io/protostuff/parser/ProtoParser.g:97:9: AT var ( LEFTPAREN annotation_keyval[proto, annotation] ( COMMA annotation_keyval[proto, annotation] )* RIGHTPAREN )?
            {
            root_0 = (Object)adaptor.nil();

            AT18=(Token)match(input,AT,FOLLOW_AT_in_annotation_entry573); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            AT18_tree = (Object)adaptor.create(AT18);
            adaptor.addChild(root_0, AT18_tree);
            }
            pushFollow(FOLLOW_var_in_annotation_entry575);
            var19=var();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, var19.getTree());
            if ( state.backtracking==0 ) {
               annotation = new Annotation((var19!=null?input.toString(var19.start,var19.stop):null)); 
            }
            // io/protostuff/parser/ProtoParser.g:98:9: ( LEFTPAREN annotation_keyval[proto, annotation] ( COMMA annotation_keyval[proto, annotation] )* RIGHTPAREN )?
            int alt6=2;
            switch ( input.LA(1) ) {
                case LEFTPAREN:
                    {
                    alt6=1;
                    }
                    break;
            }

            switch (alt6) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:98:10: LEFTPAREN annotation_keyval[proto, annotation] ( COMMA annotation_keyval[proto, annotation] )* RIGHTPAREN
                    {
                    LEFTPAREN20=(Token)match(input,LEFTPAREN,FOLLOW_LEFTPAREN_in_annotation_entry588); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LEFTPAREN20_tree = (Object)adaptor.create(LEFTPAREN20);
                    adaptor.addChild(root_0, LEFTPAREN20_tree);
                    }
                    pushFollow(FOLLOW_annotation_keyval_in_annotation_entry599);
                    annotation_keyval21=annotation_keyval(proto, annotation);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotation_keyval21.getTree());
                    // io/protostuff/parser/ProtoParser.g:99:46: ( COMMA annotation_keyval[proto, annotation] )*
                    loop5:
                    do {
                        int alt5=2;
                        switch ( input.LA(1) ) {
                        case COMMA:
                            {
                            alt5=1;
                            }
                            break;

                        }

                        switch (alt5) {
                    	case 1 :
                    	    // io/protostuff/parser/ProtoParser.g:99:47: COMMA annotation_keyval[proto, annotation]
                    	    {
                    	    COMMA22=(Token)match(input,COMMA,FOLLOW_COMMA_in_annotation_entry603); if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) {
                    	    COMMA22_tree = (Object)adaptor.create(COMMA22);
                    	    adaptor.addChild(root_0, COMMA22_tree);
                    	    }
                    	    pushFollow(FOLLOW_annotation_keyval_in_annotation_entry605);
                    	    annotation_keyval23=annotation_keyval(proto, annotation);

                    	    state._fsp--;
                    	    if (state.failed) return retval;
                    	    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotation_keyval23.getTree());

                    	    }
                    	    break;

                    	default :
                    	    break loop5;
                        }
                    } while (true);

                    RIGHTPAREN24=(Token)match(input,RIGHTPAREN,FOLLOW_RIGHTPAREN_in_annotation_entry619); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    RIGHTPAREN24_tree = (Object)adaptor.create(RIGHTPAREN24);
                    adaptor.addChild(root_0, RIGHTPAREN24_tree);
                    }

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

                          proto.add(annotation);
                      
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "annotation_entry"

    public static class annotation_keyval_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "annotation_keyval"
    // io/protostuff/parser/ProtoParser.g:105:1: annotation_keyval[Proto proto, Annotation annotation] : k= var_full ASSIGN (vr= var_reserved | ID | fid= FULL_ID | NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE | STRING_LITERAL ) ;
    public final ProtoParser.annotation_keyval_return annotation_keyval(Proto proto, Annotation annotation) throws RecognitionException {
        ProtoParser.annotation_keyval_return retval = new ProtoParser.annotation_keyval_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token fid=null;
        Token ASSIGN25=null;
        Token ID26=null;
        Token NUMFLOAT27=null;
        Token NUMINT28=null;
        Token NUMDOUBLE29=null;
        Token TRUE30=null;
        Token FALSE31=null;
        Token STRING_LITERAL32=null;
        ProtoParser.var_full_return k = null;

        ProtoParser.var_reserved_return vr = null;


        Object fid_tree=null;
        Object ASSIGN25_tree=null;
        Object ID26_tree=null;
        Object NUMFLOAT27_tree=null;
        Object NUMINT28_tree=null;
        Object NUMDOUBLE29_tree=null;
        Object TRUE30_tree=null;
        Object FALSE31_tree=null;
        Object STRING_LITERAL32_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:106:5: (k= var_full ASSIGN (vr= var_reserved | ID | fid= FULL_ID | NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE | STRING_LITERAL ) )
            // io/protostuff/parser/ProtoParser.g:106:9: k= var_full ASSIGN (vr= var_reserved | ID | fid= FULL_ID | NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE | STRING_LITERAL )
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_var_full_in_annotation_keyval646);
            k=var_full();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, k.getTree());
            ASSIGN25=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_annotation_keyval648); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ASSIGN25_tree = (Object)adaptor.create(ASSIGN25);
            adaptor.addChild(root_0, ASSIGN25_tree);
            }
            // io/protostuff/parser/ProtoParser.g:106:27: (vr= var_reserved | ID | fid= FULL_ID | NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE | STRING_LITERAL )
            int alt7=9;
            switch ( input.LA(1) ) {
            case TO:
            case PKG:
            case SYNTAX:
            case IMPORT:
            case OPTION:
            case MESSAGE:
            case SERVICE:
            case ENUM:
            case REQUIRED:
            case OPTIONAL:
            case REPEATED:
            case EXTENSIONS:
            case EXTEND:
            case GROUP:
            case RPC:
            case RETURNS:
            case INT32:
            case INT64:
            case UINT32:
            case UINT64:
            case SINT32:
            case SINT64:
            case FIXED32:
            case FIXED64:
            case SFIXED32:
            case SFIXED64:
            case FLOAT:
            case DOUBLE:
            case BOOL:
            case STRING:
            case BYTES:
            case DEFAULT:
            case MAX:
            case VOID:
                {
                alt7=1;
                }
                break;
            case ID:
                {
                alt7=2;
                }
                break;
            case FULL_ID:
                {
                alt7=3;
                }
                break;
            case NUMFLOAT:
                {
                alt7=4;
                }
                break;
            case NUMINT:
                {
                alt7=5;
                }
                break;
            case NUMDOUBLE:
                {
                alt7=6;
                }
                break;
            case TRUE:
                {
                alt7=7;
                }
                break;
            case FALSE:
                {
                alt7=8;
                }
                break;
            case STRING_LITERAL:
                {
                alt7=9;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }

            switch (alt7) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:107:17: vr= var_reserved
                    {
                    pushFollow(FOLLOW_var_reserved_in_annotation_keyval670);
                    vr=var_reserved();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, vr.getTree());
                    if ( state.backtracking==0 ) {
                       annotation.put((k!=null?input.toString(k.start,k.stop):null), (vr!=null?input.toString(vr.start,vr.stop):null)); 
                    }

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:108:17: ID
                    {
                    ID26=(Token)match(input,ID,FOLLOW_ID_in_annotation_keyval690); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ID26_tree = (Object)adaptor.create(ID26);
                    adaptor.addChild(root_0, ID26_tree);
                    }
                    if ( state.backtracking==0 ) {
                       annotation.putRef((k!=null?input.toString(k.start,k.stop):null), (ID26!=null?ID26.getText():null)); 
                    }

                    }
                    break;
                case 3 :
                    // io/protostuff/parser/ProtoParser.g:109:17: fid= FULL_ID
                    {
                    fid=(Token)match(input,FULL_ID,FOLLOW_FULL_ID_in_annotation_keyval712); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    fid_tree = (Object)adaptor.create(fid);
                    adaptor.addChild(root_0, fid_tree);
                    }
                    if ( state.backtracking==0 ) {
                       annotation.putRef((k!=null?input.toString(k.start,k.stop):null), (fid!=null?fid.getText():null)); 
                    }

                    }
                    break;
                case 4 :
                    // io/protostuff/parser/ProtoParser.g:110:17: NUMFLOAT
                    {
                    NUMFLOAT27=(Token)match(input,NUMFLOAT,FOLLOW_NUMFLOAT_in_annotation_keyval732); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMFLOAT27_tree = (Object)adaptor.create(NUMFLOAT27);
                    adaptor.addChild(root_0, NUMFLOAT27_tree);
                    }
                    if ( state.backtracking==0 ) {
                       annotation.put((k!=null?input.toString(k.start,k.stop):null), Float.valueOf((NUMFLOAT27!=null?NUMFLOAT27.getText():null))); 
                    }

                    }
                    break;
                case 5 :
                    // io/protostuff/parser/ProtoParser.g:111:17: NUMINT
                    {
                    NUMINT28=(Token)match(input,NUMINT,FOLLOW_NUMINT_in_annotation_keyval752); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMINT28_tree = (Object)adaptor.create(NUMINT28);
                    adaptor.addChild(root_0, NUMINT28_tree);
                    }
                    if ( state.backtracking==0 ) {
                       annotation.put((k!=null?input.toString(k.start,k.stop):null), Integer.valueOf((NUMINT28!=null?NUMINT28.getText():null))); 
                    }

                    }
                    break;
                case 6 :
                    // io/protostuff/parser/ProtoParser.g:112:17: NUMDOUBLE
                    {
                    NUMDOUBLE29=(Token)match(input,NUMDOUBLE,FOLLOW_NUMDOUBLE_in_annotation_keyval772); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMDOUBLE29_tree = (Object)adaptor.create(NUMDOUBLE29);
                    adaptor.addChild(root_0, NUMDOUBLE29_tree);
                    }
                    if ( state.backtracking==0 ) {
                       annotation.put((k!=null?input.toString(k.start,k.stop):null), Double.valueOf((NUMDOUBLE29!=null?NUMDOUBLE29.getText():null))); 
                    }

                    }
                    break;
                case 7 :
                    // io/protostuff/parser/ProtoParser.g:113:17: TRUE
                    {
                    TRUE30=(Token)match(input,TRUE,FOLLOW_TRUE_in_annotation_keyval792); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    TRUE30_tree = (Object)adaptor.create(TRUE30);
                    adaptor.addChild(root_0, TRUE30_tree);
                    }
                    if ( state.backtracking==0 ) {
                       annotation.put((k!=null?input.toString(k.start,k.stop):null), Boolean.TRUE); 
                    }

                    }
                    break;
                case 8 :
                    // io/protostuff/parser/ProtoParser.g:114:17: FALSE
                    {
                    FALSE31=(Token)match(input,FALSE,FOLLOW_FALSE_in_annotation_keyval812); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FALSE31_tree = (Object)adaptor.create(FALSE31);
                    adaptor.addChild(root_0, FALSE31_tree);
                    }
                    if ( state.backtracking==0 ) {
                       annotation.put((k!=null?input.toString(k.start,k.stop):null), Boolean.FALSE); 
                    }

                    }
                    break;
                case 9 :
                    // io/protostuff/parser/ProtoParser.g:115:17: STRING_LITERAL
                    {
                    STRING_LITERAL32=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_annotation_keyval832); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING_LITERAL32_tree = (Object)adaptor.create(STRING_LITERAL32);
                    adaptor.addChild(root_0, STRING_LITERAL32_tree);
                    }
                    if ( state.backtracking==0 ) {
                       annotation.put((k!=null?input.toString(k.start,k.stop):null), getStringFromStringLiteral((STRING_LITERAL32!=null?STRING_LITERAL32.getText():null))); 
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "annotation_keyval"

    public static class doc_entry_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "doc_entry"
    // io/protostuff/parser/ProtoParser.g:119:1: doc_entry[Proto proto] : DOC ;
    public final ProtoParser.doc_entry_return doc_entry(Proto proto) throws RecognitionException {
        ProtoParser.doc_entry_return retval = new ProtoParser.doc_entry_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token DOC33=null;

        Object DOC33_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:120:5: ( DOC )
            // io/protostuff/parser/ProtoParser.g:120:9: DOC
            {
            root_0 = (Object)adaptor.nil();

            DOC33=(Token)match(input,DOC,FOLLOW_DOC_in_doc_entry865); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            DOC33_tree = (Object)adaptor.create(DOC33);
            adaptor.addChild(root_0, DOC33_tree);
            }
            if ( state.backtracking==0 ) {

                          proto.addDoc((DOC33!=null?DOC33.getText():null).substring(3).trim());
                      
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "doc_entry"

    public static class header_syntax_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "header_syntax"
    // io/protostuff/parser/ProtoParser.g:125:1: header_syntax[Proto proto] : SYNTAX ASSIGN STRING_LITERAL SEMICOLON ;
    public final ProtoParser.header_syntax_return header_syntax(Proto proto) throws RecognitionException {
        ProtoParser.header_syntax_return retval = new ProtoParser.header_syntax_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SYNTAX34=null;
        Token ASSIGN35=null;
        Token STRING_LITERAL36=null;
        Token SEMICOLON37=null;

        Object SYNTAX34_tree=null;
        Object ASSIGN35_tree=null;
        Object STRING_LITERAL36_tree=null;
        Object SEMICOLON37_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:126:5: ( SYNTAX ASSIGN STRING_LITERAL SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:126:9: SYNTAX ASSIGN STRING_LITERAL SEMICOLON
            {
            root_0 = (Object)adaptor.nil();

            SYNTAX34=(Token)match(input,SYNTAX,FOLLOW_SYNTAX_in_header_syntax888); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            SYNTAX34_tree = (Object)adaptor.create(SYNTAX34);
            adaptor.addChild(root_0, SYNTAX34_tree);
            }
            ASSIGN35=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_header_syntax890); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ASSIGN35_tree = (Object)adaptor.create(ASSIGN35);
            adaptor.addChild(root_0, ASSIGN35_tree);
            }
            STRING_LITERAL36=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_header_syntax892); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            STRING_LITERAL36_tree = (Object)adaptor.create(STRING_LITERAL36);
            adaptor.addChild(root_0, STRING_LITERAL36_tree);
            }
            SEMICOLON37=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_header_syntax894); if (state.failed) return retval;
            if ( state.backtracking==0 ) {

                          if(!"proto2".equals(getStringFromStringLiteral((STRING_LITERAL36!=null?STRING_LITERAL36.getText():null)))) {
                              throw new IllegalStateException("Syntax isn't proto2: '" +
                                getStringFromStringLiteral((STRING_LITERAL36!=null?STRING_LITERAL36.getText():null))+"'");
                          }
                                
                          if(!proto.annotations.isEmpty())
                              throw new IllegalStateException("Misplaced annotations: " + proto.annotations);
                          if(!proto.docs.isEmpty())
                              throw new IllegalStateException("Misplaced docs: " + proto.docs);
                      
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "header_syntax"

    public static class header_package_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "header_package"
    // io/protostuff/parser/ProtoParser.g:139:1: header_package[Proto proto] : PKG ( FULL_ID | var ) SEMICOLON ;
    public final ProtoParser.header_package_return header_package(Proto proto) throws RecognitionException {
        ProtoParser.header_package_return retval = new ProtoParser.header_package_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token PKG38=null;
        Token FULL_ID39=null;
        Token SEMICOLON41=null;
        ProtoParser.var_return var40 = null;


        Object PKG38_tree=null;
        Object FULL_ID39_tree=null;
        Object SEMICOLON41_tree=null;


            String value = null;

        try {
            // io/protostuff/parser/ProtoParser.g:143:5: ( PKG ( FULL_ID | var ) SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:143:9: PKG ( FULL_ID | var ) SEMICOLON
            {
            root_0 = (Object)adaptor.nil();

            PKG38=(Token)match(input,PKG,FOLLOW_PKG_in_header_package923); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            PKG38_tree = (Object)adaptor.create(PKG38);
            adaptor.addChild(root_0, PKG38_tree);
            }
            // io/protostuff/parser/ProtoParser.g:143:13: ( FULL_ID | var )
            int alt8=2;
            switch ( input.LA(1) ) {
            case FULL_ID:
                {
                alt8=1;
                }
                break;
            case TO:
            case PKG:
            case SYNTAX:
            case IMPORT:
            case OPTION:
            case MESSAGE:
            case SERVICE:
            case ENUM:
            case REQUIRED:
            case OPTIONAL:
            case REPEATED:
            case EXTENSIONS:
            case EXTEND:
            case GROUP:
            case RPC:
            case RETURNS:
            case INT32:
            case INT64:
            case UINT32:
            case UINT64:
            case SINT32:
            case SINT64:
            case FIXED32:
            case FIXED64:
            case SFIXED32:
            case SFIXED64:
            case FLOAT:
            case DOUBLE:
            case BOOL:
            case STRING:
            case BYTES:
            case DEFAULT:
            case MAX:
            case VOID:
            case ID:
                {
                alt8=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }

            switch (alt8) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:143:14: FULL_ID
                    {
                    FULL_ID39=(Token)match(input,FULL_ID,FOLLOW_FULL_ID_in_header_package926); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FULL_ID39_tree = (Object)adaptor.create(FULL_ID39);
                    adaptor.addChild(root_0, FULL_ID39_tree);
                    }
                    if ( state.backtracking==0 ) {
                       value = (FULL_ID39!=null?FULL_ID39.getText():null); 
                    }

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:143:51: var
                    {
                    pushFollow(FOLLOW_var_in_header_package932);
                    var40=var();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, var40.getTree());
                    if ( state.backtracking==0 ) {
                       value = (var40!=null?input.toString(var40.start,var40.stop):null); 
                    }

                    }
                    break;

            }

            SEMICOLON41=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_header_package937); if (state.failed) return retval;
            if ( state.backtracking==0 ) {

                          if(proto.getPackageName() != null)
                              throw new IllegalStateException("Multiple package definitions.");
                          
                          proto.setPackageName(value);
                          
                          if(!proto.annotations.isEmpty())
                              throw new IllegalStateException("Misplaced annotations: " + proto.annotations);
                          if(!proto.docs.isEmpty())
                              throw new IllegalStateException("Misplaced docs: " + proto.docs);
                      
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "header_package"

    public static class header_import_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "header_import"
    // io/protostuff/parser/ProtoParser.g:156:1: header_import[Proto proto] : IMPORT STRING_LITERAL SEMICOLON ;
    public final ProtoParser.header_import_return header_import(Proto proto) throws RecognitionException {
        ProtoParser.header_import_return retval = new ProtoParser.header_import_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token IMPORT42=null;
        Token STRING_LITERAL43=null;
        Token SEMICOLON44=null;

        Object IMPORT42_tree=null;
        Object STRING_LITERAL43_tree=null;
        Object SEMICOLON44_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:157:5: ( IMPORT STRING_LITERAL SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:157:9: IMPORT STRING_LITERAL SEMICOLON
            {
            root_0 = (Object)adaptor.nil();

            IMPORT42=(Token)match(input,IMPORT,FOLLOW_IMPORT_in_header_import965); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            IMPORT42_tree = (Object)adaptor.create(IMPORT42);
            adaptor.addChild(root_0, IMPORT42_tree);
            }
            STRING_LITERAL43=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_header_import967); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            STRING_LITERAL43_tree = (Object)adaptor.create(STRING_LITERAL43);
            adaptor.addChild(root_0, STRING_LITERAL43_tree);
            }
            SEMICOLON44=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_header_import969); if (state.failed) return retval;
            if ( state.backtracking==0 ) {

                          proto.importProto(getStringFromStringLiteral((STRING_LITERAL43!=null?STRING_LITERAL43.getText():null)));
                          
                          if(!proto.annotations.isEmpty())
                              throw new IllegalStateException("Misplaced annotations: " + proto.annotations);
                          if(!proto.docs.isEmpty())
                              throw new IllegalStateException("Misplaced docs: " + proto.docs);
                      
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "header_import"

    public static class option_entry_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "option_entry"
    // io/protostuff/parser/ProtoParser.g:167:1: option_entry[Proto proto, HasOptions ho] : OPTION ( LEFTPAREN )? k= var_full ( RIGHTPAREN )? ASSIGN (vr= var_reserved | id= ID | fid= FULL_ID | NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE | STRING_LITERAL ) SEMICOLON ;
    public final ProtoParser.option_entry_return option_entry(Proto proto, HasOptions ho) throws RecognitionException {
        ProtoParser.option_entry_return retval = new ProtoParser.option_entry_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token id=null;
        Token fid=null;
        Token OPTION45=null;
        Token LEFTPAREN46=null;
        Token RIGHTPAREN47=null;
        Token ASSIGN48=null;
        Token NUMFLOAT49=null;
        Token NUMINT50=null;
        Token NUMDOUBLE51=null;
        Token TRUE52=null;
        Token FALSE53=null;
        Token STRING_LITERAL54=null;
        Token SEMICOLON55=null;
        ProtoParser.var_full_return k = null;

        ProtoParser.var_reserved_return vr = null;


        Object id_tree=null;
        Object fid_tree=null;
        Object OPTION45_tree=null;
        Object LEFTPAREN46_tree=null;
        Object RIGHTPAREN47_tree=null;
        Object ASSIGN48_tree=null;
        Object NUMFLOAT49_tree=null;
        Object NUMINT50_tree=null;
        Object NUMDOUBLE51_tree=null;
        Object TRUE52_tree=null;
        Object FALSE53_tree=null;
        Object STRING_LITERAL54_tree=null;
        Object SEMICOLON55_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:168:5: ( OPTION ( LEFTPAREN )? k= var_full ( RIGHTPAREN )? ASSIGN (vr= var_reserved | id= ID | fid= FULL_ID | NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE | STRING_LITERAL ) SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:168:9: OPTION ( LEFTPAREN )? k= var_full ( RIGHTPAREN )? ASSIGN (vr= var_reserved | id= ID | fid= FULL_ID | NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE | STRING_LITERAL ) SEMICOLON
            {
            root_0 = (Object)adaptor.nil();

            OPTION45=(Token)match(input,OPTION,FOLLOW_OPTION_in_option_entry993); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            OPTION45_tree = (Object)adaptor.create(OPTION45);
            adaptor.addChild(root_0, OPTION45_tree);
            }
            // io/protostuff/parser/ProtoParser.g:168:16: ( LEFTPAREN )?
            int alt9=2;
            switch ( input.LA(1) ) {
                case LEFTPAREN:
                    {
                    alt9=1;
                    }
                    break;
            }

            switch (alt9) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:168:16: LEFTPAREN
                    {
                    LEFTPAREN46=(Token)match(input,LEFTPAREN,FOLLOW_LEFTPAREN_in_option_entry995); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    LEFTPAREN46_tree = (Object)adaptor.create(LEFTPAREN46);
                    adaptor.addChild(root_0, LEFTPAREN46_tree);
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_var_full_in_option_entry1000);
            k=var_full();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, k.getTree());
            // io/protostuff/parser/ProtoParser.g:168:38: ( RIGHTPAREN )?
            int alt10=2;
            switch ( input.LA(1) ) {
                case RIGHTPAREN:
                    {
                    alt10=1;
                    }
                    break;
            }

            switch (alt10) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:168:38: RIGHTPAREN
                    {
                    RIGHTPAREN47=(Token)match(input,RIGHTPAREN,FOLLOW_RIGHTPAREN_in_option_entry1002); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    RIGHTPAREN47_tree = (Object)adaptor.create(RIGHTPAREN47);
                    adaptor.addChild(root_0, RIGHTPAREN47_tree);
                    }

                    }
                    break;

            }

            ASSIGN48=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_option_entry1005); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ASSIGN48_tree = (Object)adaptor.create(ASSIGN48);
            adaptor.addChild(root_0, ASSIGN48_tree);
            }
            // io/protostuff/parser/ProtoParser.g:168:57: (vr= var_reserved | id= ID | fid= FULL_ID | NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE | STRING_LITERAL )
            int alt11=9;
            switch ( input.LA(1) ) {
            case TO:
            case PKG:
            case SYNTAX:
            case IMPORT:
            case OPTION:
            case MESSAGE:
            case SERVICE:
            case ENUM:
            case REQUIRED:
            case OPTIONAL:
            case REPEATED:
            case EXTENSIONS:
            case EXTEND:
            case GROUP:
            case RPC:
            case RETURNS:
            case INT32:
            case INT64:
            case UINT32:
            case UINT64:
            case SINT32:
            case SINT64:
            case FIXED32:
            case FIXED64:
            case SFIXED32:
            case SFIXED64:
            case FLOAT:
            case DOUBLE:
            case BOOL:
            case STRING:
            case BYTES:
            case DEFAULT:
            case MAX:
            case VOID:
                {
                alt11=1;
                }
                break;
            case ID:
                {
                alt11=2;
                }
                break;
            case FULL_ID:
                {
                alt11=3;
                }
                break;
            case NUMFLOAT:
                {
                alt11=4;
                }
                break;
            case NUMINT:
                {
                alt11=5;
                }
                break;
            case NUMDOUBLE:
                {
                alt11=6;
                }
                break;
            case TRUE:
                {
                alt11=7;
                }
                break;
            case FALSE:
                {
                alt11=8;
                }
                break;
            case STRING_LITERAL:
                {
                alt11=9;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }

            switch (alt11) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:169:17: vr= var_reserved
                    {
                    pushFollow(FOLLOW_var_reserved_in_option_entry1027);
                    vr=var_reserved();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, vr.getTree());
                    if ( state.backtracking==0 ) {
                       ho.putExtraOption((k!=null?input.toString(k.start,k.stop):null), (vr!=null?input.toString(vr.start,vr.stop):null)); 
                    }

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:170:17: id= ID
                    {
                    id=(Token)match(input,ID,FOLLOW_ID_in_option_entry1049); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    id_tree = (Object)adaptor.create(id);
                    adaptor.addChild(root_0, id_tree);
                    }
                    if ( state.backtracking==0 ) {
                       ho.putStandardOption((k!=null?input.toString(k.start,k.stop):null), (id!=null?id.getText():null)); 
                    }

                    }
                    break;
                case 3 :
                    // io/protostuff/parser/ProtoParser.g:171:17: fid= FULL_ID
                    {
                    fid=(Token)match(input,FULL_ID,FOLLOW_FULL_ID_in_option_entry1071); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    fid_tree = (Object)adaptor.create(fid);
                    adaptor.addChild(root_0, fid_tree);
                    }
                    if ( state.backtracking==0 ) {
                       ho.putStandardOption((k!=null?input.toString(k.start,k.stop):null), (fid!=null?fid.getText():null)); 
                    }

                    }
                    break;
                case 4 :
                    // io/protostuff/parser/ProtoParser.g:172:17: NUMFLOAT
                    {
                    NUMFLOAT49=(Token)match(input,NUMFLOAT,FOLLOW_NUMFLOAT_in_option_entry1091); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMFLOAT49_tree = (Object)adaptor.create(NUMFLOAT49);
                    adaptor.addChild(root_0, NUMFLOAT49_tree);
                    }
                    if ( state.backtracking==0 ) {
                       ho.putExtraOption((k!=null?input.toString(k.start,k.stop):null), Float.valueOf((NUMFLOAT49!=null?NUMFLOAT49.getText():null))); 
                    }

                    }
                    break;
                case 5 :
                    // io/protostuff/parser/ProtoParser.g:173:17: NUMINT
                    {
                    NUMINT50=(Token)match(input,NUMINT,FOLLOW_NUMINT_in_option_entry1111); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMINT50_tree = (Object)adaptor.create(NUMINT50);
                    adaptor.addChild(root_0, NUMINT50_tree);
                    }
                    if ( state.backtracking==0 ) {
                       ho.putExtraOption((k!=null?input.toString(k.start,k.stop):null), Integer.valueOf((NUMINT50!=null?NUMINT50.getText():null))); 
                    }

                    }
                    break;
                case 6 :
                    // io/protostuff/parser/ProtoParser.g:174:17: NUMDOUBLE
                    {
                    NUMDOUBLE51=(Token)match(input,NUMDOUBLE,FOLLOW_NUMDOUBLE_in_option_entry1131); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMDOUBLE51_tree = (Object)adaptor.create(NUMDOUBLE51);
                    adaptor.addChild(root_0, NUMDOUBLE51_tree);
                    }
                    if ( state.backtracking==0 ) {
                       ho.putExtraOption((k!=null?input.toString(k.start,k.stop):null), Double.valueOf((NUMDOUBLE51!=null?NUMDOUBLE51.getText():null))); 
                    }

                    }
                    break;
                case 7 :
                    // io/protostuff/parser/ProtoParser.g:175:17: TRUE
                    {
                    TRUE52=(Token)match(input,TRUE,FOLLOW_TRUE_in_option_entry1151); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    TRUE52_tree = (Object)adaptor.create(TRUE52);
                    adaptor.addChild(root_0, TRUE52_tree);
                    }
                    if ( state.backtracking==0 ) {
                       ho.putExtraOption((k!=null?input.toString(k.start,k.stop):null), Boolean.TRUE); 
                    }

                    }
                    break;
                case 8 :
                    // io/protostuff/parser/ProtoParser.g:176:17: FALSE
                    {
                    FALSE53=(Token)match(input,FALSE,FOLLOW_FALSE_in_option_entry1171); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FALSE53_tree = (Object)adaptor.create(FALSE53);
                    adaptor.addChild(root_0, FALSE53_tree);
                    }
                    if ( state.backtracking==0 ) {
                       ho.putExtraOption((k!=null?input.toString(k.start,k.stop):null), Boolean.FALSE); 
                    }

                    }
                    break;
                case 9 :
                    // io/protostuff/parser/ProtoParser.g:177:17: STRING_LITERAL
                    {
                    STRING_LITERAL54=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_option_entry1191); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING_LITERAL54_tree = (Object)adaptor.create(STRING_LITERAL54);
                    adaptor.addChild(root_0, STRING_LITERAL54_tree);
                    }
                    if ( state.backtracking==0 ) {
                       ho.putExtraOption((k!=null?input.toString(k.start,k.stop):null), getStringFromStringLiteral((STRING_LITERAL54!=null?STRING_LITERAL54.getText():null))); 
                    }

                    }
                    break;

            }

            SEMICOLON55=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_option_entry1205); if (state.failed) return retval;
            if ( state.backtracking==0 ) {

                          if(!proto.annotations.isEmpty())
                              throw new IllegalStateException("Misplaced annotations: " + proto.annotations);
                          if(!proto.docs.isEmpty())
                              throw new IllegalStateException("Misplaced docs: " + proto.docs);
                      
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "option_entry"

    public static class message_block_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "message_block"
    // io/protostuff/parser/ProtoParser.g:186:1: message_block[Proto proto, Message parent] : MESSAGE ID LEFTCURLY ( message_body[proto, message] )* RIGHTCURLY ;
    public final ProtoParser.message_block_return message_block(Proto proto, Message parent) throws RecognitionException {
        ProtoParser.message_block_return retval = new ProtoParser.message_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token MESSAGE56=null;
        Token ID57=null;
        Token LEFTCURLY58=null;
        Token RIGHTCURLY60=null;
        ProtoParser.message_body_return message_body59 = null;


        Object MESSAGE56_tree=null;
        Object ID57_tree=null;
        Object LEFTCURLY58_tree=null;
        Object RIGHTCURLY60_tree=null;


            Message message = null;

        try {
            // io/protostuff/parser/ProtoParser.g:190:5: ( MESSAGE ID LEFTCURLY ( message_body[proto, message] )* RIGHTCURLY )
            // io/protostuff/parser/ProtoParser.g:190:9: MESSAGE ID LEFTCURLY ( message_body[proto, message] )* RIGHTCURLY
            {
            root_0 = (Object)adaptor.nil();

            MESSAGE56=(Token)match(input,MESSAGE,FOLLOW_MESSAGE_in_message_block1238); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            MESSAGE56_tree = (Object)adaptor.create(MESSAGE56);
            adaptor.addChild(root_0, MESSAGE56_tree);
            }
            ID57=(Token)match(input,ID,FOLLOW_ID_in_message_block1240); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID57_tree = (Object)adaptor.create(ID57);
            adaptor.addChild(root_0, ID57_tree);
            }
            if ( state.backtracking==0 ) {
               
                          message = new Message((ID57!=null?ID57.getText():null), parent, proto);
                          proto.addAnnotationsTo(message);
                      
            }
            LEFTCURLY58=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_message_block1253); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEFTCURLY58_tree = (Object)adaptor.create(LEFTCURLY58);
            adaptor.addChild(root_0, LEFTCURLY58_tree);
            }
            // io/protostuff/parser/ProtoParser.g:194:19: ( message_body[proto, message] )*
            loop12:
            do {
                int alt12=2;
                switch ( input.LA(1) ) {
                case AT:
                case OPTION:
                case MESSAGE:
                case SERVICE:
                case ENUM:
                case REQUIRED:
                case OPTIONAL:
                case REPEATED:
                case EXTENSIONS:
                case EXTEND:
                case DOC:
                    {
                    alt12=1;
                    }
                    break;

                }

                switch (alt12) {
            	case 1 :
            	    // io/protostuff/parser/ProtoParser.g:194:20: message_body[proto, message]
            	    {
            	    pushFollow(FOLLOW_message_body_in_message_block1256);
            	    message_body59=message_body(proto, message);

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, message_body59.getTree());

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);

            RIGHTCURLY60=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_message_block1261); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            RIGHTCURLY60_tree = (Object)adaptor.create(RIGHTCURLY60);
            adaptor.addChild(root_0, RIGHTCURLY60_tree);
            }
            if ( state.backtracking==0 ) {

                          if(!proto.annotations.isEmpty())
                              throw new IllegalStateException("Misplaced annotations: " + proto.annotations);
                          if(!proto.docs.isEmpty())
                              throw new IllegalStateException("Misplaced docs: " + proto.docs);
                      
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "message_block"

    public static class message_body_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "message_body"
    // io/protostuff/parser/ProtoParser.g:202:1: message_body[Proto proto, Message message] : ( message_block[proto, message] | message_field[proto, message] | enum_block[proto, message] | service_block[proto, message] | extend_block[proto, message] | extensions_range[proto, message] | annotation_entry[proto] | doc_entry[proto] | option_entry[proto, message] );
    public final ProtoParser.message_body_return message_body(Proto proto, Message message) throws RecognitionException {
        ProtoParser.message_body_return retval = new ProtoParser.message_body_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ProtoParser.message_block_return message_block61 = null;

        ProtoParser.message_field_return message_field62 = null;

        ProtoParser.enum_block_return enum_block63 = null;

        ProtoParser.service_block_return service_block64 = null;

        ProtoParser.extend_block_return extend_block65 = null;

        ProtoParser.extensions_range_return extensions_range66 = null;

        ProtoParser.annotation_entry_return annotation_entry67 = null;

        ProtoParser.doc_entry_return doc_entry68 = null;

        ProtoParser.option_entry_return option_entry69 = null;



        try {
            // io/protostuff/parser/ProtoParser.g:203:5: ( message_block[proto, message] | message_field[proto, message] | enum_block[proto, message] | service_block[proto, message] | extend_block[proto, message] | extensions_range[proto, message] | annotation_entry[proto] | doc_entry[proto] | option_entry[proto, message] )
            int alt13=9;
            switch ( input.LA(1) ) {
            case MESSAGE:
                {
                alt13=1;
                }
                break;
            case REQUIRED:
            case OPTIONAL:
            case REPEATED:
                {
                alt13=2;
                }
                break;
            case ENUM:
                {
                alt13=3;
                }
                break;
            case SERVICE:
                {
                alt13=4;
                }
                break;
            case EXTEND:
                {
                alt13=5;
                }
                break;
            case EXTENSIONS:
                {
                alt13=6;
                }
                break;
            case AT:
                {
                alt13=7;
                }
                break;
            case DOC:
                {
                alt13=8;
                }
                break;
            case OPTION:
                {
                alt13=9;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:203:9: message_block[proto, message]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_message_block_in_message_body1284);
                    message_block61=message_block(proto, message);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, message_block61.getTree());

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:204:9: message_field[proto, message]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_message_field_in_message_body1295);
                    message_field62=message_field(proto, message);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, message_field62.getTree());

                    }
                    break;
                case 3 :
                    // io/protostuff/parser/ProtoParser.g:205:9: enum_block[proto, message]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_block_in_message_body1306);
                    enum_block63=enum_block(proto, message);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_block63.getTree());

                    }
                    break;
                case 4 :
                    // io/protostuff/parser/ProtoParser.g:206:9: service_block[proto, message]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_service_block_in_message_body1317);
                    service_block64=service_block(proto, message);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, service_block64.getTree());

                    }
                    break;
                case 5 :
                    // io/protostuff/parser/ProtoParser.g:207:9: extend_block[proto, message]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_extend_block_in_message_body1328);
                    extend_block65=extend_block(proto, message);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, extend_block65.getTree());

                    }
                    break;
                case 6 :
                    // io/protostuff/parser/ProtoParser.g:208:9: extensions_range[proto, message]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_extensions_range_in_message_body1339);
                    extensions_range66=extensions_range(proto, message);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, extensions_range66.getTree());

                    }
                    break;
                case 7 :
                    // io/protostuff/parser/ProtoParser.g:209:9: annotation_entry[proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_annotation_entry_in_message_body1350);
                    annotation_entry67=annotation_entry(proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotation_entry67.getTree());

                    }
                    break;
                case 8 :
                    // io/protostuff/parser/ProtoParser.g:210:9: doc_entry[proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_doc_entry_in_message_body1361);
                    doc_entry68=doc_entry(proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, doc_entry68.getTree());

                    }
                    break;
                case 9 :
                    // io/protostuff/parser/ProtoParser.g:211:9: option_entry[proto, message]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_option_entry_in_message_body1372);
                    option_entry69=option_entry(proto, message);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, option_entry69.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "message_body"

    public static class extensions_range_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "extensions_range"
    // io/protostuff/parser/ProtoParser.g:214:1: extensions_range[Proto proto, Message message] : EXTENSIONS f= NUMINT ( TO (l= NUMINT | MAX ) )? SEMICOLON ;
    public final ProtoParser.extensions_range_return extensions_range(Proto proto, Message message) throws RecognitionException {
        ProtoParser.extensions_range_return retval = new ProtoParser.extensions_range_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token f=null;
        Token l=null;
        Token EXTENSIONS70=null;
        Token TO71=null;
        Token MAX72=null;
        Token SEMICOLON73=null;

        Object f_tree=null;
        Object l_tree=null;
        Object EXTENSIONS70_tree=null;
        Object TO71_tree=null;
        Object MAX72_tree=null;
        Object SEMICOLON73_tree=null;


          int first = -1;
          int last = -1;

        try {
            // io/protostuff/parser/ProtoParser.g:219:5: ( EXTENSIONS f= NUMINT ( TO (l= NUMINT | MAX ) )? SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:219:9: EXTENSIONS f= NUMINT ( TO (l= NUMINT | MAX ) )? SEMICOLON
            {
            root_0 = (Object)adaptor.nil();

            EXTENSIONS70=(Token)match(input,EXTENSIONS,FOLLOW_EXTENSIONS_in_extensions_range1403); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            EXTENSIONS70_tree = (Object)adaptor.create(EXTENSIONS70);
            adaptor.addChild(root_0, EXTENSIONS70_tree);
            }
            f=(Token)match(input,NUMINT,FOLLOW_NUMINT_in_extensions_range1407); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            f_tree = (Object)adaptor.create(f);
            adaptor.addChild(root_0, f_tree);
            }
            if ( state.backtracking==0 ) {
               first = Integer.parseInt((f!=null?f.getText():null)); last = first;
            }
            // io/protostuff/parser/ProtoParser.g:220:9: ( TO (l= NUMINT | MAX ) )?
            int alt15=2;
            switch ( input.LA(1) ) {
                case TO:
                    {
                    alt15=1;
                    }
                    break;
            }

            switch (alt15) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:220:11: TO (l= NUMINT | MAX )
                    {
                    TO71=(Token)match(input,TO,FOLLOW_TO_in_extensions_range1421); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    TO71_tree = (Object)adaptor.create(TO71);
                    adaptor.addChild(root_0, TO71_tree);
                    }
                    // io/protostuff/parser/ProtoParser.g:220:14: (l= NUMINT | MAX )
                    int alt14=2;
                    switch ( input.LA(1) ) {
                    case NUMINT:
                        {
                        alt14=1;
                        }
                        break;
                    case MAX:
                        {
                        alt14=2;
                        }
                        break;
                    default:
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 14, 0, input);

                        throw nvae;
                    }

                    switch (alt14) {
                        case 1 :
                            // io/protostuff/parser/ProtoParser.g:220:16: l= NUMINT
                            {
                            l=(Token)match(input,NUMINT,FOLLOW_NUMINT_in_extensions_range1427); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            l_tree = (Object)adaptor.create(l);
                            adaptor.addChild(root_0, l_tree);
                            }
                            if ( state.backtracking==0 ) {
                               last = Integer.parseInt((l!=null?l.getText():null)); 
                            }

                            }
                            break;
                        case 2 :
                            // io/protostuff/parser/ProtoParser.g:220:65: MAX
                            {
                            MAX72=(Token)match(input,MAX,FOLLOW_MAX_in_extensions_range1433); if (state.failed) return retval;
                            if ( state.backtracking==0 ) {
                            MAX72_tree = (Object)adaptor.create(MAX72);
                            adaptor.addChild(root_0, MAX72_tree);
                            }
                            if ( state.backtracking==0 ) {
                              last = 536870911; 
                            }

                            }
                            break;

                    }


                    }
                    break;

            }

            SEMICOLON73=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_extensions_range1450); if (state.failed) return retval;
            if ( state.backtracking==0 ) {

                          message.defineExtensionRange(first, last);
                      
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "extensions_range"

    public static class message_field_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "message_field"
    // io/protostuff/parser/ProtoParser.g:226:1: message_field[Proto proto, HasFields message] : ( OPTIONAL | REQUIRED | REPEATED ) field_type[proto, message, fieldHolder] var ASSIGN NUMINT ( field_options[proto, message, fieldHolder.field] )? ( SEMICOLON | ignore_block ) ;
    public final ProtoParser.message_field_return message_field(Proto proto, HasFields message) throws RecognitionException {
        ProtoParser.message_field_return retval = new ProtoParser.message_field_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OPTIONAL74=null;
        Token REQUIRED75=null;
        Token REPEATED76=null;
        Token ASSIGN79=null;
        Token NUMINT80=null;
        Token SEMICOLON82=null;
        ProtoParser.field_type_return field_type77 = null;

        ProtoParser.var_return var78 = null;

        ProtoParser.field_options_return field_options81 = null;

        ProtoParser.ignore_block_return ignore_block83 = null;


        Object OPTIONAL74_tree=null;
        Object REQUIRED75_tree=null;
        Object REPEATED76_tree=null;
        Object ASSIGN79_tree=null;
        Object NUMINT80_tree=null;
        Object SEMICOLON82_tree=null;


            Field.Modifier modifier = null;
            FieldHolder fieldHolder = null;

        try {
            // io/protostuff/parser/ProtoParser.g:231:5: ( ( OPTIONAL | REQUIRED | REPEATED ) field_type[proto, message, fieldHolder] var ASSIGN NUMINT ( field_options[proto, message, fieldHolder.field] )? ( SEMICOLON | ignore_block ) )
            // io/protostuff/parser/ProtoParser.g:231:9: ( OPTIONAL | REQUIRED | REPEATED ) field_type[proto, message, fieldHolder] var ASSIGN NUMINT ( field_options[proto, message, fieldHolder.field] )? ( SEMICOLON | ignore_block )
            {
            root_0 = (Object)adaptor.nil();

            // io/protostuff/parser/ProtoParser.g:231:9: ( OPTIONAL | REQUIRED | REPEATED )
            int alt16=3;
            switch ( input.LA(1) ) {
            case OPTIONAL:
                {
                alt16=1;
                }
                break;
            case REQUIRED:
                {
                alt16=2;
                }
                break;
            case REPEATED:
                {
                alt16=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }

            switch (alt16) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:231:10: OPTIONAL
                    {
                    OPTIONAL74=(Token)match(input,OPTIONAL,FOLLOW_OPTIONAL_in_message_field1484); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    OPTIONAL74_tree = (Object)adaptor.create(OPTIONAL74);
                    adaptor.addChild(root_0, OPTIONAL74_tree);
                    }
                    if ( state.backtracking==0 ) {
                       modifier = Field.Modifier.OPTIONAL;  
                    }

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:232:13: REQUIRED
                    {
                    REQUIRED75=(Token)match(input,REQUIRED,FOLLOW_REQUIRED_in_message_field1501); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    REQUIRED75_tree = (Object)adaptor.create(REQUIRED75);
                    adaptor.addChild(root_0, REQUIRED75_tree);
                    }
                    if ( state.backtracking==0 ) {
                       modifier = Field.Modifier.REQUIRED; 
                    }

                    }
                    break;
                case 3 :
                    // io/protostuff/parser/ProtoParser.g:233:13: REPEATED
                    {
                    REPEATED76=(Token)match(input,REPEATED,FOLLOW_REPEATED_in_message_field1518); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    REPEATED76_tree = (Object)adaptor.create(REPEATED76);
                    adaptor.addChild(root_0, REPEATED76_tree);
                    }
                    if ( state.backtracking==0 ) {
                       modifier = Field.Modifier.REPEATED; 
                    }

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

                          fieldHolder = new FieldHolder();
                      
            }
            pushFollow(FOLLOW_field_type_in_message_field1533);
            field_type77=field_type(proto, message, fieldHolder);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, field_type77.getTree());
            pushFollow(FOLLOW_var_in_message_field1545);
            var78=var();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, var78.getTree());
            ASSIGN79=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_message_field1547); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ASSIGN79_tree = (Object)adaptor.create(ASSIGN79);
            adaptor.addChild(root_0, ASSIGN79_tree);
            }
            NUMINT80=(Token)match(input,NUMINT,FOLLOW_NUMINT_in_message_field1549); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NUMINT80_tree = (Object)adaptor.create(NUMINT80);
            adaptor.addChild(root_0, NUMINT80_tree);
            }
            if ( state.backtracking==0 ) {

                          if(fieldHolder.field != null) {
                              fieldHolder.field.modifier = modifier;
                              fieldHolder.field.name = (var78!=null?input.toString(var78.start,var78.stop):null);
                              fieldHolder.field.number = Integer.parseInt((NUMINT80!=null?NUMINT80.getText():null));
                          }
                      
            }
            // io/protostuff/parser/ProtoParser.g:244:9: ( field_options[proto, message, fieldHolder.field] )?
            int alt17=2;
            switch ( input.LA(1) ) {
                case LEFTSQUARE:
                    {
                    alt17=1;
                    }
                    break;
            }

            switch (alt17) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:244:10: field_options[proto, message, fieldHolder.field]
                    {
                    pushFollow(FOLLOW_field_options_in_message_field1563);
                    field_options81=field_options(proto, message, fieldHolder.field);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, field_options81.getTree());

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

                          if(fieldHolder.field != null) {
                              proto.addAnnotationsTo(fieldHolder.field, message.getEnclosingNamespace());
                              message.addField(fieldHolder.field);
                          }
                      
            }
            // io/protostuff/parser/ProtoParser.g:250:9: ( SEMICOLON | ignore_block )
            int alt18=2;
            switch ( input.LA(1) ) {
            case SEMICOLON:
                {
                alt18=1;
                }
                break;
            case LEFTCURLY:
                {
                alt18=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:250:10: SEMICOLON
                    {
                    SEMICOLON82=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_message_field1579); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:250:23: ignore_block
                    {
                    pushFollow(FOLLOW_ignore_block_in_message_field1584);
                    ignore_block83=ignore_block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, ignore_block83.getTree());

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "message_field"

    public static class field_type_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "field_type"
    // io/protostuff/parser/ProtoParser.g:253:1: field_type[Proto proto, HasFields message, FieldHolder fieldHolder] : ( INT32 | UINT32 | SINT32 | FIXED32 | SFIXED32 | INT64 | UINT64 | SINT64 | FIXED64 | SFIXED64 | FLOAT | DOUBLE | BOOL | STRING | BYTES | GROUP | FULL_ID | ID );
    public final ProtoParser.field_type_return field_type(Proto proto, HasFields message, FieldHolder fieldHolder) throws RecognitionException {
        ProtoParser.field_type_return retval = new ProtoParser.field_type_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token INT3284=null;
        Token UINT3285=null;
        Token SINT3286=null;
        Token FIXED3287=null;
        Token SFIXED3288=null;
        Token INT6489=null;
        Token UINT6490=null;
        Token SINT6491=null;
        Token FIXED6492=null;
        Token SFIXED6493=null;
        Token FLOAT94=null;
        Token DOUBLE95=null;
        Token BOOL96=null;
        Token STRING97=null;
        Token BYTES98=null;
        Token GROUP99=null;
        Token FULL_ID100=null;
        Token ID101=null;

        Object INT3284_tree=null;
        Object UINT3285_tree=null;
        Object SINT3286_tree=null;
        Object FIXED3287_tree=null;
        Object SFIXED3288_tree=null;
        Object INT6489_tree=null;
        Object UINT6490_tree=null;
        Object SINT6491_tree=null;
        Object FIXED6492_tree=null;
        Object SFIXED6493_tree=null;
        Object FLOAT94_tree=null;
        Object DOUBLE95_tree=null;
        Object BOOL96_tree=null;
        Object STRING97_tree=null;
        Object BYTES98_tree=null;
        Object GROUP99_tree=null;
        Object FULL_ID100_tree=null;
        Object ID101_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:254:5: ( INT32 | UINT32 | SINT32 | FIXED32 | SFIXED32 | INT64 | UINT64 | SINT64 | FIXED64 | SFIXED64 | FLOAT | DOUBLE | BOOL | STRING | BYTES | GROUP | FULL_ID | ID )
            int alt19=18;
            switch ( input.LA(1) ) {
            case INT32:
                {
                alt19=1;
                }
                break;
            case UINT32:
                {
                alt19=2;
                }
                break;
            case SINT32:
                {
                alt19=3;
                }
                break;
            case FIXED32:
                {
                alt19=4;
                }
                break;
            case SFIXED32:
                {
                alt19=5;
                }
                break;
            case INT64:
                {
                alt19=6;
                }
                break;
            case UINT64:
                {
                alt19=7;
                }
                break;
            case SINT64:
                {
                alt19=8;
                }
                break;
            case FIXED64:
                {
                alt19=9;
                }
                break;
            case SFIXED64:
                {
                alt19=10;
                }
                break;
            case FLOAT:
                {
                alt19=11;
                }
                break;
            case DOUBLE:
                {
                alt19=12;
                }
                break;
            case BOOL:
                {
                alt19=13;
                }
                break;
            case STRING:
                {
                alt19=14;
                }
                break;
            case BYTES:
                {
                alt19=15;
                }
                break;
            case GROUP:
                {
                alt19=16;
                }
                break;
            case FULL_ID:
                {
                alt19=17;
                }
                break;
            case ID:
                {
                alt19=18;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }

            switch (alt19) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:254:9: INT32
                    {
                    root_0 = (Object)adaptor.nil();

                    INT3284=(Token)match(input,INT32,FOLLOW_INT32_in_field_type1610); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT3284_tree = (Object)adaptor.create(INT3284);
                    adaptor.addChild(root_0, INT3284_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.Int32()); 
                    }

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:255:9: UINT32
                    {
                    root_0 = (Object)adaptor.nil();

                    UINT3285=(Token)match(input,UINT32,FOLLOW_UINT32_in_field_type1622); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    UINT3285_tree = (Object)adaptor.create(UINT3285);
                    adaptor.addChild(root_0, UINT3285_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.UInt32()); 
                    }

                    }
                    break;
                case 3 :
                    // io/protostuff/parser/ProtoParser.g:256:9: SINT32
                    {
                    root_0 = (Object)adaptor.nil();

                    SINT3286=(Token)match(input,SINT32,FOLLOW_SINT32_in_field_type1634); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SINT3286_tree = (Object)adaptor.create(SINT3286);
                    adaptor.addChild(root_0, SINT3286_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.SInt32()); 
                    }

                    }
                    break;
                case 4 :
                    // io/protostuff/parser/ProtoParser.g:257:9: FIXED32
                    {
                    root_0 = (Object)adaptor.nil();

                    FIXED3287=(Token)match(input,FIXED32,FOLLOW_FIXED32_in_field_type1646); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FIXED3287_tree = (Object)adaptor.create(FIXED3287);
                    adaptor.addChild(root_0, FIXED3287_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.Fixed32()); 
                    }

                    }
                    break;
                case 5 :
                    // io/protostuff/parser/ProtoParser.g:258:9: SFIXED32
                    {
                    root_0 = (Object)adaptor.nil();

                    SFIXED3288=(Token)match(input,SFIXED32,FOLLOW_SFIXED32_in_field_type1658); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SFIXED3288_tree = (Object)adaptor.create(SFIXED3288);
                    adaptor.addChild(root_0, SFIXED3288_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.SFixed32()); 
                    }

                    }
                    break;
                case 6 :
                    // io/protostuff/parser/ProtoParser.g:259:9: INT64
                    {
                    root_0 = (Object)adaptor.nil();

                    INT6489=(Token)match(input,INT64,FOLLOW_INT64_in_field_type1670); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    INT6489_tree = (Object)adaptor.create(INT6489);
                    adaptor.addChild(root_0, INT6489_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.Int64()); 
                    }

                    }
                    break;
                case 7 :
                    // io/protostuff/parser/ProtoParser.g:260:9: UINT64
                    {
                    root_0 = (Object)adaptor.nil();

                    UINT6490=(Token)match(input,UINT64,FOLLOW_UINT64_in_field_type1682); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    UINT6490_tree = (Object)adaptor.create(UINT6490);
                    adaptor.addChild(root_0, UINT6490_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.UInt64()); 
                    }

                    }
                    break;
                case 8 :
                    // io/protostuff/parser/ProtoParser.g:261:9: SINT64
                    {
                    root_0 = (Object)adaptor.nil();

                    SINT6491=(Token)match(input,SINT64,FOLLOW_SINT64_in_field_type1694); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SINT6491_tree = (Object)adaptor.create(SINT6491);
                    adaptor.addChild(root_0, SINT6491_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.SInt64()); 
                    }

                    }
                    break;
                case 9 :
                    // io/protostuff/parser/ProtoParser.g:262:9: FIXED64
                    {
                    root_0 = (Object)adaptor.nil();

                    FIXED6492=(Token)match(input,FIXED64,FOLLOW_FIXED64_in_field_type1706); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FIXED6492_tree = (Object)adaptor.create(FIXED6492);
                    adaptor.addChild(root_0, FIXED6492_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.Fixed64()); 
                    }

                    }
                    break;
                case 10 :
                    // io/protostuff/parser/ProtoParser.g:263:9: SFIXED64
                    {
                    root_0 = (Object)adaptor.nil();

                    SFIXED6493=(Token)match(input,SFIXED64,FOLLOW_SFIXED64_in_field_type1718); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SFIXED6493_tree = (Object)adaptor.create(SFIXED6493);
                    adaptor.addChild(root_0, SFIXED6493_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.SFixed64()); 
                    }

                    }
                    break;
                case 11 :
                    // io/protostuff/parser/ProtoParser.g:264:9: FLOAT
                    {
                    root_0 = (Object)adaptor.nil();

                    FLOAT94=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_field_type1730); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FLOAT94_tree = (Object)adaptor.create(FLOAT94);
                    adaptor.addChild(root_0, FLOAT94_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.Float()); 
                    }

                    }
                    break;
                case 12 :
                    // io/protostuff/parser/ProtoParser.g:265:9: DOUBLE
                    {
                    root_0 = (Object)adaptor.nil();

                    DOUBLE95=(Token)match(input,DOUBLE,FOLLOW_DOUBLE_in_field_type1742); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    DOUBLE95_tree = (Object)adaptor.create(DOUBLE95);
                    adaptor.addChild(root_0, DOUBLE95_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.Double()); 
                    }

                    }
                    break;
                case 13 :
                    // io/protostuff/parser/ProtoParser.g:266:9: BOOL
                    {
                    root_0 = (Object)adaptor.nil();

                    BOOL96=(Token)match(input,BOOL,FOLLOW_BOOL_in_field_type1754); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    BOOL96_tree = (Object)adaptor.create(BOOL96);
                    adaptor.addChild(root_0, BOOL96_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.Bool()); 
                    }

                    }
                    break;
                case 14 :
                    // io/protostuff/parser/ProtoParser.g:267:9: STRING
                    {
                    root_0 = (Object)adaptor.nil();

                    STRING97=(Token)match(input,STRING,FOLLOW_STRING_in_field_type1766); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING97_tree = (Object)adaptor.create(STRING97);
                    adaptor.addChild(root_0, STRING97_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.String()); 
                    }

                    }
                    break;
                case 15 :
                    // io/protostuff/parser/ProtoParser.g:268:9: BYTES
                    {
                    root_0 = (Object)adaptor.nil();

                    BYTES98=(Token)match(input,BYTES,FOLLOW_BYTES_in_field_type1778); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    BYTES98_tree = (Object)adaptor.create(BYTES98);
                    adaptor.addChild(root_0, BYTES98_tree);
                    }
                    if ( state.backtracking==0 ) {
                       fieldHolder.setField(new Field.Bytes()); 
                    }

                    }
                    break;
                case 16 :
                    // io/protostuff/parser/ProtoParser.g:269:9: GROUP
                    {
                    root_0 = (Object)adaptor.nil();

                    GROUP99=(Token)match(input,GROUP,FOLLOW_GROUP_in_field_type1790); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    GROUP99_tree = (Object)adaptor.create(GROUP99);
                    adaptor.addChild(root_0, GROUP99_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  String suffix = proto.getFile()==null ? "" : " of " + proto.getFile().getName();
                                  warn("'group' not supported @ line " + (GROUP99!=null?GROUP99.getLine():0) + suffix);
                              
                    }

                    }
                    break;
                case 17 :
                    // io/protostuff/parser/ProtoParser.g:273:9: FULL_ID
                    {
                    root_0 = (Object)adaptor.nil();

                    FULL_ID100=(Token)match(input,FULL_ID,FOLLOW_FULL_ID_in_field_type1802); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FULL_ID100_tree = (Object)adaptor.create(FULL_ID100);
                    adaptor.addChild(root_0, FULL_ID100_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  String fullType = (FULL_ID100!=null?FULL_ID100.getText():null);
                                  int lastDot = fullType.lastIndexOf('.');
                                  String packageName = fullType.substring(0, lastDot); 
                                  String type = fullType.substring(lastDot+1);
                                  fieldHolder.setField(new Field.Reference(packageName, type, message));
                              
                    }

                    }
                    break;
                case 18 :
                    // io/protostuff/parser/ProtoParser.g:280:9: ID
                    {
                    root_0 = (Object)adaptor.nil();

                    ID101=(Token)match(input,ID,FOLLOW_ID_in_field_type1814); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ID101_tree = (Object)adaptor.create(ID101);
                    adaptor.addChild(root_0, ID101_tree);
                    }
                    if ( state.backtracking==0 ) {
                       
                                  String type = (ID101!=null?ID101.getText():null);
                                  fieldHolder.setField(new Field.Reference(null, type, message));
                              
                    }

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "field_type"

    public static class field_options_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "field_options"
    // io/protostuff/parser/ProtoParser.g:286:1: field_options[Proto proto, HasFields message, Field field] : LEFTSQUARE field_options_keyval[proto, message, field, true] ( COMMA field_options_keyval[proto, message, field, true] )* RIGHTSQUARE ;
    public final ProtoParser.field_options_return field_options(Proto proto, HasFields message, Field field) throws RecognitionException {
        ProtoParser.field_options_return retval = new ProtoParser.field_options_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LEFTSQUARE102=null;
        Token COMMA104=null;
        Token RIGHTSQUARE106=null;
        ProtoParser.field_options_keyval_return field_options_keyval103 = null;

        ProtoParser.field_options_keyval_return field_options_keyval105 = null;


        Object LEFTSQUARE102_tree=null;
        Object COMMA104_tree=null;
        Object RIGHTSQUARE106_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:287:5: ( LEFTSQUARE field_options_keyval[proto, message, field, true] ( COMMA field_options_keyval[proto, message, field, true] )* RIGHTSQUARE )
            // io/protostuff/parser/ProtoParser.g:287:9: LEFTSQUARE field_options_keyval[proto, message, field, true] ( COMMA field_options_keyval[proto, message, field, true] )* RIGHTSQUARE
            {
            root_0 = (Object)adaptor.nil();

            LEFTSQUARE102=(Token)match(input,LEFTSQUARE,FOLLOW_LEFTSQUARE_in_field_options1841); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEFTSQUARE102_tree = (Object)adaptor.create(LEFTSQUARE102);
            adaptor.addChild(root_0, LEFTSQUARE102_tree);
            }
            pushFollow(FOLLOW_field_options_keyval_in_field_options1843);
            field_options_keyval103=field_options_keyval(proto, message, field, true);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, field_options_keyval103.getTree());
            // io/protostuff/parser/ProtoParser.g:288:9: ( COMMA field_options_keyval[proto, message, field, true] )*
            loop20:
            do {
                int alt20=2;
                switch ( input.LA(1) ) {
                case COMMA:
                    {
                    alt20=1;
                    }
                    break;

                }

                switch (alt20) {
            	case 1 :
            	    // io/protostuff/parser/ProtoParser.g:288:10: COMMA field_options_keyval[proto, message, field, true]
            	    {
            	    COMMA104=(Token)match(input,COMMA,FOLLOW_COMMA_in_field_options1856); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    COMMA104_tree = (Object)adaptor.create(COMMA104);
            	    adaptor.addChild(root_0, COMMA104_tree);
            	    }
            	    pushFollow(FOLLOW_field_options_keyval_in_field_options1858);
            	    field_options_keyval105=field_options_keyval(proto, message, field, true);

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, field_options_keyval105.getTree());

            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);

            RIGHTSQUARE106=(Token)match(input,RIGHTSQUARE,FOLLOW_RIGHTSQUARE_in_field_options1863); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            RIGHTSQUARE106_tree = (Object)adaptor.create(RIGHTSQUARE106);
            adaptor.addChild(root_0, RIGHTSQUARE106_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "field_options"

    public static class field_options_keyval_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "field_options_keyval"
    // io/protostuff/parser/ProtoParser.g:291:1: field_options_keyval[Proto proto, HasFields message, Field field, boolean checkDefault] : key= var_full ASSIGN (vr= var_reserved | STRING_LITERAL | NUMFLOAT | NUMINT | NUMDOUBLE | HEX | OCTAL | TRUE | FALSE | val= ID | FULL_ID | EXP | signed_constant[proto, message, field, $key.text, checkDefault] ) ;
    public final ProtoParser.field_options_keyval_return field_options_keyval(Proto proto, HasFields message, Field field, boolean checkDefault) throws RecognitionException {
        ProtoParser.field_options_keyval_return retval = new ProtoParser.field_options_keyval_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token val=null;
        Token ASSIGN107=null;
        Token STRING_LITERAL108=null;
        Token NUMFLOAT109=null;
        Token NUMINT110=null;
        Token NUMDOUBLE111=null;
        Token HEX112=null;
        Token OCTAL113=null;
        Token TRUE114=null;
        Token FALSE115=null;
        Token FULL_ID116=null;
        Token EXP117=null;
        ProtoParser.var_full_return key = null;

        ProtoParser.var_reserved_return vr = null;

        ProtoParser.signed_constant_return signed_constant118 = null;


        Object val_tree=null;
        Object ASSIGN107_tree=null;
        Object STRING_LITERAL108_tree=null;
        Object NUMFLOAT109_tree=null;
        Object NUMINT110_tree=null;
        Object NUMDOUBLE111_tree=null;
        Object HEX112_tree=null;
        Object OCTAL113_tree=null;
        Object TRUE114_tree=null;
        Object FALSE115_tree=null;
        Object FULL_ID116_tree=null;
        Object EXP117_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:292:5: (key= var_full ASSIGN (vr= var_reserved | STRING_LITERAL | NUMFLOAT | NUMINT | NUMDOUBLE | HEX | OCTAL | TRUE | FALSE | val= ID | FULL_ID | EXP | signed_constant[proto, message, field, $key.text, checkDefault] ) )
            // io/protostuff/parser/ProtoParser.g:292:9: key= var_full ASSIGN (vr= var_reserved | STRING_LITERAL | NUMFLOAT | NUMINT | NUMDOUBLE | HEX | OCTAL | TRUE | FALSE | val= ID | FULL_ID | EXP | signed_constant[proto, message, field, $key.text, checkDefault] )
            {
            root_0 = (Object)adaptor.nil();

            pushFollow(FOLLOW_var_full_in_field_options_keyval1890);
            key=var_full();

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, key.getTree());
            ASSIGN107=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_field_options_keyval1892); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ASSIGN107_tree = (Object)adaptor.create(ASSIGN107);
            adaptor.addChild(root_0, ASSIGN107_tree);
            }
            // io/protostuff/parser/ProtoParser.g:292:29: (vr= var_reserved | STRING_LITERAL | NUMFLOAT | NUMINT | NUMDOUBLE | HEX | OCTAL | TRUE | FALSE | val= ID | FULL_ID | EXP | signed_constant[proto, message, field, $key.text, checkDefault] )
            int alt21=13;
            switch ( input.LA(1) ) {
            case TO:
            case PKG:
            case SYNTAX:
            case IMPORT:
            case OPTION:
            case MESSAGE:
            case SERVICE:
            case ENUM:
            case REQUIRED:
            case OPTIONAL:
            case REPEATED:
            case EXTENSIONS:
            case EXTEND:
            case GROUP:
            case RPC:
            case RETURNS:
            case INT32:
            case INT64:
            case UINT32:
            case UINT64:
            case SINT32:
            case SINT64:
            case FIXED32:
            case FIXED64:
            case SFIXED32:
            case SFIXED64:
            case FLOAT:
            case DOUBLE:
            case BOOL:
            case STRING:
            case BYTES:
            case DEFAULT:
            case MAX:
            case VOID:
                {
                alt21=1;
                }
                break;
            case STRING_LITERAL:
                {
                alt21=2;
                }
                break;
            case NUMFLOAT:
                {
                alt21=3;
                }
                break;
            case NUMINT:
                {
                alt21=4;
                }
                break;
            case NUMDOUBLE:
                {
                alt21=5;
                }
                break;
            case HEX:
                {
                alt21=6;
                }
                break;
            case OCTAL:
                {
                alt21=7;
                }
                break;
            case TRUE:
                {
                alt21=8;
                }
                break;
            case FALSE:
                {
                alt21=9;
                }
                break;
            case ID:
                {
                alt21=10;
                }
                break;
            case FULL_ID:
                {
                alt21=11;
                }
                break;
            case EXP:
                {
                alt21=12;
                }
                break;
            case MINUS:
                {
                alt21=13;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;
            }

            switch (alt21) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:292:30: vr= var_reserved
                    {
                    pushFollow(FOLLOW_var_reserved_in_field_options_keyval1897);
                    vr=var_reserved();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, vr.getTree());
                    if ( state.backtracking==0 ) {

                                  field.putExtraOption((key!=null?input.toString(key.start,key.stop):null), (vr!=null?input.toString(vr.start,vr.stop):null));
                              
                    }

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:295:9: STRING_LITERAL
                    {
                    STRING_LITERAL108=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_field_options_keyval1910); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    STRING_LITERAL108_tree = (Object)adaptor.create(STRING_LITERAL108);
                    adaptor.addChild(root_0, STRING_LITERAL108_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  if(checkDefault && "default".equals((key!=null?input.toString(key.start,key.stop):null))) {
                                      if(field.defaultValue!=null || field.modifier == Field.Modifier.REPEATED)
                                          throw new IllegalStateException("a field can only have a single default value");
                                      
                                      if(field instanceof Field.String)
                                          field.defaultValue = getStringFromStringLiteral((STRING_LITERAL108!=null?STRING_LITERAL108.getText():null));
                                      else if(field instanceof Field.Bytes)
                                          field.defaultValue = getBytesFromStringLiteral((STRING_LITERAL108!=null?STRING_LITERAL108.getText():null));
                                      else
                                          throw new IllegalStateException("Invalid string default value for the field: " + field.getClass().getSimpleName() + " " + field.name);
                                  }
                                  
                                  field.putExtraOption((key!=null?input.toString(key.start,key.stop):null), getStringFromStringLiteral((STRING_LITERAL108!=null?STRING_LITERAL108.getText():null)));
                              
                    }

                    }
                    break;
                case 3 :
                    // io/protostuff/parser/ProtoParser.g:310:9: NUMFLOAT
                    {
                    NUMFLOAT109=(Token)match(input,NUMFLOAT,FOLLOW_NUMFLOAT_in_field_options_keyval1922); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMFLOAT109_tree = (Object)adaptor.create(NUMFLOAT109);
                    adaptor.addChild(root_0, NUMFLOAT109_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  if(checkDefault && "default".equals((key!=null?input.toString(key.start,key.stop):null))) {
                                      if(field.defaultValue!=null || field.modifier == Field.Modifier.REPEATED)
                                          throw new IllegalStateException("a field can only have a single default value");
                                      
                                      if(field instanceof Field.Float)
                                          field.defaultValue = Float.valueOf((NUMFLOAT109!=null?NUMFLOAT109.getText():null));
                                      else if(field instanceof Field.Double) 
                                          field.defaultValue = Double.valueOf((NUMFLOAT109!=null?NUMFLOAT109.getText():null));
                                      else
                                          throw new IllegalStateException("Invalid float default value for the field: " + field.getClass().getSimpleName() + " " + field.name);
                                  }
                                  
                                  field.putExtraOption((key!=null?input.toString(key.start,key.stop):null), Float.valueOf((NUMFLOAT109!=null?NUMFLOAT109.getText():null)));
                              
                    }

                    }
                    break;
                case 4 :
                    // io/protostuff/parser/ProtoParser.g:325:9: NUMINT
                    {
                    NUMINT110=(Token)match(input,NUMINT,FOLLOW_NUMINT_in_field_options_keyval1935); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMINT110_tree = (Object)adaptor.create(NUMINT110);
                    adaptor.addChild(root_0, NUMINT110_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  if(checkDefault && "default".equals((key!=null?input.toString(key.start,key.stop):null))) {
                                      if(field.defaultValue!=null || field.modifier == Field.Modifier.REPEATED)
                                          throw new IllegalStateException("a field can only have a single default value");
                                      
                                      if(field instanceof Field.Number) {
                                          if(field.getClass().getSimpleName().endsWith("32"))
                                              field.defaultValue = Integer.valueOf((NUMINT110!=null?NUMINT110.getText():null));
                                          else if(field.getClass().getSimpleName().endsWith("64"))
                                              field.defaultValue = Long.valueOf((NUMINT110!=null?NUMINT110.getText():null));
                                          else if(field instanceof Field.Float)
                                              field.defaultValue = Float.valueOf((NUMINT110!=null?NUMINT110.getText():null));
                                          else if(field instanceof Field.Double) 
                                              field.defaultValue = Double.valueOf((NUMINT110!=null?NUMINT110.getText():null));
                                          else
                                              throw new IllegalStateException("Invalid numeric default value for the field: " + field.getClass().getSimpleName() + " " + field.name);
                                      }
                                      else
                                          throw new IllegalStateException("Invalid numeric default value for the field: " + field.getClass().getSimpleName() + " " + field.name);
                                  }
                                  
                                  field.putExtraOption((key!=null?input.toString(key.start,key.stop):null), Integer.valueOf((NUMINT110!=null?NUMINT110.getText():null)));
                              
                    }

                    }
                    break;
                case 5 :
                    // io/protostuff/parser/ProtoParser.g:348:9: NUMDOUBLE
                    {
                    NUMDOUBLE111=(Token)match(input,NUMDOUBLE,FOLLOW_NUMDOUBLE_in_field_options_keyval1947); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    NUMDOUBLE111_tree = (Object)adaptor.create(NUMDOUBLE111);
                    adaptor.addChild(root_0, NUMDOUBLE111_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  if(checkDefault && "default".equals((key!=null?input.toString(key.start,key.stop):null))) {
                                      if(field.defaultValue!=null || field.modifier == Field.Modifier.REPEATED)
                                          throw new IllegalStateException("a field can only have a single default value");

                                      if(field instanceof Field.Float)
                                          field.defaultValue = Float.valueOf((NUMDOUBLE111!=null?NUMDOUBLE111.getText():null));
                                      else if(field instanceof Field.Double) 
                                          field.defaultValue = Double.valueOf((NUMDOUBLE111!=null?NUMDOUBLE111.getText():null));
                                      else
                                          throw new IllegalStateException("Invalid numeric default value for the field: " + field.getClass().getSimpleName() + " " + field.name);
                                  }
                                  
                                  field.putExtraOption((key!=null?input.toString(key.start,key.stop):null), Double.valueOf((NUMDOUBLE111!=null?NUMDOUBLE111.getText():null)));
                              
                    }

                    }
                    break;
                case 6 :
                    // io/protostuff/parser/ProtoParser.g:363:9: HEX
                    {
                    HEX112=(Token)match(input,HEX,FOLLOW_HEX_in_field_options_keyval1959); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    HEX112_tree = (Object)adaptor.create(HEX112);
                    adaptor.addChild(root_0, HEX112_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  if(checkDefault && "default".equals((key!=null?input.toString(key.start,key.stop):null))) {
                                      if(field.defaultValue!=null || field.modifier == Field.Modifier.REPEATED)
                                          throw new IllegalStateException("a field can only have a single default value");
                                      
                                      if(field instanceof Field.Number) {
                                          if(field instanceof Field.Int32)
                                              field.defaultValue = new Integer(TextFormat.parseInt32((HEX112!=null?HEX112.getText():null)));
                                          else if(field instanceof Field.UInt32)
                                              field.defaultValue = new Integer(TextFormat.parseUInt32((HEX112!=null?HEX112.getText():null)));
                                          else if(field instanceof Field.Int64)
                                              field.defaultValue = new Long(TextFormat.parseInt64((HEX112!=null?HEX112.getText():null)));
                                          else if(field instanceof Field.UInt64)
                                              field.defaultValue = new Long(TextFormat.parseUInt64((HEX112!=null?HEX112.getText():null)));
                                          else if(field instanceof Field.Float)
                                              field.defaultValue = new Float(Long.decode((HEX112!=null?HEX112.getText():null)).floatValue());
                                          else if(field instanceof Field.Double) 
                                              field.defaultValue = new Double(Long.decode((HEX112!=null?HEX112.getText():null)).doubleValue());
                                      }
                                      else if(field instanceof Field.Bytes) {
                                          field.defaultValue = getBytesFromHexString((HEX112!=null?HEX112.getText():null));
                                      }
                                      else
                                          throw new IllegalStateException("Invalid numeric default value for the field: " + field.getClass().getSimpleName() + " " + field.name);
                                      
                                  }
                                  
                                  field.putExtraOption((key!=null?input.toString(key.start,key.stop):null), (HEX112!=null?HEX112.getText():null));
                              
                    }

                    }
                    break;
                case 7 :
                    // io/protostuff/parser/ProtoParser.g:392:9: OCTAL
                    {
                    OCTAL113=(Token)match(input,OCTAL,FOLLOW_OCTAL_in_field_options_keyval1971); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    OCTAL113_tree = (Object)adaptor.create(OCTAL113);
                    adaptor.addChild(root_0, OCTAL113_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  if(checkDefault && "default".equals((key!=null?input.toString(key.start,key.stop):null))) {
                                      if(field.defaultValue!=null || field.modifier == Field.Modifier.REPEATED)
                                          throw new IllegalStateException("a field can only have a single default value");
                                      
                                      if(field instanceof Field.Number) {
                                          if(field instanceof Field.Int32)
                                              field.defaultValue = new Integer(TextFormat.parseInt32((OCTAL113!=null?OCTAL113.getText():null)));
                                          else if(field instanceof Field.UInt32)
                                              field.defaultValue = new Integer(TextFormat.parseUInt32((OCTAL113!=null?OCTAL113.getText():null)));
                                          else if(field instanceof Field.Int64)
                                              field.defaultValue = new Long(TextFormat.parseInt64((OCTAL113!=null?OCTAL113.getText():null)));
                                          else if(field instanceof Field.UInt64)
                                              field.defaultValue = new Long(TextFormat.parseUInt64((OCTAL113!=null?OCTAL113.getText():null)));
                                          else if(field instanceof Field.Float)
                                              field.defaultValue = new Float(Long.decode((OCTAL113!=null?OCTAL113.getText():null)).floatValue());
                                          else if(field instanceof Field.Double) 
                                              field.defaultValue = new Double(Long.decode((OCTAL113!=null?OCTAL113.getText():null)).doubleValue());
                                      }
                                      else
                                          throw new IllegalStateException("Invalid numeric default value for the field: " + field.getClass().getSimpleName() + " " + field.name);
                                  }
                                  
                                  field.putExtraOption((key!=null?input.toString(key.start,key.stop):null), (OCTAL113!=null?OCTAL113.getText():null));
                              
                    }

                    }
                    break;
                case 8 :
                    // io/protostuff/parser/ProtoParser.g:417:9: TRUE
                    {
                    TRUE114=(Token)match(input,TRUE,FOLLOW_TRUE_in_field_options_keyval1983); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    TRUE114_tree = (Object)adaptor.create(TRUE114);
                    adaptor.addChild(root_0, TRUE114_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  if(checkDefault && "default".equals((key!=null?input.toString(key.start,key.stop):null))) {
                                      if(field.defaultValue!=null || field.modifier == Field.Modifier.REPEATED)
                                          throw new IllegalStateException("a field can only have a single default value");
                                      
                                      if(field instanceof Field.Bool)
                                          field.defaultValue = Boolean.TRUE;
                                      else
                                          throw new IllegalStateException("invalid boolean default value for the non-boolean field: " + field.getClass().getSimpleName() + " " + field.name);
                                  }
                                  
                                  field.putExtraOption((key!=null?input.toString(key.start,key.stop):null), Boolean.TRUE);
                              
                    }

                    }
                    break;
                case 9 :
                    // io/protostuff/parser/ProtoParser.g:430:9: FALSE
                    {
                    FALSE115=(Token)match(input,FALSE,FOLLOW_FALSE_in_field_options_keyval1999); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FALSE115_tree = (Object)adaptor.create(FALSE115);
                    adaptor.addChild(root_0, FALSE115_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  if(checkDefault && "default".equals((key!=null?input.toString(key.start,key.stop):null))) {
                                      if(field.defaultValue!=null || field.modifier == Field.Modifier.REPEATED)
                                          throw new IllegalStateException("a field can only have a single default value");
                                      
                                      if(field instanceof Field.Bool)
                                          field.defaultValue = Boolean.FALSE;
                                      else
                                          throw new IllegalStateException("invalid boolean default value for the non-boolean field: " + field.getClass().getSimpleName() + " " + field.name);
                                  }
                                  
                                  field.putExtraOption((key!=null?input.toString(key.start,key.stop):null), Boolean.FALSE);
                              
                    }

                    }
                    break;
                case 10 :
                    // io/protostuff/parser/ProtoParser.g:443:9: val= ID
                    {
                    val=(Token)match(input,ID,FOLLOW_ID_in_field_options_keyval2013); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    val_tree = (Object)adaptor.create(val);
                    adaptor.addChild(root_0, val_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  boolean refOption = false;
                                  if(checkDefault && "default".equals((key!=null?input.toString(key.start,key.stop):null))) {
                                      if(field.defaultValue!=null || field.modifier == Field.Modifier.REPEATED)
                                          throw new IllegalStateException("a field can only have a single default value");
                                      
                                      String refName = (val!=null?val.getText():null);
                                      if(field instanceof Field.Reference)
                                          field.defaultValue = refName;
                                      else if(field instanceof Field.Float) {
                                          if("inf".equals(refName)) {
                                              field.defaultValue = Float.POSITIVE_INFINITY;
                                              field.defaultValueConstant = "Float.POSITIVE_INFINITY";
                                          }
                                          else if("nan".equals(refName)) {
                                              field.defaultValue = Float.NaN;
                                              field.defaultValueConstant = "Float.NaN";
                                          }
                                          else
                                              throw new IllegalStateException("Invalid float default value for the field: " + field.getClass().getSimpleName() + " " + field.name);
                                      }
                                      else if(field instanceof Field.Double) {
                                          if("inf".equals(refName)) {
                                              field.defaultValue = Double.POSITIVE_INFINITY;
                                              field.defaultValueConstant = "Double.POSITIVE_INFINITY";
                                          }
                                          else if("nan".equals(refName)) {
                                              field.defaultValue = Double.NaN;
                                              field.defaultValueConstant = "Double.NaN";
                                          }
                                          else
                                              throw new IllegalStateException("Invalid double default value for the field: " + field.getClass().getSimpleName() + " " + field.name);
                                      }   
                                      else {
                                          refOption = true;
                                          //throw new IllegalStateException("invalid field value '" + refName + "' for the field: " + field.getClass().getSimpleName() + " " + field.name);
                                      }
                                  }
                                  else {
                                      refOption = true;
                                  }
                                  
                                  if(refOption)
                                      field.putStandardOption((key!=null?input.toString(key.start,key.stop):null), (val!=null?val.getText():null));
                                  else
                                      field.putExtraOption((key!=null?input.toString(key.start,key.stop):null), (val!=null?val.getText():null));
                              
                    }

                    }
                    break;
                case 11 :
                    // io/protostuff/parser/ProtoParser.g:490:9: FULL_ID
                    {
                    FULL_ID116=(Token)match(input,FULL_ID,FOLLOW_FULL_ID_in_field_options_keyval2025); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FULL_ID116_tree = (Object)adaptor.create(FULL_ID116);
                    adaptor.addChild(root_0, FULL_ID116_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  field.putStandardOption((key!=null?input.toString(key.start,key.stop):null), (FULL_ID116!=null?FULL_ID116.getText():null));
                              
                    }

                    }
                    break;
                case 12 :
                    // io/protostuff/parser/ProtoParser.g:493:9: EXP
                    {
                    EXP117=(Token)match(input,EXP,FOLLOW_EXP_in_field_options_keyval2037); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    EXP117_tree = (Object)adaptor.create(EXP117);
                    adaptor.addChild(root_0, EXP117_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  if(checkDefault && "default".equals((key!=null?input.toString(key.start,key.stop):null))) {
                                      if(field.defaultValue!=null || field.modifier == Field.Modifier.REPEATED)
                                          throw new IllegalStateException("a field can only have a single default value");
                                      
                                      if(field instanceof Field.Float)
                                          field.defaultValue = Float.valueOf((EXP117!=null?EXP117.getText():null));
                                      else if(field instanceof Field.Double) 
                                          field.defaultValue = Double.valueOf((EXP117!=null?EXP117.getText():null));
                                      else
                                          throw new IllegalStateException("Invalid float default value for the field: " + field.getClass().getSimpleName() + " " + field.name);
                                  }
                                  
                                  field.putExtraOption((key!=null?input.toString(key.start,key.stop):null), (EXP117!=null?EXP117.getText():null));
                              
                    }

                    }
                    break;
                case 13 :
                    // io/protostuff/parser/ProtoParser.g:508:9: signed_constant[proto, message, field, $key.text, checkDefault]
                    {
                    pushFollow(FOLLOW_signed_constant_in_field_options_keyval2049);
                    signed_constant118=signed_constant(proto, message, field, (key!=null?input.toString(key.start,key.stop):null), checkDefault);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, signed_constant118.getTree());
                    if ( state.backtracking==0 ) {

                                  field.putExtraOption((key!=null?input.toString(key.start,key.stop):null), (signed_constant118!=null?input.toString(signed_constant118.start,signed_constant118.stop):null));
                              
                    }

                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "field_options_keyval"

    public static class signed_constant_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "signed_constant"
    // io/protostuff/parser/ProtoParser.g:514:1: signed_constant[Proto proto, HasFields message, Field field, String key, boolean checkDefault] : MINUS ID ;
    public final ProtoParser.signed_constant_return signed_constant(Proto proto, HasFields message, Field field, String key, boolean checkDefault) throws RecognitionException {
        ProtoParser.signed_constant_return retval = new ProtoParser.signed_constant_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token MINUS119=null;
        Token ID120=null;

        Object MINUS119_tree=null;
        Object ID120_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:515:5: ( MINUS ID )
            // io/protostuff/parser/ProtoParser.g:515:9: MINUS ID
            {
            root_0 = (Object)adaptor.nil();

            MINUS119=(Token)match(input,MINUS,FOLLOW_MINUS_in_signed_constant2087); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            MINUS119_tree = (Object)adaptor.create(MINUS119);
            adaptor.addChild(root_0, MINUS119_tree);
            }
            ID120=(Token)match(input,ID,FOLLOW_ID_in_signed_constant2089); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID120_tree = (Object)adaptor.create(ID120);
            adaptor.addChild(root_0, ID120_tree);
            }
            if ( state.backtracking==0 ) {

                          if(checkDefault && "default".equals(key)) {
                              if(field.defaultValue!=null || field.modifier == Field.Modifier.REPEATED)
                                  throw new IllegalStateException("a field can only have a single default value");
                              
                              String refName = (ID120!=null?ID120.getText():null);
                              if(field instanceof Field.Float) {
                                  if("inf".equals(refName)) {
                                      field.defaultValue = Float.NEGATIVE_INFINITY;
                                      field.defaultValueConstant = "Float.NEGATIVE_INFINITY";
                                  }
                                  else
                                      throw new IllegalStateException("Invalid float default value for the field: " + field.getClass().getSimpleName() + " " + field.name);
                              }
                              else if(field instanceof Field.Double) {
                                  if("inf".equals(refName)) {
                                      field.defaultValue = Double.NEGATIVE_INFINITY;
                                      field.defaultValueConstant = "Double.NEGATIVE_INFINITY";
                                  }
                                  else
                                      throw new IllegalStateException("Invalid double default value for the field: " + field.getClass().getSimpleName() + " " + field.name);
                              }   
                              else
                                  throw new IllegalStateException("invalid field value '" + refName + "' for the field: " + field.getClass().getSimpleName() + " " + field.name);
                          }
                      
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "signed_constant"

    public static class enum_block_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "enum_block"
    // io/protostuff/parser/ProtoParser.g:543:1: enum_block[Proto proto, Message message] : ENUM ID LEFTCURLY ( enum_body[proto, message, enumGroup] )* RIGHTCURLY ( ( SEMICOLON )? ) ;
    public final ProtoParser.enum_block_return enum_block(Proto proto, Message message) throws RecognitionException {
        ProtoParser.enum_block_return retval = new ProtoParser.enum_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ENUM121=null;
        Token ID122=null;
        Token LEFTCURLY123=null;
        Token RIGHTCURLY125=null;
        Token SEMICOLON126=null;
        ProtoParser.enum_body_return enum_body124 = null;


        Object ENUM121_tree=null;
        Object ID122_tree=null;
        Object LEFTCURLY123_tree=null;
        Object RIGHTCURLY125_tree=null;
        Object SEMICOLON126_tree=null;


            EnumGroup enumGroup = null;

        try {
            // io/protostuff/parser/ProtoParser.g:547:5: ( ENUM ID LEFTCURLY ( enum_body[proto, message, enumGroup] )* RIGHTCURLY ( ( SEMICOLON )? ) )
            // io/protostuff/parser/ProtoParser.g:547:9: ENUM ID LEFTCURLY ( enum_body[proto, message, enumGroup] )* RIGHTCURLY ( ( SEMICOLON )? )
            {
            root_0 = (Object)adaptor.nil();

            ENUM121=(Token)match(input,ENUM,FOLLOW_ENUM_in_enum_block2121); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ENUM121_tree = (Object)adaptor.create(ENUM121);
            adaptor.addChild(root_0, ENUM121_tree);
            }
            ID122=(Token)match(input,ID,FOLLOW_ID_in_enum_block2123); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID122_tree = (Object)adaptor.create(ID122);
            adaptor.addChild(root_0, ID122_tree);
            }
            if ( state.backtracking==0 ) {
               
                          enumGroup = new EnumGroup((ID122!=null?ID122.getText():null), message, proto);
                          proto.addAnnotationsTo(enumGroup);
                      
            }
            LEFTCURLY123=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_enum_block2136); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEFTCURLY123_tree = (Object)adaptor.create(LEFTCURLY123);
            adaptor.addChild(root_0, LEFTCURLY123_tree);
            }
            // io/protostuff/parser/ProtoParser.g:551:19: ( enum_body[proto, message, enumGroup] )*
            loop22:
            do {
                int alt22=2;
                switch ( input.LA(1) ) {
                case AT:
                case OPTION:
                case ID:
                case DOC:
                    {
                    alt22=1;
                    }
                    break;

                }

                switch (alt22) {
            	case 1 :
            	    // io/protostuff/parser/ProtoParser.g:551:20: enum_body[proto, message, enumGroup]
            	    {
            	    pushFollow(FOLLOW_enum_body_in_enum_block2139);
            	    enum_body124=enum_body(proto, message, enumGroup);

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_body124.getTree());

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);

            RIGHTCURLY125=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_enum_block2144); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            RIGHTCURLY125_tree = (Object)adaptor.create(RIGHTCURLY125);
            adaptor.addChild(root_0, RIGHTCURLY125_tree);
            }
            if ( state.backtracking==0 ) {

                          if(!proto.annotations.isEmpty())
                              throw new IllegalStateException("Misplaced annotations: " + proto.annotations);
                          if(!proto.docs.isEmpty())
                              throw new IllegalStateException("Misplaced docs: " + proto.docs);
                      
            }
            // io/protostuff/parser/ProtoParser.g:556:11: ( ( SEMICOLON )? )
            // io/protostuff/parser/ProtoParser.g:556:12: ( SEMICOLON )?
            {
            // io/protostuff/parser/ProtoParser.g:556:12: ( SEMICOLON )?
            int alt23=2;
            switch ( input.LA(1) ) {
                case SEMICOLON:
                    {
                    alt23=1;
                    }
                    break;
            }

            switch (alt23) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:556:12: SEMICOLON
                    {
                    SEMICOLON126=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_enum_block2149); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SEMICOLON126_tree = (Object)adaptor.create(SEMICOLON126);
                    adaptor.addChild(root_0, SEMICOLON126_tree);
                    }

                    }
                    break;

            }


            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "enum_block"

    public static class enum_body_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "enum_body"
    // io/protostuff/parser/ProtoParser.g:559:1: enum_body[Proto proto, Message message, EnumGroup enumGroup] : ( enum_field[proto, message, enumGroup] | annotation_entry[proto] | doc_entry[proto] | option_entry[proto, enumGroup] );
    public final ProtoParser.enum_body_return enum_body(Proto proto, Message message, EnumGroup enumGroup) throws RecognitionException {
        ProtoParser.enum_body_return retval = new ProtoParser.enum_body_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ProtoParser.enum_field_return enum_field127 = null;

        ProtoParser.annotation_entry_return annotation_entry128 = null;

        ProtoParser.doc_entry_return doc_entry129 = null;

        ProtoParser.option_entry_return option_entry130 = null;



        try {
            // io/protostuff/parser/ProtoParser.g:560:5: ( enum_field[proto, message, enumGroup] | annotation_entry[proto] | doc_entry[proto] | option_entry[proto, enumGroup] )
            int alt24=4;
            switch ( input.LA(1) ) {
            case ID:
                {
                alt24=1;
                }
                break;
            case AT:
                {
                alt24=2;
                }
                break;
            case DOC:
                {
                alt24=3;
                }
                break;
            case OPTION:
                {
                alt24=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;
            }

            switch (alt24) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:560:9: enum_field[proto, message, enumGroup]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_enum_field_in_enum_body2177);
                    enum_field127=enum_field(proto, message, enumGroup);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_field127.getTree());

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:561:9: annotation_entry[proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_annotation_entry_in_enum_body2188);
                    annotation_entry128=annotation_entry(proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotation_entry128.getTree());

                    }
                    break;
                case 3 :
                    // io/protostuff/parser/ProtoParser.g:562:9: doc_entry[proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_doc_entry_in_enum_body2199);
                    doc_entry129=doc_entry(proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, doc_entry129.getTree());

                    }
                    break;
                case 4 :
                    // io/protostuff/parser/ProtoParser.g:563:9: option_entry[proto, enumGroup]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_option_entry_in_enum_body2210);
                    option_entry130=option_entry(proto, enumGroup);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, option_entry130.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "enum_body"

    public static class enum_field_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "enum_field"
    // io/protostuff/parser/ProtoParser.g:566:1: enum_field[Proto proto, Message message, EnumGroup enumGroup] : ID ASSIGN NUMINT ( enum_options[proto, enumGroup, v] )? SEMICOLON ;
    public final ProtoParser.enum_field_return enum_field(Proto proto, Message message, EnumGroup enumGroup) throws RecognitionException {
        ProtoParser.enum_field_return retval = new ProtoParser.enum_field_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID131=null;
        Token ASSIGN132=null;
        Token NUMINT133=null;
        Token SEMICOLON135=null;
        ProtoParser.enum_options_return enum_options134 = null;


        Object ID131_tree=null;
        Object ASSIGN132_tree=null;
        Object NUMINT133_tree=null;
        Object SEMICOLON135_tree=null;


            EnumGroup.Value v = null;

        try {
            // io/protostuff/parser/ProtoParser.g:570:5: ( ID ASSIGN NUMINT ( enum_options[proto, enumGroup, v] )? SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:570:9: ID ASSIGN NUMINT ( enum_options[proto, enumGroup, v] )? SEMICOLON
            {
            root_0 = (Object)adaptor.nil();

            ID131=(Token)match(input,ID,FOLLOW_ID_in_enum_field2237); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID131_tree = (Object)adaptor.create(ID131);
            adaptor.addChild(root_0, ID131_tree);
            }
            ASSIGN132=(Token)match(input,ASSIGN,FOLLOW_ASSIGN_in_enum_field2239); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ASSIGN132_tree = (Object)adaptor.create(ASSIGN132);
            adaptor.addChild(root_0, ASSIGN132_tree);
            }
            NUMINT133=(Token)match(input,NUMINT,FOLLOW_NUMINT_in_enum_field2241); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            NUMINT133_tree = (Object)adaptor.create(NUMINT133);
            adaptor.addChild(root_0, NUMINT133_tree);
            }
            if ( state.backtracking==0 ) {

                          v = new EnumGroup.Value((ID131!=null?ID131.getText():null), Integer.parseInt((NUMINT133!=null?NUMINT133.getText():null)), enumGroup);
                          proto.addAnnotationsTo(v);
                      
            }
            // io/protostuff/parser/ProtoParser.g:573:11: ( enum_options[proto, enumGroup, v] )?
            int alt25=2;
            switch ( input.LA(1) ) {
                case LEFTSQUARE:
                    {
                    alt25=1;
                    }
                    break;
            }

            switch (alt25) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:573:12: enum_options[proto, enumGroup, v]
                    {
                    pushFollow(FOLLOW_enum_options_in_enum_field2246);
                    enum_options134=enum_options(proto, enumGroup, v);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, enum_options134.getTree());

                    }
                    break;

            }

            SEMICOLON135=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_enum_field2251); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "enum_field"

    public static class enum_options_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "enum_options"
    // io/protostuff/parser/ProtoParser.g:576:1: enum_options[Proto proto, EnumGroup enumGroup, EnumGroup.Value v] : LEFTSQUARE field_options_keyval[proto, null, v.field, false] ( COMMA field_options_keyval[proto, null, v.field, false] )* RIGHTSQUARE ;
    public final ProtoParser.enum_options_return enum_options(Proto proto, EnumGroup enumGroup, EnumGroup.Value v) throws RecognitionException {
        ProtoParser.enum_options_return retval = new ProtoParser.enum_options_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LEFTSQUARE136=null;
        Token COMMA138=null;
        Token RIGHTSQUARE140=null;
        ProtoParser.field_options_keyval_return field_options_keyval137 = null;

        ProtoParser.field_options_keyval_return field_options_keyval139 = null;


        Object LEFTSQUARE136_tree=null;
        Object COMMA138_tree=null;
        Object RIGHTSQUARE140_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:577:5: ( LEFTSQUARE field_options_keyval[proto, null, v.field, false] ( COMMA field_options_keyval[proto, null, v.field, false] )* RIGHTSQUARE )
            // io/protostuff/parser/ProtoParser.g:577:9: LEFTSQUARE field_options_keyval[proto, null, v.field, false] ( COMMA field_options_keyval[proto, null, v.field, false] )* RIGHTSQUARE
            {
            root_0 = (Object)adaptor.nil();

            LEFTSQUARE136=(Token)match(input,LEFTSQUARE,FOLLOW_LEFTSQUARE_in_enum_options2274); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEFTSQUARE136_tree = (Object)adaptor.create(LEFTSQUARE136);
            adaptor.addChild(root_0, LEFTSQUARE136_tree);
            }
            pushFollow(FOLLOW_field_options_keyval_in_enum_options2276);
            field_options_keyval137=field_options_keyval(proto, null, v.field, false);

            state._fsp--;
            if (state.failed) return retval;
            if ( state.backtracking==0 ) adaptor.addChild(root_0, field_options_keyval137.getTree());
            // io/protostuff/parser/ProtoParser.g:578:9: ( COMMA field_options_keyval[proto, null, v.field, false] )*
            loop26:
            do {
                int alt26=2;
                switch ( input.LA(1) ) {
                case COMMA:
                    {
                    alt26=1;
                    }
                    break;

                }

                switch (alt26) {
            	case 1 :
            	    // io/protostuff/parser/ProtoParser.g:578:10: COMMA field_options_keyval[proto, null, v.field, false]
            	    {
            	    COMMA138=(Token)match(input,COMMA,FOLLOW_COMMA_in_enum_options2289); if (state.failed) return retval;
            	    if ( state.backtracking==0 ) {
            	    COMMA138_tree = (Object)adaptor.create(COMMA138);
            	    adaptor.addChild(root_0, COMMA138_tree);
            	    }
            	    pushFollow(FOLLOW_field_options_keyval_in_enum_options2291);
            	    field_options_keyval139=field_options_keyval(proto, null, v.field, false);

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, field_options_keyval139.getTree());

            	    }
            	    break;

            	default :
            	    break loop26;
                }
            } while (true);

            RIGHTSQUARE140=(Token)match(input,RIGHTSQUARE,FOLLOW_RIGHTSQUARE_in_enum_options2296); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            RIGHTSQUARE140_tree = (Object)adaptor.create(RIGHTSQUARE140);
            adaptor.addChild(root_0, RIGHTSQUARE140_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "enum_options"

    public static class service_block_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "service_block"
    // io/protostuff/parser/ProtoParser.g:581:1: service_block[Proto proto, Message message] : SERVICE ID LEFTCURLY ( service_body[proto, service] )+ RIGHTCURLY ( ( SEMICOLON )? ) ;
    public final ProtoParser.service_block_return service_block(Proto proto, Message message) throws RecognitionException {
        ProtoParser.service_block_return retval = new ProtoParser.service_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SERVICE141=null;
        Token ID142=null;
        Token LEFTCURLY143=null;
        Token RIGHTCURLY145=null;
        Token SEMICOLON146=null;
        ProtoParser.service_body_return service_body144 = null;


        Object SERVICE141_tree=null;
        Object ID142_tree=null;
        Object LEFTCURLY143_tree=null;
        Object RIGHTCURLY145_tree=null;
        Object SEMICOLON146_tree=null;


            Service service = null;

        try {
            // io/protostuff/parser/ProtoParser.g:585:5: ( SERVICE ID LEFTCURLY ( service_body[proto, service] )+ RIGHTCURLY ( ( SEMICOLON )? ) )
            // io/protostuff/parser/ProtoParser.g:585:9: SERVICE ID LEFTCURLY ( service_body[proto, service] )+ RIGHTCURLY ( ( SEMICOLON )? )
            {
            root_0 = (Object)adaptor.nil();

            SERVICE141=(Token)match(input,SERVICE,FOLLOW_SERVICE_in_service_block2326); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            SERVICE141_tree = (Object)adaptor.create(SERVICE141);
            adaptor.addChild(root_0, SERVICE141_tree);
            }
            ID142=(Token)match(input,ID,FOLLOW_ID_in_service_block2328); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            ID142_tree = (Object)adaptor.create(ID142);
            adaptor.addChild(root_0, ID142_tree);
            }
            if ( state.backtracking==0 ) {
               
                          service = new Service((ID142!=null?ID142.getText():null), message, proto); 
                          proto.addAnnotationsTo(service);
                      
            }
            LEFTCURLY143=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_service_block2332); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEFTCURLY143_tree = (Object)adaptor.create(LEFTCURLY143);
            adaptor.addChild(root_0, LEFTCURLY143_tree);
            }
            // io/protostuff/parser/ProtoParser.g:589:9: ( service_body[proto, service] )+
            int cnt27=0;
            loop27:
            do {
                int alt27=2;
                switch ( input.LA(1) ) {
                case AT:
                case OPTION:
                case RPC:
                case DOC:
                    {
                    alt27=1;
                    }
                    break;

                }

                switch (alt27) {
            	case 1 :
            	    // io/protostuff/parser/ProtoParser.g:589:10: service_body[proto, service]
            	    {
            	    pushFollow(FOLLOW_service_body_in_service_block2343);
            	    service_body144=service_body(proto, service);

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, service_body144.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt27 >= 1 ) break loop27;
            	    if (state.backtracking>0) {state.failed=true; return retval;}
                        EarlyExitException eee =
                            new EarlyExitException(27, input);
                        throw eee;
                }
                cnt27++;
            } while (true);

            RIGHTCURLY145=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_service_block2348); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            RIGHTCURLY145_tree = (Object)adaptor.create(RIGHTCURLY145);
            adaptor.addChild(root_0, RIGHTCURLY145_tree);
            }
            // io/protostuff/parser/ProtoParser.g:589:52: ( ( SEMICOLON )? )
            // io/protostuff/parser/ProtoParser.g:589:53: ( SEMICOLON )?
            {
            // io/protostuff/parser/ProtoParser.g:589:53: ( SEMICOLON )?
            int alt28=2;
            switch ( input.LA(1) ) {
                case SEMICOLON:
                    {
                    alt28=1;
                    }
                    break;
            }

            switch (alt28) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:589:53: SEMICOLON
                    {
                    SEMICOLON146=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_service_block2351); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SEMICOLON146_tree = (Object)adaptor.create(SEMICOLON146);
                    adaptor.addChild(root_0, SEMICOLON146_tree);
                    }

                    }
                    break;

            }


            }

            if ( state.backtracking==0 ) {

                          if(service.rpcMethods.isEmpty())
                              throw new IllegalStateException("Empty Service block: " + service.getName());
                              
                          if(!proto.annotations.isEmpty())
                              throw new IllegalStateException("Misplaced annotations: " + proto.annotations);
                          if(!proto.docs.isEmpty())
                              throw new IllegalStateException("Misplaced docs: " + proto.docs);
                      
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "service_block"

    public static class service_body_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "service_body"
    // io/protostuff/parser/ProtoParser.g:600:1: service_body[Proto proto, Service service] : ( rpc_block[proto, service] | annotation_entry[proto] | doc_entry[proto] | option_entry[proto, service] );
    public final ProtoParser.service_body_return service_body(Proto proto, Service service) throws RecognitionException {
        ProtoParser.service_body_return retval = new ProtoParser.service_body_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ProtoParser.rpc_block_return rpc_block147 = null;

        ProtoParser.annotation_entry_return annotation_entry148 = null;

        ProtoParser.doc_entry_return doc_entry149 = null;

        ProtoParser.option_entry_return option_entry150 = null;



        try {
            // io/protostuff/parser/ProtoParser.g:601:5: ( rpc_block[proto, service] | annotation_entry[proto] | doc_entry[proto] | option_entry[proto, service] )
            int alt29=4;
            switch ( input.LA(1) ) {
            case RPC:
                {
                alt29=1;
                }
                break;
            case AT:
                {
                alt29=2;
                }
                break;
            case DOC:
                {
                alt29=3;
                }
                break;
            case OPTION:
                {
                alt29=4;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;
            }

            switch (alt29) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:601:9: rpc_block[proto, service]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_rpc_block_in_service_body2381);
                    rpc_block147=rpc_block(proto, service);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, rpc_block147.getTree());

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:602:9: annotation_entry[proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_annotation_entry_in_service_body2392);
                    annotation_entry148=annotation_entry(proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotation_entry148.getTree());

                    }
                    break;
                case 3 :
                    // io/protostuff/parser/ProtoParser.g:603:9: doc_entry[proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_doc_entry_in_service_body2403);
                    doc_entry149=doc_entry(proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, doc_entry149.getTree());

                    }
                    break;
                case 4 :
                    // io/protostuff/parser/ProtoParser.g:604:9: option_entry[proto, service]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_option_entry_in_service_body2414);
                    option_entry150=option_entry(proto, service);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, option_entry150.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "service_body"

    public static class rpc_block_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "rpc_block"
    // io/protostuff/parser/ProtoParser.g:607:1: rpc_block[Proto proto, Service service] : RPC n= ID LEFTPAREN (ap= FULL_ID | a= ( VOID | ID ) ) RIGHTPAREN RETURNS LEFTPAREN (rp= FULL_ID | r= ( VOID | ID ) ) RIGHTPAREN ( rpc_body_block[proto, rm] )? SEMICOLON ;
    public final ProtoParser.rpc_block_return rpc_block(Proto proto, Service service) throws RecognitionException {
        ProtoParser.rpc_block_return retval = new ProtoParser.rpc_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token n=null;
        Token ap=null;
        Token a=null;
        Token rp=null;
        Token r=null;
        Token RPC151=null;
        Token LEFTPAREN152=null;
        Token RIGHTPAREN153=null;
        Token RETURNS154=null;
        Token LEFTPAREN155=null;
        Token RIGHTPAREN156=null;
        Token SEMICOLON158=null;
        ProtoParser.rpc_body_block_return rpc_body_block157 = null;


        Object n_tree=null;
        Object ap_tree=null;
        Object a_tree=null;
        Object rp_tree=null;
        Object r_tree=null;
        Object RPC151_tree=null;
        Object LEFTPAREN152_tree=null;
        Object RIGHTPAREN153_tree=null;
        Object RETURNS154_tree=null;
        Object LEFTPAREN155_tree=null;
        Object RIGHTPAREN156_tree=null;
        Object SEMICOLON158_tree=null;


            String argName = null, argPackage = null, retName = null, retPackage = null;
            Service.RpcMethod rm = null;

        try {
            // io/protostuff/parser/ProtoParser.g:612:5: ( RPC n= ID LEFTPAREN (ap= FULL_ID | a= ( VOID | ID ) ) RIGHTPAREN RETURNS LEFTPAREN (rp= FULL_ID | r= ( VOID | ID ) ) RIGHTPAREN ( rpc_body_block[proto, rm] )? SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:612:9: RPC n= ID LEFTPAREN (ap= FULL_ID | a= ( VOID | ID ) ) RIGHTPAREN RETURNS LEFTPAREN (rp= FULL_ID | r= ( VOID | ID ) ) RIGHTPAREN ( rpc_body_block[proto, rm] )? SEMICOLON
            {
            root_0 = (Object)adaptor.nil();

            RPC151=(Token)match(input,RPC,FOLLOW_RPC_in_rpc_block2445); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            RPC151_tree = (Object)adaptor.create(RPC151);
            adaptor.addChild(root_0, RPC151_tree);
            }
            n=(Token)match(input,ID,FOLLOW_ID_in_rpc_block2449); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            n_tree = (Object)adaptor.create(n);
            adaptor.addChild(root_0, n_tree);
            }
            LEFTPAREN152=(Token)match(input,LEFTPAREN,FOLLOW_LEFTPAREN_in_rpc_block2451); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEFTPAREN152_tree = (Object)adaptor.create(LEFTPAREN152);
            adaptor.addChild(root_0, LEFTPAREN152_tree);
            }
            // io/protostuff/parser/ProtoParser.g:612:28: (ap= FULL_ID | a= ( VOID | ID ) )
            int alt30=2;
            switch ( input.LA(1) ) {
            case FULL_ID:
                {
                alt30=1;
                }
                break;
            case VOID:
            case ID:
                {
                alt30=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 30, 0, input);

                throw nvae;
            }

            switch (alt30) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:612:29: ap= FULL_ID
                    {
                    ap=(Token)match(input,FULL_ID,FOLLOW_FULL_ID_in_rpc_block2456); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ap_tree = (Object)adaptor.create(ap);
                    adaptor.addChild(root_0, ap_tree);
                    }
                    if ( state.backtracking==0 ) {
                        
                                  String argFull = (ap!=null?ap.getText():null);
                                  int lastDot = argFull.lastIndexOf('.');
                                  argPackage = argFull.substring(0, lastDot); 
                                  argName = argFull.substring(lastDot+1);
                              
                    }

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:617:13: a= ( VOID | ID )
                    {
                    a=(Token)input.LT(1);
                    if ( (input.LA(1)>=VOID && input.LA(1)<=ID) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(a));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    if ( state.backtracking==0 ) {
                       argName = (a!=null?a.getText():null); 
                    }

                    }
                    break;

            }

            RIGHTPAREN153=(Token)match(input,RIGHTPAREN,FOLLOW_RIGHTPAREN_in_rpc_block2473); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            RIGHTPAREN153_tree = (Object)adaptor.create(RIGHTPAREN153);
            adaptor.addChild(root_0, RIGHTPAREN153_tree);
            }
            RETURNS154=(Token)match(input,RETURNS,FOLLOW_RETURNS_in_rpc_block2484); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            RETURNS154_tree = (Object)adaptor.create(RETURNS154);
            adaptor.addChild(root_0, RETURNS154_tree);
            }
            LEFTPAREN155=(Token)match(input,LEFTPAREN,FOLLOW_LEFTPAREN_in_rpc_block2486); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEFTPAREN155_tree = (Object)adaptor.create(LEFTPAREN155);
            adaptor.addChild(root_0, LEFTPAREN155_tree);
            }
            // io/protostuff/parser/ProtoParser.g:618:27: (rp= FULL_ID | r= ( VOID | ID ) )
            int alt31=2;
            switch ( input.LA(1) ) {
            case FULL_ID:
                {
                alt31=1;
                }
                break;
            case VOID:
            case ID:
                {
                alt31=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 31, 0, input);

                throw nvae;
            }

            switch (alt31) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:618:28: rp= FULL_ID
                    {
                    rp=(Token)match(input,FULL_ID,FOLLOW_FULL_ID_in_rpc_block2491); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    rp_tree = (Object)adaptor.create(rp);
                    adaptor.addChild(root_0, rp_tree);
                    }
                    if ( state.backtracking==0 ) {
                        
                                  String retFull = (rp!=null?rp.getText():null);
                                  int lastDot = retFull.lastIndexOf('.');
                                  retPackage = retFull.substring(0, lastDot); 
                                  retName = retFull.substring(lastDot+1);
                              
                    }

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:623:13: r= ( VOID | ID )
                    {
                    r=(Token)input.LT(1);
                    if ( (input.LA(1)>=VOID && input.LA(1)<=ID) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(r));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    if ( state.backtracking==0 ) {
                       retName = (r!=null?r.getText():null); 
                    }

                    }
                    break;

            }

            RIGHTPAREN156=(Token)match(input,RIGHTPAREN,FOLLOW_RIGHTPAREN_in_rpc_block2508); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            RIGHTPAREN156_tree = (Object)adaptor.create(RIGHTPAREN156);
            adaptor.addChild(root_0, RIGHTPAREN156_tree);
            }
            if ( state.backtracking==0 ) {

                          rm = service.addRpcMethod((n!=null?n.getText():null), argName, argPackage, retName, retPackage);
                          proto.addAnnotationsTo(rm);
                      
            }
            // io/protostuff/parser/ProtoParser.g:626:11: ( rpc_body_block[proto, rm] )?
            int alt32=2;
            switch ( input.LA(1) ) {
                case LEFTCURLY:
                    {
                    alt32=1;
                    }
                    break;
            }

            switch (alt32) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:626:11: rpc_body_block[proto, rm]
                    {
                    pushFollow(FOLLOW_rpc_body_block_in_rpc_block2512);
                    rpc_body_block157=rpc_body_block(proto, rm);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, rpc_body_block157.getTree());

                    }
                    break;

            }

            SEMICOLON158=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_rpc_block2516); if (state.failed) return retval;

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "rpc_block"

    public static class rpc_body_block_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "rpc_body_block"
    // io/protostuff/parser/ProtoParser.g:629:1: rpc_body_block[Proto proto, Service.RpcMethod rm] : LEFTCURLY ( option_entry[proto, rm] )* RIGHTCURLY ;
    public final ProtoParser.rpc_body_block_return rpc_body_block(Proto proto, Service.RpcMethod rm) throws RecognitionException {
        ProtoParser.rpc_body_block_return retval = new ProtoParser.rpc_body_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LEFTCURLY159=null;
        Token RIGHTCURLY161=null;
        ProtoParser.option_entry_return option_entry160 = null;


        Object LEFTCURLY159_tree=null;
        Object RIGHTCURLY161_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:630:5: ( LEFTCURLY ( option_entry[proto, rm] )* RIGHTCURLY )
            // io/protostuff/parser/ProtoParser.g:630:9: LEFTCURLY ( option_entry[proto, rm] )* RIGHTCURLY
            {
            root_0 = (Object)adaptor.nil();

            LEFTCURLY159=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_rpc_body_block2542); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEFTCURLY159_tree = (Object)adaptor.create(LEFTCURLY159);
            adaptor.addChild(root_0, LEFTCURLY159_tree);
            }
            // io/protostuff/parser/ProtoParser.g:630:19: ( option_entry[proto, rm] )*
            loop33:
            do {
                int alt33=2;
                switch ( input.LA(1) ) {
                case OPTION:
                    {
                    alt33=1;
                    }
                    break;

                }

                switch (alt33) {
            	case 1 :
            	    // io/protostuff/parser/ProtoParser.g:630:19: option_entry[proto, rm]
            	    {
            	    pushFollow(FOLLOW_option_entry_in_rpc_body_block2544);
            	    option_entry160=option_entry(proto, rm);

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, option_entry160.getTree());

            	    }
            	    break;

            	default :
            	    break loop33;
                }
            } while (true);

            RIGHTCURLY161=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_rpc_body_block2548); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            RIGHTCURLY161_tree = (Object)adaptor.create(RIGHTCURLY161);
            adaptor.addChild(root_0, RIGHTCURLY161_tree);
            }
            if ( state.backtracking==0 ) {

                          if(!proto.annotations.isEmpty())
                              throw new IllegalStateException("Misplaced annotations: " + proto.annotations);
                          if(!proto.docs.isEmpty())
                              throw new IllegalStateException("Misplaced docs: " + proto.docs);
                      
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "rpc_body_block"

    public static class extend_block_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "extend_block"
    // io/protostuff/parser/ProtoParser.g:638:1: extend_block[Proto proto, Message parent] : EXTEND ( FULL_ID | ID ) LEFTCURLY ( extend_body[proto, extension] )* RIGHTCURLY ( ( SEMICOLON )? ) ;
    public final ProtoParser.extend_block_return extend_block(Proto proto, Message parent) throws RecognitionException {
        ProtoParser.extend_block_return retval = new ProtoParser.extend_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token EXTEND162=null;
        Token FULL_ID163=null;
        Token ID164=null;
        Token LEFTCURLY165=null;
        Token RIGHTCURLY167=null;
        Token SEMICOLON168=null;
        ProtoParser.extend_body_return extend_body166 = null;


        Object EXTEND162_tree=null;
        Object FULL_ID163_tree=null;
        Object ID164_tree=null;
        Object LEFTCURLY165_tree=null;
        Object RIGHTCURLY167_tree=null;
        Object SEMICOLON168_tree=null;


            Extension extension = null;

        try {
            // io/protostuff/parser/ProtoParser.g:642:5: ( EXTEND ( FULL_ID | ID ) LEFTCURLY ( extend_body[proto, extension] )* RIGHTCURLY ( ( SEMICOLON )? ) )
            // io/protostuff/parser/ProtoParser.g:642:9: EXTEND ( FULL_ID | ID ) LEFTCURLY ( extend_body[proto, extension] )* RIGHTCURLY ( ( SEMICOLON )? )
            {
            root_0 = (Object)adaptor.nil();

            EXTEND162=(Token)match(input,EXTEND,FOLLOW_EXTEND_in_extend_block2580); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            EXTEND162_tree = (Object)adaptor.create(EXTEND162);
            adaptor.addChild(root_0, EXTEND162_tree);
            }
            // io/protostuff/parser/ProtoParser.g:642:16: ( FULL_ID | ID )
            int alt34=2;
            switch ( input.LA(1) ) {
            case FULL_ID:
                {
                alt34=1;
                }
                break;
            case ID:
                {
                alt34=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 34, 0, input);

                throw nvae;
            }

            switch (alt34) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:643:9: FULL_ID
                    {
                    FULL_ID163=(Token)match(input,FULL_ID,FOLLOW_FULL_ID_in_extend_block2592); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    FULL_ID163_tree = (Object)adaptor.create(FULL_ID163);
                    adaptor.addChild(root_0, FULL_ID163_tree);
                    }
                    if ( state.backtracking==0 ) {

                                  String fullType = (FULL_ID163!=null?FULL_ID163.getText():null);
                                  int lastDot = fullType.lastIndexOf('.');
                                  String packageName = fullType.substring(0, lastDot); 
                                  String type = fullType.substring(lastDot+1);
                                  extension = new Extension(proto, parent, packageName, type);
                              
                    }

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:649:13: ID
                    {
                    ID164=(Token)match(input,ID,FOLLOW_ID_in_extend_block2598); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    ID164_tree = (Object)adaptor.create(ID164);
                    adaptor.addChild(root_0, ID164_tree);
                    }
                    if ( state.backtracking==0 ) {
                       extension = new Extension(proto, parent, null, (ID164!=null?ID164.getText():null)); 
                    }

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

                          if(parent==null)
                              proto.addExtension(extension);
                          else
                              parent.addNestedExtension(extension);
                              
                          proto.addAnnotationsTo(extension);
                      
            }
            LEFTCURLY165=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_extend_block2614); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEFTCURLY165_tree = (Object)adaptor.create(LEFTCURLY165);
            adaptor.addChild(root_0, LEFTCURLY165_tree);
            }
            // io/protostuff/parser/ProtoParser.g:657:19: ( extend_body[proto, extension] )*
            loop35:
            do {
                int alt35=2;
                switch ( input.LA(1) ) {
                case AT:
                case REQUIRED:
                case OPTIONAL:
                case REPEATED:
                case DOC:
                    {
                    alt35=1;
                    }
                    break;

                }

                switch (alt35) {
            	case 1 :
            	    // io/protostuff/parser/ProtoParser.g:657:20: extend_body[proto, extension]
            	    {
            	    pushFollow(FOLLOW_extend_body_in_extend_block2617);
            	    extend_body166=extend_body(proto, extension);

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, extend_body166.getTree());

            	    }
            	    break;

            	default :
            	    break loop35;
                }
            } while (true);

            RIGHTCURLY167=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_extend_block2622); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            RIGHTCURLY167_tree = (Object)adaptor.create(RIGHTCURLY167);
            adaptor.addChild(root_0, RIGHTCURLY167_tree);
            }
            if ( state.backtracking==0 ) {

                          if(!proto.annotations.isEmpty())
                              throw new IllegalStateException("Misplaced annotations: " + proto.annotations);
                          if(!proto.docs.isEmpty())
                              throw new IllegalStateException("Misplaced docs: " + proto.docs);
                              
                      
            }
            // io/protostuff/parser/ProtoParser.g:663:11: ( ( SEMICOLON )? )
            // io/protostuff/parser/ProtoParser.g:663:12: ( SEMICOLON )?
            {
            // io/protostuff/parser/ProtoParser.g:663:12: ( SEMICOLON )?
            int alt36=2;
            switch ( input.LA(1) ) {
                case SEMICOLON:
                    {
                    alt36=1;
                    }
                    break;
            }

            switch (alt36) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:663:12: SEMICOLON
                    {
                    SEMICOLON168=(Token)match(input,SEMICOLON,FOLLOW_SEMICOLON_in_extend_block2627); if (state.failed) return retval;
                    if ( state.backtracking==0 ) {
                    SEMICOLON168_tree = (Object)adaptor.create(SEMICOLON168);
                    adaptor.addChild(root_0, SEMICOLON168_tree);
                    }

                    }
                    break;

            }


            }


            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "extend_block"

    public static class extend_body_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "extend_body"
    // io/protostuff/parser/ProtoParser.g:666:1: extend_body[Proto proto, Extension extension] : ( message_field[proto, extension] | annotation_entry[proto] | doc_entry[proto] );
    public final ProtoParser.extend_body_return extend_body(Proto proto, Extension extension) throws RecognitionException {
        ProtoParser.extend_body_return retval = new ProtoParser.extend_body_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ProtoParser.message_field_return message_field169 = null;

        ProtoParser.annotation_entry_return annotation_entry170 = null;

        ProtoParser.doc_entry_return doc_entry171 = null;



        try {
            // io/protostuff/parser/ProtoParser.g:667:5: ( message_field[proto, extension] | annotation_entry[proto] | doc_entry[proto] )
            int alt37=3;
            switch ( input.LA(1) ) {
            case REQUIRED:
            case OPTIONAL:
            case REPEATED:
                {
                alt37=1;
                }
                break;
            case AT:
                {
                alt37=2;
                }
                break;
            case DOC:
                {
                alt37=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 37, 0, input);

                throw nvae;
            }

            switch (alt37) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:667:9: message_field[proto, extension]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_message_field_in_extend_body2655);
                    message_field169=message_field(proto, extension);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, message_field169.getTree());

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:668:9: annotation_entry[proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_annotation_entry_in_extend_body2666);
                    annotation_entry170=annotation_entry(proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, annotation_entry170.getTree());

                    }
                    break;
                case 3 :
                    // io/protostuff/parser/ProtoParser.g:669:9: doc_entry[proto]
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_doc_entry_in_extend_body2677);
                    doc_entry171=doc_entry(proto);

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, doc_entry171.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "extend_body"

    public static class ignore_block_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ignore_block"
    // io/protostuff/parser/ProtoParser.g:672:1: ignore_block : LEFTCURLY ( ignore_block_body )* RIGHTCURLY ;
    public final ProtoParser.ignore_block_return ignore_block() throws RecognitionException {
        ProtoParser.ignore_block_return retval = new ProtoParser.ignore_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LEFTCURLY172=null;
        Token RIGHTCURLY174=null;
        ProtoParser.ignore_block_body_return ignore_block_body173 = null;


        Object LEFTCURLY172_tree=null;
        Object RIGHTCURLY174_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:673:5: ( LEFTCURLY ( ignore_block_body )* RIGHTCURLY )
            // io/protostuff/parser/ProtoParser.g:673:9: LEFTCURLY ( ignore_block_body )* RIGHTCURLY
            {
            root_0 = (Object)adaptor.nil();

            LEFTCURLY172=(Token)match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_ignore_block2701); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            LEFTCURLY172_tree = (Object)adaptor.create(LEFTCURLY172);
            adaptor.addChild(root_0, LEFTCURLY172_tree);
            }
            // io/protostuff/parser/ProtoParser.g:673:19: ( ignore_block_body )*
            loop38:
            do {
                int alt38=2;
                switch ( input.LA(1) ) {
                case ASSIGN:
                case AT:
                case LEFTCURLY:
                case LEFTPAREN:
                case RIGHTPAREN:
                case LEFTSQUARE:
                case RIGHTSQUARE:
                case SEMICOLON:
                case COMMA:
                case PLUS:
                case MINUS:
                case TO:
                case TRUE:
                case FALSE:
                case PKG:
                case SYNTAX:
                case IMPORT:
                case OPTION:
                case MESSAGE:
                case SERVICE:
                case ENUM:
                case REQUIRED:
                case OPTIONAL:
                case REPEATED:
                case EXTENSIONS:
                case EXTEND:
                case GROUP:
                case RPC:
                case RETURNS:
                case INT32:
                case INT64:
                case UINT32:
                case UINT64:
                case SINT32:
                case SINT64:
                case FIXED32:
                case FIXED64:
                case SFIXED32:
                case SFIXED64:
                case FLOAT:
                case DOUBLE:
                case BOOL:
                case STRING:
                case BYTES:
                case DEFAULT:
                case MAX:
                case VOID:
                case ID:
                case FULL_ID:
                case NUMINT:
                case EXP:
                case NUMFLOAT:
                case NUMDOUBLE:
                case HEX_DIGIT:
                case HEX:
                case OCTAL:
                case DOC:
                case COMMENT:
                case WS:
                case ESC_SEQ:
                case STRING_LITERAL:
                case UNICODE_ESC:
                case OCTAL_ESC:
                    {
                    alt38=1;
                    }
                    break;

                }

                switch (alt38) {
            	case 1 :
            	    // io/protostuff/parser/ProtoParser.g:673:19: ignore_block_body
            	    {
            	    pushFollow(FOLLOW_ignore_block_body_in_ignore_block2703);
            	    ignore_block_body173=ignore_block_body();

            	    state._fsp--;
            	    if (state.failed) return retval;
            	    if ( state.backtracking==0 ) adaptor.addChild(root_0, ignore_block_body173.getTree());

            	    }
            	    break;

            	default :
            	    break loop38;
                }
            } while (true);

            RIGHTCURLY174=(Token)match(input,RIGHTCURLY,FOLLOW_RIGHTCURLY_in_ignore_block2706); if (state.failed) return retval;
            if ( state.backtracking==0 ) {
            RIGHTCURLY174_tree = (Object)adaptor.create(RIGHTCURLY174);
            adaptor.addChild(root_0, RIGHTCURLY174_tree);
            }

            }

            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ignore_block"

    public static class ignore_block_body_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "ignore_block_body"
    // io/protostuff/parser/ProtoParser.g:676:1: ignore_block_body : ( ( LEFTCURLY )=> ignore_block | ~ RIGHTCURLY );
    public final ProtoParser.ignore_block_body_return ignore_block_body() throws RecognitionException {
        ProtoParser.ignore_block_body_return retval = new ProtoParser.ignore_block_body_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set176=null;
        ProtoParser.ignore_block_return ignore_block175 = null;


        Object set176_tree=null;

        try {
            // io/protostuff/parser/ProtoParser.g:677:5: ( ( LEFTCURLY )=> ignore_block | ~ RIGHTCURLY )
            int alt39=2;
            switch ( input.LA(1) ) {
            case LEFTCURLY:
                {
                int LA39_1 = input.LA(2);

                if ( (synpred1_ProtoParser()) ) {
                    alt39=1;
                }
                else if ( (true) ) {
                    alt39=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return retval;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 39, 1, input);

                    throw nvae;
                }
                }
                break;
            case ASSIGN:
            case AT:
            case LEFTPAREN:
            case RIGHTPAREN:
            case LEFTSQUARE:
            case RIGHTSQUARE:
            case SEMICOLON:
            case COMMA:
            case PLUS:
            case MINUS:
            case TO:
            case TRUE:
            case FALSE:
            case PKG:
            case SYNTAX:
            case IMPORT:
            case OPTION:
            case MESSAGE:
            case SERVICE:
            case ENUM:
            case REQUIRED:
            case OPTIONAL:
            case REPEATED:
            case EXTENSIONS:
            case EXTEND:
            case GROUP:
            case RPC:
            case RETURNS:
            case INT32:
            case INT64:
            case UINT32:
            case UINT64:
            case SINT32:
            case SINT64:
            case FIXED32:
            case FIXED64:
            case SFIXED32:
            case SFIXED64:
            case FLOAT:
            case DOUBLE:
            case BOOL:
            case STRING:
            case BYTES:
            case DEFAULT:
            case MAX:
            case VOID:
            case ID:
            case FULL_ID:
            case NUMINT:
            case EXP:
            case NUMFLOAT:
            case NUMDOUBLE:
            case HEX_DIGIT:
            case HEX:
            case OCTAL:
            case DOC:
            case COMMENT:
            case WS:
            case ESC_SEQ:
            case STRING_LITERAL:
            case UNICODE_ESC:
            case OCTAL_ESC:
                {
                alt39=2;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 39, 0, input);

                throw nvae;
            }

            switch (alt39) {
                case 1 :
                    // io/protostuff/parser/ProtoParser.g:677:9: ( LEFTCURLY )=> ignore_block
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_ignore_block_in_ignore_block_body2734);
                    ignore_block175=ignore_block();

                    state._fsp--;
                    if (state.failed) return retval;
                    if ( state.backtracking==0 ) adaptor.addChild(root_0, ignore_block175.getTree());

                    }
                    break;
                case 2 :
                    // io/protostuff/parser/ProtoParser.g:678:9: ~ RIGHTCURLY
                    {
                    root_0 = (Object)adaptor.nil();

                    set176=(Token)input.LT(1);
                    if ( (input.LA(1)>=ASSIGN && input.LA(1)<=LEFTCURLY)||(input.LA(1)>=LEFTPAREN && input.LA(1)<=OCTAL_ESC) ) {
                        input.consume();
                        if ( state.backtracking==0 ) adaptor.addChild(root_0, (Object)adaptor.create(set176));
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            if ( state.backtracking==0 ) {

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
    	retval.tree = (Object)adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "ignore_block_body"

    // $ANTLR start synpred1_ProtoParser
    public final void synpred1_ProtoParser_fragment() throws RecognitionException {   
        // io/protostuff/parser/ProtoParser.g:677:9: ( LEFTCURLY )
        // io/protostuff/parser/ProtoParser.g:677:10: LEFTCURLY
        {
        match(input,LEFTCURLY,FOLLOW_LEFTCURLY_in_synpred1_ProtoParser2730); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred1_ProtoParser

    // Delegated rules

    public final boolean synpred1_ProtoParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred1_ProtoParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


 

    public static final BitSet FOLLOW_statement_in_parse178 = new BitSet(new long[]{0x2000000043F80020L});
    public static final BitSet FOLLOW_EOF_in_parse183 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_header_syntax_in_statement211 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_header_package_in_statement222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_header_import_in_statement233 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_message_block_in_statement244 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_block_in_statement255 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_extend_block_in_statement266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_service_block_in_statement277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_entry_in_statement288 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_doc_entry_in_statement299 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_option_entry_in_statement310 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_var_reserved0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_var520 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_reserved_in_var524 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FULL_ID_in_var_full543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_in_var_full547 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AT_in_annotation_entry573 = new BitSet(new long[]{0x001FFFFFFFF90000L});
    public static final BitSet FOLLOW_var_in_annotation_entry575 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_LEFTPAREN_in_annotation_entry588 = new BitSet(new long[]{0x003FFFFFFFF90000L});
    public static final BitSet FOLLOW_annotation_keyval_in_annotation_entry599 = new BitSet(new long[]{0x0000000000002200L});
    public static final BitSet FOLLOW_COMMA_in_annotation_entry603 = new BitSet(new long[]{0x003FFFFFFFF90000L});
    public static final BitSet FOLLOW_annotation_keyval_in_annotation_entry605 = new BitSet(new long[]{0x0000000000002200L});
    public static final BitSet FOLLOW_RIGHTPAREN_in_annotation_entry619 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_full_in_annotation_keyval646 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ASSIGN_in_annotation_keyval648 = new BitSet(new long[]{0x037FFFFFFFFF0000L,0x0000000000000002L});
    public static final BitSet FOLLOW_var_reserved_in_annotation_keyval670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_annotation_keyval690 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FULL_ID_in_annotation_keyval712 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMFLOAT_in_annotation_keyval732 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMINT_in_annotation_keyval752 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMDOUBLE_in_annotation_keyval772 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_annotation_keyval792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_annotation_keyval812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_annotation_keyval832 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOC_in_doc_entry865 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SYNTAX_in_header_syntax888 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ASSIGN_in_header_syntax890 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_header_syntax892 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_SEMICOLON_in_header_syntax894 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PKG_in_header_package923 = new BitSet(new long[]{0x003FFFFFFFF90000L});
    public static final BitSet FOLLOW_FULL_ID_in_header_package926 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_var_in_header_package932 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_SEMICOLON_in_header_package937 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_header_import965 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_header_import967 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_SEMICOLON_in_header_import969 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPTION_in_option_entry993 = new BitSet(new long[]{0x003FFFFFFFF90100L});
    public static final BitSet FOLLOW_LEFTPAREN_in_option_entry995 = new BitSet(new long[]{0x003FFFFFFFF90000L});
    public static final BitSet FOLLOW_var_full_in_option_entry1000 = new BitSet(new long[]{0x0000000000000210L});
    public static final BitSet FOLLOW_RIGHTPAREN_in_option_entry1002 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ASSIGN_in_option_entry1005 = new BitSet(new long[]{0x037FFFFFFFFF0000L,0x0000000000000002L});
    public static final BitSet FOLLOW_var_reserved_in_option_entry1027 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_ID_in_option_entry1049 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_FULL_ID_in_option_entry1071 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_NUMFLOAT_in_option_entry1091 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_NUMINT_in_option_entry1111 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_NUMDOUBLE_in_option_entry1131 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_TRUE_in_option_entry1151 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_FALSE_in_option_entry1171 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_option_entry1191 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_SEMICOLON_in_option_entry1205 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MESSAGE_in_message_block1238 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_ID_in_message_block1240 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_LEFTCURLY_in_message_block1253 = new BitSet(new long[]{0x200000007FF800A0L});
    public static final BitSet FOLLOW_message_body_in_message_block1256 = new BitSet(new long[]{0x200000007FF800A0L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_message_block1261 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_message_block_in_message_body1284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_message_field_in_message_body1295 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_block_in_message_body1306 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_service_block_in_message_body1317 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_extend_block_in_message_body1328 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_extensions_range_in_message_body1339 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_entry_in_message_body1350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_doc_entry_in_message_body1361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_option_entry_in_message_body1372 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTENSIONS_in_extensions_range1403 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_NUMINT_in_extensions_range1407 = new BitSet(new long[]{0x0000000000011000L});
    public static final BitSet FOLLOW_TO_in_extensions_range1421 = new BitSet(new long[]{0x0044000000000000L});
    public static final BitSet FOLLOW_NUMINT_in_extensions_range1427 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_MAX_in_extensions_range1433 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_SEMICOLON_in_extensions_range1450 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OPTIONAL_in_message_field1484 = new BitSet(new long[]{0x0031FFFC80000000L});
    public static final BitSet FOLLOW_REQUIRED_in_message_field1501 = new BitSet(new long[]{0x0031FFFC80000000L});
    public static final BitSet FOLLOW_REPEATED_in_message_field1518 = new BitSet(new long[]{0x0031FFFC80000000L});
    public static final BitSet FOLLOW_field_type_in_message_field1533 = new BitSet(new long[]{0x001FFFFFFFF90000L});
    public static final BitSet FOLLOW_var_in_message_field1545 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ASSIGN_in_message_field1547 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_NUMINT_in_message_field1549 = new BitSet(new long[]{0x0000000000001440L});
    public static final BitSet FOLLOW_field_options_in_message_field1563 = new BitSet(new long[]{0x0000000000001440L});
    public static final BitSet FOLLOW_SEMICOLON_in_message_field1579 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ignore_block_in_message_field1584 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT32_in_field_type1610 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UINT32_in_field_type1622 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SINT32_in_field_type1634 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FIXED32_in_field_type1646 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SFIXED32_in_field_type1658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT64_in_field_type1670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UINT64_in_field_type1682 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SINT64_in_field_type1694 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FIXED64_in_field_type1706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SFIXED64_in_field_type1718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_field_type1730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLE_in_field_type1742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BOOL_in_field_type1754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_field_type1766 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BYTES_in_field_type1778 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GROUP_in_field_type1790 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FULL_ID_in_field_type1802 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_field_type1814 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFTSQUARE_in_field_options1841 = new BitSet(new long[]{0x003FFFFFFFF90000L});
    public static final BitSet FOLLOW_field_options_keyval_in_field_options1843 = new BitSet(new long[]{0x0000000000002800L});
    public static final BitSet FOLLOW_COMMA_in_field_options1856 = new BitSet(new long[]{0x003FFFFFFFF90000L});
    public static final BitSet FOLLOW_field_options_keyval_in_field_options1858 = new BitSet(new long[]{0x0000000000002800L});
    public static final BitSet FOLLOW_RIGHTSQUARE_in_field_options1863 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_var_full_in_field_options_keyval1890 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ASSIGN_in_field_options_keyval1892 = new BitSet(new long[]{0x1BFFFFFFFFFF8000L,0x0000000000000002L});
    public static final BitSet FOLLOW_var_reserved_in_field_options_keyval1897 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_LITERAL_in_field_options_keyval1910 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMFLOAT_in_field_options_keyval1922 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMINT_in_field_options_keyval1935 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMDOUBLE_in_field_options_keyval1947 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HEX_in_field_options_keyval1959 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_OCTAL_in_field_options_keyval1971 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRUE_in_field_options_keyval1983 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FALSE_in_field_options_keyval1999 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_field_options_keyval2013 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FULL_ID_in_field_options_keyval2025 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXP_in_field_options_keyval2037 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_signed_constant_in_field_options_keyval2049 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_MINUS_in_signed_constant2087 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_ID_in_signed_constant2089 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ENUM_in_enum_block2121 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_ID_in_enum_block2123 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_LEFTCURLY_in_enum_block2136 = new BitSet(new long[]{0x2010000043F800A0L});
    public static final BitSet FOLLOW_enum_body_in_enum_block2139 = new BitSet(new long[]{0x2010000043F800A0L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_enum_block2144 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_SEMICOLON_in_enum_block2149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enum_field_in_enum_body2177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_entry_in_enum_body2188 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_doc_entry_in_enum_body2199 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_option_entry_in_enum_body2210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_enum_field2237 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ASSIGN_in_enum_field2239 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_NUMINT_in_enum_field2241 = new BitSet(new long[]{0x0000000000001400L});
    public static final BitSet FOLLOW_enum_options_in_enum_field2246 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_SEMICOLON_in_enum_field2251 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFTSQUARE_in_enum_options2274 = new BitSet(new long[]{0x003FFFFFFFF90000L});
    public static final BitSet FOLLOW_field_options_keyval_in_enum_options2276 = new BitSet(new long[]{0x0000000000002800L});
    public static final BitSet FOLLOW_COMMA_in_enum_options2289 = new BitSet(new long[]{0x003FFFFFFFF90000L});
    public static final BitSet FOLLOW_field_options_keyval_in_enum_options2291 = new BitSet(new long[]{0x0000000000002800L});
    public static final BitSet FOLLOW_RIGHTSQUARE_in_enum_options2296 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SERVICE_in_service_block2326 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_ID_in_service_block2328 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_LEFTCURLY_in_service_block2332 = new BitSet(new long[]{0x2000000143F80020L});
    public static final BitSet FOLLOW_service_body_in_service_block2343 = new BitSet(new long[]{0x2000000143F800A0L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_service_block2348 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_SEMICOLON_in_service_block2351 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rpc_block_in_service_body2381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_entry_in_service_body2392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_doc_entry_in_service_body2403 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_option_entry_in_service_body2414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RPC_in_rpc_block2445 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_ID_in_rpc_block2449 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LEFTPAREN_in_rpc_block2451 = new BitSet(new long[]{0x0038000000000000L});
    public static final BitSet FOLLOW_FULL_ID_in_rpc_block2456 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_set_in_rpc_block2464 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_RIGHTPAREN_in_rpc_block2473 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_RETURNS_in_rpc_block2484 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LEFTPAREN_in_rpc_block2486 = new BitSet(new long[]{0x0038000000000000L});
    public static final BitSet FOLLOW_FULL_ID_in_rpc_block2491 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_set_in_rpc_block2499 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_RIGHTPAREN_in_rpc_block2508 = new BitSet(new long[]{0x0000000000001040L});
    public static final BitSet FOLLOW_rpc_body_block_in_rpc_block2512 = new BitSet(new long[]{0x0000000000001000L});
    public static final BitSet FOLLOW_SEMICOLON_in_rpc_block2516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFTCURLY_in_rpc_body_block2542 = new BitSet(new long[]{0x2000000043F800A0L});
    public static final BitSet FOLLOW_option_entry_in_rpc_body_block2544 = new BitSet(new long[]{0x2000000043F800A0L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_rpc_body_block2548 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EXTEND_in_extend_block2580 = new BitSet(new long[]{0x0030000000000000L});
    public static final BitSet FOLLOW_FULL_ID_in_extend_block2592 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_ID_in_extend_block2598 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_LEFTCURLY_in_extend_block2614 = new BitSet(new long[]{0x200000001C0000A0L});
    public static final BitSet FOLLOW_extend_body_in_extend_block2617 = new BitSet(new long[]{0x200000001C0000A0L});
    public static final BitSet FOLLOW_RIGHTCURLY_in_extend_block2622 = new BitSet(new long[]{0x0000000000001002L});
    public static final BitSet FOLLOW_SEMICOLON_in_extend_block2627 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_message_field_in_extend_body2655 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_entry_in_extend_body2666 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_doc_entry_in_extend_body2677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFTCURLY_in_ignore_block2701 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x000000000000000FL});
    public static final BitSet FOLLOW_ignore_block_body_in_ignore_block2703 = new BitSet(new long[]{0xFFFFFFFFFFFFFFF0L,0x000000000000000FL});
    public static final BitSet FOLLOW_RIGHTCURLY_in_ignore_block2706 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ignore_block_in_ignore_block_body2734 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_ignore_block_body2744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LEFTCURLY_in_synpred1_ProtoParser2730 = new BitSet(new long[]{0x0000000000000002L});

}