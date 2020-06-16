package com.mccspace.hs.game;

import java.util.ArrayList;
import java.util.List;

/**
 * CheckerBoardÀà
 * Git to£º http://hs.mccspace.com:3000/Qing_ning/CheckerServer5.0/
 *
 * @TIME 2020/6/7 13:29
 * @AUTHOR º«Ë¶~
 */

public record CheckerBoard(long notEmote, long isBlack, long isKing) {

    public ChessType getChess(int n) {
        if (n == -1)
            return ChessType.Bar;
        if ((notEmote >> n) % 2 != 0) {
            if ((isBlack >> n) % 2 == 0) {
                if ((isKing >> n) % 2 == 0)
                    return ChessType.White;
                else
                    return ChessType.WhiteKing;
            } else {
                if ((isKing >> n) % 2 == 0)
                    return ChessType.Black;
                else
                    return ChessType.BlackKing;
            }
        } else {
            if ((isBlack >> n) % 2 == 0) {
                return ChessType.Emote;
            } else {
                return ChessType.Bar;
            }
        }
    }

    @Override
    public CheckerBoard clone() {
        return new CheckerBoard(notEmote, isBlack, isKing);
    }
}
