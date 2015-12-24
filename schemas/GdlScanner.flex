package net.alloyggp.griddle.generated;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java_cup.runtime.Symbol;

//Still working on this!
%%

%class GdlScanner
%apiprivate
%unicode
%cupsym Symbols
%cup
%char
%line
%column
%caseless

%{
    private boolean includeCommentsAndWhitespace;
    
    public void setIncludeCommentsAndWhitespace(boolean newValue) {
        this.includeCommentsAndWhitespace = newValue;
    }
    
    private Symbol symbol(int type) {
        int length = zzMarkedPos - zzStartRead;
        return new ComplexSymbolFactory.ComplexSymbol("", type,
                new Location(yyline, yycolumn, yychar),
                new Location(yyline, yycolumn + length, yychar + length));
    }
    private Symbol symbol(int type, Object value) {
        int length = zzMarkedPos - zzStartRead;
        return new ComplexSymbolFactory.ComplexSymbol("", type,
                new Location(yyline, yycolumn, yychar),
                new Location(yyline, yycolumn + length, yychar + length), value);
    }
%}

%eofval{
    return new java_cup.runtime.ComplexSymbolFactory.ComplexSymbol("", Symbols.EOF);
%eofval}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]
ConstantChar   = [^ \r\n\t\f()]
ConstantCharStart = [^ \r\n\t\f()?;]

Comment = ";" {InputCharacter}* {LineTerminator}?

Variable = "?" {ConstantChar}+
Constant = {ConstantCharStart} {ConstantChar}*

%%

//TODO: I think I can put this in a category of sorts
<YYINITIAL> "("         { return symbol(Symbols.POPEN); }
<YYINITIAL> ")"         { return symbol(Symbols.PCLOSE); }

<YYINITIAL> "<="        { return symbol(Symbols.IMPLIES); }

//The %caseless option makes this work regardless of case
<YYINITIAL> "distinct"  { return symbol(Symbols.DISTINCT); }
<YYINITIAL> "or"        { return symbol(Symbols.OR); }
<YYINITIAL> "not"       { return symbol(Symbols.NOT); }

<YYINITIAL> {Variable}  { return symbol(Symbols.VARIABLE, yytext()); }
<YYINITIAL> {Constant}  { return symbol(Symbols.CONSTANT, yytext()); }

<YYINITIAL> {Comment}   { if (includeCommentsAndWhitespace) {return symbol(Symbols2.COMMENT, yytext());} }
<YYINITIAL> {WhiteSpace} { if (includeCommentsAndWhitespace) {return symbol(Symbols2.WHITESPACE, yytext());} }
