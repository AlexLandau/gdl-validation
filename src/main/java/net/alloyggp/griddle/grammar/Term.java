package net.alloyggp.griddle.grammar;

import java_cup.runtime.ComplexSymbolFactory.Location;
import net.alloyggp.griddle.Position;

public class Term implements GdlVisitable {
    //Exactly one of these is non-null.
    private final String constantName;
    private final String variableName;
    private final Function function;

    private final Position position;

    private Term(String constantName, String variableName, Function function,
            int left, int right, int line) {
        this.constantName = constantName;
        this.variableName = variableName;
        this.function = function;
        this.position = new Position(left, right, line);
    }

    public static Term createFunction(Function f, Location left, Location right) {
        if (f == null) {
            throw new NullPointerException();
        }
        return new Term(null, null, f, left.getOffset(), right.getOffset(), left.getLine());
    }

    public static Term createVariable(String v, Location left, Location right) {
        if (v == null) {
            throw new NullPointerException();
        }
        return new Term(null, v, null, left.getOffset(), right.getOffset(), left.getLine());
    }

    public static Term createConstant(String c, Location left, Location right) {
        if (c == null) {
            throw new NullPointerException();
        }
        return new Term(c, null, null, left.getOffset(), right.getOffset(), left.getLine());
    }

    public boolean isConstant() {
        return constantName != null;
    }

    public boolean isVariable() {
        return variableName != null;
    }

    public boolean isFunction() {
        return function != null;
    }

    public String getConstantName() {
        return constantName;
    }

    public String getVariableName() {
        return variableName;
    }

    public Function getFunction() {
        return function;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((constantName == null) ? 0 : constantName.hashCode());
        result = prime * result
                + ((function == null) ? 0 : function.hashCode());
        result = prime * result
                + ((position == null) ? 0 : position.hashCode());
        result = prime * result
                + ((variableName == null) ? 0 : variableName.hashCode());
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
        Term other = (Term) obj;
        if (constantName == null) {
            if (other.constantName != null)
                return false;
        } else if (!constantName.equals(other.constantName))
            return false;
        if (function == null) {
            if (other.function != null)
                return false;
        } else if (!function.equals(other.function))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        if (variableName == null) {
            if (other.variableName != null)
                return false;
        } else if (!variableName.equals(other.variableName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Term [constantName=" + constantName + ", variableName="
                + variableName + ", function=" + function + ", position="
                + position + "]";
    }

    @Override
    public void accept(GdlVisitor visitor) {
        if (constantName != null) {
            visitor.visitConstant(constantName, position);
        } else if (variableName != null) {
            visitor.visitVariable(variableName, position);
        } else {
            function.accept(visitor);
        }
    }

    //TODO: Consider making this the toString() implementation
    public String getUserFriendlyString() {
        if (isConstant()) {
            return constantName;
        } else if (isVariable()) {
            return variableName;
        } else {
            return function.getUserFriendlyString();
        }
    }

    public boolean equalsIgnorePosition(Term other) {
        if (other == null) {
            return false;
        }
        if (isConstant()) {
            return other.isConstant() && constantName.equals(other.constantName);
        } else if (isVariable()) {
            return other.isVariable() && variableName.equals(other.variableName);
        } else {
            return other.isFunction() && function.equalsIgnorePosition(other.function);
        }
    }

    public boolean isGround() {
        if (isConstant()) {
            return true;
        } else if (isVariable()) {
            return false;
        } else {
            return function.isGround();
        }
    }

}
