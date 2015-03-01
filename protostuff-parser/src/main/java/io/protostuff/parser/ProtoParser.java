// $ANTLR 3.2 Sep 23, 2009 14:05:07 io/protostuff/parser/ProtoParser.g 2014-01-20 11:24:40

package io.protostuff.parser;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;

public class ProtoParser extends AbstractParser
{
    public static final String[] tokenNames = new String[] {
            "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ASSIGN", "AT", "LEFTCURLY", "RIGHTCURLY", "LEFTPAREN",
            "RIGHTPAREN", "LEFTSQUARE", "RIGHTSQUARE", "SEMICOLON", "COMMA", "PLUS", "MINUS", "TO", "TRUE", "FALSE",
            "PKG", "SYNTAX", "IMPORT", "OPTION", "MESSAGE", "SERVICE", "ENUM", "REQUIRED", "OPTIONAL", "REPEATED",
            "EXTENSIONS", "EXTEND", "GROUP", "RPC", "RETURNS", "INT32", "INT64", "UINT32", "UINT64", "SINT32",
            "SINT64", "FIXED32", "FIXED64", "SFIXED32", "SFIXED64", "FLOAT", "DOUBLE", "BOOL", "STRING", "BYTES",
            "DEFAULT", "MAX", "VOID", "ID", "FULL_ID", "NUMINT", "EXP", "NUMFLOAT", "NUMDOUBLE", "HEX_DIGIT", "HEX",
            "OCTAL", "COMMENT", "WS", "ESC_SEQ", "STRING_LITERAL", "UNICODE_ESC", "OCTAL_ESC"
    };
    public static final int OPTION = 22;
    public static final int FULL_ID = 53;
    public static final int PKG = 19;
    public static final int OCTAL_ESC = 66;
    public static final int NUMFLOAT = 56;
    public static final int MAX = 50;
    public static final int FLOAT = 44;
    public static final int FIXED64 = 41;
    public static final int ID = 52;
    public static final int EOF = -1;
    public static final int AT = 5;
    public static final int SYNTAX = 20;
    public static final int LEFTPAREN = 8;
    public static final int EXTEND = 30;
    public static final int IMPORT = 21;
    public static final int STRING_LITERAL = 64;
    public static final int ESC_SEQ = 63;
    public static final int EXTENSIONS = 29;
    public static final int SINT64 = 39;
    public static final int LEFTCURLY = 6;
    public static final int EXP = 55;
    public static final int HEX = 59;
    public static final int COMMA = 13;
    public static final int FIXED32 = 40;
    public static final int RIGHTCURLY = 7;
    public static final int SFIXED32 = 42;
    public static final int DOUBLE = 45;
    public static final int MESSAGE = 23;
    public static final int PLUS = 14;
    public static final int VOID = 51;
    public static final int NUMDOUBLE = 57;
    public static final int COMMENT = 61;
    public static final int NUMINT = 54;
    public static final int SINT32 = 38;
    public static final int RETURNS = 33;
    public static final int TO = 16;
    public static final int INT64 = 35;
    public static final int RIGHTSQUARE = 11;
    public static final int UNICODE_ESC = 65;
    public static final int DEFAULT = 49;
    public static final int BOOL = 46;
    public static final int REPEATED = 28;
    public static final int HEX_DIGIT = 58;
    public static final int SEMICOLON = 12;
    public static final int REQUIRED = 26;
    public static final int MINUS = 15;
    public static final int TRUE = 17;
    public static final int UINT64 = 37;
    public static final int OPTIONAL = 27;
    public static final int INT32 = 34;
    public static final int GROUP = 31;
    public static final int WS = 62;
    public static final int ENUM = 25;
    public static final int SERVICE = 24;
    public static final int RIGHTPAREN = 9;
    public static final int LEFTSQUARE = 10;
    public static final int SFIXED64 = 43;
    public static final int BYTES = 48;
    public static final int ASSIGN = 4;
    public static final int RPC = 32;
    public static final int OCTAL = 60;
    public static final int UINT32 = 36;
    public static final int FALSE = 18;
    public static final int STRING = 47;

    // delegates
    // delegators

    public ProtoParser(TokenStream input)
    {
        this(input, new RecognizerSharedState());
    }

