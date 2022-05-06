# Chess
A chess game that was made using Swing and programmed in just Java. To run it, download the code in some IDE like Eclipse, Replit, or IntelliJ. IF you want to play the game, in the main method in Main.java, make sure 'debugFileLoader' is commented out while uncommenting 'debugGraphics'

___
# Controls:
<img src="/images/Main screen.png"></img>

The parts of this layout are:
* The top bar indicates who's turn it is.
* The bar below indicates how many turns are left. The standard limit is 60.
* The grid indicates how many points each side has. The left is white's points, the right is black's.
* The three boxes in a row allow one to select a move.
  * The left box indicates the position the piece starts at. The right indicates what position the piece will mvoe to. While you can place any piece, only valid moves will allow the game to continue and for your turn to end. For what valid moves are, see the rules below.
* The buttons have a few different features:
  * Reset allows you to reset the board.
  * Display allows you to see what moves a piece can perform, provided the left box is filled. For what that looks like, see below.
  * Confirm actually makes the move, so long as both boxes are performed and the move meets all requirements.
  * The promotion box is filled with the piece a pawn will promote to once reaching the end of the board. The default option is queen. The default option is also chosen if it cannot recognize what the piece the program is saying.
___
# Display
The game has a 'display feature,' which allows you to see how a piece can move, as shown below. To activate the display, select the square the piece is on, then click 'Display Move'.

<img src="/images/En passant display.png"></img>

<img src="/images/Castle.png"></img>

<img src="/images/Display feature.png"></img>

* Green Squares indicate you can move to that position.
* Red Squares indicate the opposing piece on that space can be captured.
* Blue squares indicate that you can perform a castle (when initating a castle, you must select the king piece first, then the one where the king would go to upon performing a successful castle)
* Purple squares indicate you can perform en-passant.

You can only perform moves that are considered 'valid,' meaning you could realisticly do them without placing your king in check or checkmate. If you do not see a move you'd normally expect to see, it is either because:
* You are in check
* Moving the piece from its position would place you in check.
___
# Rules 
The rules are the same as chess, with two exceptions:
* The winner of stalemate is determined by whoever has the most points, awarded upon capturing another piece.
  * Pawns are worth *one* point
  * Rooks and Knights are worth *three* points
  *  Bishops are worth *five* points
  * Queens are worth *nine* points

* A pawn can perform en passant once the conditions are met. However en passant can still be done long after the opposing pawn has already done its double move forward.

*The main rules of chess are:*
* Turns alternate between white and black. White starts first.
* A turn only ends once a piece moves. This piece cannot belong to the opposing side and opposing pieces can never be moved. `
* When in check, the respective king must get out of check by blocking the opposing piece's path if possible, attacking the opposing piece, or moving out of the way. You cannot escape check by going into a place where you would also be placed in check.
  * Check is defined as when a piece could cature the king on the piece's next turn.
  * If none of these options are available, the king is placed in checkmate. They then lose, causing the other side to win, no matter how many points they may have.
* No pieces can go off the 8 by 8 board.
* Rows are called 'ranks' and are labeled 1-8
* Columns are called 'files' and are labeled a-h
* When capturing, pieces can only capture those of opposite color.
* Captured pieces are removed from the board and can no longer make any moves.
* The initial board layout is as follows:
<img src="/images/Main screen.png"></img>
  
* Pawns are allowed to move forward one space. If it is their first time moving, then they can move two spaces forward instead of one.
  * Pawns have to move towards their end rank when moving. For white, it is 8. For black, it is 1.
  * Once they reach the end rank, they can promote to any other piece, even if it is already present on the board.
  * If there is a piece of opposing color one space away and on the diagonal of said pawn, they can capture the piece. They do so by moving to the space, replacing it with their own. A capture is only allowed if the pawn also moves towards its end rank.
  * If they are on the fifth or fourth rank for white and black respectively, and if a pawn of opposing color moves forward two spaces, then the pawn can perform a move called 'en passant'. In this move, the pawn moves diagonally one space towards the file the opposing pawn is on and one space forward. THe opposing pawn is captured.

* Rooks are able to move in the cardinal directions, like those indicated by a plus sign (+).
  * They are able to move to a space as well as capture it.
  * If they have not moved and the king has not moved, they can castle. Castling rules are described in the king's section.

* Knights move in an L shape -- Either two moves horizontally and one move vertically OR one move horizontally and two moves vertically.
  * When it moves, it ignores any piece it its way, whether they are friendly or an enemy.
  * They can move to a space just as they can capture it.

* Bishops can move diagonally, similar to how an 'x' looks. (x)
   * As a result of their movement, they can only move to squares that are the same color as them.
   * They can move to a space just as they can capture it.

* Queens move in the same directions that rooks and bishops do. However, they can only move in one direction at a time.
  * This means you can't chain moves together by moving diagonally, then moving cardinally. 
  * Queens also cannot castle with the king.

* The king's basic move option is one space in any direction.
  * The king cannot delibrately move themselves into check or checkmate.
  * The king can perform a castle to either file c or file g. In these conditions, the rook they castled with will move to file d and file f respectively. A castle can be performed with a rook under the following conditions:
    * All spaces between them and the rook are empty.
    * The king has not moved.
    * The rook has not moved.
    * The king is currently not in check.
    * If any of the spaces are being attacked by one of the oppposing color.

# Current bugs:
* Cannot check for stalemate quite yet.
* Currently working on a second mode where you can open a game. Please consider this feature unusable for now.
