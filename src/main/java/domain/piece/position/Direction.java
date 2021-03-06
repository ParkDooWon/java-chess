package domain.piece.position;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import domain.board.Rank;

public enum Direction {
	N(1, 0, (rowGap, columnGap) -> rowGap > 0 && columnGap == 0),
	S(-1, 0, (rowGap, columnGap) -> rowGap < 0 && columnGap == 0),
	E(0, 1, (rowGap, columnGap) -> rowGap == 0 && columnGap > 0),
	W(0, -1, (rowGap, columnGap) -> rowGap == 0 && columnGap < 0),

	NE(1, 1, (rowGap, columnGap) -> isSameAbs(rowGap, columnGap) && rowGap > 0 && columnGap > 0),
	NW(1, -1, (rowGap, columnGap) -> isSameAbs(rowGap, columnGap) && rowGap > 0 && columnGap < 0),
	SE(-1, 1, (rowGap, columnGap) -> isSameAbs(rowGap, columnGap) && rowGap < 0 && columnGap > 0),
	SW(-1, -1, (rowGap, columnGap) -> isSameAbs(rowGap, columnGap) && rowGap < 0 && columnGap < 0),

	NNE(2, 1, (rowGap, columnGap) -> rowGap == 2 && columnGap == 1),
	NNW(2, -1, (rowGap, columnGap) -> rowGap == 2 && columnGap == -1),
	SSE(-2, 1, (rowGap, columnGap) -> rowGap == -2 && columnGap == 1),
	SSW(-2, -1, (rowGap, columnGap) -> rowGap == -2 && columnGap == -1),
	NEE(1, 2, (rowGap, columnGap) -> rowGap == 1 && columnGap == 2),
	NWW(1, -2, (rowGap, columnGap) -> rowGap == 1 && columnGap == -2),
	SEE(-1, 2, (rowGap, columnGap) -> rowGap == -1 && columnGap == 2),
	SWW(-1, -2, (rowGap, columnGap) -> rowGap == -1 && columnGap == -2);

	private int rowGap;

	private int columnGap;
	private BiFunction<Integer, Integer, Boolean> find;

	Direction(int rowGap, int columnGap, BiFunction<Integer, Integer, Boolean> find) {
		this.rowGap = rowGap;
		this.columnGap = columnGap;
		this.find = find;
	}

	private static boolean isSameAbs(int rowGap, int columnGap) {
		return Math.abs(rowGap) == Math.abs(columnGap);
	}

	public static List<Direction> everyDirection() {
		return Arrays.asList(N, S, E, W, NE, NW, SE, SW);
	}

	public static List<Direction> linearDirection() {
		return Arrays.asList(N, S, E, W);
	}

	public static List<Direction> diagonalDirection() {
		return Arrays.asList(NE, NW, SE, SW);
	}

	public static List<Direction> knightDirection() {
		return Arrays.asList(NNE, NNW, SSE, SSW, NEE, NWW, SEE, SWW);
	}

	public static List<Direction> whitePawnDirection() {
		return Arrays.asList(N, NE, NW);
	}

	public static List<Direction> blackPawnDirection() {
		return Arrays.asList(S, SE, SW);
	}

	public int getRowGap() {
		return rowGap;
	}

	public int getColumnGap() {
		return columnGap;
	}

	public BiFunction<Integer, Integer, Boolean> getFind() {
		return find;
	}

	public boolean hasPieceInRoute(Position position, Position targetPosition, List<Rank> ranks) {
		int loopCount = calculateLoopCount(position, targetPosition) - 1;
		int routeRow = position.getRow();
		int routeColumn = position.getColumn().getNumber();
		for (int i = 0; i < loopCount; i++) {
			routeRow += this.getRowGap();
			routeColumn += this.getColumnGap();
			if (hasPieceInBoard(ranks, routeRow, routeColumn)) {
				return true;
			}
		}
		return false;
	}

	private int calculateLoopCount(Position position, Position targetPosition) {
		int columnGap = Math.abs(position.calculateColumnGap(targetPosition));
		int rowGap = Math.abs(position.calculateRowGap(targetPosition));
		return Math.max(columnGap, rowGap);
	}

	private boolean hasPieceInBoard(List<Rank> ranks, int routeRow, int routeColumn) {
		return ranks.stream()
			.flatMap(rank -> rank.getPieces().stream())
			.anyMatch(piece -> piece.getPosition().getColumn().getNumber() == routeColumn
				&& piece.getPosition().getRow() == routeRow);
	}
}
