// Author: Eunsub Lee

public class minimax extends AIModule {
    private int player, opponent, maxDepth = 5;
    private int[][] positionScores = {  { 3, 4, 5,  7,  5,  4, 3 }, 
                                        { 4, 6, 8,  10, 8,  6, 4 }, 
                                        { 5, 8, 11, 13, 11, 8, 5 },
                                        { 5, 8, 11, 13, 11, 8, 5 }, 
                                        { 4, 6, 8,  10, 8,  6, 4 }, 
                                        { 3, 4, 5,  7,  5,  4, 3 } };

    public void getNextMove(final GameStateModule game) {
        player = game.getActivePlayer();
        opponent = (player == 1 ? 2 : 1);

        // First play a winning move, if any
        for (int i = 0; i < game.getWidth(); i++) {
            if (game.canMakeMove(i)) {
                game.makeMove(i);

                if (game.isGameOver()) {
                    if (game.getWinner() == player) {
                        chosenMove = i;
                        return;
                    }
                }

                game.unMakeMove();
            }
        }

        minimax_helper(game, 0, true);
    }

    private int minimax_helper(final GameStateModule state, int depth, boolean isMaximizing) {
        if (depth == maxDepth || state.isGameOver()) {
            return eval(state);
        }

        int bestVal = Integer.MIN_VALUE;
        if (isMaximizing) { // Your turn
            int value = Integer.MIN_VALUE + 1;

            for (int i = 0; i < state.getWidth(); i++) {
                if (state.canMakeMove(i)) {
                    state.makeMove(i);
                    value = Math.max(value, minimax_helper(state, depth + 1, false));
                    state.unMakeMove();

                    if (value > bestVal) {
                        bestVal = value;
                        if (depth == 0)
                            chosenMove = i;
                    }
                }
            }

            return value;
        } else { // Opponent's turn
            int value = Integer.MAX_VALUE;

            for (int i = 0; i < state.getWidth(); i++) {
                if (state.canMakeMove(i)) {
                    state.makeMove(i);
                    value = Math.min(value, minimax_helper(state, depth + 1, true));
                    state.unMakeMove();
                }
            }

            return value;
        }
    }

    private int eval(final GameStateModule state) {
        if (state.isGameOver()) {
            int winner = state.getWinner();

            if (winner == player) {
                return Integer.MAX_VALUE;
            } else {
                return Integer.MIN_VALUE;
            }
        }

        int score = 0;

        for (int i = 0; i < state.getWidth(); i++) {
            for (int j = 0; j < state.getHeight(); j++) {
                int id = state.getAt(i, j);
                
                if (id == player) {
                    score += positionScores[state.getHeight() - 1 - j][i];
                } else if (id == opponent) {
                    score -= positionScores[state.getHeight() - 1 - j][i];
                }
            }
        }

        return score;
    }
}