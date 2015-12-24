package net.alloyggp.griddle.grammar;

import net.alloyggp.griddle.Position;

/**
 * This visitor uses the Adapter pattern, so implementations only need to
 * implement the relevant calls.
 */
@SuppressWarnings("unused")
public abstract class GdlVisitor {
    public void visitConstant(String constant, Position position) {
        //Subclasses can override this to do something.
    }
    public void visitVariable(String variable, Position position) {
        //Subclasses can override this to do something.
    }
    public void visitFunction(Function function) {
        //Subclasses can override this to do something.
    }
    public void visitRule(Rule rule) {
        //Subclasses can override this to do something.
    }
    public void visitLiteral(Literal literal) {
        //Subclasses can override this to do something.
    }
    public void visitSentence(Sentence sentence) {
        //Subclasses can override this to do something.
    }
    public void visitNegation(Literal negation) {
        //Subclasses can override this to do something.
    }
    public void visitDistinct(Literal distinct) {
        //Subclasses can override this to do something.
    }
    public void visitDisjunction(Literal disjunction) {
        //Subclasses can override this to do something.
    }
    public void visitErrorString(String errorString, Position position) {
        //Subclasses can override this to do something.
    }
}