    public ProtoParser(TokenStream input, RecognizerSharedState state)
    {
        super(input, state);

    }

    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor)
    {
        this.adaptor = adaptor;
    }

    public TreeAdaptor getTreeAdaptor()
    {
        return adaptor;
    }

    @Override
    public String[] getTokenNames()
    {
        return ProtoParser.tokenNames;
    }

    @Override
    public String getGrammarFileName()
    {
        return "io/protostuff/parser/ProtoParser.g";
    }

    public static class parse_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "parse"
    // io/protostuff/parser/ProtoParser.g:52:1: parse[Proto proto] : ( statement[proto] )+ EOF ;
    public final ProtoParser.parse_return parse(Proto proto) throws RecognitionException
    {
        ProtoParser.parse_return retval = new ProtoParser.parse_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token EOF2 = null;
        ProtoParser.statement_return statement1 = null;

        Object EOF2_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:53:5: ( ( statement[proto] )+ EOF )
            // io/protostuff/parser/ProtoParser.g:53:9: ( statement[proto] )+ EOF
            {
                root_0 = (Object) adaptor.nil();

                // io/protostuff/parser/ProtoParser.g:53:9: ( statement[proto] )+
                int cnt1 = 0;
                loop1: do
                {
                    int alt1 = 2;
                    switch (input.LA(1))
                    {
                        case AT:
                        case PKG:
                        case SYNTAX:
                        case IMPORT:
                        case OPTION:
                        case MESSAGE:
                        case SERVICE:
                        case ENUM:
                        case EXTEND:
                        {
                            alt1 = 1;
                        }
                            break;

                    }

                    switch (alt1)
                    {
                        case 1:
                        // io/protostuff/parser/ProtoParser.g:53:10: statement[proto]
                        {
                            pushFollow(FOLLOW_statement_in_parse178);
                            statement1 = statement(proto);

                            state._fsp--;
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                                adaptor.addChild(root_0, statement1.getTree());

                        }
                            break;

                        default:
                            if (cnt1 >= 1)
                                break loop1;
                            if (state.backtracking > 0)
                            {
                                state.failed = true;
                                return retval;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(1, input);
                            throw eee;
                    }
                    cnt1++;
                } while (true);

                EOF2 = (Token) match(input, EOF, FOLLOW_EOF_in_parse183);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {

                    if (!proto.annotations.isEmpty())
                        throw new IllegalStateException("Misplaced annotations: " + proto.annotations);

                    proto.postParse();

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "parse"

    public static class statement_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "statement"
    // io/protostuff/parser/ProtoParser.g:61:1: statement[Proto proto] : ( header_syntax[proto] |
    // header_package[proto] | header_import[proto] | message_block[proto, null] | enum_block[proto, null] |
    // extend_block[proto, null] | service_block[proto, null] | annotation_entry[proto] | option_entry[proto, proto] );
    public final ProtoParser.statement_return statement(Proto proto) throws RecognitionException
    {
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

        ProtoParser.option_entry_return option_entry11 = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:62:5: ( header_syntax[proto] | header_package[proto] |
            // header_import[proto] | message_block[proto, null] | enum_block[proto, null] | extend_block[proto, null] |
            // service_block[proto, null] | annotation_entry[proto] | option_entry[proto, proto] )
            int alt2 = 9;
            switch (input.LA(1))
            {
                case SYNTAX:
                {
                    alt2 = 1;
                }
                    break;
                case PKG:
                {
                    alt2 = 2;
                }
                    break;
                case IMPORT:
                {
                    alt2 = 3;
                }
                    break;
                case MESSAGE:
                {
                    alt2 = 4;
                }
                    break;
                case ENUM:
                {
                    alt2 = 5;
                }
                    break;
                case EXTEND:
                {
                    alt2 = 6;
                }
                    break;
                case SERVICE:
                {
                    alt2 = 7;
                }
                    break;
                case AT:
                {
                    alt2 = 8;
                }
                    break;
                case OPTION:
                {
                    alt2 = 9;
                }
                    break;
                default:
                    if (state.backtracking > 0)
                    {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 2, 0, input);

                    throw nvae;
            }

            switch (alt2)
            {
                case 1:
                // io/protostuff/parser/ProtoParser.g:62:9: header_syntax[proto]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_header_syntax_in_statement211);
                    header_syntax3 = header_syntax(proto);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, header_syntax3.getTree());

                }
                    break;
                case 2:
                // io/protostuff/parser/ProtoParser.g:63:9: header_package[proto]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_header_package_in_statement222);
                    header_package4 = header_package(proto);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, header_package4.getTree());

                }
                    break;
                case 3:
                // io/protostuff/parser/ProtoParser.g:64:9: header_import[proto]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_header_import_in_statement233);
                    header_import5 = header_import(proto);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, header_import5.getTree());

                }
                    break;
                case 4:
                // io/protostuff/parser/ProtoParser.g:65:9: message_block[proto, null]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_message_block_in_statement244);
                    message_block6 = message_block(proto, null);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, message_block6.getTree());

                }
                    break;
                case 5:
                // io/protostuff/parser/ProtoParser.g:66:9: enum_block[proto, null]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_enum_block_in_statement255);
                    enum_block7 = enum_block(proto, null);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, enum_block7.getTree());

                }
                    break;
                case 6:
                // io/protostuff/parser/ProtoParser.g:67:9: extend_block[proto, null]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_extend_block_in_statement266);
                    extend_block8 = extend_block(proto, null);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, extend_block8.getTree());

                }
                    break;
                case 7:
                // io/protostuff/parser/ProtoParser.g:68:9: service_block[proto, null]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_service_block_in_statement277);
                    service_block9 = service_block(proto, null);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, service_block9.getTree());

                }
                    break;
                case 8:
                // io/protostuff/parser/ProtoParser.g:69:9: annotation_entry[proto]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_annotation_entry_in_statement288);
                    annotation_entry10 = annotation_entry(proto);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, annotation_entry10.getTree());

                }
                    break;
                case 9:
                // io/protostuff/parser/ProtoParser.g:70:9: option_entry[proto, proto]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_option_entry_in_statement299);
                    option_entry11 = option_entry(proto, proto);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, option_entry11.getTree());

                }
                    break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "statement"

    public static class var_reserved_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "var_reserved"
    // io/protostuff/parser/ProtoParser.g:74:1: var_reserved : ( TO | PKG | SYNTAX | IMPORT | OPTION |
    // MESSAGE | SERVICE | ENUM | REQUIRED | OPTIONAL | REPEATED | EXTENSIONS | EXTEND | GROUP | RPC | RETURNS | INT32 |
    // INT64 | UINT32 | UINT64 | SINT32 | SINT64 | FIXED32 | FIXED64 | SFIXED32 | SFIXED64 | FLOAT | DOUBLE | BOOL |
    // STRING | BYTES | DEFAULT | MAX | VOID );
    public final ProtoParser.var_reserved_return var_reserved() throws RecognitionException
    {
        ProtoParser.var_reserved_return retval = new ProtoParser.var_reserved_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set12 = null;

        Object set12_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:75:5: ( TO | PKG | SYNTAX | IMPORT | OPTION | MESSAGE |
            // SERVICE | ENUM | REQUIRED | OPTIONAL | REPEATED | EXTENSIONS | EXTEND | GROUP | RPC | RETURNS | INT32 |
            // INT64 | UINT32 | UINT64 | SINT32 | SINT64 | FIXED32 | FIXED64 | SFIXED32 | SFIXED64 | FLOAT | DOUBLE |
            // BOOL | STRING | BYTES | DEFAULT | MAX | VOID )
            // io/protostuff/parser/ProtoParser.g:
            {
                root_0 = (Object) adaptor.nil();

                set12 = (Token) input.LT(1);
                if (input.LA(1) == TO || (input.LA(1) >= PKG && input.LA(1) <= VOID))
                {
                    input.consume();
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, (Object) adaptor.create(set12));
                    state.errorRecovery = false;
                    state.failed = false;
                }
                else
                {
                    if (state.backtracking > 0)
                    {
                        state.failed = true;
                        return retval;
                    }
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    throw mse;
                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "var_reserved"

    public static class var_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "var"
    // io/protostuff/parser/ProtoParser.g:82:1: var : ( ID | var_reserved );
    public final ProtoParser.var_return var() throws RecognitionException
    {
        ProtoParser.var_return retval = new ProtoParser.var_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID13 = null;
        ProtoParser.var_reserved_return var_reserved14 = null;

        Object ID13_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:83:5: ( ID | var_reserved )
            int alt3 = 2;
            switch (input.LA(1))
            {
                case ID:
                {
                    alt3 = 1;
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
                    alt3 = 2;
                }
                    break;
                default:
                    if (state.backtracking > 0)
                    {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 3, 0, input);

                    throw nvae;
            }

            switch (alt3)
            {
                case 1:
                // io/protostuff/parser/ProtoParser.g:83:9: ID
                {
                    root_0 = (Object) adaptor.nil();

                    ID13 = (Token) match(input, ID, FOLLOW_ID_in_var509);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        ID13_tree = (Object) adaptor.create(ID13);
                        adaptor.addChild(root_0, ID13_tree);
                    }

                }
                    break;
                case 2:
                // io/protostuff/parser/ProtoParser.g:83:14: var_reserved
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_var_reserved_in_var513);
                    var_reserved14 = var_reserved();

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, var_reserved14.getTree());

                }
                    break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "var"

    public static class var_full_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "var_full"
    // io/protostuff/parser/ProtoParser.g:86:1: var_full : ( FULL_ID | var );
    public final ProtoParser.var_full_return var_full() throws RecognitionException
    {
        ProtoParser.var_full_return retval = new ProtoParser.var_full_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token FULL_ID15 = null;
        ProtoParser.var_return var16 = null;

        Object FULL_ID15_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:87:5: ( FULL_ID | var )
            int alt4 = 2;
            switch (input.LA(1))
            {
                case FULL_ID:
                {
                    alt4 = 1;
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
                    alt4 = 2;
                }
                    break;
                default:
                    if (state.backtracking > 0)
                    {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 4, 0, input);

                    throw nvae;
            }

            switch (alt4)
            {
                case 1:
                // io/protostuff/parser/ProtoParser.g:87:9: FULL_ID
                {
                    root_0 = (Object) adaptor.nil();

                    FULL_ID15 = (Token) match(input, FULL_ID, FOLLOW_FULL_ID_in_var_full532);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        FULL_ID15_tree = (Object) adaptor.create(FULL_ID15);
                        adaptor.addChild(root_0, FULL_ID15_tree);
                    }

                }
                    break;
                case 2:
                // io/protostuff/parser/ProtoParser.g:87:19: var
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_var_in_var_full536);
                    var16 = var();

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, var16.getTree());

                }
                    break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "var_full"

    public static class annotation_entry_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "annotation_entry"
    // io/protostuff/parser/ProtoParser.g:90:1: annotation_entry[Proto proto] : AT var ( LEFTPAREN
    // annotation_keyval[proto, annotation] ( COMMA annotation_keyval[proto, annotation] )* RIGHTPAREN )? ;
    public final ProtoParser.annotation_entry_return annotation_entry(Proto proto) throws RecognitionException
    {
        ProtoParser.annotation_entry_return retval = new ProtoParser.annotation_entry_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token AT17 = null;
        Token LEFTPAREN19 = null;
        Token COMMA21 = null;
        Token RIGHTPAREN23 = null;
        ProtoParser.var_return var18 = null;

        ProtoParser.annotation_keyval_return annotation_keyval20 = null;

        ProtoParser.annotation_keyval_return annotation_keyval22 = null;

        Object AT17_tree = null;
        Object LEFTPAREN19_tree = null;
        Object COMMA21_tree = null;
        Object RIGHTPAREN23_tree = null;

        Annotation annotation = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:94:5: ( AT var ( LEFTPAREN annotation_keyval[proto,
            // annotation] ( COMMA annotation_keyval[proto, annotation] )* RIGHTPAREN )? )
            // io/protostuff/parser/ProtoParser.g:94:9: AT var ( LEFTPAREN annotation_keyval[proto,
            // annotation] ( COMMA annotation_keyval[proto, annotation] )* RIGHTPAREN )?
            {
                root_0 = (Object) adaptor.nil();

                AT17 = (Token) match(input, AT, FOLLOW_AT_in_annotation_entry562);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    AT17_tree = (Object) adaptor.create(AT17);
                    adaptor.addChild(root_0, AT17_tree);
                }
                pushFollow(FOLLOW_var_in_annotation_entry564);
                var18 = var();

                state._fsp--;
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                    adaptor.addChild(root_0, var18.getTree());
                if (state.backtracking == 0)
                {
                    annotation = new Annotation((var18 != null ? input.toString(var18.start, var18.stop) : null));
                }
                // io/protostuff/parser/ProtoParser.g:95:9: ( LEFTPAREN annotation_keyval[proto, annotation]
                // ( COMMA annotation_keyval[proto, annotation] )* RIGHTPAREN )?
                int alt6 = 2;
                switch (input.LA(1))
                {
                    case LEFTPAREN:
                    {
                        alt6 = 1;
                    }
                        break;
                }

                switch (alt6)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:95:10: LEFTPAREN annotation_keyval[proto,
                    // annotation] ( COMMA annotation_keyval[proto, annotation] )* RIGHTPAREN
                    {
                        LEFTPAREN19 = (Token) match(input, LEFTPAREN, FOLLOW_LEFTPAREN_in_annotation_entry577);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            LEFTPAREN19_tree = (Object) adaptor.create(LEFTPAREN19);
                            adaptor.addChild(root_0, LEFTPAREN19_tree);
                        }
                        pushFollow(FOLLOW_annotation_keyval_in_annotation_entry588);
                        annotation_keyval20 = annotation_keyval(proto, annotation);

                        state._fsp--;
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                            adaptor.addChild(root_0, annotation_keyval20.getTree());
                        // io/protostuff/parser/ProtoParser.g:96:46: ( COMMA annotation_keyval[proto,
                        // annotation] )*
                        loop5: do
                        {
                            int alt5 = 2;
                            switch (input.LA(1))
                            {
                                case COMMA:
                                {
                                    alt5 = 1;
                                }
                                    break;

                            }

                            switch (alt5)
                            {
                                case 1:
                                // io/protostuff/parser/ProtoParser.g:96:47: COMMA annotation_keyval[proto,
                                // annotation]
                                {
                                    COMMA21 = (Token) match(input, COMMA, FOLLOW_COMMA_in_annotation_entry592);
                                    if (state.failed)
                                        return retval;
                                    if (state.backtracking == 0)
                                    {
                                        COMMA21_tree = (Object) adaptor.create(COMMA21);
                                        adaptor.addChild(root_0, COMMA21_tree);
                                    }
                                    pushFollow(FOLLOW_annotation_keyval_in_annotation_entry594);
                                    annotation_keyval22 = annotation_keyval(proto, annotation);

                                    state._fsp--;
                                    if (state.failed)
                                        return retval;
                                    if (state.backtracking == 0)
                                        adaptor.addChild(root_0, annotation_keyval22.getTree());

                                }
                                    break;

                                default:
                                    break loop5;
                            }
                        } while (true);

                        RIGHTPAREN23 = (Token) match(input, RIGHTPAREN, FOLLOW_RIGHTPAREN_in_annotation_entry608);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            RIGHTPAREN23_tree = (Object) adaptor.create(RIGHTPAREN23);
                            adaptor.addChild(root_0, RIGHTPAREN23_tree);
                        }

                    }
                        break;

                }

                if (state.backtracking == 0)
                {

                    proto.add(annotation);

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "annotation_entry"

    public static class annotation_keyval_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "annotation_keyval"
    // io/protostuff/parser/ProtoParser.g:102:1: annotation_keyval[Proto proto, Annotation annotation] : k=
    // var_full ASSIGN (vr= var_reserved | ID | fid= FULL_ID | NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE |
    // STRING_LITERAL ) ;
    public final ProtoParser.annotation_keyval_return annotation_keyval(Proto proto, Annotation annotation)
            throws RecognitionException
    {
        ProtoParser.annotation_keyval_return retval = new ProtoParser.annotation_keyval_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token fid = null;
        Token ASSIGN24 = null;
        Token ID25 = null;
        Token NUMFLOAT26 = null;
        Token NUMINT27 = null;
        Token NUMDOUBLE28 = null;
        Token TRUE29 = null;
        Token FALSE30 = null;
        Token STRING_LITERAL31 = null;
        ProtoParser.var_full_return k = null;

        ProtoParser.var_reserved_return vr = null;

        Object fid_tree = null;
        Object ASSIGN24_tree = null;
        Object ID25_tree = null;
        Object NUMFLOAT26_tree = null;
        Object NUMINT27_tree = null;
        Object NUMDOUBLE28_tree = null;
        Object TRUE29_tree = null;
        Object FALSE30_tree = null;
        Object STRING_LITERAL31_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:103:5: (k= var_full ASSIGN (vr= var_reserved | ID | fid=
            // FULL_ID | NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE | STRING_LITERAL ) )
            // io/protostuff/parser/ProtoParser.g:103:9: k= var_full ASSIGN (vr= var_reserved | ID | fid=
            // FULL_ID | NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE | STRING_LITERAL )
            {
                root_0 = (Object) adaptor.nil();

                pushFollow(FOLLOW_var_full_in_annotation_keyval635);
                k = var_full();

                state._fsp--;
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                    adaptor.addChild(root_0, k.getTree());
                ASSIGN24 = (Token) match(input, ASSIGN, FOLLOW_ASSIGN_in_annotation_keyval637);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    ASSIGN24_tree = (Object) adaptor.create(ASSIGN24);
                    adaptor.addChild(root_0, ASSIGN24_tree);
                }
                // io/protostuff/parser/ProtoParser.g:103:27: (vr= var_reserved | ID | fid= FULL_ID |
                // NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE | STRING_LITERAL )
                int alt7 = 9;
                switch (input.LA(1))
                {
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
                        alt7 = 1;
                    }
                        break;
                    case ID:
                    {
                        alt7 = 2;
                    }
                        break;
                    case FULL_ID:
                    {
                        alt7 = 3;
                    }
                        break;
                    case NUMFLOAT:
                    {
                        alt7 = 4;
                    }
                        break;
                    case NUMINT:
                    {
                        alt7 = 5;
                    }
                        break;
                    case NUMDOUBLE:
                    {
                        alt7 = 6;
                    }
                        break;
                    case TRUE:
                    {
                        alt7 = 7;
                    }
                        break;
                    case FALSE:
                    {
                        alt7 = 8;
                    }
                        break;
                    case STRING_LITERAL:
                    {
                        alt7 = 9;
                    }
                        break;
                    default:
                        if (state.backtracking > 0)
                        {
                            state.failed = true;
                            return retval;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 7, 0, input);

                        throw nvae;
                }

                switch (alt7)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:104:17: vr= var_reserved
                    {
                        pushFollow(FOLLOW_var_reserved_in_annotation_keyval659);
                        vr = var_reserved();

                        state._fsp--;
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                            adaptor.addChild(root_0, vr.getTree());
                        if (state.backtracking == 0)
                        {
                            annotation.put((k != null ? input.toString(k.start, k.stop) : null),
                                    (vr != null ? input.toString(vr.start, vr.stop) : null));
                        }

                    }
                        break;
                    case 2:
                    // io/protostuff/parser/ProtoParser.g:105:17: ID
                    {
                        ID25 = (Token) match(input, ID, FOLLOW_ID_in_annotation_keyval679);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            ID25_tree = (Object) adaptor.create(ID25);
                            adaptor.addChild(root_0, ID25_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            annotation.putRef((k != null ? input.toString(k.start, k.stop) : null),
                                    (ID25 != null ? ID25.getText() : null));
                        }

                    }
                        break;
                    case 3:
                    // io/protostuff/parser/ProtoParser.g:106:17: fid= FULL_ID
                    {
                        fid = (Token) match(input, FULL_ID, FOLLOW_FULL_ID_in_annotation_keyval701);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            fid_tree = (Object) adaptor.create(fid);
                            adaptor.addChild(root_0, fid_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            annotation.putRef((k != null ? input.toString(k.start, k.stop) : null),
                                    (fid != null ? fid.getText() : null));
                        }

                    }
                        break;
                    case 4:
                    // io/protostuff/parser/ProtoParser.g:107:17: NUMFLOAT
                    {
                        NUMFLOAT26 = (Token) match(input, NUMFLOAT, FOLLOW_NUMFLOAT_in_annotation_keyval721);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            NUMFLOAT26_tree = (Object) adaptor.create(NUMFLOAT26);
                            adaptor.addChild(root_0, NUMFLOAT26_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            annotation.put((k != null ? input.toString(k.start, k.stop) : null),
                                    Float.valueOf((NUMFLOAT26 != null ? NUMFLOAT26.getText() : null)));
                        }

                    }
                        break;
                    case 5:
                    // io/protostuff/parser/ProtoParser.g:108:17: NUMINT
                    {
                        NUMINT27 = (Token) match(input, NUMINT, FOLLOW_NUMINT_in_annotation_keyval741);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            NUMINT27_tree = (Object) adaptor.create(NUMINT27);
                            adaptor.addChild(root_0, NUMINT27_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            annotation.put((k != null ? input.toString(k.start, k.stop) : null),
                                    Integer.valueOf((NUMINT27 != null ? NUMINT27.getText() : null)));
                        }

                    }
                        break;
                    case 6:
                    // io/protostuff/parser/ProtoParser.g:109:17: NUMDOUBLE
                    {
                        NUMDOUBLE28 = (Token) match(input, NUMDOUBLE, FOLLOW_NUMDOUBLE_in_annotation_keyval761);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            NUMDOUBLE28_tree = (Object) adaptor.create(NUMDOUBLE28);
                            adaptor.addChild(root_0, NUMDOUBLE28_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            annotation.put((k != null ? input.toString(k.start, k.stop) : null),
                                    Double.valueOf((NUMDOUBLE28 != null ? NUMDOUBLE28.getText() : null)));
                        }

                    }
                        break;
                    case 7:
                    // io/protostuff/parser/ProtoParser.g:110:17: TRUE
                    {
                        TRUE29 = (Token) match(input, TRUE, FOLLOW_TRUE_in_annotation_keyval781);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            TRUE29_tree = (Object) adaptor.create(TRUE29);
                            adaptor.addChild(root_0, TRUE29_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            annotation.put((k != null ? input.toString(k.start, k.stop) : null), Boolean.TRUE);
                        }

                    }
                        break;
                    case 8:
                    // io/protostuff/parser/ProtoParser.g:111:17: FALSE
                    {
                        FALSE30 = (Token) match(input, FALSE, FOLLOW_FALSE_in_annotation_keyval801);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            FALSE30_tree = (Object) adaptor.create(FALSE30);
                            adaptor.addChild(root_0, FALSE30_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            annotation.put((k != null ? input.toString(k.start, k.stop) : null), Boolean.FALSE);
                        }

                    }
                        break;
                    case 9:
                    // io/protostuff/parser/ProtoParser.g:112:17: STRING_LITERAL
                    {
                        STRING_LITERAL31 = (Token) match(input, STRING_LITERAL,
                                FOLLOW_STRING_LITERAL_in_annotation_keyval821);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            STRING_LITERAL31_tree = (Object) adaptor.create(STRING_LITERAL31);
                            adaptor.addChild(root_0, STRING_LITERAL31_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            annotation.put((k != null ? input.toString(k.start, k.stop) : null),
                                    getStringFromStringLiteral((STRING_LITERAL31 != null ? STRING_LITERAL31.getText()
                                            : null)));
                        }

                    }
                        break;

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "annotation_keyval"

    public static class header_syntax_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "header_syntax"
    // io/protostuff/parser/ProtoParser.g:116:1: header_syntax[Proto proto] : SYNTAX ASSIGN STRING_LITERAL
    // SEMICOLON ;
    public final ProtoParser.header_syntax_return header_syntax(Proto proto) throws RecognitionException
    {
        ProtoParser.header_syntax_return retval = new ProtoParser.header_syntax_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SYNTAX32 = null;
        Token ASSIGN33 = null;
        Token STRING_LITERAL34 = null;
        Token SEMICOLON35 = null;

        Object SYNTAX32_tree = null;
        Object ASSIGN33_tree = null;
        Object STRING_LITERAL34_tree = null;
        Object SEMICOLON35_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:117:5: ( SYNTAX ASSIGN STRING_LITERAL SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:117:9: SYNTAX ASSIGN STRING_LITERAL SEMICOLON
            {
                root_0 = (Object) adaptor.nil();

                SYNTAX32 = (Token) match(input, SYNTAX, FOLLOW_SYNTAX_in_header_syntax854);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    SYNTAX32_tree = (Object) adaptor.create(SYNTAX32);
                    adaptor.addChild(root_0, SYNTAX32_tree);
                }
                ASSIGN33 = (Token) match(input, ASSIGN, FOLLOW_ASSIGN_in_header_syntax856);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    ASSIGN33_tree = (Object) adaptor.create(ASSIGN33);
                    adaptor.addChild(root_0, ASSIGN33_tree);
                }
                STRING_LITERAL34 = (Token) match(input, STRING_LITERAL, FOLLOW_STRING_LITERAL_in_header_syntax858);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    STRING_LITERAL34_tree = (Object) adaptor.create(STRING_LITERAL34);
                    adaptor.addChild(root_0, STRING_LITERAL34_tree);
                }
                SEMICOLON35 = (Token) match(input, SEMICOLON, FOLLOW_SEMICOLON_in_header_syntax860);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {

                    if (!"proto2".equals(getStringFromStringLiteral((STRING_LITERAL34 != null ? STRING_LITERAL34
                            .getText() : null))))
                    {
                        throw new IllegalStateException("Syntax isn't proto2: '"
                                +
                                getStringFromStringLiteral((STRING_LITERAL34 != null ? STRING_LITERAL34.getText()
                                        : null)) + "'");
                    }

                    if (!proto.annotations.isEmpty())
                        throw new IllegalStateException("Misplaced annotations: " + proto.annotations);

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "header_syntax"

    public static class header_package_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "header_package"
    // io/protostuff/parser/ProtoParser.g:128:1: header_package[Proto proto] : PKG ( FULL_ID | var )
    // SEMICOLON ;
    public final ProtoParser.header_package_return header_package(Proto proto) throws RecognitionException
    {
        ProtoParser.header_package_return retval = new ProtoParser.header_package_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token PKG36 = null;
        Token FULL_ID37 = null;
        Token SEMICOLON39 = null;
        ProtoParser.var_return var38 = null;

        Object PKG36_tree = null;
        Object FULL_ID37_tree = null;
        Object SEMICOLON39_tree = null;

        String value = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:132:5: ( PKG ( FULL_ID | var ) SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:132:9: PKG ( FULL_ID | var ) SEMICOLON
            {
                root_0 = (Object) adaptor.nil();

                PKG36 = (Token) match(input, PKG, FOLLOW_PKG_in_header_package889);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    PKG36_tree = (Object) adaptor.create(PKG36);
                    adaptor.addChild(root_0, PKG36_tree);
                }
                // io/protostuff/parser/ProtoParser.g:132:13: ( FULL_ID | var )
                int alt8 = 2;
                switch (input.LA(1))
                {
                    case FULL_ID:
                    {
                        alt8 = 1;
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
                        alt8 = 2;
                    }
                        break;
                    default:
                        if (state.backtracking > 0)
                        {
                            state.failed = true;
                            return retval;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 8, 0, input);

                        throw nvae;
                }

                switch (alt8)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:132:14: FULL_ID
                    {
                        FULL_ID37 = (Token) match(input, FULL_ID, FOLLOW_FULL_ID_in_header_package892);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            FULL_ID37_tree = (Object) adaptor.create(FULL_ID37);
                            adaptor.addChild(root_0, FULL_ID37_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            value = (FULL_ID37 != null ? FULL_ID37.getText() : null);
                        }

                    }
                        break;
                    case 2:
                    // io/protostuff/parser/ProtoParser.g:132:51: var
                    {
                        pushFollow(FOLLOW_var_in_header_package898);
                        var38 = var();

                        state._fsp--;
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                            adaptor.addChild(root_0, var38.getTree());
                        if (state.backtracking == 0)
                        {
                            value = (var38 != null ? input.toString(var38.start, var38.stop) : null);
                        }

                    }
                        break;

                }

                SEMICOLON39 = (Token) match(input, SEMICOLON, FOLLOW_SEMICOLON_in_header_package903);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {

                    if (proto.getPackageName() != null)
                        throw new IllegalStateException("Multiple package definitions.");

                    proto.setPackageName(value);

                    if (!proto.annotations.isEmpty())
                        throw new IllegalStateException("Misplaced annotations: " + proto.annotations);

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "header_package"

    public static class header_import_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "header_import"
    // io/protostuff/parser/ProtoParser.g:143:1: header_import[Proto proto] : IMPORT STRING_LITERAL
    // SEMICOLON ;
    public final ProtoParser.header_import_return header_import(Proto proto) throws RecognitionException
    {
        ProtoParser.header_import_return retval = new ProtoParser.header_import_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token IMPORT40 = null;
        Token STRING_LITERAL41 = null;
        Token SEMICOLON42 = null;

        Object IMPORT40_tree = null;
        Object STRING_LITERAL41_tree = null;
        Object SEMICOLON42_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:144:5: ( IMPORT STRING_LITERAL SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:144:9: IMPORT STRING_LITERAL SEMICOLON
            {
                root_0 = (Object) adaptor.nil();

                IMPORT40 = (Token) match(input, IMPORT, FOLLOW_IMPORT_in_header_import931);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    IMPORT40_tree = (Object) adaptor.create(IMPORT40);
                    adaptor.addChild(root_0, IMPORT40_tree);
                }
                STRING_LITERAL41 = (Token) match(input, STRING_LITERAL, FOLLOW_STRING_LITERAL_in_header_import933);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    STRING_LITERAL41_tree = (Object) adaptor.create(STRING_LITERAL41);
                    adaptor.addChild(root_0, STRING_LITERAL41_tree);
                }
                SEMICOLON42 = (Token) match(input, SEMICOLON, FOLLOW_SEMICOLON_in_header_import935);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {

                    proto.importProto(getStringFromStringLiteral((STRING_LITERAL41 != null ? STRING_LITERAL41.getText()
                            : null)));

                    if (!proto.annotations.isEmpty())
                        throw new IllegalStateException("Misplaced annotations: " + proto.annotations);

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "header_import"

    public static class option_entry_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "option_entry"
    // io/protostuff/parser/ProtoParser.g:152:1: option_entry[Proto proto, HasOptions ho] : OPTION (
    // LEFTPAREN )? k= var_full ( RIGHTPAREN )? ASSIGN (vr= var_reserved | id= ID | fid= FULL_ID | NUMFLOAT | NUMINT |
    // NUMDOUBLE | TRUE | FALSE | STRING_LITERAL ) SEMICOLON ;
    public final ProtoParser.option_entry_return option_entry(Proto proto, HasOptions ho) throws RecognitionException
    {
        ProtoParser.option_entry_return retval = new ProtoParser.option_entry_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token id = null;
        Token fid = null;
        Token OPTION43 = null;
        Token LEFTPAREN44 = null;
        Token RIGHTPAREN45 = null;
        Token ASSIGN46 = null;
        Token NUMFLOAT47 = null;
        Token NUMINT48 = null;
        Token NUMDOUBLE49 = null;
        Token TRUE50 = null;
        Token FALSE51 = null;
        Token STRING_LITERAL52 = null;
        Token SEMICOLON53 = null;
        ProtoParser.var_full_return k = null;

        ProtoParser.var_reserved_return vr = null;

        Object id_tree = null;
        Object fid_tree = null;
        Object OPTION43_tree = null;
        Object LEFTPAREN44_tree = null;
        Object RIGHTPAREN45_tree = null;
        Object ASSIGN46_tree = null;
        Object NUMFLOAT47_tree = null;
        Object NUMINT48_tree = null;
        Object NUMDOUBLE49_tree = null;
        Object TRUE50_tree = null;
        Object FALSE51_tree = null;
        Object STRING_LITERAL52_tree = null;
        Object SEMICOLON53_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:153:5: ( OPTION ( LEFTPAREN )? k= var_full ( RIGHTPAREN )?
            // ASSIGN (vr= var_reserved | id= ID | fid= FULL_ID | NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE |
            // STRING_LITERAL ) SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:153:9: OPTION ( LEFTPAREN )? k= var_full ( RIGHTPAREN )?
            // ASSIGN (vr= var_reserved | id= ID | fid= FULL_ID | NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE |
            // STRING_LITERAL ) SEMICOLON
            {
                root_0 = (Object) adaptor.nil();

                OPTION43 = (Token) match(input, OPTION, FOLLOW_OPTION_in_option_entry959);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    OPTION43_tree = (Object) adaptor.create(OPTION43);
                    adaptor.addChild(root_0, OPTION43_tree);
                }
                // io/protostuff/parser/ProtoParser.g:153:16: ( LEFTPAREN )?
                int alt9 = 2;
                switch (input.LA(1))
                {
                    case LEFTPAREN:
                    {
                        alt9 = 1;
                    }
                        break;
                }

                switch (alt9)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:153:16: LEFTPAREN
                    {
                        LEFTPAREN44 = (Token) match(input, LEFTPAREN, FOLLOW_LEFTPAREN_in_option_entry961);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            LEFTPAREN44_tree = (Object) adaptor.create(LEFTPAREN44);
                            adaptor.addChild(root_0, LEFTPAREN44_tree);
                        }

                    }
                        break;

                }

                pushFollow(FOLLOW_var_full_in_option_entry966);
                k = var_full();

                state._fsp--;
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                    adaptor.addChild(root_0, k.getTree());
                // io/protostuff/parser/ProtoParser.g:153:38: ( RIGHTPAREN )?
                int alt10 = 2;
                switch (input.LA(1))
                {
                    case RIGHTPAREN:
                    {
                        alt10 = 1;
                    }
                        break;
                }

                switch (alt10)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:153:38: RIGHTPAREN
                    {
                        RIGHTPAREN45 = (Token) match(input, RIGHTPAREN, FOLLOW_RIGHTPAREN_in_option_entry968);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            RIGHTPAREN45_tree = (Object) adaptor.create(RIGHTPAREN45);
                            adaptor.addChild(root_0, RIGHTPAREN45_tree);
                        }

                    }
                        break;

                }

                ASSIGN46 = (Token) match(input, ASSIGN, FOLLOW_ASSIGN_in_option_entry971);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    ASSIGN46_tree = (Object) adaptor.create(ASSIGN46);
                    adaptor.addChild(root_0, ASSIGN46_tree);
                }
                // io/protostuff/parser/ProtoParser.g:153:57: (vr= var_reserved | id= ID | fid= FULL_ID |
                // NUMFLOAT | NUMINT | NUMDOUBLE | TRUE | FALSE | STRING_LITERAL )
                int alt11 = 9;
                switch (input.LA(1))
                {
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
                        alt11 = 1;
                    }
                        break;
                    case ID:
                    {
                        alt11 = 2;
                    }
                        break;
                    case FULL_ID:
                    {
                        alt11 = 3;
                    }
                        break;
                    case NUMFLOAT:
                    {
                        alt11 = 4;
                    }
                        break;
                    case NUMINT:
                    {
                        alt11 = 5;
                    }
                        break;
                    case NUMDOUBLE:
                    {
                        alt11 = 6;
                    }
                        break;
                    case TRUE:
                    {
                        alt11 = 7;
                    }
                        break;
                    case FALSE:
                    {
                        alt11 = 8;
                    }
                        break;
                    case STRING_LITERAL:
                    {
                        alt11 = 9;
                    }
                        break;
                    default:
                        if (state.backtracking > 0)
                        {
                            state.failed = true;
                            return retval;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 11, 0, input);

                        throw nvae;
                }

                switch (alt11)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:154:17: vr= var_reserved
                    {
                        pushFollow(FOLLOW_var_reserved_in_option_entry993);
                        vr = var_reserved();

                        state._fsp--;
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                            adaptor.addChild(root_0, vr.getTree());
                        if (state.backtracking == 0)
                        {
                            ho.putExtraOption((k != null ? input.toString(k.start, k.stop) : null),
                                    (vr != null ? input.toString(vr.start, vr.stop) : null));
                        }

                    }
                        break;
                    case 2:
                    // io/protostuff/parser/ProtoParser.g:155:17: id= ID
                    {
                        id = (Token) match(input, ID, FOLLOW_ID_in_option_entry1015);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            id_tree = (Object) adaptor.create(id);
                            adaptor.addChild(root_0, id_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            ho.putStandardOption((k != null ? input.toString(k.start, k.stop) : null),
                                    (id != null ? id.getText() : null));
                        }

                    }
                        break;
                    case 3:
                    // io/protostuff/parser/ProtoParser.g:156:17: fid= FULL_ID
                    {
                        fid = (Token) match(input, FULL_ID, FOLLOW_FULL_ID_in_option_entry1037);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            fid_tree = (Object) adaptor.create(fid);
                            adaptor.addChild(root_0, fid_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            ho.putStandardOption((k != null ? input.toString(k.start, k.stop) : null),
                                    (fid != null ? fid.getText() : null));
                        }

                    }
                        break;
                    case 4:
                    // io/protostuff/parser/ProtoParser.g:157:17: NUMFLOAT
                    {
                        NUMFLOAT47 = (Token) match(input, NUMFLOAT, FOLLOW_NUMFLOAT_in_option_entry1057);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            NUMFLOAT47_tree = (Object) adaptor.create(NUMFLOAT47);
                            adaptor.addChild(root_0, NUMFLOAT47_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            ho.putExtraOption((k != null ? input.toString(k.start, k.stop) : null),
                                    Float.valueOf((NUMFLOAT47 != null ? NUMFLOAT47.getText() : null)));
                        }

                    }
                        break;
                    case 5:
                    // io/protostuff/parser/ProtoParser.g:158:17: NUMINT
                    {
                        NUMINT48 = (Token) match(input, NUMINT, FOLLOW_NUMINT_in_option_entry1077);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            NUMINT48_tree = (Object) adaptor.create(NUMINT48);
                            adaptor.addChild(root_0, NUMINT48_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            ho.putExtraOption((k != null ? input.toString(k.start, k.stop) : null),
                                    Integer.valueOf((NUMINT48 != null ? NUMINT48.getText() : null)));
                        }

                    }
                        break;
                    case 6:
                    // io/protostuff/parser/ProtoParser.g:159:17: NUMDOUBLE
                    {
                        NUMDOUBLE49 = (Token) match(input, NUMDOUBLE, FOLLOW_NUMDOUBLE_in_option_entry1097);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            NUMDOUBLE49_tree = (Object) adaptor.create(NUMDOUBLE49);
                            adaptor.addChild(root_0, NUMDOUBLE49_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            ho.putExtraOption((k != null ? input.toString(k.start, k.stop) : null),
                                    Double.valueOf((NUMDOUBLE49 != null ? NUMDOUBLE49.getText() : null)));
                        }

                    }
                        break;
                    case 7:
                    // io/protostuff/parser/ProtoParser.g:160:17: TRUE
                    {
                        TRUE50 = (Token) match(input, TRUE, FOLLOW_TRUE_in_option_entry1117);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            TRUE50_tree = (Object) adaptor.create(TRUE50);
                            adaptor.addChild(root_0, TRUE50_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            ho.putExtraOption((k != null ? input.toString(k.start, k.stop) : null), Boolean.TRUE);
                        }

                    }
                        break;
                    case 8:
                    // io/protostuff/parser/ProtoParser.g:161:17: FALSE
                    {
                        FALSE51 = (Token) match(input, FALSE, FOLLOW_FALSE_in_option_entry1137);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            FALSE51_tree = (Object) adaptor.create(FALSE51);
                            adaptor.addChild(root_0, FALSE51_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            ho.putExtraOption((k != null ? input.toString(k.start, k.stop) : null), Boolean.FALSE);
                        }

                    }
                        break;
                    case 9:
                    // io/protostuff/parser/ProtoParser.g:162:17: STRING_LITERAL
                    {
                        STRING_LITERAL52 = (Token) match(input, STRING_LITERAL,
                                FOLLOW_STRING_LITERAL_in_option_entry1157);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            STRING_LITERAL52_tree = (Object) adaptor.create(STRING_LITERAL52);
                            adaptor.addChild(root_0, STRING_LITERAL52_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            ho.putExtraOption((k != null ? input.toString(k.start, k.stop) : null),
                                    getStringFromStringLiteral((STRING_LITERAL52 != null ? STRING_LITERAL52.getText()
                                            : null)));
                        }

                    }
                        break;

                }

                SEMICOLON53 = (Token) match(input, SEMICOLON, FOLLOW_SEMICOLON_in_option_entry1171);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {

                    if (!proto.annotations.isEmpty())
                        throw new IllegalStateException("Misplaced annotations: " + proto.annotations);

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "option_entry"

    public static class message_block_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "message_block"
    // io/protostuff/parser/ProtoParser.g:169:1: message_block[Proto proto, Message parent] : MESSAGE ID
    // LEFTCURLY ( message_body[proto, message] )* RIGHTCURLY ;
    public final ProtoParser.message_block_return message_block(Proto proto, Message parent)
            throws RecognitionException
    {
        ProtoParser.message_block_return retval = new ProtoParser.message_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token MESSAGE54 = null;
        Token ID55 = null;
        Token LEFTCURLY56 = null;
        Token RIGHTCURLY58 = null;
        ProtoParser.message_body_return message_body57 = null;

        Object MESSAGE54_tree = null;
        Object ID55_tree = null;
        Object LEFTCURLY56_tree = null;
        Object RIGHTCURLY58_tree = null;

        Message message = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:173:5: ( MESSAGE ID LEFTCURLY ( message_body[proto,
            // message] )* RIGHTCURLY )
            // io/protostuff/parser/ProtoParser.g:173:9: MESSAGE ID LEFTCURLY ( message_body[proto, message]
            // )* RIGHTCURLY
            {
                root_0 = (Object) adaptor.nil();

                MESSAGE54 = (Token) match(input, MESSAGE, FOLLOW_MESSAGE_in_message_block1204);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    MESSAGE54_tree = (Object) adaptor.create(MESSAGE54);
                    adaptor.addChild(root_0, MESSAGE54_tree);
                }
                ID55 = (Token) match(input, ID, FOLLOW_ID_in_message_block1206);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    ID55_tree = (Object) adaptor.create(ID55);
                    adaptor.addChild(root_0, ID55_tree);
                }
                if (state.backtracking == 0)
                {

                    message = new Message((ID55 != null ? ID55.getText() : null), parent, proto);
                    proto.addAnnotationsTo(message);

                }
                LEFTCURLY56 = (Token) match(input, LEFTCURLY, FOLLOW_LEFTCURLY_in_message_block1219);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    LEFTCURLY56_tree = (Object) adaptor.create(LEFTCURLY56);
                    adaptor.addChild(root_0, LEFTCURLY56_tree);
                }
                // io/protostuff/parser/ProtoParser.g:177:19: ( message_body[proto, message] )*
                loop12: do
                {
                    int alt12 = 2;
                    switch (input.LA(1))
                    {
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
                        {
                            alt12 = 1;
                        }
                            break;

                    }

                    switch (alt12)
                    {
                        case 1:
                        // io/protostuff/parser/ProtoParser.g:177:20: message_body[proto, message]
                        {
                            pushFollow(FOLLOW_message_body_in_message_block1222);
                            message_body57 = message_body(proto, message);

                            state._fsp--;
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                                adaptor.addChild(root_0, message_body57.getTree());

                        }
                            break;

                        default:
                            break loop12;
                    }
                } while (true);

                RIGHTCURLY58 = (Token) match(input, RIGHTCURLY, FOLLOW_RIGHTCURLY_in_message_block1227);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    RIGHTCURLY58_tree = (Object) adaptor.create(RIGHTCURLY58);
                    adaptor.addChild(root_0, RIGHTCURLY58_tree);
                }
                if (state.backtracking == 0)
                {

                    if (!proto.annotations.isEmpty())
                        throw new IllegalStateException("Misplaced annotations: " + proto.annotations);

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "message_block"

    public static class message_body_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "message_body"
    // io/protostuff/parser/ProtoParser.g:183:1: message_body[Proto proto, Message message] : (
    // message_block[proto, message] | message_field[proto, message] | enum_block[proto, message] | service_block[proto,
    // message] | extend_block[proto, message] | extensions_range[proto, message] | annotation_entry[proto] |
    // option_entry[proto, message] );
    public final ProtoParser.message_body_return message_body(Proto proto, Message message) throws RecognitionException
    {
        ProtoParser.message_body_return retval = new ProtoParser.message_body_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ProtoParser.message_block_return message_block59 = null;

        ProtoParser.message_field_return message_field60 = null;

        ProtoParser.enum_block_return enum_block61 = null;

        ProtoParser.service_block_return service_block62 = null;

        ProtoParser.extend_block_return extend_block63 = null;

        ProtoParser.extensions_range_return extensions_range64 = null;

        ProtoParser.annotation_entry_return annotation_entry65 = null;

        ProtoParser.option_entry_return option_entry66 = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:184:5: ( message_block[proto, message] |
            // message_field[proto, message] | enum_block[proto, message] | service_block[proto, message] |
            // extend_block[proto, message] | extensions_range[proto, message] | annotation_entry[proto] |
            // option_entry[proto, message] )
            int alt13 = 8;
            switch (input.LA(1))
            {
                case MESSAGE:
                {
                    alt13 = 1;
                }
                    break;
                case REQUIRED:
                case OPTIONAL:
                case REPEATED:
                {
                    alt13 = 2;
                }
                    break;
                case ENUM:
                {
                    alt13 = 3;
                }
                    break;
                case SERVICE:
                {
                    alt13 = 4;
                }
                    break;
                case EXTEND:
                {
                    alt13 = 5;
                }
                    break;
                case EXTENSIONS:
                {
                    alt13 = 6;
                }
                    break;
                case AT:
                {
                    alt13 = 7;
                }
                    break;
                case OPTION:
                {
                    alt13 = 8;
                }
                    break;
                default:
                    if (state.backtracking > 0)
                    {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 13, 0, input);

                    throw nvae;
            }

            switch (alt13)
            {
                case 1:
                // io/protostuff/parser/ProtoParser.g:184:9: message_block[proto, message]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_message_block_in_message_body1250);
                    message_block59 = message_block(proto, message);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, message_block59.getTree());

                }
                    break;
                case 2:
                // io/protostuff/parser/ProtoParser.g:185:9: message_field[proto, message]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_message_field_in_message_body1261);
                    message_field60 = message_field(proto, message);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, message_field60.getTree());

                }
                    break;
                case 3:
                // io/protostuff/parser/ProtoParser.g:186:9: enum_block[proto, message]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_enum_block_in_message_body1272);
                    enum_block61 = enum_block(proto, message);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, enum_block61.getTree());

                }
                    break;
                case 4:
                // io/protostuff/parser/ProtoParser.g:187:9: service_block[proto, message]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_service_block_in_message_body1283);
                    service_block62 = service_block(proto, message);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, service_block62.getTree());

                }
                    break;
                case 5:
                // io/protostuff/parser/ProtoParser.g:188:9: extend_block[proto, message]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_extend_block_in_message_body1294);
                    extend_block63 = extend_block(proto, message);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, extend_block63.getTree());

                }
                    break;
                case 6:
                // io/protostuff/parser/ProtoParser.g:189:9: extensions_range[proto, message]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_extensions_range_in_message_body1305);
                    extensions_range64 = extensions_range(proto, message);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, extensions_range64.getTree());

                }
                    break;
                case 7:
                // io/protostuff/parser/ProtoParser.g:190:9: annotation_entry[proto]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_annotation_entry_in_message_body1316);
                    annotation_entry65 = annotation_entry(proto);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, annotation_entry65.getTree());

                }
                    break;
                case 8:
                // io/protostuff/parser/ProtoParser.g:191:9: option_entry[proto, message]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_option_entry_in_message_body1327);
                    option_entry66 = option_entry(proto, message);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, option_entry66.getTree());

                }
                    break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "message_body"

    public static class extensions_range_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "extensions_range"
    // io/protostuff/parser/ProtoParser.g:194:1: extensions_range[Proto proto, Message message] : EXTENSIONS
    // f= NUMINT ( TO (l= NUMINT | MAX ) )? SEMICOLON ;
    public final ProtoParser.extensions_range_return extensions_range(Proto proto, Message message)
            throws RecognitionException
    {
        ProtoParser.extensions_range_return retval = new ProtoParser.extensions_range_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token f = null;
        Token l = null;
        Token EXTENSIONS67 = null;
        Token TO68 = null;
        Token MAX69 = null;
        Token SEMICOLON70 = null;

        Object f_tree = null;
        Object l_tree = null;
        Object EXTENSIONS67_tree = null;
        Object TO68_tree = null;
        Object MAX69_tree = null;
        Object SEMICOLON70_tree = null;

        int first = -1;
        int last = -1;

        try
        {
            // io/protostuff/parser/ProtoParser.g:199:5: ( EXTENSIONS f= NUMINT ( TO (l= NUMINT | MAX ) )?
            // SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:199:9: EXTENSIONS f= NUMINT ( TO (l= NUMINT | MAX ) )?
            // SEMICOLON
            {
                root_0 = (Object) adaptor.nil();

                EXTENSIONS67 = (Token) match(input, EXTENSIONS, FOLLOW_EXTENSIONS_in_extensions_range1358);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    EXTENSIONS67_tree = (Object) adaptor.create(EXTENSIONS67);
                    adaptor.addChild(root_0, EXTENSIONS67_tree);
                }
                f = (Token) match(input, NUMINT, FOLLOW_NUMINT_in_extensions_range1362);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    f_tree = (Object) adaptor.create(f);
                    adaptor.addChild(root_0, f_tree);
                }
                if (state.backtracking == 0)
                {
                    first = Integer.parseInt((f != null ? f.getText() : null));
                    last = first;
                }
                // io/protostuff/parser/ProtoParser.g:200:9: ( TO (l= NUMINT | MAX ) )?
                int alt15 = 2;
                switch (input.LA(1))
                {
                    case TO:
                    {
                        alt15 = 1;
                    }
                        break;
                }

                switch (alt15)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:200:11: TO (l= NUMINT | MAX )
                    {
                        TO68 = (Token) match(input, TO, FOLLOW_TO_in_extensions_range1376);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            TO68_tree = (Object) adaptor.create(TO68);
                            adaptor.addChild(root_0, TO68_tree);
                        }
                        // io/protostuff/parser/ProtoParser.g:200:14: (l= NUMINT | MAX )
                        int alt14 = 2;
                        switch (input.LA(1))
                        {
                            case NUMINT:
                            {
                                alt14 = 1;
                            }
                                break;
                            case MAX:
                            {
                                alt14 = 2;
                            }
                                break;
                            default:
                                if (state.backtracking > 0)
                                {
                                    state.failed = true;
                                    return retval;
                                }
                                NoViableAltException nvae =
                                        new NoViableAltException("", 14, 0, input);

                                throw nvae;
                        }

                        switch (alt14)
                        {
                            case 1:
                            // io/protostuff/parser/ProtoParser.g:200:16: l= NUMINT
                            {
                                l = (Token) match(input, NUMINT, FOLLOW_NUMINT_in_extensions_range1382);
                                if (state.failed)
                                    return retval;
                                if (state.backtracking == 0)
                                {
                                    l_tree = (Object) adaptor.create(l);
                                    adaptor.addChild(root_0, l_tree);
                                }
                                if (state.backtracking == 0)
                                {
                                    last = Integer.parseInt((l != null ? l.getText() : null));
                                }

                            }
                                break;
                            case 2:
                            // io/protostuff/parser/ProtoParser.g:200:65: MAX
                            {
                                MAX69 = (Token) match(input, MAX, FOLLOW_MAX_in_extensions_range1388);
                                if (state.failed)
                                    return retval;
                                if (state.backtracking == 0)
                                {
                                    MAX69_tree = (Object) adaptor.create(MAX69);
                                    adaptor.addChild(root_0, MAX69_tree);
                                }
                                if (state.backtracking == 0)
                                {
                                    last = 536870911;
                                }

                            }
                                break;

                        }

                    }
                        break;

                }

                SEMICOLON70 = (Token) match(input, SEMICOLON, FOLLOW_SEMICOLON_in_extensions_range1405);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {

                    message.defineExtensionRange(first, last);

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "extensions_range"

    public static class message_field_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "message_field"
    // io/protostuff/parser/ProtoParser.g:206:1: message_field[Proto proto, HasFields message] : ( OPTIONAL
    // | REQUIRED | REPEATED ) field_type[proto, message, fieldHolder] var ASSIGN NUMINT ( field_options[proto, message,
    // fieldHolder.field] )? ( SEMICOLON | ignore_block ) ;
    public final ProtoParser.message_field_return message_field(Proto proto, HasFields message)
            throws RecognitionException
    {
        ProtoParser.message_field_return retval = new ProtoParser.message_field_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token OPTIONAL71 = null;
        Token REQUIRED72 = null;
        Token REPEATED73 = null;
        Token ASSIGN76 = null;
        Token NUMINT77 = null;
        Token SEMICOLON79 = null;
        ProtoParser.field_type_return field_type74 = null;

        ProtoParser.var_return var75 = null;

        ProtoParser.field_options_return field_options78 = null;

        ProtoParser.ignore_block_return ignore_block80 = null;

        Object OPTIONAL71_tree = null;
        Object REQUIRED72_tree = null;
        Object REPEATED73_tree = null;
        Object ASSIGN76_tree = null;
        Object NUMINT77_tree = null;
        Object SEMICOLON79_tree = null;

        Field.Modifier modifier = null;
        FieldHolder fieldHolder = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:211:5: ( ( OPTIONAL | REQUIRED | REPEATED )
            // field_type[proto, message, fieldHolder] var ASSIGN NUMINT ( field_options[proto, message,
            // fieldHolder.field] )? ( SEMICOLON | ignore_block ) )
            // io/protostuff/parser/ProtoParser.g:211:9: ( OPTIONAL | REQUIRED | REPEATED )
            // field_type[proto, message, fieldHolder] var ASSIGN NUMINT ( field_options[proto, message,
            // fieldHolder.field] )? ( SEMICOLON | ignore_block )
            {
                root_0 = (Object) adaptor.nil();

                // io/protostuff/parser/ProtoParser.g:211:9: ( OPTIONAL | REQUIRED | REPEATED )
                int alt16 = 3;
                switch (input.LA(1))
                {
                    case OPTIONAL:
                    {
                        alt16 = 1;
                    }
                        break;
                    case REQUIRED:
                    {
                        alt16 = 2;
                    }
                        break;
                    case REPEATED:
                    {
                        alt16 = 3;
                    }
                        break;
                    default:
                        if (state.backtracking > 0)
                        {
                            state.failed = true;
                            return retval;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 16, 0, input);

                        throw nvae;
                }

                switch (alt16)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:211:10: OPTIONAL
                    {
                        OPTIONAL71 = (Token) match(input, OPTIONAL, FOLLOW_OPTIONAL_in_message_field1439);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            OPTIONAL71_tree = (Object) adaptor.create(OPTIONAL71);
                            adaptor.addChild(root_0, OPTIONAL71_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            modifier = Field.Modifier.OPTIONAL;
                        }

                    }
                        break;
                    case 2:
                    // io/protostuff/parser/ProtoParser.g:212:13: REQUIRED
                    {
                        REQUIRED72 = (Token) match(input, REQUIRED, FOLLOW_REQUIRED_in_message_field1456);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            REQUIRED72_tree = (Object) adaptor.create(REQUIRED72);
                            adaptor.addChild(root_0, REQUIRED72_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            modifier = Field.Modifier.REQUIRED;
                        }

                    }
                        break;
                    case 3:
                    // io/protostuff/parser/ProtoParser.g:213:13: REPEATED
                    {
                        REPEATED73 = (Token) match(input, REPEATED, FOLLOW_REPEATED_in_message_field1473);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            REPEATED73_tree = (Object) adaptor.create(REPEATED73);
                            adaptor.addChild(root_0, REPEATED73_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            modifier = Field.Modifier.REPEATED;
                        }

                    }
                        break;

                }

                if (state.backtracking == 0)
                {

                    fieldHolder = new FieldHolder();

                }
                pushFollow(FOLLOW_field_type_in_message_field1488);
                field_type74 = field_type(proto, message, fieldHolder);

                state._fsp--;
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                    adaptor.addChild(root_0, field_type74.getTree());
                pushFollow(FOLLOW_var_in_message_field1500);
                var75 = var();

                state._fsp--;
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                    adaptor.addChild(root_0, var75.getTree());
                ASSIGN76 = (Token) match(input, ASSIGN, FOLLOW_ASSIGN_in_message_field1502);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    ASSIGN76_tree = (Object) adaptor.create(ASSIGN76);
                    adaptor.addChild(root_0, ASSIGN76_tree);
                }
                NUMINT77 = (Token) match(input, NUMINT, FOLLOW_NUMINT_in_message_field1504);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    NUMINT77_tree = (Object) adaptor.create(NUMINT77);
                    adaptor.addChild(root_0, NUMINT77_tree);
                }
                if (state.backtracking == 0)
                {

                    if (fieldHolder.field != null)
                    {
                        fieldHolder.field.modifier = modifier;
                        fieldHolder.field.name = (var75 != null ? input.toString(var75.start, var75.stop) : null);
                        fieldHolder.field.number = Integer.parseInt((NUMINT77 != null ? NUMINT77.getText() : null));
                    }

                }
                // io/protostuff/parser/ProtoParser.g:224:9: ( field_options[proto, message,
                // fieldHolder.field] )?
                int alt17 = 2;
                switch (input.LA(1))
                {
                    case LEFTSQUARE:
                    {
                        alt17 = 1;
                    }
                        break;
                }

                switch (alt17)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:224:10: field_options[proto, message,
                    // fieldHolder.field]
                    {
                        pushFollow(FOLLOW_field_options_in_message_field1518);
                        field_options78 = field_options(proto, message, fieldHolder.field);

                        state._fsp--;
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                            adaptor.addChild(root_0, field_options78.getTree());

                    }
                        break;

                }

                if (state.backtracking == 0)
                {

                    if (fieldHolder.field != null)
                    {
                        proto.addAnnotationsTo(fieldHolder.field, message.getEnclosingNamespace());
                        message.addField(fieldHolder.field);
                    }

                }
                // io/protostuff/parser/ProtoParser.g:230:9: ( SEMICOLON | ignore_block )
                int alt18 = 2;
                switch (input.LA(1))
                {
                    case SEMICOLON:
                    {
                        alt18 = 1;
                    }
                        break;
                    case LEFTCURLY:
                    {
                        alt18 = 2;
                    }
                        break;
                    default:
                        if (state.backtracking > 0)
                        {
                            state.failed = true;
                            return retval;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 18, 0, input);

                        throw nvae;
                }

                switch (alt18)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:230:10: SEMICOLON
                    {
                        SEMICOLON79 = (Token) match(input, SEMICOLON, FOLLOW_SEMICOLON_in_message_field1534);
                        if (state.failed)
                            return retval;

                    }
                        break;
                    case 2:
                    // io/protostuff/parser/ProtoParser.g:230:23: ignore_block
                    {
                        pushFollow(FOLLOW_ignore_block_in_message_field1539);
                        ignore_block80 = ignore_block();

                        state._fsp--;
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                            adaptor.addChild(root_0, ignore_block80.getTree());

                    }
                        break;

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "message_field"

    public static class field_type_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "field_type"
    // io/protostuff/parser/ProtoParser.g:233:1: field_type[Proto proto, HasFields message, FieldHolder
    // fieldHolder] : ( INT32 | UINT32 | SINT32 | FIXED32 | SFIXED32 | INT64 | UINT64 | SINT64 | FIXED64 | SFIXED64 |
    // FLOAT | DOUBLE | BOOL | STRING | BYTES | GROUP | FULL_ID | ID );
    public final ProtoParser.field_type_return field_type(Proto proto, HasFields message, FieldHolder fieldHolder)
            throws RecognitionException
    {
        ProtoParser.field_type_return retval = new ProtoParser.field_type_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token INT3281 = null;
        Token UINT3282 = null;
        Token SINT3283 = null;
        Token FIXED3284 = null;
        Token SFIXED3285 = null;
        Token INT6486 = null;
        Token UINT6487 = null;
        Token SINT6488 = null;
        Token FIXED6489 = null;
        Token SFIXED6490 = null;
        Token FLOAT91 = null;
        Token DOUBLE92 = null;
        Token BOOL93 = null;
        Token STRING94 = null;
        Token BYTES95 = null;
        Token GROUP96 = null;
        Token FULL_ID97 = null;
        Token ID98 = null;

        Object INT3281_tree = null;
        Object UINT3282_tree = null;
        Object SINT3283_tree = null;
        Object FIXED3284_tree = null;
        Object SFIXED3285_tree = null;
        Object INT6486_tree = null;
        Object UINT6487_tree = null;
        Object SINT6488_tree = null;
        Object FIXED6489_tree = null;
        Object SFIXED6490_tree = null;
        Object FLOAT91_tree = null;
        Object DOUBLE92_tree = null;
        Object BOOL93_tree = null;
        Object STRING94_tree = null;
        Object BYTES95_tree = null;
        Object GROUP96_tree = null;
        Object FULL_ID97_tree = null;
        Object ID98_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:234:5: ( INT32 | UINT32 | SINT32 | FIXED32 | SFIXED32 |
            // INT64 | UINT64 | SINT64 | FIXED64 | SFIXED64 | FLOAT | DOUBLE | BOOL | STRING | BYTES | GROUP | FULL_ID |
            // ID )
            int alt19 = 18;
            switch (input.LA(1))
            {
                case INT32:
                {
                    alt19 = 1;
                }
                    break;
                case UINT32:
                {
                    alt19 = 2;
                }
                    break;
                case SINT32:
                {
                    alt19 = 3;
                }
                    break;
                case FIXED32:
                {
                    alt19 = 4;
                }
                    break;
                case SFIXED32:
                {
                    alt19 = 5;
                }
                    break;
                case INT64:
                {
                    alt19 = 6;
                }
                    break;
                case UINT64:
                {
                    alt19 = 7;
                }
                    break;
                case SINT64:
                {
                    alt19 = 8;
                }
                    break;
                case FIXED64:
                {
                    alt19 = 9;
                }
                    break;
                case SFIXED64:
                {
                    alt19 = 10;
                }
                    break;
                case FLOAT:
                {
                    alt19 = 11;
                }
                    break;
                case DOUBLE:
                {
                    alt19 = 12;
                }
                    break;
                case BOOL:
                {
                    alt19 = 13;
                }
                    break;
                case STRING:
                {
                    alt19 = 14;
                }
                    break;
                case BYTES:
                {
                    alt19 = 15;
                }
                    break;
                case GROUP:
                {
                    alt19 = 16;
                }
                    break;
                case FULL_ID:
                {
                    alt19 = 17;
                }
                    break;
                case ID:
                {
                    alt19 = 18;
                }
                    break;
                default:
                    if (state.backtracking > 0)
                    {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 19, 0, input);

                    throw nvae;
            }

            switch (alt19)
            {
                case 1:
                // io/protostuff/parser/ProtoParser.g:234:9: INT32
                {
                    root_0 = (Object) adaptor.nil();

                    INT3281 = (Token) match(input, INT32, FOLLOW_INT32_in_field_type1565);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        INT3281_tree = (Object) adaptor.create(INT3281);
                        adaptor.addChild(root_0, INT3281_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.Int32());
                    }

                }
                    break;
                case 2:
                // io/protostuff/parser/ProtoParser.g:235:9: UINT32
                {
                    root_0 = (Object) adaptor.nil();

                    UINT3282 = (Token) match(input, UINT32, FOLLOW_UINT32_in_field_type1577);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        UINT3282_tree = (Object) adaptor.create(UINT3282);
                        adaptor.addChild(root_0, UINT3282_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.UInt32());
                    }

                }
                    break;
                case 3:
                // io/protostuff/parser/ProtoParser.g:236:9: SINT32
                {
                    root_0 = (Object) adaptor.nil();

                    SINT3283 = (Token) match(input, SINT32, FOLLOW_SINT32_in_field_type1589);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        SINT3283_tree = (Object) adaptor.create(SINT3283);
                        adaptor.addChild(root_0, SINT3283_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.SInt32());
                    }

                }
                    break;
                case 4:
                // io/protostuff/parser/ProtoParser.g:237:9: FIXED32
                {
                    root_0 = (Object) adaptor.nil();

                    FIXED3284 = (Token) match(input, FIXED32, FOLLOW_FIXED32_in_field_type1601);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        FIXED3284_tree = (Object) adaptor.create(FIXED3284);
                        adaptor.addChild(root_0, FIXED3284_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.Fixed32());
                    }

                }
                    break;
                case 5:
                // io/protostuff/parser/ProtoParser.g:238:9: SFIXED32
                {
                    root_0 = (Object) adaptor.nil();

                    SFIXED3285 = (Token) match(input, SFIXED32, FOLLOW_SFIXED32_in_field_type1613);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        SFIXED3285_tree = (Object) adaptor.create(SFIXED3285);
                        adaptor.addChild(root_0, SFIXED3285_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.SFixed32());
                    }

                }
                    break;
                case 6:
                // io/protostuff/parser/ProtoParser.g:239:9: INT64
                {
                    root_0 = (Object) adaptor.nil();

                    INT6486 = (Token) match(input, INT64, FOLLOW_INT64_in_field_type1625);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        INT6486_tree = (Object) adaptor.create(INT6486);
                        adaptor.addChild(root_0, INT6486_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.Int64());
                    }

                }
                    break;
                case 7:
                // io/protostuff/parser/ProtoParser.g:240:9: UINT64
                {
                    root_0 = (Object) adaptor.nil();

                    UINT6487 = (Token) match(input, UINT64, FOLLOW_UINT64_in_field_type1637);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        UINT6487_tree = (Object) adaptor.create(UINT6487);
                        adaptor.addChild(root_0, UINT6487_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.UInt64());
                    }

                }
                    break;
                case 8:
                // io/protostuff/parser/ProtoParser.g:241:9: SINT64
                {
                    root_0 = (Object) adaptor.nil();

                    SINT6488 = (Token) match(input, SINT64, FOLLOW_SINT64_in_field_type1649);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        SINT6488_tree = (Object) adaptor.create(SINT6488);
                        adaptor.addChild(root_0, SINT6488_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.SInt64());
                    }

                }
                    break;
                case 9:
                // io/protostuff/parser/ProtoParser.g:242:9: FIXED64
                {
                    root_0 = (Object) adaptor.nil();

                    FIXED6489 = (Token) match(input, FIXED64, FOLLOW_FIXED64_in_field_type1661);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        FIXED6489_tree = (Object) adaptor.create(FIXED6489);
                        adaptor.addChild(root_0, FIXED6489_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.Fixed64());
                    }

                }
                    break;
                case 10:
                // io/protostuff/parser/ProtoParser.g:243:9: SFIXED64
                {
                    root_0 = (Object) adaptor.nil();

                    SFIXED6490 = (Token) match(input, SFIXED64, FOLLOW_SFIXED64_in_field_type1673);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        SFIXED6490_tree = (Object) adaptor.create(SFIXED6490);
                        adaptor.addChild(root_0, SFIXED6490_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.SFixed64());
                    }

                }
                    break;
                case 11:
                // io/protostuff/parser/ProtoParser.g:244:9: FLOAT
                {
                    root_0 = (Object) adaptor.nil();

                    FLOAT91 = (Token) match(input, FLOAT, FOLLOW_FLOAT_in_field_type1685);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        FLOAT91_tree = (Object) adaptor.create(FLOAT91);
                        adaptor.addChild(root_0, FLOAT91_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.Float());
                    }

                }
                    break;
                case 12:
                // io/protostuff/parser/ProtoParser.g:245:9: DOUBLE
                {
                    root_0 = (Object) adaptor.nil();

                    DOUBLE92 = (Token) match(input, DOUBLE, FOLLOW_DOUBLE_in_field_type1697);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        DOUBLE92_tree = (Object) adaptor.create(DOUBLE92);
                        adaptor.addChild(root_0, DOUBLE92_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.Double());
                    }

                }
                    break;
                case 13:
                // io/protostuff/parser/ProtoParser.g:246:9: BOOL
                {
                    root_0 = (Object) adaptor.nil();

                    BOOL93 = (Token) match(input, BOOL, FOLLOW_BOOL_in_field_type1709);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        BOOL93_tree = (Object) adaptor.create(BOOL93);
                        adaptor.addChild(root_0, BOOL93_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.Bool());
                    }

                }
                    break;
                case 14:
                // io/protostuff/parser/ProtoParser.g:247:9: STRING
                {
                    root_0 = (Object) adaptor.nil();

                    STRING94 = (Token) match(input, STRING, FOLLOW_STRING_in_field_type1721);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        STRING94_tree = (Object) adaptor.create(STRING94);
                        adaptor.addChild(root_0, STRING94_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.String());
                    }

                }
                    break;
                case 15:
                // io/protostuff/parser/ProtoParser.g:248:9: BYTES
                {
                    root_0 = (Object) adaptor.nil();

                    BYTES95 = (Token) match(input, BYTES, FOLLOW_BYTES_in_field_type1733);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        BYTES95_tree = (Object) adaptor.create(BYTES95);
                        adaptor.addChild(root_0, BYTES95_tree);
                    }
                    if (state.backtracking == 0)
                    {
                        fieldHolder.setField(new Field.Bytes());
                    }

                }
                    break;
                case 16:
                // io/protostuff/parser/ProtoParser.g:249:9: GROUP
                {
                    root_0 = (Object) adaptor.nil();

                    GROUP96 = (Token) match(input, GROUP, FOLLOW_GROUP_in_field_type1745);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        GROUP96_tree = (Object) adaptor.create(GROUP96);
                        adaptor.addChild(root_0, GROUP96_tree);
                    }
                    if (state.backtracking == 0)
                    {

                        String suffix = proto.getFile() == null ? "" : " of " + proto.getFile().getName();
                        warn("'group' not supported @ line " + (GROUP96 != null ? GROUP96.getLine() : 0) + suffix);

                    }

                }
                    break;
                case 17:
                // io/protostuff/parser/ProtoParser.g:253:9: FULL_ID
                {
                    root_0 = (Object) adaptor.nil();

                    FULL_ID97 = (Token) match(input, FULL_ID, FOLLOW_FULL_ID_in_field_type1757);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        FULL_ID97_tree = (Object) adaptor.create(FULL_ID97);
                        adaptor.addChild(root_0, FULL_ID97_tree);
                    }
                    if (state.backtracking == 0)
                    {

                        String fullType = (FULL_ID97 != null ? FULL_ID97.getText() : null);
                        int lastDot = fullType.lastIndexOf('.');
                        String packageName = fullType.substring(0, lastDot);
                        String type = fullType.substring(lastDot + 1);
                        fieldHolder.setField(new Field.Reference(packageName, type, message));

                    }

                }
                    break;
                case 18:
                // io/protostuff/parser/ProtoParser.g:260:9: ID
                {
                    root_0 = (Object) adaptor.nil();

                    ID98 = (Token) match(input, ID, FOLLOW_ID_in_field_type1769);
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                    {
                        ID98_tree = (Object) adaptor.create(ID98);
                        adaptor.addChild(root_0, ID98_tree);
                    }
                    if (state.backtracking == 0)
                    {

                        String type = (ID98 != null ? ID98.getText() : null);
                        fieldHolder.setField(new Field.Reference(null, type, message));

                    }

                }
                    break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "field_type"

    public static class field_options_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "field_options"
    // io/protostuff/parser/ProtoParser.g:266:1: field_options[Proto proto, HasFields message, Field field]
    // : LEFTSQUARE field_options_keyval[proto, message, field, true] ( COMMA field_options_keyval[proto, message,
    // field, true] )* RIGHTSQUARE ;
    public final ProtoParser.field_options_return field_options(Proto proto, HasFields message, Field field)
            throws RecognitionException
    {
        ProtoParser.field_options_return retval = new ProtoParser.field_options_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LEFTSQUARE99 = null;
        Token COMMA101 = null;
        Token RIGHTSQUARE103 = null;
        ProtoParser.field_options_keyval_return field_options_keyval100 = null;

        ProtoParser.field_options_keyval_return field_options_keyval102 = null;

        Object LEFTSQUARE99_tree = null;
        Object COMMA101_tree = null;
        Object RIGHTSQUARE103_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:267:5: ( LEFTSQUARE field_options_keyval[proto, message,
            // field, true] ( COMMA field_options_keyval[proto, message, field, true] )* RIGHTSQUARE )
            // io/protostuff/parser/ProtoParser.g:267:9: LEFTSQUARE field_options_keyval[proto, message,
            // field, true] ( COMMA field_options_keyval[proto, message, field, true] )* RIGHTSQUARE
            {
                root_0 = (Object) adaptor.nil();

                LEFTSQUARE99 = (Token) match(input, LEFTSQUARE, FOLLOW_LEFTSQUARE_in_field_options1796);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    LEFTSQUARE99_tree = (Object) adaptor.create(LEFTSQUARE99);
                    adaptor.addChild(root_0, LEFTSQUARE99_tree);
                }
                pushFollow(FOLLOW_field_options_keyval_in_field_options1798);
                field_options_keyval100 = field_options_keyval(proto, message, field, true);

                state._fsp--;
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                    adaptor.addChild(root_0, field_options_keyval100.getTree());
                // io/protostuff/parser/ProtoParser.g:268:9: ( COMMA field_options_keyval[proto, message,
                // field, true] )*
                loop20: do
                {
                    int alt20 = 2;
                    switch (input.LA(1))
                    {
                        case COMMA:
                        {
                            alt20 = 1;
                        }
                            break;

                    }

                    switch (alt20)
                    {
                        case 1:
                        // io/protostuff/parser/ProtoParser.g:268:10: COMMA field_options_keyval[proto,
                        // message, field, true]
                        {
                            COMMA101 = (Token) match(input, COMMA, FOLLOW_COMMA_in_field_options1811);
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                            {
                                COMMA101_tree = (Object) adaptor.create(COMMA101);
                                adaptor.addChild(root_0, COMMA101_tree);
                            }
                            pushFollow(FOLLOW_field_options_keyval_in_field_options1813);
                            field_options_keyval102 = field_options_keyval(proto, message, field, true);

                            state._fsp--;
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                                adaptor.addChild(root_0, field_options_keyval102.getTree());

                        }
                            break;

                        default:
                            break loop20;
                    }
                } while (true);

                RIGHTSQUARE103 = (Token) match(input, RIGHTSQUARE, FOLLOW_RIGHTSQUARE_in_field_options1818);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    RIGHTSQUARE103_tree = (Object) adaptor.create(RIGHTSQUARE103);
                    adaptor.addChild(root_0, RIGHTSQUARE103_tree);
                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "field_options"

    public static class field_options_keyval_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "field_options_keyval"
    // io/protostuff/parser/ProtoParser.g:271:1: field_options_keyval[Proto proto, HasFields message, Field
    // field, boolean checkDefault] : key= var_full ASSIGN (vr= var_reserved | STRING_LITERAL | NUMFLOAT | NUMINT |
    // NUMDOUBLE | HEX | OCTAL | TRUE | FALSE | val= ID | FULL_ID | EXP | signed_constant[proto, message, field,
    // $key.text, checkDefault] ) ;
    public final ProtoParser.field_options_keyval_return field_options_keyval(Proto proto, HasFields message,
            Field field, boolean checkDefault) throws RecognitionException
    {
        ProtoParser.field_options_keyval_return retval = new ProtoParser.field_options_keyval_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token val = null;
        Token ASSIGN104 = null;
        Token STRING_LITERAL105 = null;
        Token NUMFLOAT106 = null;
        Token NUMINT107 = null;
        Token NUMDOUBLE108 = null;
        Token HEX109 = null;
        Token OCTAL110 = null;
        Token TRUE111 = null;
        Token FALSE112 = null;
        Token FULL_ID113 = null;
        Token EXP114 = null;
        ProtoParser.var_full_return key = null;

        ProtoParser.var_reserved_return vr = null;

        ProtoParser.signed_constant_return signed_constant115 = null;

        Object val_tree = null;
        Object ASSIGN104_tree = null;
        Object STRING_LITERAL105_tree = null;
        Object NUMFLOAT106_tree = null;
        Object NUMINT107_tree = null;
        Object NUMDOUBLE108_tree = null;
        Object HEX109_tree = null;
        Object OCTAL110_tree = null;
        Object TRUE111_tree = null;
        Object FALSE112_tree = null;
        Object FULL_ID113_tree = null;
        Object EXP114_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:272:5: (key= var_full ASSIGN (vr= var_reserved |
            // STRING_LITERAL | NUMFLOAT | NUMINT | NUMDOUBLE | HEX | OCTAL | TRUE | FALSE | val= ID | FULL_ID | EXP |
            // signed_constant[proto, message, field, $key.text, checkDefault] ) )
            // io/protostuff/parser/ProtoParser.g:272:9: key= var_full ASSIGN (vr= var_reserved |
            // STRING_LITERAL | NUMFLOAT | NUMINT | NUMDOUBLE | HEX | OCTAL | TRUE | FALSE | val= ID | FULL_ID | EXP |
            // signed_constant[proto, message, field, $key.text, checkDefault] )
            {
                root_0 = (Object) adaptor.nil();

                pushFollow(FOLLOW_var_full_in_field_options_keyval1845);
                key = var_full();

                state._fsp--;
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                    adaptor.addChild(root_0, key.getTree());
                ASSIGN104 = (Token) match(input, ASSIGN, FOLLOW_ASSIGN_in_field_options_keyval1847);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    ASSIGN104_tree = (Object) adaptor.create(ASSIGN104);
                    adaptor.addChild(root_0, ASSIGN104_tree);
                }
                // io/protostuff/parser/ProtoParser.g:272:29: (vr= var_reserved | STRING_LITERAL | NUMFLOAT
                // | NUMINT | NUMDOUBLE | HEX | OCTAL | TRUE | FALSE | val= ID | FULL_ID | EXP | signed_constant[proto,
                // message, field, $key.text, checkDefault] )
                int alt21 = 13;
                switch (input.LA(1))
                {
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
                        alt21 = 1;
                    }
                        break;
                    case STRING_LITERAL:
                    {
                        alt21 = 2;
                    }
                        break;
                    case NUMFLOAT:
                    {
                        alt21 = 3;
                    }
                        break;
                    case NUMINT:
                    {
                        alt21 = 4;
                    }
                        break;
                    case NUMDOUBLE:
                    {
                        alt21 = 5;
                    }
                        break;
                    case HEX:
                    {
                        alt21 = 6;
                    }
                        break;
                    case OCTAL:
                    {
                        alt21 = 7;
                    }
                        break;
                    case TRUE:
                    {
                        alt21 = 8;
                    }
                        break;
                    case FALSE:
                    {
                        alt21 = 9;
                    }
                        break;
                    case ID:
                    {
                        alt21 = 10;
                    }
                        break;
                    case FULL_ID:
                    {
                        alt21 = 11;
                    }
                        break;
                    case EXP:
                    {
                        alt21 = 12;
                    }
                        break;
                    case MINUS:
                    {
                        alt21 = 13;
                    }
                        break;
                    default:
                        if (state.backtracking > 0)
                        {
                            state.failed = true;
                            return retval;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 21, 0, input);

                        throw nvae;
                }

                switch (alt21)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:272:30: vr= var_reserved
                    {
                        pushFollow(FOLLOW_var_reserved_in_field_options_keyval1852);
                        vr = var_reserved();

                        state._fsp--;
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                            adaptor.addChild(root_0, vr.getTree());
                        if (state.backtracking == 0)
                        {

                            field.putExtraOption((key != null ? input.toString(key.start, key.stop) : null),
                                    (vr != null ? input.toString(vr.start, vr.stop) : null));

                        }

                    }
                        break;
                    case 2:
                    // io/protostuff/parser/ProtoParser.g:275:9: STRING_LITERAL
                    {
                        STRING_LITERAL105 = (Token) match(input, STRING_LITERAL,
                                FOLLOW_STRING_LITERAL_in_field_options_keyval1865);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            STRING_LITERAL105_tree = (Object) adaptor.create(STRING_LITERAL105);
                            adaptor.addChild(root_0, STRING_LITERAL105_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            if (checkDefault
                                    && "default".equals((key != null ? input.toString(key.start, key.stop) : null)))
                            {
                                if (field.defaultValue != null || field.modifier == Field.Modifier.REPEATED)
                                    throw new IllegalStateException("a field can only have a single default value");

                                if (field instanceof Field.String)
                                    field.defaultValue = getStringFromStringLiteral((STRING_LITERAL105 != null ? STRING_LITERAL105
                                            .getText()
                                            : null));
                                else if (field instanceof Field.Bytes)
                                    field.defaultValue = getBytesFromStringLiteral((STRING_LITERAL105 != null ? STRING_LITERAL105
                                            .getText()
                                            : null));
                                else
                                    throw new IllegalStateException("Invalid string default value for the field: "
                                            + field.getClass().getSimpleName() + " " + field.name);
                            }

                            field.putExtraOption((key != null ? input.toString(key.start, key.stop) : null),
                                    getStringFromStringLiteral((STRING_LITERAL105 != null ? STRING_LITERAL105.getText()
                                            : null)));

                        }

                    }
                        break;
                    case 3:
                    // io/protostuff/parser/ProtoParser.g:290:9: NUMFLOAT
                    {
                        NUMFLOAT106 = (Token) match(input, NUMFLOAT, FOLLOW_NUMFLOAT_in_field_options_keyval1877);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            NUMFLOAT106_tree = (Object) adaptor.create(NUMFLOAT106);
                            adaptor.addChild(root_0, NUMFLOAT106_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            if (checkDefault
                                    && "default".equals((key != null ? input.toString(key.start, key.stop) : null)))
                            {
                                if (field.defaultValue != null || field.modifier == Field.Modifier.REPEATED)
                                    throw new IllegalStateException("a field can only have a single default value");

                                if (field instanceof Field.Float)
                                    field.defaultValue = Float.valueOf((NUMFLOAT106 != null ? NUMFLOAT106.getText()
                                            : null));
                                else if (field instanceof Field.Double)
                                    field.defaultValue = Double.valueOf((NUMFLOAT106 != null ? NUMFLOAT106.getText()
                                            : null));
                                else
                                    throw new IllegalStateException("Invalid float default value for the field: "
                                            + field.getClass().getSimpleName() + " " + field.name);
                            }

                            field.putExtraOption((key != null ? input.toString(key.start, key.stop) : null),
                                    Float.valueOf((NUMFLOAT106 != null ? NUMFLOAT106.getText() : null)));

                        }

                    }
                        break;
                    case 4:
                    // io/protostuff/parser/ProtoParser.g:305:9: NUMINT
                    {
                        NUMINT107 = (Token) match(input, NUMINT, FOLLOW_NUMINT_in_field_options_keyval1890);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            NUMINT107_tree = (Object) adaptor.create(NUMINT107);
                            adaptor.addChild(root_0, NUMINT107_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            if (checkDefault
                                    && "default".equals((key != null ? input.toString(key.start, key.stop) : null)))
                            {
                                if (field.defaultValue != null || field.modifier == Field.Modifier.REPEATED)
                                    throw new IllegalStateException("a field can only have a single default value");

                                if (field instanceof Field.Number)
                                {
                                    if (field.getClass().getSimpleName().endsWith("32"))
                                        field.defaultValue = Integer.valueOf((NUMINT107 != null ? NUMINT107.getText()
                                                : null));
                                    else if (field.getClass().getSimpleName().endsWith("64"))
                                        field.defaultValue = Long.valueOf((NUMINT107 != null ? NUMINT107.getText()
                                                : null));
                                    else if (field instanceof Field.Float)
                                        field.defaultValue = Float.valueOf((NUMINT107 != null ? NUMINT107.getText()
                                                : null));
                                    else if (field instanceof Field.Double)
                                        field.defaultValue = Double.valueOf((NUMINT107 != null ? NUMINT107.getText()
                                                : null));
                                    else
                                        throw new IllegalStateException("Invalid numeric default value for the field: "
                                                + field.getClass().getSimpleName() + " " + field.name);
                                }
                                else
                                    throw new IllegalStateException("Invalid numeric default value for the field: "
                                            + field.getClass().getSimpleName() + " " + field.name);
                            }

                            field.putExtraOption((key != null ? input.toString(key.start, key.stop) : null),
                                    Integer.valueOf((NUMINT107 != null ? NUMINT107.getText() : null)));

                        }

                    }
                        break;
                    case 5:
                    // io/protostuff/parser/ProtoParser.g:328:9: NUMDOUBLE
                    {
                        NUMDOUBLE108 = (Token) match(input, NUMDOUBLE, FOLLOW_NUMDOUBLE_in_field_options_keyval1902);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            NUMDOUBLE108_tree = (Object) adaptor.create(NUMDOUBLE108);
                            adaptor.addChild(root_0, NUMDOUBLE108_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            if (checkDefault
                                    && "default".equals((key != null ? input.toString(key.start, key.stop) : null)))
                            {
                                if (field.defaultValue != null || field.modifier == Field.Modifier.REPEATED)
                                    throw new IllegalStateException("a field can only have a single default value");

                                if (field instanceof Field.Float)
                                    field.defaultValue = Float.valueOf((NUMDOUBLE108 != null ? NUMDOUBLE108.getText()
                                            : null));
                                else if (field instanceof Field.Double)
                                    field.defaultValue = Double.valueOf((NUMDOUBLE108 != null ? NUMDOUBLE108.getText()
                                            : null));
                                else
                                    throw new IllegalStateException("Invalid numeric default value for the field: "
                                            + field.getClass().getSimpleName() + " " + field.name);
                            }

                            field.putExtraOption((key != null ? input.toString(key.start, key.stop) : null),
                                    Double.valueOf((NUMDOUBLE108 != null ? NUMDOUBLE108.getText() : null)));

                        }

                    }
                        break;
                    case 6:
                    // io/protostuff/parser/ProtoParser.g:343:9: HEX
                    {
                        HEX109 = (Token) match(input, HEX, FOLLOW_HEX_in_field_options_keyval1914);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            HEX109_tree = (Object) adaptor.create(HEX109);
                            adaptor.addChild(root_0, HEX109_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            if (checkDefault
                                    && "default".equals((key != null ? input.toString(key.start, key.stop) : null)))
                            {
                                if (field.defaultValue != null || field.modifier == Field.Modifier.REPEATED)
                                    throw new IllegalStateException("a field can only have a single default value");

                                if (field instanceof Field.Number)
                                {
                                    if (field instanceof Field.Int32)
                                        field.defaultValue = new Integer(TextFormat.parseInt32((HEX109 != null ? HEX109
                                                .getText() : null)));
                                    else if (field instanceof Field.UInt32)
                                        field.defaultValue = new Integer(
                                                TextFormat.parseUInt32((HEX109 != null ? HEX109.getText() : null)));
                                    else if (field instanceof Field.Int64)
                                        field.defaultValue = new Long(TextFormat.parseInt64((HEX109 != null ? HEX109
                                                .getText() : null)));
                                    else if (field instanceof Field.UInt64)
                                        field.defaultValue = new Long(TextFormat.parseUInt64((HEX109 != null ? HEX109
                                                .getText() : null)));
                                    else if (field instanceof Field.Float)
                                        field.defaultValue = new Float(Long.decode(
                                                (HEX109 != null ? HEX109.getText() : null)).floatValue());
                                    else if (field instanceof Field.Double)
                                        field.defaultValue = new Double(Long.decode(
                                                (HEX109 != null ? HEX109.getText() : null)).doubleValue());
                                }
                                else if (field instanceof Field.Bytes)
                                {
                                    field.defaultValue = getBytesFromHexString((HEX109 != null ? HEX109.getText()
                                            : null));
                                }
                                else
                                    throw new IllegalStateException("Invalid numeric default value for the field: "
                                            + field.getClass().getSimpleName() + " " + field.name);

                            }

                            field.putExtraOption((key != null ? input.toString(key.start, key.stop) : null),
                                    (HEX109 != null ? HEX109.getText() : null));

                        }

                    }
                        break;
                    case 7:
                    // io/protostuff/parser/ProtoParser.g:372:9: OCTAL
                    {
                        OCTAL110 = (Token) match(input, OCTAL, FOLLOW_OCTAL_in_field_options_keyval1926);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            OCTAL110_tree = (Object) adaptor.create(OCTAL110);
                            adaptor.addChild(root_0, OCTAL110_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            if (checkDefault
                                    && "default".equals((key != null ? input.toString(key.start, key.stop) : null)))
                            {
                                if (field.defaultValue != null || field.modifier == Field.Modifier.REPEATED)
                                    throw new IllegalStateException("a field can only have a single default value");

                                if (field instanceof Field.Number)
                                {
                                    if (field instanceof Field.Int32)
                                        field.defaultValue = new Integer(
                                                TextFormat.parseInt32((OCTAL110 != null ? OCTAL110.getText() : null)));
                                    else if (field instanceof Field.UInt32)
                                        field.defaultValue = new Integer(
                                                TextFormat.parseUInt32((OCTAL110 != null ? OCTAL110.getText() : null)));
                                    else if (field instanceof Field.Int64)
                                        field.defaultValue = new Long(
                                                TextFormat.parseInt64((OCTAL110 != null ? OCTAL110.getText() : null)));
                                    else if (field instanceof Field.UInt64)
                                        field.defaultValue = new Long(
                                                TextFormat.parseUInt64((OCTAL110 != null ? OCTAL110.getText() : null)));
                                    else if (field instanceof Field.Float)
                                        field.defaultValue = new Float(Long.decode(
                                                (OCTAL110 != null ? OCTAL110.getText() : null)).floatValue());
                                    else if (field instanceof Field.Double)
                                        field.defaultValue = new Double(Long.decode(
                                                (OCTAL110 != null ? OCTAL110.getText() : null)).doubleValue());
                                }
                                else
                                    throw new IllegalStateException("Invalid numeric default value for the field: "
                                            + field.getClass().getSimpleName() + " " + field.name);
                            }

                            field.putExtraOption((key != null ? input.toString(key.start, key.stop) : null),
                                    (OCTAL110 != null ? OCTAL110.getText() : null));

                        }

                    }
                        break;
                    case 8:
                    // io/protostuff/parser/ProtoParser.g:397:9: TRUE
                    {
                        TRUE111 = (Token) match(input, TRUE, FOLLOW_TRUE_in_field_options_keyval1938);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            TRUE111_tree = (Object) adaptor.create(TRUE111);
                            adaptor.addChild(root_0, TRUE111_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            if (checkDefault
                                    && "default".equals((key != null ? input.toString(key.start, key.stop) : null)))
                            {
                                if (field.defaultValue != null || field.modifier == Field.Modifier.REPEATED)
                                    throw new IllegalStateException("a field can only have a single default value");

                                if (field instanceof Field.Bool)
                                    field.defaultValue = Boolean.TRUE;
                                else
                                    throw new IllegalStateException(
                                            "invalid boolean default value for the non-boolean field: "
                                                    + field.getClass().getSimpleName() + " " + field.name);
                            }

                            field.putExtraOption((key != null ? input.toString(key.start, key.stop) : null),
                                    Boolean.TRUE);

                        }

                    }
                        break;
                    case 9:
                    // io/protostuff/parser/ProtoParser.g:410:9: FALSE
                    {
                        FALSE112 = (Token) match(input, FALSE, FOLLOW_FALSE_in_field_options_keyval1954);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            FALSE112_tree = (Object) adaptor.create(FALSE112);
                            adaptor.addChild(root_0, FALSE112_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            if (checkDefault
                                    && "default".equals((key != null ? input.toString(key.start, key.stop) : null)))
                            {
                                if (field.defaultValue != null || field.modifier == Field.Modifier.REPEATED)
                                    throw new IllegalStateException("a field can only have a single default value");

                                if (field instanceof Field.Bool)
                                    field.defaultValue = Boolean.FALSE;
                                else
                                    throw new IllegalStateException(
                                            "invalid boolean default value for the non-boolean field: "
                                                    + field.getClass().getSimpleName() + " " + field.name);
                            }

                            field.putExtraOption((key != null ? input.toString(key.start, key.stop) : null),
                                    Boolean.FALSE);

                        }

                    }
                        break;
                    case 10:
                    // io/protostuff/parser/ProtoParser.g:423:9: val= ID
                    {
                        val = (Token) match(input, ID, FOLLOW_ID_in_field_options_keyval1968);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            val_tree = (Object) adaptor.create(val);
                            adaptor.addChild(root_0, val_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            boolean refOption = false;
                            if (checkDefault
                                    && "default".equals((key != null ? input.toString(key.start, key.stop) : null)))
                            {
                                if (field.defaultValue != null || field.modifier == Field.Modifier.REPEATED)
                                    throw new IllegalStateException("a field can only have a single default value");

                                String refName = (val != null ? val.getText() : null);
                                if (field instanceof Field.Reference)
                                    field.defaultValue = refName;
                                else if (field instanceof Field.Float)
                                {
                                    if ("inf".equals(refName))
                                    {
                                        field.defaultValue = Float.POSITIVE_INFINITY;
                                        field.defaultValueConstant = "Float.POSITIVE_INFINITY";
                                    }
                                    else if ("nan".equals(refName))
                                    {
                                        field.defaultValue = Float.NaN;
                                        field.defaultValueConstant = "Float.NaN";
                                    }
                                    else
                                        throw new IllegalStateException("Invalid float default value for the field: "
                                                + field.getClass().getSimpleName() + " " + field.name);
                                }
                                else if (field instanceof Field.Double)
                                {
                                    if ("inf".equals(refName))
                                    {
                                        field.defaultValue = Double.POSITIVE_INFINITY;
                                        field.defaultValueConstant = "Double.POSITIVE_INFINITY";
                                    }
                                    else if ("nan".equals(refName))
                                    {
                                        field.defaultValue = Double.NaN;
                                        field.defaultValueConstant = "Double.NaN";
                                    }
                                    else
                                        throw new IllegalStateException("Invalid double default value for the field: "
                                                + field.getClass().getSimpleName() + " " + field.name);
                                }
                                else
                                {
                                    refOption = true;
                                    // throw new IllegalStateException("invalid field value '" + refName +
                                    // "' for the field: " + field.getClass().getSimpleName() + " " + field.name);
                                }
                            }
                            else
                            {
                                refOption = true;
                            }

                            if (refOption)
                                field.putStandardOption((key != null ? input.toString(key.start, key.stop) : null),
                                        (val != null ? val.getText() : null));
                            else
                                field.putExtraOption((key != null ? input.toString(key.start, key.stop) : null),
                                        (val != null ? val.getText() : null));

                        }

                    }
                        break;
                    case 11:
                    // io/protostuff/parser/ProtoParser.g:470:9: FULL_ID
                    {
                        FULL_ID113 = (Token) match(input, FULL_ID, FOLLOW_FULL_ID_in_field_options_keyval1980);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            FULL_ID113_tree = (Object) adaptor.create(FULL_ID113);
                            adaptor.addChild(root_0, FULL_ID113_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            field.putStandardOption((key != null ? input.toString(key.start, key.stop) : null),
                                    (FULL_ID113 != null ? FULL_ID113.getText() : null));

                        }

                    }
                        break;
                    case 12:
                    // io/protostuff/parser/ProtoParser.g:473:9: EXP
                    {
                        EXP114 = (Token) match(input, EXP, FOLLOW_EXP_in_field_options_keyval1992);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            EXP114_tree = (Object) adaptor.create(EXP114);
                            adaptor.addChild(root_0, EXP114_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            if (checkDefault
                                    && "default".equals((key != null ? input.toString(key.start, key.stop) : null)))
                            {
                                if (field.defaultValue != null || field.modifier == Field.Modifier.REPEATED)
                                    throw new IllegalStateException("a field can only have a single default value");

                                if (field instanceof Field.Float)
                                    field.defaultValue = Float.valueOf((EXP114 != null ? EXP114.getText() : null));
                                else if (field instanceof Field.Double)
                                    field.defaultValue = Double.valueOf((EXP114 != null ? EXP114.getText() : null));
                                else
                                    throw new IllegalStateException("Invalid float default value for the field: "
                                            + field.getClass().getSimpleName() + " " + field.name);
                            }

                            field.putExtraOption((key != null ? input.toString(key.start, key.stop) : null),
                                    (EXP114 != null ? EXP114.getText() : null));

                        }

                    }
                        break;
                    case 13:
                    // io/protostuff/parser/ProtoParser.g:488:9: signed_constant[proto, message, field,
                    // $key.text, checkDefault]
                    {
                        pushFollow(FOLLOW_signed_constant_in_field_options_keyval2004);
                        signed_constant115 = signed_constant(proto, message, field,
                                (key != null ? input.toString(key.start, key.stop) : null), checkDefault);

                        state._fsp--;
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                            adaptor.addChild(root_0, signed_constant115.getTree());
                        if (state.backtracking == 0)
                        {

                            field.putExtraOption(
                                    (key != null ? input.toString(key.start, key.stop) : null),
                                    (signed_constant115 != null ? input.toString(signed_constant115.start,
                                            signed_constant115.stop) : null));

                        }

                    }
                        break;

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "field_options_keyval"

    public static class signed_constant_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "signed_constant"
    // io/protostuff/parser/ProtoParser.g:494:1: signed_constant[Proto proto, HasFields message, Field
    // field, String key, boolean checkDefault] : MINUS ID ;
    public final ProtoParser.signed_constant_return signed_constant(Proto proto, HasFields message, Field field,
            String key, boolean checkDefault) throws RecognitionException
    {
        ProtoParser.signed_constant_return retval = new ProtoParser.signed_constant_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token MINUS116 = null;
        Token ID117 = null;

        Object MINUS116_tree = null;
        Object ID117_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:495:5: ( MINUS ID )
            // io/protostuff/parser/ProtoParser.g:495:9: MINUS ID
            {
                root_0 = (Object) adaptor.nil();

                MINUS116 = (Token) match(input, MINUS, FOLLOW_MINUS_in_signed_constant2042);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    MINUS116_tree = (Object) adaptor.create(MINUS116);
                    adaptor.addChild(root_0, MINUS116_tree);
                }
                ID117 = (Token) match(input, ID, FOLLOW_ID_in_signed_constant2044);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    ID117_tree = (Object) adaptor.create(ID117);
                    adaptor.addChild(root_0, ID117_tree);
                }
                if (state.backtracking == 0)
                {

                    if (checkDefault && "default".equals(key))
                    {
                        if (field.defaultValue != null || field.modifier == Field.Modifier.REPEATED)
                            throw new IllegalStateException("a field can only have a single default value");

                        String refName = (ID117 != null ? ID117.getText() : null);
                        if (field instanceof Field.Float)
                        {
                            if ("inf".equals(refName))
                            {
                                field.defaultValue = Float.NEGATIVE_INFINITY;
                                field.defaultValueConstant = "Float.NEGATIVE_INFINITY";
                            }
                            else
                                throw new IllegalStateException("Invalid float default value for the field: "
                                        + field.getClass().getSimpleName() + " " + field.name);
                        }
                        else if (field instanceof Field.Double)
                        {
                            if ("inf".equals(refName))
                            {
                                field.defaultValue = Double.NEGATIVE_INFINITY;
                                field.defaultValueConstant = "Double.NEGATIVE_INFINITY";
                            }
                            else
                                throw new IllegalStateException("Invalid double default value for the field: "
                                        + field.getClass().getSimpleName() + " " + field.name);
                        }
                        else
                            throw new IllegalStateException("invalid field value '" + refName + "' for the field: "
                                    + field.getClass().getSimpleName() + " " + field.name);
                    }

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "signed_constant"

    public static class enum_block_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "enum_block"
    // io/protostuff/parser/ProtoParser.g:523:1: enum_block[Proto proto, Message message] : ENUM ID
    // LEFTCURLY ( enum_body[proto, message, enumGroup] )* RIGHTCURLY ( ( SEMICOLON )? ) ;
    public final ProtoParser.enum_block_return enum_block(Proto proto, Message message) throws RecognitionException
    {
        ProtoParser.enum_block_return retval = new ProtoParser.enum_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ENUM118 = null;
        Token ID119 = null;
        Token LEFTCURLY120 = null;
        Token RIGHTCURLY122 = null;
        Token SEMICOLON123 = null;
        ProtoParser.enum_body_return enum_body121 = null;

        Object ENUM118_tree = null;
        Object ID119_tree = null;
        Object LEFTCURLY120_tree = null;
        Object RIGHTCURLY122_tree = null;
        Object SEMICOLON123_tree = null;

        EnumGroup enumGroup = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:527:5: ( ENUM ID LEFTCURLY ( enum_body[proto, message,
            // enumGroup] )* RIGHTCURLY ( ( SEMICOLON )? ) )
            // io/protostuff/parser/ProtoParser.g:527:9: ENUM ID LEFTCURLY ( enum_body[proto, message,
            // enumGroup] )* RIGHTCURLY ( ( SEMICOLON )? )
            {
                root_0 = (Object) adaptor.nil();

                ENUM118 = (Token) match(input, ENUM, FOLLOW_ENUM_in_enum_block2076);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    ENUM118_tree = (Object) adaptor.create(ENUM118);
                    adaptor.addChild(root_0, ENUM118_tree);
                }
                ID119 = (Token) match(input, ID, FOLLOW_ID_in_enum_block2078);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    ID119_tree = (Object) adaptor.create(ID119);
                    adaptor.addChild(root_0, ID119_tree);
                }
                if (state.backtracking == 0)
                {

                    enumGroup = new EnumGroup((ID119 != null ? ID119.getText() : null), message, proto);
                    proto.addAnnotationsTo(enumGroup);

                }
                LEFTCURLY120 = (Token) match(input, LEFTCURLY, FOLLOW_LEFTCURLY_in_enum_block2091);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    LEFTCURLY120_tree = (Object) adaptor.create(LEFTCURLY120);
                    adaptor.addChild(root_0, LEFTCURLY120_tree);
                }
                // io/protostuff/parser/ProtoParser.g:531:19: ( enum_body[proto, message, enumGroup] )*
                loop22: do
                {
                    int alt22 = 2;
                    switch (input.LA(1))
                    {
                        case AT:
                        case OPTION:
                        case ID:
                        {
                            alt22 = 1;
                        }
                            break;

                    }

                    switch (alt22)
                    {
                        case 1:
                        // io/protostuff/parser/ProtoParser.g:531:20: enum_body[proto, message, enumGroup]
                        {
                            pushFollow(FOLLOW_enum_body_in_enum_block2094);
                            enum_body121 = enum_body(proto, message, enumGroup);

                            state._fsp--;
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                                adaptor.addChild(root_0, enum_body121.getTree());

                        }
                            break;

                        default:
                            break loop22;
                    }
                } while (true);

                RIGHTCURLY122 = (Token) match(input, RIGHTCURLY, FOLLOW_RIGHTCURLY_in_enum_block2099);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    RIGHTCURLY122_tree = (Object) adaptor.create(RIGHTCURLY122);
                    adaptor.addChild(root_0, RIGHTCURLY122_tree);
                }
                if (state.backtracking == 0)
                {

                    if (!proto.annotations.isEmpty())
                        throw new IllegalStateException("Misplaced annotations: " + proto.annotations);

                }
                // io/protostuff/parser/ProtoParser.g:534:11: ( ( SEMICOLON )? )
                // io/protostuff/parser/ProtoParser.g:534:12: ( SEMICOLON )?
                {
                    // io/protostuff/parser/ProtoParser.g:534:12: ( SEMICOLON )?
                    int alt23 = 2;
                    switch (input.LA(1))
                    {
                        case SEMICOLON:
                        {
                            alt23 = 1;
                        }
                            break;
                    }

                    switch (alt23)
                    {
                        case 1:
                        // io/protostuff/parser/ProtoParser.g:534:12: SEMICOLON
                        {
                            SEMICOLON123 = (Token) match(input, SEMICOLON, FOLLOW_SEMICOLON_in_enum_block2104);
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                            {
                                SEMICOLON123_tree = (Object) adaptor.create(SEMICOLON123);
                                adaptor.addChild(root_0, SEMICOLON123_tree);
                            }

                        }
                            break;

                    }

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "enum_block"

    public static class enum_body_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "enum_body"
    // io/protostuff/parser/ProtoParser.g:537:1: enum_body[Proto proto, Message message, EnumGroup
    // enumGroup] : ( enum_field[proto, message, enumGroup] | annotation_entry[proto] | option_entry[proto, enumGroup]
    // );
    public final ProtoParser.enum_body_return enum_body(Proto proto, Message message, EnumGroup enumGroup)
            throws RecognitionException
    {
        ProtoParser.enum_body_return retval = new ProtoParser.enum_body_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ProtoParser.enum_field_return enum_field124 = null;

        ProtoParser.annotation_entry_return annotation_entry125 = null;

        ProtoParser.option_entry_return option_entry126 = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:538:5: ( enum_field[proto, message, enumGroup] |
            // annotation_entry[proto] | option_entry[proto, enumGroup] )
            int alt24 = 3;
            switch (input.LA(1))
            {
                case ID:
                {
                    alt24 = 1;
                }
                    break;
                case AT:
                {
                    alt24 = 2;
                }
                    break;
                case OPTION:
                {
                    alt24 = 3;
                }
                    break;
                default:
                    if (state.backtracking > 0)
                    {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 24, 0, input);

                    throw nvae;
            }

            switch (alt24)
            {
                case 1:
                // io/protostuff/parser/ProtoParser.g:538:9: enum_field[proto, message, enumGroup]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_enum_field_in_enum_body2132);
                    enum_field124 = enum_field(proto, message, enumGroup);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, enum_field124.getTree());

                }
                    break;
                case 2:
                // io/protostuff/parser/ProtoParser.g:539:9: annotation_entry[proto]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_annotation_entry_in_enum_body2143);
                    annotation_entry125 = annotation_entry(proto);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, annotation_entry125.getTree());

                }
                    break;
                case 3:
                // io/protostuff/parser/ProtoParser.g:540:9: option_entry[proto, enumGroup]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_option_entry_in_enum_body2154);
                    option_entry126 = option_entry(proto, enumGroup);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, option_entry126.getTree());

                }
                    break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "enum_body"

    public static class enum_field_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "enum_field"
    // io/protostuff/parser/ProtoParser.g:543:1: enum_field[Proto proto, Message message, EnumGroup
    // enumGroup] : ID ASSIGN NUMINT ( enum_options[proto, enumGroup, v] )? SEMICOLON ;
    public final ProtoParser.enum_field_return enum_field(Proto proto, Message message, EnumGroup enumGroup)
            throws RecognitionException
    {
        ProtoParser.enum_field_return retval = new ProtoParser.enum_field_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ID127 = null;
        Token ASSIGN128 = null;
        Token NUMINT129 = null;
        Token SEMICOLON131 = null;
        ProtoParser.enum_options_return enum_options130 = null;

        Object ID127_tree = null;
        Object ASSIGN128_tree = null;
        Object NUMINT129_tree = null;
        Object SEMICOLON131_tree = null;

        EnumGroup.Value v = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:547:5: ( ID ASSIGN NUMINT ( enum_options[proto, enumGroup,
            // v] )? SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:547:9: ID ASSIGN NUMINT ( enum_options[proto, enumGroup,
            // v] )? SEMICOLON
            {
                root_0 = (Object) adaptor.nil();

                ID127 = (Token) match(input, ID, FOLLOW_ID_in_enum_field2181);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    ID127_tree = (Object) adaptor.create(ID127);
                    adaptor.addChild(root_0, ID127_tree);
                }
                ASSIGN128 = (Token) match(input, ASSIGN, FOLLOW_ASSIGN_in_enum_field2183);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    ASSIGN128_tree = (Object) adaptor.create(ASSIGN128);
                    adaptor.addChild(root_0, ASSIGN128_tree);
                }
                NUMINT129 = (Token) match(input, NUMINT, FOLLOW_NUMINT_in_enum_field2185);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    NUMINT129_tree = (Object) adaptor.create(NUMINT129);
                    adaptor.addChild(root_0, NUMINT129_tree);
                }
                if (state.backtracking == 0)
                {

                    v = new EnumGroup.Value((ID127 != null ? ID127.getText() : null),
                            Integer.parseInt((NUMINT129 != null ? NUMINT129.getText() : null)), enumGroup);
                    proto.addAnnotationsTo(v);

                }
                // io/protostuff/parser/ProtoParser.g:550:11: ( enum_options[proto, enumGroup, v] )?
                int alt25 = 2;
                switch (input.LA(1))
                {
                    case LEFTSQUARE:
                    {
                        alt25 = 1;
                    }
                        break;
                }

                switch (alt25)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:550:12: enum_options[proto, enumGroup, v]
                    {
                        pushFollow(FOLLOW_enum_options_in_enum_field2190);
                        enum_options130 = enum_options(proto, enumGroup, v);

                        state._fsp--;
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                            adaptor.addChild(root_0, enum_options130.getTree());

                    }
                        break;

                }

                SEMICOLON131 = (Token) match(input, SEMICOLON, FOLLOW_SEMICOLON_in_enum_field2195);
                if (state.failed)
                    return retval;

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "enum_field"

    public static class enum_options_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "enum_options"
    // io/protostuff/parser/ProtoParser.g:553:1: enum_options[Proto proto, EnumGroup enumGroup,
    // EnumGroup.Value v] : LEFTSQUARE field_options_keyval[proto, null, v.field, false] ( COMMA
    // field_options_keyval[proto, null, v.field, false] )* RIGHTSQUARE ;
    public final ProtoParser.enum_options_return enum_options(Proto proto, EnumGroup enumGroup, EnumGroup.Value v)
            throws RecognitionException
    {
        ProtoParser.enum_options_return retval = new ProtoParser.enum_options_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LEFTSQUARE132 = null;
        Token COMMA134 = null;
        Token RIGHTSQUARE136 = null;
        ProtoParser.field_options_keyval_return field_options_keyval133 = null;

        ProtoParser.field_options_keyval_return field_options_keyval135 = null;

        Object LEFTSQUARE132_tree = null;
        Object COMMA134_tree = null;
        Object RIGHTSQUARE136_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:554:5: ( LEFTSQUARE field_options_keyval[proto, null,
            // v.field, false] ( COMMA field_options_keyval[proto, null, v.field, false] )* RIGHTSQUARE )
            // io/protostuff/parser/ProtoParser.g:554:9: LEFTSQUARE field_options_keyval[proto, null,
            // v.field, false] ( COMMA field_options_keyval[proto, null, v.field, false] )* RIGHTSQUARE
            {
                root_0 = (Object) adaptor.nil();

                LEFTSQUARE132 = (Token) match(input, LEFTSQUARE, FOLLOW_LEFTSQUARE_in_enum_options2218);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    LEFTSQUARE132_tree = (Object) adaptor.create(LEFTSQUARE132);
                    adaptor.addChild(root_0, LEFTSQUARE132_tree);
                }
                pushFollow(FOLLOW_field_options_keyval_in_enum_options2220);
                field_options_keyval133 = field_options_keyval(proto, null, v.field, false);

                state._fsp--;
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                    adaptor.addChild(root_0, field_options_keyval133.getTree());
                // io/protostuff/parser/ProtoParser.g:555:9: ( COMMA field_options_keyval[proto, null,
                // v.field, false] )*
                loop26: do
                {
                    int alt26 = 2;
                    switch (input.LA(1))
                    {
                        case COMMA:
                        {
                            alt26 = 1;
                        }
                            break;

                    }

                    switch (alt26)
                    {
                        case 1:
                        // io/protostuff/parser/ProtoParser.g:555:10: COMMA field_options_keyval[proto,
                        // null, v.field, false]
                        {
                            COMMA134 = (Token) match(input, COMMA, FOLLOW_COMMA_in_enum_options2233);
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                            {
                                COMMA134_tree = (Object) adaptor.create(COMMA134);
                                adaptor.addChild(root_0, COMMA134_tree);
                            }
                            pushFollow(FOLLOW_field_options_keyval_in_enum_options2235);
                            field_options_keyval135 = field_options_keyval(proto, null, v.field, false);

                            state._fsp--;
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                                adaptor.addChild(root_0, field_options_keyval135.getTree());

                        }
                            break;

                        default:
                            break loop26;
                    }
                } while (true);

                RIGHTSQUARE136 = (Token) match(input, RIGHTSQUARE, FOLLOW_RIGHTSQUARE_in_enum_options2240);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    RIGHTSQUARE136_tree = (Object) adaptor.create(RIGHTSQUARE136);
                    adaptor.addChild(root_0, RIGHTSQUARE136_tree);
                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "enum_options"

    public static class service_block_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "service_block"
    // io/protostuff/parser/ProtoParser.g:558:1: service_block[Proto proto, Message message] : SERVICE ID
    // LEFTCURLY ( service_body[proto, service] )+ RIGHTCURLY ( ( SEMICOLON )? ) ;
    public final ProtoParser.service_block_return service_block(Proto proto, Message message)
            throws RecognitionException
    {
        ProtoParser.service_block_return retval = new ProtoParser.service_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token SERVICE137 = null;
        Token ID138 = null;
        Token LEFTCURLY139 = null;
        Token RIGHTCURLY141 = null;
        Token SEMICOLON142 = null;
        ProtoParser.service_body_return service_body140 = null;

        Object SERVICE137_tree = null;
        Object ID138_tree = null;
        Object LEFTCURLY139_tree = null;
        Object RIGHTCURLY141_tree = null;
        Object SEMICOLON142_tree = null;

        Service service = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:562:5: ( SERVICE ID LEFTCURLY ( service_body[proto,
            // service] )+ RIGHTCURLY ( ( SEMICOLON )? ) )
            // io/protostuff/parser/ProtoParser.g:562:9: SERVICE ID LEFTCURLY ( service_body[proto, service]
            // )+ RIGHTCURLY ( ( SEMICOLON )? )
            {
                root_0 = (Object) adaptor.nil();

                SERVICE137 = (Token) match(input, SERVICE, FOLLOW_SERVICE_in_service_block2270);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    SERVICE137_tree = (Object) adaptor.create(SERVICE137);
                    adaptor.addChild(root_0, SERVICE137_tree);
                }
                ID138 = (Token) match(input, ID, FOLLOW_ID_in_service_block2272);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    ID138_tree = (Object) adaptor.create(ID138);
                    adaptor.addChild(root_0, ID138_tree);
                }
                if (state.backtracking == 0)
                {

                    service = new Service((ID138 != null ? ID138.getText() : null), message, proto);
                    proto.addAnnotationsTo(service);

                }
                LEFTCURLY139 = (Token) match(input, LEFTCURLY, FOLLOW_LEFTCURLY_in_service_block2276);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    LEFTCURLY139_tree = (Object) adaptor.create(LEFTCURLY139);
                    adaptor.addChild(root_0, LEFTCURLY139_tree);
                }
                // io/protostuff/parser/ProtoParser.g:566:9: ( service_body[proto, service] )+
                int cnt27 = 0;
                loop27: do
                {
                    int alt27 = 2;
                    switch (input.LA(1))
                    {
                        case AT:
                        case OPTION:
                        case RPC:
                        {
                            alt27 = 1;
                        }
                            break;

                    }

                    switch (alt27)
                    {
                        case 1:
                        // io/protostuff/parser/ProtoParser.g:566:10: service_body[proto, service]
                        {
                            pushFollow(FOLLOW_service_body_in_service_block2287);
                            service_body140 = service_body(proto, service);

                            state._fsp--;
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                                adaptor.addChild(root_0, service_body140.getTree());

                        }
                            break;

                        default:
                            if (cnt27 >= 1)
                                break loop27;
                            if (state.backtracking > 0)
                            {
                                state.failed = true;
                                return retval;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(27, input);
                            throw eee;
                    }
                    cnt27++;
                } while (true);

                RIGHTCURLY141 = (Token) match(input, RIGHTCURLY, FOLLOW_RIGHTCURLY_in_service_block2292);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    RIGHTCURLY141_tree = (Object) adaptor.create(RIGHTCURLY141);
                    adaptor.addChild(root_0, RIGHTCURLY141_tree);
                }
                // io/protostuff/parser/ProtoParser.g:566:52: ( ( SEMICOLON )? )
                // io/protostuff/parser/ProtoParser.g:566:53: ( SEMICOLON )?
                {
                    // io/protostuff/parser/ProtoParser.g:566:53: ( SEMICOLON )?
                    int alt28 = 2;
                    switch (input.LA(1))
                    {
                        case SEMICOLON:
                        {
                            alt28 = 1;
                        }
                            break;
                    }

                    switch (alt28)
                    {
                        case 1:
                        // io/protostuff/parser/ProtoParser.g:566:53: SEMICOLON
                        {
                            SEMICOLON142 = (Token) match(input, SEMICOLON, FOLLOW_SEMICOLON_in_service_block2295);
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                            {
                                SEMICOLON142_tree = (Object) adaptor.create(SEMICOLON142);
                                adaptor.addChild(root_0, SEMICOLON142_tree);
                            }

                        }
                            break;

                    }

                }

                if (state.backtracking == 0)
                {

                    if (service.rpcMethods.isEmpty())
                        throw new IllegalStateException("Empty Service block: " + service.getName());

                    if (!proto.annotations.isEmpty())
                        throw new IllegalStateException("Misplaced annotations: " + proto.annotations);

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "service_block"

    public static class service_body_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "service_body"
    // io/protostuff/parser/ProtoParser.g:575:1: service_body[Proto proto, Service service] : (
    // rpc_block[proto, service] | annotation_entry[proto] | option_entry[proto, service] );
    public final ProtoParser.service_body_return service_body(Proto proto, Service service) throws RecognitionException
    {
        ProtoParser.service_body_return retval = new ProtoParser.service_body_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ProtoParser.rpc_block_return rpc_block143 = null;

        ProtoParser.annotation_entry_return annotation_entry144 = null;

        ProtoParser.option_entry_return option_entry145 = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:576:5: ( rpc_block[proto, service] |
            // annotation_entry[proto] | option_entry[proto, service] )
            int alt29 = 3;
            switch (input.LA(1))
            {
                case RPC:
                {
                    alt29 = 1;
                }
                    break;
                case AT:
                {
                    alt29 = 2;
                }
                    break;
                case OPTION:
                {
                    alt29 = 3;
                }
                    break;
                default:
                    if (state.backtracking > 0)
                    {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 29, 0, input);

                    throw nvae;
            }

            switch (alt29)
            {
                case 1:
                // io/protostuff/parser/ProtoParser.g:576:9: rpc_block[proto, service]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_rpc_block_in_service_body2325);
                    rpc_block143 = rpc_block(proto, service);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, rpc_block143.getTree());

                }
                    break;
                case 2:
                // io/protostuff/parser/ProtoParser.g:577:9: annotation_entry[proto]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_annotation_entry_in_service_body2336);
                    annotation_entry144 = annotation_entry(proto);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, annotation_entry144.getTree());

                }
                    break;
                case 3:
                // io/protostuff/parser/ProtoParser.g:578:9: option_entry[proto, service]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_option_entry_in_service_body2347);
                    option_entry145 = option_entry(proto, service);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, option_entry145.getTree());

                }
                    break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "service_body"

    public static class rpc_block_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "rpc_block"
    // io/protostuff/parser/ProtoParser.g:581:1: rpc_block[Proto proto, Service service] : RPC n= ID
    // LEFTPAREN (ap= FULL_ID | a= ( VOID | ID ) ) RIGHTPAREN RETURNS LEFTPAREN (rp= FULL_ID | r= ( VOID | ID ) )
    // RIGHTPAREN ( rpc_body_block[proto, rm] )? SEMICOLON ;
    public final ProtoParser.rpc_block_return rpc_block(Proto proto, Service service) throws RecognitionException
    {
        ProtoParser.rpc_block_return retval = new ProtoParser.rpc_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token n = null;
        Token ap = null;
        Token a = null;
        Token rp = null;
        Token r = null;
        Token RPC146 = null;
        Token LEFTPAREN147 = null;
        Token RIGHTPAREN148 = null;
        Token RETURNS149 = null;
        Token LEFTPAREN150 = null;
        Token RIGHTPAREN151 = null;
        Token SEMICOLON153 = null;
        ProtoParser.rpc_body_block_return rpc_body_block152 = null;

        Object n_tree = null;
        Object ap_tree = null;
        Object a_tree = null;
        Object rp_tree = null;
        Object r_tree = null;
        Object RPC146_tree = null;
        Object LEFTPAREN147_tree = null;
        Object RIGHTPAREN148_tree = null;
        Object RETURNS149_tree = null;
        Object LEFTPAREN150_tree = null;
        Object RIGHTPAREN151_tree = null;
        Object SEMICOLON153_tree = null;

        String argName = null, argPackage = null, retName = null, retPackage = null;
        Service.RpcMethod rm = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:586:5: ( RPC n= ID LEFTPAREN (ap= FULL_ID | a= ( VOID | ID
            // ) ) RIGHTPAREN RETURNS LEFTPAREN (rp= FULL_ID | r= ( VOID | ID ) ) RIGHTPAREN ( rpc_body_block[proto, rm]
            // )? SEMICOLON )
            // io/protostuff/parser/ProtoParser.g:586:9: RPC n= ID LEFTPAREN (ap= FULL_ID | a= ( VOID | ID )
            // ) RIGHTPAREN RETURNS LEFTPAREN (rp= FULL_ID | r= ( VOID | ID ) ) RIGHTPAREN ( rpc_body_block[proto, rm]
            // )? SEMICOLON
            {
                root_0 = (Object) adaptor.nil();

                RPC146 = (Token) match(input, RPC, FOLLOW_RPC_in_rpc_block2378);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    RPC146_tree = (Object) adaptor.create(RPC146);
                    adaptor.addChild(root_0, RPC146_tree);
                }
                n = (Token) match(input, ID, FOLLOW_ID_in_rpc_block2382);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    n_tree = (Object) adaptor.create(n);
                    adaptor.addChild(root_0, n_tree);
                }
                LEFTPAREN147 = (Token) match(input, LEFTPAREN, FOLLOW_LEFTPAREN_in_rpc_block2384);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    LEFTPAREN147_tree = (Object) adaptor.create(LEFTPAREN147);
                    adaptor.addChild(root_0, LEFTPAREN147_tree);
                }
                // io/protostuff/parser/ProtoParser.g:586:28: (ap= FULL_ID | a= ( VOID | ID ) )
                int alt30 = 2;
                switch (input.LA(1))
                {
                    case FULL_ID:
                    {
                        alt30 = 1;
                    }
                        break;
                    case VOID:
                    case ID:
                    {
                        alt30 = 2;
                    }
                        break;
                    default:
                        if (state.backtracking > 0)
                        {
                            state.failed = true;
                            return retval;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 30, 0, input);

                        throw nvae;
                }

                switch (alt30)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:586:29: ap= FULL_ID
                    {
                        ap = (Token) match(input, FULL_ID, FOLLOW_FULL_ID_in_rpc_block2389);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            ap_tree = (Object) adaptor.create(ap);
                            adaptor.addChild(root_0, ap_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            String argFull = (ap != null ? ap.getText() : null);
                            int lastDot = argFull.lastIndexOf('.');
                            argPackage = argFull.substring(0, lastDot);
                            argName = argFull.substring(lastDot + 1);

                        }

                    }
                        break;
                    case 2:
                    // io/protostuff/parser/ProtoParser.g:591:13: a= ( VOID | ID )
                    {
                        a = (Token) input.LT(1);
                        if ((input.LA(1) >= VOID && input.LA(1) <= ID))
                        {
                            input.consume();
                            if (state.backtracking == 0)
                                adaptor.addChild(root_0, (Object) adaptor.create(a));
                            state.errorRecovery = false;
                            state.failed = false;
                        }
                        else
                        {
                            if (state.backtracking > 0)
                            {
                                state.failed = true;
                                return retval;
                            }
                            MismatchedSetException mse = new MismatchedSetException(null, input);
                            throw mse;
                        }

                        if (state.backtracking == 0)
                        {
                            argName = (a != null ? a.getText() : null);
                        }

                    }
                        break;

                }

                RIGHTPAREN148 = (Token) match(input, RIGHTPAREN, FOLLOW_RIGHTPAREN_in_rpc_block2406);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    RIGHTPAREN148_tree = (Object) adaptor.create(RIGHTPAREN148);
                    adaptor.addChild(root_0, RIGHTPAREN148_tree);
                }
                RETURNS149 = (Token) match(input, RETURNS, FOLLOW_RETURNS_in_rpc_block2417);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    RETURNS149_tree = (Object) adaptor.create(RETURNS149);
                    adaptor.addChild(root_0, RETURNS149_tree);
                }
                LEFTPAREN150 = (Token) match(input, LEFTPAREN, FOLLOW_LEFTPAREN_in_rpc_block2419);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    LEFTPAREN150_tree = (Object) adaptor.create(LEFTPAREN150);
                    adaptor.addChild(root_0, LEFTPAREN150_tree);
                }
                // io/protostuff/parser/ProtoParser.g:592:27: (rp= FULL_ID | r= ( VOID | ID ) )
                int alt31 = 2;
                switch (input.LA(1))
                {
                    case FULL_ID:
                    {
                        alt31 = 1;
                    }
                        break;
                    case VOID:
                    case ID:
                    {
                        alt31 = 2;
                    }
                        break;
                    default:
                        if (state.backtracking > 0)
                        {
                            state.failed = true;
                            return retval;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 31, 0, input);

                        throw nvae;
                }

                switch (alt31)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:592:28: rp= FULL_ID
                    {
                        rp = (Token) match(input, FULL_ID, FOLLOW_FULL_ID_in_rpc_block2424);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            rp_tree = (Object) adaptor.create(rp);
                            adaptor.addChild(root_0, rp_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            String retFull = (rp != null ? rp.getText() : null);
                            int lastDot = retFull.lastIndexOf('.');
                            retPackage = retFull.substring(0, lastDot);
                            retName = retFull.substring(lastDot + 1);

                        }

                    }
                        break;
                    case 2:
                    // io/protostuff/parser/ProtoParser.g:597:13: r= ( VOID | ID )
                    {
                        r = (Token) input.LT(1);
                        if ((input.LA(1) >= VOID && input.LA(1) <= ID))
                        {
                            input.consume();
                            if (state.backtracking == 0)
                                adaptor.addChild(root_0, (Object) adaptor.create(r));
                            state.errorRecovery = false;
                            state.failed = false;
                        }
                        else
                        {
                            if (state.backtracking > 0)
                            {
                                state.failed = true;
                                return retval;
                            }
                            MismatchedSetException mse = new MismatchedSetException(null, input);
                            throw mse;
                        }

                        if (state.backtracking == 0)
                        {
                            retName = (r != null ? r.getText() : null);
                        }

                    }
                        break;

                }

                RIGHTPAREN151 = (Token) match(input, RIGHTPAREN, FOLLOW_RIGHTPAREN_in_rpc_block2441);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    RIGHTPAREN151_tree = (Object) adaptor.create(RIGHTPAREN151);
                    adaptor.addChild(root_0, RIGHTPAREN151_tree);
                }
                if (state.backtracking == 0)
                {

                    rm = service.addRpcMethod((n != null ? n.getText() : null), argName, argPackage, retName,
                            retPackage);
                    proto.addAnnotationsTo(rm);

                }
                // io/protostuff/parser/ProtoParser.g:600:11: ( rpc_body_block[proto, rm] )?
                int alt32 = 2;
                switch (input.LA(1))
                {
                    case LEFTCURLY:
                    {
                        alt32 = 1;
                    }
                        break;
                }

                switch (alt32)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:600:11: rpc_body_block[proto, rm]
                    {
                        pushFollow(FOLLOW_rpc_body_block_in_rpc_block2445);
                        rpc_body_block152 = rpc_body_block(proto, rm);

                        state._fsp--;
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                            adaptor.addChild(root_0, rpc_body_block152.getTree());

                    }
                        break;

                }

                SEMICOLON153 = (Token) match(input, SEMICOLON, FOLLOW_SEMICOLON_in_rpc_block2449);
                if (state.failed)
                    return retval;

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "rpc_block"

    public static class rpc_body_block_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "rpc_body_block"
    // io/protostuff/parser/ProtoParser.g:603:1: rpc_body_block[Proto proto, Service.RpcMethod rm] :
    // LEFTCURLY ( option_entry[proto, rm] )* RIGHTCURLY ;
    public final ProtoParser.rpc_body_block_return rpc_body_block(Proto proto, Service.RpcMethod rm)
            throws RecognitionException
    {
        ProtoParser.rpc_body_block_return retval = new ProtoParser.rpc_body_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LEFTCURLY154 = null;
        Token RIGHTCURLY156 = null;
        ProtoParser.option_entry_return option_entry155 = null;

        Object LEFTCURLY154_tree = null;
        Object RIGHTCURLY156_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:604:5: ( LEFTCURLY ( option_entry[proto, rm] )* RIGHTCURLY
            // )
            // io/protostuff/parser/ProtoParser.g:604:9: LEFTCURLY ( option_entry[proto, rm] )* RIGHTCURLY
            {
                root_0 = (Object) adaptor.nil();

                LEFTCURLY154 = (Token) match(input, LEFTCURLY, FOLLOW_LEFTCURLY_in_rpc_body_block2475);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    LEFTCURLY154_tree = (Object) adaptor.create(LEFTCURLY154);
                    adaptor.addChild(root_0, LEFTCURLY154_tree);
                }
                // io/protostuff/parser/ProtoParser.g:604:19: ( option_entry[proto, rm] )*
                loop33: do
                {
                    int alt33 = 2;
                    switch (input.LA(1))
                    {
                        case OPTION:
                        {
                            alt33 = 1;
                        }
                            break;

                    }

                    switch (alt33)
                    {
                        case 1:
                        // io/protostuff/parser/ProtoParser.g:604:19: option_entry[proto, rm]
                        {
                            pushFollow(FOLLOW_option_entry_in_rpc_body_block2477);
                            option_entry155 = option_entry(proto, rm);

                            state._fsp--;
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                                adaptor.addChild(root_0, option_entry155.getTree());

                        }
                            break;

                        default:
                            break loop33;
                    }
                } while (true);

                RIGHTCURLY156 = (Token) match(input, RIGHTCURLY, FOLLOW_RIGHTCURLY_in_rpc_body_block2481);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    RIGHTCURLY156_tree = (Object) adaptor.create(RIGHTCURLY156);
                    adaptor.addChild(root_0, RIGHTCURLY156_tree);
                }
                if (state.backtracking == 0)
                {

                    if (!proto.annotations.isEmpty())
                        throw new IllegalStateException("Misplaced annotations: " + proto.annotations);

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "rpc_body_block"

    public static class extend_block_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "extend_block"
    // io/protostuff/parser/ProtoParser.g:610:1: extend_block[Proto proto, Message parent] : EXTEND (
    // FULL_ID | ID ) LEFTCURLY ( extend_body[proto, extension] )* RIGHTCURLY ( ( SEMICOLON )? ) ;
    public final ProtoParser.extend_block_return extend_block(Proto proto, Message parent) throws RecognitionException
    {
        ProtoParser.extend_block_return retval = new ProtoParser.extend_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token EXTEND157 = null;
        Token FULL_ID158 = null;
        Token ID159 = null;
        Token LEFTCURLY160 = null;
        Token RIGHTCURLY162 = null;
        Token SEMICOLON163 = null;
        ProtoParser.extend_body_return extend_body161 = null;

        Object EXTEND157_tree = null;
        Object FULL_ID158_tree = null;
        Object ID159_tree = null;
        Object LEFTCURLY160_tree = null;
        Object RIGHTCURLY162_tree = null;
        Object SEMICOLON163_tree = null;

        Extension extension = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:614:5: ( EXTEND ( FULL_ID | ID ) LEFTCURLY (
            // extend_body[proto, extension] )* RIGHTCURLY ( ( SEMICOLON )? ) )
            // io/protostuff/parser/ProtoParser.g:614:9: EXTEND ( FULL_ID | ID ) LEFTCURLY (
            // extend_body[proto, extension] )* RIGHTCURLY ( ( SEMICOLON )? )
            {
                root_0 = (Object) adaptor.nil();

                EXTEND157 = (Token) match(input, EXTEND, FOLLOW_EXTEND_in_extend_block2513);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    EXTEND157_tree = (Object) adaptor.create(EXTEND157);
                    adaptor.addChild(root_0, EXTEND157_tree);
                }
                // io/protostuff/parser/ProtoParser.g:614:16: ( FULL_ID | ID )
                int alt34 = 2;
                switch (input.LA(1))
                {
                    case FULL_ID:
                    {
                        alt34 = 1;
                    }
                        break;
                    case ID:
                    {
                        alt34 = 2;
                    }
                        break;
                    default:
                        if (state.backtracking > 0)
                        {
                            state.failed = true;
                            return retval;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 34, 0, input);

                        throw nvae;
                }

                switch (alt34)
                {
                    case 1:
                    // io/protostuff/parser/ProtoParser.g:615:9: FULL_ID
                    {
                        FULL_ID158 = (Token) match(input, FULL_ID, FOLLOW_FULL_ID_in_extend_block2525);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            FULL_ID158_tree = (Object) adaptor.create(FULL_ID158);
                            adaptor.addChild(root_0, FULL_ID158_tree);
                        }
                        if (state.backtracking == 0)
                        {

                            String fullType = (FULL_ID158 != null ? FULL_ID158.getText() : null);
                            int lastDot = fullType.lastIndexOf('.');
                            String packageName = fullType.substring(0, lastDot);
                            String type = fullType.substring(lastDot + 1);
                            extension = new Extension(proto, parent, packageName, type);

                        }

                    }
                        break;
                    case 2:
                    // io/protostuff/parser/ProtoParser.g:621:13: ID
                    {
                        ID159 = (Token) match(input, ID, FOLLOW_ID_in_extend_block2531);
                        if (state.failed)
                            return retval;
                        if (state.backtracking == 0)
                        {
                            ID159_tree = (Object) adaptor.create(ID159);
                            adaptor.addChild(root_0, ID159_tree);
                        }
                        if (state.backtracking == 0)
                        {
                            extension = new Extension(proto, parent, null, (ID159 != null ? ID159.getText() : null));
                        }

                    }
                        break;

                }

                if (state.backtracking == 0)
                {

                    if (parent == null)
                        proto.addExtension(extension);
                    else
                        parent.addNestedExtension(extension);

                    proto.addAnnotationsTo(extension);

                }
                LEFTCURLY160 = (Token) match(input, LEFTCURLY, FOLLOW_LEFTCURLY_in_extend_block2547);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    LEFTCURLY160_tree = (Object) adaptor.create(LEFTCURLY160);
                    adaptor.addChild(root_0, LEFTCURLY160_tree);
                }
                // io/protostuff/parser/ProtoParser.g:629:19: ( extend_body[proto, extension] )*
                loop35: do
                {
                    int alt35 = 2;
                    switch (input.LA(1))
                    {
                        case AT:
                        case REQUIRED:
                        case OPTIONAL:
                        case REPEATED:
                        {
                            alt35 = 1;
                        }
                            break;

                    }

                    switch (alt35)
                    {
                        case 1:
                        // io/protostuff/parser/ProtoParser.g:629:20: extend_body[proto, extension]
                        {
                            pushFollow(FOLLOW_extend_body_in_extend_block2550);
                            extend_body161 = extend_body(proto, extension);

                            state._fsp--;
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                                adaptor.addChild(root_0, extend_body161.getTree());

                        }
                            break;

                        default:
                            break loop35;
                    }
                } while (true);

                RIGHTCURLY162 = (Token) match(input, RIGHTCURLY, FOLLOW_RIGHTCURLY_in_extend_block2555);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    RIGHTCURLY162_tree = (Object) adaptor.create(RIGHTCURLY162);
                    adaptor.addChild(root_0, RIGHTCURLY162_tree);
                }
                if (state.backtracking == 0)
                {

                    if (!proto.annotations.isEmpty())
                        throw new IllegalStateException("Misplaced annotations: " + proto.annotations);

                }
                // io/protostuff/parser/ProtoParser.g:633:11: ( ( SEMICOLON )? )
                // io/protostuff/parser/ProtoParser.g:633:12: ( SEMICOLON )?
                {
                    // io/protostuff/parser/ProtoParser.g:633:12: ( SEMICOLON )?
                    int alt36 = 2;
                    switch (input.LA(1))
                    {
                        case SEMICOLON:
                        {
                            alt36 = 1;
                        }
                            break;
                    }

                    switch (alt36)
                    {
                        case 1:
                        // io/protostuff/parser/ProtoParser.g:633:12: SEMICOLON
                        {
                            SEMICOLON163 = (Token) match(input, SEMICOLON, FOLLOW_SEMICOLON_in_extend_block2560);
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                            {
                                SEMICOLON163_tree = (Object) adaptor.create(SEMICOLON163);
                                adaptor.addChild(root_0, SEMICOLON163_tree);
                            }

                        }
                            break;

                    }

                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "extend_block"

    public static class extend_body_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "extend_body"
    // io/protostuff/parser/ProtoParser.g:636:1: extend_body[Proto proto, Extension extension] : (
    // message_field[proto, extension] | annotation_entry[proto] );
    public final ProtoParser.extend_body_return extend_body(Proto proto, Extension extension)
            throws RecognitionException
    {
        ProtoParser.extend_body_return retval = new ProtoParser.extend_body_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        ProtoParser.message_field_return message_field164 = null;

        ProtoParser.annotation_entry_return annotation_entry165 = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:637:5: ( message_field[proto, extension] |
            // annotation_entry[proto] )
            int alt37 = 2;
            switch (input.LA(1))
            {
                case REQUIRED:
                case OPTIONAL:
                case REPEATED:
                {
                    alt37 = 1;
                }
                    break;
                case AT:
                {
                    alt37 = 2;
                }
                    break;
                default:
                    if (state.backtracking > 0)
                    {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 37, 0, input);

                    throw nvae;
            }

            switch (alt37)
            {
                case 1:
                // io/protostuff/parser/ProtoParser.g:637:9: message_field[proto, extension]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_message_field_in_extend_body2588);
                    message_field164 = message_field(proto, extension);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, message_field164.getTree());

                }
                    break;
                case 2:
                // io/protostuff/parser/ProtoParser.g:638:9: annotation_entry[proto]
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_annotation_entry_in_extend_body2599);
                    annotation_entry165 = annotation_entry(proto);

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, annotation_entry165.getTree());

                }
                    break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "extend_body"

    public static class ignore_block_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "ignore_block"
    // io/protostuff/parser/ProtoParser.g:641:1: ignore_block : LEFTCURLY ( ignore_block_body )* RIGHTCURLY
    // ;
    public final ProtoParser.ignore_block_return ignore_block() throws RecognitionException
    {
        ProtoParser.ignore_block_return retval = new ProtoParser.ignore_block_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LEFTCURLY166 = null;
        Token RIGHTCURLY168 = null;
        ProtoParser.ignore_block_body_return ignore_block_body167 = null;

        Object LEFTCURLY166_tree = null;
        Object RIGHTCURLY168_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:642:5: ( LEFTCURLY ( ignore_block_body )* RIGHTCURLY )
            // io/protostuff/parser/ProtoParser.g:642:9: LEFTCURLY ( ignore_block_body )* RIGHTCURLY
            {
                root_0 = (Object) adaptor.nil();

                LEFTCURLY166 = (Token) match(input, LEFTCURLY, FOLLOW_LEFTCURLY_in_ignore_block2623);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    LEFTCURLY166_tree = (Object) adaptor.create(LEFTCURLY166);
                    adaptor.addChild(root_0, LEFTCURLY166_tree);
                }
                // io/protostuff/parser/ProtoParser.g:642:19: ( ignore_block_body )*
                loop38: do
                {
                    int alt38 = 2;
                    switch (input.LA(1))
                    {
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
                        case COMMENT:
                        case WS:
                        case ESC_SEQ:
                        case STRING_LITERAL:
                        case UNICODE_ESC:
                        case OCTAL_ESC:
                        {
                            alt38 = 1;
                        }
                            break;

                    }

                    switch (alt38)
                    {
                        case 1:
                        // io/protostuff/parser/ProtoParser.g:642:19: ignore_block_body
                        {
                            pushFollow(FOLLOW_ignore_block_body_in_ignore_block2625);
                            ignore_block_body167 = ignore_block_body();

                            state._fsp--;
                            if (state.failed)
                                return retval;
                            if (state.backtracking == 0)
                                adaptor.addChild(root_0, ignore_block_body167.getTree());

                        }
                            break;

                        default:
                            break loop38;
                    }
                } while (true);

                RIGHTCURLY168 = (Token) match(input, RIGHTCURLY, FOLLOW_RIGHTCURLY_in_ignore_block2628);
                if (state.failed)
                    return retval;
                if (state.backtracking == 0)
                {
                    RIGHTCURLY168_tree = (Object) adaptor.create(RIGHTCURLY168);
                    adaptor.addChild(root_0, RIGHTCURLY168_tree);
                }

            }

            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "ignore_block"

    public static class ignore_block_body_return extends ParserRuleReturnScope
    {
        Object tree;

        @Override
        public Object getTree()
        {
            return tree;
        }
    }

    ;

    // $ANTLR start "ignore_block_body"
    // io/protostuff/parser/ProtoParser.g:645:1: ignore_block_body : ( ( LEFTCURLY )=> ignore_block | ~
    // RIGHTCURLY );
    public final ProtoParser.ignore_block_body_return ignore_block_body() throws RecognitionException
    {
        ProtoParser.ignore_block_body_return retval = new ProtoParser.ignore_block_body_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set170 = null;
        ProtoParser.ignore_block_return ignore_block169 = null;

        Object set170_tree = null;

        try
        {
            // io/protostuff/parser/ProtoParser.g:646:5: ( ( LEFTCURLY )=> ignore_block | ~ RIGHTCURLY )
            int alt39 = 2;
            switch (input.LA(1))
            {
                case LEFTCURLY:
                {
                    int LA39_1 = input.LA(2);

                    if ((synpred1_ProtoParser()))
                    {
                        alt39 = 1;
                    }
                    else if ((true))
                    {
                        alt39 = 2;
                    }
                    else
                    {
                        if (state.backtracking > 0)
                        {
                            state.failed = true;
                            return retval;
                        }
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
                case COMMENT:
                case WS:
                case ESC_SEQ:
                case STRING_LITERAL:
                case UNICODE_ESC:
                case OCTAL_ESC:
                {
                    alt39 = 2;
                }
                    break;
                default:
                    if (state.backtracking > 0)
                    {
                        state.failed = true;
                        return retval;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 39, 0, input);

                    throw nvae;
            }

            switch (alt39)
            {
                case 1:
                // io/protostuff/parser/ProtoParser.g:646:9: ( LEFTCURLY )=> ignore_block
                {
                    root_0 = (Object) adaptor.nil();

                    pushFollow(FOLLOW_ignore_block_in_ignore_block_body2656);
                    ignore_block169 = ignore_block();

                    state._fsp--;
                    if (state.failed)
                        return retval;
                    if (state.backtracking == 0)
                        adaptor.addChild(root_0, ignore_block169.getTree());

                }
                    break;
                case 2:
                // io/protostuff/parser/ProtoParser.g:647:9: ~ RIGHTCURLY
                {
                    root_0 = (Object) adaptor.nil();

                    set170 = (Token) input.LT(1);
                    if ((input.LA(1) >= ASSIGN && input.LA(1) <= LEFTCURLY)
                            || (input.LA(1) >= LEFTPAREN && input.LA(1) <= OCTAL_ESC))
                    {
                        input.consume();
                        if (state.backtracking == 0)
                            adaptor.addChild(root_0, (Object) adaptor.create(set170));
                        state.errorRecovery = false;
                        state.failed = false;
                    }
                    else
                    {
                        if (state.backtracking > 0)
                        {
                            state.failed = true;
                            return retval;
                        }
                        MismatchedSetException mse = new MismatchedSetException(null, input);
                        throw mse;
                    }

                }
                    break;

            }
            retval.stop = input.LT(-1);

            if (state.backtracking == 0)
            {

                retval.tree = (Object) adaptor.rulePostProcessing(root_0);
                adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
            }
        }
        catch (RecognitionException re)
        {
            reportError(re);
            recover(input, re);
            retval.tree = (Object) adaptor.errorNode(input, retval.start, input.LT(-1), re);

        }
        finally
        {
        }
        return retval;
    }

    // $ANTLR end "ignore_block_body"

    // $ANTLR start synpred1_ProtoParser
    public final void synpred1_ProtoParser_fragment() throws RecognitionException
    {
        // io/protostuff/parser/ProtoParser.g:646:9: ( LEFTCURLY )
        // io/protostuff/parser/ProtoParser.g:646:10: LEFTCURLY
        {
            match(input, LEFTCURLY, FOLLOW_LEFTCURLY_in_synpred1_ProtoParser2652);
            if (state.failed)
                return;

        }
    }

    // $ANTLR end synpred1_ProtoParser

    // Delegated rules

    public final boolean synpred1_ProtoParser()
    {
        state.backtracking++;
        int start = input.mark();
        try
        {
            synpred1_ProtoParser_fragment(); // can never throw exception
        }
        catch (RecognitionException re)
        {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public static final BitSet FOLLOW_statement_in_parse178 = new BitSet(new long[] { 0x0000000043F80020L });
    public static final BitSet FOLLOW_EOF_in_parse183 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_header_syntax_in_statement211 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_header_package_in_statement222 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_header_import_in_statement233 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_message_block_in_statement244 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_enum_block_in_statement255 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_extend_block_in_statement266 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_service_block_in_statement277 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_annotation_entry_in_statement288 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_option_entry_in_statement299 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_set_in_var_reserved0 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ID_in_var509 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_var_reserved_in_var513 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_FULL_ID_in_var_full532 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_var_in_var_full536 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_AT_in_annotation_entry562 = new BitSet(new long[] { 0x001FFFFFFFF90000L });
    public static final BitSet FOLLOW_var_in_annotation_entry564 = new BitSet(new long[] { 0x0000000000000102L });
    public static final BitSet FOLLOW_LEFTPAREN_in_annotation_entry577 = new BitSet(new long[] { 0x003FFFFFFFF90000L });
    public static final BitSet FOLLOW_annotation_keyval_in_annotation_entry588 = new BitSet(
            new long[] { 0x0000000000002200L });
    public static final BitSet FOLLOW_COMMA_in_annotation_entry592 = new BitSet(new long[] { 0x003FFFFFFFF90000L });
    public static final BitSet FOLLOW_annotation_keyval_in_annotation_entry594 = new BitSet(
            new long[] { 0x0000000000002200L });
    public static final BitSet FOLLOW_RIGHTPAREN_in_annotation_entry608 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_var_full_in_annotation_keyval635 = new BitSet(new long[] { 0x0000000000000010L });
    public static final BitSet FOLLOW_ASSIGN_in_annotation_keyval637 = new BitSet(new long[] { 0x037FFFFFFFFF0000L,
            0x0000000000000001L });
    public static final BitSet FOLLOW_var_reserved_in_annotation_keyval659 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ID_in_annotation_keyval679 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_FULL_ID_in_annotation_keyval701 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_NUMFLOAT_in_annotation_keyval721 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_NUMINT_in_annotation_keyval741 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_NUMDOUBLE_in_annotation_keyval761 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_TRUE_in_annotation_keyval781 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_FALSE_in_annotation_keyval801 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_STRING_LITERAL_in_annotation_keyval821 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_SYNTAX_in_header_syntax854 = new BitSet(new long[] { 0x0000000000000010L });
    public static final BitSet FOLLOW_ASSIGN_in_header_syntax856 = new BitSet(new long[] { 0x0000000000000000L,
            0x0000000000000001L });
    public static final BitSet FOLLOW_STRING_LITERAL_in_header_syntax858 = new BitSet(
            new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_SEMICOLON_in_header_syntax860 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_PKG_in_header_package889 = new BitSet(new long[] { 0x003FFFFFFFF90000L });
    public static final BitSet FOLLOW_FULL_ID_in_header_package892 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_var_in_header_package898 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_SEMICOLON_in_header_package903 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_IMPORT_in_header_import931 = new BitSet(new long[] { 0x0000000000000000L,
            0x0000000000000001L });
    public static final BitSet FOLLOW_STRING_LITERAL_in_header_import933 = new BitSet(
            new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_SEMICOLON_in_header_import935 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_OPTION_in_option_entry959 = new BitSet(new long[] { 0x003FFFFFFFF90100L });
    public static final BitSet FOLLOW_LEFTPAREN_in_option_entry961 = new BitSet(new long[] { 0x003FFFFFFFF90000L });
    public static final BitSet FOLLOW_var_full_in_option_entry966 = new BitSet(new long[] { 0x0000000000000210L });
    public static final BitSet FOLLOW_RIGHTPAREN_in_option_entry968 = new BitSet(new long[] { 0x0000000000000010L });
    public static final BitSet FOLLOW_ASSIGN_in_option_entry971 = new BitSet(new long[] { 0x037FFFFFFFFF0000L,
            0x0000000000000001L });
    public static final BitSet FOLLOW_var_reserved_in_option_entry993 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_ID_in_option_entry1015 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_FULL_ID_in_option_entry1037 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_NUMFLOAT_in_option_entry1057 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_NUMINT_in_option_entry1077 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_NUMDOUBLE_in_option_entry1097 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_TRUE_in_option_entry1117 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_FALSE_in_option_entry1137 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_STRING_LITERAL_in_option_entry1157 = new BitSet(
            new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_SEMICOLON_in_option_entry1171 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_MESSAGE_in_message_block1204 = new BitSet(new long[] { 0x0010000000000000L });
    public static final BitSet FOLLOW_ID_in_message_block1206 = new BitSet(new long[] { 0x0000000000000040L });
    public static final BitSet FOLLOW_LEFTCURLY_in_message_block1219 = new BitSet(new long[] { 0x000000007FF800A0L });
    public static final BitSet FOLLOW_message_body_in_message_block1222 = new BitSet(new long[] { 0x000000007FF800A0L });
    public static final BitSet FOLLOW_RIGHTCURLY_in_message_block1227 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_message_block_in_message_body1250 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_message_field_in_message_body1261 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_enum_block_in_message_body1272 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_service_block_in_message_body1283 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_extend_block_in_message_body1294 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_extensions_range_in_message_body1305 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_annotation_entry_in_message_body1316 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_option_entry_in_message_body1327 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_EXTENSIONS_in_extensions_range1358 = new BitSet(
            new long[] { 0x0040000000000000L });
    public static final BitSet FOLLOW_NUMINT_in_extensions_range1362 = new BitSet(new long[] { 0x0000000000011000L });
    public static final BitSet FOLLOW_TO_in_extensions_range1376 = new BitSet(new long[] { 0x0044000000000000L });
    public static final BitSet FOLLOW_NUMINT_in_extensions_range1382 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_MAX_in_extensions_range1388 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_SEMICOLON_in_extensions_range1405 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_OPTIONAL_in_message_field1439 = new BitSet(new long[] { 0x0031FFFC80000000L });
    public static final BitSet FOLLOW_REQUIRED_in_message_field1456 = new BitSet(new long[] { 0x0031FFFC80000000L });
    public static final BitSet FOLLOW_REPEATED_in_message_field1473 = new BitSet(new long[] { 0x0031FFFC80000000L });
    public static final BitSet FOLLOW_field_type_in_message_field1488 = new BitSet(new long[] { 0x001FFFFFFFF90000L });
    public static final BitSet FOLLOW_var_in_message_field1500 = new BitSet(new long[] { 0x0000000000000010L });
    public static final BitSet FOLLOW_ASSIGN_in_message_field1502 = new BitSet(new long[] { 0x0040000000000000L });
    public static final BitSet FOLLOW_NUMINT_in_message_field1504 = new BitSet(new long[] { 0x0000000000001440L });
    public static final BitSet FOLLOW_field_options_in_message_field1518 = new BitSet(
            new long[] { 0x0000000000001440L });
    public static final BitSet FOLLOW_SEMICOLON_in_message_field1534 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ignore_block_in_message_field1539 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_INT32_in_field_type1565 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_UINT32_in_field_type1577 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_SINT32_in_field_type1589 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_FIXED32_in_field_type1601 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_SFIXED32_in_field_type1613 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_INT64_in_field_type1625 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_UINT64_in_field_type1637 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_SINT64_in_field_type1649 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_FIXED64_in_field_type1661 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_SFIXED64_in_field_type1673 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_FLOAT_in_field_type1685 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_DOUBLE_in_field_type1697 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_BOOL_in_field_type1709 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_STRING_in_field_type1721 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_BYTES_in_field_type1733 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_GROUP_in_field_type1745 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_FULL_ID_in_field_type1757 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ID_in_field_type1769 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_LEFTSQUARE_in_field_options1796 = new BitSet(new long[] { 0x003FFFFFFFF90000L });
    public static final BitSet FOLLOW_field_options_keyval_in_field_options1798 = new BitSet(
            new long[] { 0x0000000000002800L });
    public static final BitSet FOLLOW_COMMA_in_field_options1811 = new BitSet(new long[] { 0x003FFFFFFFF90000L });
    public static final BitSet FOLLOW_field_options_keyval_in_field_options1813 = new BitSet(
            new long[] { 0x0000000000002800L });
    public static final BitSet FOLLOW_RIGHTSQUARE_in_field_options1818 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_var_full_in_field_options_keyval1845 = new BitSet(
            new long[] { 0x0000000000000010L });
    public static final BitSet FOLLOW_ASSIGN_in_field_options_keyval1847 = new BitSet(new long[] { 0x1BFFFFFFFFFF8000L,
            0x0000000000000001L });
    public static final BitSet FOLLOW_var_reserved_in_field_options_keyval1852 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_STRING_LITERAL_in_field_options_keyval1865 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_NUMFLOAT_in_field_options_keyval1877 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_NUMINT_in_field_options_keyval1890 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_NUMDOUBLE_in_field_options_keyval1902 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_HEX_in_field_options_keyval1914 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_OCTAL_in_field_options_keyval1926 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_TRUE_in_field_options_keyval1938 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_FALSE_in_field_options_keyval1954 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ID_in_field_options_keyval1968 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_FULL_ID_in_field_options_keyval1980 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_EXP_in_field_options_keyval1992 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_signed_constant_in_field_options_keyval2004 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_MINUS_in_signed_constant2042 = new BitSet(new long[] { 0x0010000000000000L });
    public static final BitSet FOLLOW_ID_in_signed_constant2044 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ENUM_in_enum_block2076 = new BitSet(new long[] { 0x0010000000000000L });
    public static final BitSet FOLLOW_ID_in_enum_block2078 = new BitSet(new long[] { 0x0000000000000040L });
    public static final BitSet FOLLOW_LEFTCURLY_in_enum_block2091 = new BitSet(new long[] { 0x0010000043F800A0L });
    public static final BitSet FOLLOW_enum_body_in_enum_block2094 = new BitSet(new long[] { 0x0010000043F800A0L });
    public static final BitSet FOLLOW_RIGHTCURLY_in_enum_block2099 = new BitSet(new long[] { 0x0000000000001002L });
    public static final BitSet FOLLOW_SEMICOLON_in_enum_block2104 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_enum_field_in_enum_body2132 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_annotation_entry_in_enum_body2143 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_option_entry_in_enum_body2154 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ID_in_enum_field2181 = new BitSet(new long[] { 0x0000000000000010L });
    public static final BitSet FOLLOW_ASSIGN_in_enum_field2183 = new BitSet(new long[] { 0x0040000000000000L });
    public static final BitSet FOLLOW_NUMINT_in_enum_field2185 = new BitSet(new long[] { 0x0000000000001400L });
    public static final BitSet FOLLOW_enum_options_in_enum_field2190 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_SEMICOLON_in_enum_field2195 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_LEFTSQUARE_in_enum_options2218 = new BitSet(new long[] { 0x003FFFFFFFF90000L });
    public static final BitSet FOLLOW_field_options_keyval_in_enum_options2220 = new BitSet(
            new long[] { 0x0000000000002800L });
    public static final BitSet FOLLOW_COMMA_in_enum_options2233 = new BitSet(new long[] { 0x003FFFFFFFF90000L });
    public static final BitSet FOLLOW_field_options_keyval_in_enum_options2235 = new BitSet(
            new long[] { 0x0000000000002800L });
    public static final BitSet FOLLOW_RIGHTSQUARE_in_enum_options2240 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_SERVICE_in_service_block2270 = new BitSet(new long[] { 0x0010000000000000L });
    public static final BitSet FOLLOW_ID_in_service_block2272 = new BitSet(new long[] { 0x0000000000000040L });
    public static final BitSet FOLLOW_LEFTCURLY_in_service_block2276 = new BitSet(new long[] { 0x0000000143F80020L });
    public static final BitSet FOLLOW_service_body_in_service_block2287 = new BitSet(new long[] { 0x0000000143F800A0L });
    public static final BitSet FOLLOW_RIGHTCURLY_in_service_block2292 = new BitSet(new long[] { 0x0000000000001002L });
    public static final BitSet FOLLOW_SEMICOLON_in_service_block2295 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_rpc_block_in_service_body2325 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_annotation_entry_in_service_body2336 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_option_entry_in_service_body2347 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_RPC_in_rpc_block2378 = new BitSet(new long[] { 0x0010000000000000L });
    public static final BitSet FOLLOW_ID_in_rpc_block2382 = new BitSet(new long[] { 0x0000000000000100L });
    public static final BitSet FOLLOW_LEFTPAREN_in_rpc_block2384 = new BitSet(new long[] { 0x0038000000000000L });
    public static final BitSet FOLLOW_FULL_ID_in_rpc_block2389 = new BitSet(new long[] { 0x0000000000000200L });
    public static final BitSet FOLLOW_set_in_rpc_block2397 = new BitSet(new long[] { 0x0000000000000200L });
    public static final BitSet FOLLOW_RIGHTPAREN_in_rpc_block2406 = new BitSet(new long[] { 0x0000000200000000L });
    public static final BitSet FOLLOW_RETURNS_in_rpc_block2417 = new BitSet(new long[] { 0x0000000000000100L });
    public static final BitSet FOLLOW_LEFTPAREN_in_rpc_block2419 = new BitSet(new long[] { 0x0038000000000000L });
    public static final BitSet FOLLOW_FULL_ID_in_rpc_block2424 = new BitSet(new long[] { 0x0000000000000200L });
    public static final BitSet FOLLOW_set_in_rpc_block2432 = new BitSet(new long[] { 0x0000000000000200L });
    public static final BitSet FOLLOW_RIGHTPAREN_in_rpc_block2441 = new BitSet(new long[] { 0x0000000000001040L });
    public static final BitSet FOLLOW_rpc_body_block_in_rpc_block2445 = new BitSet(new long[] { 0x0000000000001000L });
    public static final BitSet FOLLOW_SEMICOLON_in_rpc_block2449 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_LEFTCURLY_in_rpc_body_block2475 = new BitSet(new long[] { 0x0000000043F800A0L });
    public static final BitSet FOLLOW_option_entry_in_rpc_body_block2477 = new BitSet(
            new long[] { 0x0000000043F800A0L });
    public static final BitSet FOLLOW_RIGHTCURLY_in_rpc_body_block2481 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_EXTEND_in_extend_block2513 = new BitSet(new long[] { 0x0030000000000000L });
    public static final BitSet FOLLOW_FULL_ID_in_extend_block2525 = new BitSet(new long[] { 0x0000000000000040L });
    public static final BitSet FOLLOW_ID_in_extend_block2531 = new BitSet(new long[] { 0x0000000000000040L });
    public static final BitSet FOLLOW_LEFTCURLY_in_extend_block2547 = new BitSet(new long[] { 0x000000001C0000A0L });
    public static final BitSet FOLLOW_extend_body_in_extend_block2550 = new BitSet(new long[] { 0x000000001C0000A0L });
    public static final BitSet FOLLOW_RIGHTCURLY_in_extend_block2555 = new BitSet(new long[] { 0x0000000000001002L });
    public static final BitSet FOLLOW_SEMICOLON_in_extend_block2560 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_message_field_in_extend_body2588 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_annotation_entry_in_extend_body2599 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_LEFTCURLY_in_ignore_block2623 = new BitSet(new long[] { 0xFFFFFFFFFFFFFFF0L,
            0x0000000000000007L });
    public static final BitSet FOLLOW_ignore_block_body_in_ignore_block2625 = new BitSet(new long[] {
            0xFFFFFFFFFFFFFFF0L, 0x0000000000000007L });
    public static final BitSet FOLLOW_RIGHTCURLY_in_ignore_block2628 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_ignore_block_in_ignore_block_body2656 = new BitSet(
            new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_set_in_ignore_block_body2666 = new BitSet(new long[] { 0x0000000000000002L });
    public static final BitSet FOLLOW_LEFTCURLY_in_synpred1_ProtoParser2652 = new BitSet(
            new long[] { 0x0000000000000002L });

}