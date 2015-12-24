package net.alloyggp.griddle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import net.alloyggp.griddle.generated.ParserHelper;
import net.alloyggp.griddle.grammar.TopLevelGdl;

public class TestGames {
    private TestGames() {
        //Not instantiable
    }

    public static String getGameString(String gameName) throws IOException {
        BufferedReader in = new BufferedReader(getGameReader(gameName));
        try {
            StringBuilder sb = new StringBuilder();
            while (true) {
                int c = in.read();
                if (c == -1) {
                    break;
                }
                sb.append((char) c);
            }
            return sb.toString();
        } finally {
            in.close();
        }
    }

    private static Reader getGameReader(String gameName) throws FileNotFoundException {
        File gameFile = new File("testgames", gameName + ".kif");
        return new FileReader(gameFile);
    }

    public static List<TopLevelGdl> getParsedGame(String gameName) throws FileNotFoundException, Exception {
        return ParserHelper.parse(getGameReader(gameName));
    }
}
