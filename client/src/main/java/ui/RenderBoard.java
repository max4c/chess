package ui;

public class RenderBoard {
    private String blackBoard;
    private String whiteBoard;

    public RenderBoard() {
        this.whiteBoard = "";

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 0 || i == 9 || j == 0 || j == 9) {
                    whiteBoard += EscapeSequences.SET_BG_COLOR_DARK_GREY;
                } else {
                    if ((i + j) % 2 == 0) {
                        whiteBoard += EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
                    } else {
                        whiteBoard += EscapeSequences.SET_BG_COLOR_DARK_GREY;
                    }
                }
                if (i == 0 || i == 9) {
                    switch (j) {
                        case 1: whiteBoard += "a" + EscapeSequences.EMPTY; break;
                        case 2: whiteBoard += "b" + EscapeSequences.EMPTY; break;
                        case 3: whiteBoard += "c" + EscapeSequences.EMPTY; break;
                        case 4: whiteBoard += "d" + EscapeSequences.EMPTY; break;
                        case 5: whiteBoard += "e" + EscapeSequences.EMPTY; break;
                        case 6: whiteBoard += "f" + EscapeSequences.EMPTY; break;
                        case 7: whiteBoard += "g" + EscapeSequences.EMPTY; break;
                        case 8: whiteBoard += "h" + EscapeSequences.EMPTY;; break;
                        default: whiteBoard += " ";
                    }
                } else if (j == 0 || j == 9) {
                    whiteBoard += Integer.toString(i);
                } else {
                    if(i == 1) {
                        switch (j) {
                            case 1, 8:
                                whiteBoard += "R" + EscapeSequences.EMPTY;
                                break;
                            case 2, 7:
                                whiteBoard += "N" + EscapeSequences.EMPTY;
                                break;
                            case 3, 6:
                                whiteBoard += "B" + EscapeSequences.EMPTY;
                                break;
                            case 4:
                                whiteBoard += "Q" + EscapeSequences.EMPTY;
                                break;
                            case 5:
                                whiteBoard += "K" + EscapeSequences.EMPTY;
                                break;
                        }
                    }
                    else if(i == 8) {
                        switch (j) {
                            case 1, 8:
                                whiteBoard += "r" + EscapeSequences.EMPTY;
                                break;
                            case 2, 7:
                                whiteBoard += "n" + EscapeSequences.EMPTY;
                                break;
                            case 3, 6:
                                whiteBoard += "b" + EscapeSequences.EMPTY;
                                break;
                            case 4:
                                whiteBoard += "q" + EscapeSequences.EMPTY;
                                break;
                            case 5:
                                whiteBoard += "k" + EscapeSequences.EMPTY;
                                break;
                        }
                    }
                    else if (i == 2 || i == 7){
                        if (i == 7){
                            whiteBoard += "p" + EscapeSequences.EMPTY;
                        }
                        else{
                            whiteBoard += "P" + EscapeSequences.EMPTY;
                        }
                    }
                    else{
                        whiteBoard += " " + EscapeSequences.EMPTY;
                    }
                }

                whiteBoard += EscapeSequences.RESET_BG_COLOR;
            }
            whiteBoard += "\n"; // New line at the end of each row
        }

        this.blackBoard = "";

        for (int i = 9; i >= 0; i--) {
            for (int j = 9; j >= 0; j--) {
                if (i == 0 || i == 9 || j == 0 || j == 9) {
                    blackBoard += EscapeSequences.SET_BG_COLOR_DARK_GREY;
                } else {
                    if ((i + j) % 2 == 0) {
                        blackBoard += EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
                    } else {
                        blackBoard += EscapeSequences.SET_BG_COLOR_DARK_GREY;
                    }
                }
                if (i == 0 || i == 9) {
                    switch (j) {
                        case 8: blackBoard += "h" + EscapeSequences.EMPTY; break;
                        case 7: blackBoard += "g" + EscapeSequences.EMPTY; break;
                        case 6: blackBoard += "f" + EscapeSequences.EMPTY; break;
                        case 5: blackBoard += "e" + EscapeSequences.EMPTY; break;
                        case 4: blackBoard += "d" + EscapeSequences.EMPTY; break;
                        case 3: blackBoard += "c" + EscapeSequences.EMPTY; break;
                        case 2: blackBoard += "b" + EscapeSequences.EMPTY; break;
                        case 1: blackBoard += "a" + EscapeSequences.EMPTY; break;
                        default: blackBoard += " ";
                    }
                } else if (j == 0 || j == 9) {
                    blackBoard += Integer.toString(i);
                } else {
                    if(i == 1) {
                        switch (j) {
                            case 8, 1:
                                blackBoard += "R" + EscapeSequences.EMPTY;
                                break;
                            case 7, 2:
                                blackBoard += "N" + EscapeSequences.EMPTY;
                                break;
                            case 6, 3:
                                blackBoard += "B" + EscapeSequences.EMPTY;
                                break;
                            case 5:
                                blackBoard += "Q" + EscapeSequences.EMPTY;
                                break;
                            case 4:
                                blackBoard += "K" + EscapeSequences.EMPTY;
                                break;
                        }
                    } else if(i == 8) {
                        switch (j) {
                            case 8, 1:
                                blackBoard += "r" + EscapeSequences.EMPTY;
                                break;
                            case 7, 2:
                                blackBoard += "n" + EscapeSequences.EMPTY;
                                break;
                            case 6, 3:
                                blackBoard += "b" + EscapeSequences.EMPTY;
                                break;
                            case 5:
                                blackBoard += "q" + EscapeSequences.EMPTY;
                                break;
                            case 4:
                                blackBoard += "k" + EscapeSequences.EMPTY;
                                break;
                        }
                    } else if (i == 2 || i == 7){
                        if (i == 2){
                            blackBoard += "P" + EscapeSequences.EMPTY;
                        } else {
                            blackBoard += "p" + EscapeSequences.EMPTY;
                        }
                    } else {
                        blackBoard += " " + EscapeSequences.EMPTY;
                    }
                }

                blackBoard += EscapeSequences.RESET_BG_COLOR;
            }

            blackBoard += "\n"; // New line at the end of each row
        }

    }

    public String getBlackBoard() {
        return blackBoard;
    }

    public String getWhiteBoard() {
        return whiteBoard;
    }
}
