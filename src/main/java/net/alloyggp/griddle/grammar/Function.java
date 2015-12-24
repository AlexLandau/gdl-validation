package net.alloyggp.griddle.grammar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java_cup.runtime.ComplexSymbolFactory.Location;
import net.alloyggp.griddle.Position;

public class Function implements GdlVisitable {
    private final String name;
    private final List<Term> body;

    private final Position namePosition;
    private final Position position;

    private Function(String name, int nameLeft, int nameRight, int nameLine, List<Term> body, int left, int right, int line) {
        if (name == null || body == null) {
            throw new NullPointerException();
        }
        this.name = name;
        this.body = body;

        this.namePosition = new Position(nameLeft, nameRight, nameLine);
        this.position = new Position(left, right, line);
    }

    public static Function create(String name, Location nameLeft, Location nameRight,
            List<Term> body, Location left, Location right) {
        return new Function(name, nameLeft.getOffset(), nameRight.getOffset(), nameLeft.getLine(),
                Collections.unmodifiableList(new ArrayList<Term>(body)),
                left.getOffset(), right.getOffset(), left.getLine());
    }

    public String getName() {
        return name;
    }

    public List<Term> getBody() {
        return body;
    }

    public Position getNamePosition() {
        return namePosition;
    }

    public Position getPosition() {
        return position;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((body == null) ? 0 : body.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((position == null) ? 0 : position.hashCode());
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
        Function other = (Function) obj;
        if (body == null) {
            if (other.body != null)
                return false;
        } else if (!body.equals(other.body))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (position == null) {
            if (other.position != null)
                return false;
        } else if (!position.equals(other.position))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Function [name=" + name + ", body=" + body + ", position="
                + position + "]";
    }

    @Override
    public void accept(GdlVisitor visitor) {
        visitor.visitFunction(this);
        visitor.visitConstant(name, namePosition);
        for (Term term : body) {
            term.accept(visitor);
        }
    }

    public String getUserFriendlyString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(")
        .append(name);
        for (Term term : body) {
            sb.append(" ")
            .append(term.getUserFriendlyString());
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean equalsIgnorePosition(Function other) {
        if (other == null) {
            return false;
        }
        if (!name.equals(other.name)) {
            return false;
        }
        if (body.size() != other.body.size()) {
            return false;
        }
        for (int i = 0; i < body.size(); i++) {
            if (!body.get(i).equalsIgnorePosition(other.body.get(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isGround() {
        for (Term term : body) {
            if (!term.isGround()) {
                return false;
            }
        }
        return true;
    }
}
