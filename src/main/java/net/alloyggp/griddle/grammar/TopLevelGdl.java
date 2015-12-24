package net.alloyggp.griddle.grammar;

import java_cup.runtime.ComplexSymbolFactory.Location;
import net.alloyggp.griddle.Position;

public class TopLevelGdl implements GdlVisitable {
    //Exactly one of these is non-null.
    private final Sentence sentence;
    private final Rule rule;
    private final String errorString;

    private final Position position;

    private TopLevelGdl(Sentence sentence, Rule rule, String errorString, int left, int right, int line) {
        this.sentence = sentence;
        this.rule = rule;
        this.errorString = errorString;
        this.position = new Position(left, right, line);
    }

    public static TopLevelGdl create(Sentence sentence, Location left, Location right) {
        if (sentence == null) {
            throw new NullPointerException();
        }
        return new TopLevelGdl(sentence, null, null, left.getOffset(), right.getOffset(), left.getLine());
    }

    public static TopLevelGdl create(Rule rule, Location left, Location right) {
        if (rule == null) {
            throw new NullPointerException();
        }
        return new TopLevelGdl(null, rule, null, left.getOffset(), right.getOffset(), left.getLine());
    }

    public static TopLevelGdl createError(Object e, Location left, Location right) {
        if (e == null || e.toString() == null || e.toString().isEmpty()) {
            e = "unknown error";
        }
        return new TopLevelGdl(null, null, e.toString(), left.getOffset(), right.getOffset(), left.getLine());
    }

    public boolean isSentence() {
        return sentence != null;
    }

    public boolean isRule() {
        return rule != null;
    }

    public boolean isErrorString() {
        return errorString != null;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public Rule getRule() {
        return rule;
    }

    public String getErrorString() {
        return errorString;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((errorString == null) ? 0 : errorString.hashCode());
        result = prime * result
                + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((rule == null) ? 0 : rule.hashCode());
        result = prime * result
                + ((sentence == null) ? 0 : sentence.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TopLevelGdl other = (TopLevelGdl) obj;
        if (errorString == null) {
            if (other.errorString != null)
                return false;
        } else if (!errorString.equals(other.errorString))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (rule == null) {
            if (other.rule != null)
                return false;
        } else if (!rule.equals(other.rule))
            return false;
        if (sentence == null) {
            if (other.sentence != null)
                return false;
        } else if (!sentence.equals(other.sentence))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TopLevelGdl [sentence=" + sentence + ", rule=" + rule
                + ", errorString=" + errorString + ", position=" + position
                + "]";
    }

    @Override
    public void accept(GdlVisitor visitor) {
        if (sentence != null) {
            sentence.accept(visitor);
        } else if (rule != null) {
            rule.accept(visitor);
        } else {
            visitor.visitErrorString(errorString, position);
        }
    }

}
