package net.alloyggp.griddle.generated;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;
import net.alloyggp.griddle.grammar.TopLevelGdl;

/*
 * This class isn't generated, but mediates access to the generated code
 * (which is largely package-private) from other packages.
 */
public class ParserHelper {
    private ParserHelper() {
        //Not instantiable
    }

    /**
     * This consumes and closes the input.
     */
    @SuppressWarnings("unchecked")
    public static List<TopLevelGdl> parse(Reader input) throws Exception {
        try {
            Scanner lexer = new GdlScanner(input);
            SymbolFactory symbolFactory = new ComplexSymbolFactory();
            Symbol result = new GdlParser(lexer, symbolFactory).parse();
            return (List<TopLevelGdl>) result.value;
        } finally {
            input.close();
        }
    }

    /**
     * Note: The output is not thread-safe.
     */
    public static List<Symbol> scan(String input, boolean includeCommentsAndWhitespace) throws Exception {
        GdlScanner scanner = new GdlScanner(new StringReader(input));
        scanner.setIncludeCommentsAndWhitespace(includeCommentsAndWhitespace);
        List<Symbol> tokens = new ArrayList<Symbol>();
        while (true) {
            Symbol token = scanner.next_token();
            tokens.add(token);
            if (token.sym == Symbols.EOF) {
                break;
            }
        }
        return tokens;
    }
}
