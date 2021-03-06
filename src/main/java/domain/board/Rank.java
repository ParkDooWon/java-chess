package domain.board;

import java.util.List;
import java.util.Objects;

import domain.piece.Piece;

public class Rank {
	private List<Piece> pieces;

	public Rank(List<Piece> pieces) {
		this.pieces = pieces;
	}

	public List<Piece> getPieces() {
		return pieces;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Rank rank = (Rank)o;
		return Objects.equals(pieces, rank.pieces);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pieces);
	}
}
