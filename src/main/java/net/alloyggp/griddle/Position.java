package net.alloyggp.griddle;

public class Position {
    private final int charStart;
    private final int charEnd;
    private final int lineNumber;

    public Position(int charStart, int charEnd, int lineNumber) {
        this.charStart = charStart;
        this.charEnd = charEnd;
        this.lineNumber = lineNumber;
    }

    public int getStart() {
        return charStart;
    }

    public int getEnd() {
        return charEnd;
    }

    /**
     * These line numbers are 0-indexed.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + charEnd;
        result = prime * result + charStart;
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
        Position other = (Position) obj;
        if (charEnd != other.charEnd)
            return false;
        if (charStart != other.charStart)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Position [charStart=" + charStart + ", charEnd=" + charEnd
                + ", lineNumber=" + lineNumber + "]";
    }
}
