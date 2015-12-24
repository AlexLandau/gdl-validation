package net.alloyggp.griddle.indentation;

import java.util.ArrayList;
import java.util.List;

import java_cup.runtime.Symbol;
import net.alloyggp.griddle.generated.ParserHelper;
import net.alloyggp.griddle.generated.Symbols;
import net.alloyggp.griddle.generated.Symbols2;

public class GameIndenter {
    private GameIndenter() {
        //Not instantiable
    }

    //Returns null if the indentation fails.
    public static String reindentGameDescription(String gameRules) throws Exception {
        List<Symbol> tokens = ParserHelper.scan(gameRules, true);
        tokens = collapseWhitespaceTokens(tokens);

        StringBuilder result = new StringBuilder();
        int parensLevel = 0; // "( so far" minus ") so far"
        int nestingLevel = 0; // "<=" and "or" level; when == parens level, use newlines
        for (int i = 0; i < tokens.size(); i++) {
            Symbol token = tokens.get(i);
            switch (token.sym) {
            case Symbols.CONSTANT:
            case Symbols.VARIABLE:
                result.append(token.value);
                break;
            case Symbols.POPEN:
                result.append("(");
                parensLevel++;
                break;
            case Symbols.PCLOSE:
                result.append(")");
                if (parensLevel > 0) {
                    parensLevel--;
                }
                if (nestingLevel > parensLevel) {
                    nestingLevel = parensLevel;
                }
                break;
            case Symbols.IMPLIES:
                result.append("<=");
                nestingLevel++;
                break;
            case Symbols.NOT:
                result.append("not");
                break;
            case Symbols.DISTINCT:
                result.append("distinct");
                break;
            case Symbols.OR:
                result.append("or");
                nestingLevel++;
                break;
            case Symbols.EOF:
                break;
            case Symbols2.COMMENT:
                result.append(token.value);
                break;
            case Symbols2.WHITESPACE:
                result.append(rewriteWhitespace((String) token.value, tokens, i,
                        parensLevel, nestingLevel));
                break;
            }
        }
        return result.toString();
    }

    private static List<Symbol> collapseWhitespaceTokens(List<Symbol> tokens) {
        List<Symbol> result = new ArrayList<Symbol>();
        Symbol lastAdded = null;
        for (Symbol symbol : tokens) {
            if (lastAdded != null && lastAdded.sym == Symbols2.WHITESPACE
                    && symbol.sym == Symbols2.WHITESPACE) {
                Symbol merged = new Symbol(Symbols2.WHITESPACE, lastAdded, symbol,
                        lastAdded.value.toString() + symbol.value.toString());
                result.remove(result.size() - 1);
                result.add(merged);
                lastAdded = merged;
            } else {
                result.add(symbol);
                lastAdded = symbol;
            }
        }
        return result;
    }

    private static String rewriteWhitespace(String whitespace, List<Symbol> tokens,
            int i, int parensLevel, int nestingLevel) {
        if (i == 0) {
            if (tokens.size() == 1) {
                return whitespace;
            }
            int followingType = tokens.get(1).sym;
            if (followingType == Symbols.POPEN || followingType == Symbols.CONSTANT) {
                return removeNonNewlineWhitespace(whitespace);
            }
            return whitespace;
        } else if (i == tokens.size() - 1) {
            //Shouldn't happen (EOF should be there) but just in case...
            return whitespace;
        }
        int precedingType = tokens.get(i - 1).sym;
        int followingType = tokens.get(i + 1).sym;

        if (precedingType == Symbols.POPEN && isWord(followingType)) {
            return "";
        } else if (isWord(precedingType) && followingType == Symbols.PCLOSE) {
            return "";
        } else if (precedingType == Symbols.PCLOSE && followingType == Symbols.PCLOSE) {
            return "";
        } else if ((isWord(precedingType) || precedingType == Symbols.PCLOSE)
                && (isWord(followingType) || followingType == Symbols.POPEN)) {
            if (precedingType == Symbols.IMPLIES || precedingType == Symbols.OR) {
                return " ";
            } else if (parensLevel == 0) {
                //Preserve extra newlines
                String removed = removeNonNewlineWhitespace(whitespace);
                if (removed.equals("")) {
                    return "\n";
                }
                return removed;
            } else if (parensLevel == nestingLevel) {
                return "\n" + nIndents(nestingLevel);
            } else {
                return " ";
            }
        } else if (parensLevel == 0 && precedingType == Symbols2.COMMENT &&
                (isWord(followingType) || followingType == Symbols.POPEN)) {
            return removeNonNewlineWhitespace(whitespace);
        } else if (parensLevel == 0 && followingType == Symbols2.COMMENT &&
                (isWord(precedingType) || precedingType == Symbols.PCLOSE)) {
            return removeNonNewlineWhitespace(whitespace);
        }

        return whitespace;
    }

    private static String removeNonNewlineWhitespace(String whitespace) {
        return whitespace.replaceAll("[^\r\n]", "");
    }

    private static String nIndents(int nestingLevel) {
        String result = "";
        for (int i = 0; i < nestingLevel; i++) {
            result += "    ";
        }
        return result;
    }

    private static boolean isWord(int symType) {
        return symType == Symbols.CONSTANT
                || symType == Symbols.VARIABLE
                || symType == Symbols.IMPLIES
                || symType == Symbols.NOT
                || symType == Symbols.OR
                || symType == Symbols.DISTINCT;
    }
}
