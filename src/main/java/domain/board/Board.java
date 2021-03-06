package domain.board;

import java.util.List;

import domain.piece.Piece;
import domain.piece.position.InvalidPositionException;
import domain.piece.position.Position;
import domain.piece.team.Team;

public class Board {
	private static final int ROW_INDEX = 1;
	private static final int COLUMN_INDEX = 0;

	private List<Rank> ranks;

	public Board(List<Rank> ranks) {
		this.ranks = ranks;
	}

	public List<Rank> getRanks() {
		return ranks;
	}

	public void move(String sourcePosition, String inputTargetPosition, Team turn) {
		int rankLine = Integer.parseInt(String.valueOf(sourcePosition.charAt(ROW_INDEX)));
		Rank rank = ranks.get(rankLine-1);
 		Piece piece = findPiece(sourcePosition, rank);
		Position targetPosition = Position.of(inputTargetPosition);
		if (piece.canMove(targetPosition, turn, ranks)) {
			piece.move(targetPosition, ranks);
		}
	}

	public Piece findPiece(String sourcePosition, Rank rank) {
		return rank.getPieces().stream()
			.filter(piece -> piece.getPosition().isSamePosition(Position.of(sourcePosition)))
			.findFirst()
			.orElseThrow(() -> new InvalidPositionException(InvalidPositionException.INVALID_SOURCE_POSITION));
	}
}
