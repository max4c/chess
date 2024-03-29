package ui;

public class RenderBoard {
    private String blackBoard;
    private String whiteBoard;
    public RenderBoard() {
       this.whiteBoard ="""
                   a b c d e f g h
                8 |r|n|b|q|k|b|n|r| 8
                7 |p|p|p|p|p|p|p|p| 7
                6 | | | | | | | | | 6
                5 | | | | | | | | | 5
                4 | | | | | | | | | 4
                3 | | | | | | | | | 3
                2 |P|P|P|P|P|P|P|P| 2
                1 |R|N|B|Q|K|B|N|R| 1
                   a b c d e f g h
                """;
       this.blackBoard = """
                   h g f e d c b a
                1 |R|N|B|Q|K|B|N|R| 1
                2 |P|P|P|P|P|P|P|P| 2
                3 | | | | | | | | | 3
                4 | | | | | | | | | 4
                5 | | | | | | | | | 5
                6 | | | | | | | | | 6
                7 |p|p|p|p|p|p|p|p| 7
                8 |r|n|b|q|k|b|n|r| 8
                   h g f e d c b a
                """;
    }

    public String getBlackBoard() {
        return blackBoard;
    }

    public String getWhiteBoard() {
        return whiteBoard;
    }
}
